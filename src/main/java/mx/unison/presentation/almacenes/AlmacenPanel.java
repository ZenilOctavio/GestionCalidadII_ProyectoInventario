package mx.unison.presentation.almacenes;

import mx.unison.core.domain.models.Almacen;
import mx.unison.core.domain.models.Producto;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AlmacenPanel extends JPanel implements AlmacenesView {
    private final AlmacenesPresenter presenter;
    private JTable table;
    private DefaultTableModel model;

    public AlmacenPanel(AlmacenesPresenter presenter) {
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
        model = new DefaultTableModel(new Object[]{"ID","Nombre","Creado","Últ.Mod","Últ.Usuario"}, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);
    }


    @Override
    public void refreshTable(List<Almacen> almacenes) {
        model.setRowCount(0);
        for (Almacen a : almacenes) {
            model.addRow(new Object[]{a.id(), a.nombre(), a.fechaHoraCreacion(), a.fechaHoraUltimaMod(), a.ultimoUsuario()});
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
