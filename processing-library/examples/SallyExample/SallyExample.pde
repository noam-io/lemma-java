// Copyright (c) 2014, IDEO

//------------------------------------------------------------------------------------------------//
// 1. Import core library + Event (Helps IDE parse "EventHandler.Callback(Event event)" below)
//------------------------------------------------------------------------------------------------//

import lemma.library.Event;
import lemma.library.EventHandler;
import lemma.library.Lemma;
import java.util.Random;
//------------------------------------------------------------------------------------------------//
// 2. Instance one or more Lemmas. Construct, setup listeners, run in loop.
//------------------------------------------------------------------------------------------------//

Lemma guest1;
Lemma guest2;
long lastSecond = second();
String[] Greetings = new String[]{
  "Hello", "Hallo", "Goedendag", "Hi", "Salut", "Hej",
  "Bonjour", "Guten Tag", "Shalom", "Ohayoou", "Oi", "Privet!", "Hola"
};

void setup(){  
  guest1 = new Lemma(this, "Sally", "");
  
  // Listen for an Event
  guest1.hear("BillySpeaks", new Guest1Handler());
}

void draw(){
  
  long currSecond = second();
  if(currSecond != lastSecond && currSecond != lastSecond + 1){
    int elemNum = new Random().nextInt(Greetings.length);
    String randomGreeting = Greetings[elemNum];
    guest1.sendEvent("SallySpeaks", randomGreeting);
    lastSecond = currSecond;
  }
  
  //connect and handle incoming events
  guest1.run();
}

//----------------------------------------------------------------------------------------------------------------//
// 3. Extend Lemma to implement EventHandler interface (Processing's Main sketch can't implement interfaces...)
//----------------------------------------------------------------------------------------------------------------//

class Guest1Handler implements EventHandler {

  // Called each time we receive an event
  public void callback(Event event){
    guest1.sendEvent("SallyFeelingsGood", "Yup");
  }
}
