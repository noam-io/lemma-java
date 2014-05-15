// Copyright (c) 2014, IDEO

//------------------------------------------------------------------------------------------------//
// 1. Import core library + Event (Helps IDE parse "EventHandler.Callback(Event event)" below)
//------------------------------------------------------------------------------------------------//

import lemma.library.Event;
import lemma.library.EventHandler;
import lemma.library.Lemma;
import java.util.Random;
import java.lang.Thread;
//------------------------------------------------------------------------------------------------//
// 2. Instance one or more Lemmas. Construct, setup listeners, run in loop.
//------------------------------------------------------------------------------------------------//

Lemma guest1;
Lemma guest2;
int goodFeelings = 0;
long lastSecond = second();
String[] Greetings = new String[]{
  "Hello", "Hallo", "Goedendag", "Hi", "Salut", "Hej",
  "Bonjour", "Guten Tag", "Shalom", "Ohayoou", "Oi", "Privet!", "Hola"
};

void setup(){  
  guest2 = new Lemma(this, "Billy", "");
  
  // Listen for an Event
  guest2.hear("SallySpeaks", new GuestTwoHandler());
}

void draw(){
  //connect and handle incoming events
  guest2.run();
}

//----------------------------------------------------------------------------------------------------------------//
// 3. Extend Lemma to implement EventHandler interface (Processing's Main sketch can't implement interfaces...)
//----------------------------------------------------------------------------------------------------------------//

class GuestTwoHandler implements EventHandler {

  // Called each time we receive an event
  public void callback(Event event){
    try{
      Thread.sleep(500);
    } catch(InterruptedException e){
    }
    int elemNum = new Random().nextInt(Greetings.length);
    String randomGreeting = Greetings[elemNum];
    guest2.sendEvent("BillySpeaks", randomGreeting);
  }
}
