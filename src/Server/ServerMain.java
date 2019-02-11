package Server;

import Server.ServerApp.Server;

public class ServerMain {
    public static void main(String[] args){
        new Thread(new Server()).start();
    }
}
