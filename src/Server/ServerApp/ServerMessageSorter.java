package Server.ServerApp;

import Common.Message;

import java.util.concurrent.LinkedBlockingDeque;

public class ServerMessageSorter implements Runnable{
    private LinkedBlockingDeque<Message> MESSAGES;
    private Server server;

    public ServerMessageSorter(Server server){
        this.server = server;
        MESSAGES = new LinkedBlockingDeque<>();
    }

    protected void addToOutbox(Message message){
        MESSAGES.add(message);
    }

    private void sendToChannels(Message message){

    }

    private void sendToUser(Message message){

    }

    private void commandSwitch(Message message){
        switch (message.type){
            case 'm':
                sendToChannels(message);
                break;
            case 'w':
                sendToUser(message);
                break;
            default:
                break;
        }
    }

    @Override
    public void run() {
        while(server.isRunning()){
            if(MESSAGES.size() > 0){
                commandSwitch(MESSAGES.removeFirst());
            }
        }
    }
}
