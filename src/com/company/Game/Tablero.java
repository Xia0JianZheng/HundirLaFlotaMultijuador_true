package com.company.Game;

import com.company.Game.Jugada;

import java.io.Serializable;
//Esta clase se encarga de gestionar el funcionamiento del tablero de cada jugador
public class Tablero implements Serializable {
    public static final long serialVersionUID = 1L;
    private String nombreTablero;
    public String[][] tablero_PosicionBarcos;
    public String[][] tablero_jugadores;
    private boolean colocado = false;
    private int total_casilla_barco;
    private boolean gameOver = false;


    public Tablero() {
        tablero_PosicionBarcos = new String[10][10];
        tablero_jugadores = new String[10][10];
        total_casilla_barco = 15;
    }

    public void rellenarTableroPosicion() {
        int x, y;

        // Colocara dos fragatas de manera aleatoria en el campo de batalla (ocuparan 1 espacio en el tablero)
        for (int i = 0; i < 2; i++) {
            while (!colocado){
                x = (int) (Math.random()*9);
                y = (int) (Math.random()*9);
                if (tablero_PosicionBarcos[x][y] == null){
                    tablero_PosicionBarcos[x][y] = "F";
                    colocado = true;
                }
            }
            colocado = false;
        }

        //  Colocara 3 destructores en el campo de batalla (ocuparan 3 espacios en el tablero)
        for (int i = 0; i < 3; i++) {
            boolean colocado = false;
            while (!colocado) {
                x = (int) (Math.random() * 9);
                y = (int) (Math.random() * 9);
                boolean isValidPlacement = true;

                // Comprobara que hay espacio para colocarlos de manera horizontal
                if (y + 2 >= 9) {
                    isValidPlacement = false;
                } else {
                    // Comrpobara que los 3 espacios no estan ocupados por ninun otro barco aun
                    for (int j = 0; j < 3; j++) {
                        if (tablero_PosicionBarcos[x][y + j] != null) {
                            isValidPlacement = false;
                            break;
                        }
                    }
                }
                //Si se cumplen todos los requisitos colocara el barco
                if (isValidPlacement) {
                    for (int j = 0; j < 3; j++) {
                        tablero_PosicionBarcos[x][y + j] = "D";
                    }
                    colocado = true;
                }
            }
        }

        // Colocara 1 portaviones en el campo de batalla (ocupara 4 espacios en el tablero)
        for (int i = 0; i < 1; i++) {
            while (!colocado){
                x = (int) (Math.random()*6);
                y = (int) (Math.random()*6);
                if (tablero_PosicionBarcos[x][y] == null &&
                        tablero_PosicionBarcos[x+1][y] == null &&
                        tablero_PosicionBarcos[x+2][y] == null &&
                        tablero_PosicionBarcos[x+3][y] == null){
                    tablero_PosicionBarcos[x][y] = "P";
                    tablero_PosicionBarcos[x+1][y] = "P";
                    tablero_PosicionBarcos[x+2][y] = "P";
                    tablero_PosicionBarcos[x+3][y] = "P";
                    colocado = true;
                }
            }
            colocado = false;
        }

        //Mostrará el tablero con los barcos colocados
        System.out.println("Mi Tablero");
        for (int i = 0; i < 10; i++) {
            System.out.print(" || ");
            for (int j = 0; j < 10; j++) {
                if (tablero_PosicionBarcos[i][j] == null){
                    tablero_PosicionBarcos[i][j] = "A";
                }
                System.out.print(tablero_PosicionBarcos[i][j]+" || ");
            }
            System.out.println();
        }
        System.out.println();
    }

    //Este metodo rellenara el tablero de los jugadores con "?" hasta que destapen las casillas
    public void rellenarTableroJugadores(){
        for (int i = 0; i < 10; i++) {
            // Separador entre casillas
            System.out.print(" || ");
            for (int j = 0; j < 10; j++) {
                // Si no contiene información rellena
                if (tablero_jugadores[i][j] == null){
                    tablero_jugadores[i][j] = "?";
                }
                System.out.print(tablero_jugadores[i][j]+" || ");
            }
            // Siguiente fila
            System.out.println();
        }
    }

     // Este metodo comprobara si la "Jugada" de uno de los clientes ha inpactado en algun barco y devolverá la información con un String

    public String haImpactado(Jugada jugada){
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if (i == jugada.x && j == jugada.y){
                    if (tablero_jugadores[i][j].equals("?")){
                        //Si la posición aún no ha sido atacada
                        String barco = tablero_PosicionBarcos[i][j];
                        switch (barco) {
                            case "D","F","P" -> {
                                //Si la posición contiene un barco marca una "X" y resta 1 al numero total de casillas que contienen un barco
                                tablero_jugadores[i][j] = "X";
                                total_casilla_barco--;
                                return "Impacto en un barco";
                            }
                            case "A" -> {
                                //Si la posición no contiene ningún barco "destapa" la casilla y dejará un espacio en blanco " "
                                tablero_jugadores[i][j] = " ";
                                return "No ha habido impacto";
                            }
                        }
                    }
                }
            }
        }
        //Si ya has atacado esta posición mostrara el mensaje por pantalla
        return "Ya has atacado esta casilla.";
    }

    public int numBarcos(){
        return total_casilla_barco;
    }

    public String getNombreTablero() {
        return nombreTablero;
    }

    public void setNombreTablero(String nombreTablero) {
        this.nombreTablero = nombreTablero;
    }

    public String[][] getTablero_jugadores() {
        return tablero_jugadores;
    }

    public void setTablero_jugadores(String[][] tablero) {
        this.tablero_jugadores = tablero;
    }

    public String[][] getTablero_PosicionBarcos() {
        return tablero_PosicionBarcos;
    }

    public void setTablero_PosicionBarcos(String[][] tablero_PosicionBarcos) {
        this.tablero_PosicionBarcos = tablero_PosicionBarcos;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }
}
