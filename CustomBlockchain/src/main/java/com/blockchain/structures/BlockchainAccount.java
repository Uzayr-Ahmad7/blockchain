package com.blockchain.structures;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class BlockchainAccount {
    private final String id;
    private float balance;
    private String userName;
    private String password;
    private final String nodeID;
    private JSONArray transactionHist;

    // public BlockchainAccount(String id, String userName, String password){
    //     this.id = id;
    //     this.balance = 0;
    //     this.userName = userName;
    //     this.password = password;
    //     this.transactionHist = new JSONArray();
    // }

    public BlockchainAccount(String id, String username, String password, String nodeID){
        this.id = id;
        this.balance = 0;
        this.userName = username;
        this.password = password;
        this.nodeID = nodeID;
        this.transactionHist = new JSONArray();
    }

    public float getBalance(){
        return balance;
    }

    public String getID(){
        return id;
    }

    public boolean addBalance(float amount){
        balance+=amount;
        return true;
    }

    public boolean removeBalance(float amount){
        if(amount>balance) return false;

        balance-=amount;
        return true;
    }

    public boolean isNode(){
        return nodeID!=null;
    }

    public boolean addTransaction(JSONObject transaction){
        if(transaction.get("sender")==id){
            if(!removeBalance((float) transaction.get("amount"))) return false;
        }
        else if(transaction.get("recipient")==id){
            addBalance((float) transaction.get("amount"));
        }
        else return false;

        transactionHist.add(transaction);
        return true;
    }
}
