//Copyright (c) 2014, IDEO 

/* -*- mode: java; c-basic-offset: 2; indent-tabs-mode: nil -*- */

/*
  TCPServer - basic network server implementation
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
 * A server sends and receives data to and from its associated clients 
 * (other programs connected to it). When a server is started, it begins 
 * listening for connections on the port specified by the <b>port</b> 
 * parameter. Computers have many ports for transferring data and some are 
 * commonly used so be sure to not select one of these. For example, web 
 * servers usually use port 80 and POP mail uses port 110.
 * 
 * @webref net
 * @usage application
 * @brief The server class is used to create server objects which send and receives data to and from its associated clients (other programs connected to it). 
 * @instanceName server  	any variable of type TCPServer
 */
public class TCPServer extends Thread {
  private Object parent;
  private Method serverEventMethod;

  private ServerSocket server;
  private int port;

  /** Number of clients currently connected. */
  private int clientCount;
  /** Array of client objects, useful length is determined by clientCount. */
  private TCPClient[] clients;


    /**
   * @param parent typically use "this"
   * @param port port used to transfer data
   */
  public TCPServer(Object parent, int port) {
    this.parent = parent;
    this.port = port;

    try {
      server = new ServerSocket(port);
      clients = new TCPClient[10];

      // reflection to check whether host applet has a call for
      // public void serverEvent(TCPServer s, TCPClient c);
      // which is called when a new guy connects
      try {
        serverEventMethod =
          parent.getClass().getMethod("serverEvent",
                                      new Class[] { TCPServer.class,
                                                    TCPClient.class });
      } catch (Exception e) {
        // no such method, or an error.. which is fine, just ignore
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public int getLocalPort(){
     return server.getLocalPort();
  }

  /**
   *
   * Disconnect a particular client.
   * 
   * @brief Disconnect a particular client.
   * @webref server:server
   * @param client the client to disconnect
   */
  private void disconnect(TCPClient client) {
    client.stop();
    int index = clientIndex(client);
    if (index != -1) {
      removeIndex(index);
    }
  }
  
  
  private void removeIndex(int index) {
    clientCount--;
    // shift down the remaining clients
    for (int i = index; i < clientCount; i++) {
      clients[i] = clients[i+1];
    }
    // mark last empty var for garbage collection
    clients[clientCount] = null;
  }
  
  
  private void addClient(TCPClient client) {
    if (clientCount == clients.length) {
        int newSize = clients.length + 10;
        Object temp = Array.newInstance(TCPClient.class, newSize);
        System.arraycopy(clients, 0, temp, 0,
                Math.min(Array.getLength(clients), newSize));
      clients = (TCPClient[]) temp;
    }
    clients[clientCount++] = client;
  }
  
  
  private int clientIndex(TCPClient client) {
    for (int i = 0; i < clientCount; i++) {
      if (clients[i] == client) {
        return i;
      }
    }
    return -1;
  }

  
  static public String ip() {
    try {
      return InetAddress.getLocalHost().getHostAddress();
    } catch (UnknownHostException e) {
      e.printStackTrace();
    }
    return null;
  }


  // the last index used for available. can't just cycle through
  // the clients in order from 0 each time, because if client 0 won't
  // shut up, then the rest of the clients will never be heard from.
  int lastAvailable = -1;

  /**
   *
   * Returns the next client in line with a new message.
   * 
   * @brief Returns the next client in line with a new message.
   * @webref server
   * @usage application
   */
  public TCPClient available() {
    synchronized (clients) {
      int index = lastAvailable + 1;
      if (index >= clientCount) index = 0;

      for (int i = 0; i < clientCount; i++) {
        int which = (index + i) % clientCount;
        TCPClient client = clients[which];
        if (client.available() > 0) {
          lastAvailable = which;
          return client;
        }
      }
    }
    return null;
  }

  @Override
  public void interrupt(){

    if (clients != null) {
      for (int i = 0; i < clientCount; i++) {
        disconnect(clients[i]);
      }
      clientCount = 0;
      clients = null;
    }

    try {
      if (server != null) {
        server.close();
        server = null;
      }
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
        super.interrupt();
    }
  }


  @Override
  public synchronized void run() {

    while (true) {
      try {
        Socket socket = server.accept();
        TCPClient client = new TCPClient(parent, socket);
        synchronized (clients) {
          addClient(client);
          if (serverEventMethod != null) {
            try {
              serverEventMethod.invoke(parent, new Object[] { this, client });
            } catch (Exception e) {
              System.err.println("Disabling serverEvent() for port " + port);
              e.printStackTrace();
              serverEventMethod = null;
            }
          }
        }
      } catch (Exception ex) {
        break;
      }
    }
  }



}
