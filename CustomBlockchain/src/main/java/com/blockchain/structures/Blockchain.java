package com.blockchain.structures;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import com.blockchain.account.BlockchainAccount;
import com.blockchain.structures.objects.Block;
import com.blockchain.structures.objects.Transaction;


public class Blockchain implements Serializable{
    private ArrayList<Block> chain;
    private ArrayList<Transaction> transactions;
    //private int blockCount;
    Set<Integer> nodePorts;
    HashMap<Integer, String> nodeAccounts;
    Set<String> accountIDs;
    HashMap<String, BlockchainAccount> accountMap;

    public Blockchain(){

        //Initialise variables
        chain = new ArrayList<>();
        nodePorts = new HashSet<Integer>();
        nodeAccounts = new HashMap<Integer, String>();
        transactions = new ArrayList<Transaction>();
        accountMap = new HashMap<String, BlockchainAccount>();
        accountIDs = new HashSet<String>();

        //Create Genesis Block
        createGenesis();
    }

    public ArrayList<Block> getChain(){ return chain; }

    public ArrayList<Transaction> getTransactions(){ return transactions; }

    public Set<Integer> getNodePorts(){ return nodePorts; }

    public HashMap<Integer, String> getNodeAccounts(){ return nodeAccounts; }

    public Object[] getAccountIDs(){ return accountIDs.toArray(); }

    public HashMap<String, BlockchainAccount> getAccountMap(){ return accountMap; }

    public Block createGenesis(){
        ArrayList<Transaction> transac = new ArrayList<>(transactions);
        chain.add(new Block(0, transac, "1", 100));
        return chain.get(0);
    }

    public Block createBlock(String prevhash, int proof){
        /*
         Adds a new block to the chain
         :param: <String> prevhash: hash of the previous block
                 <int> proof: proof for this block
         :return: <boolean> true if block added, false if not
         */

        //Adds the block to chain
        ArrayList<Transaction> transac = new ArrayList<>(transactions);
        chain.add(new Block(chain.size(), transac, prevhash, proof));

        //Resets the transaction list and count;
        transactions.clear();
    
        return chain.get(chain.size()-1);
    }

    public void addBlock (Block block){
        chain.add(block);
        transactions.clear();
    }

    public boolean createTransaction(String sender, String recipient, int amount){
        /*
         Creates a new transaction & adds to the current list
         :param: <String> sender; <String> recipient; <Integer> amount 
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
        /*
         Adds a transaction to the current list
         :param: <Transaction> transaction: 
         :return: <boolean> true if transaction added, false if not

         
         */
        System.out.println("ADDING TRANSACTION");

        if(!validTransaction(transaction)) {
            System.out.println("INVALID TRANSACTION RECEIVED");
            return false;
        }

        String sender = transaction.getSender();
        String recipient = transaction.getRecipient();
        int amount = transaction.getAmount();

        if(accountMap.get(sender).addTransaction(transaction)){
            accountMap.get(recipient).addTransaction(transaction);
            transactions.add(transaction);
            System.out.println("TRANSACTION SUCCESSFUL");
            return true;
        }
        System.out.println("FAILED TRANSACTION: SENDER DOES NOT HAVE THE FUNDS!");

        return false;
    }

    public Block mine(){
        Block lastBlock = lastBlock();
        int lastProof = lastBlock.getProof();
        int proof = Blockchain.PoW(lastProof);

        //TODO: get node address and corresponding account id to send transaction
        //blockchain.addTransaction("0", "recipient", 1);

        String prevhash = Block.hashBlock(lastBlock);
        Block block = createBlock(prevhash, proof);

        return block;
    }

    public Block lastBlock(){
        return chain.get(chain.size()-1);
    }

    public static int PoW(int lastProof){
        int proof = 0;
        while(!validPoW(lastProof, proof)){
            proof++;
        }
        return proof;
    }
    
    public static boolean validPoW(int lastProof, int currentProof){
        String str = String.valueOf(lastProof*currentProof);
        String guess = Block.hashString(str);

        return guess.substring(guess.length()-4).equals("0000");
    }

    public boolean registerNode(int listeningPort) {
        //TODO: update

        if(nodePorts.add(listeningPort)){
            BlockchainAccount account = createAccount(listeningPort);
            nodeAccounts.put(listeningPort, account.getID());
            return true;
        }
        
        return false;
    }

    public static boolean validChain(Blockchain blockchain){
        ArrayList<Block> chain = blockchain.getChain();
        Block lastBlock = chain.get(0);
        int currentIndex = 1;
        Block block;

        while(currentIndex<blockchain.getChain().size()){
            block = chain.get(currentIndex);

            // Checks hash of block is correct
            if(!block.getPrevhash().equals(Block.hashBlock(lastBlock))) return false;
        
            //Checks proof of work is correct
            if(!validPoW(lastBlock.getProof(), block.getProof())) return false;
            
            lastBlock = block;
            currentIndex++;
        }

        return true;
    }

    public boolean validBlock(Block block){
        /* 
        Checks whether newly mined block is valid
         :param: <Block> block: newly mined block
         :return: <boolean> true if valid, false if not 
         */

        Block lastBlock = lastBlock();

        //Checks each value stored in block is accurate
        if(block.getIndex()!=lastBlock.getIndex()+1) return false;
        if(block.getTimestamp()<=lastBlock.getTimestamp() || block.getTimestamp()>System.currentTimeMillis()) return false;
        if(!block.getTransactions().equals(transactions)) return false;
        if(block.getPrevhash()!=lastBlock.getHash()) return false;
        if(block.getHash()!=Block.hashBlock(block)) return false;
        if(!validPoW(lastBlock.getProof(), block.getProof())) return false;

        return true;
    }

    public boolean validTransaction(Transaction transaction){

        //Checks if each stored value is accurate
        if(!accountIDs.contains(transaction.getSender()) || !accountIDs.contains(transaction.getRecipient())) {
            System.out.println("TRANSACTION MEMBERS DONT EXIST");
            return false;
        }

        if(transaction.getTimestamp()>=System.currentTimeMillis()) {
            System.out.println("TRANSACTION TIMESTAMP INVALID");
            return false;
        }

        if(!transactions.isEmpty() && transaction.getTimestamp()<=transactions.get(transactions.size()-1).getTimestamp()){
            System.out.println("TRANSACTION TIMESTAMP INVALID");
            return false;
        }
        
        return true;
    }

    public BlockchainAccount createAccount(){
        UUID uuid = UUID.randomUUID();
        String id = String.valueOf(uuid);
        
        if(!accountIDs.add(id)) {
            return createAccount();
        }
        
        BlockchainAccount account = new BlockchainAccount(id);
        accountMap.put(id, account);

        return account;

    }

    public BlockchainAccount createAccount(String id){
        if(!accountIDs.add(id)){
            return createAccount(String.valueOf(Integer.valueOf(id)+1));
        }

        BlockchainAccount account = new BlockchainAccount(id);
        accountMap.put(id, account);

        return account;
    }

    public BlockchainAccount createAccount(int nodePort){
        UUID uuid = UUID.randomUUID();
        String id = String.valueOf(uuid);
        
        if(!accountIDs.add(id)) {
            return createAccount(nodePort);
        }
        
        BlockchainAccount account = new BlockchainAccount(id, nodePort);
        accountMap.put(id, account);

        return account;

    }

    public boolean addAccount(BlockchainAccount account){
        if(accountIDs.add(account.getID())){
            accountMap.put(account.getID(), account);
            return true;
        }

        return false;
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

}
