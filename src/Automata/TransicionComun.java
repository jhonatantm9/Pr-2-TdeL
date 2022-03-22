/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Automata;

import java.util.Objects;

/**
 * Clase de las transiciones que no son de aceptación. Tienen tres operaciones
 * habituales de una transición (en pila, de estado y de entrada) y un String que
 * define los caracteres que se usan cuando la operación es Apilar o Replace
 * @author jhona
 */
public class TransicionComun extends Transicion{
    public String operacionPila;
    public String operacionEstado;
    public String operacionEntrada;
    public String simboloAR;
    
    public TransicionComun(String opPila, String opEstado, String opEntrada, char simboloPila, char simboloEntrada){
        super(simboloPila, simboloEntrada);
        esAceptacion = false;
        operacionPila = opPila;
        operacionEstado = opEstado;
        operacionEntrada = opEntrada;
        simboloAR = "";
    }
    
    public TransicionComun(String opPila, String opEstado, String opEntrada, String simboloAR, char simboloPila, char simboloEntrada){
        super(simboloPila, simboloEntrada);
        esAceptacion = false;
        operacionPila = opPila;
        operacionEstado = opEstado;
        operacionEntrada = opEntrada;
        this.simboloAR = simboloAR;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final TransicionComun other = (TransicionComun) obj;
        if (!Objects.equals(this.operacionPila, other.operacionPila)) {
            return false;
        }
        if (!Objects.equals(this.operacionEstado, other.operacionEstado)) {
            return false;
        }
        if (!Objects.equals(this.operacionEntrada, other.operacionEntrada)) {
            return false;
        }
        if (!Objects.equals(this.simboloAR, other.simboloAR)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        if(operacionPila.equals("Replace")){
            return("Replace(" + simboloAR + "), " + operacionEstado + ", " + operacionEntrada);
        }if(operacionPila.equals("Apilar")){
            return("Apile(" + simboloAR + "), " + operacionEstado + ", " + operacionEntrada);
        }if(operacionPila.equals("Ninguna")){
            return(operacionEstado + ", " + operacionEntrada);
        }
        return(operacionPila + ", " + operacionEstado + ", " + operacionEntrada);
    }
    
    
}
