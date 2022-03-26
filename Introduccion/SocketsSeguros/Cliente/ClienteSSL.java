package Cliente;
/**
 * Cliente
 * java -Djavax.net.ssl.trustStore=keystore_cliente.jks -Djavax.net.ssl.trustStorePassword=1234567 ClienteSSL <archivo>
 */
import java.net.*;
import java.nio.ByteBuffer;

import javax.net.ssl.SSLSocketFactory;

import java.io.*;
public class ClienteSSL {

    static void read(DataInputStream f,byte[] b,int posicion,int longitud) throws Exception
    {
        while (longitud > 0)
        {
            int n = f.read(b,posicion,longitud);
            posicion += n;
            longitud -= n;
        }
    }

    static byte[] lee_archivo(String archivo) throws Exception
    {
        FileInputStream f = new FileInputStream(archivo);
        byte[] buffer;
        try
        {
            buffer = new byte[f.available()];
            f.read(buffer);
        }
        finally
        {
            f.close();
        }
        return buffer;
    }

    public static void main(String[] args) {
        try {
            File archivo = new File(args[0]);
            byte[] buffer = lee_archivo(archivo.getName());
            SSLSocketFactory cliente = (SSLSocketFactory) SSLSocketFactory.getDefault();
            Socket conexion = cliente.createSocket("localhost",50000);
            DataOutputStream salida = new DataOutputStream(conexion.getOutputStream());
            salida.writeUTF(archivo.getName());
            salida.writeInt((int)archivo.length());
            salida.write(buffer);
            Thread.sleep(1000);
            conexion.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
}