package com.blockchain.network;

public class TestClient {
    public static void main(String[] args){
        Client client = new Client();
        Object object = (Object) 777;
        Object object2 = (Object) 333;
        client.run(object, 9000);
        client.run(object2, 9000);

    } 
}
