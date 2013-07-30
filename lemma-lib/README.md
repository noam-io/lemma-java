## Lemma Library
------------------------

A [Java](http://www.java.com/) library for interacting with [Noam](https://github.com/ideo/noam)

#### Build

        mvn clean package

#### Install

Unzip target/lemma-lib-bin.zip

       lemma-lib.jar
       lib
       	 json.jar
       examples

Make sure lemma-lib.jar and json.jar are in your classpath.

#### Example

```java
package lemma.library.example;

import lemma.library.Event;
import lemma.library.EventHandler;
import lemma.library.Lemma;


public class LemmaExample {

    public static void main(String[] args) throws Exception {
        LemmaExample example = new LemmaExample();
        //Create and configure lemma
        Lemma lemma= new Lemma(example, "my-lemma", 1030, 6083);
        //listen for event snd define callback
        lemma.hear("myEvent",new EventHandler() {
            @Override
            public void callback(Event event) {
                System.out.println("I just heard the '"+ event.name + "' event with a value of '" + event.stringValue);
            }
        });

        //optionally wait for connection to noam so you don't miss sending any messages
        while (!lemma.connected())   {
            lemma.run();
            Thread.sleep(10);
        }

        //publish events and listen in loop
        int count = 1;
        while(true){
            lemma.run();
            lemma.sendEvent("myEvent", "Event #" + count);
            count++;
            Thread.sleep(200);
        }
    }

}
```
