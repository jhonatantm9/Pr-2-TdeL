/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Automata;

/**
 *
 * @author jhona
 */
public class Transicion {
    public boolean esAceptacion;
    public Character simboloPila;
    public Character simboloEntrada;
    public String estado;

    public Transicion(char simboloPila, char simboloEntrada) {
        this.simboloPila = simboloPila;
        this.simboloEntrada = simboloEntrada;
    }

    public Transicion(Character simboloPila, Character simboloEntrada, String estado) {
        this.simboloPila = simboloPila;
        this.simboloEntrada = simboloEntrada;
        this.estado = estado;
    }
    
    
}
