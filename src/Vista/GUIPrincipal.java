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
 *
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
            JOptionPane.showMessageDialog(this, "YA EXISTE UNA TRANSICIÓN EN EL ESTADO DADO SON LOS SÍMBOLOS DE PILA"
                    + " Y ENTRADA DADOS\nSI DESEA REEMPLAZARLA PRIMERO ELIMINE LA EXISTENTE", "ERROR TRANSICIÓN NO AGREGADA",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
    
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
                            transiciones.remove(i);
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
        labelFin = new javax.swing.JLabel();
        labelResultado = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        textCadenaLeida = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        botonEliminarSimboloEntrada.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        botonEliminarSimboloEntrada.setText("Eliminar");
        botonEliminarSimboloEntrada.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonEliminarSimboloEntradaActionPerformed(evt);
            }
        });

        botonEliminarEstado.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        botonEliminarEstado.setText("Eliminar");
        botonEliminarEstado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonEliminarEstadoActionPerformed(evt);
            }
        });

        botonAddEstado.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        botonAddEstado.setText("Añadir");
        botonAddEstado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonAddEstadoActionPerformed(evt);
            }
        });

        textEstado.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        textEstado.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        textEstado.setText("S0");

        textSimboloEntrada.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        textSimboloEntrada.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        textSimboloEntrada.setText("0");

        botonAddSimboloEntrada.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        botonAddSimboloEntrada.setText("Añadir");
        botonAddSimboloEntrada.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonAddSimboloEntradaActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel2.setText("1) Definir símbolos de entrada:");

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel3.setText("2) Definir conjunto de estados:");

        labelEstadoI.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        labelEstadoI.setText("Estado inicial: NO SELECCIONADO");

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel7.setText("4) Definir estado inicial:");

        comboEstadoI.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        comboEstadoI.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboEstadoIActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel4.setText("3) Definir símbolos en la pila:");

        botonEliminarSimboloPila.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        botonEliminarSimboloPila.setText("Eliminar");
        botonEliminarSimboloPila.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonEliminarSimboloPilaActionPerformed(evt);
            }
        });

        botonAddSimboloPila.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        botonAddSimboloPila.setText("Añadir");
        botonAddSimboloPila.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonAddSimboloPilaActionPerformed(evt);
            }
        });

        textSimboloPila.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        textSimboloPila.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        textSimboloPila.setText("0");

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

        jLabel8.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel8.setText("5) Definir configuración inicial de la pila:");

        comboConfig.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        comboConfig.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboConfigActionPerformed(evt);
            }
        });

        botonEliminarConfig.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        botonEliminarConfig.setText("Eliminar cima");
        botonEliminarConfig.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonEliminarConfigActionPerformed(evt);
            }
        });

        botonAddConfig.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        botonAddConfig.setText("Añadir");
        botonAddConfig.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonAddConfigActionPerformed(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel9.setText("6) Definir transiciones:");

        comboSimPilaTr.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        comboSimPilaTr.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Simbolo pila", "▼" }));

        comboEstadoTr.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        comboEstadoTr.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Estado" }));

        comboSimEntTr.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        comboSimEntTr.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Simbolo entrada", "¬" }));

        comboOpPilaTr.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        comboOpPilaTr.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Op pila", "Apilar", "Desapilar", "Ninguna", "Replace" }));
        comboOpPilaTr.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboOpPilaTrActionPerformed(evt);
            }
        });

        comboOpEstadoTr.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        comboOpEstadoTr.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Op estado", "Ninguna" }));

        comboOpEntradaTr.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        comboOpEntradaTr.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Op entrada", "Avance", "Retenga" }));

        botonEliminarTransicion.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        botonEliminarTransicion.setText("Eliminar transición");
        botonEliminarTransicion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonEliminarTransicionActionPerformed(evt);
            }
        });

        botonAddTransicion.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        botonAddTransicion.setText("Añadir transición");
        botonAddTransicion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonAddTransicionActionPerformed(evt);
            }
        });

        botonConfirmarAP.setFont(new java.awt.Font("Rockwell", 0, 14)); // NOI18N
        botonConfirmarAP.setText("Confirmar autómata");
        botonConfirmarAP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonConfirmarAPActionPerformed(evt);
            }
        });

        botonEditarAP.setFont(new java.awt.Font("Rockwell", 0, 14)); // NOI18N
        botonEditarAP.setText("Editar automata");
        botonEditarAP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonEditarAPActionPerformed(evt);
            }
        });

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

        botonConfirmar1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        botonConfirmar1.setText("Confirmar símbolos y estados");
        botonConfirmar1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonConfirmar1ActionPerformed(evt);
            }
        });

        textEntradaAR.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        textEntradaAR.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        labelSimbolosEntrada.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        labelSimbolosEntrada.setText("Símbolos de entrada: ¬");

        labelEstados.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        labelEstados.setText("Estados: ");

        labelSimbolosPila.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        labelSimbolosPila.setText("Símbolos en la pila: ▼");

        labelConfig.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        labelConfig.setText("Config. inicial: ▼");

        botonTrAceptacion.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        botonTrAceptacion.setText("Aceptación");
        botonTrAceptacion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonTrAceptacionActionPerformed(evt);
            }
        });

        textDescripcionTr.setEditable(false);
        textDescripcionTr.setColumns(20);
        textDescripcionTr.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        textDescripcionTr.setRows(5);
        jScrollPane3.setViewportView(textDescripcionTr);

        textCadenaEntrada.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        textCadenaEntrada.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        botonReconocer.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        botonReconocer.setText("Reconocer");
        botonReconocer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonReconocerActionPerformed(evt);
            }
        });

        jLabel15.setFont(new java.awt.Font("Rockwell", 0, 14)); // NOI18N
        jLabel15.setText("Estado actual");

        botonSiguiente.setText("Siguiente");
        botonSiguiente.setEnabled(false);
        botonSiguiente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonSiguienteActionPerformed(evt);
            }
        });

        jLabel16.setFont(new java.awt.Font("Rockwell", 0, 14)); // NOI18N
        jLabel16.setText("Símbolo a leer");

        textEstadoActual.setEditable(false);
        textEstadoActual.setEnabled(false);

        textSimboloActual.setEditable(false);
        textSimboloActual.setEnabled(false);

        botonIniciar.setText("Iniciar");
        botonIniciar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonIniciarActionPerformed(evt);
            }
        });

        labelFin.setFont(new java.awt.Font("Rockwell", 0, 14)); // NOI18N
        labelFin.setText("FIN DE CADENA");

        labelResultado.setFont(new java.awt.Font("Rockwell", 0, 14)); // NOI18N
        labelResultado.setText("RECONOCIDA");

        jLabel14.setFont(new java.awt.Font("Rockwell", 1, 14)); // NOI18N
        jLabel14.setText("Simular paso a paso");

        jLabel17.setFont(new java.awt.Font("Rockwell", 0, 14)); // NOI18N
        jLabel17.setText("Cadena leída");

        textCadenaLeida.setEditable(false);
        textCadenaLeida.setEnabled(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(textSimboloEntrada, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(6, 6, 6)
                                .addComponent(botonAddSimboloEntrada)
                                .addGap(6, 6, 6)
                                .addComponent(botonEliminarSimboloEntrada)
                                .addGap(10, 10, 10)
                                .addComponent(labelSimbolosEntrada))
                            .addComponent(jLabel3)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(textEstado, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(6, 6, 6)
                                .addComponent(botonAddEstado)
                                .addGap(6, 6, 6)
                                .addComponent(botonEliminarEstado)
                                .addGap(10, 10, 10)
                                .addComponent(labelEstados))
                            .addComponent(jLabel4)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(textSimboloPila, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(6, 6, 6)
                                .addComponent(botonAddSimboloPila)
                                .addGap(6, 6, 6)
                                .addComponent(botonEliminarSimboloPila)
                                .addGap(10, 10, 10)
                                .addComponent(labelSimbolosPila))
                            .addComponent(botonConfirmar1)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel7)
                                .addGap(4, 4, 4)
                                .addComponent(comboEstadoI, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(9, 9, 9)
                                .addComponent(labelEstadoI)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 102, Short.MAX_VALUE)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(27, 27, 27)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 391, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(comboConfig, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(6, 6, 6)
                                .addComponent(botonAddConfig)
                                .addGap(6, 6, 6)
                                .addComponent(botonEliminarConfig)
                                .addGap(10, 10, 10)
                                .addComponent(labelConfig))
                            .addComponent(jLabel9)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(comboEstadoTr, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(6, 6, 6)
                                .addComponent(comboSimPilaTr, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(6, 6, 6)
                                .addComponent(comboSimEntTr, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(10, 10, 10)
                                .addComponent(botonTrAceptacion))
                            .addComponent(jLabel8)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(comboOpPilaTr, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(6, 6, 6)
                                .addComponent(comboOpEstadoTr, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(6, 6, 6)
                                .addComponent(comboOpEntradaTr, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(textEntradaAR, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(botonAddTransicion)
                                .addGap(18, 18, 18)
                                .addComponent(botonEliminarTransicion))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(botonConfirmarAP)
                                .addGap(10, 10, 10)
                                .addComponent(botonEditarAP)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel17)
                                .addGap(18, 18, 18)
                                .addComponent(textCadenaLeida, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 391, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(1, 1, 1)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(textCadenaEntrada, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(botonReconocer))
                                    .addComponent(jLabel14)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel15)
                                            .addComponent(jLabel16))
                                        .addGap(10, 10, 10)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(textEstadoActual, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(textSimboloActual, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(18, 18, 18)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(botonIniciar, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(botonSiguiente))
                                        .addGap(18, 18, 18)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(labelFin)
                                            .addComponent(labelResultado))))))))
                .addGap(21, 21, 21))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(6, 6, 6)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(1, 1, 1)
                                .addComponent(textSimboloEntrada, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(botonAddSimboloEntrada)
                            .addComponent(botonEliminarSimboloEntrada)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(4, 4, 4)
                                .addComponent(labelSimbolosEntrada)))
                        .addGap(7, 7, 7)
                        .addComponent(jLabel3)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(1, 1, 1)
                                .addComponent(textEstado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(botonAddEstado)
                            .addComponent(botonEliminarEstado)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(4, 4, 4)
                                .addComponent(labelEstados)))
                        .addGap(6, 6, 6)
                        .addComponent(jLabel4)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(1, 1, 1)
                                .addComponent(textSimboloPila, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(botonAddSimboloPila)
                            .addComponent(botonEliminarSimboloPila)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(4, 4, 4)
                                .addComponent(labelSimbolosPila)))
                        .addGap(18, 18, 18)
                        .addComponent(botonConfirmar1)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(comboEstadoI, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(3, 3, 3)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel7)
                                    .addComponent(labelEstadoI)))))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 272, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 272, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addComponent(jLabel8)
                .addGap(2, 2, 2)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(9, 9, 9)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(1, 1, 1)
                                .addComponent(comboConfig, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(botonAddConfig)
                            .addComponent(botonEliminarConfig)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(4, 4, 4)
                                .addComponent(labelConfig)))
                        .addGap(18, 18, 18)
                        .addComponent(jLabel9)
                        .addGap(11, 11, 11)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(botonTrAceptacion)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(1, 1, 1)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(comboEstadoTr, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(comboSimPilaTr, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(comboSimEntTr, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(botonReconocer)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(comboOpPilaTr, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(comboOpEstadoTr, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(comboOpEntradaTr, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(textCadenaEntrada, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(textEntradaAR, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(jLabel14)))
                .addGap(6, 6, 6)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(botonIniciar)
                                .addGap(6, 6, 6)
                                .addComponent(botonSiguiente))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(1, 1, 1)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel15)
                                        .addGap(11, 11, 11)
                                        .addComponent(jLabel16))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(textEstadoActual, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(9, 9, 9)
                                        .addComponent(textSimboloActual, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(labelFin)
                                        .addGap(11, 11, 11)
                                        .addComponent(labelResultado)))))
                        .addGap(10, 10, 10)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel17)
                            .addComponent(textCadenaLeida, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(botonAddTransicion)
                            .addComponent(botonEliminarTransicion))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(botonConfirmarAP)
                            .addComponent(botonEditarAP))))
                .addContainerGap(26, Short.MAX_VALUE))
        );

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
            textEntradaAR.setVisible(true);
        }else{
            textEntradaAR.setVisible(false);
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
            }else{
                if(comboOpEntradaTr.getSelectedIndex() != 0 && comboOpEstadoTr.getSelectedIndex() != 0 && comboOpPilaTr.getSelectedIndex() != 0){
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
        }else{
            JOptionPane.showMessageDialog(rootPane, "NO SE HA SELECCIONADO NINGUN ESTADO DE INICIAL",
                "ERROR EN EL AUTOMATA", JOptionPane.ERROR_MESSAGE);
        }

    }//GEN-LAST:event_botonConfirmarAPActionPerformed

    private void botonEditarAPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonEditarAPActionPerformed
        cambiarEstadoBotones1(true);
        botonConfirmar1.setEnabled(true);
        cambiarEstadoBotones2(false);
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
        // TODO add your handling code here:
    }//GEN-LAST:event_botonTrAceptacionActionPerformed

    private void botonReconocerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonReconocerActionPerformed
        String cadena = textCadenaEntrada.getText();
        if(!cadenaConSimbolosEntrada(cadena)){
            JOptionPane.showMessageDialog(this, "LA CADENA INGRESADA DEBE ESTAR COMPUESTA POR LOS SÍMBOLOS DE ENTRADA"
                    + " DEL AUTOMATA", "ERROR", JOptionPane.ERROR_MESSAGE);
        }else{
            if(cadena.charAt(cadena.length() - 1) != '¬'){
                cadena += "¬";
            }
            if(ap.reconocer(cadena)){
                JOptionPane.showMessageDialog(this, "Cadena reconocida", "Reconocedor", JOptionPane.PLAIN_MESSAGE);
            }else{
                JOptionPane.showMessageDialog(this, "Cadena no reconocida", "Reconocedor", JOptionPane.WARNING_MESSAGE);
            }
        }
    }//GEN-LAST:event_botonReconocerActionPerformed

    private void botonSiguienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonSiguienteActionPerformed
        //TODO
    }//GEN-LAST:event_botonSiguienteActionPerformed

    private void botonIniciarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonIniciarActionPerformed
        String cadena = textCadenaEntrada.getText();
        if(!cadenaConSimbolosEntrada(cadena)){
            JOptionPane.showMessageDialog(this, "LA CADENA INGRESADA DEBE ESTAR COMPUESTA POR LOS SÍMBOLOS DE ENTRADA"
                    + " DEL AUTOMATA", "ERROR", JOptionPane.ERROR_MESSAGE);
        }else{
            botonIniciar.setEnabled(false);
            botonSiguiente.setEnabled(true);
            textCadenaEntrada.setEditable(false);
            textSimboloActual.setText(cadena.substring(0, 1));
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
        comboOpEntradaTr.setEnabled(b);
        comboOpEstadoTr.setEnabled(b);
        comboOpPilaTr.setEnabled(b);
        textEntradaAR.setEnabled(b);
        botonAddTransicion.setEnabled(b);
        botonEliminarTransicion.setEnabled(b);
        botonConfirmarAP.setEnabled(b);
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
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JLabel labelConfig;
    private javax.swing.JLabel labelEstadoI;
    private javax.swing.JLabel labelEstados;
    private javax.swing.JLabel labelFin;
    private javax.swing.JLabel labelResultado;
    private javax.swing.JLabel labelSimbolosEntrada;
    private javax.swing.JLabel labelSimbolosPila;
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
