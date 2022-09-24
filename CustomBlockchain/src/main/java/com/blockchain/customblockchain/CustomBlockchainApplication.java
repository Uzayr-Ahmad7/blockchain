package com.blockchain.customblockchain;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.context.ServletWebServerInitializedEvent;
import org.springframework.context.ApplicationListener;

import com.blockchain.structures.objects.Block;


@SpringBootApplication
public class CustomBlockchainApplication implements ApplicationListener<ServletWebServerInitializedEvent> {

	private static int port;

	public static void main(String[] args) {
		SpringApplication.run(CustomBlockchainApplication.class, args);
	}

	@Override
    public void onApplicationEvent(final ServletWebServerInitializedEvent event) {
		/*Listens for application start */

		//Gets the running port
        port = event.getWebServer().getPort();
        System.out.println("PORT NUMBER (onApplication): " + port);

		//Initialises the node
		BlockController.init(port);
    }

	public static int viewPort(){
		return port;
	}
}
