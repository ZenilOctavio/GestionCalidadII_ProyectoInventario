package mx.unison.usecases.productos;

import mx.unison.core.domain.models.Producto;
import mx.unison.core.domain.repository.ProductsRepository;

/**
 * Caso de uso para modificar la información de un producto existente.
 */
public class ModifyProductoUseCase {
    private ProductsRepository repository;

    /**
     * Crea una instancia de ModifyProductoUseCase.
     * @param repository El repositorio de productos.
     */
    public ModifyProductoUseCase(ProductsRepository repository) {
        this.repository = repository;
    }

    /**
     * Ejecuta la lógica para actualizar un producto.
     *
     * @param id Identificador del producto a modificar.
     * @param nombre Nuevo nombre del producto.
     * @param precio Nuevo precio.
     * @param cantidad Nueva cantidad.
     * @param descripcion Nueva descripción.
     * @param idAlmacen Nuevo identificador de almacén asociado.
     * @return true si la actualización fue exitosa, false en caso contrario.
     */
    public boolean execute(int id, String nombre, double precio, int cantidad, String descripcion, int idAlmacen) {
        // Validaciones de negocio
        if (precio < 0) {
            throw new IllegalArgumentException("El precio no puede ser negativo");
        }
        if (cantidad < 0) {
            throw new IllegalArgumentException("La cantidad no puede ser negativa");
        }
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre es obligatorio");
        }

        long timeStamp = System.currentTimeMillis();

        var nuevoProducto = new Producto();
        nuevoProducto.setId(id);
        nuevoProducto.setNombre(nombre);
        nuevoProducto.setPrecio(precio);
        nuevoProducto.setCantidad(cantidad);
        nuevoProducto.setDescripcion(descripcion);
        nuevoProducto.setAlmacenId(idAlmacen);
        nuevoProducto.setDepartamento("");
        nuevoProducto.setFechaModificacion(String.valueOf(timeStamp));
        nuevoProducto.setUltimoUsuario("ADMIN");

        return repository.updateProduct(nuevoProducto);
    }
}