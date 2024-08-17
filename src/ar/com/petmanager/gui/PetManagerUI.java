package ar.com.petmanager.gui;

import javax.swing.JFrame;

import java.awt.Color;

import javax.swing.*;
import ar.com.petmanager.domain.*;
import ar.com.petmanager.domain.Pet;

public class PetManagerUI extends JFrame {
	private JTextField ownerNameField;
	private JTextField petNameField;
	private JButton addPetButton;

	private Owner owner;

	public PetManagerUI() {
		owner = new Owner("Leonardo", "Puebla", "Mendoza", "Portal");

		setTitle("Pet Manager");
		setSize(500, 500);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);

		getContentPane().setBackground(Color.BLUE);
		ImageIcon image = new ImageIcon("src/ar/com/petmanager/assets/petIcon.png");
		setIconImage(image.getImage());

		JPanel panel = new JPanel();
		panel.setBackground(Color.black);
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		ownerNameField = new JTextField("Enter owner's name");
		panel.add(ownerNameField);

		petNameField = new JTextField("Enter pet's name");
		panel.add(petNameField);

		addPetButton = new JButton("Add Pet");
		panel.add(addPetButton);

		addPetButton.addActionListener(e -> {
			String petName = petNameField.getText();
			Pet pet = new Pet(petName);
			pet.setOwner(owner);

			JOptionPane.showMessageDialog(this, "Pet " + petName + " added to " + owner.getName());
		});

		add(panel);
	}

}
