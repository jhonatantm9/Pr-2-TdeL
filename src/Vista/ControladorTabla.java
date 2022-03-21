/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vista;

import Automata.*;
import java.util.ArrayList;
import java.util.Hashtable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author jhona
 */
public class ControladorTabla {
    
    private DefaultTableModel modeloTabla;
    DefaultTableModel modeloPila;
    public int numEstados;

    public ControladorTabla() {
        modeloTabla = new DefaultTableModel();
        modeloPila = new DefaultTableModel(15, 1);
        numEstados = 0;
    }
    
    /**
     * Crea una tabla con los símbolos de entrada, de pila y estados dados. Esta
     * tabla es la representación gráfica del automata sin las transiciones
     * @param simbolosEntrada
     * @param simbolosPila
     * @param estados
     * @return DefaultTableModel con el modelo de la tabla
     */
    public DefaultTableModel crearTabla(ArrayList<Character> simbolosEntrada, ArrayList<Character> simbolosPila, ArrayList<String> estados){
        modeloTabla = new DefaultTableModel(); //Empezar la tabla desde cero
        
        Object[] simbolosEntVec = simbolosEntrada.toArray();
        ArrayList<Object> columnas = new ArrayList();

        columnas.add("S. PILA/S. ENTRADA");
        for (int i = 0; i < simbolosEntrada.size(); i++) {
            columnas.add(simbolosEntVec[i]);
        }
        modeloTabla.setColumnIdentifiers(columnas.toArray()); //Añadimos la cabecera de la tabla
        
        numEstados = estados.size();
        //Se agregan los símbolos de la pila y nombres de estados si hay más de uno
        if(estados.size() == 1){
            for (Character simbolo : simbolosPila) {
                ArrayList<Object> fila = new ArrayList<>();
                fila.add(simbolo);
                for (int i = 0; i < simbolosEntrada.size(); i++) {
                    fila.add("");
                }
                modeloTabla.addRow(fila.toArray());
            }
        }else{
            ArrayList<ArrayList<Object>> filas = new ArrayList<>();
            for (Character simbolo : simbolosPila) {
                ArrayList<Object> fila = new ArrayList<>();
                fila.add(simbolo);
                for (int i = 0; i < simbolosEntrada.size(); i++) {
                    fila.add("");
                }
                filas.add(fila);
            }
            for(String estado: estados){
                ArrayList<Object> fila = new ArrayList<>();
                fila.add(estado);
                for (int i = 0; i < simbolosEntrada.size(); i++) {
                    fila.add("");
                }
                modeloTabla.addRow(fila.toArray());
                for(ArrayList<Object> arrFila: filas){
                    modeloTabla.addRow(arrFila.toArray());
                }
                modeloTabla.setRowCount(modeloTabla.getRowCount() + 1);
            }
            modeloTabla.removeRow(modeloTabla.getRowCount() - 1);
        }
        return modeloTabla;
    }
    
    public DefaultTableModel actualizarPila(){
        modeloPila = new DefaultTableModel(15, 1);
        Object[] encabezado = {"Pila"};
        modeloPila.setColumnIdentifiers(encabezado);
        modeloPila.setValueAt('▼', 14, 0);
        if(Automata.AutomataPila.pila.size() < 15){
            for (int i = 0; i < Automata.AutomataPila.pila.size(); i++) {            
                modeloPila.setValueAt(Automata.AutomataPila.pila.get(i), 13 - i, 0);
            }
        }else{
            for (int i = 0; i < 14; i++) {            
                modeloPila.setValueAt(Automata.AutomataPila.pila.get(i), 13 - i, 0);
            }
        }
        return modeloPila;
    }
    
    public void agregarTransicionTabla(String estado, String key, Transicion tr){
        int numFila = 0;
        int numColumna = 0;
        if(numEstados == 1){
            for (int i = 0; i < modeloTabla.getRowCount(); i++) {
                if(modeloTabla.getValueAt(i, 0).equals(tr.simboloPila)){
                    numFila = i;
                    break;
                }
            }
            for (int i = 1; i < modeloTabla.getColumnCount(); i++) {
                if(tr.simboloEntrada.equals(modeloTabla.getColumnName(i).charAt(0))){
                    numColumna = i;
                    break;
                }
            }
            modeloTabla.setValueAt(key, numFila, numColumna);
        }else{
            while(numFila < modeloTabla.getRowCount()){
                if(estado.equals(modeloTabla.getValueAt(numFila, 0))){
                    break;
                }
                numFila++;
            }
            while(numFila < modeloTabla.getRowCount()){
                if(tr.simboloPila.equals(modeloTabla.getValueAt(numFila, 0))){
                    break;
                }
                numFila++;
            }
            for (int i = 1; i < modeloTabla.getColumnCount(); i++) {
                if(tr.simboloEntrada.equals(modeloTabla.getColumnName(i).charAt(0))){
                    numColumna = i;
                    break;
                }
            }
            modeloTabla.setValueAt(key, numFila, numColumna);
        }
    }
    
