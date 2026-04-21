package mx.unison.usecases.productos;

import mx.unison.core.domain.models.Producto;
import mx.unison.core.domain.repository.ProductsRepository;

public class ModifyProductoUseCase {
    private ProductsRepository repository;

    public ModifyProductoUseCase(ProductsRepository repository) {
        this.repository = repository;
    }

    public boolean execute(int id, String nombre, double precio, int cantidad, String descripcion, int idAlmacen){
        long timeStamp = System.currentTimeMillis();
        var nuevoProducto = new Producto.Builder()
                .setId(id)
                .setNombre(nombre)
                .setPrecio(precio)
                .setCantidad(cantidad)
                .setDescripcion(descripcion)
                .setAlmacenId(idAlmacen)
                .setDepartamento("")
                .setFechaModificacion(String.valueOf(timeStamp))
                .setUltimoUsuario("ADMIN")
                .build();

        return repository.updateProduct(nuevoProducto);

    }
}
