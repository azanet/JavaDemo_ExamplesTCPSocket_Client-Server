package tcp_client_oneshot;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.nio.channels.FileLock;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 *
 * @author David Freyre Muñoz <https://github.com/azanet>
 *
 * Clase para trabajar con "InputStream y OutputStream" y Sockets más facilmente
 * Tambien para Ficheros.
 *
 *
 * Cuenta con los siguientes METODOS;
 * 
 * ----------------------------------------------------------------------------
 *  OPERACIONES CON FICHEROS
 * ----------------------------------------------------------------------------
 * --readFileToString               =====> Lee un archivo y lo retorna todo en una String
 * --readFileToArrayList            =====> Lee un archivo y Retorna un ArrayList con las lineas Leidas 
 * --readFileAndPrint               =====> Lee un archivo y lo imprime por pantalla
 * --readFileObjectToObjectArrayList ====> (Lee archivo binario de objetos, 
 *                                           y retorna un ArrayList con todos los objetos)
 * --writeObjectInFile              =====> Escribir OBJETO en un Archivo
 * --writeTextInFile                =====> Escribir TEXTO a un archivo
 * --copyBinaryFileToFile           =====> Realiza una copia binaria de un archivo (a otro)
 * 

 * 
 *          -------PARA TRABAJAR CON SOCKETS (Mas que nada)------------------
 * ---------------------------------------------------------------------------- 
 *   OBTENER STRINGS de InputStream - ENVIAR Bytes de STRINGS por OutputStream
 * ---------------------------------------------------------------------------- 
 * --readAllLines                   =====> Lee TODAS las Lineas Hasta Recibir UNA Linea VACIA ("") 
 * --readOneLine                    =====> Lee UNA Linea 
 * --writeMessage                   =====> Envia un Mensaje(STRING)
 * 
 * ----------------------------------------------------------------------------
 *    OBTENER OBJETOS desde el InputStream - ENVIAR OBJETOS por el OutputStream 
 * ----------------------------------------------------------------------------
 * --readObject                     =====> Lee un OBJETO 
 * --writeObject                    =====> Envia un Objeto
 * 
 * ----------------------------------------------------------------------------
 *          DESCARGA y ENVIO de FICHEROS
 * ----------------------------------------------------------------------------
 * --requestAndDownloadFile         =====> Solicitar y descargar un archivo del Servidor
 * --sendRequestedFile              =====> Enviar Archivo Solicitado por el Cliente
 * --OBJFfileSubmission             =====> Clase usada para la transferencia 
 * --OBJFileRequest                 =====> Clase usada para la transferencia 
 *
 *
 */
public class ReadWriteUtils {

//================================================================================================================================= 
//Lee Flujo de BYTES y devuelve MENSAJE CARACTERES Codificado en UTF8
    /**
     * Este método RECIBE un InputStream, Leerá la entrada de BYTES
     * proporcionada por el InputStream y DEVOLVERÁ una STRING con TODO el
     * MENSAJE codificada en UTF8.
     *
     * NECESITA RECIBIR UNA LINEA VACÍA("") PARA DETERMINAR QUE ES EL FINAL. Si
     * no, se quedará esperando sin retornar
     *
     * SE PUEDE cambiar "END_OF_MESSAGE" para determinar EL FIN DEL MENSAJE.
     *
     * @param inputStream ====> InputStream del Socket o Consola
     *
     * @return message ========> RETORNA una string con TODO el MENSAJE recibido
     *
     * @throws java.io.UnsupportedEncodingException
     */
    //Leer MENSAJES en BYTES y devuelve CARACTERES Codificado en UTF8
    public String readAllLines(InputStream inputStream) throws UnsupportedEncodingException, IOException, NullPointerException {

        //VARIABLE que Determina el "FIN DEL MENSAJE"
        String END_OF_MESSAGE = "";
        String message = "";

        BufferedReader readerMessage = null;

        try {
            //Leyendo BYTES y Devolviendo STRING
            readerMessage = new BufferedReader(new InputStreamReader(inputStream, "UTF8"));

            //Leyendo MENSAJE que exista en la "Entada de datos"  
            //Parará de leer cuando reciba una linea VACIA "";    
            String aux = readerMessage.readLine();

            while (!(aux.equals(END_OF_MESSAGE))) {

                message += aux;

                //vOLVEMOS A Leer, Y SI Existe Otra línea, AGREGAMOS UN SALTO DE LINEA.
                if (!(aux = readerMessage.readLine()).equals(END_OF_MESSAGE)) {

                    message += "\n";
                }

            }

        } catch (NullPointerException npe) {
            throw new NullPointerException("\n[!]ERROR Null Pointer Exception\n--InputStream NULL");

        } catch (UnsupportedEncodingException use) {

            throw new UnsupportedEncodingException("\n[!]Error en la CODIFICACIÓN:\n--" + use.getMessage());

        } catch (IOException ex) {
            throw new IOException("\n[!]ERROR en la Entrada de Datos:\n--" + ex.getMessage());

        } finally {
            //Cerrando Lector (No se puede cerrar porque mataría el SOCKET)
            //  readerMessage.close();
            readerMessage = null;
        }

        //RETORNANDO MENSAJE RECIBIDO
        return message;
    }//Fin readAllLines

