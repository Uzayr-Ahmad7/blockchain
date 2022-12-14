package com.blockchain.structures;

import org.json.simple.JSONObject;

public class Transaction {
    private String sender;
    private String recipient;
    private int amount;
    private long timestamp;
    private JSONObject jsonTransaction;

    public Transaction(String sender, String recipient, int amount){
        this.sender = sender;
        this.recipient = recipient;
        this.amount = amount;
        this.timestamp = System.currentTimeMillis();

        jsonTransaction.put("sender", this.sender);
        jsonTransaction.put("recipient", this.recipient);
        jsonTransaction.put("amount", this.amount);
        jsonTransaction.put("timestamp", this.timestamp);

    }

    public JSONObject viewTransaction(){
        return jsonTransaction;
    }

    public String getSender(){
        return sender;
    }
    public String getRecipient(){
        return recipient;
    }
    public int getAmount(){
        return amount;
    }

}
