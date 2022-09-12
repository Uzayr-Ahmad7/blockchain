package com.blockchain.structures;

import com.blockchain.structures.config.JsonReader;
import com.blockchain.structures.config.JsonWriter;

public class Database {
    
    public Database(){}

    public static Blockchain createBlockchain(String filename){
        //TODO: return error if filename already exists in folder - ensures no files are overwritten
        Blockchain blockchain = new Blockchain();
        JsonWriter.write(blockchain, filename);
        return blockchain;
    }

    public static Blockchain createBlockchain(int port, String filename){
        //TODO: return error if filename already exists in folder - ensures no files are overwritten
        Blockchain blockchain = new Blockchain();
        JsonWriter.write(blockchain, port, filename);
        return blockchain;
    }

    public static void saveBlockchain(Blockchain blockchain, String filename){
        JsonWriter.write(blockchain, filename);
    }

    public static void saveBlockchain(Blockchain blockchain, int port, String filename){
        JsonWriter.write(blockchain, port, filename);
    }

    public static Blockchain loadBlockchain(String filename){
        return JsonReader.read(filename);
    }

    public static Blockchain loadBlockchain(int port, String filename){
        return JsonReader.read(port, filename);
    }
}
