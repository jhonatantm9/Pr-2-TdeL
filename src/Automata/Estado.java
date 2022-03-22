/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Automata;

import java.util.ArrayList;

/**
 * Clase que define los estados que tiene un autómata. Tiene una matriz con las
 * transiciones pertenecientes al estado, dos vectores que definen los símbolos 
 * de entrada y pila y que son usados para algunas operaciones y el nombre del
 * estado
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
    
    /**
     * Inicializa la matriz de transiciones con el número de símbolos de pila
     * y entrada dados
     * @param numSimbolosPila
     * @param numSimbolosEntrada 
     */
    public void inicializarTransiciones(int numSimbolosPila, int numSimbolosEntrada){
        transiciones = new Transicion[numSimbolosPila][numSimbolosEntrada];
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
    
    /**
     * Agrega una transicion al estado
     * @param tr Transicion a agregar
     */
    public void agregarTransicion(Transicion tr){
        int indiceSimEntr = getIndiceSimEntrada(tr.simboloEntrada);
        int indiceSimPila = getIndiceSimPila(tr.simboloPila);
        if(indiceSimEntr != -1 && indiceSimPila != -1){
            transiciones[indiceSimPila][indiceSimEntr] = tr;
        }
    }
    
    /**
     * Retorna la transicion que coincide con los símbolos de entrada y pila dados
     * @param simboloEntrada Simbolo de entrada de la transición
     * @param simboloPila Simbolo de pila de la transición
     * @return Transicion pedida o null en caso de que no exista
     */
    public Transicion getTransicion(char simboloEntrada, char simboloPila){
        int indiceSimEntr = getIndiceSimEntrada(simboloEntrada);
        int indiceSimPila = getIndiceSimPila(simboloPila);
        if(indiceSimEntr != -1 && indiceSimPila != -1){
            return(transiciones[indiceSimPila][indiceSimEntr]);
        }
        return null;
    }
    
    /**
     * Elimina una transición del estado en caso de que exista
     * @param tr Transición a eliminar
     */
    public void eliminarTransicion(Transicion tr){
        int indiceSimEntr = getIndiceSimEntrada(tr.simboloEntrada);
        int indiceSimPila = getIndiceSimPila(tr.simboloPila);
        if(indiceSimEntr != -1 && indiceSimPila != -1){
            transiciones[indiceSimPila][indiceSimEntr] = null;
        }
    }
    
    /**
     * Devuelve un ArrayList con todas las transiciones de este estado
     * @return ArrayList con las transiciones del estado
     */
    public ArrayList<Transicion> getTransiciones(){
        ArrayList<Transicion> arrTransiciones = new ArrayList<>();
        for (int i = 0; i < simbolosPila.length; i++) {
            for (int j = 0; j < simbolosEntrada.length; j++) {
                if(transiciones[i][j]!= null){
                  arrTransiciones.add(transiciones[i][j]);
                }
            }   
        }
        return arrTransiciones;
   }
}
