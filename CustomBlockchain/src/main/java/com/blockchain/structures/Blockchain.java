package com.blockchain.structures;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.blockchain.structures.objects.Block;
import com.blockchain.structures.objects.BlockchainAccount;
import com.blockchain.structures.objects.Node;
import com.blockchain.structures.objects.Transaction;

@Entity
public class Blockchain {
    private @Id @GeneratedValue final int chainID; 
    private ArrayList<Block> chain;
    private ArrayList<Transaction> transactions;
    private int blockCount;
    Set<String> nodeIDs;
    HashMap<String, Node> nodes;
    Set<String> accountIDs;
    HashMap<String, BlockchainAccount> accountMap;

    public Blockchain(){

        //Initialise variables
        chainID = 1;
        chain = new ArrayList<>();
        nodeIDs = new HashSet<String>();
        nodes = new HashMap<String, Node>();
        transactions = new ArrayList<Transaction>();
        blockCount = 0;
        accountMap = new HashMap<String, BlockchainAccount>();
        accountIDs = new HashSet<String>();

        //Create Genesis Block
        createGenesis();
    }

    public Block createGenesis(){
        ArrayList<Transaction> transac = new ArrayList<>(transactions);
        chain.add(new Block(0, transac, "1", 100));
        blockCount++;
        return chain.get(0);
    }

    public Block addBlock(String prevhash, int proof){
        /*
         Adds a new block to the chain
         :param: <String> prevhash: hash of the previous block
                 <int> proof: proof for this block
         :return: <boolean> true if block added, false if not
         */

        //Adds the block to chain
        ArrayList<Transaction> transac = new ArrayList<>(transactions);
        chain.add(new Block(blockCount, transac, prevhash, proof));

        //Resets the transaction list and count;
        transactions.clear();

        blockCount++;
    
        return chain.get(chain.size()-1);
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

        //Checks IDs are valid
        if(!accountIDs.contains(sender) || !accountIDs.contains(recipient)) return false;
        
        //Creates new transaction
        Transaction transaction = new Transaction(sender, recipient, amount);

        //Checks if payment is valid, then adds transaction to blockchain & account history 
        if(accountMap.get(sender).addTransaction(transaction)){
            accountMap.get(recipient).addTransaction(transaction);
            transactions.add(transaction);
            return true;
        }
        

        return false;
    }

    public boolean addTransaction(Transaction transaction){
        String sender = transaction.getSender();
        String recipient = transaction.getRecipient();
        int amount = transaction.getAmount();

        if(accountMap.get(sender).addTransaction(transaction)){
            accountMap.get(recipient).addTransaction(transaction);
            transactions.add(transaction);
            return true;
        }
        
        return false;
    }

    public Block mine(){
        Block lastBlock = lastBlock();
        int lastProof = lastBlock.getProof();
        int proof = Blockchain.PoW(lastProof);

        //TODO: get node address and corresponding account id to send transaction
        //blockchain.addTransaction("0", "recipient", 1);

        String prevhash = Block.hashBlock(lastBlock);
        Block block = addBlock(prevhash, proof);

        return block;
    }

    public Block lastBlock(){
        return chain.get(chain.size()-1);
    }

    public ArrayList<Block> getChain(){
        return chain;
    }

    public int getBlockCount(){
        return chain.size();
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

        return guess.substring(guess.length()-4).equals("0000");
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
        ArrayList<Block> chain = blockchain.getChain();
        Block lastBlock = chain.get(0);
        int currentIndex = 1;
        Block block;

        while(currentIndex<blockchain.getBlockCount()){
            block = chain.get(currentIndex);

            // Checks hash of block is correct
            if(block.getPrevhash() != Block.hashBlock(lastBlock)) return false;

            //Checks proof of work is correct
            if(!validPoW(lastBlock.getProof(), block.getProof())) return false;

            lastBlock = block;
            currentIndex++;
        }

        return true;
    }

    public BlockchainAccount addAccount(){
        UUID uuid = UUID.randomUUID();
        String id = String.valueOf(uuid);
        
        if(!accountIDs.add(id)) {
            return addAccount();
        }
        
        BlockchainAccount account = new BlockchainAccount(id, null);
        accountMap.put(id, account);

        return account;

    }

    public BlockchainAccount addAccount(String id){
        if(!accountIDs.add(id)){
            return addAccount(String.valueOf(Integer.valueOf(id)+1));
        }

        BlockchainAccount account = new BlockchainAccount(id, null);
        accountMap.put(id, account);

        return account;
    }

    public BlockchainAccount[] accounts(){
        int size = accountIDs.size();
        BlockchainAccount[] accountsArr = new BlockchainAccount[size];
        int count = 0;

        for(String key: accountMap.keySet()){
            accountsArr[count] = accountMap.get(key);
            count++;
        }
        return accountsArr;
    }

    public HashMap<String, BlockchainAccount> getAccountMap(){
        return accountMap;
    }

    public Object[] getAccountIDs(){
        return accountIDs.toArray();
    }

    public ArrayList<Transaction> getTransactions(){
        return transactions;
    }
}
