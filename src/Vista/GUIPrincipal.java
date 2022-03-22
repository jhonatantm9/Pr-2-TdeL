/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vista;

import Automata.*;
import java.util.ArrayList;
import java.util.Hashtable;
import javax.swing.JOptionPane;

/**
 * Ventana principal del AP. Aqui el usuario interactúa con todo el programa
 * @author jhona
 */
public class GUIPrincipal extends javax.swing.JFrame {

    ArrayList<Character> simbolosEntrada = new ArrayList<>();
    ArrayList<Character> simbolosPila = new ArrayList<>();
    ArrayList<String> estados = new ArrayList<>();
    AutomataPila ap = new AutomataPila();
    ControladorTabla controladorTabla = new ControladorTabla();
    ArrayList<Character> configInicialPila = new ArrayList<>();
    ArrayList<Transicion> transiciones = new ArrayList<>();
    Hashtable<String, Integer> contadorTransiciones = new Hashtable<>();
    
    
    /**
     * Creates new form GUIPrincipal
     */
    public GUIPrincipal() {
        initComponents();
        this.setLocationRelativeTo(null);
        cambiarEstadoBotones2(false); 
        cambiarEstadoBotones3(false);
        
        botonConfirmarAP.setEnabled(false);
        simbolosEntrada.add('¬');
        simbolosPila.add('▼');
        tablaPila.setModel(controladorTabla.actualizarPila());
        tablaTransiciones.setModel(controladorTabla.getModeloTabla());
        configInicialPila.add('▼');
        contadorTransiciones.put("A", 0);
    }
    
    public boolean elementoRepetido(String e, ArrayList<String> coleccion){
        for (String string : coleccion) {
            if(string.equals(e)){
                return true;
            }
        }
        return false;
    }
    
    public boolean elementoRepetido(char e, ArrayList<Character> coleccion){
        for (Character caracter : coleccion) {
            if(caracter.equals(e)){
                return true;
            }
        }
        return false;
    }
    
    /**
     * Retorna los elementos del array de símbolos de pila o entrada separados
     * con una coma ','
     * @param arrSimbolos Arreglo del que se tomarán los elementos
     * @return String con los símbolos
     */
    public String getSimbolosSeparados(ArrayList<Character> arrSimbolos){
        String simbolos = "";
        for (int i = 0; i < arrSimbolos.size() - 1; i++) {
            simbolos += arrSimbolos.get(i).toString();
            simbolos += ", ";
        }
        if(arrSimbolos.size() >= 1){
            simbolos += arrSimbolos.get(arrSimbolos.size() - 1).toString();
        }
        return(simbolos);
    }
    
    /**
     * Retorna los elementos del array de símbolos de pila o entrada separados
     * con un espacio ' '
     * @param arrSimbolos Arreglo del que se tomarán los elementos
     * @return String con los símbolos
     */
    public String getSimbolosContinuos(ArrayList<Character> arrSimbolos){
        String simbolos = "";
        for (int i = 0; i < arrSimbolos.size() - 1; i++) {
            simbolos += arrSimbolos.get(i).toString();
            simbolos += " ";
        }
        if(arrSimbolos.size() >= 1){
            simbolos += arrSimbolos.get(arrSimbolos.size() - 1).toString();
        }
        return(simbolos);
    }
    
    /**
     * Retorna los elementos del array de estados separados con una coma ','
     * @return String con los estados del automata
     */
    public String getEstados(){
        String simbolos = "";
        for (int i = 0; i < estados.size() - 1; i++) {
            simbolos += estados.get(i);
            simbolos += ", ";
        }
        if(estados.size() >= 1){
            simbolos += estados.get(estados.size() - 1);
        }
        return(simbolos);
    }
    
