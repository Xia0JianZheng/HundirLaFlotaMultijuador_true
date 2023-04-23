package com.company.Game;

import java.io.Serializable;
//clase para gestionar las jugadas realizadas por los clientes
public class Jugada implements Serializable {
    public static final long serialVersionUID = 1L;
    String Nom;
    int x,y;

    public String getNom() {
        return Nom;
    }

    public void setNom(String nom) {
        Nom = nom;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

}
