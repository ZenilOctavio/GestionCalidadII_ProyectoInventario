package mx.unison.presentation.almacenes;

import javax.swing.*;
import java.awt.*;

public class ModifyAlmacenDialog extends JDialog implements ModifyAlmacenView {
    private final ModifyAlmacenPresenter presenter;
    private JTextField txtNombre = new JTextField(20);


    public ModifyAlmacenDialog(Frame parent, boolean modal, ModifyAlmacenPresenter presenter){
        super(parent, modal);
        this.presenter = presenter;
        presenter.setView(this);

        JButton btnGuardar = new JButton("Guardar");
        btnGuardar.addActionListener(e -> {
            presenter.onModifyAlmacen(txtNombre.getText());
        });


        setLayout(new GridLayout(2, 2, 10, 10));

        add(new JLabel("Nombre:"));     add(txtNombre);

        add(new JLabel(""));            add(btnGuardar);
        pack();
    }

    @Override
    public void initializeForm(String nombre) {
        txtNombre.setText(nombre);

    }

    @Override
    public void close() {
        this.dispose();
    }

    @Override
    public void showError(String message) {
        JOptionPane.showMessageDialog(this, message,
                "Error", JOptionPane.ERROR_MESSAGE);
    }
}
