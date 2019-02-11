package Server.ServerApp;

import java.util.Scanner;

public class ServerScanner implements Runnable{

    Scanner scan = new Scanner(System.in);
    Server server;

    public ServerScanner(Server server){
        this.server = server;
    }

    private void actionOnCommand(String input){
        switch (input.trim().toLowerCase()){
            case "help":
                System.out.println("Only command is kill");
                break;
            case "kill":
                server.kill();
                break;
            default:
                System.out.println("Unknown command. Try help");
                break;
        }
    }

    @Override
    public void run() {
        while (server.isRunning()){
            actionOnCommand(scan.nextLine());
        }
    }
}