    public String descripcionTransiciones(ArrayList<Transicion> transiciones){
        String descripcion = "A: Aceptación\n";
        for (int i = 0; i < transiciones.size(); i++) {
            descripcion += ("#" + (i+1) + ": " + transiciones.get(i).toString() + "\n");
        }
        return descripcion;
    }
    
    public void actualizarTransicionesTabla(ArrayList<Transicion> transicionesGUI, AutomataPila ap) {
        eliminarTransicionesTabla();
        for (Estado estado : ap.getEstados()) {
            String nombre = estado.nombre;
            ArrayList<Transicion> transiciones = estado.getTransiciones();
            for (int i = 0; i < transiciones.size(); i++) {
                if (transiciones.get(i).esAceptacion) {
                    agregarTransicionTabla(nombre, "A", transiciones.get(i));
                } else {
                    for (int j = 0; j < transicionesGUI.size(); j++) {
                        if (transicionesGUI.get(j).equals(transiciones.get(i))) {
                            agregarTransicionTabla(nombre, String.valueOf(j + 1), transiciones.get(i));
                        }
                    }
                }
            }
        }
    }
    
    public void eliminarTransicionesTabla(){
        for (int i = 0; i < modeloTabla.getRowCount(); i++) {
            for (int j = 1; j < modeloTabla.getColumnCount(); j++) {
                modeloTabla.setValueAt(null, i, j);
            }            
        }    
    }
    
    public void actualizarSimbolos(ArrayList<Character> simbolosEntrada, ArrayList<Character> simbolosPila, ArrayList<String> estados){
        modeloTabla = new DefaultTableModel(); //Empezar la tabla desde cero
        
        Object[] simbolosEntVec = simbolosEntrada.toArray();
        ArrayList<Object> columnas = new ArrayList();

        columnas.add("S. PILA/S. ENTRADA");
        for (int i = 0; i < simbolosEntrada.size(); i++) {
            columnas.add(simbolosEntVec[i]);
        }
        modeloTabla.setColumnIdentifiers(columnas.toArray()); //Añadimos la cabecera de la tabla
        
        numEstados = estados.size();
        //Se agregan los símbolos de la pila y nombres de estados si hay más de uno
        if(estados.size() == 1){
            for (Character simbolo : simbolosPila) {
                ArrayList<Object> fila = new ArrayList<>();
                fila.add(simbolo);
                for (int i = 0; i < simbolosEntrada.size(); i++) {
                    fila.add("");
                }
                modeloTabla.addRow(fila.toArray());
            }
        }else{
            ArrayList<ArrayList<Object>> filas = new ArrayList<>();
            for (Character simbolo : simbolosPila) {
                ArrayList<Object> fila = new ArrayList<>();
                fila.add(simbolo);
                for (int i = 0; i < simbolosEntrada.size(); i++) {
                    fila.add("");
                }
                filas.add(fila);
            }
            for(String estado: estados){
                ArrayList<Object> fila = new ArrayList<>();
                fila.add(estado);
                for (int i = 0; i < simbolosEntrada.size(); i++) {
                    fila.add("");
                }
                modeloTabla.addRow(fila.toArray());
                for(ArrayList<Object> arrFila: filas){
                    modeloTabla.addRow(arrFila.toArray());
                }
                modeloTabla.setRowCount(modeloTabla.getRowCount() + 1);
            }
            modeloTabla.removeRow(modeloTabla.getRowCount() - 1);
        }
    }
    
    public void actualizarTabla(ArrayList<Character> simbolosEntrada, ArrayList<Character> simbolosPila, ArrayList<Transicion> transiciones, AutomataPila ap){
        ArrayList<Estado> estados = ap.getEstados();
        ArrayList<String> nombresEstados = new ArrayList<>();
        for (Estado estado : estados) {
            nombresEstados.add(estado.nombre);
        }
        actualizarSimbolos(simbolosEntrada, simbolosPila, nombresEstados);
        actualizarTransicionesTabla(transiciones, ap);
    }

    public DefaultTableModel getModeloTabla() {
        return modeloTabla;
    }
    
    
}
