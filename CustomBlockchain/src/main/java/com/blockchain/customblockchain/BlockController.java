package com.blockchain.customblockchain;

import org.springframework.web.bind.annotation.RestController;

import com.blockchain.structures.Blockchain;
import com.blockchain.structures.Database;
import com.blockchain.structures.objects.Block;
import com.blockchain.structures.objects.BlockchainAccount;
import com.blockchain.structures.objects.Transaction;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PreDestroy;

import org.json.simple.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
public class BlockController {
    
    private Database database = new Database();
    Blockchain blockchain = database.loadBlockchain("001");

    @GetMapping("/mine")
    Block mine(){
        Block lastBlock = blockchain.lastBlock();
        int lastProof = lastBlock.getProof();
        int proof = Blockchain.PoW(lastProof);

        //TODO: get node address and corresponding account id to send transaction
        //blockchain.addTransaction("0", "recipient", 1);

        String prevhash = Block.hashBlock(lastBlock);
        Block block = blockchain.addBlock(prevhash, proof);

        database.saveBlockchain(blockchain, "001");

        return block;
    }

    @GetMapping("chain/last")
    public Block viewLastBlock(){
        return blockchain.lastBlock();
    }

    @GetMapping("chain/full")
    public ArrayList<Block> viewChain(){
        return blockchain.getChain();
    }

    @PostMapping("/transactions/new")
    public Transaction newTransaction(@RequestBody Map<String, String> json){
        String sender = json.get("sender");
        String recipient = json.get("recipient");
        int amount = Integer.valueOf(json.get("amount"));

        if(blockchain.addTransaction(sender, recipient, amount)){
                return new Transaction(sender, recipient, amount);
        }

        database.saveBlockchain(blockchain, "001");

        return new Transaction(null, null, 0);
    }

    @PostMapping("accounts/new")
    public BlockchainAccount addAccount(){
        // String id = json.get("id");
        // String username = json.get("username");
        // String password = json.get("password");
        // String nodeID = json.get("nodeID");

        database.saveBlockchain(blockchain, "001");
        return blockchain.addAccount();
    }

    @GetMapping("accounts/all")
    public BlockchainAccount[] viewAccounts(){
        return blockchain.accounts();
    }

    @GetMapping("accounts/IDs")
    public Object[] getAccountIDs(){
        return blockchain.getAccountIDs();
    }

    @PreDestroy
    public void destroy(){
        System.out.println("Application Closed");
    }
}
