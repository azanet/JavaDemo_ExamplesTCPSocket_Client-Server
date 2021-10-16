/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tcp_server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author David Freyre Muñoz
 */
public class TCP_Server {

    /**
     * @param args the command line arguments
     */
 public static void main(String[] args) {
           
        final ManagerSession ms= new ManagerSession();
        final int PORT = 51000;
        
        
        try {
            final ServerSocket serverSocket = new ServerSocket(PORT);
            
            
            while(1==1){
                
                final Socket socketClientIn = serverSocket.accept();
                
                System.out.println("Nuevo Cliente Conectado");
                
                //Lanzando Hilo para Sesion DE Cliente
                new Thread(new Session(socketClientIn, ms)).start();
                
            }
            
        } catch (IOException ex) {
            System.out.println("\n[!]ERROR AL LANZAR EL SERVER");
        }
    }
}
