class Chat {
    static void envia_mensaje_multicast(byte[] buffer,String ip,int puerto) throws IOException
    {
        DatagramSocket socket = new DatagramSocket();
        socket.send(new DatagramPacket(buffer,buffer.length,InetAddress.getByName(ip),puerto));
        socket.close();
    }

    static byte[] recibe_mensaje_multicast(MulticastSocket socket,int longitud_mensaje) throws IOException
    {
        byte[] buffer = new byte[longitud_mensaje];
        DatagramPacket paquete = new DatagramPacket(buffer,buffer.length);
        socket.receive(paquete);
        return paquete.getData();
    }

    static class Worker extends Thread
    {
        public void run()
        {
        // En un ciclo infinito se recibirán los mensajes enviados al
        // grupo 230.0.0.0 a través del puerto 50000 y se desplegarán en la pantalla.
        }
    }
    public static void main(String[] args) throws Exception
    {
        new Worker().start();
        String nombre = args[0];
        // En un ciclo infinito se leerá cada mensaje del teclado y se enviará el mensaje al
        // grupo 230.0.0.0 a través del puerto 50000.
    }
}
