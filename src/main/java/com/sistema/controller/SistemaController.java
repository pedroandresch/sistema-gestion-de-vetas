package com.sistema.controller;

import com.google.gson.reflect.TypeToken;
import com.sistema.model.*;
import com.sistema.persistence.JsonRepository;

import java.lang.reflect.Type;
import java.util.List;

public class SistemaController {

    private static final String ARCHIVO_PRODUCTOS = "data/productos.json";
    private static final String ARCHIVO_CLIENTES = "data/clientes.json";
    private static final String ARCHIVO_EMPLEADOS = "data/empleados.json";
    private static final String ARCHIVO_BODEGAS = "data/bodegas.json";
    private static final String ARCHIVO_INVENTARIOS = "data/inventarios.json";
    private static final String ARCHIVO_ORDENES = "data/ordenes.json";

    private List<Producto> productos;
    private List<Cliente> clientes;
    private List<Empleado> empleados;
    private List<Bodega> bodegas;
    private List<Inventario> inventarios;
    private List<OrdenCompra> ordenes;

    public SistemaController() {
        cargarDatos();
    }

    // Carga los datos guardados en los archivos JSON al iniciar el programa
    public void cargarDatos() {
        Type tipoProductos = new TypeToken<List<Producto>>() {}.getType();
        productos = JsonRepository.cargar(ARCHIVO_PRODUCTOS, tipoProductos);

        Type tipoClientes = new TypeToken<List<Cliente>>() {}.getType();
        clientes = JsonRepository.cargar(ARCHIVO_CLIENTES, tipoClientes);

        Type tipoEmpleados = new TypeToken<List<Empleado>>() {}.getType();
        empleados = JsonRepository.cargar(ARCHIVO_EMPLEADOS, tipoEmpleados);

        Type tipoBodegas = new TypeToken<List<Bodega>>() {}.getType();
        bodegas = JsonRepository.cargar(ARCHIVO_BODEGAS, tipoBodegas);

        Type tipoInventarios = new TypeToken<List<Inventario>>() {}.getType();
        inventarios = JsonRepository.cargar(ARCHIVO_INVENTARIOS, tipoInventarios);

        Type tipoOrdenes = new TypeToken<List<OrdenCompra>>() {}.getType();
        ordenes = JsonRepository.cargar(ARCHIVO_ORDENES, tipoOrdenes);
    }

    public List<Producto> getProductos() {
        return productos;
    }

    public void registrarProducto(Producto producto) throws Exception {
        if (producto.getCodigo() == null || producto.getCodigo().trim().isEmpty()) {
            throw new Exception("El codigo del producto es obligatorio.");
        }
        if (buscarProducto(producto.getCodigo()) != null) {
            throw new Exception("Ya existe un producto con el codigo: " + producto.getCodigo());
        }
        productos.add(producto);
        JsonRepository.guardar(ARCHIVO_PRODUCTOS, productos);
    }

    public Producto buscarProducto(String codigo) {
        for (Producto p : productos) {
            if (p.getCodigo().equalsIgnoreCase(codigo)) {
                return p;
            }
        }
        return null;
    }

    public void actualizarProducto(Producto producto) throws Exception {
        if (producto.getCodigo() == null || producto.getCodigo().trim().isEmpty()) {
            throw new Exception("El codigo del producto es obligatorio.");
        }
        Producto encontrado = buscarProducto(producto.getCodigo());
        if (encontrado == null) {
            throw new Exception("No existe el producto con codigo: " + producto.getCodigo());
        }
        encontrado.setNombre(producto.getNombre());
        encontrado.setDescripcion(producto.getDescripcion());
        encontrado.setMarca(producto.getMarca());
        encontrado.setPrecio(producto.getPrecio());
        JsonRepository.guardar(ARCHIVO_PRODUCTOS, productos);
    }

    public List<Cliente> getClientes() {
        return clientes;
    }

    public void registrarCliente(Cliente cliente) throws Exception {
        if (cliente.getDpi() == null || cliente.getDpi().trim().isEmpty()) {
            throw new Exception("El DPI del cliente es obligatorio.");
        }
        if (buscarCliente(cliente.getDpi()) != null) {
            throw new Exception("Ya existe un cliente con el DPI: " + cliente.getDpi());
        }
        clientes.add(cliente);
        JsonRepository.guardar(ARCHIVO_CLIENTES, clientes);
    }

