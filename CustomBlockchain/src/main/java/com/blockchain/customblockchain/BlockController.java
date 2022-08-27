package com.blockchain.customblockchain;

import org.springframework.web.bind.annotation.RestController;

import com.blockchain.structures.Block;
import com.blockchain.structures.Blockchain;
import com.blockchain.structures.BlockchainAccount;
import com.blockchain.structures.Transaction;

import java.util.Map;

import org.json.simple.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
public class BlockController {
    
    private Blockchain blockchain = new Blockchain();

    @GetMapping("/mine")
    Block mine(){
        Block lastBlock = blockchain.getLastBlock();
        int lastProof = lastBlock.getProof();
        int proof = Blockchain.PoW(lastProof);

        //TODO: get node address and corresponding account id to send transaction
        //blockchain.addTransaction("0", "recipient", 1);

        String prevhash = Block.hashBlock(lastBlock);
        Block block = blockchain.addBlock(prevhash, proof);

        return block;
    }

    @GetMapping("chain/last")
    public Block viewLastBlock(){
        return blockchain.getLastBlock();
    }

    @PostMapping("/transactions/new")
    public Transaction newTransaction(@RequestBody Map<String, String> json){
        String sender = json.get("sender");
        String recipient = json.get("recipient");
        int amount = Integer.valueOf(json.get("amount"));

        //TODO: create POST request for account creation
        // if(blockchain.addTransaction(sender, recipient, amount)){
        //         return new Transaction(sender, recipient, amount);
        // }

        return new Transaction(sender, recipient, amount);
    }

    @PostMapping("accounts/new")
    public BlockchainAccount addAccount(){
        // String id = json.get("id");
        // String username = json.get("username");
        // String password = json.get("password");
        // String nodeID = json.get("nodeID");

        return blockchain.addAccount();
    }

    @GetMapping("accounts/all")
    public BlockchainAccount[] viewAccounts(){
        return blockchain.getAccounts();
    }


}
