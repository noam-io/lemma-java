## Max Java External Lemma Library
------------------------

A thin wrapper around the Java library creating a [Noam](https://github.com/ideo/noam) bridge for use in [Max](http://cycling74.com/products/max/) patches 

#### Build 


        mvn clean package
        
#### Install 
====================


Unzip target/max-external-bin.zip 
       
**Contents: **

	README.md (this file)
	examples
       (contains example patches for max demonstrating the lemma library)
	lib
	   (contains jars that need to be Max's classpath for the lemma to work)

###### Configuring Max to use the Lemma library

Set up Max's classpath to find your Java externals.

Go to the file [Max installation]/Cycling '74/java/max.java.config.txt.
Edit the file and find the line:

       ;max.dynamic.jar.dir /Users/topher/myjars     ;

Uncomment the line and set it to the lib directory where you unzipped this file.
For Example:

       max.dynamic.jar.dir [max lemma installation]/lib

Note: you need to restart Max after making changes to this file.


#### Using (See example patch)
====================


Create an instance of MaxLemma by dragging the "Object" box into your sketch.

Enter "mxj lemma.library.max.MaxLemma"

**Defaults:** 

* lemma name used during registration and message sending: **max-lemma**
* list of events lemma listens for: **\<empty list>**

Defaults can be overridden by passing to the max objects as attributes.

* @LEMMA_NAME <String/Symbol>
* @HEARS [<list of Strings / Symbols>]

**Example:**

      mxj lemma.library.max.MaxLemma @LEMMA_NAME myMaxApp @HEARS messageOne messageTwo

Send a "begin" message to inlet(0) to connect to noam and start listening.

Events received from Noam will be broadcast as pairs of strings from outlet(0) as they are received.

Send a "sendEvent" message to inlet(0) with the event name and value as arguments

Send a "stop" message to inlet (0) to temporarily stop the connection.

All attributes can be dynamically set by sending "<attribute_name> <argument messages>" to inlet (0) and restarting the
MaxLemma with the "stop" and "begin" messages.

**Example:**
Send
  HEARS foo bar
to the inlet to reset the messages the lemma is listening to to "foo" and "bar"
This will take effect once a "stop" and "begin" are then sent to the lemma.


