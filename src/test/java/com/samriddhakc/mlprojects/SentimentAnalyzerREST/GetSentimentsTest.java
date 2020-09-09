package com.samriddhakc.mlprojects.SentimentAnalyzerREST;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import org.glassfish.grizzly.http.server.HttpServer;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class GetSentimentsTest {

    private HttpServer server;
    private WebTarget target;

    @Before
    public void setUp() throws Exception {
        // start the server
        server = Main.startServer();
        // create the client
        Client c = ClientBuilder.newClient();

        // uncomment the following line if you want to enable
        // support for JSON in the client (you also have to uncomment
        // dependency on jersey-media-json module in pom.xml and Main.startServer())
        // --
        // c.configuration().enable(new org.glassfish.jersey.media.json.JsonJaxbFeature());

        target = c.target(Main.BASE_URI);
    }

    @After
    public void tearDown() throws Exception {
        server.stop();
    }

    /**
     * Test to see that the message "Got it!" is sent in the response.
     */
   
    
    @Test
    public void testGetSentiments() {
        String responseMsgTwitter = target.path("getsentiments/twitter").request().get(String.class);
        assertEquals("get twitter sentiments!",responseMsgTwitter);
        String responseMsgReddit= target.path("getsentiments/reddit").request().get(String.class);
        assertEquals("get reddit sentiments!",responseMsgReddit);
        String responseMsgAgg= target.path("getsentiments/aggregate").request().get(String.class);
        assertEquals("get aggregate sentiments!",responseMsgAgg);
    }
  
    
    @Test
    public void testSentimentStream() throws InterruptedException {
       SentimentStream stream1=new SentimentStream();
    }
}
