/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Automata;

import java.util.ArrayList;

/**
 *
 * @author jhona
 */
public class Estado {
    public String nombre;
    public Transicion[][] transiciones;
    public Character[] simbolosPila;
    public Character[] simbolosEntrada;

    public Estado(String nombre) {
        this.nombre = nombre;
    }
    
    public void agregarSimbolosEntrada(ArrayList<Character> arrSimbolosEntrada){
        simbolosEntrada = arrSimbolosEntrada.toArray(simbolosEntrada);
    }
    
    public void agregarSimbolosPila(ArrayList<Character> arrSimbolosEntrada){
        simbolosPila = arrSimbolosEntrada.toArray(simbolosPila);
    }
    
    public void inicializarTransiciones(int numSimbolosPila, int numSimbolosEntrada){
        transiciones = new Transicion[numSimbolosPila][numSimbolosEntrada];
    }
    
    public int getIndiceSimPila(char simbolo){
        for (int i = 0; i < simbolosPila.length; i++) {
            if(simbolosPila[i].equals(simbolo)){
                return i;
            }
        }
        return -1;
    }
    
    public int getIndiceSimEntrada(char simbolo){
        for (int i = 0; i < simbolosEntrada.length; i++) {
            if(simbolosEntrada[i].equals(simbolo)){
                return i;
            }
        }
        return -1;
    }
    
    public Transicion getTransicion(char simboloEntrada, char simboloPila){
        int indiceSimEntr = getIndiceSimEntrada(simboloEntrada);
        int indiceSimPila = getIndiceSimEntrada(simboloPila);
        return(transiciones[indiceSimPila][indiceSimEntr]);
    }
}
