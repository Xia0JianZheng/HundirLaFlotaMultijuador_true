package com.company.Client;

import com.company.Game.Jugada;
import com.company.Game.RespuestaServer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
// Esta clase contendrá lo necesario para hacer funcionar el apartado del cliente
public class ClienteTCP extends Thread{

    Scanner sc = new Scanner(System.in);
    String hostname;
    int port;
    boolean continueConnected;
    String nombreUsuario;
    RespuestaServer respuestaServer;
    private String nombreUsuarioTurno;
    private String mensajeFinPartida;

    public ClienteTCP(String hostname, int port) {
        this.hostname = hostname;
        this.port = port;
        continueConnected = true;
    }
    //Este metodo hará funcionar el lado destinado a la conexión del cliente con el servidor
    public void run() {
        Socket socket;
        ObjectInputStream ois;
        ObjectOutputStream oos;

        try {
            //Conecta al server usando hostname y puertos especificados que introduzca el usuario
            socket = new Socket(InetAddress.getByName(hostname), port);
            oos = new ObjectOutputStream(socket.getOutputStream());
            ois = new ObjectInputStream(socket.getInputStream());

            // Comprobara que los datos introducidos son correctos
            nombreUsuario = (String) ois.readObject();
            respuestaServer = (RespuestaServer) ois.readObject();
            comprobarRespuestaServer(respuestaServer);

            // Leera la información mientras la conexion este activa
            while(continueConnected){
                // Saber el nombre del jugador que tiene el turno actualmente
                nombreUsuarioTurno = (String) ois.readObject();

                // Selecciona al jugador que le toca jugar
                if (nombreUsuarioTurno.equals(nombreUsuario)){
                    Jugada jugada = getRequest();
                    oos.writeObject(jugada);
                    // Recibe la respuesta del servidor y comprueba la jugada
                    respuestaServer = (RespuestaServer) ois.readObject();
                    comprobarRespuestaServer(respuestaServer);
                    // Si el juego ha terminado lo mostrara por pantalla
                    if (respuestaServer.isGameOver()){
                        mensajeFinPartida = (String) ois.readObject();
                        System.out.println(mensajeFinPartida);
                    }

                }else {
                    // Si es el turno del otro jugador comprobara la respuesta del server y esperara su turno
                    System.out.println();
                    System.out.println("Turno del otro jugador");
                    respuestaServer = (RespuestaServer) ois.readObject();
                    comprobarRespuestaServer(respuestaServer);
                    //  Si el juego ha terminado lo mostrara por pantalla
                    if (respuestaServer.isGameOver()){
                        mensajeFinPartida = (String) ois.readObject();
                        System.out.println(mensajeFinPartida);
                    }
                }

            }
            //Cierra el socket cuando termine
            close(socket);
        } catch (UnknownHostException ex) {
            //Mostrara el mensaje de error al introducir el nombre del host
            System.out.println("Error de connexió. No existeix el host: " + ex.getMessage());
        } catch (IOException ex) {
            // Mensaje de error relacionado con IO
            System.out.println("Error de connexió indefinit: " + ex.getMessage());
        } catch (ClassNotFoundException e) {
            // Mensaje de error al no encontrar la clase
            e.printStackTrace();
        }
    }

    // Este metodo comprobara la respuesta del servidor y mostrara por terminal el barco atacado si el jugador acierta la posición, de lo contrario mostrara que ha fallado
    private void comprobarRespuestaServer(RespuestaServer respuestaServer) {
        if (respuestaServer != null){
            if (respuestaServer.getTablero() != null){
                System.out.println("Tablero Atacado!");
                for (int i = 0; i < 10; i++) {
                    System.out.print("||");
                    for (int j = 0; j < 10; j++) {
                        System.out.print(respuestaServer.getTablero()[i][j]+"||");
                    }
                    System.out.println();
                }
                System.out.println(respuestaServer.getMensaje());
            }
            // Comprobará si el juego ha terminado basandose en la respuesta del servidor
            comprobarFinalEnemigo(respuestaServer.isGameOver());
        }
    }

    // Con este metodo se creara un objeto "Jugada" que contendrá la columna y fila introducidos por el usuario (cliente) y lo devolverá
    public Jugada getRequest() {
        // Inicializamos un nuevo objeto "Jugada"
        Jugada jugada = new Jugada();
        int columna,fila;

        System.out.println("tu turno: ");

        // Preguntamos por la fila y la columna al usuario
        System.out.println("Selecciona fila: ");
        columna = sc.nextInt();
        System.out.println();
        System.out.println("Selecciona columna: ");
        fila = sc.nextInt();

        // Set del nombre del jugador (1 o 2) basandonos en el turno actual
        if (nombreUsuario.equals("jugador1")){
            jugada.setNom("1");
        }else jugada.setNom("2");

        // Set de la columna y la fila
        jugada.setX(columna);
        jugada.setY(fila);


        // Return del objeto "Jugada" creado
        return jugada;
    }

    // Este metodo comprobará si el juego ha terminado basandonos en la respuesta del servidor
    public boolean comprobarFinalEnemigo(boolean gameOver) {
        //Si el juego ha terminado haremos un set de "continueConnected" a false
        if (gameOver){
            continueConnected = false;
            return false;
        }
        // Si el juego no ha terminado devolveremos un true y continuara la partida
        return continueConnected;
    }


    // Este metodo se encargara de gestionar la información de los Sockets y despues los cerrara
    private void close(Socket socket){
        try {
            if(socket!=null && !socket.isClosed()){
                // Comprueba si esta abierto y entonces lo cierra
                if(!socket.isInputShutdown()){
                    socket.shutdownInput();
                }
                // Comprueba si esta abierto y entonces lo cierra
                if(!socket.isOutputShutdown()){
                    socket.shutdownOutput();
                }
                //Cierra el socket
                socket.close();
            }
        } catch (IOException ex) {
            // Registro de cualquier error ocurrido durante el proceso
            Logger.getLogger(ClienteTCP.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("<---------------------->");
        System.out.println("         Menu           ");
        System.out.println("<---------------------->");
        System.out.println("1. Comenzar partida.    ");
        System.out.println("<---------------------->");
        System.out.println("2. Salir                ");
        System.out.println("<---------------------->");
        int opcion = sc.nextInt();
        sc.nextLine();
        switch (opcion){
            case 1:
                System.out.println("Introduzca la IP del server: ");
                String ip = sc.nextLine();
                System.out.println();
                System.out.println("Introduzca el puerto del server: ");
                int puerto = sc.nextInt();
                ClienteTCP clientTcp = new ClienteTCP(ip,puerto);
                clientTcp.start();
                break;
            case 2:
                System.out.println("Cerrando la APP");
        }
    }
}
