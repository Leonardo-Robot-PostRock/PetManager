package ar.com.petmanager.gui;

import javax.swing.*;

public class AdoptionUI extends JFrame {

    public AdoptionUI() {
        // Configuración del JFrame
        setTitle("Hola Mundo en Swing"); // Título de la ventana
        setSize(300, 200); // Tamaño de la ventana
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Cierra la aplicación al cerrar la ventana
        setLocationRelativeTo(null); // Centra la ventana en la pantalla

        // Crear un JLabel con el texto "Hola Mundo"
        JLabel label = new JLabel("Hola Mundo", SwingConstants.CENTER);

        // Agregar el JLabel al JFrame
        add(label);
    }

    public static void main(String[] args) {
        // Ejecutar el código en el hilo de eventos de Swing
        SwingUtilities.invokeLater(() -> {
            // Crear una instancia de AdoptionUI y hacerla visible
            AdoptionUI frame = new AdoptionUI();
            frame.setVisible(true);
        });
    }
}
