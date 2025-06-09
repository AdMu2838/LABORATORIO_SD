package empresa.cliente;

import empresa.common.Mensaje;
import empresa.common.Operacion;
import empresa.modelo.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class GUIPrincipal extends JFrame {
    private ClienteApp cliente;
    private JTabbedPane tabbedPane;
    private JLabel lblEstadoConexion;
    
    // Tablas para mostrar datos
    private JTable tablaDepartamentos;
    private JTable tablaIngenieros;
    private JTable tablaProyectos;
    private DefaultTableModel modeloDepartamentos;
    private DefaultTableModel modeloIngenieros;
    private DefaultTableModel modeloProyectos;
    
    public GUIPrincipal() {
        cliente = new ClienteApp();
        initComponents();
        setupUI();
        conectarServidor();
    }
    
    private void initComponents() {
        setTitle("Sistema de Gestión Empresarial - Cliente/Servidor JDBC");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 700);
        setLocationRelativeTo(null);
        
        tabbedPane = new JTabbedPane();
        lblEstadoConexion = new JLabel("Desconectado");
        lblEstadoConexion.setForeground(Color.RED);
    }
    
    private void setupUI() {
        // Panel principal
        JPanel panelPrincipal = new JPanel(new BorderLayout());
        
        // Panel de estado
        JPanel panelEstado = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelEstado.add(new JLabel("Estado: "));
        panelEstado.add(lblEstadoConexion);
        JButton btnReconectar = new JButton("Reconectar");
        btnReconectar.addActionListener(e -> conectarServidor());
        panelEstado.add(btnReconectar);
        
        // Crear pestañas
        tabbedPane.addTab("Departamentos", crearPanelDepartamentos());
        tabbedPane.addTab("Ingenieros", crearPanelIngenieros());
        tabbedPane.addTab("Proyectos", crearPanelProyectos());
        tabbedPane.addTab("Consultas Especiales", crearPanelConsultas());
        
        panelPrincipal.add(panelEstado, BorderLayout.NORTH);
        panelPrincipal.add(tabbedPane, BorderLayout.CENTER);
        
        add(panelPrincipal);
    }
    
    private JPanel crearPanelDepartamentos() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Tabla
        String[] columnas = {"ID", "Nombre", "Teléfono", "Fax"};
        modeloDepartamentos = new DefaultTableModel(columnas, 0);
        tablaDepartamentos = new JTable(modeloDepartamentos);
        JScrollPane scrollPane = new JScrollPane(tablaDepartamentos);
        
        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout());
        JButton btnInsertar = new JButton("Insertar");
        JButton btnActualizar = new JButton("Actualizar");
        JButton btnEliminar = new JButton("Eliminar");
        JButton btnRefrescar = new JButton("Refrescar");
        
        panelBotones.add(btnInsertar);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnRefrescar);
        
        // Eventos
        btnInsertar.addActionListener(e -> insertarDepartamento());
        btnActualizar.addActionListener(e -> actualizarDepartamento());
        btnEliminar.addActionListener(e -> eliminarDepartamento());
        btnRefrescar.addActionListener(e -> cargarDepartamentos());
        
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(panelBotones, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel crearPanelIngenieros() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Tabla
        String[] columnas = {"ID", "Nombre", "Especialidad", "Cargo"};
        modeloIngenieros = new DefaultTableModel(columnas, 0);
        tablaIngenieros = new JTable(modeloIngenieros);
        JScrollPane scrollPane = new JScrollPane(tablaIngenieros);
        
        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout());
        JButton btnInsertar = new JButton("Insertar");
        JButton btnActualizar = new JButton("Actualizar");
        JButton btnEliminar = new JButton("Eliminar");
        JButton btnRefrescar = new JButton("Refrescar");
        
        panelBotones.add(btnInsertar);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnRefrescar);
        
        // Eventos
        btnInsertar.addActionListener(e -> insertarIngeniero());
        btnActualizar.addActionListener(e -> actualizarIngeniero());
        btnEliminar.addActionListener(e -> eliminarIngeniero());
        btnRefrescar.addActionListener(e -> cargarIngenieros());
        
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(panelBotones, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel crearPanelProyectos() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Tabla
        String[] columnas = {"ID", "Nombre", "Fecha Inicio", "Fecha Término", "Ingeniero", "Departamento"};
        modeloProyectos = new DefaultTableModel(columnas, 0);
        tablaProyectos = new JTable(modeloProyectos);
        JScrollPane scrollPane = new JScrollPane(tablaProyectos);
        
        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout());
        JButton btnInsertar = new JButton("Insertar");
        JButton btnActualizar = new JButton("Actualizar");
        JButton btnEliminar = new JButton("Eliminar");
        JButton btnRefrescar = new JButton("Refrescar");
        
        panelBotones.add(btnInsertar);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnRefrescar);
        
        // Eventos
        btnInsertar.addActionListener(e -> insertarProyecto());
        btnActualizar.addActionListener(e -> actualizarProyecto());
        btnEliminar.addActionListener(e -> eliminarProyecto());
        btnRefrescar.addActionListener(e -> cargarProyectos());
        
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(panelBotones, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel crearPanelConsultas() {
        JPanel panel = new JPanel(new GridLayout(2, 1));
        
        // Consulta 1: Proyectos por departamento
        JPanel panel1 = new JPanel(new BorderLayout());
        panel1.setBorder(BorderFactory.createTitledBorder("Proyectos por Departamento"));
        
        JPanel panelInput1 = new JPanel(new FlowLayout());
        panelInput1.add(new JLabel("ID Departamento:"));
        JTextField txtIdDept = new JTextField(10);
        panelInput1.add(txtIdDept);
        JButton btnConsultar1 = new JButton("Consultar");
        panelInput1.add(btnConsultar1);
        
        JTextArea resultados1 = new JTextArea();
        resultados1.setEditable(false);
        
        btnConsultar1.addActionListener(e -> {
            try {
                int idDept = Integer.parseInt(txtIdDept.getText());
                consultarProyectosPorDepartamento(idDept, resultados1);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "ID de departamento inválido");
            }
        });
        
        panel1.add(panelInput1, BorderLayout.NORTH);
        panel1.add(new JScrollPane(resultados1), BorderLayout.CENTER);
        
        // Consulta 2: Ingenieros por proyecto
        JPanel panel2 = new JPanel(new BorderLayout());
        panel2.setBorder(BorderFactory.createTitledBorder("Ingenieros por Proyecto"));
        
        JPanel panelInput2 = new JPanel(new FlowLayout());
        panelInput2.add(new JLabel("ID Proyecto:"));
        JTextField txtIdProy = new JTextField(10);
        panelInput2.add(txtIdProy);
        JButton btnConsultar2 = new JButton("Consultar");
        panelInput2.add(btnConsultar2);
        
        JTextArea resultados2 = new JTextArea();
        resultados2.setEditable(false);
        
        btnConsultar2.addActionListener(e -> {
            try {
                int idProy = Integer.parseInt(txtIdProy.getText());
                consultarIngenierosPorProyecto(idProy, resultados2);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "ID de proyecto inválido");
            }
        });
        
        panel2.add(panelInput2, BorderLayout.NORTH);
        panel2.add(new JScrollPane(resultados2), BorderLayout.CENTER);
        
        panel.add(panel1);
        panel.add(panel2);
        
        return panel;
    }
    
    private void conectarServidor() {
        if (cliente.conectar()) {
            lblEstadoConexion.setText("Conectado");
            lblEstadoConexion.setForeground(Color.GREEN);
            cargarDatos();
        } else {
            lblEstadoConexion.setText("Error de conexión");
            lblEstadoConexion.setForeground(Color.RED);
        }
    }
    
    private void cargarDatos() {
        cargarDepartamentos();
        cargarIngenieros();
        cargarProyectos();
    }
    
    private void cargarDepartamentos() {
        if (!cliente.estaConectado()) return;
        
        Mensaje respuesta = cliente.enviarPeticion(Operacion.LISTAR_DEPARTAMENTOS, null);
        if (respuesta != null && respuesta.isExitoso()) {
            modeloDepartamentos.setRowCount(0);
            @SuppressWarnings("unchecked")
            List<Departamento> departamentos = (List<Departamento>) respuesta.getDatos();
            for (Departamento dept : departamentos) {
                modeloDepartamentos.addRow(new Object[]{
                    dept.getIdDpto(), dept.getNombre(), dept.getTelefono(), dept.getFax()
                });
            }
        }
    }
    
    private void cargarIngenieros() {
        if (!cliente.estaConectado()) return;
        
        Mensaje respuesta = cliente.enviarPeticion(Operacion.LISTAR_INGENIEROS, null);
        if (respuesta != null && respuesta.isExitoso()) {
            modeloIngenieros.setRowCount(0);
            @SuppressWarnings("unchecked")
            List<Ingeniero> ingenieros = (List<Ingeniero>) respuesta.getDatos();
            for (Ingeniero ing : ingenieros) {
                modeloIngenieros.addRow(new Object[]{
                    ing.getIdIng(), ing.getNombre(), ing.getEspecialidad(), ing.getCargo()
                });
            }
        }
    }
    
    private void cargarProyectos() {
        if (!cliente.estaConectado()) return;
        
        Mensaje respuesta = cliente.enviarPeticion(Operacion.LISTAR_PROYECTOS, null);
        if (respuesta != null && respuesta.isExitoso()) {
            modeloProyectos.setRowCount(0);
            @SuppressWarnings("unchecked")
            List<Proyecto> proyectos = (List<Proyecto>) respuesta.getDatos();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            for (Proyecto proy : proyectos) {
                modeloProyectos.addRow(new Object[]{
                    proy.getIdProy(), 
                    proy.getNombre(), 
                    proy.getFecInicio().format(formatter),
                    proy.getFecTermino() != null ? proy.getFecTermino().format(formatter) : "",
                    proy.getNombreIngeniero() != null ? proy.getNombreIngeniero() : "Sin asignar",
                    proy.getNombreDepartamento()
                });
            }
        }
    }
    
    // Métodos CRUD para Departamentos
    private void insertarDepartamento() {
        DepartamentoDialog dialog = new DepartamentoDialog(this, "Insertar Departamento", null);
        dialog.setVisible(true);
        
        if (dialog.isConfirmado()) {
            Departamento dept = dialog.getDepartamento();
            Mensaje respuesta = cliente.enviarPeticion(Operacion.INSERTAR_DEPARTAMENTO, dept);
            if (respuesta != null) {
                JOptionPane.showMessageDialog(this, respuesta.getMensaje());
                if (respuesta.isExitoso()) {
                    cargarDepartamentos();
                }
            }
        }
    }
    
    private void actualizarDepartamento() {
        int fila = tablaDepartamentos.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un departamento para actualizar");
            return;
        }
        
        // Obtener datos actuales
        int id = (Integer) modeloDepartamentos.getValueAt(fila, 0);
        String nombre = (String) modeloDepartamentos.getValueAt(fila, 1);
        String telefono = (String) modeloDepartamentos.getValueAt(fila, 2);
        String fax = (String) modeloDepartamentos.getValueAt(fila, 3);
        
        Departamento deptActual = new Departamento(id, nombre, telefono, fax);
        DepartamentoDialog dialog = new DepartamentoDialog(this, "Actualizar Departamento", deptActual);
        dialog.setVisible(true);
        
        if (dialog.isConfirmado()) {
            Departamento dept = dialog.getDepartamento();
            Mensaje respuesta = cliente.enviarPeticion(Operacion.ACTUALIZAR_DEPARTAMENTO, dept);
            if (respuesta != null) {
                JOptionPane.showMessageDialog(this, respuesta.getMensaje());
                if (respuesta.isExitoso()) {
                    cargarDepartamentos();
                }
            }
        }
    }
    
    private void eliminarDepartamento() {
        int fila = tablaDepartamentos.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un departamento para eliminar");
            return;
        }
        
        int id = (Integer) modeloDepartamentos.getValueAt(fila, 0);
        String nombre = (String) modeloDepartamentos.getValueAt(fila, 1);
        
        int opcion = JOptionPane.showConfirmDialog(this, 
            "¿Está seguro de eliminar el departamento '" + nombre + "'?", 
            "Confirmar eliminación", 
            JOptionPane.YES_NO_OPTION);
            
        if (opcion == JOptionPane.YES_OPTION) {
            Mensaje respuesta = cliente.enviarPeticion(Operacion.ELIMINAR_DEPARTAMENTO, id);
            if (respuesta != null) {
                JOptionPane.showMessageDialog(this, respuesta.getMensaje());
                if (respuesta.isExitoso()) {
                    cargarDepartamentos();
                }
            }
        }
    }
    
    // Métodos CRUD para Ingenieros (similar implementación)
    private void insertarIngeniero() {
        IngenieroDialog dialog = new IngenieroDialog(this, "Insertar Ingeniero", null);
        dialog.setVisible(true);
        
        if (dialog.isConfirmado()) {
            Ingeniero ing = dialog.getIngeniero();
            Mensaje respuesta = cliente.enviarPeticion(Operacion.INSERTAR_INGENIERO, ing);
            if (respuesta != null) {
                JOptionPane.showMessageDialog(this, respuesta.getMensaje());
                if (respuesta.isExitoso()) {
                    cargarIngenieros();
                }
            }
        }
    }
    
    private void actualizarIngeniero() {
        int fila = tablaIngenieros.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un ingeniero para actualizar");
            return;
        }
        
        int id = (Integer) modeloIngenieros.getValueAt(fila, 0);
        String nombre = (String) modeloIngenieros.getValueAt(fila, 1);
        String especialidad = (String) modeloIngenieros.getValueAt(fila, 2);
        String cargo = (String) modeloIngenieros.getValueAt(fila, 3);
        
        Ingeniero ingActual = new Ingeniero(id, nombre, especialidad, cargo);
        IngenieroDialog dialog = new IngenieroDialog(this, "Actualizar Ingeniero", ingActual);
        dialog.setVisible(true);
        
        if (dialog.isConfirmado()) {
            Ingeniero ing = dialog.getIngeniero();
            Mensaje respuesta = cliente.enviarPeticion(Operacion.ACTUALIZAR_INGENIERO, ing);
            if (respuesta != null) {
                JOptionPane.showMessageDialog(this, respuesta.getMensaje());
                if (respuesta.isExitoso()) {
                    cargarIngenieros();
                }
            }
        }
    }
    
    private void eliminarIngeniero() {
        int fila = tablaIngenieros.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un ingeniero para eliminar");
            return;
        }
        
        int id = (Integer) modeloIngenieros.getValueAt(fila, 0);
        String nombre = (String) modeloIngenieros.getValueAt(fila, 1);
        
        int opcion = JOptionPane.showConfirmDialog(this, 
            "¿Está seguro de eliminar el ingeniero '" + nombre + "'?", 
            "Confirmar eliminación", 
            JOptionPane.YES_NO_OPTION);
            
        if (opcion == JOptionPane.YES_OPTION) {
            Mensaje respuesta = cliente.enviarPeticion(Operacion.ELIMINAR_INGENIERO, id);
            if (respuesta != null) {
                JOptionPane.showMessageDialog(this, respuesta.getMensaje());
                if (respuesta.isExitoso()) {
                    cargarIngenieros();
                }
            }
        }
    }
    
    // Métodos CRUD para Proyectos (similar implementación)
    private void insertarProyecto() {
        ProyectoDialog dialog = new ProyectoDialog(this, "Insertar Proyecto", null, cliente);
        dialog.setVisible(true);
        
        if (dialog.isConfirmado()) {
            Proyecto proy = dialog.getProyecto();
            Mensaje respuesta = cliente.enviarPeticion(Operacion.INSERTAR_PROYECTO, proy);
            if (respuesta != null) {
                JOptionPane.showMessageDialog(this, respuesta.getMensaje());
                if (respuesta.isExitoso()) {
                    cargarProyectos();
                }
            }
        }
    }
    
    private void actualizarProyecto() {
        int fila = tablaProyectos.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un proyecto para actualizar");
            return;
        }
        
        int id = (Integer) modeloProyectos.getValueAt(fila, 0);
        
        // Obtener el proyecto completo del servidor
        Mensaje respuesta = cliente.enviarPeticion(Operacion.OBTENER_PROYECTO, id);
        if (respuesta != null && respuesta.isExitoso()) {
            Proyecto proyActual = (Proyecto) respuesta.getDatos();
            ProyectoDialog dialog = new ProyectoDialog(this, "Actualizar Proyecto", proyActual, cliente);
            dialog.setVisible(true);
            
            if (dialog.isConfirmado()) {
                Proyecto proy = dialog.getProyecto();
                Mensaje respuestaUpdate = cliente.enviarPeticion(Operacion.ACTUALIZAR_PROYECTO, proy);
                if (respuestaUpdate != null) {
                    JOptionPane.showMessageDialog(this, respuestaUpdate.getMensaje());
                    if (respuestaUpdate.isExitoso()) {
                        cargarProyectos();
                    }
                }
            }
        }
    }
    
    private void eliminarProyecto() {
        int fila = tablaProyectos.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un proyecto para eliminar");
            return;
        }
        
        int id = (Integer) modeloProyectos.getValueAt(fila, 0);
        String nombre = (String) modeloProyectos.getValueAt(fila, 1);
        
        int opcion = JOptionPane.showConfirmDialog(this, 
            "¿Está seguro de eliminar el proyecto '" + nombre + "'?", 
            "Confirmar eliminación", 
            JOptionPane.YES_NO_OPTION);
            
        if (opcion == JOptionPane.YES_OPTION) {
            Mensaje respuesta = cliente.enviarPeticion(Operacion.ELIMINAR_PROYECTO, id);
            if (respuesta != null) {
                JOptionPane.showMessageDialog(this, respuesta.getMensaje());
                if (respuesta.isExitoso()) {
                    cargarProyectos();
                }
            }
        }
    }
    
    // Consultas especiales requeridas
    private void consultarProyectosPorDepartamento(int idDepartamento, JTextArea resultados) {
        Mensaje respuesta = cliente.enviarPeticion(Operacion.PROYECTOS_POR_DEPARTAMENTO, idDepartamento);
        if (respuesta != null && respuesta.isExitoso()) {
            @SuppressWarnings("unchecked")
            List<Proyecto> proyectos = (List<Proyecto>) respuesta.getDatos();
            
            StringBuilder sb = new StringBuilder();
            sb.append("PROYECTOS DEL DEPARTAMENTO ID ").append(idDepartamento).append(":\n\n");
            
            if (proyectos.isEmpty()) {
                sb.append("No hay proyectos para este departamento.\n");
            } else {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                for (Proyecto proy : proyectos) {
                    sb.append("ID: ").append(proy.getIdProy()).append("\n");
                    sb.append("Nombre: ").append(proy.getNombre()).append("\n");
                    sb.append("Fecha Inicio: ").append(proy.getFecInicio().format(formatter)).append("\n");
                    if (proy.getFecTermino() != null) {
                        sb.append("Fecha Término: ").append(proy.getFecTermino().format(formatter)).append("\n");
                    }
                    if (proy.getNombreIngeniero() != null) {
                        sb.append("Ingeniero: ").append(proy.getNombreIngeniero()).append("\n");
                    }
                    sb.append("Departamento: ").append(proy.getNombreDepartamento()).append("\n");
                    sb.append("----------------------------\n");
                }
            }
            resultados.setText(sb.toString());
        } else {
            resultados.setText("Error al consultar proyectos: " + 
                (respuesta != null ? respuesta.getMensaje() : "Sin respuesta del servidor"));
        }
    }
    
    private void consultarIngenierosPorProyecto(int idProyecto, JTextArea resultados) {
        Mensaje respuesta = cliente.enviarPeticion(Operacion.INGENIEROS_POR_PROYECTO, idProyecto);
        if (respuesta != null && respuesta.isExitoso()) {
            @SuppressWarnings("unchecked")
            List<Ingeniero> ingenieros = (List<Ingeniero>) respuesta.getDatos();
            
            StringBuilder sb = new StringBuilder();
            sb.append("INGENIEROS DEL PROYECTO ID ").append(idProyecto).append(":\n\n");
            
            if (ingenieros.isEmpty()) {
                sb.append("No hay ingenieros asignados a este proyecto.\n");
            } else {
                for (Ingeniero ing : ingenieros) {
                    sb.append("ID: ").append(ing.getIdIng()).append("\n");
                    sb.append("Nombre: ").append(ing.getNombre()).append("\n");
                    sb.append("Especialidad: ").append(ing.getEspecialidad()).append("\n");
                    sb.append("Cargo: ").append(ing.getCargo()).append("\n");
                    sb.append("----------------------------\n");
                }
            }
            resultados.setText(sb.toString());
        } else {
            resultados.setText("Error al consultar ingenieros: " + 
                (respuesta != null ? respuesta.getMensaje() : "Sin respuesta del servidor"));
        }
    }
}

