package Utils;


import java.io.Serializable;

/**
 *
 * @author David Freyre Muñoz <https://github.com/azanet>
 */
public class OBJFileSubmission implements Serializable{
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
