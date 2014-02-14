//------------------------------------------------------------------------------------------------//
// 1. Import core library + Event (Helps IDE parse "EventHandler.Callback(Event event)" below)
//------------------------------------------------------------------------------------------------//

import lemma.library.Event;
import lemma.library.EventHandler;
import lemma.library.Lemma;

//------------------------------------------------------------------------------------------------//
// 2. Instance one or more Lemmas. Construct, setup listeners, run in loop.
//------------------------------------------------------------------------------------------------//

Lemma lemma;
String messageString = "Hello";
boolean messageBool = true;
int messageInt = 1;
float messageFloat = 1.5f;
double messageDouble = 1.5;

String messageStringArr[] = {"Hello", "World", "Again"};
boolean messageBoolArr[] = {true, false, true};
int messageIntArr[] = {1,2,3};
float messageFloatArr[] = {1.5f, 2.5f, 3.5f};
double messageDoubleArr[] = {1.5, 2.5, 3.5};

int lastSecond = second();

void setup(){
  lemma = new Lemma(this, "lemmaID", "pa-m-dbrody.local - Noam Moderator");
  // Listen for an Event
  lemma.hear("messageInt", new IntHandler());
  lemma.hear("messageBool", new BoolHandler());
  lemma.hear("messageDouble", new DoubleHandler());
  lemma.hear("messageFloat", new FloatHandler());
  lemma.hear("messageString", new StringHandler());
  
  lemma.hear("messageIntArr", new IntArrHandler());
  lemma.hear("messageBoolArr", new BoolArrHandler());
  lemma.hear("messageDoubleArr", new DoubleArrHandler());
  lemma.hear("messageFloatArr", new FloatArrHandler());
  lemma.hear("messageStringArr", new StringArrHandler());
}
void draw(){
  int currSecond = second();
  if(lastSecond != currSecond){
    lastSecond = currSecond;
    // Try to send an event
    if ( lemma.sendEvent("messageInt", messageInt) ){
      messageInt++;
    }
    if ( lemma.sendEvent("messageBool", messageBool) ){
      messageBool = !messageBool;
    }
    if ( lemma.sendEvent("messageFloat", messageFloat) ){
      messageFloat++;
    }
    if ( lemma.sendEvent("messageDouble", messageDouble) ){
      messageDouble++;
    }
    if ( lemma.sendEvent("messageString", messageString) ){
      messageString = (messageString == "Hello") ? "World" : "Hello";
    }
    
    if ( lemma.sendEvent("messageStringArr", messageStringArr) ){
      messageStringArr[0] = messageString;
    }
    if ( lemma.sendEvent("messageBoolArr", messageBoolArr) ){
      messageBoolArr[0] = messageBool;
    }
    if ( lemma.sendEvent("messageIntArr", messageIntArr) ){
      messageIntArr[0] = messageInt;
    }
    if ( lemma.sendEvent("messageFloatArr", messageFloatArr) ){
      messageFloatArr[0] = messageFloat;
    }
    if ( lemma.sendEvent("messageDoubleArr", messageDoubleArr) ){
      messageDoubleArr[0] = messageDouble;
    }
  }
  //connect and handle incoming events
  lemma.run();
}

//----------------------------------------------------------------------------------------------------------------//
// 3. Extend Lemma to implement EventHandler interface (Processing's Main sketch can't implement interfaces...)
//----------------------------------------------------------------------------------------------------------------//

class BoolHandler implements EventHandler {

  // Called each time we receive an event
  public void callback(Event event){
    System.out.println("Caught event : " + event.name + " : " + event.booleanValue);
  }
}

class IntHandler implements EventHandler {

  // Called each time we receive an event
  public void callback(Event event){
    System.out.println("Caught event : " + event.name + " : " + event.intValue);
  }
}

class FloatHandler implements EventHandler {

  // Called each time we receive an event
  public void callback(Event event){
    System.out.println("Caught event : " + event.name + " : " + event.floatValue);
  }
}

class DoubleHandler implements EventHandler {

  // Called each time we receive an event
  public void callback(Event event){
    System.out.println("Caught event : " + event.name + " : " + event.doubleValue);
  }
}

class StringHandler implements EventHandler {

  // Called each time we receive an event
  public void callback(Event event){
    System.out.println("Caught event : " + event.name + " : " + event.stringValue);
  }
}



class BoolArrHandler implements EventHandler {

  // Called each time we receive an event
  public void callback(Event event){
    System.out.print("Caught event : " + event.name);
    for(int i = 0; i < event.arraySize; i++){
       System.out.print(event.booleanArray[i] + ", ");
    }
    System.out.println("");
  }
}

class IntArrHandler implements EventHandler {

  // Called each time we receive an event
  public void callback(Event event){
    System.out.print("Caught event : " + event.name);
    for(int i = 0; i < event.arraySize; i++){
       System.out.print(event.intArray[i] + ", ");
    }
    System.out.println("");
  }
}

class FloatArrHandler implements EventHandler {

  // Called each time we receive an event
  public void callback(Event event){
    System.out.print("Caught event : " + event.name);
    for(int i = 0; i < event.arraySize; i++){
       System.out.print(event.floatArray[i] + ", ");
    }
    System.out.println("");
  }
}

class DoubleArrHandler implements EventHandler {

  // Called each time we receive an event
  public void callback(Event event){
    System.out.print("Caught event : " + event.name);
    for(int i = 0; i < event.arraySize; i++){
       System.out.print(event.doubleArray[i] + ", ");
    }
    System.out.println("");
  }
}

class StringArrHandler implements EventHandler {

  // Called each time we receive an event
  public void callback(Event event){
    System.out.print("Caught event : " + event.name);
    for(int i = 0; i < event.arraySize; i++){
       System.out.print(event.stringArray[i] + ", ");
    }
    System.out.println("");
  }
}
