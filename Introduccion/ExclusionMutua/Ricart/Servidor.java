import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

class Servidor {
    static String[] hosts;
    static int[] puertos;
    static int num_nodos;
    static int nodo;
    static long reloj_logico;

    public static void envia_mensaje(long reloj_logico, String host, int puerto) throws IOException, InterruptedException {
        Socket cliente = null;
        for (;;) {
            try {
                cliente = new Socket(host,puerto);
                break;
            } catch (Exception e) {
                Thread.sleep(1000);
            }
        }
        DataOutputStream salida = new DataOutputStream(cliente.getOutputStream());
        salida.writeLong(reloj_logico);
        cliente.close();
    }

    static class Cliente extends Thread{
        int destino;
        Cliente(int destino){
            this.destino=destino;
        }

        @Override
        public void run() {
            try{ 
                envia_mensaje(reloj_logico, hosts[destino], puertos[destino]);
            }
            catch(Exception e){
                System.out.println(e.getMessage());
            }
        }
    }

    static class Reloj extends Thread {
        static Object obj = new Object();
        public void run() {
            try {
                for(;;){
                    System.out.println(String.valueOf(reloj_logico));
                    switch (nodo) {
                        case 0:
                            synchronized(obj){
                                reloj_logico+=4;
                            }
                            Thread.sleep(1000);
                            break;
                        case 1:
                            synchronized(obj){
                                reloj_logico+=5;
                            }
                            Thread.sleep(1000);
                            break;
                        case 2:
                            synchronized(obj){
                                reloj_logico+=6;
                            }
                            Thread.sleep(1000);
                            break;
                        default:
                            break;
                    }
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    static class Worker extends Thread{
        Socket conexion;
        static Object obj = new Object();

        Worker(Socket conexion){
            this.conexion=conexion;
        }

        public void run() {
            System.out.println("Inicio el thread Worker");            
            try {
                long tiempo_recibido;
                DataInputStream entrada = new DataInputStream(conexion.getInputStream());
                tiempo_recibido=entrada.readLong();
                synchronized(obj){
                    if (reloj_logico<=tiempo_recibido) {
                        reloj_logico=tiempo_recibido+1;
                    }
                }

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }


        }
    }

    static class Server extends Thread{
        public void run() {
            try {
                ServerSocket server = new ServerSocket(puertos[nodo]);
                for (;;){
                    Socket conexion=server.accept();
                    Worker w = new Worker(conexion);
                    w.start();
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public static void main(String[] args) throws Exception {
        nodo=Integer.parseInt(args[0]);
        num_nodos=args.length-1;
        //System.out.println(String.valueOf(num_nodos));
        String[] aux=Arrays.copyOfRange(args, 1, args.length);
        hosts=new String[num_nodos];
        puertos=new int[num_nodos];
        int indx=0;
        String[] parts;
        for (String pareja : aux) {
            parts=pareja.split(":");
            hosts[indx]=parts[0];
            puertos[indx]=Integer.parseInt(parts[1]);
            indx=indx+1;
        }
        Server s = new Server();
        s.start();
        
        Thread[] envio = new Thread[num_nodos];
        for (int i = 0; i < num_nodos; i++) {
            envio[i]=new Cliente(i);
            envio[i].start();
        }
        for (Thread thread : envio) {
            thread.join();
        }


        Reloj r = new Reloj();
        r.start();
        s.join();
    }



}
