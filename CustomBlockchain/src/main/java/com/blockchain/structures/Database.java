package com.blockchain.structures;

import com.blockchain.structures.config.JsonReader;
import com.blockchain.structures.config.JsonWriter;

public class Database {
    
    public Database(){}

    public Blockchain createBlockchain(String filename){
        //TODO: return error if filename already exists in folder - ensures no files are overwritten
        Blockchain blockchain = new Blockchain();
        JsonWriter.jsonWrite(blockchain, filename);
        return blockchain;
    }

    public void saveBlockchain(Blockchain blockchain, String filename){
        JsonWriter.jsonWrite(blockchain, filename);
    }

    public Blockchain loadBlockchain(String filename){
        return JsonReader.read(filename);
    }
}
