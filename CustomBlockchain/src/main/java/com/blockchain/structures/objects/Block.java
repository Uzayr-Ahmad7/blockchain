package com.blockchain.structures.objects;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class Block {
    private int index;
    private long timestamp;
    private ArrayList<Transaction> transactions;
    private String prevhash;
    private String hash;
    private int proof;

    public Block(){
        super();
    }

    public Block(int index, ArrayList<Transaction> transactions, String prevhash, int proof){
        this.index = index;
        this.timestamp = System.currentTimeMillis();
        this.transactions = transactions;
        this.prevhash = prevhash;
        this.hash = hashBlock(this);
        this.proof = proof;
    }


    public int getIndex(){
        return this.index;
    }

    public long getTimestamp(){
        return this.timestamp;
    }

    public ArrayList<Transaction> getTransactions(){
        return this.transactions;
    }

    public String getPrevhash(){
        return this.prevhash;
    }

    public String getHash(){
        return this.hash;
    }

    public int getProof(){
        return this.proof;
    }

    public JSONObject toJSON(){
        JSONObject jsonBlock = new JSONObject();
        jsonBlock.put("index", index);
        jsonBlock.put("timestamp", timestamp);
        jsonBlock.put("prevhash", prevhash);
        jsonBlock.put("hash", hash);
        jsonBlock.put("proof", proof);
        jsonBlock.put("transactions", transactions);

        return jsonBlock;
    }

    public static String hashBlock(Block block){
        String str =  String.valueOf(block.getIndex())+
                        String.valueOf(block.getTimestamp())+
                        block.getTransactions()+
                        block.getPrevhash()+
                        String.valueOf(block.getProof());
                        
        return hashString(str);
    }

    public static String hashString(String str){
    
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("SHA3-256");
            final byte[] hashbytes = digest.digest(str.getBytes(StandardCharsets.UTF_8));
            String sha3Hex = bytesToHex(hashbytes);

            return sha3Hex;

        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "";

    }

    private static String bytesToHex(byte[] hash){
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        
        for (int i=0; i<hash.length; i++){
            String hex = Integer.toHexString(0xff & hash[i]);    
            if(hex.length()==1) hexString.append('0');
            
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
