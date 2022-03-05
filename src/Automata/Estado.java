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
public class Estado {
    public String nombre;
    public Transicion[][] transiciones;
    
    public Estado(String nombre, int numSimbolosPila, int numSimbolosEntrada){
        this.nombre = nombre;
        transiciones = new Transicion[numSimbolosPila][numSimbolosEntrada];
    }

    public Estado(String nombre) {
        this.nombre = nombre;
    }
    
    
}
