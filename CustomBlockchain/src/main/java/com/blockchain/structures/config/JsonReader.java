package com.blockchain.structures.config;

import java.io.File;

import com.blockchain.structures.Blockchain;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonReader {

    // public static void main(String[] args){
    //     Blockchain blockchain = read("jsonData3");
    //     System.out.println("BlockCount: " + blockchain.getBlockCount());
    //     System.out.println("No. of Transactions: " + blockchain.getTransactions().size());
    //     System.out.println(blockchain.lastBlock().toJSON());

    //     blockchain = update(blockchain);

    //     JsonWriter.jsonWrite(blockchain, "jsonData3Read");

    // }

    public static Blockchain read(String fileName){
        Blockchain blockchain = new Blockchain();
        try {
            ObjectMapper mapper = new ObjectMapper();
            blockchain = mapper.readValue(new File("/Users/uzayr/OneDrive/Documents/Coding/Projects/Blockchain/JAVA spring/data/"+fileName+".json"), Blockchain.class);
            System.out.println("File read");
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

        return blockchain;
    }

    private static Blockchain update(Blockchain blockchain){
        blockchain.addAccount();
        String id1 = blockchain.accounts()[0].getID();
        String id2 = blockchain.accounts()[1].getID();
        String id3 = blockchain.accounts()[2].getID();
        String id4 = blockchain.accounts()[3].getID();

        blockchain.addTransaction(id4, id2, 8);
        blockchain.mine();
        blockchain.addTransaction(id2, id3, 5);
        blockchain.mine();

        return blockchain;
    }
}
