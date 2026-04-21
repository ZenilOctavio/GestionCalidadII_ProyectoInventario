package mx.unison.presentation.factories;

import mx.unison.presentation.almacenes.CreateAlmacenDialog;
import mx.unison.presentation.almacenes.CreateAlmacenPresenter;
import mx.unison.presentation.almacenes.ModifyAlmacenDialog;
import mx.unison.presentation.almacenes.ModifyAlmacenPresenter;
import mx.unison.presentation.productos.CreateProductoDialog;
import mx.unison.presentation.productos.CreateProductoPresenter;
import mx.unison.presentation.productos.ModifyProductoDialog;
import mx.unison.presentation.productos.ModifyProductoPresenter;
import mx.unison.usecases.almacenes.CreateAlmacenUseCase;
import mx.unison.usecases.almacenes.FindByIdAlmacenUseCase;
import mx.unison.usecases.almacenes.GetAllAlmacenesUseCase;
import mx.unison.usecases.almacenes.ModifyAlmacenUseCase;
import mx.unison.usecases.productos.CreateProductoUseCase;
import mx.unison.usecases.productos.FindByIdProductoUseCase;
import mx.unison.usecases.productos.ModifyProductoUseCase;

import javax.swing.*;
import java.awt.*;

public class ViewFactory {
    private final CreateProductoUseCase createProductoUseCase;
    private final ModifyProductoUseCase modifyProductoUseCase;
    private final FindByIdProductoUseCase findByIdProductoUseCase;

    private final CreateAlmacenUseCase createAlmacenUseCase;
    private final ModifyAlmacenUseCase modifyAlmacenUseCase;
    private final FindByIdAlmacenUseCase findByIdAlmacenUseCase;
    private final GetAllAlmacenesUseCase getAllAlmacenesUseCase;

    public ViewFactory (
            CreateProductoUseCase createProductoUseCase,
            ModifyProductoUseCase modifyProductoUseCase,
            FindByIdProductoUseCase findByIdProductoUseCase,
            CreateAlmacenUseCase createAlmacenUseCase,
            ModifyAlmacenUseCase modifyAlmacenUseCase,
            FindByIdAlmacenUseCase findByIdAlmacenUseCase,
            GetAllAlmacenesUseCase getAllAlmacenesUseCase
    ){
        this.createProductoUseCase = createProductoUseCase;
        this.modifyProductoUseCase = modifyProductoUseCase;
        this.findByIdProductoUseCase = findByIdProductoUseCase;
        this.createAlmacenUseCase = createAlmacenUseCase;
        this.modifyAlmacenUseCase = modifyAlmacenUseCase;
        this.findByIdAlmacenUseCase = findByIdAlmacenUseCase;
        this.getAllAlmacenesUseCase = getAllAlmacenesUseCase;
    }

    public JDialog makeCreateProductDialog(Frame parent, Runnable onSaveSuccess){
        CreateProductoPresenter presenter = new CreateProductoPresenter(
                createProductoUseCase,
                getAllAlmacenesUseCase,
                onSaveSuccess
        );

        return new CreateProductoDialog(parent, true, presenter);
    }

    public JDialog makeModifyProductDialog(Frame parent, int idProducto, Runnable onSaveSuccess){
        ModifyProductoPresenter presenter = new ModifyProductoPresenter(
            idProducto,
            modifyProductoUseCase,
            findByIdProductoUseCase,
            getAllAlmacenesUseCase,
            onSaveSuccess
        );

        return new ModifyProductoDialog(parent, true, presenter);
    }

    public JDialog makeCreateAlmacenDialog(Frame parent, Runnable onSaveSuccess){
        CreateAlmacenPresenter presenter = new CreateAlmacenPresenter(
                createAlmacenUseCase,
                onSaveSuccess
        );

        return new CreateAlmacenDialog(parent, true, presenter);
    }

    public JDialog makeModifyAlmacenDialog(Frame parent, int idAlmacen, Runnable onSaveSuccess){
        ModifyAlmacenPresenter presenter = new ModifyAlmacenPresenter(
            idAlmacen,
            modifyAlmacenUseCase,
            findByIdAlmacenUseCase,
            onSaveSuccess
        );

        return new ModifyAlmacenDialog(parent, true, presenter);
    }
}
