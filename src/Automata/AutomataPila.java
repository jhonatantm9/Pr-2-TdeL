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
    private ArrayList<Estado> estados;
    private Estado estadoInicial;
    public static Stack<String> pila;
    public String estadoActual = "";
    
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
    
    public void inicializarEstados(ArrayList<Character> simbolosPila, ArrayList<Character> simbolosEntrada){
        for (Estado estado : estados) {
            if(estado.transiciones == null){
                estado.inicializarTransiciones(simbolosPila.size(), simbolosEntrada.size());
                Character[] vecSimbolosPila = new Character[1];
                vecSimbolosPila = simbolosPila.toArray(vecSimbolosPila);
                Character[] vecSimbolosEntrada = new Character[1];
                vecSimbolosEntrada = simbolosEntrada.toArray(vecSimbolosEntrada);
                estado.simbolosEntrada = vecSimbolosEntrada;
                estado.simbolosPila = vecSimbolosPila;
            }else{
                reorganizarEstado(estado, simbolosPila, simbolosEntrada);
            }
        }
    }
    
    public void agregarTransicion(String estado, Transicion transicion){
        Estado e = getEstado(estado);
        if(e != null){
            e.agregarTransicion(transicion);
        }
    }
    
    public Transicion getTransicion(String estado, Transicion transicion){
        Estado e = getEstado(estado);
        return(e.getTransicion(transicion.simboloEntrada, transicion.simboloPila));
    }
    
    public Transicion getTransicion(String estado, Character simboloPila, Character simboloEntrada){
        Estado e = getEstado(estado);
        return(e.getTransicion(simboloEntrada, simboloPila));
    }
    
    public void eliminarTransicion(String estado, Transicion transicion){
        Estado e = getEstado(estado);
        if(e != null){
            e.eliminarTransicion(transicion);
        }
    }
    
    public void reorganizarEstado(Estado estado, ArrayList<Character> simbolosPila, ArrayList<Character> simbolosEntrada){
        ArrayList<Transicion> transiciones = estado.getTransiciones();
        
        estado.inicializarTransiciones(simbolosPila.size(), simbolosEntrada.size());
        Character[] vecSimbolosPila = new Character[1];
        vecSimbolosPila = simbolosPila.toArray(vecSimbolosPila);
        Character[] vecSimbolosEntrada = new Character[1];
        vecSimbolosEntrada = simbolosEntrada.toArray(vecSimbolosEntrada);
        estado.simbolosEntrada = vecSimbolosEntrada;
        estado.simbolosPila = vecSimbolosPila;
        
        for (Transicion tr : transiciones) {
            estado.agregarTransicion(tr);
        }
    }
    
    /**
     * Reconoce una cadena ingresada por el usuario
     * @param cadena Hilera a reconocer
     * @return true si la cadena es reconocida por el autómata, false de lo contrario
     */
    public boolean reconocer(String cadena){
        char[] simbolo = cadena.toCharArray();
        Estado estadoActual = this.getEstadoInicial();

        for (int i = 0; i < simbolo.length; i++) {
            Character simboloPila;
            if(pila.empty()){
                simboloPila = '▼';
            }else{
                simboloPila = pila.peek().charAt(0);
            }
            Transicion tr = estadoActual.getTransicion(simbolo[i], simboloPila);
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
    
    /**
     * Realiza una operación de pila y en caso de ser necesario desapila el tope
     * de la pila o apila uno o más elementos
     * @param opPila Operación a realizar
     * @param simboloAR Símbolo para la operación Apilar o símbolos para la
     * operación Replace
     */
    public void operarPila(String opPila, String simboloAR){
        if(opPila.equals("Desapilar")){
            pila.pop();
        }else if(opPila.equals("Apilar")){
            pila.push(simboloAR);
        }else if(opPila.equals("Replace")){
            pila.pop();
            char[] simbolosAR = simboloAR.toCharArray();
            for (char c : simbolosAR) {
                pila.push(String.valueOf(c));
            }
        }
    }
    
    /**
     * Realiza una operación de estado y retorna el nuevo estado donde se
     * encontrará el reconocedor
     * @param nombreEstado Nombre del estado siguiente
     * @return Variable Estado con el nuevo estado actual
     */
    public Estado operarEstado(String nombreEstado){
        Estado estado = getEstado(nombreEstado);
        return estado;
    }
    
    /**
     * Realiza una operación de entrada y retorna un booleano dependiendo de
     * esta
     * @param opEntrada Nombre de la operación
     * @return true, si es necesario avanzar para reconocer el siguiente símbolo,
     * false si la operación es retenga
     */
    public boolean operarEntrada(String opEntrada){
        if(opEntrada.equals("Avance")){
            return true;
        }
        return false;
    }
    
    public boolean reconocerSimbolo(Character simboloEntrada, String nombreEstado){
        Character simboloPila;
        if(pila.empty()){
            simboloPila = '▼';
        }else{
            simboloPila = pila.peek().charAt(0);
        }
        Transicion tr = getTransicion(nombreEstado, simboloPila, simboloEntrada);
        if(tr == null){
            estadoActual = "";
            return false;
        }else{
            return true;
        }
    }

    public ArrayList<Estado> getEstados() {
        return estados;
    }
}
