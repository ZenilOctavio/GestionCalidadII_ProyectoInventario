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
        var nuevoProducto = new Producto.Builder()
                .setNombre(nombre)
                .setPrecio(precio)
                .setCantidad(cantidad)
                .setDescripcion(descripcion)
                .setAlmacenId(idAlmacen)
                .setDepartamento("")
                .setFechaCreacion((int) timeStamp)
                .setFechaModificacion(String.valueOf(timeStamp))
                .setUltimoUsuario("ADMIN")
                .build();

        return repository.createProduct(nuevoProducto);

    }

}
