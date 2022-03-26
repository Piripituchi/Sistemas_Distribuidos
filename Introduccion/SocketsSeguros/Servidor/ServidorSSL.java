package Servidor;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.*;
import java.nio.ByteBuffer;

import javax.net.ssl.SSLServerSocketFactory;

class ServidorSSL {
    /**
     staticer extends Thread
     java -Djavax.net.ssl.keyStore=keystore_servidor.jks -Djavax.net.ssl.keyStorePassword=1234567 ServidorSSL
     */
    static void read(DataInputStream f,byte[] b,int posicion,int longitud) throws Exception
    {
        while (longitud > 0)
        {
            int n = f.read(b,posicion,longitud);
            posicion += n;
            longitud -= n;
        }
    }

    static void escribe_archivo(String archivo,byte[] buffer) throws Exception
    {
        FileOutputStream f = new FileOutputStream(archivo);
        try
        {
            f.write(buffer);
        }
        finally
        {
            f.close();
        }
    }
    
    static class Worker extends Thread {
        Socket conexion;
        Worker(Socket conexion){
            this.conexion=conexion;
        }
        public void run() {
            try
            {
                //DataOutputStream salida = new DataOutputStream(conexion.getOutputStream());
                DataInputStream entrada = new DataInputStream(conexion.getInputStream());
                String nombre = entrada.readUTF();
                int longitud = entrada.readInt();
                byte[] buffer = new byte[longitud];
                read(entrada,buffer,0,longitud);
                escribe_archivo(nombre, buffer);
                //System.out.println(new String(buffer,"UTF-8"));
                //salida.write("HOLA".getBytes());
                conexion.close();
            }
            catch(Exception e)
            {
                System.out.println(e.getMessage());
            }
                
        }
    }
    
    public static void main(String[] args) throws Exception{
        SSLServerSocketFactory socket_factory =(SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
        ServerSocket socket_servidor= socket_factory.createServerSocket(50000);
        for (;;) {
            Socket conexion = socket_servidor.accept();
            Worker w = new Worker(conexion);
            w.start();
        }
    }
}
