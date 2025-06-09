package empresa.cliente;

import empresa.common.Mensaje;
import empresa.common.Operacion;

import java.io.*;
import java.net.*;

public class ClienteApp {
    private Socket socket;
    private ObjectInputStream entrada;
    private ObjectOutputStream salida;
    private static final String SERVIDOR_HOST = "localhost";
    private static final int SERVIDOR_PUERTO = 8080;
    
    public boolean conectar() {
        try {
            socket = new Socket(SERVIDOR_HOST, SERVIDOR_PUERTO);
            salida = new ObjectOutputStream(socket.getOutputStream());
            entrada = new ObjectInputStream(socket.getInputStream());
            System.out.println("Conectado al servidor en " + SERVIDOR_HOST + ":" + SERVIDOR_PUERTO);
            return true;
        } catch (IOException e) {
            System.err.println("Error al conectar con el servidor: " + e.getMessage());
            return false;
        }
    }
    
    public void desconectar() {
        try {
            if (entrada != null) entrada.close();
            if (salida != null) salida.close();
            if (socket != null) socket.close();
            System.out.println("Desconectado del servidor");
        } catch (IOException e) {
            System.err.println("Error al desconectar: " + e.getMessage());
        }
    }
    
    public Mensaje enviarPeticion(Operacion operacion, Object datos) {
        try {
            Mensaje peticion = new Mensaje(operacion, datos);
            salida.writeObject(peticion);
            salida.flush();
            
            Mensaje respuesta = (Mensaje) entrada.readObject();
            return respuesta;
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error en comunicación con servidor: " + e.getMessage());
            return null;
        }
    }
    
    public boolean estaConectado() {
        return socket != null && !socket.isClosed() && socket.isConnected();
    }
    
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            GUIPrincipal ventana = new GUIPrincipal();
            ventana.setVisible(true);
        });
    }
}
