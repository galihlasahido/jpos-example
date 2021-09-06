package com.ads.jpos;

import org.jpos.core.Configurable;
import org.jpos.core.Configuration;
import org.jpos.core.ConfigurationException;
import org.jpos.iso.ISOMsg;
import org.jpos.transaction.Context;
import org.jpos.transaction.GroupSelector;
import org.jpos.util.Log;

import java.io.Serializable;

public class Selector implements GroupSelector, Configurable {
    private Configuration cfg;
    private String isorequest;
    private String logger;

    @Override
    public void setConfiguration(Configuration configuration) throws ConfigurationException {
        this.cfg = configuration;
        this.isorequest = configuration.get("isorequest");
        this.logger = configuration.get("logger");
    }

    @Override
    public String select(long l, Serializable serializable) {
        String group = "Unknown";
        ISOMsg m = ((Context) serializable).get(this.isorequest);

        try {
            switch(m.getMTI()) {
                default:
                case "0800":
                    group = cfg.get (m.getMTI()+m.getString(3));
                break;
            }

        } catch (Exception e) {
            StringBuffer ste = new StringBuffer();
            ste.append(e.getMessage());
            for(StackTraceElement element : e.getStackTrace()) {
                ste.append(element.toString()+"\n");
            }
            Log.getLog(this.logger, this.logger).error(ste.toString());
            ste = null;
        }

        return group;
    }

    @Override
    public int prepare(long l, Serializable serializable) {
        return PREPARED | READONLY;
    }

    @Override
    public void commit(long l, Serializable serializable) {

    }

    @Override
    public void abort(long l, Serializable serializable) {

    }
}
