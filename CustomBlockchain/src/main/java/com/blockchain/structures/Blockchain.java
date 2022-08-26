package com.blockchain.structures;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

@Entity
public class Blockchain {
    private @Id @GeneratedValue final int chainID; 
    private Block[] chain;
    private JSONArray transactions;
    private int blockCount;
    Set<String> nodeIDs;
    HashMap<String, Node> nodes;
    Set<String> accountIDs;
    HashMap<String, BlockchainAccount> accounts;

    public Blockchain(){

        //Initialise variables
        chainID = 1;
        chain = new Block[100];
        nodeIDs = new HashSet<String>();
        nodes = new HashMap<String, Node>();
        transactions = new JSONArray();
        blockCount = 0;
        accounts = new HashMap<String, BlockchainAccount>();

        //Create Genesis Block
        addBlock("1", 100);
    }

    public Block addBlock(String prevhash, int proof){
        /*
         Adds a new block to the chain
         :param: <String> prevhash: hash of the previous block
                 <int> proof: proof for this block
         :return: <boolean> true if block added, false if not
         */

        //Adds the block to chain
        chain[blockCount] = new Block(blockCount, this.transactions, prevhash, proof);

        //Resets the transaction list and count;
        transactions.clear();

        blockCount++;
    
        return chain[blockCount-1];
    }

    public boolean addTransaction(String sender, String recipient, int amount){
        /*
         Adds a new transaction to the current list
         :param: <String> type: type of transactions (see below)
         :return: <boolean> true if transaction added, false if not

         Transaction Types:
            payment: transfer of token
            vote: a vote
         */


        // switch(type.toUpperCase()){
        //     case "PAYMENT":
        //         break;
        //     case "VOTE":
        //         break;
        //     default:
        //         return false;
        // }

        if(!accountIDs.contains(sender) || !accountIDs.contains(recipient)) return false;
        
        //TODO: Add timestamp to transaction
        JSONObject transaction = new JSONObject();
        transaction.put("sender", sender);
        transaction.put("recipient", recipient);
        transaction.put("amount", amount);
        transaction.put("timestamp", System.currentTimeMillis());

        // JSONObject transactionObj = new JSONObject();
        // transactionObj.put(type.toUpperCase(), transaction);


        //Checks if payment is valid, then adds transaction to blockchain & account history 
        if(accounts.get(sender).addTransaction(transaction)){
            accounts.get(recipient).addTransaction(transaction);
            transactions.add(transaction);
            return true;
        }
        

        return false;
    }

    public boolean addTransaction(Transaction transaction){
        String sender = transaction.getSender();
        String recipient = transaction.getRecipient();
        int amount = transaction.getAmount();

        if(accounts.get(sender).addTransaction(transaction.viewTransaction())){
            accounts.get(recipient).addTransaction(transaction.viewTransaction());
            transactions.add(transaction);
            return true;
        }
        
        return false;
    }

    public Block getLastBlock(){
        return chain[blockCount-1];
    }

    public Block[] getChain(){
        return chain;
    }

    public int getBlockCount(){
        return blockCount;
    }

    public static int PoW(int lastProof){
        int proof = 0;
        while(!validPoW(lastProof, proof)){
            proof++;
        }
        return proof;
    }
    
    private static boolean validPoW(int lastProof, int currentProof){
        String str = String.valueOf(lastProof*currentProof);
        String guess = Block.hashString(str);

        return guess.substring(guess.length()-4) == "0000";
    }

    public boolean registerNode(String address) throws MalformedURLException{
        URL url = new URL(address);

        if(nodeIDs.add(url.getHost())){
            BlockchainAccount account = addAccount(url.getHost());
            nodes.put(url.getHost(), new Node(url.getHost(), account.getID()));
            return true;
        }
        
        return false;
    }

    private static boolean validChain(Blockchain blockchain){
        Block[] chain = blockchain.getChain();
        Block lastBlock = chain[0];
        int currentIndex = 1;
        Block block;

        while(currentIndex<blockchain.getBlockCount()){
            block = chain[currentIndex];

            // Checks hash of block is correct
            if(block.getPrevhash() != Block.hashBlock(lastBlock)) return false;

            //Checks proof of work is correct
            if(!validPoW(lastBlock.getProof(), block.getProof())) return false;

            lastBlock = block;
            currentIndex++;
        }

        return true;
    }

    private BlockchainAccount addAccount(){
        UUID uuid = UUID.randomUUID();
        String id = String.valueOf(uuid);

        if(!accountIDs.add(id)) {
            return addAccount();
        }
        
        BlockchainAccount account = new BlockchainAccount(id, String.valueOf(accountIDs.size()), String.valueOf(accountIDs.size()), null);
        accounts.put(id, account);

        return account;

    }

    private BlockchainAccount addAccount(String nodeID){
        UUID uuid = UUID.randomUUID();
        String id = String.valueOf(uuid);

        if(!accountIDs.add(id)) {
            return addAccount();
        }

        BlockchainAccount account = new BlockchainAccount(id, String.valueOf(accountIDs.size()), String.valueOf(accountIDs.size()), nodeID);
        accounts.put(id, account);

        return account;

    }
}
