package Client;

import Client.ClientApp.Client;

public class ClientMain {
    public static void main(String[] args){
        new Thread(new Client()).start();
    }
}
