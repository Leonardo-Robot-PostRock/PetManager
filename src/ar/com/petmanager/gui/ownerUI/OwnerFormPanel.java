package ar.com.petmanager.gui.ownerUI;

import ar.com.petmanager.domain.Owner;
import ar.com.petmanager.domain.Vet;
import ar.com.petmanager.service.OwnerServiceImpl;
import ar.com.petmanager.service.VetServiceImpl;

import javax.swing.*;
import java.awt.*;

public class OwnerFormPanel extends OwnerPanelBase {
    private final OwnerServiceImpl ownerService;

    public OwnerFormPanel(OwnerServiceImpl ownerService, VetServiceImpl vetService) {
        super(vetService);
        this.ownerService = ownerService;

        setPanelTitle("Agregar dueño");

        JButton btnSaveOwner = new JButton("Guardar");
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 10;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        add(btnSaveOwner, gbc);

        btnSaveOwner.addActionListener(e -> addOwner());
        loadVets();
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
