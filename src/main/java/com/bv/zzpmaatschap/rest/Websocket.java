package com.bv.zzpmaatschap.rest;

import com.bv.zzpmaatschap.services.PriceListImportService;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

@Stateless
@ServerEndpoint(value = "/ws")
public class Websocket  {
    private static final Set<Session> peers = Collections.synchronizedSet(new HashSet<Session>());


    @EJB
    public PriceListImportService priceListImportService;

    private Logger logger = Logger.getLogger(this.getClass().getName());

    @OnOpen
    public void onOpen(Session session) {
        peers.add(session
        );
        logger.info("Connected ... " + session.getId());
    }

    @OnMessage
    public String onMessage(String message, Session session) {

        logger.info(priceListImportService.getFuture().toString());

        switch (message) {
            case "quit":
                try {
                    session.close(new CloseReason(CloseReason.CloseCodes.NORMAL_CLOSURE, "Game ended"));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                break;
        }
        return message;
    }

    @OnClose
    public void onClose(Session session, CloseReason closeReason) {
        logger.info(String.format("Session %s closed because of %s", session.getId(), closeReason));
    }
}
