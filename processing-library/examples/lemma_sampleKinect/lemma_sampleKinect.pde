// Copyright (c) 2014, IDEO

//------------------------------------------------------------------------------------------------//
// 1. Import core library + Event (Helps IDE parse "EventHandler.Callback(Event event)" below)
//------------------------------------------------------------------------------------------------//

import lemma.library.Event;
import lemma.library.EventHandler;
import lemma.library.Lemma;
import org.json.JSONArray;

//------------------------------------------------------------------------------------------------//
// 2. Instance one or more Lemmas. Construct, setup listeners, run in loop.
//------------------------------------------------------------------------------------------------//

Lemma lemma;
String LEMMA_NAME = "lemmaID";
String ROOM_NAME = "";

//------------------------------------------------------------------------------------------------//
// 3. Set up the variables we need for the program.
//------------------------------------------------------------------------------------------------//

// Have we gotten data before?
boolean startingDrawing = false;

// Width and Height of the data
int drawingHeight = 800;
int drawingWidth = 800;
int maxWidth = 0;
int maxHeight = 0;

// Data Store for storing the image data and drawing objects
LargePixel [][] imageData = null;

// This sets up the imageData array with the right size.
// We wait until our first message to do this so we know
// how big the messages are.
void setupBlankImageData(){
  imageData = new LargePixel[maxWidth][maxHeight];
  
  int HSize = drawingHeight / (maxHeight + 2);
  int WSize = drawingWidth / (maxHeight + 2);
  
  for(int x = 0; x < maxWidth; x++){
    for(int y = 0; y < maxHeight; y++){
     imageData[x][y] = new LargePixel(x * (WSize+2), y * (HSize+2), WSize, HSize);  
    }
  } 
  startingDrawing = true;
}

// Setup the application for drawing.
void setup(){
  // Setup Drawing Area
  size(drawingWidth, drawingHeight);

  // Create Lemma with the lemma's name and the room name
  lemma = new Lemma(this, LEMMA_NAME, ROOM_NAME);
  
  // Listen for an Event - this is how we get the Kinect data
  lemma.hear("presenceData", new MyEventHandler());
}

// Draw - this is called in a loop over and over again.
void draw(){
  background(0);
  if(startingDrawing){
    for(int x = 0; x < maxWidth; x++){
      for(int y = 0; y < maxHeight; y++){
        imageData[x][y].display();
      }
    }
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
    if(event.objectValue instanceof Object[]){
      Object[] values = (Object[])event.objectValue;
      maxWidth = values.length;
      for(int i = 0; i <maxWidth; i++){
        JSONArray arr = (JSONArray)values[i];
        maxHeight = arr.length();
        if(imageData == null){
          setupBlankImageData();
        }
        for(int j = 0; j < maxHeight; j++){
          imageData[i][j].setValue(arr.getInt(j));
        }
      }
    }
    System.out.println("Caught event : " + event.name + " : " + event.stringValue);
  }
}


// Class to represent a single large pixel
class LargePixel {
 
  float x, y;
  float w, h;
  float value;
 
  LargePixel(int tempX, int tempY, int tempW, int tempH){
   x = tempX;
   y = tempY;
   w = tempW;
   h = tempH;
   value = 0;
  } 
  
  void setValue(int val){
    value = val; 
  }
   
  void display(){
    stroke(255);
    fill(255 * value);
    rect(x, y, w, h);
  } 
  
}
