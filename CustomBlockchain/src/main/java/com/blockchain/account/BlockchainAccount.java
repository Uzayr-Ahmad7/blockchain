package com.blockchain.account;

import java.io.Serializable;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.blockchain.structures.objects.Transaction;

public class BlockchainAccount implements Serializable{
    private String id;
    private float balance;
    // private String username;
    // private String password;
    private int nodePort;
    private ArrayList<Transaction> transactionHist;

    // public BlockchainAccount(String id, String userName, String password){
    //     this.id = id;
    //     this.balance = 0;
    //     this.userName = userName;
    //     this.password = password;
    //     this.transactionHist = new JSONArray();
    // }

    public BlockchainAccount(){
        super();
    }
    public BlockchainAccount(String id, int nodePort){
        this.id = id;
        this.balance = 10;
        // this.username = username;
        // this.password = password;
        this.nodePort = nodePort;
        this.transactionHist = new ArrayList<Transaction>();
    }

    public BlockchainAccount(String id){
        this.id = id;
        this.balance = 10;
        // this.username = username;
        // this.password = password;
        this.transactionHist = new ArrayList<Transaction>();
    }

    public float getBalance(){
        return balance;
    }

    public String getID(){
        return id;
    }

    public int getNodePort(){
        return nodePort;
    }

    public ArrayList<Transaction> getTransactionHist(){
        return transactionHist;
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

    // public boolean isNode(){
    //     return nodeID!=null;
    // }

    public boolean addTransaction(Transaction transaction){
        if(transaction.getSender().equals(id)){
            if(!removeBalance((float) transaction.getAmount())) return false;
        }
        else if(transaction.getRecipient().equals(id)){
            addBalance((float) transaction.getAmount());
        }
        else return false;

        transactionHist.add(transaction);
        return true;
    }
}
