package com.blockchain.structures;

public class Node {
    private String urlHost;
    private String accountID;

    public Node(String urlHost, String accountID){
        this.urlHost = urlHost;
        this.accountID = accountID;
    }

    public String getUrlHost(){
        return urlHost;
    }

    public String getAccountID(){
        return accountID;
    }
}
