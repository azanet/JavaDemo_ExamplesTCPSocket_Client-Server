
package tcp_server;

import Utils.ReadWriteUtils;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.Socket;
import java.util.ArrayList;

/**
 *
 * @author David Freyre Muñoz
 */
public class ManagerSession {
    //ArrayList que será compartido para TODOS LOS CLIENTES
    private ArrayList<String> arrayList=new ArrayList<>(); 
    
    //Clase de Utilidades para operar con flujos E/S y archivos
    private ReadWriteUtils rwUtils = new ReadWriteUtils();

    public ManagerSession() {
  
    }


    
    public void init(Socket sk) {
        
        boolean killswitch = true;
        final Socket socketClientIn = sk;
        
        try {

            Inet4Address ip = (Inet4Address) socketClientIn.getInetAddress();
            String laIP = ip.getHostAddress();

     
            while (killswitch) {

  
                System.out.println("Esperando Mesaje del CLIENTE:");
                String message = "";

    //==> //////LEYENDO/ESPERANDO -MESAJE COMPLETO- DEL CLIENTE///   
                message = rwUtils.readAllLines(socketClientIn.getInputStream());

                System.out.println(laIP + ": " + message);

    //==> //////ENVIANDO -STRING(mensaje)- AL CLIENTE/// 
                String a = ("Recivido en SRVR: " + laIP + ": " + message);
                rwUtils.writeMessage(socketClientIn.getOutputStream(), a);
                
    //==> //////ENVIANDO -FIN DE MENSAJE- AL CLIENTE///(Linea vacía)           
                rwUtils.writeMessage(socketClientIn.getOutputStream(), ""); //No es necesario ("message" ya contiene un salto de linea al final)

                //ENVIANDO FIN DEL MENSAJE
                System.out.println("MENSAJE RETORNADO AL CLIENTE: " + laIP + ": " + message);

                
          
                    
    //==> //////RECIBIENDO -OBJETO- DEL SERVIDOR /// (Nos quedamos con los NO REPETIDOS ))
                        ArrayList<String> arrayListRECEIVED = (ArrayList<String>) rwUtils.readObject(socketClientIn.getInputStream());
                        //ELIMINANOS OBJETOS REPETIDOS
                        arrayListRECEIVED.removeAll(arrayList);
                        //AGREGAMOS LOS "NUEVOS OBJETOS" a NUESTRO ARRAYLIST
                        arrayList.addAll(arrayListRECEIVED);
                    
                    //LEEMOS EL OBJETO
                    for (String string : arrayList) {
                        System.out.println("VALOR OBJETO:" + string + "\n");
                    }

           
    //==> //////ENVIANDO -OBJETO- AL CLIENTE///
                    rwUtils.writeObject(socketClientIn.getOutputStream(), arrayList);



                System.out.println("\n\nEsperando la SOLICUTUD del Fichero a Descargar:");
         rwUtils.sendRequestedFile(socketClientIn.getInputStream(), socketClientIn.getOutputStream());
                System.out.println("Fichero Enviado Satisfactoriamente!!\n\n");
            }

        }catch (NullPointerException npe) {
            System.out.println("\n[!]ERROR, el CLIENTE se ha DESCONECTADO");

        } catch (IOException ex) {
            System.out.println(ex.getMessage());

        } finally {
            try {
                socketClientIn.close();
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
            System.out.println("Hilo: ["+Thread.currentThread().getName()+"] FINALIZADO!");
        }

    }

}
