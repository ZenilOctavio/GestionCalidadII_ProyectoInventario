package mx.unison.presentation.productos;

import mx.unison.core.domain.models.Producto;
import mx.unison.infrastructure.persistence.Database;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ProductosPanel extends JPanel implements ProductosView{
    private final ProductosPresenter presenter;
    private JTable table;
    private DefaultTableModel model;

    public ProductosPanel(ProductosPresenter presenter) {
        this.presenter = presenter;
        setLayout(new BorderLayout());
        initTop();
        initTable();
        this.presenter.setView(this);

    }
    private void initTop() {
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton back = new JButton("Regresar");
        back.addActionListener(e -> {presenter.onRegresarClicked();});
        JButton add = new JButton("Agregar");
        add.addActionListener(e -> {presenter.onAgregarClicked();});
        JButton edit = new JButton("Modificar");
        edit.addActionListener(e -> {
            int r = table.getSelectedRow();
            if (r == -1) {
                showError("Debe de seleccionar un producto primero");
                return;
            }
            int id = (int) model.getValueAt(r, 0);
            presenter.onModificarClicked(id);

        });
        JButton del = new JButton("Eliminar");
        del.addActionListener(e -> {
            int r = table.getSelectedRow();
            if (r == -1) {
                showError("Debe de seleccionar un producto primero");
                return;
            }
            int id = (int) model.getValueAt(r, 0);
            String nombre = (String) model.getValueAt(r,1);
            presenter.onEliminarClicked(id, nombre);

        });
        top.add(back); top.add(add); top.add(edit); top.add(del);
        add(top, BorderLayout.NORTH);
    }

    private void initTable() {
        model = new DefaultTableModel(new Object[]{"ID","Nombre","Descripción","Cantidad","Precio","Almacén","Creado","Últ.Mod","Últ.Usuario"}, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);
    }


    @Override
    public void refreshTable(List<Producto> productos) {
        model.setRowCount(0);
        for (Producto p : productos) {
            model.addRow(new Object[]{p.id(), p.nombre(), p.descripcion(), p.cantidad(), p.precio(), p.almacenId(), p.fechaCreacion(), p.fechaModificacion(), p.ultimoUsuario()});
        }
    }

    @Override
    public boolean confirmAction(String message) {
        int response = JOptionPane.showConfirmDialog(
                this,
                message,
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );
        return response == JOptionPane.YES_OPTION;
    }

    @Override
    public void showError(String message) {
        JOptionPane.showMessageDialog(this, message,
                "Error", JOptionPane.ERROR_MESSAGE);
    }
}