    public Cliente buscarCliente(String dpi) {
        for (Cliente c : clientes) {
            if (c.getDpi().equalsIgnoreCase(dpi)) {
                return c;
            }
        }
        return null;
    }

    public void actualizarCliente(Cliente cliente) throws Exception {
        if (cliente.getDpi() == null || cliente.getDpi().trim().isEmpty()) {
            throw new Exception("El DPI del cliente es obligatorio.");
        }
        Cliente encontrado = buscarCliente(cliente.getDpi());
        if (encontrado == null) {
            throw new Exception("No existe el cliente con DPI: " + cliente.getDpi());
        }
        encontrado.setNombre(cliente.getNombre());
        encontrado.setDireccion(cliente.getDireccion());
        encontrado.setFechaNacimiento(cliente.getFechaNacimiento());
        encontrado.setTipo(cliente.getTipo());
        JsonRepository.guardar(ARCHIVO_CLIENTES, clientes);
    }

    public List<Empleado> getEmpleados() {
        return empleados;
    }

    public void registrarEmpleado(Empleado empleado) throws Exception {
        if (empleado.getIdEmpleado() == null || empleado.getIdEmpleado().trim().isEmpty()) {
            throw new Exception("El ID del empleado es obligatorio.");
        }
        for (Empleado e : empleados) {
            if (e.getIdEmpleado().equalsIgnoreCase(empleado.getIdEmpleado())) {
                throw new Exception("Ya existe un empleado con el ID: " + empleado.getIdEmpleado());
            }
            if (e.getDpi().equalsIgnoreCase(empleado.getDpi())) {
                throw new Exception("Ya existe un empleado con el DPI: " + empleado.getDpi());
            }
        }
        empleados.add(empleado);
        JsonRepository.guardar(ARCHIVO_EMPLEADOS, empleados);
    }

    public Empleado buscarEmpleado(String idEmpleado) {
        for (Empleado e : empleados) {
            if (e.getIdEmpleado().equalsIgnoreCase(idEmpleado)) {
                return e;
            }
        }
        return null;
    }

    public List<Bodega> getBodegas() {
        return bodegas;
    }

    public void registrarBodega(Bodega bodega) throws Exception {
        if (bodega.getIdBodega() == null || bodega.getIdBodega().trim().isEmpty()) {
            throw new Exception("El ID de bodega es obligatorio.");
        }
        if (buscarBodega(bodega.getIdBodega()) != null) {
            throw new Exception("Ya existe una bodega con el ID: " + bodega.getIdBodega());
        }
        bodegas.add(bodega);
        obtenerOCrearInventario(bodega.getIdBodega());
        JsonRepository.guardar(ARCHIVO_BODEGAS, bodegas);
    }

    public Bodega buscarBodega(String idBodega) {
        for (Bodega b : bodegas) {
            if (b.getIdBodega().equalsIgnoreCase(idBodega)) {
                return b;
            }
        }
        return null;
    }

    public List<Inventario> getInventarios() {
        return inventarios;
    }

    public Inventario obtenerOCrearInventario(String idBodega) {
        for (Inventario inv : inventarios) {
            if (inv.getIdBodega().equalsIgnoreCase(idBodega)) {
                return inv;
            }
        }
        Inventario nuevo = new Inventario(idBodega);
        inventarios.add(nuevo);
        JsonRepository.guardar(ARCHIVO_INVENTARIOS, inventarios);
        return nuevo;
    }

    // Agrega stock de un producto validando no superar la capacidad maxima de la bodega
    public void registrarStock(String idBodega, Producto producto, int cantidad) throws Exception {
        if (cantidad <= 0) {
            throw new Exception("La cantidad debe ser mayor a cero.");
        }
        Bodega bodega = buscarBodega(idBodega);
        if (bodega == null) {
            throw new Exception("No existe la bodega indicada: " + idBodega);
        }
        Inventario inventario = obtenerOCrearInventario(idBodega);

        int totalActual = inventario.getTotalUnidadesAlmacenadas();
        if (totalActual + cantidad > bodega.getCapacidad()) {
            int espacioDisponible = bodega.getCapacidad() - totalActual;
            throw new Exception("Capacidad excedida en bodega " + idBodega + ". Espacio restante: " + espacioDisponible + " unidades.");
        }

        inventario.registrarOActualizarStock(producto, cantidad);
        JsonRepository.guardar(ARCHIVO_INVENTARIOS, inventarios);
    }

