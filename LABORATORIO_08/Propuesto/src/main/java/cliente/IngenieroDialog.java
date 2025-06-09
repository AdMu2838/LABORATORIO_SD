package empresa.cliente;

import empresa.modelo.Ingeniero;
import javax.swing.*;
import java.awt.*;

public class IngenieroDialog extends JDialog {
    private JTextField txtId;
    private JTextField txtNombre;
    private JTextField txtEspecialidad;
    private JTextField txtCargo;
    private boolean confirmado = false;
    private Ingeniero ingeniero;
    
    public IngenieroDialog(Frame parent, String title, Ingeniero ing) {
        super(parent, title, true);
        this.ingeniero = ing;
        initComponents();
        if (ing != null) {
            cargarDatos(ing);
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
        
        // Especialidad
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        panelCampos.add(new JLabel("Especialidad:"), gbc);
        
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        txtEspecialidad = new JTextField(20);
        panelCampos.add(txtEspecialidad, gbc);
        
        // Cargo
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        panelCampos.add(new JLabel("Cargo:"), gbc);
        
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        txtCargo = new JTextField(20);
        panelCampos.add(txtCargo, gbc);
        
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
    
    private void cargarDatos(Ingeniero ing) {
        txtId.setText(String.valueOf(ing.getIdIng()));
        txtId.setEditable(false); // No permitir editar el ID en actualización
        txtNombre.setText(ing.getNombre());
        txtEspecialidad.setText(ing.getEspecialidad());
        txtCargo.setText(ing.getCargo());
    }
    
    private void aceptar() {
        try {
            int id = Integer.parseInt(txtId.getText().trim());
            String nombre = txtNombre.getText().trim();
            String especialidad = txtEspecialidad.getText().trim();
            String cargo = txtCargo.getText().trim();
            
            if (nombre.isEmpty()) {
                JOptionPane.showMessageDialog(this, "El nombre es obligatorio");
                return;
            }
            
            ingeniero = new Ingeniero(id, nombre, especialidad, cargo);
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
    
    public Ingeniero getIngeniero() {
        return ingeniero;
    }
}
