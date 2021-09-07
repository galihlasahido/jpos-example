package com.ads.jpos;

import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISORequestListener;
import org.jpos.iso.ISOSource;

import java.io.IOException;

public class IsoListener implements ISORequestListener {
    @Override
    public boolean process(ISOSource source, ISOMsg m) {
        try {
            m.setResponseMTI();
            m.set(39, "00");
            source.send(m);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ISOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
