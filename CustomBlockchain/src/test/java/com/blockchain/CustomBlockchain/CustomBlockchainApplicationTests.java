package com.blockchain.CustomBlockchain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;


import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.blockchain.structures.Block;
import com.blockchain.structures.Blockchain;
import com.blockchain.structures.Transaction;

@SpringBootTest
class CustomBlockchainApplicationTests {

	private Blockchain blockchain = new Blockchain();

	@Test
	void contextLoads() {
	}

	@Test
	void testGenesis(){
		assertEquals(new Block(0, new ArrayList<Transaction>(), "1", 100), blockchain.getLastBlock());
		assertEquals(1, blockchain.getBlockCount());
	}

	@Test
	void testTransactions(){
		// int index = blockchain.getLastBlock().getIndex();
		String prevhash = blockchain.getLastBlock().getHash();
		// int prevProof = blockchain.getLastBlock().getProof();
		// ArrayList<Transaction> transactions = blockchain.getTransactions();

		//Init
		blockchain.addAccount("1");
		blockchain.addAccount("2");
		blockchain.addAccount("3");

		assertEquals(0, blockchain.getChain()[0].getTransactions().size());
		assertTrue(blockchain.addTransaction("1", "2", 3));

		//Checks token transfer
		assertEquals(7, blockchain.getAccountMap().get("1").getBalance());
		assertEquals(13, blockchain.getAccountMap().get("2").getBalance());

		//Transactions recorded in account history
		assertEquals(1, blockchain.getAccountMap().get("1").getTransactionHist().size());
		assertEquals(1, blockchain.getAccountMap().get("2").getTransactionHist().size());


		assertEquals(1, blockchain.getTransactions().size());
		assertEquals(0, blockchain.getChain()[0].getTransactions().size());


		blockchain.addBlock(prevhash, 24);
		
		assertEquals(1, blockchain.getLastBlock().getTransactions().size());
		assertEquals(0, blockchain.getTransactions().size());

	}

}
