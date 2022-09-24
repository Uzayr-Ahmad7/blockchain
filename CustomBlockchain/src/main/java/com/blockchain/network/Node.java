package com.blockchain.network;

import java.util.ArrayList;

import com.blockchain.account.BlockchainAccount;
import com.blockchain.structures.Blockchain;
import com.blockchain.structures.Database;
import com.blockchain.structures.objects.Block;
import com.blockchain.structures.objects.Transaction;

public class Node {
    //private String urlHost;
    //private String accountID;
    private int runningPort;
    private int listeningPort;
    private ArrayList<Integer> connectedPorts;
    private Server server;
    private Client client;
    private Blockchain blockchain;

    /*
     * Port Numbers: 
     * Running ports start with 8
     * Listening ports start with 7
     * Corresponding ports have a value difference of 1000 e.g. rPort = 8046, lPort = 7046
     */

    public Node(int runningPort){
        this.runningPort = runningPort;
        this.listeningPort = runningPort-1000;
        server = new Server(listeningPort);
        client = new Client();
        connectedPorts = new ArrayList<>();
        //TODO: access latest blockchain from daemon
        blockchain = new Blockchain();
        registerNode(listeningPort);
    }
    
    public void updateBlockchain(Blockchain blockchain) { this.blockchain = blockchain; }

    public int getListeningPort() { return listeningPort; }

    public ArrayList<Integer> getConnectedPorts() { return connectedPorts; }

    public Blockchain getBlockchain() { return blockchain; }

    public void resolveConflicts(){
        /* Requests all nodes in the network to resolve any conflicts */
        //TODO: COMPLETE
        String msg = "resolve";
        broadcast((Object) msg, connectedPorts);
    }

    /* CLIENT: 
     * used to send data to other nodes on the network
     * 
    */

    private void registerNode(int listeningPort){
        /* Sends node's listening Port to the daemon to register & connect to the network */
        client.run((Object) listeningPort, 9000);
    }

    public void broadcast(Object object, int port){
        client.run(object, port);
    }

    public void broadcast(Object object, ArrayList<Integer> ports){
        for(int port: ports){
            client.run(object, port);
        }
    }

    private void chain2Daemon(Blockchain blockchain){
        /* Sends the node's chain to the daemon */
        client.run((Object) blockchain, 9000);
    }

    /* SERVER:
     * use run function from daemon
     * case handling for input type: Integer means new node; block means new mine; transactions means new transaction 
     */

    public void runServer(){
        /* Runs the server for the node. Allows the node to receive new transactions/blocks/etc. */

        while(server.isRunning()){
            Object object = server.listen();
            if(object instanceof Integer){
                //register new node - add node to connected ports
                int port = (int) object;

                if(!validPort(port)){
                    //TODO: Relay message to rest of ports;
                    System.out.println("Server " + listeningPort + ": invalid port received");
                    continue;
                }

                connectedPorts.add(port);
            }
            else if(object instanceof Block){
                //Check valid block, add to blockchain
                Block block = (Block) object;

                if(!blockchain.validBlock(block)){
                    //TODO: Relay message to rest of ports;
                    System.out.println("Server " + listeningPort + ": invalid block received");
                    resolveConflicts();
                    continue;
                }

                blockchain.addBlock(block);
            }
            else if(object instanceof Transaction){
                //Check valid transcation, add to blockchain
                //TODO: Check valid transaction if not return transaction invalid
                Transaction transaction = (Transaction) object;
                System.out.println("OBJECT INSTANCEOF TRANSACTION");
                blockchain.addTransaction(transaction);
            }
            else if(object instanceof Blockchain){
                //resolves conflicts between node's chain and received chain
                Blockchain newChain = (Blockchain) object;

                if(blockchain.getChain().size()==1) {
                    blockchain = newChain;
                    System.out.println("Server " + listeningPort+": chain size = 1");
                    System.out.println("Server " + listeningPort+": new chain size = " + blockchain.getChain().size());
                    continue;
                }

                System.out.println("Server " + listeningPort+": chain size != 1");

                if(newChain.getChain().size()>blockchain.getChain().size() && Blockchain.validChain(newChain)){
                    blockchain = newChain;
                    System.out.println("Server " + listeningPort + ": accepted new chain");
                    //TODO: send updated blockchain to controller
                }
                else{
                    System.out.println("Server " + listeningPort + ": rejected chain");
                }
            }
            else if(object instanceof BlockchainAccount){
                blockchain.addAccount((BlockchainAccount) object);
            }
            else if(object instanceof String && ((String) object).equals("resolve")){
                broadcast((Object) blockchain, connectedPorts);
            }
            else if(object instanceof String && ((String) object).equals("daemon resolve")){
                client.run((Object) blockchain, 6000);
                System.out.println("Node sent daemon blockchain size: " + blockchain.getChain().size());
            }
            else{
                System.out.println("Server " + listeningPort + " received " + object + " of invalid TYPE");
            }

        }
    }

    private boolean validPort(int port){

        if(connectedPorts.contains(port)) return false;

        if(port<1023 || port>65536) return false;

        return true;
    }
    
}
