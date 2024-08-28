package ar.com.petmanager.gui.ownerUI;

import ar.com.petmanager.domain.Owner;
import ar.com.petmanager.service.OwnerServiceImpl;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class OwnerDetailPanel extends JPanel {
    private JTextField txtDni, txtName, txtSurname, txtPhone, txtStreet, txtCity;
    private JButton btnEdit, btnSave, btnCancel, btnDelete, btnBack;
    private OwnerServiceImpl ownerService;
    private Owner currentOwner;
    private OwnerUI ownerUI;

    public OwnerDetailPanel(OwnerServiceImpl ownerService) {
        this.ownerService = ownerService;

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("DNI:"), gbc);
        txtDni = new JTextField(15);
        txtDni.setEditable(false);
        gbc.gridx = 1;
        add(txtDni, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        add(new JLabel("Nombre:"), gbc);
        txtName = new JTextField(15);
        gbc.gridx = 1;
        add(txtName, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        add(new JLabel("Apellido:"), gbc);
        txtSurname = new JTextField(15);
        gbc.gridx = 1;
        add(txtSurname, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        add(new JLabel("Teléfono:"), gbc);
        txtPhone = new JTextField(15);
        gbc.gridx = 1;
        add(txtPhone, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        add(new JLabel("Calle:"), gbc);
        txtStreet = new JTextField(15);
        gbc.gridx = 1;
        add(txtStreet, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        add(new JLabel("Ciudad:"), gbc);
        txtCity = new JTextField(15);
        gbc.gridx = 1;
        add(txtCity, gbc);

        btnEdit = new JButton("Editar");
        btnSave = new JButton("Guardar");
        btnCancel = new JButton("Cancelar");
        btnDelete = new JButton("Eliminar");
        btnBack = new JButton("Volver");

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(btnEdit);
        buttonPanel.add(btnSave);
        buttonPanel.add(btnCancel);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnBack);
        add(buttonPanel, gbc);

        btnEdit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setFieldsEditable(true);
                btnSave.setEnabled(true);
                btnCancel.setEnabled(true);
                btnEdit.setEnabled(false);
                btnDelete.setEnabled(false);
            }
        });

        btnSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveOwner();
            }
        });

        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cancelEdit();
            }
        });

        btnDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteOwner();
            }
        });

        btnBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showOwnerList();
            }
        });

        setFieldsEditable(false);
        btnSave.setEnabled(false);
        btnCancel.setEnabled(false);
        btnDelete.setEnabled(true);
    }

    public void setOwner(Owner owner) {
        this.currentOwner = owner;
        if (owner != null) {
            txtDni.setText(String.valueOf(owner.getDni()));
            txtName.setText(owner.getName());
            txtSurname.setText(owner.getSurname());
            txtPhone.setText(String.valueOf(owner.getPhone()));
            txtStreet.setText(owner.getAddress().getStreet());
            txtCity.setText(owner.getAddress().getCity());
        }
    }

    private void setFieldsEditable(boolean editable) {
        txtName.setEditable(editable);
        txtSurname.setEditable(editable);
        txtPhone.setEditable(editable);
        txtStreet.setEditable(editable);
        txtCity.setEditable(editable);
    }

    private void saveOwner() {
        if (currentOwner != null) {
            try {
                currentOwner.setName(txtName.getText());
                currentOwner.setSurname(txtSurname.getText());
                currentOwner.setPhone(Integer.parseInt(txtPhone.getText()));
                currentOwner.getAddress().setStreet(txtStreet.getText());
                currentOwner.getAddress().setCity(txtCity.getText());

                ownerService.update(currentOwner);
                JOptionPane.showMessageDialog(this, "Dueño actualizado exitosamente.");
                setFieldsEditable(false);
                btnSave.setEnabled(false);
                btnCancel.setEnabled(false);
                btnEdit.setEnabled(true);
                btnDelete.setEnabled(true);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Error: Teléfono debe ser un número.");
            }
        }
    }

    private void cancelEdit() {
        setFieldsEditable(false);
        btnSave.setEnabled(false);
        btnCancel.setEnabled(false);
        btnEdit.setEnabled(true);
        btnDelete.setEnabled(true);
    }

    private void deleteOwner() {
        if (currentOwner != null) {
            int confirm = JOptionPane.showConfirmDialog(this, "¿Estás seguro de que quieres eliminar este dueño?", "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                ownerService.deleteById(currentOwner.getDni());
                JOptionPane.showMessageDialog(this, "Dueño eliminado exitosamente.");
                showOwnerList();
            }
        }
    }

    private void showOwnerList() {
        // Implement this method to switch back to the owner list or main panel
    }

    public void setOwnerUI(OwnerUI ownerUI) {
        this.ownerUI = ownerUI;
    }
}
