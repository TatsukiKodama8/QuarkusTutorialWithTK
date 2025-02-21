package org.acme.rabbitmq.producer;

import java.util.UUID;

import jakarta.ws.rs.GET;
// import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import org.acme.rabbitmq.model.Quote;
// import org.acme.rabbitmq.model.Quote;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

import io.smallrye.mutiny.Multi;

// import io.smallrye.mutiny.Multi;

@Path("/quotes")
public class QuotesResource {

    @Channel("quote-requests") Emitter<String> quoteRequestEmitter; 
    @Channel("quotes") Multi<Quote> quotes; // 追加
    /**
     * Endpoint to generate a new quote request id and send it to "quote-requests" channel (which
     * maps to the "quote-requests" RabbitMQ exchange) using the emitter.
     */
    @GET
    @Produces(MediaType.SERVER_SENT_EVENTS) 
    public Multi<Quote> stream() {
        return quotes; 
    }

    @POST
    @Path("/request")
    @Produces(MediaType.TEXT_PLAIN)
    public String createRequest() {
        UUID uuid = UUID.randomUUID();
        quoteRequestEmitter.send(uuid.toString()); 
        return uuid.toString();
    }
}