package com.blockchain.network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

import com.blockchain.structures.objects.Block;
import com.blockchain.structures.objects.Transaction;


public class Server {
    private ServerSocket serverSocket;
    private int port;
    private Socket socket;
    private ObjectInputStream objectInputStream;

    public Server(int port){
        this.port = port;
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public Object listen(){
        try {
            //listens for client
            socket = serverSocket.accept();
            System.out.println("SERVER " + port + " LISTENING");
            //Reads client output
            objectInputStream = new ObjectInputStream(socket.getInputStream());
            Object object = objectInputStream.readObject();
            System.out.println("SERVER READ " + object);

            socket.close();
            
            // objectInputStream.close();
            // socket.close();
            // serverSocket.close();
            //Validates client output type
            // if(!(object instanceof Block) || !(object instanceof Transaction)){
            //     throw new ClassNotFoundException("SERVER INPUT OBJECT NEITHER OF TYPE: BLOCK or TRANSACTION");
            // }

            return object;

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return new Object();
    }

    public void close(){
        try {
            serverSocket.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public boolean isRunning(){ return !serverSocket.isClosed(); }
}
