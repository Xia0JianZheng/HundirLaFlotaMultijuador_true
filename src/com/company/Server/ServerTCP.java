package com.company.Server;

import com.company.Game.Tablero;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
// Esta clase se encargará de gestionar el servidor
public class ServerTCP {

    int port;

    public ServerTCP(int port ) {
        this.port = port;
    }

    // Este método escuchara las llamadas de los clientes y creara un objeto ThreadServidor por cada par de conexiones para gestionar la conexión entre ellos

    public void listen() {
        ServerSocket serverSocket;
        Socket client1Socket;
        Socket client2Socket;
        try {
            serverSocket = new ServerSocket(port);
            while(true) {
                client1Socket = serverSocket.accept();
                client2Socket = serverSocket.accept();
                ThreadServidor FilServidor = new ThreadServidor(client1Socket,client2Socket);
                Thread client = new Thread(FilServidor);
                client.start();
            }
        } catch (IOException ex) {
            Logger.getLogger(ServerTCP.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    //Este método creará un nuevo objeto ServerTCP y empezará a recibir las peticiones
    public static void main(String[] args) {
        ServerTCP srv = new ServerTCP(5558);
        srv.listen();
    }

}
