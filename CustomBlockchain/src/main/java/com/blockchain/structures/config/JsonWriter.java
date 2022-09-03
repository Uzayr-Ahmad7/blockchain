package com.blockchain.structures.config;

import java.io.File;
import java.io.FileWriter;

import com.blockchain.structures.Blockchain;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

public class JsonWriter {

    private static FileWriter file;
    // public static void main(String[] args){
    //     Blockchain blockchain = data();
    //     jsonWrite(blockchain, "jsonData3");
    // }
    
    private static Blockchain data(){
        Blockchain blockchain = new Blockchain();
        blockchain.addAccount();
        blockchain.addAccount();
        blockchain.addAccount();
        String id1 = blockchain.accounts()[0].getID();
        String id2 = blockchain.accounts()[1].getID();
        String id3 = blockchain.accounts()[2].getID();

        blockchain.addTransaction(id1, id2, 3);
        blockchain.mine();
        blockchain.addTransaction(id2, id3, 5);
        
        return blockchain;
    }

    public void write(Blockchain blockchain, String fileName){

        try {
            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            String json = ow.writeValueAsString(blockchain);

            file = new FileWriter("/Users/uzayr/OneDrive/Documents/Coding/Projects/Blockchain/JAVA spring/data/"+fileName+".txt");
            file.write(json);
            
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        } finally{
            try {
                file.flush();
                file.close();
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }
        }
    }

    public static void jsonWrite(Blockchain blockchain, String fileName){

        try {
            ObjectMapper ow = new ObjectMapper();
            ow.writeValue(new File("/Users/uzayr/OneDrive/Documents/Coding/Projects/Blockchain/JAVA spring/data/"+fileName+".json"), blockchain);
            
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        } 
    }
}
