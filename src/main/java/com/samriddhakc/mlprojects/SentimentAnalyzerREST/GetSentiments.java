package com.samriddhakc.mlprojects.SentimentAnalyzerREST;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Root resource (exposed at "myresource" path)
 */
@Path("getsentiments")
public class GetSentiments {

    /**
     * Method handling HTTP GET requests. The returned object will be sent
     * to the client as "text/plain" media type.
     *
     * @return String that will be returned as a text/plain response.
     */
    @GET @Path("/twitter")
    @Produces(MediaType.TEXT_PLAIN)
    public String getTwitterSentiments() {
        return "get twitter sentiments!";
    }
    
    @GET @Path("/reddit")
    @Produces(MediaType.TEXT_PLAIN)
    public String getRedditSentiments() {
        return "get reddit sentiments!";
    }
    
    @GET @Path("/aggregate")
    @Produces(MediaType.TEXT_PLAIN)
    public String getAggregateSentiments() {
        return "get aggregate sentiments!";
    }
    
    
}
