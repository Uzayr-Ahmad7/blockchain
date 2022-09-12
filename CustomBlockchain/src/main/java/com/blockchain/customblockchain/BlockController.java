package com.blockchain.customblockchain;

import org.springframework.web.bind.annotation.RestController;

import com.blockchain.account.BlockchainAccount;
import com.blockchain.network.Node;
import com.blockchain.structures.Blockchain;
import com.blockchain.structures.Database;
import com.blockchain.structures.objects.Block;
import com.blockchain.structures.objects.Transaction;

import java.util.ArrayList;
import java.util.Map;

import javax.annotation.PreDestroy;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
public class BlockController {
    
    private static Blockchain blockchain;
    private static Node node;

    public static void init(int runningPort){
        node = new Node(runningPort);
        node.runServer();
    }

    @GetMapping("/mine")
    Block mine(){
        blockchain = node.getBlockchain();        

        Block lastBlock = blockchain.lastBlock();
        int lastProof = lastBlock.getProof();
        int proof = Blockchain.PoW(lastProof);

        //TODO: get node address and corresponding account id to send transaction
        //blockchain.addTransaction("0", "recipient", 1);

        String prevhash = Block.hashBlock(lastBlock);
        Block block = blockchain.createBlock(prevhash, proof);

        node.broadcast((Object) block, node.getConnectedPorts());
        Database.saveBlockchain(blockchain, CustomBlockchainApplication.viewPort(), "chain");

        node.updateBlockchain(blockchain);

        return block;
    }

    @GetMapping("chain/last")
    public Block viewLastBlock(){
        return node.getBlockchain().lastBlock();
    }

    @GetMapping("chain/full")
    public ArrayList<Block> viewChain(){
        return node.getBlockchain().getChain();
    }

    @PostMapping("/transactions/new")
    public Transaction newTransaction(@RequestBody Map<String, String> json){
        blockchain = node.getBlockchain();        


        String sender = json.get("sender");
        String recipient = json.get("recipient");
        int amount = Integer.valueOf(json.get("amount"));

        if(blockchain.createTransaction(sender, recipient, amount)){
            Transaction transaction = blockchain.getTransactions().get(blockchain.getTransactions().size()-1);
            node.updateBlockchain(blockchain);
            node.broadcast((Object) transaction, node.getConnectedPorts());
            Database.saveBlockchain(blockchain, CustomBlockchainApplication.viewPort(), "chain");

            return new Transaction(sender, recipient, amount);
        }


        return new Transaction(null, null, 0);
    }

    @PostMapping("accounts/new")
    public BlockchainAccount addAccount(){
        // String id = json.get("id");
        // String username = json.get("username");
        // String password = json.get("password");
        // String nodeID = json.get("nodeID");
        blockchain = node.getBlockchain();        

        BlockchainAccount account = blockchain.createAccount();
        node.broadcast((Object) account, node.getConnectedPorts());
        Database.saveBlockchain(blockchain, CustomBlockchainApplication.viewPort(), "chain");
        node.updateBlockchain(blockchain);

        return account;
    }

    @GetMapping("accounts/all")
    public BlockchainAccount[] viewAccounts(){
        return node.getBlockchain().accounts();
    }

    @GetMapping("accounts/IDs")
    public Object[] getAccountIDs(){
        return node.getBlockchain().getAccountIDs();
    }

    @GetMapping("transactions/current")
    public ArrayList<Transaction> getCurrentTransactions(){
        return node.getBlockchain().getTransactions();
    }
}
