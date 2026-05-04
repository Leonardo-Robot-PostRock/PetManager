package ar.com.petmanager.gui.base;

import ar.com.petmanager.gui.constants.UIConstants;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Panel base con fondo personalizado para todas las vistas de PetManager.
 * Carga y escala automáticamente el background desde petBackground.jpg.
 */
public class BasePanel extends JPanel {

    private BufferedImage backgroundImage;

    public BasePanel() {
        setLayout(new BorderLayout());
        setOpaque(false);
        loadBackground();
    }

    private void loadBackground() {
        try {
            backgroundImage = ImageIO.read(getClass().getResource(UIConstants.PATH_BACKGROUND));
        } catch (IOException | IllegalArgumentException e) {
            System.err.println("No se pudo cargar la imagen de fondo: " + e.getMessage());
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2d.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            g2d.dispose();
        }
    }
}