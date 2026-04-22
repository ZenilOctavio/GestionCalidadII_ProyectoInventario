package mx.unison.mocks.productos;

import mx.unison.core.domain.models.Producto;
import mx.unison.usecases.productos.CreateProductoUseCase;

public class CreateProductoUseCaseMock extends CreateProductoUseCase {
    public boolean saveCalled = false;
    public Producto productoRecibido;

    public CreateProductoUseCaseMock() {
        super(null); // No necesitamos el repositorio real para el mock
    }

    @Override
    public boolean execute(String nombre, double precio, int cantidad, String descripcion, int idAlmacen) {
        long timeStamp = System.currentTimeMillis();
        var producto = new Producto();
        producto.setNombre(nombre);
        producto.setPrecio(precio);
        producto.setCantidad(cantidad);
        producto.setDescripcion(descripcion);
        producto.setAlmacenId(idAlmacen);
        producto.setDepartamento("");
        producto.setFechaCreacion((int) timeStamp);
        producto.setFechaModificacion(String.valueOf(timeStamp));
        producto.setUltimoUsuario("ADMIN");


        this.saveCalled = true;
        this.productoRecibido = producto;
        return true;
    }
}
