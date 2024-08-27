package ar.com.petmanager.gui;

import ar.com.petmanager.domain.Address;
import ar.com.petmanager.domain.Owner;
import ar.com.petmanager.domain.Vet;
import ar.com.petmanager.service.OwnerServiceImpl;
import ar.com.petmanager.service.VetServiceImpl;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class OwnerUI extends JPanel {

    private OwnerServiceImpl ownerService;
    private VetServiceImpl vetService;

    private JTextField txtDni;
    private JTextField txtName;
    private JTextField txtSurname;
    private JTextField txtPhone;
    private JTextField txtStreet;
    private JTextField txtCity;
    private JComboBox<Vet> selectPreferredVet;
    private JLabel lblTitle;
    private JTextArea txtOwners;
    private JPanel formPanel;

    public OwnerUI() {
        this.ownerService = new OwnerServiceImpl();
        this.vetService = new VetServiceImpl(ownerService);

        setLayout(new BorderLayout());

        // Crear componentes de la UI
        this.lblTitle = new JLabel("Gestión de Dueños");
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));

        txtDni = new JTextField(10);
        txtName = new JTextField(15);
        txtSurname = new JTextField(15);
        txtPhone = new JTextField(10);
        txtStreet = new JTextField(20);
        txtCity = new JTextField(20);
        selectPreferredVet = new JComboBox();

        JButton btnAddOwner = new JButton("Agregar Dueño");
        JButton btnUpdateOwner = new JButton("Actualizar Dueño");
        JButton btnDeleteOwner = new JButton("Eliminar Dueño");
        JButton btnViewOwners = new JButton("Ver Dueños");

        // Crear un panel de formulario para agregar dueños
        this.formPanel = new JPanel(new GridLayout(6, 1));
        formPanel.add(new JLabel("DNI:"));
        formPanel.add(txtDni);
        formPanel.add(new JLabel("Nombre:"));
        formPanel.add(txtName);
        formPanel.add(new JLabel("Apellido:"));
        formPanel.add(txtSurname);
        formPanel.add(new JLabel("Teléfono:"));
        formPanel.add(txtPhone);
        formPanel.add(new JLabel("Dirección:"));
        formPanel.add(new JScrollPane(txtStreet));
        formPanel.add(new JLabel("Veterinario Preferido:"));
        formPanel.add(selectPreferredVet);

        // Panel de botones
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(btnAddOwner);
        buttonPanel.add(btnUpdateOwner);
        buttonPanel.add(btnDeleteOwner);
        buttonPanel.add(btnViewOwners);

        txtOwners = new JTextArea();
        txtOwners.setEditable(false);

        // Agregar componentes al OwnerUI
        add(lblTitle, BorderLayout.NORTH);
        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.NORTH);
        add(new JScrollPane(txtOwners), BorderLayout.EAST);

        // Action Listeners para los botones
        btnAddOwner.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addOwner();
            }
        });

        btnUpdateOwner.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateOwner();
            }
        });

        btnDeleteOwner.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteOwner();
            }
        });

        btnViewOwners.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewOwners();
            }
        });

    }

    private void populateVets() {
        for (Vet vet : vetService.getAll()) {
            selectPreferredVet.addItem(vet);

        }
    }

    // Método para agregar un nuevo dueño
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
            JOptionPane.showMessageDialog(this, "Dueño agregado exitosamente.");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Error: DNI y teléfono deben ser números.");
        }
    }


    // Método para actualizar un dueño existente
    private void updateOwner() {
        try {
            int dni = Integer.parseInt(txtDni.getText());
            Owner existingOwner = ownerService.getById(dni);
            if (existingOwner != null) {
                existingOwner.setName(txtName.getText());
                existingOwner.setSurname(txtSurname.getText());
                existingOwner.setPhone(Integer.parseInt(txtPhone.getText()));

                Address ownerAddress = new Address(txtStreet.getText(), txtCity.getText());

                existingOwner.setAddress(ownerAddress);
                Vet preferredVet = (Vet) selectPreferredVet.getSelectedItem();
                if (preferredVet != null) {
                    existingOwner.setPreferredVet(preferredVet);
                }
                ownerService.update(existingOwner);
                JOptionPane.showMessageDialog(this, "Dueño actualizado exitosamente.");
            } else {
                JOptionPane.showMessageDialog(this, "Dueño no encontrado.");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Error: DNI debe ser un número.");
        }
    }

    // Método para eliminar un dueño existente
    private void deleteOwner() {
        try {
            int dni = Integer.parseInt(txtDni.getText());
            Owner existingOwner = ownerService.getById(dni);
            if (existingOwner != null) {
                ownerService.delete(existingOwner);
                JOptionPane.showMessageDialog(this, "Dueño eliminado exitosamente.");
            } else {
                JOptionPane.showMessageDialog(this, "Dueño no encontrado.");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Error: DNI debe ser un número.");
        }
    }

    // Método para ver todos los dueños registrados
    private void viewOwners() {
        txtOwners.setText("");
        for (Owner owner : ownerService.getAll()) {
            txtOwners.append(owner.toString() + "\n");
        }
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
