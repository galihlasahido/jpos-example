package com.ads.jpos;

import org.jpos.core.Configurable;
import org.jpos.core.Configuration;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISOSource;
import org.jpos.transaction.Context;
import org.jpos.transaction.TransactionParticipant;

import java.io.IOException;
import java.io.Serializable;

public class Network implements TransactionParticipant, Configurable {
    private Configuration cfg;
    private String isorequest;
    private String isosource;

    @Override
    public void setConfiguration(Configuration configuration) {
        this.cfg = configuration;
        this.isorequest = this.cfg.get("isorequest");
        this.isosource = this.cfg.get("isosource");
    }

    @Override
    public int prepare(long l, Serializable serializable) {
        return PREPARED | READONLY;
    }

    @Override
    public void commit(long l, Serializable serializable) {
        ISOMsg message = ((Context) serializable).get(this.isorequest);
        ISOSource source = ((Context) serializable).get(this.isosource);
        try {
            message.setResponseMTI();
            message.set(39, "00");
            source.send(message);
        } catch (ISOException | IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void abort(long l, Serializable serializable) { }
    
    
}