    //================================================================================================================================= 
//Lee Flujo de BYTES y devuelve MENSAJE CARACTERES Codificado en UTF8
    /**
     * Este método RECIBE un InputStream, Leerá la entrada de BYTES
     * proporcionada por el InputStream y DEVOLVERÁ una STRING codificada en
     * UTF8
     *
     * LEERA UNA SOLA LINEA
     *
     * @param inputStream ====> InputStream del Socket o Consola
     *
     * @return message ========> RETORNA una string con TODO el MENSAJE recibido
     *
     * @throws java.io.UnsupportedEncodingException
     */
    //Leer MENSAJES en BYTES y devuelve CARACTERES Codificado en UTF8
    public String readOneLine(InputStream inputStream) throws UnsupportedEncodingException, IOException, NullPointerException {

        String message = "";

        BufferedReader readerMessage = null;

        try {
            //Leyendo BYTES y Devolviendo STRING
            readerMessage = new BufferedReader(new InputStreamReader(inputStream, "UTF8"));

            //Leyendo UNA sola Linea
            message = readerMessage.readLine();

        } catch (NullPointerException npe) {
            throw new NullPointerException("\n[!]ERROR Null Pointer Exception\n--InputStream NULL");

        } catch (UnsupportedEncodingException use) {

            throw new UnsupportedEncodingException("\n[!]Error en la CODIFICACIÓN:\n--" + use.getMessage());

        } catch (IOException ex) {
            throw new IOException("\n[!]ERROR en la Entrada de Datos:\n--" + ex.getMessage());

        } finally {
            //Cerrando Lector (No se puede cerrar porque mataría el SOCKET)
            //  readerMessage.close();
            readerMessage = null;
        }

        //RETORNANDO MENSAJE RECIBIDO
        return message;
    }//Fin readONELine

//================================================================================================================================= 
//Escribir-Enviar STRING en BYTES 
    /**
     * Este método recibe un OutputStream, Y una STRING(mensaje).
     *
     * Recibe la entrada en STRING(codificada un utf8) proporcionada por el
     * InputStream y enviará una cadena de BYTES.
     *
     * @param outputStream ====> OutputStream del Socket o Consola
     * @param message =========> Mensaje que se necesita ENVIAR.
     *
     * @throws java.io.UnsupportedEncodingException
     */
    public void writeMessage(OutputStream outputStream, String message) throws UnsupportedEncodingException, IOException, NullPointerException {

        BufferedWriter writeMessage;

        try {

            writeMessage = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF8"));

            if (message.length() > 0) {
                writeMessage.write(message);
            }

            writeMessage.newLine(); //O Tambien se puede enviar un salto de linea  "writeMessage.write("\n")"   
            writeMessage.flush();

        } catch (NullPointerException npe) {
            throw new NullPointerException("\n[!]ERROR Null Pointer Exception\n--outputStream NULL");

        } catch (UnsupportedEncodingException use) {

            throw new UnsupportedEncodingException("\n[!]Error en la CODIFICACIÓN:\n--" + use.getMessage());

        } catch (IOException ex) {
            throw new IOException("\n[!]ERROR en la Entrada de Datos:\n--" + ex.getMessage());

        } finally {
            //Cerrando Escritor (No se puede cerrar porque mataría el SOCKET)
            //  readFromServer.close();  
            writeMessage = null;
        }
    }

//===========================================================================    
//=========================================================================== 
//Leer OBJETOS del InputStream
    /**
     * Este método recibe un InputStream, Lee la entrada el Flujo proporcionada
     * por el InputStream y lo convierte a un OBJETO tipo "Object", el cual
     * RETORNA, Listo para ser CASTEADO en el objeto DEBIDO.
     *
     *
     * @param inputStream ====> Recibe un InputStream el cual leerá.
     *
     * @return ======> Retorna un "Object"
     *
     * @throws java.io.IOException
     * @throws java.lang.ClassNotFoundException
     */
    public Object readObject(InputStream inputStream) throws IOException, NullPointerException, ClassNotFoundException {

        Object obj = null;
        ObjectInputStream readObj = null;

        try {

            readObj = new ObjectInputStream(inputStream);

            //LEYENDO OBJETO
            obj = (Object) readObj.readObject();

        } catch (NullPointerException npe) {
            throw new NullPointerException("\n[!]ERROR Null Pointer Exception\n--InputStream NULL");

        } catch (IOException ex) {

            throw new IOException("\n[!]ERROR en la Entrada de Datos:\n--" + ex.getMessage());

        } catch (ClassNotFoundException cne) {
            throw new ClassNotFoundException("[!]ERROR al LEER el OBJETO\n--" + cne.getMessage());
        } finally {
            //Cerrando lector (No se puede cerrar porque mataría el SOCKET)
            //  readObj.close();
            readObj = null;
        }

        return obj;

    }

//===========================================================================     
//Enviar OBJETOS AL Cliente
    /**
     * Este método recibe un OutputStream, y un OBJETO(el que sea) y Enviará el
     * OBJETO que haya recibido a través del OutputStream.
     * 
     * @param outputStream
     * 
     * @param OBJECT
     * 
     * @throws java.io.IOException
     */
    public void writeObject(OutputStream outputStream, Object OBJECT) throws IOException, NullPointerException {

        ObjectOutputStream writeObject = null;

        try {

            writeObject = new ObjectOutputStream(outputStream);

            writeObject.writeObject(OBJECT);

            writeObject.flush();

        } catch (NullPointerException npe) {
            throw new NullPointerException("\n[!]ERROR Null Pointer Exception\n--outputStream NULL");

        } catch (IOException ex) {
            throw new IOException("\n[!]ERROR en la Entrada de Datos:\n--" + ex.getMessage());

        } finally {
            //Cerrando Escritor (No se puede cerrar porque mataría el SOCKET)
            //   writeObject.close();

            writeObject = null;
        }
    }

//==============================================================================    
//==============================================================================    
//            EN CONSTRUCCION
//==============================================================================     
    
/**************************************************************************
*     RanDomAccesFile - Access Modes
* ----------------------------------------------
* The Java RandomAccessFile supports the following access modes:
* ---------------------------------------------------------------
* Mode	Description
* r	Read mode. Calling write methods will result in an IOException.
* rw	Read and write mode.
* rwd	Read and write mode - synchronously. All updates to file content is written to the disk synchronously.
* rws	Read and write mode - synchronously. All updates to file content or meta data is written to the disk synchronously. 
 */    

/////+++++++++++++++++++++++++++++++++++++++++++++++++++++++++

//LEE ARCHIVO Y LO RETORNA EN UNA STRING 
    public String readFileToString(String filePath) {
        try {

            String data;

            //Abrimos el archivo
            RandomAccessFile archivo = new RandomAccessFile(filePath, "rw");

            //Bloqueamos archivo por seguridad
            FileLock bloqueo = archivo.getChannel().lock();

            // Nos posicionamos al principio del fichero
            archivo.seek(0);

            // Creamos un BUFFER, del tamaño del ARCHIVO a LEER
            byte[] buffer = new byte[(int) archivo.length()];

            //Leemos en fichero completo(indicamos los Bytes a LEER. que en este caso va a valer BUFFER)
            archivo.readFully(buffer);

            //Leemos un ByteArray del tamaño de BUFFER, y lo enviamos al BufferesReader
            BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(buffer)));

