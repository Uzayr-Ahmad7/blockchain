package com.blockchain.structures.config;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.blockchain.structures.Blockchain;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonWriter{

    public static void write(Blockchain blockchain, String fileName){

        try {
            ObjectMapper ow = new ObjectMapper();
            String path = "/Users/uzayr/OneDrive/Documents/Coding/Projects/Blockchain/JAVA spring/data/";
            ow.writeValue(new File(path+fileName+".json"), blockchain);
            
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        } 
    }

    public static void write(Blockchain blockchain, int port, String fileName){
        String folder = "/Users/uzayr/OneDrive/Documents/Coding/Projects/Blockchain/JAVA spring/data/nodes/"+String.valueOf(port);
        try {
            ObjectMapper ow = new ObjectMapper();
            ow.writeValue(new File(folder+"/"+fileName+".json"), blockchain);
            
        } catch (FileNotFoundException e){
            System.out.println("PATH NOT FOUND: " + folder);
            mkdir(folder);
            System.out.println("PATH CREATED: " + folder);
            write(blockchain, port, fileName);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private static boolean mkdir(String folder){
        try {
            Path path = Paths.get(folder);
            Files.createDirectory(path);
            return true;
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        } 

        return false;
    }

}
