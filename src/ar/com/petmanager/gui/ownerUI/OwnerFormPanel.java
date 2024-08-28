package ar.com.petmanager.gui.ownerUI;

import ar.com.petmanager.domain.Owner;
import ar.com.petmanager.domain.Vet;
import ar.com.petmanager.service.OwnerServiceImpl;
import ar.com.petmanager.service.VetServiceImpl;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class OwnerFormPanel extends JPanel {

    private JTextField txtDni;
    private JTextField txtName;
    private JTextField txtSurname;
    private JTextField txtPhone;
    private JTextField txtStreet;
    private JTextField txtCity;
    private JComboBox<Vet> selectPreferredVet;

    private OwnerServiceImpl ownerService;
    private VetServiceImpl vetService;

    public OwnerFormPanel(OwnerServiceImpl ownerService, VetServiceImpl vetService) {
        this.ownerService = ownerService;
        this.vetService = vetService;

        setLayout(new GridBagLayout());
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.insets = new Insets(5, 5, 5, 5);

        gridBagConstraints.anchor = GridBagConstraints.WEST;

        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        add(new JLabel("DNI:"), gridBagConstraints);
        txtDni = new JTextField(15);
        gridBagConstraints.gridx = 1;
        add(txtDni, gridBagConstraints);

        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        add(new JLabel("Nombre:"), gridBagConstraints);
        txtName = new JTextField(15);
        gridBagConstraints.gridx = 1;
        add(txtName, gridBagConstraints);

        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        add(new JLabel("Apellido:"), gridBagConstraints);
        txtSurname = new JTextField(15);
        gridBagConstraints.gridx = 1;
        add(txtSurname, gridBagConstraints);

        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        add(new JLabel("Teléfono:"), gridBagConstraints);
        txtPhone = new JTextField(15);
        gridBagConstraints.gridx = 1;
        add(txtPhone, gridBagConstraints);

        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        add(new JLabel("Calle:"), gridBagConstraints);
        txtStreet = new JTextField(15);
        gridBagConstraints.gridx = 1;
        add(txtStreet, gridBagConstraints);

        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        add(new JLabel("Ciudad:"), gridBagConstraints);
        txtCity = new JTextField(15);
        gridBagConstraints.gridx = 1;
        add(txtCity, gridBagConstraints);

        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        add(new JLabel("Veterinario Preferido:"), gridBagConstraints);
        selectPreferredVet = new JComboBox<>();
        selectPreferredVet.setPreferredSize(new Dimension(170,20));
        populateVets();
        gridBagConstraints.gridx = 1;
        add(selectPreferredVet, gridBagConstraints);

        JButton btnSaveOwner = new JButton("Guardar");
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = GridBagConstraints.CENTER;
        add(btnSaveOwner, gridBagConstraints);
        btnSaveOwner.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addOwner();
            }
        });
    }

    private void populateVets() {
        for (Vet vet : vetService.getAll()) {
            selectPreferredVet.addItem(vet);
        }
    }

    private void addOwner() {
        try {
            int dni = Integer.parseInt(txtDni.getText());
            String name = txtName.getText();
            String surname = txtSurname.getText();
            int phone = Integer.parseInt(txtPhone.getText());
            String street = txtStreet.getText();
            String city = txtCity.getText();

            Owner owner = new Owner(dni, name, surname, phone, street, city);

            Vet preferredVet = (Vet) selectPreferredVet.getSelectedItem();

            if (preferredVet != null) {
                owner.setPreferredVet(preferredVet);
            }

            ownerService.add(owner);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Error: DNI y teléfono deben ser números.");
        }
    }
}
