package com.ads.jpos;

import org.jpos.iso.*;
import org.jpos.util.Log;
import java.util.Date;
import java.util.EventObject;
import java.util.concurrent.atomic.AtomicLong;

public class Logon extends Log implements ISOServerEventListener {
    AtomicLong stan = new AtomicLong();
    @Override
    public void handleISOServerEvent(EventObject event) {
        if (event instanceof ISOServerAcceptEvent) {
            ISOServer server = (ISOServer) event.getSource();
            ISOChannel channel = server.getLastConnectedISOChannel();
            try {
                channel.send(createMsg("001"));
            } catch (Exception e) {
                error(e);
            }
        }
    }
    private ISOMsg createMsg (String msgType) throws ISOException {
        ISOMsg m = new ISOMsg ("0800");
        m.set (7, ISODate.getDateTime (new Date()));
        m.set (11, ISOUtil.zeropad (Long.toString (stan.incrementAndGet()), 6));
        m.set (70, msgType);
        return m;
    }
}
