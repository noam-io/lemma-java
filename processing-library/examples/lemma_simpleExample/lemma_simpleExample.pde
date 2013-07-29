//------------------------------------------------------------------------------------------------//
// 1. Import core library + Event (Helps IDE parse "EventHandler.Callback(Event event)" below)
//------------------------------------------------------------------------------------------------//

import lemma.library.*;
import lemma.library.Event;

//------------------------------------------------------------------------------------------------//
// 2. Instance one or more ConcreteLemmas. Construct, begin, run in loop.
//------------------------------------------------------------------------------------------------//

ConcreteLemma lemma;

void setup(){
  lemma = new ConcreteLemma(this, "test", 9934);
  lemma.begin();
}
void draw(){
  lemma.run();
}

//----------------------------------------------------------------------------------------------------------------//
// 3. Extend Lemma to implement EventHandler interface (Processing's Main sketch can't implement interfaces...)
//----------------------------------------------------------------------------------------------------------------//

class ConcreteLemma extends Lemma implements EventHandler {
    
  int messagesSent;
  
  ConcreteLemma(PApplet parent, String lemmaID, int port){    // Construct parent 
    super(parent, lemmaID, port);
  }
  
  void begin(){                                               
    hear("messagesSent", this);                               // Register to recieve events
    super.begin();                                            // Initialize parent
  }
  
  void run(){
    if ( sendEvent("messagesSent", messagesSent) ){
      messagesSent++;
    }
    super.run();                                              // Run Parent
  }
  
  void callback(Event event){                                 // Called each time we recieve an event
    System.out.println("Caught event : " + event.name + " : " + event.stringValue);
  }
}
