package com.blockchain.customblockchain;

import com.blockchain.structures.Blockchain;

public class ChainInit {
    private Blockchain blockchain;

    public ChainInit(){
        blockchain = new Blockchain();
    }

    public Blockchain getBlockchain(){
        return blockchain;
    }
}
