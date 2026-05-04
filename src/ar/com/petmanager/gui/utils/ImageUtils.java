package ar.com.petmanager.gui.utils;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Utilidades para carga y manipulación de imágenes en la UI.
 */
public final class ImageUtils {

    private ImageUtils() {
        throw new UnsupportedOperationException("Clase de utilidades, no instanciable");
    }

    /**
     * Carga una imagen desde el classpath y la escala al tamaño especificado.
     *
     * @param resourcePath ruta del recurso (ej: "/ar/com/petmanager/assets/images/petFoot.svg")
     * @param width        ancho deseado
     * @param height       alto deseado
     * @return ImageIcon escalado, o null si no se encuentra el recurso
     */
    public static ImageIcon loadScaledIcon(String resourcePath, int width, int height) {
        try {
            BufferedImage img = ImageIO.read(ImageUtils.class.getResource(resourcePath));
            if (img != null) {
                Image scaled = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
                return new ImageIcon(scaled);
            }
        } catch (IOException | IllegalArgumentException e) {
            System.err.println("No se pudo cargar la imagen: " + resourcePath);
        }
        return null;
    }

    /**
     * Crea un placeholder circular con las iniciales del texto.
     * Útil cuando no hay foto disponible.
     *
     * @param text       texto para iniciales (máx 2 caracteres)
     * @param size       tamaño del círculo
     * @param background color de fondo
     * @return JPanel con las iniciales
     */
    public static JPanel createAvatarPlaceholder(String text, int size, Color background) {
        JPanel avatar = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(background);
                g2d.fillOval(0, 0, size, size);
                g2d.setColor(Color.WHITE);
                g2d.setFont(new Font("Segoe UI", Font.BOLD, size / 3));
                FontMetrics fm = g2d.getFontMetrics();
                String initials = text.length() > 2 ? text.substring(0, 2) : text;
                int x = (size - fm.stringWidth(initials)) / 2;
                int y = (size - fm.getHeight()) / 2 + fm.getAscent();
                g2d.drawString(initials.toUpperCase(), x, y);
                g2d.dispose();
            }
        };
        avatar.setPreferredSize(new Dimension(size, size));
        avatar.setOpaque(false);
        return avatar;
    }

    /**
     * Crea un JLabel con ícono centrado.
     *
     * @param resourcePath ruta del icono
     * @param width        ancho
     * @param height       alto
     * @return JLabel con el icono
     */
    public static JLabel createIconLabel(String resourcePath, int width, int height) {
        ImageIcon icon = loadScaledIcon(resourcePath, width, height);
        JLabel label = new JLabel();
        if (icon != null) {
            label.setIcon(icon);
        }
        label.setHorizontalAlignment(SwingConstants.CENTER);
        return label;
    }
}