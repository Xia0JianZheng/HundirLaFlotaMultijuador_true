package com.company.Game;

import java.io.Serializable;

// Clase para gestionar la respuesta del Servidor
public class RespuestaServer implements Serializable {

    String mensaje;
    String[][] tablero;
    boolean gameOver;

    public RespuestaServer(String mensaje, String[][] tablero, boolean gameOver) {
        this.mensaje = mensaje;
        this.tablero = tablero;
        this.gameOver = gameOver;
    }

    public RespuestaServer() {

    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String[][] getTablero() {
        return tablero;
    }

    public void setTablero(String[][] tablero) {
        this.tablero = tablero;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }
}