            //Almacenando el ARCHIVO Leido en una STRING y procedemos a retornarla
            data = reader.lines().collect(Collectors.joining("\n"));

            //Cerrando BuffRead
            buffer = null;
            reader.close();
            //Quitamos bloqueo y cerramos el lector  
            archivo.close();
            bloqueo.release();

            //Retornando String con todo el contenido del fichero
            return data;

        } catch (Exception e) {
            System.out.println("No se puede leer el archivo"
                    + e.getMessage());
        }

        return "";

    }
    
 
  /////+++++++++++++++++++++++++++++++++++++++++++++++++++++++++

//LEE ARCHIVO Y LO RETORNA EN UN ArrayList
    public ArrayList<String> readFileToArrayList(String filePath) {
        try {

            String data;

            //Abrimos el archivo
            RandomAccessFile archivo = new RandomAccessFile(filePath, "rw");

            //Bloqueamos archivo por seguridad
            FileLock bloqueo = archivo.getChannel().lock();

            // Nos posicionamos al principio del fichero
            archivo.seek(0);

            // Creamos un BUFFER, del tamaño del ARCHIVO a LEER
            byte[] buffer = new byte[(int) archivo.length()];

            //Leemos en fichero completo(indicamos los Bytes a LEER. que en este caso va a valer BUFFER)
            archivo.readFully(buffer);

            //Leemos un ByteArray del tamaño de BUFFER, y lo enviamos al BufferesReader
            BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(buffer)));

            //Almacenando el ARCHIVO Leido en una STRING 
            data = reader.lines().collect(Collectors.joining("\n"));
            //Pasamos el Archivo a un ArrayList, Contiene una linea en cada Elemento de la Lista.
            ArrayList<String> arrayList = new ArrayList<>(Arrays.asList(data.split("\n"))); 

            //Cerrando BuffRead
            buffer = null;
            reader.close();
            //Quitamos bloqueo y cerramos el lector  
            
            archivo.close();
            bloqueo.release();
            //Retornando String con todo el contenido del fichero
            return arrayList;

        } catch (Exception e) {
            System.out.println("No se puede leer el archivo"
                    + e.getMessage());
        }
        //Retornando ArrayList Vacio
        return new ArrayList<>();

    }
    
    
      /////+++++++++++++++++++++++++++++++++++++++++++++++++++++++++

