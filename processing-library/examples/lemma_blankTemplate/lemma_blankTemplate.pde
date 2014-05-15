// Copyright (c) 2014, IDEO

import lemma.library.Event;
import lemma.library.EventHandler;
import lemma.library.Lemma;

//----------------------------------------------------------------------------------------------------------------//

Lemma lemma;

void setup(){
  lemma = new Lemma(this, "lemmaID", "Desired Room");
  // register as listener ... e.g. [lemma.hear("eventName", new MyEventHandler())]
}
void draw(){
  // send events ... e.g. [lemma.sendEvent("eventName", eventValue)]
  lemma.run();
}

//----------------------------------------------------------------------------------------------------------------//

class MyEventHandler implements EventHandler {
  public void callback(Event event){
    // handle event ... e.g. [Event.name / Event.stringValue / Event.intValue / Event.floatValue]
  }
}
