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
    String operacionPila;
    String operacionEstado;
    String operacionEntrada;
    String operacionReplace;
    
    public Transicion(String opPila, String opEstado, String opEntrada){
        operacionPila = opPila;
        operacionEstado = opEstado;
        operacionEntrada = opEntrada;
        operacionReplace = "";
    }
    
    public Transicion(String opPila, String opEstado, String opEntrada, String opReplace){
        operacionPila = opPila;
        operacionEstado = opEstado;
        operacionEntrada = opEntrada;
        operacionReplace = opReplace;
    }
}
