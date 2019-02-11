package Server.ServerApp;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class ClientHandler implements Runnable {
    private Socket socket;
    private Server server;
    private DataInputStream streamIn;
    private DataOutputStream streamOut;
    private final int SLEEP_MS = 500;
    protected Client client;
    private boolean running = true;

    public ClientHandler(Socket socket, Server server){
        this.socket = socket;
        this.server = server;
        try{
            socket.setSoTimeout(SLEEP_MS);
            streamIn = new DataInputStream(socket.getInputStream());
            streamOut = new DataOutputStream(socket.getOutputStream());
        } catch (Exception e){
            kill();
        }
    }

    protected boolean isRunning(){
        return running;
    }

    protected void kill(){
        System.out.println("Failed connection with client " + socket.getInetAddress());
        running = false;
    }

    private void requestClientNickname(){
        while (client == null && running && server.isRunning()){
            String nickname = "";
            try{
                streamOut.writeUTF("n?");
                nickname = streamIn.readUTF();
            }catch(SocketTimeoutException e){ }
            catch (Exception e){
                kill();
            }

            if(NicknameController.checkNick(nickname)){
                server.addToLog(nickname + socket.getInetAddress() + " connected");
                this.client = new Client(nickname);
                break;
            }
        }
    }

    @Override
    public void run() {

        requestClientNickname();

        // Other exit needed?
        while(server.isRunning() && client != null && running){
            if(client.MESSAGES.size() > 0){
                // SKRIV TILL KLIENT
            }
            try{
                String indata = streamIn.readUTF();
                System.out.println(socket.getInetAddress() + ": " + indata);
                streamOut.writeUTF("Message " + indata.substring(2).trim() + " was printed on the server");
            }catch(SocketTimeoutException e){ }
            catch (Exception e){
                kill();
            }
        }
        try{
            streamOut.close();
            streamIn.close();
            socket.close();
        }catch (Exception e){}
    }
}
