package empresa.cliente;

import empresa.modelo.Departamento;
import javax.swing.*;
import java.awt.*;

public class DepartamentoDialog extends JDialog {
    private JTextField txtId;
    private JTextField txtNombre;
    private JTextField txtTelefono;
    private JTextField txtFax;
    private boolean confirmado = false;
    private Departamento departamento;
    
    public DepartamentoDialog(Frame parent, String title, Departamento dept) {
        super(parent, title, true);
        this.departamento = dept;
        initComponents();
        if (dept != null) {
            cargarDatos(dept);
        }
    }
    
    private void initComponents() {
        setLayout(new BorderLayout());
        setSize(350, 200);
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
        
        // Teléfono
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        panelCampos.add(new JLabel("Teléfono:"), gbc);
        
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        txtTelefono = new JTextField(15);
        panelCampos.add(txtTelefono, gbc);
        
        // Fax
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        panelCampos.add(new JLabel("Fax:"), gbc);
        
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        txtFax = new JTextField(15);
        panelCampos.add(txtFax, gbc);
        
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
    
    private void cargarDatos(Departamento dept) {
        txtId.setText(String.valueOf(dept.getIdDpto()));
        txtId.setEditable(false); // No permitir editar el ID en actualización
        txtNombre.setText(dept.getNombre());
        txtTelefono.setText(dept.getTelefono());
        txtFax.setText(dept.getFax());
    }
    
    private void aceptar() {
        try {
            int id = Integer.parseInt(txtId.getText().trim());
            String nombre = txtNombre.getText().trim();
            String telefono = txtTelefono.getText().trim();
            String fax = txtFax.getText().trim();
            
            if (nombre.isEmpty()) {
                JOptionPane.showMessageDialog(this, "El nombre es obligatorio");
                return;
            }
            
            departamento = new Departamento(id, nombre, telefono, fax);
            confirmado = true;
            dispose();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "ID debe ser un número válido");
        }
    }
    
    private void cancelar() {
        confirmado = false;
        dispose();
    }
    
    public boolean isConfirmado() {
        return confirmado;
    }
    
    public Departamento getDepartamento() {
        return departamento;
    }
}
