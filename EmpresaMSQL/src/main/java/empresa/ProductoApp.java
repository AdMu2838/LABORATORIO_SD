package empresa;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class ProductoApp extends JFrame {

    private JTextField txtId, txtNombre, txtUnidad, txtPrecio, txtBuscar;
    private JComboBox<String> comboCategoria;
    private JTable tablaProductos;
    private DefaultTableModel modeloTabla;
    private Map<String, Integer> mapaCategorias;
    
    // Colores del tema moderno
    private final Color COLOR_PRIMARIO = new Color(41, 128, 185);       // Azul elegante
    private final Color COLOR_SECUNDARIO = new Color(52, 152, 219);     // Azul claro
    private final Color COLOR_FONDO = new Color(248, 249, 250);         // Gris muy claro
    private final Color COLOR_BLANCO = Color.WHITE;
    private final Color COLOR_TEXTO = new Color(33, 37, 41);            // Gris oscuro
    private final Color COLOR_EXITO = new Color(40, 167, 69);           // Verde
    private final Color COLOR_PELIGRO = new Color(220, 53, 69);         // Rojo
    private final Color COLOR_ADVERTENCIA = new Color(255, 193, 7);     // Amarillo
    private final Color COLOR_BORDE = new Color(206, 212, 218);         // Gris claro
    //COLOR NEGRO
    private final Color COLOR_NEGRO = Color.BLACK;
    // Fuentes
    private final Font FUENTE_TITULO = new Font("Segoe UI", Font.BOLD, 16);
    private final Font FUENTE_NORMAL = new Font("Segoe UI", Font.PLAIN, 12);
    private final Font FUENTE_BOTON = new Font("Segoe UI", Font.BOLD, 11);

    public ProductoApp() {
        configurarVentanaPrincipal();
        inicializarComponentes();
        configurarLayout();
        configurarEventos();
        cargarCategorias();
        listar();
    }

    private void configurarVentanaPrincipal() {
        setTitle("Sistema de Gestión de Productos - EmpresaMSQL");
        setSize(1100, 750);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(900, 650));
        
        // Configurar Look and Feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        getContentPane().setBackground(COLOR_FONDO);
    }

    private void inicializarComponentes() {
        // Configurar campos de texto con estilo moderno
        txtId = crearCampoTexto("ID del producto", 150);
        txtNombre = crearCampoTexto("Nombre del producto", 300);
        txtUnidad = crearCampoTexto("Ej: 500ml, 1kg, 12 unidades", 200);
        txtPrecio = crearCampoTexto("0.00", 120);
        txtBuscar = crearCampoTexto("Buscar productos...", 250);
        
        // ComboBox con estilo
        comboCategoria = new JComboBox<>();
        comboCategoria.setPreferredSize(new Dimension(200, 40));
        comboCategoria.setFont(FUENTE_NORMAL);
        comboCategoria.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COLOR_BORDE, 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        
        // Configurar tabla
        configurarTabla();
        
        // Configurar búsqueda en tiempo real
        configurarBusqueda();
    }

    private JTextField crearCampoTexto(String placeholder, int ancho) {
        JTextField campo = new JTextField();
        campo.setPreferredSize(new Dimension(ancho, 40));
        campo.setFont(FUENTE_NORMAL);
        campo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COLOR_BORDE, 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        
        // Efecto placeholder
        campo.setText(placeholder);
        campo.setForeground(Color.GRAY);
        
        campo.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (campo.getText().equals(placeholder)) {
                    campo.setText("");
                    campo.setForeground(COLOR_TEXTO);
                }
                campo.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(COLOR_PRIMARIO, 2),
                    BorderFactory.createEmptyBorder(7, 11, 7, 11)
                ));
            }
            
            @Override
            public void focusLost(FocusEvent e) {
                if (campo.getText().isEmpty()) {
                    campo.setText(placeholder);
                    campo.setForeground(Color.GRAY);
                }
                campo.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(COLOR_BORDE, 1),
                    BorderFactory.createEmptyBorder(8, 12, 8, 12)
                ));
            }
        });
        
        return campo;
    }

    private void configurarTabla() {
        String[] columnas = {"ID", "Producto", "Cantidad/Unidad", "Precio (S/)", "Categoría"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tablaProductos = new JTable(modeloTabla);
        tablaProductos.setRowHeight(35);
        tablaProductos.setFont(FUENTE_NORMAL);
        tablaProductos.setGridColor(new Color(230, 230, 230));
        tablaProductos.setSelectionBackground(COLOR_SECUNDARIO);
        tablaProductos.setSelectionForeground(Color.WHITE);
        tablaProductos.setShowVerticalLines(true);
        tablaProductos.setShowHorizontalLines(true);
        tablaProductos.setIntercellSpacing(new Dimension(1, 1));
        
        // Configurar header de la tabla
        JTableHeader header = tablaProductos.getTableHeader();
        header.setBackground(COLOR_PRIMARIO);
        header.setForeground(COLOR_NEGRO); // Blanco puro para mejor contraste
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setPreferredSize(new Dimension(header.getPreferredSize().width, 42));
        header.setOpaque(true);
        
        // Centrar contenido de las columnas numéricas
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        tablaProductos.getColumnModel().getColumn(0).setCellRenderer(centerRenderer); // ID
        tablaProductos.getColumnModel().getColumn(3).setCellRenderer(centerRenderer); // Precio
        
        // Ajustar anchos de columnas
        tablaProductos.getColumnModel().getColumn(0).setPreferredWidth(80);  // ID
        tablaProductos.getColumnModel().getColumn(1).setPreferredWidth(250); // Producto
        tablaProductos.getColumnModel().getColumn(2).setPreferredWidth(150); // Cantidad
        tablaProductos.getColumnModel().getColumn(3).setPreferredWidth(120); // Precio
        tablaProductos.getColumnModel().getColumn(4).setPreferredWidth(150); // Categoría
        
        // Selección de fila completa
        tablaProductos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Evento de clic en tabla para cargar datos
        tablaProductos.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int filaSeleccionada = tablaProductos.getSelectedRow();
                if (filaSeleccionada != -1) {
                    cargarDatosEnFormulario(filaSeleccionada);
                }
            }
        });
    }

    private void cargarDatosEnFormulario(int fila) {
        // Limpiar placeholders antes de cargar datos
        limpiarPlaceholders();
        
        txtId.setText(modeloTabla.getValueAt(fila, 0).toString());
        txtNombre.setText(modeloTabla.getValueAt(fila, 1).toString());
        txtUnidad.setText(modeloTabla.getValueAt(fila, 2).toString());
        txtPrecio.setText(modeloTabla.getValueAt(fila, 3).toString());
        
        // Buscar la categoría en el combo
        String categoria = modeloTabla.getValueAt(fila, 4).toString();
        for (String cat : mapaCategorias.keySet()) {
            if (mapaCategorias.get(cat).toString().equals(categoria)) {
                comboCategoria.setSelectedItem(cat);
                break;
            }
        }
    }

    private void configurarLayout() {
        setLayout(new BorderLayout(20, 20));
        
        // Panel principal con márgenes
        JPanel panelPrincipal = new JPanel(new BorderLayout(15, 15));
        panelPrincipal.setBorder(new EmptyBorder(20, 20, 20, 20));
        panelPrincipal.setBackground(COLOR_FONDO);
        
        // TÍTULO
        JLabel lblTitulo = crearTituloConIcono("SISTEMA DE GESTIÓN DE PRODUCTOS");
        
        // PANEL DE FORMULARIO
        JPanel panelFormulario = crearPanelFormulario();
        
        // PANEL DE BOTONES
        JPanel panelBotones = crearPanelBotones();
        
        // PANEL DE TABLA
        JPanel panelTabla = crearPanelTabla();
        
        // Agregar componentes
        panelPrincipal.add(lblTitulo, BorderLayout.NORTH);
        
        JPanel panelSuperior = new JPanel(new BorderLayout(10, 10));
        panelSuperior.setBackground(COLOR_FONDO);
        panelSuperior.add(panelFormulario, BorderLayout.CENTER);
        panelSuperior.add(panelBotones, BorderLayout.SOUTH);
        
        panelPrincipal.add(panelSuperior, BorderLayout.NORTH);
        panelPrincipal.add(panelTabla, BorderLayout.CENTER);
        
        add(panelPrincipal);
    }

    private JPanel crearPanelFormulario() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBackground(COLOR_BLANCO);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COLOR_BORDE, 1),
            new EmptyBorder(20, 25, 20, 25)
        ));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Fila 1: ID y Categoría
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(crearEtiquetaConIcono("ID del Producto:", "new.png"), gbc);
        gbc.gridx = 1;
        panel.add(txtId, gbc);
        
        gbc.gridx = 2;
        panel.add(crearEtiquetaConIcono("Categoría:", "list.png"), gbc);
        gbc.gridx = 3;
        panel.add(comboCategoria, gbc);
        
        // Fila 2: Nombre (span completo)
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(crearEtiquetaConIcono("Nombre del Producto:", "add.png"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 3; gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(txtNombre, gbc);
        gbc.gridwidth = 1; gbc.fill = GridBagConstraints.NONE;
        
        // Fila 3: Unidad y Precio
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(crearEtiquetaConIcono("Cantidad/Unidad:", "update.png"), gbc);
        gbc.gridx = 1;
        panel.add(txtUnidad, gbc);
        
        gbc.gridx = 2;
        panel.add(crearEtiquetaConIcono("Precio (S/):", "update.png"), gbc);
        gbc.gridx = 3;
        panel.add(txtPrecio, gbc);
        
        return panel;
    }

    private JLabel crearTituloConIcono(String texto) {
        JLabel titulo = new JLabel(texto, JLabel.CENTER);
        
        // Intentar cargar un icono para el título
        try {
            java.net.URL iconURL = getClass().getClassLoader().getResource("icons/list.png");
            if (iconURL != null) {
                ImageIcon icono = new ImageIcon(iconURL);
                Image img = icono.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
                titulo.setIcon(new ImageIcon(img));
                titulo.setHorizontalTextPosition(SwingConstants.RIGHT);
                titulo.setIconTextGap(15);
            }
        } catch (Exception e) {
            System.out.println("No se pudo cargar el icono del título");
        }
        
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titulo.setForeground(COLOR_PRIMARIO);
        titulo.setBorder(new EmptyBorder(10, 0, 25, 0));
        
        // Agregar efecto de sombra al título
        titulo.setOpaque(true);
        titulo.setBackground(COLOR_BLANCO);
        titulo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COLOR_BORDE, 1),
            new EmptyBorder(15, 0, 15, 0)
        ));
        
        return titulo;
    }

    private JLabel crearEtiqueta(String texto) {
        JLabel etiqueta = new JLabel(texto);
        etiqueta.setFont(FUENTE_NORMAL);
        etiqueta.setForeground(COLOR_TEXTO);
        return etiqueta;
    }

    private JLabel crearEtiquetaConIcono(String texto, String nombreIcono) {
        JLabel etiqueta = new JLabel(texto);
        
        // Intentar cargar un icono
        try {
            java.net.URL iconURL = getClass().getClassLoader().getResource("icons/" + nombreIcono);
            if (iconURL != null) {
                ImageIcon icono = new ImageIcon(iconURL);
                Image img = icono.getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH);
                etiqueta.setIcon(new ImageIcon(img));
                etiqueta.setHorizontalTextPosition(SwingConstants.RIGHT);
                etiqueta.setIconTextGap(5);
            }
        } catch (Exception e) {
            System.out.println("No se pudo cargar el icono: " + nombreIcono);
        }
        
        etiqueta.setFont(FUENTE_NORMAL);
        etiqueta.setForeground(COLOR_TEXTO);
        return etiqueta;
    }

    private JPanel crearPanelBotones() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        panel.setBackground(COLOR_FONDO);
        
        JButton btnInsertar = crearBotonConIcono("Agregar", "add.png", COLOR_EXITO);
        JButton btnActualizar = crearBotonConIcono("Actualizar", "update.png", COLOR_ADVERTENCIA);
        JButton btnEliminar = crearBotonConIcono("Eliminar", "delete.png", COLOR_PELIGRO);
        JButton btnLimpiar = crearBotonConIcono("Limpiar", "new.png", COLOR_SECUNDARIO);
        JButton btnRefrescar = crearBotonConIcono("Refrescar", "list.png", COLOR_PRIMARIO);
        
        panel.add(btnInsertar);
        panel.add(btnActualizar);
        panel.add(btnEliminar);
        panel.add(btnLimpiar);
        panel.add(btnRefrescar);
        
        return panel;
    }

    private JButton crearBotonConIcono(String texto, String nombreIcono, Color color) {
        JButton boton = new JButton(texto);
        
        // Cargar icono
        try {
            java.net.URL iconURL = getClass().getClassLoader().getResource("icons/" + nombreIcono);
            if (iconURL != null) {
                ImageIcon icono = new ImageIcon(iconURL);
                // Redimensionar icono a 16x16
                Image img = icono.getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH);
                boton.setIcon(new ImageIcon(img));
            }
        } catch (Exception e) {
            System.out.println("No se pudo cargar el icono: " + nombreIcono);
        }
        
        boton.setFont(FUENTE_BOTON);
        boton.setBackground(color);
        boton.setForeground(Color.WHITE);
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setPreferredSize(new Dimension(130, 40));
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.setHorizontalTextPosition(SwingConstants.RIGHT);
        boton.setIconTextGap(8);
        
        // Efecto hover
        boton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                boton.setBackground(color.darker());
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                boton.setBackground(color);
            }
        });
        
        return boton;
    }

    private JPanel crearPanelTabla() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(COLOR_BLANCO);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COLOR_BORDE, 1),
            new EmptyBorder(15, 15, 15, 15)
        ));
        
        // Panel superior con título y búsqueda
        JPanel panelSuperiorTabla = new JPanel(new BorderLayout());
        panelSuperiorTabla.setBackground(COLOR_BLANCO);
        
        JLabel lblTituloTabla = crearEtiquetaConIcono("LISTA DE PRODUCTOS", "list.png");
        lblTituloTabla.setFont(FUENTE_TITULO);
        lblTituloTabla.setForeground(COLOR_PRIMARIO);
        
        // Panel de búsqueda
        JPanel panelBusqueda = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBusqueda.setBackground(COLOR_BLANCO);
        
        JLabel lblBuscar = crearEtiquetaConIcono("Buscar:", "search.png");
        panelBusqueda.add(lblBuscar);
        panelBusqueda.add(txtBuscar);
        
        panelSuperiorTabla.add(lblTituloTabla, BorderLayout.WEST);
        panelSuperiorTabla.add(panelBusqueda, BorderLayout.EAST);
        panelSuperiorTabla.setBorder(new EmptyBorder(0, 0, 15, 0));
        
        JScrollPane scrollTabla = new JScrollPane(tablaProductos);
        scrollTabla.setBorder(BorderFactory.createLineBorder(COLOR_BORDE, 1));
        scrollTabla.getViewport().setBackground(Color.WHITE);
        
        // Panel de estadísticas
        JPanel panelEstadisticas = crearPanelEstadisticas();
        
        panel.add(panelSuperiorTabla, BorderLayout.NORTH);
        panel.add(scrollTabla, BorderLayout.CENTER);
        panel.add(panelEstadisticas, BorderLayout.SOUTH);
        
        return panel;
    }

    private JPanel crearPanelEstadisticas() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBackground(COLOR_FONDO);
        panel.setBorder(new EmptyBorder(10, 0, 0, 0));
        
        JLabel lblEstadisticas = new JLabel("Total de productos: 0");
        lblEstadisticas.setFont(FUENTE_NORMAL);
        lblEstadisticas.setForeground(COLOR_TEXTO);
        
        panel.add(lblEstadisticas);
        return panel;
    }

    private void configurarBusqueda() {
        txtBuscar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                filtrarTabla();
            }
        });
    }

    private void filtrarTabla() {
        String textoBusqueda = obtenerTexto(txtBuscar).toLowerCase();
        
        if (textoBusqueda.isEmpty()) {
            listar(); // Mostrar todos los productos
            return;
        }
        
        try {
            List<Producto> lista = ProductoDAO.listar();
            modeloTabla.setRowCount(0);
            
            for (Producto p : lista) {
                String nombreCategoria = obtenerNombreCategoria(p.idCategoria);
                
                // Buscar en todos los campos
                if (String.valueOf(p.idProducto).toLowerCase().contains(textoBusqueda) ||
                    p.nombre.toLowerCase().contains(textoBusqueda) ||
                    p.canUnidad.toLowerCase().contains(textoBusqueda) ||
                    String.valueOf(p.precio).toLowerCase().contains(textoBusqueda) ||
                    nombreCategoria.toLowerCase().contains(textoBusqueda)) {
                    
                    modeloTabla.addRow(new Object[]{
                            p.idProducto, 
                            p.nombre, 
                            p.canUnidad, 
                            String.format("%.2f", p.precio),
                            nombreCategoria
                    });
                }
            }
            actualizarEstadisticas();
        } catch (SQLException ex) {
            mostrarMensajeError("Error al filtrar productos: " + ex.getMessage());
        }
    }

    private void actualizarEstadisticas() {
        // Buscar el panel de estadísticas y actualizar
        try {
            JPanel panelPrincipal = (JPanel) getContentPane().getComponent(0);
            JPanel panelTabla = (JPanel) panelPrincipal.getComponent(2);
            JPanel panelEstadisticas = (JPanel) panelTabla.getComponent(2);
            JLabel lblEstadisticas = (JLabel) panelEstadisticas.getComponent(0);
            
            int totalProductos = modeloTabla.getRowCount();
            lblEstadisticas.setText("Total de productos: " + totalProductos);
        } catch (Exception e) {
            // Si hay error, no hacer nada
        }
    }

    private void configurarEventos() {
        // Obtener botones del panel
        Component[] componentes = ((JPanel)((JPanel)getContentPane().getComponent(0)).getComponent(1)).getComponents();
        JPanel panelBotones = (JPanel) componentes[1];
        
        JButton btnInsertar = (JButton) panelBotones.getComponent(0);
        JButton btnActualizar = (JButton) panelBotones.getComponent(1);
        JButton btnEliminar = (JButton) panelBotones.getComponent(2);
        JButton btnLimpiar = (JButton) panelBotones.getComponent(3);
        JButton btnRefrescar = (JButton) panelBotones.getComponent(4);
        
        btnInsertar.addActionListener(e -> insertar());
        btnActualizar.addActionListener(e -> actualizar());
        btnEliminar.addActionListener(e -> eliminar());
        btnLimpiar.addActionListener(e -> limpiarCampos());
        btnRefrescar.addActionListener(e -> listar());
    }

    private void cargarCategorias() {
        try {
            mapaCategorias = CategoriaDAO.obtenerCategorias();
            comboCategoria.removeAllItems();
            comboCategoria.addItem("-- Seleccione una categoría --");
            for (String nombre : mapaCategorias.keySet()) {
                comboCategoria.addItem(nombre);
            }
            comboCategoria.setSelectedIndex(0);
        } catch (SQLException e) {
            mostrarMensajeError("Error cargando categorías: " + e.getMessage());
        }
    }

    private void insertar() {
        if (!validarCampos()) return;
        
        try {
            int id = Integer.parseInt(obtenerTexto(txtId));
            String categoriaSeleccionada = (String) comboCategoria.getSelectedItem();
            
            if (categoriaSeleccionada.equals("-- Seleccione una categoría --")) {
                mostrarMensajeAdvertencia("Por favor seleccione una categoría válida.");
                return;
            }
            
            int idCategoria = mapaCategorias.get(categoriaSeleccionada);
            String nombre = obtenerTexto(txtNombre);
            String unidad = obtenerTexto(txtUnidad);
            float precio = Float.parseFloat(obtenerTexto(txtPrecio));

            Producto p = new Producto(id, idCategoria, nombre, unidad, precio);
            ProductoDAO.insertar(p);
            mostrarMensajeExito("Producto insertado exitosamente.");
            limpiarCampos();
            listar();
        } catch (NumberFormatException ex) {
            mostrarMensajeError("Error: Verifique que ID y Precio sean números válidos.");
        } catch (Exception ex) {
            mostrarMensajeError("Error al insertar producto: " + ex.getMessage());
        }
    }

    private void listar() {
        try {
            List<Producto> lista = ProductoDAO.listar();
            modeloTabla.setRowCount(0);
            for (Producto p : lista) {
                // Obtener nombre de categoría en lugar de ID
                String nombreCategoria = obtenerNombreCategoria(p.idCategoria);
                modeloTabla.addRow(new Object[]{
                        p.idProducto, 
                        p.nombre, 
                        p.canUnidad, 
                        String.format("%.2f", p.precio),
                        nombreCategoria
                });
            }
            actualizarEstadisticas();
        } catch (SQLException ex) {
            mostrarMensajeError("Error al listar productos: " + ex.getMessage());
        }
    }

    private String obtenerNombreCategoria(int idCategoria) {
        for (Map.Entry<String, Integer> entry : mapaCategorias.entrySet()) {
            if (entry.getValue() == idCategoria) {
                return entry.getKey();
            }
        }
        return "Desconocida";
    }

    private void actualizar() {
        if (obtenerTexto(txtId).isEmpty() || obtenerTexto(txtPrecio).isEmpty()) {
            mostrarMensajeAdvertencia("Por favor ingrese ID y Precio para actualizar.");
            return;
        }
        
        try {
            int id = Integer.parseInt(obtenerTexto(txtId));
            float precio = Float.parseFloat(obtenerTexto(txtPrecio));
            
            int confirmacion = JOptionPane.showConfirmDialog(
                this, 
                "¿Está seguro de actualizar el precio del producto ID " + id + "?",
                "Confirmar Actualización",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
            );
            
            if (confirmacion == JOptionPane.YES_OPTION) {
                ProductoDAO.actualizarPrecio(id, precio);
                mostrarMensajeExito("Precio actualizado exitosamente.");
                limpiarCampos();
                listar();
            }
        } catch (NumberFormatException ex) {
            mostrarMensajeError("Error: Verifique que ID y Precio sean números válidos.");
        } catch (Exception ex) {
            mostrarMensajeError("Error al actualizar precio: " + ex.getMessage());
        }
    }

    private void eliminar() {
        if (obtenerTexto(txtId).isEmpty()) {
            mostrarMensajeAdvertencia("Por favor ingrese el ID del producto a eliminar.");
            return;
        }
        
        try {
            int id = Integer.parseInt(obtenerTexto(txtId));
            
            int confirmacion = JOptionPane.showConfirmDialog(
                this, 
                "¿Está seguro de eliminar el producto ID " + id + "?\nEsta acción no se puede deshacer.",
                "Confirmar Eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
            );
            
            if (confirmacion == JOptionPane.YES_OPTION) {
                ProductoDAO.eliminar(id);
                mostrarMensajeExito("Producto eliminado correctamente.");
                limpiarCampos();
                listar();
            }
        } catch (NumberFormatException ex) {
            mostrarMensajeError("Error: El ID debe ser un número válido.");
        } catch (Exception ex) {
            mostrarMensajeError("Error al eliminar producto: " + ex.getMessage());
        }
    }

    private boolean validarCampos() {
        if (obtenerTexto(txtId).isEmpty() || 
            obtenerTexto(txtNombre).isEmpty() || 
            obtenerTexto(txtUnidad).isEmpty() || 
            obtenerTexto(txtPrecio).isEmpty()) {
            
            mostrarMensajeAdvertencia("Por favor complete todos los campos.");
            return false;
        }
        return true;
    }

    private String obtenerTexto(JTextField campo) {
        String texto = campo.getText().trim();
        // Verificar si es un placeholder
        if (texto.equals("ID del producto") || 
            texto.equals("Nombre del producto") || 
            texto.equals("Ej: 500ml, 1kg, 12 unidades") || 
            texto.equals("0.00") ||
            texto.equals("Buscar productos...")) {
            return "";
        }
        return texto;
    }

    private void limpiarPlaceholders() {
        if (txtId.getForeground() == Color.GRAY) {
            txtId.setText("");
            txtId.setForeground(COLOR_TEXTO);
        }
        if (txtNombre.getForeground() == Color.GRAY) {
            txtNombre.setText("");
            txtNombre.setForeground(COLOR_TEXTO);
        }
        if (txtUnidad.getForeground() == Color.GRAY) {
            txtUnidad.setText("");
            txtUnidad.setForeground(COLOR_TEXTO);
        }
        if (txtPrecio.getForeground() == Color.GRAY) {
            txtPrecio.setText("");
            txtPrecio.setForeground(COLOR_TEXTO);
        }
    }

    private void limpiarCampos() {
        txtId.setText("ID del producto");
        txtId.setForeground(Color.GRAY);
        txtNombre.setText("Nombre del producto");
        txtNombre.setForeground(Color.GRAY);
        txtUnidad.setText("Ej: 500ml, 1kg, 12 unidades");
        txtUnidad.setForeground(Color.GRAY);
        txtPrecio.setText("0.00");
        txtPrecio.setForeground(Color.GRAY);
        comboCategoria.setSelectedIndex(0);
        
        // Limpiar selección de tabla
        tablaProductos.clearSelection();
    }

    private void mostrarMensajeExito(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }

    private void mostrarMensajeError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void mostrarMensajeAdvertencia(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Advertencia", JOptionPane.WARNING_MESSAGE);
    }

    public static void main(String[] args) {
        // Configurar el Look and Feel del sistema
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            new ProductoApp().setVisible(true);
        });
    }
}


