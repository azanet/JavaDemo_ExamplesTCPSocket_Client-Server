package tcp_client_keepalive;

import Utils.ReadWriteUtils;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.ConnectException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 *
 * @author David Freyre Muñoz
 *
 *
 */
public class TCP_Client_KeepAlive {

//Creamos el Logger
    final static Logger LOG = Logger.getLogger("tcp_client_keepalive.Tcp_Client_Keepalive");
//Vinculando el Logger a un ARCHIVO "log" para que almacene las salidas que deseemos
//Creamos un Handler del archivo donde queremos Almacenar los mensajes
    static FileHandler fileTxt = null;// new FileHandler("Logging.txt");
//Para los TXT, debemos crear un "Formatter" para formatear el texto
    static SimpleFormatter formatterTxt = new SimpleFormatter();
////////////////////////////////////////////////////////////////////////////
    
    
     
        /*Haciendo para Obtener la Ruta (en cualquier SO) 
        / de donde tenemos que leer el Archivo .txt*/
        final static String nameJAR = TCP_Client_KeepAlive.class.getSimpleName()+".jar";
        final static String jarPath = (new File(TCP_Client_KeepAlive.class.getProtectionDomain().getCodeSource().getLocation().getPath())).toString().replaceAll(nameJAR, "");               
        //Ruta donde se debe encontrar el Archivo a LEER
        final static String routePath = jarPath;
    
    
    

    final static int PORT = 51000;
    final static String HOST = "localhost";
    private static Socket socket;
    //Lista de Objetos con la que trabaja el ejempo
    private static ArrayList<String> arrayList = new ArrayList<>();

    //Utilidad para LEER y ENVIAR "Streams" 
    private final static ReadWriteUtils rwUtils = new ReadWriteUtils();

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        System.out.println("\n\n\t\tDEMO\nEJECUTAR DESDE LA CONSOLA PARA SU CORRECTO FUNCIONAMIENTO\n\n");
        
