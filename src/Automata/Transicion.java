/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Automata;

/**
 * Clase Transicion con 3 atributos, simbolos de entrada y pila y un valor booleano
 * que define si es una transición de aceptación o rechazo. Este último atributo
 * es "true" para todas las transiciones de la clase hija TransicionAceptacion y
 * "false" para todas las transiciones de la clase hija TransicionComun
 * @author jhona
 */
public class Transicion {
    public boolean esAceptacion;
    public Character simboloPila;
    public Character simboloEntrada;

    public Transicion(char simboloPila, char simboloEntrada) {
        this.simboloPila = simboloPila;
        this.simboloEntrada = simboloEntrada;
    }

    public Transicion(Character simboloPila, Character simboloEntrada) {
        this.simboloPila = simboloPila;
        this.simboloEntrada = simboloEntrada;
    }
    
    
}
