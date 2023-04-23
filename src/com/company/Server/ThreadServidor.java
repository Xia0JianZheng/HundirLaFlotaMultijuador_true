package com.company.Server;


import com.company.Game.Jugada;
import com.company.Game.RespuestaServer;
import com.company.Game.Tablero;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
// Esta clase se encarga de gestionar la conexión entre los dos clientes que se encuentren jugado la partida
public class ThreadServidor implements Runnable{

    Socket client1Socket;
    Socket client2Socket;

    ObjectInputStream ois;
    ObjectOutputStream oos;
    ObjectInputStream ois2;
    ObjectOutputStream oos2;

    Jugada jugada;
    boolean acabat;
    Tablero tablero1 = new Tablero();
    Tablero tablero2 = new Tablero();
    RespuestaServer respuestaServer ;
    boolean turno = true;

    public ThreadServidor(Socket client1Socket, Socket client2Socket) throws IOException {

        this.client1Socket = client1Socket;
        this.client2Socket = client2Socket;
        acabat = false;

        tablero1.rellenarTableroPosicion();
        tablero1.rellenarTableroJugadores();

        tablero2.rellenarTableroPosicion();
        tablero2.rellenarTableroJugadores();
    }

    // Este metodo es el loop principal del programa ya que gestionara la lógica y conexión entre los clientes
    @Override
    public void run() {
        try {
            ois = new ObjectInputStream(this.client1Socket.getInputStream());
            oos = new ObjectOutputStream(this.client1Socket.getOutputStream());


            ois2 = new ObjectInputStream(this.client2Socket.getInputStream());
            oos2 = new ObjectOutputStream(this.client2Socket.getOutputStream());

            //Envía el numero de cada jugador
            oos.writeObject("jugador1");
            oos2.writeObject("jugador2");
            oos.reset();
            oos2.reset();

            // Envía el estado inicial de los tableros a los clientes
            oos.writeObject(new RespuestaServer("", tablero2.tablero_jugadores, juegoAcabado(tablero2)));
            oos2.writeObject(new RespuestaServer("",tablero1.tablero_jugadores, juegoAcabado(tablero1)));

            while(!acabat) {
                oos.reset();
                oos2.reset();

                if (turno){
                    // Turno del jugador 1
                    oos.writeObject("jugador1");
                    oos2.writeObject("jugador1");

                    // Lee el movimiento del jugador 1
                    jugada = (Jugada) ois.readObject();
                    System.out.println(jugada.getNom());

                    // Genera la respuesta basada en el movimiento y los envía a los clientes
                    respuestaServer= generaResposta(jugada);
                    oos.reset();
                    oos.writeObject(respuestaServer);
                    oos2.reset();
                    oos2.writeObject(respuestaServer);

                    //  Comprueba si el juego ha terminado y envía el mensaje a los clientes
                    if (juegoAcabado(tablero1)){
                        oos.reset();
                        oos.writeObject("Felicidades Jugador 1 has ganado!!");
                        oos2.reset();
                        oos2.writeObject("Ha ganado el jugador 1, otra vez sera.");
                    }

                }else {
                    //  Turno del jugador 2
                    oos2.writeObject("jugador2");
                    oos.writeObject("jugador2");

                    // Lee el movimiento del jugador 2
                    jugada = (Jugada) ois2.readObject();
                    System.out.println(jugada.getNom());

                    //Genera la respuesta basada en el movimiento y los envía a los clientes
                    respuestaServer= generaResposta(jugada);
                    oos2.reset();
                    oos2.writeObject(respuestaServer);
                    oos.reset();
                    oos.writeObject(respuestaServer);

                    //  Comprueba si el juego ha terminado y envía el mensaje a los clientes
                    if (juegoAcabado(tablero2)){
                        oos2.reset();
                        oos2.writeObject("Felicidades Jugador 2 has ganado!!");
                        oos.reset();
                        oos.writeObject("Ha ganado el jugador 2, otra vez sera.");
                    }

                }
                turno = !turno;
                oos.flush();
                oos2.flush();
            }
        }catch(IOException | ClassNotFoundException e){
            System.out.println(e.getLocalizedMessage());
        }

        try {
            client1Socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // Genera una respuesta basada en el movimiento de cada jugador basado en el estado actual del juego
    public RespuestaServer generaResposta(Jugada jugada) {
        if (jugada != null){
            // Crea un nuevo objeto respuestaServer
            respuestaServer = new RespuestaServer();;
            String mensaje;
            //Si es el turno del jugador 1
            if(turno){
                // Comprueba si ha impactado en uno de los barcos enemigos
                mensaje = tablero2.haImpactado(jugada);
                // Cambia el tablero del enemigo al estado actual
                respuestaServer.setTablero(tablero2.tablero_jugadores);
                // Comprueba si ha terminado el juego
                respuestaServer.setGameOver(juegoAcabado(tablero2));
                // genera el mensaje
                respuestaServer.setMensaje(mensaje);
                // devuelve la respuesServer
                return respuestaServer;

                //Si es el turno del jugador 2
            }else {
                // Comprueba si ha impactado en uno de los barcos enemigos
                mensaje = tablero1.haImpactado(jugada);
                // Cambia el tablero del enemigo al estado actual
                respuestaServer.setTablero(tablero1.tablero_jugadores);
                // Comprueba si ha terminado el juego
                respuestaServer.setGameOver(juegoAcabado(tablero1));
                // genera el mensaje
                respuestaServer.setMensaje(mensaje);
                // devuelve la respuesServer
                return respuestaServer;
            }
        }else return null;
    }
    //Comprueba si el juego ha terminado teniendo en cuenta el número de casillas con barcos disponibles
    private boolean juegoAcabado(Tablero tablero) {
        if (tablero.numBarcos() == 0){
            return true;
        }else return false;
    }
}