## Lemma Processing Library
------------------------

A packaged library for [Processing](http://processing.org/) wrapping the Java library

#### Build 

        mvn clean package
        
#### Install 

Unzip target/processing-lemma-lib-bin.zip

Copy the contents into the libraries folder of the Processing sketchbook location on your computer. You will need to create the libraries folder if this is your first contributed library.

By default the following locations are used for your sketchbook folder. For Mac users the sketchbook folder is located inside ~/Documents/Processing. After the install you should see a structure similar to 

       ~/Documents/Processing
         libraries
           examples
             example1
           	    example1.ino 
           	 ... 
           library
             lemma.jar
             json.jar
             …
#### Example Sketch

```java
// 1. Import core library + Event 
//(Helps IDE parse "EventHandler.Callback(Event event)" below)


import lemma.library.Event;
import lemma.library.EventHandler;
import lemma.library.Lemma;


// 2. Instance one or more Lemmas. 
// Construct, setup listeners, run in loop.


Lemma lemma;
int messagesSent = 1;

void setup(){
  lemma = new Lemma(this, "test", 1030, 8833);
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


// 3. Extend Lemma to implement EventHandler interface
// (Processing's Main sketch can't implement interfaces...)

class MyEventHandler implements EventHandler {

  // Called each time we receive an event
  public void callback(Event event){
    System.out.println("Caught event : " + event.name + " : " + event.stringValue);
  }
}
```

