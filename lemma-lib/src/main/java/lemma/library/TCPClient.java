//Copyright (c) 2014, IDEO 

/* -*- mode: java; c-basic-offset: 2; indent-tabs-mode: nil -*- */

/*
  TCPClient - basic network client implementation
  Part of the Processing project - http://processing.org

  Copyright (c) 2004-2007 Ben Fry and Casey Reas
  The previous version of this code was developed by Hernando Barragan

  Modified to remove processing dependencies 2013-7-24

  This library is free software; you can redistribute it and/or
  modify it under the terms of the GNU Lesser General Public
  License as published by the Free Software Foundation; either
  version 2.1 of the License, or (at your option) any later version.

  This library is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
  Lesser General Public License for more details.

  You should have received a copy of the GNU Lesser General
  Public License along with this library; if not, write to the
  Free Software Foundation, Inc., 59 Temple Place, Suite 330,
  Boston, MA  02111-1307  USA
*/

package lemma.library;

import java.io.*;
import java.lang.reflect.*;
import java.net.*;

/**
   *
   * A client connects to a server and sends data back and forth. If anything 
   * goes wrong with the connection, for example the host is not there or is 
   * listening on a different port, an exception is thrown.
   * 
 * @webref net
 * @brief The client class is used to create client Objects which connect to a server to exchange data. 
 * @instanceName client any variable of type TCPClient
 * @usage Application
 * @see_external LIB_net/clientEvent
 */
public class TCPClient implements Runnable, ITCPClient {
  Object parent;
  Method clientEventMethod;
  Method disconnectEventMethod;

  Thread thread;
  Socket socket;
  String ip;
  int port;
  String host;

  public InputStream input;
  public OutputStream output;

  byte buffer[] = new byte[32768];
  int bufferIndex;
  int bufferLast;

  
  /**
   * @param parent typically use "this"
   * @param host address of the server
   * @param port port to read/write from on the server
   */
  public TCPClient(Object parent, String host, int port) {
    this.parent = parent;
    this.host = host;
    this.port = port;

    try {
      socket = new Socket(this.host, this.port);
      input = socket.getInputStream();
      output = socket.getOutputStream();

      thread = new Thread(this);
      thread.start();

      // reflection to check whether owner has a call for
      // public void clientEvent(lemma.library.TCPClient)
      // which would be called each time an event comes in
      try {
        clientEventMethod =
          parent.getClass().getMethod("clientEvent",
                                      new Class[] { TCPClient.class });
      } catch (Exception e) {
        // no such method, or an error.. which is fine, just ignore
      }
      // do the same for disconnectEvent(TCPClient c);
      try {
        disconnectEventMethod =
          parent.getClass().getMethod("disconnectEvent",
                                      new Class[] { TCPClient.class });
      } catch (Exception e) {
        // no such method, or an error.. which is fine, just ignore
      }

    } catch (ConnectException ce) {
      ce.printStackTrace();
      dispose();

    } catch (IOException e) {
      e.printStackTrace();
      dispose();
    }
  }

  
  /**
   * @param socket any object of type Socket
   * @throws IOException
   */
  public TCPClient(Object parent, Socket socket) throws IOException {
    this.socket = socket;

    input = socket.getInputStream();
    output = socket.getOutputStream();

    thread = new Thread(this);
    thread.start();
  }


  /**
   *
   * Disconnects from the server. Use to shut the connection when you're 
   * finished with the TCPClient.
   * 
   * @webref client:client
   * @brief Disconnects from the server
   * @usage application
   */
  public void stop() {    
    if (disconnectEventMethod != null) {
      try {
        disconnectEventMethod.invoke(parent, new Object[] { this });
      } catch (Exception e) {
        e.printStackTrace();
        disconnectEventMethod = null;
      }
    }
    dispose();
  }


