package Server.ServerApp;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

public class Server implements Runnable{

    private final int PORT;
    private ServerSocket serverSocket;
    private boolean running;
    private ExecutorService executorService;
    private ConcurrentHashMap<String, Client> ACTIVE_CLIENTS;
    private ServerScanner serverScanner;
    private LinkedBlockingDeque<String> SERVER_LOG;
    protected ServerMessageSorter serverMessageSorter;

    public Server(){
        PORT = 54321;   // PORT for server. This is what clients use to connect
        running = true; // For termination later
        executorService = Executors.newCachedThreadPool();  // ThreadPool that grows/shrinks based on the demand
        ACTIVE_CLIENTS = new ConcurrentHashMap<>();  // Hash for storing sockets to clients
        serverScanner = new ServerScanner(this); // Scanner for admin input
        new Thread(serverScanner).start();      // start scanner thread
        SERVER_LOG = new LinkedBlockingDeque<>(); // For logging errors and connections
        serverMessageSorter = new ServerMessageSorter(this);

        try{
            serverSocket = new ServerSocket(PORT);
        }catch (Exception e){
            e.printStackTrace();
            kill();
        }
    }

    protected void addToLog(String log_msg){
        String date = "DATE";
        if(SERVER_LOG.size() > 100)
            SERVER_LOG.removeFirst();
        SERVER_LOG.add(date.concat(" ::: " + log_msg));
    }

    protected void printServerLog(){
        System.out.println(String.join("", SERVER_LOG
                .stream()
                .map(s -> s.concat("\n")).toArray(String[]::new)));
    }

    protected boolean isRunning() {
        return running;
    }

    protected Client getClient(String name){
        if(ACTIVE_CLIENTS.containsKey(name))
            return ACTIVE_CLIENTS.get(name);
        return null;
    }

    protected boolean connectClient(Client client){
        if(ACTIVE_CLIENTS.containsKey(client.getUserName()))
            return false;
        ACTIVE_CLIENTS.put(client.getUserName(), client);
        return true;
    }

    protected void disconnectClient(Client client){
        ACTIVE_CLIENTS.remove(client.getUserName());
    }

    protected void kill(){
        try{
            serverSocket.close();
        }catch (Exception e){}
        running = false;
    }

    @Override
    public void run() {
        System.out.println("Server is running on port: " + PORT);
        while(running){

            try{
                // Waiting for client to connect
                Socket s = serverSocket.accept();

                // Sending client to be handled in a thread
                executorService.submit(new ClientHandler(s, this));

            }catch (Exception e) {
                addToLog("ServerSocket failed");
                // What to do?! Nothing probably... maybe... who knows... Pancakes!
            }

        }
        try{
            serverSocket.close();
        }catch (Exception e){}
        kill(); // safety call, to kill all socket threads in case something is wrong
        // This needs work? All executorService threads should stop when kill has been called since server is !running
        executorService.shutdown();
        System.out.println("Server has stopped.\nActive socket connections might still be running. ");
    }
}
