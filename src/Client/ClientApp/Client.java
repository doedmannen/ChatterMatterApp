package Client.ClientApp;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.BlockingDeque;

public class Client implements Runnable{
    private Scanner scan;
    private boolean running;
    private Socket socket;
    private final int serverPORT;
    private final String serverADRESS;
    private Reciever reciever;
    private Sender sender;
    private String nickname;
    private final int CONNECTION_WAIT;
    private final int SLEEP_MS;

    public Client(){
        serverPORT = 54321;
        serverADRESS = "localhost"; // Change to servers hostname
        scan  = new Scanner(System.in);
        nickname = "ChatterMatterClient";
        running = true;
        CONNECTION_WAIT = 1000;
        SLEEP_MS = 100;

        for(int i = 0; i < 10; i++){
            try{
                socket = new Socket("localhost", serverPORT);
                socket.setSoTimeout(SLEEP_MS);
                reciever = new Reciever(new DataInputStream(socket.getInputStream()), this);
                sender = new Sender(new DataOutputStream(socket.getOutputStream()), this);
            }catch (Exception e){
                if(i == 9){
                    System.out.println("Terminating client. Server is not responding. ");
                    kill();
                } else{
                    System.out.println("Connection failed. Retrying " + (i+1));
                }
            }

            if(socket != null && reciever != null && sender != null){
                break;
            }

            try{
                Thread.sleep(CONNECTION_WAIT);
            } catch(Exception e){ }
        }
        new Thread(reciever).start();
        new Thread(sender).start();
    }

    protected BlockingDeque<String> getOutbox() {
        return sender.getOUTBOX();
    }

    public String getNickname() {
        return nickname;
    }

    public boolean isRunning() {
        return running;
    }

    public void kill(){
        sender.getOUTBOX().add("d:");
        running = false;
        try{
            socket.close();
        } catch (Exception e){}
    }

    public void actionOnCommand(String input){
        String command = input.trim().toLowerCase();
        if(command.startsWith("send")){
            sender.getOUTBOX().add("m:"+input.substring(4));
        } else if(command.startsWith("nick")){
            sender.getOUTBOX().add("n:"+input.substring(4));
        }  else if(command.startsWith("kill")){
            kill();
        }
    }

    @Override
    public void run() {
        while(running){
            actionOnCommand(scan.nextLine());
        }
    }
}