  /**
   * Disconnect from the server: internal use only.
   *
   * This should only be called by the internal functions ,
   * use stop() instead from within your own app.
   */
  public void dispose() {
    thread = null;
    try {
      if (input != null) {
        input.close();
        input = null;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    try {
      if (output != null) {
        output.close();
        output = null;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    
    try {
      if (socket != null) {
        socket.close();
        socket = null;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }


  public void run() {
    while (Thread.currentThread() == thread) {
      try {
        while (input != null) {
          int value;

          // try to read a byte using a blocking read. 
          try {
            value = input.read();
          } catch (SocketException e) {
             // the socket had a problem reading so don't try to read from it again.
             stop();
             return;
          }
        
          // read returns -1 if end-of-stream occurs (for example if the host disappears)
          if (value == -1) {
            System.err.println("TCPClient got end-of-stream.");
            stop();
            return;
          }

          synchronized (buffer) {
            // todo: at some point buffer should stop increasing in size, 
            // otherwise it could use up all the memory.
            if (bufferLast == buffer.length) {
              byte temp[] = new byte[bufferLast << 1];
              System.arraycopy(buffer, 0, temp, 0, bufferLast);
              buffer = temp;
            }
            buffer[bufferLast++] = (byte)value;
          }

          // now post an event
          if (clientEventMethod != null) {
            try {
              clientEventMethod.invoke(parent, new Object[] { this });
             } catch (Exception e) {
               System.err.println("error, disabling clientEvent() for " + host);
               e.printStackTrace();
               clientEventMethod = null;
            }
          }
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }


  /**
   * Return true if this client is still active and hasn't run
   * into any trouble.
   */
  public boolean active() {
    return (thread != null);
  }

  /**
   *
   * Returns the IP address of the computer to which the TCPClient is attached.
   * 
   * @webref client:client
   * @usage application
   * @brief Returns the IP address of the machine as a String
   */
  public String ip() {
    return socket.getInetAddress().getHostAddress();
  }


  /**
   *
   * Returns the number of bytes available. When any client has bytes 
   * available from the server, it returns the number of bytes.
   * 
   * @webref client:client
   * @usage application
   * @brief Returns the number of bytes in the buffer waiting to be read
   */
  public int available() {
    return (bufferLast - bufferIndex);
  }


  /**
   *
   * Empty the buffer, removes all the data stored there.
   * 
   * @webref client:client
   * @usage application
   * @brief Clears the buffer
   */
  public void clear() {
    bufferLast = 0;
    bufferIndex = 0;
  }

  /**
   * <h3>Advanced</h3>
   * Grab whatever is in the serial buffer, and stuff it into a
   * byte buffer passed in by the user. This is more memory/time
   * efficient than readBytes() returning a byte[] array.
   *
   * Returns an int for how many bytes were read. If more bytes
   * are available than can fit into the byte array, only those
   * that will fit are read.
   * 
   * @param bytebuffer passed in byte array to be altered
   */
  public int readBytes(byte bytebuffer[]) {
    if (bufferIndex == bufferLast) return 0;

    synchronized (buffer) {
      int length = bufferLast - bufferIndex;
      if (length > bytebuffer.length) length = bytebuffer.length;
      System.arraycopy(buffer, bufferIndex, bytebuffer, 0, length);

      bufferIndex += length;
      if (bufferIndex == bufferLast) {
        bufferIndex = 0;  // rewind
        bufferLast = 0;
      }
      return length;
    }
  }

  public void write(byte data[]) {
    try {
      output.write(data);
      output.flush();   // hmm, not sure if a good idea

    } catch (Exception e) { // null pointer or serial port dead
      e.printStackTrace();
      stop();
    }
  }


  /**
   * <h3>Advanced</h3>
   * Write a String to the output. Note that this doesn't account
   * for Unicode (two bytes per char), nor will it send UTF8
   * characters.. It assumes that you mean to send a byte buffer
   * (most often the case for networking and serial i/o) and
   * will only use the bottom 8 bits of each char in the string.
   * (Meaning that internally it uses String.getBytes)
   *
   * If you want to move Unicode data, you can first convert the
   * String to a byte stream in the representation of your choice
   * (i.e. UTF8 or two-byte Unicode data), and send it as a byte array.
   */
  public void write(String data) {
    write(data.getBytes());
  }
}
