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
public class TransicionComun extends Transicion{
    public String operacionPila;
    public String operacionEstado;
    public String operacionEntrada;
    public String simboloAR;
    
    public TransicionComun(String opPila, String opEstado, String opEntrada){
        esAceptacion = false;
        operacionPila = opPila;
        operacionEstado = opEstado;
        operacionEntrada = opEntrada;
        simboloAR = "";
    }
    
    public TransicionComun(String opPila, String opEstado, String opEntrada, String simboloAR){
        esAceptacion = false;
        operacionPila = opPila;
        operacionEstado = opEstado;
        operacionEntrada = opEntrada;
        this.simboloAR = simboloAR;
    }
}
