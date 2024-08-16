package ar.com.petmanager.presentation;

import javax.swing.JFrame;
import javax.swing.*;
import ar.com.petmanager.domain.*;
import ar.com.petmanager.domain.Pet;

public class PetManagerUI extends JFrame {
    private JTextField ownerNameField;
    private JTextField petNameField;
    private JButton addPetButton;

    private Owner owner;

    public PetManagerUI() {
        owner = new Owner("Leonardo", "Puebla","Mendoza","Portal");

        setTitle("Pet Manager");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panel principal
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // Campo para el nombre del dueño
        ownerNameField = new JTextField("Enter owner's name");
        panel.add(ownerNameField);

        // Campo para el nombre de la mascota
        petNameField = new JTextField("Enter pet's name");
        panel.add(petNameField);

        // Botón para agregar una mascota
        addPetButton = new JButton("Add Pet");
        panel.add(addPetButton);

        // Acción del botón
        addPetButton.addActionListener(e -> {
            String petName = petNameField.getText();
            Pet pet = new Pet(petName);
            pet.setOwner(owner); // Asociamos el dueño con la mascota

            JOptionPane.showMessageDialog(this, "Pet " + petName + " added to " + owner.getName());
        });

        add(panel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            PetManagerUI ui = new PetManagerUI();
            ui.setVisible(true);
        });
    }
}
