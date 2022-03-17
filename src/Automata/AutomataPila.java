/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Automata;

import java.util.ArrayList;
import java.util.Stack;

/**
 *
 * @author jhona
 */
public class AutomataPila {
    public ArrayList<Estado> estados;
    private Estado estadoInicial;
    public static Stack<String> pila;
    
    public AutomataPila(){
        estados = new ArrayList<>();
        pila = new Stack<>();
    }
    
    public void agregarEstado(Estado estado){
        estados.add(estado);
    }
    
    public void agregarEstado(String nombreEstado){
        Estado estado = new Estado(nombreEstado);
        estados.add(estado);
    }
    
    public void eliminarEstado(Estado estado){
        estados.remove(estado);
    }
    
    public void eliminarEstado(String nombreEstado){
        Estado e = getEstado(nombreEstado);
        estados.remove(e);
    }

    public void setEstadoInicial(String nombreEstado) {
        for(Estado e : estados){
            if(e.nombre.equals(nombreEstado)){
                estadoInicial = e;
            }
        }        
    }
    
    public Estado getEstadoInicial() {
        return estadoInicial;
    }
    
    public Estado getEstado(String nombreEstado){
        for (Estado estado : estados) {
            if(estado.nombre.equals(nombreEstado)){
                return estado;
            }
        }
        return null;
    }
    
    public boolean reconocer(String cadena){
        char[] simbolo = cadena.toCharArray();
        Estado estadoActual = this.getEstadoInicial();

        for (int i = 0; i < simbolo.length; i++) {
            Transicion tr = estadoActual.getTransicion(simbolo[i], pila.peek().charAt(0));
            if(tr == null){
                return false;
            }else if(tr.esAceptacion){
                return true;
            }else{
                TransicionComun transicion = (TransicionComun) tr;
                operarPila(transicion.operacionPila, transicion.simboloAR);
                estadoActual = operarEstado(transicion.operacionEstado);
                if(!operarEntrada(transicion.operacionEntrada)){
                    i = i - 1;
                }
            }
        }
        return false;
    }
    
    public void operarPila(String opPila, String simboloAR){
        if(opPila.equals("Desapilar")){
            pila.pop();
        }else if(opPila.equals("Apilar")){
            pila.push(simboloAR);
        }else if(opPila.equals("Replace")){
            char[] simbolosAR = simboloAR.toCharArray();
            for (char c : simbolosAR) {
                pila.push(String.valueOf(c));
            }
        }
    }
    
    public Estado operarEstado(String nombreEstado){
        Estado estado = getEstado(nombreEstado);
        return estado;
    }
    
    public boolean operarEntrada(String opEntrada){
        if(opEntrada.equals("Avance")){
            return true;
        }
        return false;
    }
}