    public int consultarStock(String idBodega, String codigoProducto) {
        for (Inventario inv : inventarios) {
            if (inv.getIdBodega().equalsIgnoreCase(idBodega)) {
                return inv.obtenerStock(codigoProducto);
            }
        }
        return 0;
    }

    public List<OrdenCompra> getOrdenes() {
        return ordenes;
    }

    public OrdenCompra buscarOrden(String idOrden) {
        for (OrdenCompra o : ordenes) {
            if (o.getIdOrden().equalsIgnoreCase(idOrden)) {
                return o;
            }
        }
        return null;
    }

    public void registrarOrden(OrdenCompra orden) throws Exception {
        if (orden.getIdOrden() == null || orden.getIdOrden().trim().isEmpty()) {
            throw new Exception("El ID de la orden es obligatorio.");
        }
        if (orden.getCliente() == null) {
            throw new Exception("Debe seleccionar un cliente para la orden.");
        }
        if (orden.getItems() == null || orden.getItems().isEmpty()) {
            throw new Exception("La orden de compra debe contener al menos un producto.");
        }
        if (buscarOrden(orden.getIdOrden()) != null) {
            throw new Exception("Ya existe una orden con el ID: " + orden.getIdOrden());
        }

        orden.calcularTotal();

        if (orden.getEstado() == EstadoOrden.COMPLETADA) {
            procesarDescuentoStock(orden);
        }

        ordenes.add(orden);
        JsonRepository.guardar(ARCHIVO_ORDENES, ordenes);
    }

    public void completarOrden(String idOrden) throws Exception {
        OrdenCompra orden = buscarOrden(idOrden);
        if (orden == null) {
            throw new Exception("No se encontro la orden con ID: " + idOrden);
        }
        if (orden.getEstado() == EstadoOrden.COMPLETADA) {
            throw new Exception("La orden ya se encuentra COMPLETADA.");
        }
        if (orden.getEstado() == EstadoOrden.CANCELADA) {
            throw new Exception("No se puede completar una orden CANCELADA.");
        }

        procesarDescuentoStock(orden);

        orden.setEstado(EstadoOrden.COMPLETADA);
        JsonRepository.guardar(ARCHIVO_ORDENES, ordenes);
        JsonRepository.guardar(ARCHIVO_INVENTARIOS, inventarios);
    }

    public void cancelarOrden(String idOrden) throws Exception {
        OrdenCompra orden = buscarOrden(idOrden);
        if (orden == null) {
            throw new Exception("No se encontro la orden con ID: " + idOrden);
        }
        if (orden.getEstado() == EstadoOrden.COMPLETADA) {
            throw new Exception("No se puede cancelar una orden que ya fue COMPLETADA.");
        }

        orden.setEstado(EstadoOrden.CANCELADA);
        JsonRepository.guardar(ARCHIVO_ORDENES, ordenes);
    }

    // Primero revisa que haya stock de todos los productos y luego los descuenta
    private void procesarDescuentoStock(OrdenCompra orden) throws Exception {
        String idBodega = orden.getIdBodega();
        Inventario inventario = obtenerOCrearInventario(idBodega);

        for (DetalleOrden detalle : orden.getItems()) {
            String codigoProducto = detalle.getProducto().getCodigo();
            int cantidadSolicitada = detalle.getCantidad();
            int stockDisponible = inventario.obtenerStock(codigoProducto);

            if (stockDisponible < cantidadSolicitada) {
                throw new Exception("Stock insuficiente en bodega " + idBodega + " para el producto '" +
                        detalle.getProducto().getNombre() + "' (" + codigoProducto + "). Disponible: " +
                        stockDisponible + ", Solicitado: " + cantidadSolicitada);
            }
        }

        for (DetalleOrden detalle : orden.getItems()) {
            inventario.descontarStock(detalle.getProducto().getCodigo(), detalle.getCantidad());
        }

        JsonRepository.guardar(ARCHIVO_INVENTARIOS, inventarios);
    }
}
