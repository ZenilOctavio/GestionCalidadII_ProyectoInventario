package mx.unison.presentation.almacenes;

import javax.swing.*;
import java.awt.*;

public class CreateAlmacenDialog extends JDialog implements CreateAlmacenView {
    private final CreateAlmacenPresenter presenter;
    private JTextField txtNombre = new JTextField(20);


    public CreateAlmacenDialog(Frame parent, boolean modal, CreateAlmacenPresenter presenter){
        super(parent, modal);
        this.presenter = presenter;
        this.presenter.setView(this);

        JButton btnGuardar = new JButton("Guardar");
        btnGuardar.addActionListener(e -> {
            presenter.onSaveAlmacen(txtNombre.getText());
        });


        setLayout(new GridLayout(2, 2, 10, 10));

        add(new JLabel("Nombre:"));     add(txtNombre);

        add(new JLabel(""));            add(btnGuardar);
        pack();
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
