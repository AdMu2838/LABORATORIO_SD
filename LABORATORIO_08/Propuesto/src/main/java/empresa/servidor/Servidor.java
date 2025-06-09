package empresa.servidor;

import empresa.common.Mensaje;
import empresa.common.Operacion;
import empresa.modelo.*;
import empresa.servidor.dao.*;

import java.io.*;
import java.net.*;
import java.util.List;

public class Servidor {
    private ServerSocket serverSocket;
    private static final int PUERTO = 8080;
    private boolean ejecutando = false;
    
    // DAOs
    private DepartamentoDAO departamentoDAO;
    private IngenieroDAO ingenieroDAO;
    private ProyectoDAO proyectoDAO;
    
    public Servidor() {
        this.departamentoDAO = new DepartamentoDAO();
        this.ingenieroDAO = new IngenieroDAO();
        this.proyectoDAO = new ProyectoDAO();
    }
    
    public void iniciar() {
        try {
            serverSocket = new ServerSocket(PUERTO);
            ejecutando = true;
            System.out.println("Servidor iniciado en puerto " + PUERTO);
            System.out.println("Esperando conexiones de clientes...");
            
            while (ejecutando) {
                Socket clienteSocket = serverSocket.accept();
                System.out.println("Cliente conectado desde: " + clienteSocket.getInetAddress());
                
                // Crear un hilo para manejar cada cliente
                Thread hiloCliente = new Thread(() -> manejarCliente(clienteSocket));
                hiloCliente.start();
            }
        } catch (IOException e) {
            System.err.println("Error en el servidor: " + e.getMessage());
        }
    }
    
    private void manejarCliente(Socket clienteSocket) {
        try (ObjectInputStream entrada = new ObjectInputStream(clienteSocket.getInputStream());
             ObjectOutputStream salida = new ObjectOutputStream(clienteSocket.getOutputStream())) {
            
            while (!clienteSocket.isClosed()) {
                try {
                    Mensaje peticion = (Mensaje) entrada.readObject();
                    Mensaje respuesta = procesarPeticion(peticion);
                    salida.writeObject(respuesta);
                    salida.flush();
                } catch (EOFException e) {
                    // Cliente desconectado
                    break;
                } catch (Exception e) {
                    System.err.println("Error procesando petición: " + e.getMessage());
                    break;
                }
            }
        } catch (IOException e) {
            System.err.println("Error en comunicación con cliente: " + e.getMessage());
        } finally {
            try {
                clienteSocket.close();
                System.out.println("Cliente desconectado");
            } catch (IOException e) {
                System.err.println("Error cerrando conexión: " + e.getMessage());
            }
        }
    }
    
