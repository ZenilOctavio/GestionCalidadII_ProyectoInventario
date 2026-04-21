package mx.unison.presentation.home;

import javax.swing.*;
import java.awt.*;

public class HomePanel extends JPanel{
    private final HomePresenter presenter;
    public HomePanel(HomePresenter presenter) {
        this.presenter = presenter;

        setLayout(new BorderLayout());
        JPanel center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.setBorder(BorderFactory.createEmptyBorder(40,40,40,40));
        JLabel title = new JLabel("Sistema de Inventario");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton btnProd = new JButton("Productos");
        btnProd.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnProd.addActionListener(e -> {
            presenter.onProductosClicked();
        });

        JButton btnAlm = new JButton("Almacenes");
        btnAlm.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnAlm.addActionListener(e -> {
            presenter.onAlmacenesClicked();
        });

        center.add(title);
        center.add(Box.createRigidArea(new Dimension(0,20)));
        center.add(btnProd);
        center.add(Box.createRigidArea(new Dimension(0,10)));
        center.add(btnAlm);

        add(center, BorderLayout.CENTER);
    }
}
