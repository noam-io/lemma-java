// Copyright (c) 2014, IDEO

import lemma.library.Event;
import lemma.library.EventHandler;
import lemma.library.Lemma;
import org.json.JSONObject;

Lemma lemma;

void setup() {
  lemma = new Lemma(this, "ProcessingClient", "lemma_verification");
  lemma.hear("Echo", new EchoHandler());
  lemma.hear("PlusOne", new PlusOneHandler());
  lemma.hear("Sum", new SumHandler());
  lemma.hear("Name", new NameHandler());
}

void draw() {
  lemma.run();
}

class EchoHandler implements EventHandler {
  public void callback(Event event) {
    lemma.sendEvent("EchoVerify", event.stringValue);
  }
}

class PlusOneHandler implements EventHandler {
  public void callback(Event event) {
    lemma.sendEvent("PlusOneVerify", event.intValue + 1);
  } 
}

class SumHandler implements EventHandler {
  public void callback(Event event) {
    int sum = 0;
    for(int i = 0; i < event.arraySize; i++) {
       sum += event.intArray[i];
    }
    lemma.sendEvent("SumVerify", sum);
  } 
}

class NameHandler implements EventHandler {
  public void callback(Event event) {
    JSONObject input = new JSONObject(event.stringValue);
    String output = "{\"fullName\":\"" + input.get("firstName") + " " + input.get("lastName") + "\"}";
    lemma.sendEvent("NameVerify", output);
  } 
}