    private Mensaje procesarPeticion(Mensaje peticion) {
        Mensaje respuesta = new Mensaje();
        respuesta.setOperacion(peticion.getOperacion());
        
        try {
            switch (peticion.getOperacion()) {
                // OPERACIONES DEPARTAMENTO
                case INSERTAR_DEPARTAMENTO:
                    Departamento deptInsertar = (Departamento) peticion.getDatos();
                    boolean insertadoDept = departamentoDAO.insertar(deptInsertar);
                    respuesta.setExitoso(insertadoDept);
                    respuesta.setMensaje(insertadoDept ? "Departamento insertado correctamente" : "Error al insertar departamento");
                    break;
                    
                case ACTUALIZAR_DEPARTAMENTO:
                    Departamento deptActualizar = (Departamento) peticion.getDatos();
                    boolean actualizadoDept = departamentoDAO.actualizar(deptActualizar);
                    respuesta.setExitoso(actualizadoDept);
                    respuesta.setMensaje(actualizadoDept ? "Departamento actualizado correctamente" : "Error al actualizar departamento");
                    break;
                    
                case ELIMINAR_DEPARTAMENTO:
                    int idDeptEliminar = (Integer) peticion.getDatos();
                    boolean eliminadoDept = departamentoDAO.eliminar(idDeptEliminar);
                    respuesta.setExitoso(eliminadoDept);
                    respuesta.setMensaje(eliminadoDept ? "Departamento eliminado correctamente" : "Error al eliminar departamento");
                    break;
                    
                case LISTAR_DEPARTAMENTOS:
                    List<Departamento> departamentos = departamentoDAO.listarTodos();
                    respuesta.setDatos(departamentos);
                    respuesta.setExitoso(true);
                    respuesta.setMensaje("Departamentos obtenidos correctamente");
                    break;
                    
                case OBTENER_DEPARTAMENTO:
                    int idDeptObtener = (Integer) peticion.getDatos();
                    Departamento deptObtenido = departamentoDAO.obtenerPorId(idDeptObtener);
                    respuesta.setDatos(deptObtenido);
                    respuesta.setExitoso(deptObtenido != null);
                    respuesta.setMensaje(deptObtenido != null ? "Departamento encontrado" : "Departamento no encontrado");
                    break;
                
                // OPERACIONES INGENIERO
                case INSERTAR_INGENIERO:
                    Ingeniero ingInsertar = (Ingeniero) peticion.getDatos();
                    boolean insertadoIng = ingenieroDAO.insertar(ingInsertar);
                    respuesta.setExitoso(insertadoIng);
                    respuesta.setMensaje(insertadoIng ? "Ingeniero insertado correctamente" : "Error al insertar ingeniero");
                    break;
                    
                case ACTUALIZAR_INGENIERO:
                    Ingeniero ingActualizar = (Ingeniero) peticion.getDatos();
                    boolean actualizadoIng = ingenieroDAO.actualizar(ingActualizar);
                    respuesta.setExitoso(actualizadoIng);
                    respuesta.setMensaje(actualizadoIng ? "Ingeniero actualizado correctamente" : "Error al actualizar ingeniero");
                    break;
                    
                case ELIMINAR_INGENIERO:
                    int idIngEliminar = (Integer) peticion.getDatos();
                    boolean eliminadoIng = ingenieroDAO.eliminar(idIngEliminar);
                    respuesta.setExitoso(eliminadoIng);
                    respuesta.setMensaje(eliminadoIng ? "Ingeniero eliminado correctamente" : "Error al eliminar ingeniero");
                    break;
                    
                case LISTAR_INGENIEROS:
                    List<Ingeniero> ingenieros = ingenieroDAO.listarTodos();
                    respuesta.setDatos(ingenieros);
                    respuesta.setExitoso(true);
                    respuesta.setMensaje("Ingenieros obtenidos correctamente");
                    break;
                    
                case OBTENER_INGENIERO:
                    int idIngObtener = (Integer) peticion.getDatos();
                    Ingeniero ingObtenido = ingenieroDAO.obtenerPorId(idIngObtener);
                    respuesta.setDatos(ingObtenido);
                    respuesta.setExitoso(ingObtenido != null);
                    respuesta.setMensaje(ingObtenido != null ? "Ingeniero encontrado" : "Ingeniero no encontrado");
                    break;
                
                // OPERACIONES PROYECTO
                case INSERTAR_PROYECTO:
                    Proyecto proyInsertar = (Proyecto) peticion.getDatos();
                    boolean insertadoProy = proyectoDAO.insertar(proyInsertar);
                    respuesta.setExitoso(insertadoProy);
                    respuesta.setMensaje(insertadoProy ? "Proyecto insertado correctamente" : "Error al insertar proyecto");
                    break;
                    
                case ACTUALIZAR_PROYECTO:
                    Proyecto proyActualizar = (Proyecto) peticion.getDatos();
                    boolean actualizadoProy = proyectoDAO.actualizar(proyActualizar);
                    respuesta.setExitoso(actualizadoProy);
                    respuesta.setMensaje(actualizadoProy ? "Proyecto actualizado correctamente" : "Error al actualizar proyecto");
                    break;
                    
                case ELIMINAR_PROYECTO:
                    int idProyEliminar = (Integer) peticion.getDatos();
                    boolean eliminadoProy = proyectoDAO.eliminar(idProyEliminar);
                    respuesta.setExitoso(eliminadoProy);
                    respuesta.setMensaje(eliminadoProy ? "Proyecto eliminado correctamente" : "Error al eliminar proyecto");
                    break;
                    
                case LISTAR_PROYECTOS:
                    List<Proyecto> proyectos = proyectoDAO.listarTodos();
                    respuesta.setDatos(proyectos);
                    respuesta.setExitoso(true);
                    respuesta.setMensaje("Proyectos obtenidos correctamente");
                    break;
                    
                case OBTENER_PROYECTO:
                    int idProyObtener = (Integer) peticion.getDatos();
                    Proyecto proyObtenido = proyectoDAO.obtenerPorId(idProyObtener);
                    respuesta.setDatos(proyObtenido);
                    respuesta.setExitoso(proyObtenido != null);
                    respuesta.setMensaje(proyObtenido != null ? "Proyecto encontrado" : "Proyecto no encontrado");
                    break;
                
                // CONSULTAS ESPECÍFICAS REQUERIDAS
                case PROYECTOS_POR_DEPARTAMENTO:
                    int idDeptConsulta = (Integer) peticion.getDatos();
                    List<Proyecto> proyectosPorDept = proyectoDAO.obtenerPorDepartamento(idDeptConsulta);
                    respuesta.setDatos(proyectosPorDept);
                    respuesta.setExitoso(true);
                    respuesta.setMensaje("Proyectos del departamento obtenidos correctamente");
                    break;
                    
                case INGENIEROS_POR_PROYECTO:
                    int idProyConsulta = (Integer) peticion.getDatos();
                    List<Ingeniero> ingenierosPorProy = ingenieroDAO.obtenerPorProyecto(idProyConsulta);
                    respuesta.setDatos(ingenierosPorProy);
                    respuesta.setExitoso(true);
                    respuesta.setMensaje("Ingenieros del proyecto obtenidos correctamente");
                    break;
                    
                default:
                    respuesta.setExitoso(false);
                    respuesta.setMensaje("Operación no reconocida");
            }
        } catch (Exception e) {
            respuesta.setExitoso(false);
            respuesta.setMensaje("Error en el servidor: " + e.getMessage());
            e.printStackTrace();
        }
        
        return respuesta;
    }
    
    public void detener() {
        ejecutando = false;
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
        } catch (IOException e) {
            System.err.println("Error al detener servidor: " + e.getMessage());
        }
    }
    
    public static void main(String[] args) {
        Servidor servidor = new Servidor();
        
        // Agregar shutdown hook para cerrar el servidor correctamente
        Runtime.getRuntime().addShutdownHook(new Thread(servidor::detener));
        
        servidor.iniciar();
    }
}

