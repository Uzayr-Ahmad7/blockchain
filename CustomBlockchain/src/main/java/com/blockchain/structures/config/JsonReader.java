package com.blockchain.structures.config;

import java.io.File;

import com.blockchain.structures.Blockchain;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonReader {

    public static Blockchain read(String filename){
        Blockchain blockchain = new Blockchain();
        try {
            ObjectMapper mapper = new ObjectMapper();
            String path = "/Users/uzayr/OneDrive/Documents/Coding/Projects/Blockchain/JAVA spring/data/";
            blockchain = mapper.readValue(new File(path+filename+".json"), Blockchain.class);
            System.out.println("File read");
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

        return blockchain;
    }

    public static Blockchain read(int port, String filename){
        Blockchain blockchain = new Blockchain();
        try {
            ObjectMapper mapper = new ObjectMapper();
            String path = "/Users/uzayr/OneDrive/Documents/Coding/Projects/Blockchain/JAVA spring/data/nodes/";
            blockchain = mapper.readValue(new File(path+String.valueOf(port)+"/" +filename+".json"), Blockchain.class);
            System.out.println("File read");
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

        return blockchain;
    }
}
