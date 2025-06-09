package empresa.cliente;

import empresa.common.Mensaje;
import empresa.common.Operacion;
import empresa.modelo.*;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class ProyectoDialog extends JDialog {
    private JTextField txtId;
    private JTextField txtNombre;
    private JTextField txtFecInicio;
    private JTextField txtFecTermino;
    private JComboBox<ComboItem> cmbIngeniero;
    private JComboBox<ComboItem> cmbDepartamento;
    private boolean confirmado = false;
    private Proyecto proyecto;
    private ClienteApp cliente;
    
    public ProyectoDialog(Frame parent, String title, Proyecto proy, ClienteApp cliente) {
        super(parent, title, true);
        this.proyecto = proy;
        this.cliente = cliente;
        initComponents();
        cargarCombos();
        if (proy != null) {
            cargarDatos(proy);
        }
    }
    
    private void initComponents() {
        setLayout(new BorderLayout());
        setSize(400, 300);
        setLocationRelativeTo(getParent());
        
        // Panel de campos
        JPanel panelCampos = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        
        // ID
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);
        panelCampos.add(new JLabel("ID:"), gbc);
        
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        txtId = new JTextField(10);
        panelCampos.add(txtId, gbc);
        
        // Nombre
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        panelCampos.add(new JLabel("Nombre:"), gbc);
        
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        txtNombre = new JTextField(20);
        panelCampos.add(txtNombre, gbc);
        
        // Fecha Inicio
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        panelCampos.add(new JLabel("Fecha Inicio (dd/MM/yyyy):"), gbc);
        
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        txtFecInicio = new JTextField(10);
        panelCampos.add(txtFecInicio, gbc);
        
        // Fecha Término
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        panelCampos.add(new JLabel("Fecha Término (dd/MM/yyyy):"), gbc);
        
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        txtFecTermino = new JTextField(10);
        panelCampos.add(txtFecTermino, gbc);
        
        // Ingeniero
        gbc.gridx = 0; gbc.gridy = 4;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        panelCampos.add(new JLabel("Ingeniero:"), gbc);
        
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        cmbIngeniero = new JComboBox<>();
        panelCampos.add(cmbIngeniero, gbc);
        
        // Departamento
        gbc.gridx = 0; gbc.gridy = 5;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        panelCampos.add(new JLabel("Departamento:"), gbc);
        
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        cmbDepartamento = new JComboBox<>();
        panelCampos.add(cmbDepartamento, gbc);
        
        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout());
        JButton btnAceptar = new JButton("Aceptar");
        JButton btnCancelar = new JButton("Cancelar");
        
        btnAceptar.addActionListener(e -> aceptar());
        btnCancelar.addActionListener(e -> cancelar());
        
        panelBotones.add(btnAceptar);
        panelBotones.add(btnCancelar);
        
        add(panelCampos, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);
    }
    
    private void cargarCombos() {
        // Cargar ingenieros
        cmbIngeniero.addItem(new ComboItem(null, "Sin asignar"));
        Mensaje respuestaIng = cliente.enviarPeticion(Operacion.LISTAR_INGENIEROS, null);
        if (respuestaIng != null && respuestaIng.isExitoso()) {
            @SuppressWarnings("unchecked")
            List<Ingeniero> ingenieros = (List<Ingeniero>) respuestaIng.getDatos();
            for (Ingeniero ing : ingenieros) {
                cmbIngeniero.addItem(new ComboItem(ing.getIdIng(), ing.getNombre()));
            }
        }
        
        // Cargar departamentos
        Mensaje respuestaDept = cliente.enviarPeticion(Operacion.LISTAR_DEPARTAMENTOS, null);
        if (respuestaDept != null && respuestaDept.isExitoso()) {
            @SuppressWarnings("unchecked")
            List<Departamento> departamentos = (List<Departamento>) respuestaDept.getDatos();
            for (Departamento dept : departamentos) {
                cmbDepartamento.addItem(new ComboItem(dept.getIdDpto(), dept.getNombre()));
            }
        }
    }
    
    private void cargarDatos(Proyecto proy) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        
        txtId.setText(String.valueOf(proy.getIdProy()));
        txtId.setEditable(false); // No permitir editar el ID en actualización
        txtNombre.setText(proy.getNombre());
        txtFecInicio.setText(proy.getFecInicio().format(formatter));
        if (proy.getFecTermino() != null) {
            txtFecTermino.setText(proy.getFecTermino().format(formatter));
        }
        
        // Seleccionar ingeniero
        if (proy.getIdIng() != null) {
            for (int i = 0; i < cmbIngeniero.getItemCount(); i++) {
                ComboItem item = (ComboItem) cmbIngeniero.getItemAt(i);
                if (item.getId() != null && item.getId().equals(proy.getIdIng())) {
                    cmbIngeniero.setSelectedIndex(i);
                    break;
                }
            }
        }
        
        // Seleccionar departamento
        for (int i = 0; i < cmbDepartamento.getItemCount(); i++) {
            ComboItem item = (ComboItem) cmbDepartamento.getItemAt(i);
            if (item.getId() != null && item.getId().equals(proy.getIdDpto())) {
                cmbDepartamento.setSelectedIndex(i);
                break;
            }
        }
    }
    
    private void aceptar() {
        try {
            int id = Integer.parseInt(txtId.getText().trim());
            String nombre = txtNombre.getText().trim();
            String strFecInicio = txtFecInicio.getText().trim();
            String strFecTermino = txtFecTermino.getText().trim();
            
            if (nombre.isEmpty()) {
                JOptionPane.showMessageDialog(this, "El nombre es obligatorio");
                return;
            }
            
            if (strFecInicio.isEmpty()) {
                JOptionPane.showMessageDialog(this, "La fecha de inicio es obligatoria");
                return;
            }
            
            ComboItem ingenieroItem = (ComboItem) cmbIngeniero.getSelectedItem();
            ComboItem departamentoItem = (ComboItem) cmbDepartamento.getSelectedItem();
            
            if (departamentoItem == null || departamentoItem.getId() == null) {
                JOptionPane.showMessageDialog(this, "Debe seleccionar un departamento");
                return;
            }
            
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate fecInicio = LocalDate.parse(strFecInicio, formatter);
            LocalDate fecTermino = null;
            
            if (!strFecTermino.isEmpty()) {
                fecTermino = LocalDate.parse(strFecTermino, formatter);
                if (fecTermino.isBefore(fecInicio)) {
                    JOptionPane.showMessageDialog(this, "La fecha de término no puede ser anterior a la fecha de inicio");
                    return;
                }
            }
            
            proyecto = new Proyecto(id, nombre, fecInicio, fecTermino, 
                                  ingenieroItem.getId(), departamentoItem.getId());
            confirmado = true;
            dispose();
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "ID debe ser un número válido");
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, "Formato de fecha inválido. Use dd/MM/yyyy");
        }
    }
    
    private void cancelar() {
        confirmado = false;
        dispose();
    }
    
    public boolean isConfirmado() {
        return confirmado;
    }
    
    public Proyecto getProyecto() {
        return proyecto;
    }
    
    // Clase auxiliar para los ComboBox
    private static class ComboItem {
        private Integer id;
        private String nombre;
        
        public ComboItem(Integer id, String nombre) {
            this.id = id;
            this.nombre = nombre;
        }
        
        public Integer getId() {
            return id;
        }
        
        @Override
        public String toString() {
            return nombre;
        }
    }
}
