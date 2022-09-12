package com.blockchain.customblockchain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;


import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.blockchain.network.Daemon;
import com.blockchain.network.Node;
import com.blockchain.structures.Blockchain;
import com.blockchain.structures.objects.Block;
import com.blockchain.structures.objects.Transaction;

@SpringBootTest
class CustomBlockchainApplicationTests {

	private Blockchain blockchain = new Blockchain();

	@Test
	void contextLoads() {
	}

	@Test
	void testGenesis(){
		assertEquals(new Block(0, new ArrayList<Transaction>(), "1", 100), blockchain.lastBlock());
		assertEquals(1, blockchain.getChain().size());
	}

	@Test
	void testTransactions(){
		// int index = blockchain.getLastBlock().getIndex();
		String prevhash = blockchain.lastBlock().getHash();
		// int prevProof = blockchain.getLastBlock().getProof();
		// ArrayList<Transaction> transactions = blockchain.getTransactions();

		//Init
		blockchain.createAccount("1");
		blockchain.createAccount("2");
		blockchain.createAccount("3");

		assertEquals(0, blockchain.getChain().get(0).getTransactions().size());
		assertTrue(blockchain.createTransaction("1", "2", 3));

		//Checks token transfer
		assertEquals(7, blockchain.getAccountMap().get("1").getBalance());
		assertEquals(13, blockchain.getAccountMap().get("2").getBalance());

		//Transactions recorded in account history
		assertEquals(1, blockchain.getAccountMap().get("1").getTransactionHist().size());
		assertEquals(1, blockchain.getAccountMap().get("2").getTransactionHist().size());

		//Transactions recorded in blockchain history
		assertEquals(1, blockchain.getTransactions().size());
		assertEquals(0, blockchain.getChain().get(0).getTransactions().size());

		//Mine new block
		blockchain.createBlock(prevhash, 24);
		
		//Transactions contained within previous block and not added to previously mined blocks
		assertEquals(1, blockchain.lastBlock().getTransactions().size());
		assertEquals(0, blockchain.getTransactions().size());

	}

	@Test
	void testNetwork(){
		Node node = new Node(8010);
		Blockchain blockchain = node.getBlockchain();

		blockchain.createAccount("1");
		blockchain.createAccount("2");
		blockchain.createAccount("3");

		blockchain.createTransaction("1", "2", 3);

		blockchain.mine();

		assertEquals(0, node.getBlockchain().getTransactions().size());
		assertEquals(1, node.getBlockchain().lastBlock().getTransactions().size());
		assertEquals(2, node.getBlockchain().getChain().size());

		node.getBlockchain().mine();

		assertEquals(3, blockchain.getChain().size());
	}

	@Test 
	void hashTest(){
		Blockchain blockchain = new Blockchain();
		assertEquals(Block.hashBlock(blockchain.lastBlock()), Block.hashBlock(blockchain.lastBlock()));
		assertEquals(blockchain.lastBlock().getHash(), Block.hashBlock(blockchain.lastBlock()));
	}

	@Test
	void validChainTest(){
		Blockchain blockchain = new Blockchain();

		blockchain.createAccount("1");
		blockchain.createAccount("2");
		blockchain.createAccount("3");

		blockchain.createTransaction("1", "2", 3);

		blockchain.mine();

		Block firstBlock = blockchain.getChain().get(0);
		Block secondBlock = blockchain.getChain().get(1);

		assertEquals(2, blockchain.getChain().size());
		assertEquals(secondBlock.getPrevhash(), Block.hashBlock(firstBlock));
		assertTrue(Blockchain.validPoW(firstBlock.getProof(), secondBlock.getProof()));

		assertTrue(Blockchain.validChain(blockchain));
	}

	@Test
	void testResolveConflicts(){
		// Daemon daemon = new Daemon();
		// daemon.register();

		// System.out.println("POST DAEMON");

		Node nodeOne = new Node(8051);
		Blockchain blockchainOne = nodeOne.getBlockchain();
		nodeOne.runServer();
		
		System.out.println("POST NODE 1");

		assertEquals(1, blockchainOne.getChain().size());

		Node nodeTwo = new Node(8052);
		Blockchain blockchainTwo = nodeTwo.getBlockchain();
		nodeTwo.runServer();

		System.out.println("POST NODE 2");
		
		assertEquals(1, blockchainTwo.getChain().size());
	}
}
