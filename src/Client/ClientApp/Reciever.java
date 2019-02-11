package Client.ClientApp;

import java.io.DataInputStream;

public class Reciever implements Runnable{

    Client client;
    DataInputStream streamIn;

    public Reciever(DataInputStream streamIn, Client client){
        this.client = client;
        this.streamIn = streamIn;
    }

    public void commandSwitch(String input){
        if(input != ""){

            System.out.println(input);
            String cmd = input.substring(0,2);
            switch (cmd){
                case "n?":
                    client.getOutbox().add(client.getNickname());
                    break;
                default:
                    break;
            }

        }
    }

    @Override
    public void run() {
        while(client.isRunning()){
            String indata = "";
            try{
                indata = streamIn.readUTF();
            } catch (Exception e){

            }

            commandSwitch(indata);
        }
        System.out.println("End of client listener");
        try{
            streamIn.close();
        }catch (Exception e){}
    }
}