        try {
            
            //Cuidado que el LOG se SOBREESCRIBE, 
            //establecer nombre nuevo(por ejemplo con fecha y hora) cada vez
            
            //Le aplicamos el "formatter" al "Handler"
            fileTxt = new FileHandler(routePath+"InformationLog.log");
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, ex.getMessage());
        } catch (Exception ex) {
            LOG.severe(ex.getMessage());
        }
    //Aplicamos formato al Handler   
    fileTxt.setFormatter (formatterTxt);
    //Y vinculamos el "Logger" al fichero
    LOG.addHandler(fileTxt);
    //Probando el "logger"

    LOG.info("PROBANDO EL LOGGERRRR");
    
        enviarMensajesAlServidor();

    }

    private static void enviarMensajesAlServidor() {
//Componemos el nombre del archivo a buscar,
        

        
        
        try {
            socket = new Socket(HOST, PORT);

            Scanner sc = new Scanner(System.in);
            String linea;

            while (true) {

                System.out.println("Escribe algo: ");

                linea = sc.nextLine();

                //Si el mensaje CONTIENE ALGO, lo Enviamos
                if (linea.length() > 0) {
                    //==> //////ENVIANDO -STRING(mensaje)- AL SERVIDOR///                  
                    rwUtils.writeMessage(socket.getOutputStream(), linea);
                    //==> //////ENVIANDO -FIN DE MENSAJE- AL SERVIDOR///(con una Linea vacía) 
                    rwUtils.writeMessage(socket.getOutputStream(), "");

                    System.out.println("\nESPERANDO Respuesta del SERVER:");
                    System.gc();
                    //==> //////LEYENDO/ESPERANDO -MESAJE COMPLETO- DEL SERVIDOR///         
                    System.out.println(rwUtils.readAllLines(socket.getInputStream()));

//
                    arrayList.add(linea);
                    //==> //////ENVIANDO -OBJETO- AL SERVIDOR///  Este, se agregará al ArrayList del servidor
                    //Pero solo si no existe, ya que alli nos encargamos de eso.
                    rwUtils.writeObject(socket.getOutputStream(), arrayList);

                    //==> //////RECIBIENDO -OBJETO- DEL SERVIDOR ///El servidor va a retornar su ArrayList
                    //y PERO Nos quedamos con los objetos NO REPETIDOS 
                    arrayList = (ArrayList<String>) rwUtils.readObject(socket.getInputStream());

                    //LEYENDO OBJETO RECIBIDO DEL SERVIDOR (en este caso es un ArrayList de STRING)
                    for (String string : arrayList) {
                        System.out.println("VALOR OBJETO:" + string + "\n");
                    }

                //SI el MENSAJE NO Contiene nada, alertamos    
                } else {
                    System.out.println("Mensaje VACIO, No se enviará AL SERVIDOR\n");
                }
                
    
                //Escribiendo TEXTO en el fichero
                System.out.println("agregando linea a FICHERO de TEXTO\n");
                rwUtils.writeTextInFile(routePath+"TEST.txt", linea + "\n");

                
                //Leyendo el fichero de TEXTO e imprimendo por pantalla
                System.out.println("LeyendoFicherO de TEXTO e IMPRIMIENDO desde el mismo metodo:");
                rwUtils.readFileAndPrint(routePath+"TEST.txt");

                //Leyendo archivo de TEXTO y devolviendo STRING del ARCHIVO 
                System.out.println("\nLEYENDO ARCHIVO, recuperando STRING e IMPRIMIENDOLA:");
                String texto = rwUtils.readFileToString(routePath+"TEST.txt") ;
                System.out.println(texto);
                System.out.println("FFFFIIINN de imprimiendo String recibida");

                //Leyendo archivo y devolviendo ArrayList del ARCHIVO 
                System.out.println("\nLEYENDO ARCHIVO, devolviendo ArrayList e IMPRIMIENDOLA:");
                ArrayList<String> arrList = rwUtils.readFileToArrayList(routePath+"TEST.txt");
                for (String string : arrList) {
                    System.out.println(string);
                }
                System.out.println("FFFFIIIMMM DE devolviendo ArrayList\n");

                System.out.println("Añadiendo datos de \""+routePath+"TEST.txt"+"\" a \""+routePath+"TEST.txt_ADDED\"");
       //Agregando contenido de un fichero a otro fichero
                rwUtils.addContentFROMFileIN_ToFileOUT(routePath+"TEST.txt", routePath+"TEST.txt_ADDED");
               System.out.println("Datos AÑADIDOS con EXITO.\n\n");
               
               //Haciendo copia Exacta de un archivo
                 System.out.println("Realizando copia binaria de \""+routePath+"TEST.txt"+"\" a \""+routePath+"TEST.txt_COPY\"");
                rwUtils.copyFile(routePath+"TEST.txt", routePath+"TEST.txt_COPY");
                System.out.println("Copia Realizada con EXITO.\n\n");

           //Se necesita la RUTA COMPLETA DEL ARCHIVO a Descargar
                System.out.println("Solicitando y descargando archivo desde el SERVIDOR");
                System.out.println("INTRODUZCA RUTA Y NOMBRE DE ARCHIVO A SOLICITAR:");
                 
              String pathFileRequest =routePath+"TEST.txt";
 
            
                System.out.println("INTRODUZCA RUTA Y NOMBRE para ARCHIVO DESCARGADO");
               String pathFileDownloaded = routePath+"TEST_DOWNLOAD.txt";
         
                //Solicitando y Descargando Archivo
                rwUtils.requestAndDownloadFile(socket.getOutputStream(), pathFileRequest, socket.getInputStream(), pathFileDownloaded);
                System.out.println("Archivo DESCARGADo SATISFACTORIAMENTE!!");

            //Escribiendo ArrayList(OBJETO) en un ARCHIVO(binario)
                System.out.println("ESCRIBIENDO ARRAYLIST EN EL FICHERO");
                rwUtils.writeUniqueObjectInFile(routePath+"UniqueObject.bin", arrayList);
                System.out.println("\nESCRITO CORRECTAMENTE OBJETO EN EL FICHERO\n");

            //Leyendo ARCHIVO de Objeto UNICO
                System.out.println("\nLeyendo OBJETO de ARCHIVO de 'Objeto UNICO'\n");
                //OBTENIENDO ARRAYLIST DE OBJETOS CONTENIDOS EN EL FICHERO
                ArrayList<String> recoveryUniqueObject = (ArrayList<String>) rwUtils.readUniqueObjectFromFILE(routePath+"UniqueObject.bin");
                for (String string : recoveryUniqueObject) {
                    System.out.println(string);
                }
                System.out.println("\nFFFIIINN  DE Leyendo OBJETO de ARCHIVO de Objeto UNICO\n");

                //Escribiendo ArrayList(OBJETO) en un ARCHIVO(binario) que contiene varios Objetos
                System.out.println("AGREGANDO ARRAYLIST EN EL FICHERO de MULTIPLES OBJETOS");
                rwUtils.writeSeveralObjectInFile(routePath+"OBJETOS.bin", arrayList);
                System.out.println("\nAGREGADO CORRECTAMENTE OBJETO EN EL FICHERO\n");

        //////////        
             //OBTENIENDO ARRAYLIST DE OBJETOS CONTENIDOS EN EL FICHERO
                System.out.println("\nLEYENDO ARRAY de MULTIPLES OBJETOS DEL FICHERO con VARIOS OBJETOS\n");             
                ArrayList<Object> recoveryObject = (ArrayList<Object>) rwUtils.readSeveralObjectFromFILE(routePath+"OBJETOS.bin");
                System.out.println("RECUPERANDO e IPRIMIENDO  OBJETOS deL FICHERO:");
                long count = 0L;
                //Recorremos el ArrayList de Objetos RECUPERADO
                for (Object arobj : recoveryObject) {
                    count++;
                    System.out.println("\nOBJETO LEIDO=" + count + "\nIntentando 'Castear'...");
                    //Recuperando Objeto del INTERIOR ArrayList Devuelto, esto podria ser, "Persona" por ejemplo
                    //En este caso es un ArrayList de String
                    //Casteando objeto Contenido
                    ArrayList<String> objContenido = (ArrayList<String>) arobj;

                    System.out.println("Imprimiendo objeto Contenido en el ArrayList devuelto por el metodo");
                    for (String string : objContenido) {
        //                System.out.println("propiedad del objeto de la lista Embebida");
                        System.out.println(string);
                    }
        //            System.out.println("\n***BJETO LEIDO correctamente**");
                }
        ///////////////        
                
                
            }//Fin del WHILE - TRUE

        } catch (ConnectException ce) {
            System.out.println("\n[!]ERROR,Conexion con SERVER PERDIDA\n" + ce.getMessage());
        } catch (NullPointerException ex) {

            System.out.println("\n[!]ERROR,POSOBLE ERROR de CONEXION Con EL SERVIDOR\n" + ex.getMessage());

        } catch (IOException ex) {
            Logger.getLogger(TCP_Client_KeepAlive.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                socket.close();
            } catch (IOException ex) {
                Logger.getLogger(TCP_Client_KeepAlive.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
