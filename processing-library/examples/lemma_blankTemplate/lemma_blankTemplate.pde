import lemma.library.*;
import lemma.library.Event;

//----------------------------------------------------------------------------------------------------------------//

ConcreteLemma lemma;

void setup(){
  lemma = new ConcreteLemma(this, "lemmaID", 9934);  // <-- Maestro Port
  lemma.begin();
}
void draw(){
  lemma.run();
}

//----------------------------------------------------------------------------------------------------------------//

class ConcreteLemma extends Lemma implements EventHandler {
  
  ConcreteLemma(PApplet parent, String lemmaID, int port){
    super(parent, lemmaID, port);
  }
  
  void begin(){                                               
    // register as listener ... e.g. [hear("eventName", this)]
    super.begin();
  }
  
  void run(){
    // send events ... e.g. [sendEvent("eventName", eventValue)]
    super.run();
  }
  
  void callback(Event event){
    // handle event ... e.g. [Event.name / Event.stringValue / Event.intValue / Event.floatValue]
  }
}
