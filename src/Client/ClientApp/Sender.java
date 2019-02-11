package Client.ClientApp;

import java.io.DataOutputStream;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

public class Sender implements Runnable{

    BlockingDeque<String> OUTBOX;
    Client client;
    DataOutputStream streamOut;

    public Sender(DataOutputStream streamOut, Client client){
        OUTBOX = new LinkedBlockingDeque<>();
        this.streamOut = streamOut;
        this.client = client;
    }

    public BlockingDeque<String> getOUTBOX() {
        return OUTBOX;
    }

    @Override
    public void run() {
        while(client.isRunning()){
            if(OUTBOX.size() > 0){
                String dataOut = OUTBOX.getFirst();
                try{
                    streamOut.writeUTF(dataOut);
                    OUTBOX.removeFirst();
                    Thread.sleep(100);
                } catch (Exception e){
                    System.out.println(dataOut + " was never sent to the server");
                }
            }
        }
        try{
            streamOut.close();
        } catch (Exception e){ }
    }
}
