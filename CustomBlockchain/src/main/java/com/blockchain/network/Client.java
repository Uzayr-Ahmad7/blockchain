package com.blockchain.network;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {

    private int port;

    public Client(){}

    public void run(Object object, int port){
        /* Sends the input object to the node with corresponding port
         * param: <Object> object: object to be sent
         *        <Integer> port: listening Port the object is to be sent to
         */
        try {

            //Validates client output type
            // if(!(object instanceof Block) || !(object instanceof Transaction)){
            //     throw new ClassNotFoundException("CLIENT OUTPUT OBJECT NEITHER OF TYPE: BLOCK or TRANSACTION");
            // }
            
            //Establishes connection with the listening Port
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
        /* Writes the object into the output stream */

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
