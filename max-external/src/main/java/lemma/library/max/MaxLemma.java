// Copyright (c) 2014, IDEO

package lemma.library.max;

import com.cycling74.max.*;
import lemma.library.Event;
import lemma.library.EventHandler;
import lemma.library.Lemma;

public class MaxLemma extends MaxObject implements EventHandler{

    private Lemma lemma = null;
    private volatile Thread thread = null;
    private String LEMMA_NAME = "max-lemma";
    private String[] HEARS = null;

    public MaxLemma() {
        declareAttribute("HEARS");
        declareAttribute("LEMMA_NAME");
    }

    private boolean active(){
        return lemma != null;
    }

    public void begin() {
        if (active()) {
           dispose();
        }
        lemma = new Lemma(this, LEMMA_NAME, "");

        if (HEARS != null) {
            for (String messageName : HEARS) {
                lemma.hear(messageName, this);
            }
        }
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("Starting MaxLemma...");
                while (Thread.currentThread() == thread) {
                    lemma.run();
                }
            }
        });
        thread.start();
    }

    public void sendEvent(String name, String value){
        lemma.sendEvent(name, value);
    }

    @Override
    public void callback(Event event) {
        outlet(0,event.name, Atom.newAtom(event.stringValue));
    }

    public void stop(){
        dispose();
    }

    private void dispose(){
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