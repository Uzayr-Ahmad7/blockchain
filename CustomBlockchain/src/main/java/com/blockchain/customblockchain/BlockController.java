package com.blockchain.customblockchain;

import org.springframework.web.bind.annotation.RestController;

import com.blockchain.structures.Block;
import com.blockchain.structures.Blockchain;
import com.blockchain.structures.Transaction;

import org.json.simple.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
public class BlockController {
    private final Blockchain blockchain;

    public BlockController(Blockchain blockchain){
        this.blockchain = blockchain;
    }

    @GetMapping("/mine")
    JSONObject mine(){
        Block lastBlock = blockchain.getLastBlock();
        int lastProof = lastBlock.getProof();
        int proof = Blockchain.PoW(lastProof);

        //TODO: get node address and corresponding account id to send transaction
        blockchain.addTransaction("0", "recipient", 1);

        String prevhash = Block.hashBlock(lastBlock);
        Block block = blockchain.addBlock(prevhash, proof);

        return block.toJSON();
    }

    // @PostMapping("/transactions/new")
    // public JSONObject newTransaction(@RequestBody Transaction transaction){

    //     if(blockchain.addTransaction(transaction)){

    //     }
    // }


}
