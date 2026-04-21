package mx.unison.presentation.productos;

import mx.unison.core.domain.models.Almacen;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class ModifyProductoDialog extends JDialog implements ModifyProductoView{
    private final ModifyProductoPresenter presenter;
    private JTextField txtNombre = new JTextField(20);
    private JTextField txtPrecio = new JTextField(10);
    private JTextField txtCantidad = new JTextField(10);
    private JTextArea txtDescripcion = new JTextArea();
    private JComboBox<String> almacenesOptions;
    private Map<String, Almacen> almacenesMap;

    public ModifyProductoDialog(Frame parent, boolean modal, ModifyProductoPresenter presenter){
        super(parent, modal);
        this.presenter = presenter;
        presenter.setView(this);

        JButton btnGuardar = new JButton("Guardar");
        btnGuardar.addActionListener(e -> {
            presenter.onModifyProducto(
                    txtNombre.getText(),
                    txtPrecio.getText(),
                    txtCantidad.getText(),
                    txtDescripcion.getText(),
                    almacenesMap.get((String) almacenesOptions.getSelectedItem()).id()
            );
        });

        setUpAlmacenes();

        setLayout(new GridLayout(6, 2, 10, 10));

        add(new JLabel("Nombre:"));     add(txtNombre);
        add(new JLabel("Precio:"));     add(txtPrecio);
        add(new JLabel("Cantidad:"));   add(txtCantidad);
        add(new JLabel("Almacen:"));    add(almacenesOptions);
        add(new JLabel("Descripción:")); add(txtDescripcion);
        add(new JLabel(""));            add(btnGuardar);
        pack();
    }

    @Override
    public void initializeForm(String nombre, double precio, int cantidad, String descripcion) {
        txtNombre.setText(nombre);
        txtPrecio.setText(String.valueOf(precio));
        txtCantidad.setText(String.valueOf(cantidad));
        txtDescripcion.setText(descripcion);
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

    private void setUpAlmacenes(){
        almacenesMap = new HashMap();

        presenter.getAlmacenes().forEach(a -> {
            almacenesMap.put(a.nombre(), a);
        });

        var strings = almacenesMap.keySet();
        var arr = new String[strings.size()];

        arr = strings.toArray(arr);

        almacenesOptions = new JComboBox<>(arr);

    }
}
