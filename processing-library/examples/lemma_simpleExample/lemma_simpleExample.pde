//------------------------------------------------------------------------------------------------//
// 1. Import core library + Event (Helps IDE parse "EventHandler.Callback(Event event)" below)
//------------------------------------------------------------------------------------------------//

import lemma.library.Event;
import lemma.library.EventHandler;
import lemma.library.Lemma;
import java.util.logging.Level;

//------------------------------------------------------------------------------------------------//
// 2. Instance one or more Lemmas. Construct, setup listeners, run in loop.
//------------------------------------------------------------------------------------------------//

Lemma lemma;
int messagesSent = 1;

void setup(){
  lemma = new Lemma(this, "lemmaID", "Desired Room");
  //lemma = new Lemma(this, "lemmaID", "Desired Room", Level.FINE);
  // Listen for an Event
  lemma.hear("messagesSent", new MyEventHandler());
}
void draw(){
  // Try to send an event
  if ( lemma.sendEvent("messagesSent", messagesSent) ){
    messagesSent++;
  }
  //connect and handle incoming events
  lemma.run();
}

//----------------------------------------------------------------------------------------------------------------//
// 3. Extend Lemma to implement EventHandler interface (Processing's Main sketch can't implement interfaces...)
//----------------------------------------------------------------------------------------------------------------//

class MyEventHandler implements EventHandler {

  // Called each time we receive an event
  public void callback(Event event){
    System.out.println("Caught event : " + event.name + " : " + event.stringValue);
  }
}
