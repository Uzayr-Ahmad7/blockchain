package com.blockchain.customblockchain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CustomBlockchainApplication.class,
  webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("fixedport")
public class portUnitTest{
    @Value("${server.port}")
    private int serverPort;

    @Autowired
    private ServerProperties serverProperties;

    @Test
    public void givenFixedPortAsServerPort_whenReadServerPort_thenGetThePort() {
        assertEquals(8081, serverPort);
    }
    
    @Test
    public void givenFixedPortAsServerPort_whenReadServerProps_thenGetThePort() {
        int port = serverProperties.getPort();
 
        assertEquals(8081, port);
    }
}