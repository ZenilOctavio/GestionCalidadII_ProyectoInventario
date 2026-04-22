package mx.unison.usecases.productos;

import mx.unison.core.domain.models.Producto;
import mx.unison.core.domain.repository.ProductsRepository;

public class CreateProductoUseCase {
    private ProductsRepository repository;

    public CreateProductoUseCase(ProductsRepository repository){
        this.repository = repository;
    }

    public boolean execute (String nombre, double precio, int cantidad, String descripcion, int idAlmacen) {
        long timeStamp = System.currentTimeMillis();

        var nuevoProducto = new Producto();
        nuevoProducto.setNombre(nombre);
        nuevoProducto.setPrecio(precio);
        nuevoProducto.setCantidad(cantidad);
        nuevoProducto.setDescripcion(descripcion);
        nuevoProducto.setAlmacenId(idAlmacen);
        nuevoProducto.setDepartamento("");
        nuevoProducto.setFechaCreacion(timeStamp);
        nuevoProducto.setFechaModificacion(String.valueOf(timeStamp));
        nuevoProducto.setUltimoUsuario("ADMIN");

        return repository.createProduct(nuevoProducto);
    }
}