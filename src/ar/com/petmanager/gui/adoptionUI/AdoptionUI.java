package ar.com.petmanager.gui.adoptionUI;

import javax.swing.*;

public class AdoptionUI extends JPanel {

    public AdoptionUI() {
        JFrame frame = new JFrame();
        frame.setTitle("Hola Mundo en Swing");
        frame.setSize(300, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        JLabel label = new JLabel("Hola Mundo", SwingConstants.CENTER);

        add(label);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AdoptionUI frame = new AdoptionUI();
            frame.setVisible(true);
        });
    }
}
