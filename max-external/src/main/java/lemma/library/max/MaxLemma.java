// Copyright (c) 2014, IDEO

package lemma.library.max;

import com.cycling74.max.*;
import lemma.library.Event;
import lemma.library.EventHandler;
import lemma.library.Lemma;

public class MaxLemma extends MaxObject implements EventHandler{

    private Lemma lemma = null;
    private volatile Thread thread = null;
    private String LEMMA_NAME = "maxLemma";
    private String ROOM_NAME = "";
    private String[] HEARS = null;

    public MaxLemma() {
        declareAttribute("HEARS");
        declareAttribute("LEMMA_NAME");
        declareAttribute("ROOM_NAME");
        createInfoOutlet(false);
    }

    private boolean active(){
        return lemma != null;
    }

    public void begin() {
        if (active()) {
           dispose();
        }
        lemma = new Lemma(this, LEMMA_NAME, ROOM_NAME);

        if (HEARS != null) {
            for (String messageName : HEARS) {
                lemma.hear(messageName, this);
            }
        }
        thread = new Thread(new Runnable() {
            @Override
            public synchronized void run() {
                while (!Thread.currentThread().isInterrupted()) {
                    lemma.run();
                    try {
                        thread.sleep(2);
                    } catch (InterruptedException e) {
                        break;
                    }
                }
            }
        });
        thread.start();
    }

    public void sendEvent(String name, String value){
        if (lemma != null && lemma.connected()) {
            lemma.sendEvent(name, value);
        }
    }

    @Override
    public void callback(Event event) {
        outlet(0,event.name, Atom.newAtom(event.stringValue));
    }

    public void stop(){
        dispose();
    }

    private void dispose(){
        if(thread != null) {
            thread.interrupt();
        }
        thread = null;
        if (lemma != null) {
            lemma.stop();
        }
        lemma = null;
    }
    
    @Override
    protected void notifyDeleted() {
        dispose();
    }
}