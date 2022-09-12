package com.blockchain.customblockchain;

import com.blockchain.network.Daemon;

public class runDaemon {
    public static void main(String[] args){
        Daemon daemon = new Daemon();
        daemon.register();
    }
}
