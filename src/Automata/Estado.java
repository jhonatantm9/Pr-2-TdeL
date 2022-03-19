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
        //simbolosEntrada = new Character[numSimbolosEntrada];
        //simbolosPila = new Character[numSimbolosPila];
    }
    
    public int getIndiceSimPila(char simbolo){
        for (int i = 0; i < simbolosPila.length; i++) {
            if(simbolosPila[i] != null && simbolosPila[i].equals(simbolo)){
                return i;
            }
        }
        return -1;
    }
    
    public int getIndiceSimEntrada(char simbolo){
        for (int i = 0; i < simbolosEntrada.length; i++) {
            if(simbolosEntrada[i] != null && simbolosEntrada[i].equals(simbolo)){
                return i;
            }
        }
        return -1;
    }
    
    public void agregarTransicion(Transicion tr){
        int indiceSimEntr = getIndiceSimEntrada(tr.simboloEntrada);
        int indiceSimPila = getIndiceSimPila(tr.simboloPila);
        transiciones[indiceSimPila][indiceSimEntr] = tr;
    }
    
    public Transicion getTransicion(char simboloEntrada, char simboloPila){
        int indiceSimEntr = getIndiceSimEntrada(simboloEntrada);
        int indiceSimPila = getIndiceSimPila(simboloPila);
        if(indiceSimEntr != -1 && indiceSimPila != -1){
            return(transiciones[indiceSimPila][indiceSimEntr]);
        }
        return null;
    }
    
    public void eliminarTransicion(Transicion tr){
        int indiceSimEntr = getIndiceSimEntrada(tr.simboloEntrada);
        int indiceSimPila = getIndiceSimPila(tr.simboloPila);
        if(indiceSimEntr != -1 && indiceSimPila != -1){
            transiciones[indiceSimPila][indiceSimEntr] = null;
        }
    }
}