//LEE ARCHIVO Y LO IMPRIME POR PANTALLA
    public void readFileAndPrint(String filePath) {
        try {

            String data;

            //Abrimos el archivo
            RandomAccessFile archivo = new RandomAccessFile(filePath, "rw");

            //Bloqueamos archivo por seguridad
            FileLock bloqueo = archivo.getChannel().lock();

            // Nos posicionamos al principio del fichero
            archivo.seek(0);

            // Creamos un BUFFER, del tamaño del ARCHIVO a LEER
            byte[] buffer = new byte[(int) archivo.length()];

            //Leemos en fichero completo(indicamos los Bytes a LEER. que en este caso va a valer BUFFER)
            archivo.readFully(buffer);

            //Leemos un ByteArray del tamaño de BUFFER(Todo el fichero vamos), y lo enviamos al BufferesReader
            BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(buffer)));

            //Almacenando el ARCHIVO Leido en una STRING 
            data = reader.lines().collect(Collectors.joining("\n"));
            
            //Imprimimos los DATOS por consola.
            System.out.println(data);
            
            //Cerrando BuffRead
            buffer = null;
            reader.close();
            //Quitamos bloqueo y cerramos el lector  
      
            archivo.close();
            bloqueo.release();
            
        } catch (Exception e) {
            System.out.println("No se puede leer el archivo"
                    + e.getMessage());
        }

    }
    
    

    /////+++++++++++++++++++++++++++++++++++++++++++++++++++++++++  
    
