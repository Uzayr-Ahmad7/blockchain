package com.blockchain.network;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import com.blockchain.structures.objects.Block;
import com.blockchain.structures.objects.Transaction;

public class Client {

    private int port;

    public Client(){}

    public void run(Object object, int port){
        try {

            //Validates client output type
            // if(!(object instanceof Block) || !(object instanceof Transaction)){
            //     throw new ClassNotFoundException("CLIENT OUTPUT OBJECT NEITHER OF TYPE: BLOCK or TRANSACTION");
            // }

            Socket socket = new Socket("localhost", port);
            broadcast(object, socket);
            System.out.println("Client sent " + object + " to " + port);
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void broadcast(Object object, Socket socket) {

        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectOutputStream.writeObject(object);
            objectOutputStream.flush();
            objectOutputStream.close();
            socket.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
