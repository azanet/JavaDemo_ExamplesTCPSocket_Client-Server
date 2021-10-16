/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tcp_server;

import java.net.Socket;

/**
 *
 * @author David Freyre Muñoz
 */
public class Session implements Runnable {

    private final Socket socket;
    private final ManagerSession sessionManager;
    
    
    public Session(Socket socket, ManagerSession sessionManager) {
        this.socket = socket;
        this.sessionManager = sessionManager;
    }
    
    
    @Override
    public void run() {
        
        sessionManager.init(socket);
  
    }
    
    
    
}
