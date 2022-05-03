import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

//javac -cp gson-2.3.1.jar Cliente.java
//java -cp gson-2.3.1.jar;. Cliente (En Windows)

class Usuario
{
    String email;
    String nombre;
    String apellido_paterno;
    String apellido_materno;
    String fecha_nacimiento;
    String telefono;
    String genero;
    byte[] foto;

    Usuario(String email, String nombre, String apellido_paterno, String apellido_materno, String fecha_nacimiento, String telefono, String genero) {
        this.email=email;
        this.nombre=nombre;
        this.apellido_paterno=apellido_paterno;
        this.apellido_materno=apellido_materno;
        this.fecha_nacimiento=fecha_nacimiento;
        this.telefono=telefono;
        this.genero=genero;
        this.foto=null;
    }
}

class Cliente {
    //4CV12 Jesus Eduardo Angeles Hernandez

    public static void main(String[] args) throws Exception{
        for(;;){
            Scanner sc = new Scanner(System.in);
            URL url, url2;
            Usuario u=null;
            HttpURLConnection conexion, conexion2;
            String opc, email, nombre, a_paterno, a_materno,fecha_nacmiento, telefono, genero,parametros,s;
            OutputStream os, os2;
            Gson j = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").create();
            System.out.print("\033[H\033[2J");
            System.out.flush();
            System.out.println("MENU");
            System.out.println("a. Alta usuario");
            System.out.println("b. Consulta usuario");
            System.out.println("c. Borra usuario");
            System.out.println("d. Salir");
            System.out.print("Opcion: ");
            opc=sc.nextLine();
            switch (opc) {
                case "a":
                    //Alta usuario
                    System.out.print("\033[H\033[2J");
                    System.out.flush();                
                    System.out.println("Ingrese el email:");
                    email=sc.nextLine();
                    System.out.println("Ingrese el nombre:");
                    nombre=sc.nextLine();
                    System.out.println("Ingrese el apellido paterno:");
                    a_paterno=sc.nextLine();
                    System.out.println("Ingrese el apellido materno:");
                    a_materno=sc.nextLine();
                    System.out.println("Fecha de nacimiento (yyyy-MM-dd):");
                    fecha_nacmiento=sc.nextLine();
                    System.out.println("Ingrese el numero Telefonico:");
                    telefono=sc.nextLine();
                    System.out.println("Ingrese el genero: (M | F)");
                    genero=sc.nextLine();
                    u = new Usuario(email,nombre,a_paterno,a_materno,fecha_nacmiento,telefono,genero);
                    s=j.toJson(u);

                    url = new URL("http://20.232.117.110:8080/Servicio/rest/ws/alta_usuario");
                    conexion = (HttpURLConnection) url.openConnection();
                    // true si se va a enviar un "body", en este caso el "body" son los parámetros
                    conexion.setDoOutput(true);
                    // en este caso utilizamos el método POST de HTTP
                    conexion.setRequestMethod("POST");
                    // indica que la petición estará codificada como URL
                    conexion.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
                    parametros = "usuario=" + URLEncoder.encode(s,"UTF-8");
                    os = conexion.getOutputStream();
                    os.write(parametros.getBytes());
                    os.flush();
                    System.out.print("\033[H\033[2J");
                    System.out.flush();
                    if (conexion.getResponseCode() == 200) {
                        System.out.println("OK");
                    } else {
                        BufferedReader br = new BufferedReader(new InputStreamReader((conexion.getErrorStream())));
          
                        String respuesta;
                      
                        // el método web regresa una instancia de la clase Error en formato JSON
                        while ((respuesta = br.readLine()) != null) System.out.println(respuesta);                                                                        
                    }
                    System.out.println("\n\nRegresando al menu principal");
                    Thread.sleep(3000);
                    conexion.disconnect();            
                    break;
                case "b":
                    //Consulta usuario
                    System.out.print("\033[H\033[2J");
                    System.out.flush();                
                    System.out.println("Ingrese el email:");
                    email=sc.nextLine();
                    url = new URL("http://20.232.117.110:8080/Servicio/rest/ws/consulta_usuario");
                    conexion = (HttpURLConnection) url.openConnection();
                    // true si se va a enviar un "body", en este caso el "body" son los parámetros
                    conexion.setDoOutput(true);
                    // en este caso utilizamos el método POST de HTTP
                    conexion.setRequestMethod("POST");
                    // indica que la petición estará codificada como URL
                    conexion.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
                    // el método web "consulta_usuario" recibe como parámetro el email de un usuario, en este caso el email es a@c
                    parametros = "email=" + URLEncoder.encode(email,"UTF-8");
                    os = conexion.getOutputStream();
                    os.write(parametros.getBytes());
                    os.flush();
                    System.out.print("\033[H\033[2J");
                    System.out.flush();
                    // se debe verificar si hubo error
                    if (conexion.getResponseCode() == 200)
                    {
                        // no hubo error
                        BufferedReader br = new BufferedReader(new InputStreamReader((conexion.getInputStream())));

                        u = (Usuario)j.fromJson(br,Usuario.class);         
                        System.out.println("email: "+u.email );
                        System.out.println("Nombre: "+u.nombre);
                        System.out.println("Apellido Paterno: "+u.apellido_paterno);
                        System.out.println("Apellido Materno: "+u.apellido_materno);
                        System.out.println("Fecha de nacimiento: "+u.fecha_nacimiento);
                        System.out.println("Telefono: "+u.telefono);
                        System.out.println("Genero: "+u.genero);
                        System.out.println("\n¿Desea modificar el usuario (s/n)?");
                        String mod = sc.nextLine();
                        switch (mod) {
                            case "s":
                                System.out.print("\033[H\033[2J");
                                System.out.flush();                
                                System.out.println("Ingrese el nombre:");
                                nombre=sc.nextLine();
                                System.out.println("Ingrese el apellido paterno:");
                                a_paterno=sc.nextLine();
                                System.out.println("Ingrese el apellido materno:");
                                a_materno=sc.nextLine();
                                System.out.println("Fecha de nacimiento (yyyy-MM-dd):");
                                fecha_nacmiento=sc.nextLine();
                                System.out.println("Ingrese el numero Telefonico:");
                                telefono=sc.nextLine();
                                System.out.println("Ingrese el genero: (M | F)");
                                genero=sc.nextLine();
                                if (!nombre.equals("")) {
                                    u.nombre=nombre;
                                }
                                if (!a_paterno.equals("")) {
                                    u.apellido_paterno=a_paterno;
                                }                                
                                if (!a_materno.equals("")) {
                                    u.apellido_materno=a_materno;
                                }        
                                if (!fecha_nacmiento.equals("")) {
                                    u.fecha_nacimiento=fecha_nacmiento;
                                }
                                if (!telefono.equals("")) {
                                    u.telefono=telefono;
                                }
                                if (!genero.equals("")) {
                                    u.genero=genero;
                                }
                                
                                s=j.toJson(u);
            
                                url2 = new URL("http://20.232.117.110:8080/Servicio/rest/ws/modifica_usuario");
                                conexion2 = (HttpURLConnection) url2.openConnection();
                                // true si se va a enviar un "body", en este caso el "body" son los parámetros
                                conexion2.setDoOutput(true);
                                // en este caso utilizamos el método POST de HTTP
                                conexion2.setRequestMethod("POST");
                                // indica que la petición estará codificada como URL
                                conexion2.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
                                // el método web "consulta_usuario" recibe como parámetro el email de un usuario, en este caso el email es a@c
                                parametros = "usuario=" + URLEncoder.encode(s,"UTF-8");
                                os2 = conexion2.getOutputStream();
                                os2.write(parametros.getBytes());
                                os2.flush();
                                System.out.print("\033[H\033[2J");
                                System.out.flush();      
                                if (conexion2.getResponseCode() == 200) {
                                    System.out.println("OK");
                                } else {
                                    BufferedReader br2 = new BufferedReader(new InputStreamReader((conexion2.getErrorStream())));
          
                                    String respuesta2;
                                  
                                    // el método web regresa una instancia de la clase Error en formato JSON
                                    while ((respuesta2 = br2.readLine()) != null) System.out.println(respuesta2);                                    
                                }                                                          
                                
                                break;

                            case "n":
                                break;
                        
                            default:
                                break;
                        }
                        
                    }
                    else
                    {
                    // hubo error
                        BufferedReader br = new BufferedReader(new InputStreamReader((conexion.getErrorStream())));
                        String respuesta;               
                        // el método web regresa una instancia de la clase Error en formato JSON
                        while ((respuesta = br.readLine()) != null) System.out.println(respuesta);
                    }
                    System.out.println("\n\nRegresando al menu principal");
                    Thread.sleep(3000);
                    conexion.disconnect();                    
                    break;
                case "c":
                    //Borra usuario
                    System.out.print("\033[H\033[2J");
                    System.out.flush();                
                    System.out.println("Ingrese el email:");
                    email=sc.nextLine();
                    url = new URL("http://20.232.117.110:8080/Servicio/rest/ws/borra_usuario");
                    conexion = (HttpURLConnection) url.openConnection();
                    // true si se va a enviar un "body", en este caso el "body" son los parámetros
                    conexion.setDoOutput(true);
                    // en este caso utilizamos el método POST de HTTP
                    conexion.setRequestMethod("POST");
                    // indica que la petición estará codificada como URL
                    conexion.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
                    // el método web "consulta_usuario" recibe como parámetro el email de un usuario, en este caso el email es a@c
                    parametros = "email=" + URLEncoder.encode(email,"UTF-8");
                    os = conexion.getOutputStream();
                    os.write(parametros.getBytes());
                    os.flush();
                    System.out.print("\033[H\033[2J");
                    System.out.flush();
                    if (conexion.getResponseCode()==200) {
                        System.out.println("OK");
                    } else {
                        // hubo error
                        BufferedReader br = new BufferedReader(new InputStreamReader((conexion.getErrorStream())));
                        String respuesta;               
                        // el método web regresa una instancia de la clase Error en formato JSON
                        while ((respuesta = br.readLine()) != null) System.out.println(respuesta);
                    }
                    System.out.println("\n\nRegresando al menu principal");
                    Thread.sleep(3000);
                    conexion.disconnect();                
                    break;
                case "d":
                    //Salir
                    System.out.println("\n\n...Saliendo del programa");
                    Thread.sleep(3000);
                    System.exit(0);
                    break;
                default:
                    break;
            }
        }

    }
}