//LEYENDO OBJETOS DE ARCHIVO_BINARIO 
    public ArrayList<Object> readFileObjectToObjectArrayList(String filePath){
        
        try {
            RandomAccessFile archivo = new RandomAccessFile(filePath, "rw");
            //Bloqueando el fichero
            FileLock bloqueo = archivo.getChannel().lock();
            // Nos posicionamos al principio del fichero
            archivo.seek(0);
             
            // Almacenamos los bytes del fichero en un array de bytes
            byte[] buffer = new byte[(int)archivo.length()];
             
            // Leemos todos los bytes del fichero
            archivo.readFully(buffer);
             
            // Convertimos ese array de bytes en un objeto.
            ObjectInputStream entrada = new ObjectInputStream(new ByteArrayInputStream(buffer));
             
            //Objeto GENERICO a leer 
            Object objetoRetornado;
            //Lista donde almacenaremos los objetos a retornar
            ArrayList<Object> objectList = new ArrayList<>();
        
            //Mientras Exista algun Objeto, Recolectamos y añadimos al ArrayList
            while ((objetoRetornado = (Object) entrada.readObject())!=null){
                //Agregamos Objeto al ArrayList
                objectList.add(objetoRetornado); 
            }
 
            System.out.println(objetoRetornado);
            // Cerramos el objeto ObjectInputStream
            entrada.close();
            
            archivo.close();
            bloqueo.release();
            
            return objectList;
            
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("\n[!]ERROR, Se prodijo un error:\n"
            + e.getMessage());
        }catch (Exception e) {
            System.out.println("\n[!]ERROR, No se puede leer el archivo\n"
            + e.getMessage());
        }
        
        return new ArrayList<>();
    } 
    
    
  
    
       
/////+++++++++++++++++++++++++++++++++++++++++++++++++++++++++ 
///???????????????????????????????????????????????????????????
    
     public void writeObjectInFile(String filePath, Object OBJ){
        try {
            /* Creamos o abrimos un nuevo archivo. En este caso:
            El primer parámetro hace referencia a la ruta del archivo.
            El segundo parametro es el siguiente:
            - r - read. Solo lectura.
            - rw - read/wirte. Lectura y escritura */
            RandomAccessFile archivo = new RandomAccessFile(filePath, "rw");
            //Bloqueando el fichero
            FileLock bloqueo = archivo.getChannel().lock();
            // Ponemos el puntero al final del archivo
            archivo.seek(archivo.length());
             
            // Serializamos el objeto Persona
            // Lo convertimos en una secuencia de bytees.
            ByteArrayOutputStream escribir= new ByteArrayOutputStream();
            ObjectOutputStream salida = new ObjectOutputStream(escribir);
            salida.writeObject(OBJ.toString());
             
            // Cerramos el objeto.
            salida.close();
             
            // obtenemos los bytes del libro serializado
            byte[] buffer = escribir.toByteArray();
             
            // Escribimos los bytes en el archivo.
            archivo.write(buffer);
             
            // Cerramos el archivo
            archivo.close();
            bloqueo.release();
            
        } catch (Exception e) {
            System.out.println("No se puede escribir en el archivo"
            + e.getMessage());
        }
    }
    
    
    
    
    
    ///+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
     
    public void writeTextInFile(String filePath, String message){
 
        //Instanciando objeto FileLock para realizar bloqueo del archivo
        FileLock bloqueo;
        

        try {
            //Accedemos al Archivo como LECTURA-escritura
            RandomAccessFile writer = new RandomAccessFile(filePath, "rw");

            //Bloqueamos el canal del archivo
            bloqueo = writer.getChannel().lock();
            
            //Colocamos puntero en posición FINAL del archivo para proceder a escribir
            writer.seek(writer.length());
            
            //Agregando salto de linea a mensaje
        //    message +="\n";
            //Obteniendo Bytes de Mensaje
            byte[] buffer = message.getBytes("UTF-8");

            writer.write(buffer);

           

            //Quitamos bloqueo y cerramos el lector                
            //             System.out.println("Proceso Escritor, SALIENDO de Sección Crítica");                
            writer.close();
            bloqueo.release();
            
        } catch (IOException ioe) {
            System.out.println("EXCEPTION Error al escribir el fichero.\n\n");
        } catch (Exception e) {
            System.out.println("EXCEPTION Error de escritura\n\n");

        } finally {
            bloqueo = null;
        }//Fin del Try-Catch
    }
     
