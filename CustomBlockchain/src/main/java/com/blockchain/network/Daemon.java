package com.blockchain.network;

import java.util.ArrayList;

import com.blockchain.structures.Blockchain;

public class Daemon {
    /* REGISTERS NODES:
     * Listens for port number to be sent
     * Sends port number to be registered across all nodes
     * Requests blockchain json file from a node
     * Sends blockchain json file to new node
     */
    private ArrayList<Integer> nodePorts;
    private Server registerServer;
    private Server nodeServer;
    private Client client;
    private Blockchain blockchain;

    public Daemon(){
        nodePorts = new ArrayList<>();
        registerServer = new Server(9000);
        nodeServer = new Server(6000);
        client = new Client();
        blockchain = new Blockchain();
    }

    public void register(){
        while(registerServer.isRunning()){
            Object object = registerServer.listen();
            System.out.println("SERVER RECEIVED: " + object);
            
            //Checks valid input
            if(!(object instanceof Integer) || !validPort((int) object)){
                System.out.println("NODE NOT REGISTERED: INVALID PORT NUMBER");
                continue;
            }

            int port = (int) object;

            if(nodePorts.isEmpty()){
                client.run((Object) blockchain, port);
                addNode(port);
                System.out.println("DAEMON REGISTERED FIRST PORT: " + port);
                continue;
            }

            //Registers port number of new node and sends to existing nodes
            broadcast(object, nodePorts);
            for(int nodePort: nodePorts){
                client.run((Object) nodePort, port);
            }
            //TODO: Request blockchain from node & Send blockchain and nodePorts to new node
            broadcast((Object) "daemon resolve", nodePorts);
            updateBlockchain();
            client.run((Object) blockchain, port);
            addNode(port);

            System.out.println("DAEMON REGISTERED NEW PORT: " + port);

        }
    }

    // public void listenNodeServer(){
    //     while(nodeServer.isRunning()){
    //         Object object = nodeServer.listen();
    //         if(!(object instanceof String) && !((String) object).equals("chain")){
    //             continue;
    //         }
    //         break;
    //     }
    //     client.run
    // }

    private void updateBlockchain(){

        //if(nodePorts.isEmpty()) return blockchain;
        Object object;
        Blockchain newChain;

        int count = 0;
        while(nodeServer.isRunning() && count<nodePorts.size()){
            object = nodeServer.listen();
            newChain = (Blockchain) object;
            System.out.println("DAEMON received blockchain size: " + newChain.getChain().size());
            if(newChain.getChain().size()>blockchain.getChain().size() && Blockchain.validChain(newChain)){
                System.out.println("DAEMON OVERWROTE CHAIN");
                blockchain = newChain;
            }
            count++;
        }

    }

    // public Blockchain returnBlockchain(){
    //     return getBlockchain();
    // }

    private boolean validPort(int port){
        /*Checks whether input port is valid. */

        //Checks if port already used
        if(nodePorts.contains(port)) return false;

        //Checks if port is within valid range
        if(port<1023 || port>65536) return false;

        return true;
    }

    private void addNode(int port){
        nodePorts.add(port);
    }

    private void broadcast(Object object, ArrayList<Integer> ports){
        for(int port: ports){
            client.run(object, port);
        }
    }

    private void requestBlockchain(){
        String msg = "blockchain";
        broadcast((Object) msg, nodePorts);
    }

    // private Blockchain getBlockchain(){
    //     int blockCount = 0;
    //     Blockchain blockchain;
    //     while(blockCount<nodePorts.size()){
    //         Blockchain object = (Blockchain) nodeServer.listen();
    //         if(blockCount==0){
    //             blockchain = object;
    //             blockCount++;
    //             continue;
    //         }

    //         //TODO:check equals stored blockchain, if not use consensus algo
    //     }
    // } 
}
