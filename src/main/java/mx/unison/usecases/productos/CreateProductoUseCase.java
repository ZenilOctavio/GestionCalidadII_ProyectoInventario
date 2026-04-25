package mx.unison.usecases.productos;

import mx.unison.core.domain.models.Producto;
import mx.unison.core.domain.repository.ProductsRepository;

/**
 * Caso de uso para la creación de un nuevo producto.
 */
public class CreateProductoUseCase {
    private ProductsRepository repository;

    /**
     * Crea una instancia de CreateProductoUseCase.
     * @param repository El repositorio de productos.
     */
    public CreateProductoUseCase(ProductsRepository repository){
        this.repository = repository;
    }

    /**
     * Ejecuta la lógica para crear un nuevo producto.
     *
     * @param nombre Nombre del producto.
     * @param precio Precio unitario.
     * @param cantidad Cantidad inicial en stock.
     * @param descripcion Descripción del producto.
     * @param idAlmacen Identificador del almacén asociado.
     * @return true si la creación fue exitosa, false en caso contrario.
     */
    public boolean execute (String nombre, double precio, int cantidad, String descripcion, int idAlmacen) {

        // --- VALIDACIONES PARA QUE LOS TESTS PASEN ---
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede ser nulo o vacío");
        }

        if (precio < 0) {
            throw new IllegalArgumentException("El precio no puede ser negativo");
        }

        if (cantidad < 0) {
            throw new IllegalArgumentException("La cantidad no puede ser negativa");
        }
        // ----------------------------------------------

        long timeStamp = System.currentTimeMillis();

        var nuevoProducto = new Producto();
        nuevoProducto.setNombre(nombre);
        nuevoProducto.setPrecio(precio);
        nuevoProducto.setCantidad(cantidad);
        nuevoProducto.setDescripcion(descripcion);
        nuevoProducto.setAlmacenId(idAlmacen);

        // Nota: En tus tests mencionas un campo "departamento".
        // Si es obligatorio, deberías validarlo aquí también.
        nuevoProducto.setDepartamento("");

        nuevoProducto.setFechaCreacion(timeStamp);
        nuevoProducto.setFechaModificacion(String.valueOf(timeStamp));
        nuevoProducto.setUltimoUsuario("ADMIN");

        return repository.createProduct(nuevoProducto);
    }
}