//==============================================================================      
 
//TRABAJAR CON FICHEROS BINARIOS   
    public void copyBinaryFileToFile (String ficheroOriginal, String ficheroCopia)
	{
		try
		{
                        // Se abre el fichero original para lectura
			FileInputStream fileInput = new FileInputStream(ficheroOriginal);
			BufferedInputStream bufferedInput = new BufferedInputStream(fileInput);
			
			// Se abre el fichero donde se hará la copia
			FileOutputStream fileOutput = new FileOutputStream (ficheroCopia);
			BufferedOutputStream bufferedOutput = new BufferedOutputStream(fileOutput);
			
			// Bucle para leer de un fichero y escribir en el otro.
			byte [] buffer = new byte[1024];
			int leidos = bufferedInput.read(buffer);
			while (leidos > 0)
			{
				bufferedOutput.write(buffer,0,leidos);
				leidos=bufferedInput.read(buffer);
			}

			// Cierre de los ficheros
			bufferedInput.close();
			bufferedOutput.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}


    
//////////////////////////////////////////////////////////////////////


//#####################################################################
//                  ENVIAR - PEDIR/Descargar FICHEROS (Por Sockets)
//#####################################################################

/**
* Envia un OBJETO con el "nombre del archivo" Que Solicita,
* Se queda a la espera de recibir y descargar el Archivo Solicitado.
*  
* 
* @param outputStream      ==> Flujo por donde enviará el OBJETO para SOLICITAR EL ARCHIVO
* @param ficheroAsolicitar ==> Nombre del archivo que se SOLICITA AL SERVIDOR
* @param inputStream       ==> por el que se recibe la Descarga del ARCHIVO Solicitado
* 
* 
**/
    public void requestAndDownloadFile(OutputStream outputStream, String ficheroAsolicitar, InputStream inputStream){
        try
        {
            // Proparamos el Flujo para leer Objetos
            ObjectOutputStream oos = new ObjectOutputStream(outputStream);
            //Creamos nuestro objeto Pasandole el Nombre del fichero a Solicitar
            OBJFileRequest requestFile = new OBJFileRequest(ficheroAsolicitar);
            //Enviamos Objeto de Solicitud del fichero          
            oos.writeObject(requestFile);

            // Se abre un fichero para empezar a copiar lo que se reciba.
            FileOutputStream fos = new FileOutputStream(requestFile.nombreFichero + "_RECEIVED");

            // Se crea un ObjectInputStream del socket para leer los mensajes
            // que contienen el fichero.
            ObjectInputStream ois = new ObjectInputStream(inputStream);
            OBJFfileSubmission mensajeRecibido;
            Object mensajeAux;
            do
            {
                // Se lee el mensaje en una variabla auxiliar
                mensajeAux = ois.readObject();
                
                // Si es del tipo esperado, se trata
                //if (mensajeAux instanceof MensajeTomaFichero){
                if (Class.forName("OBJFfileSubmission").isInstance(mensajeAux)){
                    
                    mensajeRecibido = (OBJFfileSubmission) mensajeAux;
                    // Se escribe en pantalla y en el fichero
              //    System.out.print(new String(mensajeRecibido.contenidoFichero, 0, mensajeRecibido.bytesValidos));
                    fos.write(mensajeRecibido.contenidoFichero, 0, mensajeRecibido.bytesValidos);
                    
                } else {
                    // Si no es del tipo esperado, se marca error y se termina
                    // el bucle
                    System.err.println("Mensaje no esperado "
                            + mensajeAux.getClass().getName());
                    break;
                }
                
            } while (!mensajeRecibido.ultimoMensaje);
            
          // Se cierra socket y fichero
       //     fos.close();
       //     ois.close();


        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
//******************************************************************************
     
     
    /**
     * Envia el fichero indicado a traves del ObjectOutputStream indicado.
     * 
     * @param inputStream por el que se recibe el objeto, que contiene el nombre del ARCHIVO Solicitado
     * @param outputStream por el que enviar el fichero
     */
    public void sendRequestedFile(InputStream inputStream, OutputStream outputStream) {
        try {

            // Se lee el OBJETO de Peticion de Fichero del cliente.
            ObjectInputStream ois = new ObjectInputStream(inputStream);
            Object mensajeRead = ois.readObject();
            String requestedFile = ""; //Nombre del fichero Solicitado

            // Si el mensaje es de petici�n de fichero
            // if (mensaje instanceof MensajeDameFichero)
            if (Class.forName("OBJFileRequest").isInstance(mensajeRead)) {

                //Se muestra en pantalla el fichero pedido y se envia
                System.out.println("Me piden: " + ((OBJFileRequest) mensajeRead).nombreFichero);

                //Almacenando nombre del fichero Solicitado
                requestedFile = ((OBJFileRequest) mensajeRead).nombreFichero;

                //Creamos flujo de salida para Enviar el Fichero
                ObjectOutputStream oos = new ObjectOutputStream(outputStream);

             //enviaFicheros********************************
                boolean enviadoUltimo = false;
                // Se abre el fichero.
                FileInputStream fis = new FileInputStream(requestedFile);

                // Se instancia y rellena un mensaje de envio de fichero
                OBJFfileSubmission receivedData = new OBJFfileSubmission();
                receivedData.nombreFichero = requestedFile;

                // Se leen los primeros bytes del fichero en un campo del mensaje
                int leidos = fis.read(receivedData.contenidoFichero);

                // Bucle mientras se vayan leyendo datos del fichero
                while (leidos > -1) {

                    // Se rellena el n�mero de bytes leidos
                    receivedData.bytesValidos = leidos;

                    // Si no se han leido el m�ximo de bytes, es porque el fichero
                    // se ha acabado y este es el �ltimo mensaje
                    if (leidos < OBJFfileSubmission.LONGITUD_MAXIMA) {
                        receivedData.ultimoMensaje = true;
                        enviadoUltimo = true;
                    } else {
                        receivedData.ultimoMensaje = false;
                    }

                    // Se env�a por el socket
                    oos.writeObject(receivedData);

                    // Si es el �ltimo mensaje, salimos del bucle.
                    if (receivedData.ultimoMensaje) {
                        break;
                    }

                    // Se crea un nuevo mensaje
                    receivedData = new OBJFfileSubmission();
                    receivedData.nombreFichero = requestedFile;

                    // y se leen sus bytes.
                    leidos = fis.read(receivedData.contenidoFichero);
                }

                if (enviadoUltimo == false) {
                    receivedData.ultimoMensaje = true;
                    receivedData.bytesValidos = 0;
                    oos.writeObject(receivedData);
                }
                // Se cierra el ObjectOutputStream
              //  oos.close();
               
            } else {
                // Si no es el mensaje esperado, se avisa y se sale todo.
                System.err.println("Mensaje no esperado " + mensajeRead.getClass().getName());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            
            
        }
    }
     
     

//#####################################################################
//    CLASES UTILIZADAS en   ENVIAR - RECIBIR FICHEROS (Por Sockets)
//#####################################################################
//#####################################################################
//Clase Utilizada EN el ENVIO y EN la RECEPCION el FICHERO
    private class OBJFfileSubmission implements Serializable {

        /**
         * Nombre del fichero que se transmite. Por defecto ""
         */
        public String nombreFichero = "";

        /**
         * Si este es el �ltimo mensaje del fichero en cuesti�n o hay m�s
         * despu�s
         */
        public boolean ultimoMensaje = true;

        /**
         * Cuantos bytes son v�lidos en el array de bytes
         */
        public int bytesValidos = 0;

        /**
         * Array con bytes leidos(o por leer) del fichero
         */
        public byte[] contenidoFichero = new byte[LONGITUD_MAXIMA];

        /**
         * N�mero m�ximo de bytes que se envia�n en cada mensaje
         */
        public final static int LONGITUD_MAXIMA = 1024;
    }

    
//Clase Utilizada para SOLICITAR el FICHERO
    private class OBJFileRequest implements Serializable {

           /** path completo del fichero que se pide */
        final public String nombreFichero;

        public OBJFileRequest(String nombreFichero) {
            this.nombreFichero = nombreFichero;
        }
    }

    
}//Fin de la clase principal

