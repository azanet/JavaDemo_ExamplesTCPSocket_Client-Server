
package tcp_client_oneshot;

import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author David Freyre Muñoz
 */
public class TCP_Client_OneShot {

    final static int PORT = 51000;
    final static String HOST = "localhost";
    
    private static Socket socket;
    private static ArrayList<String> arrayList = new ArrayList<>();
    
    //Utilidad para LEER y ENVIAR "Streams" 
    private final static ReadWriteUtils rwUtils = new ReadWriteUtils();

    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        enviarMensajesAlServidor();

    }

    private static void enviarMensajesAlServidor() {

           try {
     
            Scanner sc = new Scanner(System.in);
            String linea;
     
            while (true) {
       
                //Recibiendo Entrada del USUARIO
                System.out.println("Escribe algo: ");
                linea = sc.nextLine();
                
        
                //Si el mensaje CONTIENE ALGO, lo Enviamos
                if (linea.length() > 0) {
                    
                    //INICIAMOS un SOCKET NUEVO cada VEZ 
                    socket = new Socket(HOST, PORT); 
                    
    //==> //////ENVIANDO -STRING(mensaje)- AL SERVIDOR///                  
                    rwUtils.writeMessage(socket.getOutputStream(), linea);
    //==> //////ENVIANDO -FIN DE MENSAJE- AL SERVIDOR///(Linea vacía) 
                   //FINALIZANDO envio del mensaje, ENVIANDO una "Linea VACIA"
                    rwUtils.writeMessage(socket.getOutputStream(), "");

                    //         System.out.println("EnviaDA DATA al SERVER: ");
                    System.out.println("\nESPERANDO Respuesta del SERVER:");

    //==> //////LEYENDO/ESPERANDO -MESAJE COMPLETO- DEL SERVIDOR///         
                    System.out.print(rwUtils.readAllLines(socket.getInputStream()));


                    arrayList.add(linea);
    //==> //////ENVIANDO -OBJETO- AL SERVIDOR///            
                    rwUtils.writeObject(socket.getOutputStream(), arrayList);

                    try {
                        
    //==> //////RECIBIENDO -OBJETO- DEL SERVIDOR ///
                        //Nos quedamos con los NO REPETIDOS
                        arrayList = (ArrayList<String>) rwUtils.readObject(socket.getInputStream());
                        
                        //LEYENDO OBJETO RECIBIDO DEL SERVIDOR
                        for (String string : arrayList) {
                            System.out.println("VALOR OBJETO:" + string + "\n");
                        }

                    } catch (ClassNotFoundException ex) {
                        System.out.println("\n[!]ERROR, No se pudo CASTEAR el OBJETO");
                    }
                    
                //CERRAMOS EL SOCKET
                socket.close(); 
                 
                //SI el MENSAJE NO Contiene nada, alertamos    
                } else {
                    System.out.println("Mensaje VACIO, No se enviará AL SERVIDOR\n");
                }

              
            }//Fin del WHILE - TRUE

            
            
        } catch (ConnectException ce) {
            System.out.println("\n[!]ERROR,Conexion con SERVER PERDIDA");
        } catch (IOException | NullPointerException ex) {

            System.out.println("\n[!]ERROR,POSOBLE ERROR de CONEXION Con EL SERVIDOR\n" + ex.getMessage());

        } finally {
            try {
                socket.close();
            } catch (IOException ex) {
                Logger.getLogger(TCP_Client_OneShot.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