    /**
     * Agrega una transición ingresada por el usuario al automata de pila y la
     * envía a la parte gráfica para ser representada
     * @param estado Estado en el que estará la transición
     * @param tr Transición a agregar
     */
    public void agregarTransicion(String estado, Transicion tr){
        if(ap.getTransicion(estado, tr) == null){//Transición inexistente
            ap.agregarTransicion(estado, tr);
            if(tr.esAceptacion){
                String key = "A";
                contadorTransiciones.replace(key, contadorTransiciones.get(key) + 1);
                controladorTabla.agregarTransicionTabla(estado, key, tr);
            }else{
                boolean transicionExistente = false;
                int idTransicion = 0;
                for(int i = 0; i < transiciones.size(); i++){
                    if(transiciones.get(i).equals(tr)){
                        transicionExistente = true;
                        idTransicion = i + 1;
                        break;
                    }
                }
                String key;
                if(transicionExistente){
                    key = String.valueOf(idTransicion);
                    contadorTransiciones.replace(key, contadorTransiciones.get(key) + 1);
                }else{
                    key = String.valueOf(contadorTransiciones.size());
                    transiciones.add(tr);
                    contadorTransiciones.put(key, 1);
                }
                controladorTabla.agregarTransicionTabla(estado, "#" + key, tr);
            }
            textDescripcionTr.setText(controladorTabla.descripcionTransiciones(transiciones));
        }else{//Transicion existente
            JOptionPane.showMessageDialog(this, "YA EXISTE UNA TRANSICIÓN EN EL ESTADO DADO CON LOS SÍMBOLOS DE PILA"
                    + " Y ENTRADA DADOS\nSI DESEA REEMPLAZARLA PRIMERO ELIMINE LA EXISTENTE", "ERROR TRANSICIÓN NO AGREGADA",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Elimina una transición dada por el usuario del automata de pila y la
     * envía a la parte gráfica para ser eliminada
     * @param estado Estado en el que está la transición
     * @param simboloPila Simbolo de pila correspondiente a la transición
     * @param simboloEntrada Simbolo de entrada correspondiente a la transición
     * @return 
     */
    public boolean eliminarTransicion(String estado, Character simboloPila, Character simboloEntrada){
        Transicion tr = ap.getTransicion(estado, simboloPila, simboloEntrada);
        if(tr != null){
            ap.eliminarTransicion(estado, tr);
            if(tr.esAceptacion){
                contadorTransiciones.replace("A", contadorTransiciones.get("A") - 1);
            }else{
                for (int i = 0; i < transiciones.size(); i++) {
                    if(tr.equals(transiciones.get(i))){
                        if(contadorTransiciones.get(String.valueOf(i+1)) == 1){
                            eliminatTransicionDeContador(i+1);
                            transiciones.remove((int)i);
                        }else{
                            contadorTransiciones.replace(String.valueOf(i+1), contadorTransiciones.get(String.valueOf(i+1)) - 1);
                        }
                        break;
                    }
                }
            }
            controladorTabla.actualizarTransicionesTabla(transiciones, ap);
            textDescripcionTr.setText(controladorTabla.descripcionTransiciones(transiciones));
            return true;
        }
        return false;
    }
    
    /**
     * Elimina una transición del contador que almacena los id (números de cada
     * transición) y reacomoda el resto de los índices
     * @param keyInt Número de la transición a eliminar
     */
    public void eliminatTransicionDeContador(int keyInt){
        int keyNuevaInt = keyInt;
        String keyAntiguaStr = String.valueOf(keyInt + 1);
        int valorAntiguo;
        String keyNuevaStr = String.valueOf(keyInt);
        contadorTransiciones.remove(String.valueOf(keyInt));
        int tamanoContador = contadorTransiciones.size();
        for(int i = keyInt; i < tamanoContador; i++){
            valorAntiguo = contadorTransiciones.get(keyAntiguaStr);
            contadorTransiciones.remove(keyAntiguaStr);
            contadorTransiciones.put(keyNuevaStr, valorAntiguo);
            keyNuevaInt++;
            keyAntiguaStr = String.valueOf(keyNuevaInt + 1);
            keyNuevaStr = String.valueOf(keyNuevaInt);
        }
        contadorTransiciones.remove(keyAntiguaStr);
    }
    
    /**
     * Elimina todas las transiciónes que contengan el símbolo indicado
     * @param simbolo Símbolo de las transiciones a borrar
     * @param esSimboloDeEntrada Este parámetro indica si el símbolo dado corresponde
     * a uno de entrada (true) o si es un símbolo en la pila (false)
     */
    public void eliminarTransicionesConSimbolo(Character simbolo, boolean esSimboloDeEntrada){
        if(esSimboloDeEntrada){
            for (String estado : estados) {
                for(Character simboloPila: simbolosPila){
                    eliminarTransicion(estado, simboloPila, simbolo);
                }
            }
        }else{
            for (String estado : estados) {
                for(Character simboloEntrada: simbolosEntrada){
                    eliminarTransicion(estado, simbolo, simboloEntrada);
                }
            }
        }
    }
    
    /**
     * Reinicia la pila del autómata y en la parte gráfica
     */
    public void reiniciarPila(){
        AutomataPila.pila.removeAllElements();
        for(Character c: configInicialPila){
            if(c == '▼'){
                continue;
            }
            AutomataPila.pila.add(c.toString());
        }
        tablaPila.setModel(controladorTabla.actualizarPila());
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        botonEliminarSimboloEntrada = new javax.swing.JButton();
        botonEliminarEstado = new javax.swing.JButton();
        botonAddEstado = new javax.swing.JButton();
        textEstado = new javax.swing.JTextField();
        textSimboloEntrada = new javax.swing.JTextField();
        botonAddSimboloEntrada = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        labelEstadoI = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        comboEstadoI = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        botonEliminarSimboloPila = new javax.swing.JButton();
        botonAddSimboloPila = new javax.swing.JButton();
        textSimboloPila = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaPila = new javax.swing.JTable();
        jLabel8 = new javax.swing.JLabel();
        comboConfig = new javax.swing.JComboBox<>();
        botonEliminarConfig = new javax.swing.JButton();
        botonAddConfig = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        comboSimPilaTr = new javax.swing.JComboBox<>();
        comboEstadoTr = new javax.swing.JComboBox<>();
        comboSimEntTr = new javax.swing.JComboBox<>();
        comboOpPilaTr = new javax.swing.JComboBox<>();
        comboOpEstadoTr = new javax.swing.JComboBox<>();
        comboOpEntradaTr = new javax.swing.JComboBox<>();
        botonEliminarTransicion = new javax.swing.JButton();
        botonAddTransicion = new javax.swing.JButton();
        botonConfirmarAP = new javax.swing.JButton();
        botonEditarAP = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tablaTransiciones = new javax.swing.JTable();
        botonConfirmar1 = new javax.swing.JButton();
        textEntradaAR = new javax.swing.JTextField();
        labelSimbolosEntrada = new javax.swing.JLabel();
        labelEstados = new javax.swing.JLabel();
        labelSimbolosPila = new javax.swing.JLabel();
        labelConfig = new javax.swing.JLabel();
        botonTrAceptacion = new javax.swing.JToggleButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        textDescripcionTr = new javax.swing.JTextArea();
        textCadenaEntrada = new javax.swing.JTextField();
        botonReconocer = new javax.swing.JButton();
        jLabel15 = new javax.swing.JLabel();
        botonSiguiente = new javax.swing.JButton();
        jLabel16 = new javax.swing.JLabel();
        textEstadoActual = new javax.swing.JTextField();
        textSimboloActual = new javax.swing.JTextField();
        botonIniciar = new javax.swing.JButton();
        jLabel14 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        textCadenaLeida = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        labelTransicionUsada = new javax.swing.JLabel();
        jLabelFondo = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("<Automatas de Pila>");
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        botonEliminarSimboloEntrada.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        botonEliminarSimboloEntrada.setText("Eliminar");
        botonEliminarSimboloEntrada.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonEliminarSimboloEntradaActionPerformed(evt);
            }
        });
        getContentPane().add(botonEliminarSimboloEntrada, new org.netbeans.lib.awtextra.AbsoluteConstraints(152, 49, -1, -1));

        botonEliminarEstado.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        botonEliminarEstado.setText("Eliminar");
        botonEliminarEstado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonEliminarEstadoActionPerformed(evt);
            }
        });
        getContentPane().add(botonEliminarEstado, new org.netbeans.lib.awtextra.AbsoluteConstraints(152, 116, -1, -1));

        botonAddEstado.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        botonAddEstado.setText("Añadir");
        botonAddEstado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonAddEstadoActionPerformed(evt);
            }
        });
        getContentPane().add(botonAddEstado, new org.netbeans.lib.awtextra.AbsoluteConstraints(75, 116, -1, -1));

        textEstado.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        textEstado.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        textEstado.setText("S0");
        getContentPane().add(textEstado, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 117, 59, -1));

        textSimboloEntrada.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        textSimboloEntrada.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        textSimboloEntrada.setText("0");
        getContentPane().add(textSimboloEntrada, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, 59, -1));

        botonAddSimboloEntrada.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        botonAddSimboloEntrada.setText("Añadir");
        botonAddSimboloEntrada.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonAddSimboloEntradaActionPerformed(evt);
            }
        });
        getContentPane().add(botonAddSimboloEntrada, new org.netbeans.lib.awtextra.AbsoluteConstraints(75, 49, -1, -1));

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel2.setText("1) Definir símbolos de entrada:");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 26, -1, -1));

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel3.setText("2) Definir conjunto de estados:");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 81, -1, -1));

        labelEstadoI.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        labelEstadoI.setText("Estado inicial: NO SELECCIONADO");
        getContentPane().add(labelEstadoI, new org.netbeans.lib.awtextra.AbsoluteConstraints(225, 271, -1, -1));

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel7.setText("4) Definir estado inicial:");
        getContentPane().add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 271, -1, -1));

        comboEstadoI.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        comboEstadoI.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboEstadoIActionPerformed(evt);
            }
        });
        getContentPane().add(comboEstadoI, new org.netbeans.lib.awtextra.AbsoluteConstraints(153, 268, 63, -1));

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel4.setText("3) Definir símbolos en la pila:");
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 147, -1, -1));

        botonEliminarSimboloPila.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        botonEliminarSimboloPila.setText("Eliminar");
        botonEliminarSimboloPila.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonEliminarSimboloPilaActionPerformed(evt);
            }
        });
        getContentPane().add(botonEliminarSimboloPila, new org.netbeans.lib.awtextra.AbsoluteConstraints(152, 182, -1, -1));

        botonAddSimboloPila.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        botonAddSimboloPila.setText("Añadir");
        botonAddSimboloPila.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonAddSimboloPilaActionPerformed(evt);
            }
        });
        getContentPane().add(botonAddSimboloPila, new org.netbeans.lib.awtextra.AbsoluteConstraints(75, 182, -1, -1));

        textSimboloPila.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        textSimboloPila.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        textSimboloPila.setText("0");
        getContentPane().add(textSimboloPila, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 183, 59, -1));

        tablaPila.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        tablaPila.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null}
            },
            new String [] {
                "Pila"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tablaPila.setEnabled(false);
        jScrollPane1.setViewportView(tablaPila);
        if (tablaPila.getColumnModel().getColumnCount() > 0) {
            tablaPila.getColumnModel().getColumn(0).setResizable(false);
        }

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(958, 24, 81, 272));

        jLabel8.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel8.setText("5) Definir configuración inicial de la pila:");
        getContentPane().add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 304, -1, -1));

        comboConfig.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        comboConfig.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboConfigActionPerformed(evt);
            }
        });
        getContentPane().add(comboConfig, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 333, 63, -1));

        botonEliminarConfig.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        botonEliminarConfig.setText("Eliminar cima");
        botonEliminarConfig.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonEliminarConfigActionPerformed(evt);
            }
        });
        getContentPane().add(botonEliminarConfig, new org.netbeans.lib.awtextra.AbsoluteConstraints(156, 332, -1, -1));

        botonAddConfig.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        botonAddConfig.setText("Añadir");
        botonAddConfig.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonAddConfigActionPerformed(evt);
            }
        });
        getContentPane().add(botonAddConfig, new org.netbeans.lib.awtextra.AbsoluteConstraints(79, 332, -1, -1));

        jLabel9.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel9.setText("6) Definir transiciones:");
        getContentPane().add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 375, -1, -1));

        comboSimPilaTr.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        comboSimPilaTr.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Simbolo pila", "▼" }));
        getContentPane().add(comboSimPilaTr, new org.netbeans.lib.awtextra.AbsoluteConstraints(86, 404, -1, -1));

        comboEstadoTr.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        comboEstadoTr.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Estado" }));
        getContentPane().add(comboEstadoTr, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 404, -1, -1));

        comboSimEntTr.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        comboSimEntTr.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Simbolo entrada", "¬" }));
        getContentPane().add(comboSimEntTr, new org.netbeans.lib.awtextra.AbsoluteConstraints(191, 404, -1, -1));

        comboOpPilaTr.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        comboOpPilaTr.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Op pila", "Apilar", "Desapilar", "Ninguna", "Replace" }));
        comboOpPilaTr.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboOpPilaTrActionPerformed(evt);
            }
        });
        getContentPane().add(comboOpPilaTr, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 447, -1, -1));

        comboOpEstadoTr.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        comboOpEstadoTr.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Op estado", "Ninguna" }));
        getContentPane().add(comboOpEstadoTr, new org.netbeans.lib.awtextra.AbsoluteConstraints(98, 447, -1, -1));

        comboOpEntradaTr.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        comboOpEntradaTr.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Op entrada", "Avance", "Retenga" }));
        getContentPane().add(comboOpEntradaTr, new org.netbeans.lib.awtextra.AbsoluteConstraints(195, 447, -1, -1));

        botonEliminarTransicion.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        botonEliminarTransicion.setText("Eliminar transición");
        botonEliminarTransicion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonEliminarTransicionActionPerformed(evt);
            }
        });
        getContentPane().add(botonEliminarTransicion, new org.netbeans.lib.awtextra.AbsoluteConstraints(161, 530, -1, -1));

        botonAddTransicion.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        botonAddTransicion.setText("Añadir transición");
        botonAddTransicion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonAddTransicionActionPerformed(evt);
            }
        });
        getContentPane().add(botonAddTransicion, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 530, -1, -1));

        botonConfirmarAP.setFont(new java.awt.Font("Rockwell", 0, 14)); // NOI18N
        botonConfirmarAP.setText("Confirmar autómata");
        botonConfirmarAP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonConfirmarAPActionPerformed(evt);
            }
        });
        getContentPane().add(botonConfirmarAP, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 573, -1, -1));

        botonEditarAP.setFont(new java.awt.Font("Rockwell", 0, 14)); // NOI18N
        botonEditarAP.setText("Editar automata");
        botonEditarAP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonEditarAPActionPerformed(evt);
            }
        });
        getContentPane().add(botonEditarAP, new org.netbeans.lib.awtextra.AbsoluteConstraints(179, 573, -1, -1));

        tablaTransiciones.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        tablaTransiciones.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tablaTransiciones.setEnabled(false);
        tablaTransiciones.getTableHeader().setReorderingAllowed(false);
        jScrollPane2.setViewportView(tablaTransiciones);
        if (tablaTransiciones.getColumnModel().getColumnCount() > 0) {
            tablaTransiciones.getColumnModel().getColumn(0).setResizable(false);
            tablaTransiciones.getColumnModel().getColumn(1).setResizable(false);
            tablaTransiciones.getColumnModel().getColumn(2).setResizable(false);
            tablaTransiciones.getColumnModel().getColumn(3).setResizable(false);
        }

        getContentPane().add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(534, 24, 391, 272));

        botonConfirmar1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        botonConfirmar1.setText("Confirmar símbolos y estados");
        botonConfirmar1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonConfirmar1ActionPerformed(evt);
            }
        });
        getContentPane().add(botonConfirmar1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 225, -1, -1));

        textEntradaAR.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        textEntradaAR.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        getContentPane().add(textEntradaAR, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 489, 69, -1));

        labelSimbolosEntrada.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        labelSimbolosEntrada.setText("Símbolos de entrada: ¬");
        getContentPane().add(labelSimbolosEntrada, new org.netbeans.lib.awtextra.AbsoluteConstraints(241, 53, -1, -1));

        labelEstados.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        labelEstados.setText("Estados: ");
        getContentPane().add(labelEstados, new org.netbeans.lib.awtextra.AbsoluteConstraints(241, 120, -1, -1));

        labelSimbolosPila.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        labelSimbolosPila.setText("Símbolos en la pila: ▼");
        getContentPane().add(labelSimbolosPila, new org.netbeans.lib.awtextra.AbsoluteConstraints(241, 186, -1, -1));

        labelConfig.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        labelConfig.setText("Config. inicial: ▼");
        getContentPane().add(labelConfig, new org.netbeans.lib.awtextra.AbsoluteConstraints(277, 336, -1, -1));

        botonTrAceptacion.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        botonTrAceptacion.setText("Aceptación");
        botonTrAceptacion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonTrAceptacionActionPerformed(evt);
            }
        });
        getContentPane().add(botonTrAceptacion, new org.netbeans.lib.awtextra.AbsoluteConstraints(328, 403, -1, -1));

        textDescripcionTr.setEditable(false);
        textDescripcionTr.setColumns(20);
        textDescripcionTr.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        textDescripcionTr.setRows(5);
        jScrollPane3.setViewportView(textDescripcionTr);

        getContentPane().add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(534, 321, 391, 105));

        textCadenaEntrada.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        textCadenaEntrada.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        getContentPane().add(textCadenaEntrada, new org.netbeans.lib.awtextra.AbsoluteConstraints(535, 445, 124, -1));

        botonReconocer.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        botonReconocer.setText("Reconocer");
        botonReconocer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonReconocerActionPerformed(evt);
            }
        });
        getContentPane().add(botonReconocer, new org.netbeans.lib.awtextra.AbsoluteConstraints(677, 444, -1, -1));

        jLabel15.setFont(new java.awt.Font("Rockwell", 0, 14)); // NOI18N
        jLabel15.setText("Estado actual");
        getContentPane().add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(535, 515, -1, -1));

        botonSiguiente.setText("Siguiente");
        botonSiguiente.setEnabled(false);
        botonSiguiente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonSiguienteActionPerformed(evt);
            }
        });
        getContentPane().add(botonSiguiente, new org.netbeans.lib.awtextra.AbsoluteConstraints(988, 514, -1, -1));

        jLabel16.setFont(new java.awt.Font("Rockwell", 0, 14)); // NOI18N
        jLabel16.setText("Símbolo a leer");
        getContentPane().add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(809, 515, -1, -1));

        textEstadoActual.setEditable(false);
        getContentPane().add(textEstadoActual, new org.netbeans.lib.awtextra.AbsoluteConstraints(638, 515, 58, -1));

        textSimboloActual.setEditable(false);
        getContentPane().add(textSimboloActual, new org.netbeans.lib.awtextra.AbsoluteConstraints(912, 515, 58, -1));

        botonIniciar.setText("Iniciar");
        botonIniciar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonIniciarActionPerformed(evt);
            }
        });
        getContentPane().add(botonIniciar, new org.netbeans.lib.awtextra.AbsoluteConstraints(714, 514, 77, -1));

        jLabel14.setFont(new java.awt.Font("Rockwell", 1, 18)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(51, 0, 51));
        jLabel14.setText("Simular paso a paso");
        getContentPane().add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(535, 490, -1, -1));

        jLabel17.setFont(new java.awt.Font("Rockwell", 0, 14)); // NOI18N
        jLabel17.setText("Cadena leída");
        getContentPane().add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(534, 580, -1, -1));

        textCadenaLeida.setEditable(false);
        getContentPane().add(textCadenaLeida, new org.netbeans.lib.awtextra.AbsoluteConstraints(638, 580, 153, -1));

        jLabel18.setFont(new java.awt.Font("Rockwell", 0, 14)); // NOI18N
        jLabel18.setText("Transición usada:");
        getContentPane().add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(534, 548, -1, -1));

        labelTransicionUsada.setFont(new java.awt.Font("Rockwell", 0, 14)); // NOI18N
        labelTransicionUsada.setText("|");
        getContentPane().add(labelTransicionUsada, new org.netbeans.lib.awtextra.AbsoluteConstraints(655, 548, -1, -1));

        jLabelFondo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/Fondo3.png"))); // NOI18N
        getContentPane().add(jLabelFondo, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1080, 620));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void botonEliminarSimboloEntradaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonEliminarSimboloEntradaActionPerformed
        if(this.simbolosEntrada.contains(textSimboloEntrada.getText().trim().charAt(0))){
            if(textSimboloEntrada.getText().trim().charAt(0) == '¬'){
                JOptionPane.showMessageDialog(this, "NO SE PUEDE ELIMINAR EL SÍMBOLO DE FIN DE SECUENCIA", "ERROR",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            this.simbolosEntrada.remove((Character)textSimboloEntrada.getText().trim().charAt(0));
            comboSimEntTr.removeItem(textSimboloEntrada.getText());
            comboConfig.removeItem(textSimboloEntrada.getText());
            eliminarTransicionesConSimbolo(textSimboloEntrada.getText().trim().charAt(0), true);
            labelSimbolosEntrada.setText("Símbolos de entrada: " + getSimbolosSeparados(simbolosEntrada));
        }
    }//GEN-LAST:event_botonEliminarSimboloEntradaActionPerformed

    private void botonEliminarEstadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonEliminarEstadoActionPerformed
        String estado = textEstado.getText().trim();
        if(estados.contains(estado)){
            this.estados.remove((String)estado);
            ap.eliminarEstado(estado);
            comboEstadoI.removeItem(estado);
            comboEstadoTr.removeItem(estado);
            comboOpEstadoTr.removeItem(estado);
            labelEstados.setText("Estados: " + getEstados());
        }
    }//GEN-LAST:event_botonEliminarEstadoActionPerformed

    private void botonAddEstadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonAddEstadoActionPerformed
        if (!textEstado.getText().trim().isEmpty() && !elementoRepetido(textEstado.getText().trim(), estados)) {
            estados.add(textEstado.getText().trim());
            ap.agregarEstado(textEstado.getText().trim());
            comboEstadoI.addItem(textEstado.getText());
            comboEstadoTr.addItem(textEstado.getText());
            comboOpEstadoTr.addItem(textEstado.getText());
            labelEstados.setText("Estados: " + getEstados());
        }
    }//GEN-LAST:event_botonAddEstadoActionPerformed

    private void botonAddSimboloEntradaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonAddSimboloEntradaActionPerformed
        if (!textSimboloEntrada.getText().trim().isEmpty() && !elementoRepetido(textSimboloEntrada.getText().trim().charAt(0), simbolosEntrada)) {
            simbolosEntrada.add(textSimboloEntrada.getText().trim().charAt(0));
            comboSimEntTr.addItem(textSimboloEntrada.getText());
            labelSimbolosEntrada.setText("Símbolos de entrada: " + getSimbolosSeparados(simbolosEntrada));
        }
    }//GEN-LAST:event_botonAddSimboloEntradaActionPerformed

    private void comboEstadoIActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboEstadoIActionPerformed
        if (comboEstadoI.getItemCount() > 0) {
            if(comboEstadoI.getSelectedItem() != null){
                ap.setEstadoInicial(comboEstadoI.getSelectedItem().toString());
                labelEstadoI.setText("Estado inicial: " + comboEstadoI.getSelectedItem().toString());
            }
        }
    }//GEN-LAST:event_comboEstadoIActionPerformed

    private void botonEliminarSimboloPilaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonEliminarSimboloPilaActionPerformed
        if(this.simbolosPila.contains(textSimboloPila.getText().trim().charAt(0))){
            if(textSimboloPila.getText().trim().charAt(0) == '▼'){
                JOptionPane.showMessageDialog(this, "NO SE PUEDE ELIMINAR EL SÍMBOLO DE PILA VACIA", "ERROR",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }            
            this.simbolosPila.remove((Character)textSimboloPila.getText().trim().charAt(0));
            comboSimPilaTr.removeItem(textSimboloPila.getText());
            comboConfig.removeItem(textSimboloPila.getText());
            eliminarTransicionesConSimbolo(textSimboloEntrada.getText().trim().charAt(0), false);
            while(configInicialPila.contains(textSimboloPila.getText().trim().charAt(0))){
                configInicialPila.remove((Character)textSimboloPila.getText().trim().charAt(0));
            }
            reiniciarPila();
            labelSimbolosPila.setText("Símbolos en la pila: " + getSimbolosSeparados(simbolosPila));
        }
    }//GEN-LAST:event_botonEliminarSimboloPilaActionPerformed

    private void botonAddSimboloPilaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonAddSimboloPilaActionPerformed
        if (!textSimboloPila.getText().trim().isEmpty() && !elementoRepetido(textSimboloPila.getText().trim().charAt(0), simbolosPila)) {
            simbolosPila.add(textSimboloPila.getText().trim().charAt(0));
            comboSimPilaTr.addItem(textSimboloPila.getText());
            comboConfig.addItem(textSimboloPila.getText());
            labelSimbolosPila.setText("Símbolos en la pila: " + getSimbolosSeparados(simbolosPila));
        }
    }//GEN-LAST:event_botonAddSimboloPilaActionPerformed

    private void comboConfigActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboConfigActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_comboConfigActionPerformed

    private void botonEliminarConfigActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonEliminarConfigActionPerformed
        if(!AutomataPila.pila.empty()){
            AutomataPila.pila.pop();
            configInicialPila.remove(configInicialPila.size() - 1);
            labelConfig.setText("Config. inicial: " + getSimbolosContinuos(configInicialPila));
            tablaPila.setModel(controladorTabla.actualizarPila());
        }
    }//GEN-LAST:event_botonEliminarConfigActionPerformed

    private void botonAddConfigActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonAddConfigActionPerformed
        AutomataPila.pila.push(comboConfig.getSelectedItem().toString());
        configInicialPila.add(comboConfig.getSelectedItem().toString().charAt(0));
        labelConfig.setText("Config. inicial: " + getSimbolosContinuos(configInicialPila));
        tablaPila.setModel(controladorTabla.actualizarPila());
    }//GEN-LAST:event_botonAddConfigActionPerformed

    private void comboOpPilaTrActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboOpPilaTrActionPerformed
        if(comboOpPilaTr.getSelectedIndex() == 1 || comboOpPilaTr.getSelectedIndex() == 4){
            textEntradaAR.setEnabled(true);
        }else{
            textEntradaAR.setEnabled(false);
        }
    }//GEN-LAST:event_comboOpPilaTrActionPerformed

    private void botonEliminarTransicionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonEliminarTransicionActionPerformed
        if(comboEstadoTr.getSelectedIndex() != 0 && comboSimEntTr.getSelectedIndex() != 0 && comboSimPilaTr.getSelectedIndex() != 0){
            char simboloPila = comboSimPilaTr.getSelectedItem().toString().charAt(0);
            char simboloEntrada = comboSimEntTr.getSelectedItem().toString().charAt(0);
            String estado = comboEstadoTr.getSelectedItem().toString();
            if(!eliminarTransicion(estado, simboloPila, simboloEntrada)){
                JOptionPane.showMessageDialog(this, "NO EXISTE UNA TRANSICION EN EL ESTADO INGRESADO CON LOS SIMBOLOS DADOS",
                    "ERROR",JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }//GEN-LAST:event_botonEliminarTransicionActionPerformed

    private void botonAddTransicionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonAddTransicionActionPerformed
        if(comboEstadoTr.getSelectedIndex() != 0 && comboSimEntTr.getSelectedIndex() != 0 && comboSimPilaTr.getSelectedIndex() != 0){
            char simboloPila = comboSimPilaTr.getSelectedItem().toString().charAt(0);
            char simboloEntrada = comboSimEntTr.getSelectedItem().toString().charAt(0);
            String estado = comboEstadoTr.getSelectedItem().toString();
            if(botonTrAceptacion.isSelected()){
                TransicionAceptacion tr = new TransicionAceptacion(simboloPila, simboloEntrada);
                agregarTransicion(estado, tr);
                botonTrAceptacion.doClick();
            }else{
                if(comboOpEntradaTr.getSelectedIndex() != 0 && comboOpEstadoTr.getSelectedIndex() != 0 && comboOpPilaTr.getSelectedIndex() != 0){
                    if(simboloPila == '▼' && (comboOpPilaTr.getSelectedIndex() == 2 || comboOpPilaTr.getSelectedIndex() == 4)){
                        JOptionPane.showMessageDialog(rootPane, "NO SE PUEDE REALIZAR LA OPERACIÓN REPLACE O APILAR CUANDO EL"
                         + "SIMBOLO EN LA PILA ES '▼'", "ERROR EN LA TRANSICIÓN", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    String opEntrada = comboOpEntradaTr.getSelectedItem().toString();
                    String opPila = comboOpPilaTr.getSelectedItem().toString();
                    String opEstado = comboOpEstadoTr.getSelectedItem().toString();
                    if(comboOpEstadoTr.getSelectedIndex() == 1){
                        opEstado = estado;
                    }
                    if(comboOpPilaTr.getSelectedIndex() == 1 || comboOpPilaTr.getSelectedIndex() == 4){
                        if(cadenaConSimbolosPila(textEntradaAR.getText())){
                            TransicionComun tr = new TransicionComun(opPila, opEstado, opEntrada, textEntradaAR.getText(),
                                    simboloPila, simboloEntrada);
                            System.out.println(tr.toString());
                            agregarTransicion(estado, tr);
                        }
                    }else{
                        TransicionComun tr = new TransicionComun(opPila, opEstado, opEntrada, simboloPila, simboloEntrada);
                        System.out.println(tr.toString());
                        agregarTransicion(estado, tr);
                    }
                }else{
                    JOptionPane.showMessageDialog(rootPane, "NO SE HAN SELECCIONADO TODAS LAS OPERACIONES DE TRANSICIÓN",
                        "ERROR EN LA TRANSICIÓN", JOptionPane.ERROR_MESSAGE);
                }
            }
        }else{
            JOptionPane.showMessageDialog(rootPane, "NO SE HAN SELECCIONADO EL ESTADO Y LOS SÍMBOLOS DE PILA Y ENTRADA",
                    "ERROR EN LA TRANSICIÓN", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_botonAddTransicionActionPerformed

    private void botonConfirmarAPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonConfirmarAPActionPerformed
        if(!labelEstadoI.getText().equals("Estado inicial: NO SELECCIONADO")){
            cambiarEstadoBotones1(false);
            cambiarEstadoBotones2(false);  
            cambiarEstadoBotones3(true);
        }else{
            JOptionPane.showMessageDialog(rootPane, "NO SE HA SELECCIONADO NINGUN ESTADO DE INICIAL",
                "ERROR EN EL AUTOMATA", JOptionPane.ERROR_MESSAGE);
        }

    }//GEN-LAST:event_botonConfirmarAPActionPerformed

    private void botonEditarAPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonEditarAPActionPerformed
        cambiarEstadoBotones1(true);
        botonConfirmar1.setEnabled(true);
        cambiarEstadoBotones2(false);
        cambiarEstadoBotones3(false);
    }//GEN-LAST:event_botonEditarAPActionPerformed

    private void botonConfirmar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonConfirmar1ActionPerformed
        if(estados.size() >= 1 && simbolosEntrada.size() >= 2 && simbolosPila.size() >= 2){
            cambiarEstadoBotones1(false);
            botonConfirmar1.setEnabled(false);
            cambiarEstadoBotones2(true);
            botonConfirmarAP.setEnabled(true);
            
            ap.inicializarEstados(simbolosPila, simbolosEntrada);
            if(controladorTabla.numEstados == 0){
                tablaTransiciones.setModel(controladorTabla.crearTabla(simbolosEntrada, simbolosPila, estados));
            }else{
                controladorTabla.actualizarTabla(simbolosEntrada, simbolosPila, transiciones, ap);
                tablaTransiciones.setModel(controladorTabla.getModeloTabla());
            }            
        }else{
            JOptionPane.showMessageDialog(this, "DEBE HABER POR LO MENOS 1 ESTADO, 2 SÍMBOLOS DE ENTRADA\nY 2 SÍMBOLOS EN"
                    + " LA PILA PARA CREAR LAS TRANSICIONES", "ERROR EN EL AUTOMATA", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_botonConfirmar1ActionPerformed

    private void botonTrAceptacionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonTrAceptacionActionPerformed
        if(botonTrAceptacion.isSelected()){
            comboOpEntradaTr.setEnabled(false);
            comboOpPilaTr.setEnabled(false);
            comboOpEstadoTr.setEnabled(false);
        }else{
            comboOpEntradaTr.setEnabled(true);
            comboOpPilaTr.setEnabled(true);
            comboOpEstadoTr.setEnabled(true);
        }
    }//GEN-LAST:event_botonTrAceptacionActionPerformed

    private void botonReconocerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonReconocerActionPerformed
        String cadena = textCadenaEntrada.getText().trim();
        if(!cadenaConSimbolosEntrada(cadena)){
            JOptionPane.showMessageDialog(this, "LA CADENA INGRESADA DEBE ESTAR COMPUESTA POR LOS SÍMBOLOS DE ENTRADA"
                    + " DEL AUTOMATA", "ERROR", JOptionPane.ERROR_MESSAGE);
        }else{
            if(cadena.equals("") || cadena.charAt(cadena.length() - 1) != '¬'){
                cadena += "¬";
            }
            if(ap.reconocer(cadena)){
                JOptionPane.showMessageDialog(this, "Cadena reconocida", "Reconocedor", JOptionPane.INFORMATION_MESSAGE);
            }else{
                JOptionPane.showMessageDialog(this, "Cadena no reconocida", "Reconocedor", JOptionPane.WARNING_MESSAGE);
            }
            reiniciarPila();
        }
    }//GEN-LAST:event_botonReconocerActionPerformed

    private void botonSiguienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonSiguienteActionPerformed
        int indiceSimboloALeer = textCadenaLeida.getText().length();
        String estadoActual = textEstadoActual.getText();
        Character simboloEntrada = textCadenaEntrada.getText().charAt(indiceSimboloALeer);
        if(!ap.reconocerSimbolo(simboloEntrada, estadoActual)){
            JOptionPane.showMessageDialog(this, "Cadena no reconocida", "Reconocedor", JOptionPane.WARNING_MESSAGE);
            labelTransicionUsada.setText("Rechace");
            cambiarIniciarAReiniciar();
        }else{
            Character simboloPila;
            if(AutomataPila.pila.empty()){
                simboloPila = '▼';
            }else{
                simboloPila = AutomataPila.pila.peek().charAt(0);
            }
            Transicion tr = ap.getTransicion(estadoActual, simboloPila, simboloEntrada);
            if(tr.esAceptacion){
                JOptionPane.showMessageDialog(this, "Cadena reconocida", "Reconocedor", JOptionPane.INFORMATION_MESSAGE);
                labelTransicionUsada.setText("Acepte");
                cambiarIniciarAReiniciar();
            }else{
                TransicionComun transicion = (TransicionComun) tr;
                labelTransicionUsada.setText(textEstadoActual.getText() + ": " + tr.toString());
                textEstadoActual.setText(transicion.operacionEstado);
                ap.operarPila(transicion.operacionPila, transicion.simboloAR);
                tablaPila.setModel(controladorTabla.actualizarPila());
                if(transicion.operacionEntrada.equals("Avance")){
                    String cadenaLeida = textCadenaLeida.getText().concat(simboloEntrada.toString()); 
                    textCadenaLeida.setText(cadenaLeida);
                    String simboloActual;
                    if(cadenaLeida.length() != textCadenaEntrada.getText().length()){
                        simboloActual = textCadenaEntrada.getText().substring(cadenaLeida.length(), cadenaLeida.length() + 1);
                        textSimboloActual.setText(simboloActual);                        
                    }else{
                        JOptionPane.showMessageDialog(this, "Cadena no reconocida", "Reconocedor", JOptionPane.WARNING_MESSAGE);
                        labelTransicionUsada.setText("Rechazo por no haber más símbolos a reconocer");
                        reiniciarPila();
                    }                    
                }
            }
            
        }
    }//GEN-LAST:event_botonSiguienteActionPerformed

    private void botonIniciarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonIniciarActionPerformed
        if(botonIniciar.getText().equals("Iniciar")){
            String cadena = textCadenaEntrada.getText().trim();
            if(!cadenaConSimbolosEntrada(cadena)){
                JOptionPane.showMessageDialog(this, "LA CADENA INGRESADA DEBE ESTAR COMPUESTA POR LOS SÍMBOLOS DE ENTRADA"
                        + " DEL AUTOMATA", "ERROR", JOptionPane.ERROR_MESSAGE);
            }else{
                if(cadena.equals("") || cadena.charAt(cadena.length() - 1) != '¬'){
                    cadena += "¬";
                    textCadenaEntrada.setText(cadena);
                }
                botonIniciar.setEnabled(false);
                botonSiguiente.setEnabled(true);
                textCadenaEntrada.setEditable(false);
                textSimboloActual.setText(cadena.substring(0, 1));            
                String estadoInicial = comboEstadoI.getSelectedItem().toString();
                textEstadoActual.setText(estadoInicial);
                botonReconocer.setEnabled(false);
            }
        }else{
            reiniciarPila();
            textCadenaLeida.setText("");
            textEstadoActual.setText("");
            textSimboloActual.setText("");
            botonIniciar.setText("Iniciar");
            labelTransicionUsada.setText("|");
            textCadenaEntrada.setEditable(true);
            botonReconocer.setEnabled(true);
        }
    }//GEN-LAST:event_botonIniciarActionPerformed

    /**
     * Activa o desactiva los controles que están antes del botón de 'Confirmar
     * símbolos y estados'
     * @param b true para activar, false para desactivar
     */
    public void cambiarEstadoBotones1(boolean b){
        textSimboloEntrada.setEnabled(b);
        botonAddSimboloEntrada.setEnabled(b);
        botonEliminarSimboloEntrada.setEnabled(b);
        textSimboloPila.setEnabled(b);
        botonAddSimboloPila.setEnabled(b);
        botonEliminarSimboloPila.setEnabled(b);
        textEstado.setEnabled(b);
        botonAddEstado.setEnabled(b);
        botonEliminarEstado.setEnabled(b);
    }
    
    /**
     * Activa o desactiva los controles que están después del botón de 'Confirmar
     * símbolos y estados', a excepción del botón 'Editaar autómata'
     * @param b true para activar, false para desactivar
     */
    public void cambiarEstadoBotones2(boolean b){
        comboEstadoI.setEnabled(b);
        comboConfig.setEnabled(b);
        botonAddConfig.setEnabled(b);
        botonEliminarConfig.setEnabled(b);
        comboEstadoTr.setEnabled(b);
        comboSimEntTr.setEnabled(b);
        comboSimPilaTr.setEnabled(b);
        botonTrAceptacion.setEnabled(b);
        comboOpEntradaTr.setEnabled(b);
        comboOpEstadoTr.setEnabled(b);
        comboOpPilaTr.setEnabled(b);
        textEntradaAR.setEnabled(b);
        botonAddTransicion.setEnabled(b);
        botonEliminarTransicion.setEnabled(b);
        botonConfirmarAP.setEnabled(b);
    }
    
    /**
     * Activa o desactiva los controles que están en la parte del reconocedor
     * en donde el usuario ingresa una hilera para ser leída. También reinicia
     * los labels y cajas de texto
     * @param b true para activar, false para desactivar
     */
    public void cambiarEstadoBotones3(boolean b){
        textCadenaEntrada.setEditable(b);
        botonReconocer.setEnabled(b);
        botonIniciar.setEnabled(b);
        textCadenaLeida.setText("");
        textEstadoActual.setText("");
        textSimboloActual.setText("");
        botonIniciar.setText("Iniciar");
        labelTransicionUsada.setText("|");
        textCadenaEntrada.setEditable(b);
    }
    
    /**
     * Reinicia los botones, labels y cajas de texto de la parte gráfica del
     * reconocedor
     */
    public void terminarReconocedor(){
        textCadenaLeida.setText("");
        textEstadoActual.setText("");
        textSimboloActual.setText("");
        botonIniciar.setText("Iniciar");
        labelTransicionUsada.setText("|");
        textCadenaEntrada.setEditable(false);
        botonReconocer.setEnabled(false);
    }
    
    /**
     * Cambia el reconocedor para que se pueda ingresar una nueva hilera a reconocer
     * luego de un reconocimiento paso a paso
     */
    public void cambiarIniciarAReiniciar(){
        botonIniciar.setText("Reiniciar");
        botonIniciar.setEnabled(true);
        botonSiguiente.setEnabled(false);
    }
    
    public boolean cadenaConSimbolosPila(String cadena){
        char[] caracteres = cadena.toCharArray();
        for(char c: caracteres){
            if(!simbolosPila.contains(c)){
                return false;
            }
        }
        return true;
    }
    
    public boolean cadenaConSimbolosEntrada(String cadena){
        char[] caracteres = cadena.toCharArray();
        for(char c: caracteres){
            if(!simbolosEntrada.contains(c)){
                return false;
            }
        }
        return true;
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(GUIPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GUIPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GUIPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GUIPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new GUIPrincipal().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton botonAddConfig;
    private javax.swing.JButton botonAddEstado;
    private javax.swing.JButton botonAddSimboloEntrada;
    private javax.swing.JButton botonAddSimboloPila;
    private javax.swing.JButton botonAddTransicion;
    private javax.swing.JButton botonConfirmar1;
    private javax.swing.JButton botonConfirmarAP;
    private javax.swing.JButton botonEditarAP;
    private javax.swing.JButton botonEliminarConfig;
    private javax.swing.JButton botonEliminarEstado;
    private javax.swing.JButton botonEliminarSimboloEntrada;
    private javax.swing.JButton botonEliminarSimboloPila;
    private javax.swing.JButton botonEliminarTransicion;
    private javax.swing.JButton botonIniciar;
    private javax.swing.JButton botonReconocer;
    private javax.swing.JButton botonSiguiente;
    private javax.swing.JToggleButton botonTrAceptacion;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox<String> comboConfig;
    private javax.swing.JComboBox<String> comboEstadoI;
    private javax.swing.JComboBox<String> comboEstadoTr;
    private javax.swing.JComboBox<String> comboOpEntradaTr;
    private javax.swing.JComboBox<String> comboOpEstadoTr;
    private javax.swing.JComboBox<String> comboOpPilaTr;
    private javax.swing.JComboBox<String> comboSimEntTr;
    private javax.swing.JComboBox<String> comboSimPilaTr;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabelFondo;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JLabel labelConfig;
    private javax.swing.JLabel labelEstadoI;
    private javax.swing.JLabel labelEstados;
    private javax.swing.JLabel labelSimbolosEntrada;
    private javax.swing.JLabel labelSimbolosPila;
    private javax.swing.JLabel labelTransicionUsada;
    private javax.swing.JTable tablaPila;
    private javax.swing.JTable tablaTransiciones;
    private javax.swing.JTextField textCadenaEntrada;
    private javax.swing.JTextField textCadenaLeida;
    private javax.swing.JTextArea textDescripcionTr;
    private javax.swing.JTextField textEntradaAR;
    private javax.swing.JTextField textEstado;
    private javax.swing.JTextField textEstadoActual;
    private javax.swing.JTextField textSimboloActual;
    private javax.swing.JTextField textSimboloEntrada;
    private javax.swing.JTextField textSimboloPila;
    // End of variables declaration//GEN-END:variables
}
