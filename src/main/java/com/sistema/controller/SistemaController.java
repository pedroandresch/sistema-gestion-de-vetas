package com.sistema.controller;

import com.google.gson.reflect.TypeToken;
import com.sistema.model.*;
import com.sistema.persistence.JsonRepository;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Controlador principal del sistema (Patrón MVC).
 * Centraliza la lógica de negocio, validaciones y sincronización con archivos JSON.
 */
public class SistemaController {

    private static final String DATA_DIR = "data" + File.separator;
    private static final String FILE_PRODUCTOS = DATA_DIR + "productos.json";
    private static final String FILE_CLIENTES = DATA_DIR + "clientes.json";
    private static final String FILE_EMPLEADOS = DATA_DIR + "empleados.json";
    private static final String FILE_BODEGAS = DATA_DIR + "bodegas.json";
    private static final String FILE_INVENTARIOS = DATA_DIR + "inventarios.json";
    private static final String FILE_ORDENES = DATA_DIR + "ordenes.json";

    private List<Producto> productos;
    private List<Cliente> clientes;
    private List<Empleado> empleados;
    private List<Bodega> bodegas;
    private List<Inventario> inventarios;
    private List<OrdenCompra> ordenes;

    public SistemaController() {
        cargarDatos();
    }

    // ==========================================
    // CARGA Y GUARDADO GENERAL
    // ==========================================

    public void cargarDatos() {
        Type prodType = new TypeToken<List<Producto>>() {}.getType();
        productos = JsonRepository.cargar(FILE_PRODUCTOS, prodType);

        Type cliType = new TypeToken<List<Cliente>>() {}.getType();
        clientes = JsonRepository.cargar(FILE_CLIENTES, cliType);

        Type empType = new TypeToken<List<Empleado>>() {}.getType();
        empleados = JsonRepository.cargar(FILE_EMPLEADOS, empType);

        Type bodType = new TypeToken<List<Bodega>>() {}.getType();
        bodegas = JsonRepository.cargar(FILE_BODEGAS, bodType);

        Type invType = new TypeToken<List<Inventario>>() {}.getType();
        inventarios = JsonRepository.cargar(FILE_INVENTARIOS, invType);

        Type ordType = new TypeToken<List<OrdenCompra>>() {}.getType();
        ordenes = JsonRepository.cargar(FILE_ORDENES, ordType);
    }

    // ==========================================
    // GESTIÓN DE PRODUCTOS
    // ==========================================

    public List<Producto> getProductos() {
        return productos;
    }

    public void registrarProducto(Producto producto) throws Exception {
        if (producto.getCodigo() == null || producto.getCodigo().trim().isEmpty()) {
            throw new Exception("El código del producto es obligatorio.");
        }
        for (Producto p : productos) {
            if (p.getCodigo().equalsIgnoreCase(producto.getCodigo())) {
                throw new Exception("Ya existe un producto con el código: " + producto.getCodigo());
            }
        }
        productos.add(producto);
        JsonRepository.guardar(FILE_PRODUCTOS, productos);
    }

    public Optional<Producto> buscarProducto(String codigo) {
        return productos.stream()
                .filter(p -> p.getCodigo().equalsIgnoreCase(codigo))
                .findFirst();
    }

    // ==========================================
    // GESTIÓN DE CLIENTES
    // ==========================================

    public List<Cliente> getClientes() {
        return clientes;
    }

    public void registrarCliente(Cliente cliente) throws Exception {
        if (cliente.getDpi() == null || cliente.getDpi().trim().isEmpty()) {
            throw new Exception("El DPI del cliente es obligatorio.");
        }
        for (Cliente c : clientes) {
            if (c.getDpi().equalsIgnoreCase(cliente.getDpi())) {
                throw new Exception("Ya existe un cliente con el DPI: " + cliente.getDpi());
            }
        }
        clientes.add(cliente);
        JsonRepository.guardar(FILE_CLIENTES, clientes);
    }

    public Optional<Cliente> buscarCliente(String dpi) {
        return clientes.stream()
                .filter(c -> c.getDpi().equalsIgnoreCase(dpi))
                .findFirst();
    }

    // ==========================================
    // GESTIÓN DE EMPLEADOS
    // ==========================================

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
        JsonRepository.guardar(FILE_EMPLEADOS, empleados);
    }

    public Optional<Empleado> buscarEmpleado(String idEmpleado) {
        return empleados.stream()
                .filter(e -> e.getIdEmpleado().equalsIgnoreCase(idEmpleado))
                .findFirst();
    }

    // ==========================================
    // GESTIÓN DE BODEGAS
    // ==========================================

    public List<Bodega> getBodegas() {
        return bodegas;
    }

    public void registrarBodega(Bodega bodega) throws Exception {
        if (bodega.getIdBodega() == null || bodega.getIdBodega().trim().isEmpty()) {
            throw new Exception("El ID de bodega es obligatorio.");
        }
        for (Bodega b : bodegas) {
            if (b.getIdBodega().equalsIgnoreCase(bodega.getIdBodega())) {
                throw new Exception("Ya existe una bodega con el ID: " + bodega.getIdBodega());
            }
        }
        bodegas.add(bodega);
        // Inicializar inventario para la bodega si no existe
        obtenerOCrearInventario(bodega.getIdBodega());
        JsonRepository.guardar(FILE_BODEGAS, bodegas);
    }

    public Optional<Bodega> buscarBodega(String idBodega) {
        return bodegas.stream()
                .filter(b -> b.getIdBodega().equalsIgnoreCase(idBodega))
                .findFirst();
    }

    // ==========================================
    // GESTIÓN DE INVENTARIO
    // ==========================================

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
        JsonRepository.guardar(FILE_INVENTARIOS, inventarios);
        return nuevo;
    }

    public void registrarStock(String idBodega, Producto producto, int cantidad) throws Exception {
        if (cantidad <= 0) {
            throw new Exception("La cantidad debe ser mayor a cero.");
        }
        Optional<Bodega> bodegaOpt = buscarBodega(idBodega);
        if (bodegaOpt.isEmpty()) {
            throw new Exception("No existe la bodega indicada: " + idBodega);
        }
        Bodega bodega = bodegaOpt.get();
        Inventario inventario = obtenerOCrearInventario(idBodega);

        int totalActual = inventario.getTotalUnidadesAlmacenadas();
        if (totalActual + cantidad > bodega.getCapacidad()) {
            int espacioDisponible = bodega.getCapacidad() - totalActual;
            throw new Exception("Capacidad excedida en bodega " + idBodega + ". Espacio restante: " + espacioDisponible + " unidades.");
        }

        inventario.registrarOActualizarStock(producto, cantidad);
        JsonRepository.guardar(FILE_INVENTARIOS, inventarios);
    }

    public int consultarStock(String idBodega, String codigoProducto) {
        for (Inventario inv : inventarios) {
            if (inv.getIdBodega().equalsIgnoreCase(idBodega)) {
                return inv.obtenerStock(codigoProducto);
            }
        }
        return 0;
    }

    // ==========================================
    // GESTIÓN DE ÓRDENES DE COMPRA Y FACTURACIÓN
    // ==========================================

    public List<OrdenCompra> getOrdenes() {
        return ordenes;
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
        for (OrdenCompra o : ordenes) {
            if (o.getIdOrden().equalsIgnoreCase(orden.getIdOrden())) {
                throw new Exception("Ya existe una orden con el ID: " + orden.getIdOrden());
            }
        }

        orden.calcularTotal();
        ordenes.add(orden);
        JsonRepository.guardar(FILE_ORDENES, ordenes);

        // Si se crea directamente en estado COMPLETADA, descontar stock de una vez
        if (orden.getEstado() == EstadoOrden.COMPLETADA) {
            procesarDescuentoStock(orden);
        }
    }

    /**
     * Pasa una orden a estado COMPLETADA y descuenta el stock en inventario.
     * Requisito obligatorio: "Al completar una venta y pasar a estado completada debe realizar
     * la actualización del stock en el inventario restando el producto vendido".
     */
    public void completarOrden(String idOrden) throws Exception {
        Optional<OrdenCompra> ordenOpt = ordenes.stream()
                .filter(o -> o.getIdOrden().equalsIgnoreCase(idOrden))
                .findFirst();

        if (ordenOpt.isEmpty()) {
            throw new Exception("No se encontró la orden con ID: " + idOrden);
        }

        OrdenCompra orden = ordenOpt.get();

        if (orden.getEstado() == EstadoOrden.COMPLETADA) {
            throw new Exception("La orden ya se encuentra COMPLETADA.");
        }
        if (orden.getEstado() == EstadoOrden.CANCELADA) {
            throw new Exception("No se puede completar una orden CANCELADA.");
        }

        procesarDescuentoStock(orden);

        orden.setEstado(EstadoOrden.COMPLETADA);
        JsonRepository.guardar(FILE_ORDENES, ordenes);
        JsonRepository.guardar(FILE_INVENTARIOS, inventarios);
    }

    public void cancelarOrden(String idOrden) throws Exception {
        Optional<OrdenCompra> ordenOpt = ordenes.stream()
                .filter(o -> o.getIdOrden().equalsIgnoreCase(idOrden))
                .findFirst();

        if (ordenOpt.isEmpty()) {
            throw new Exception("No se encontró la orden con ID: " + idOrden);
        }

        OrdenCompra orden = ordenOpt.get();
        if (orden.getEstado() == EstadoOrden.COMPLETADA) {
            throw new Exception("No se puede cancelar una orden que ya fue COMPLETADA (el stock ya fue deducido).");
        }

        orden.setEstado(EstadoOrden.CANCELADA);
        JsonRepository.guardar(FILE_ORDENES, ordenes);
    }

    /**
     * Valida y descuenta el stock de todos los productos en la orden.
     */
    private void procesarDescuentoStock(OrdenCompra orden) throws Exception {
        String idBodega = orden.getIdBodega();
        Inventario inventario = obtenerOCrearInventario(idBodega);

        // 1. Primera pasada: Validar que todos los productos tengan suficiente stock
        for (DetalleOrden detalle : orden.getItems()) {
            String codProd = detalle.getProducto().getCodigo();
            int cantSolicitada = detalle.getCantidad();
            int stockDisponible = inventario.obtenerStock(codProd);

            if (stockDisponible < cantSolicitada) {
                throw new Exception("Stock insuficiente en bodega " + idBodega + " para el producto '" +
                        detalle.getProducto().getNombre() + "' (" + codProd + "). Disponible: " +
                        stockDisponible + ", Solicitado: " + cantSolicitada);
            }
        }

        // 2. Segunda pasada: Restar existencias en el inventario
        for (DetalleOrden detalle : orden.getItems()) {
            inventario.descontarStock(detalle.getProducto().getCodigo(), detalle.getCantidad());
        }

        JsonRepository.guardar(FILE_INVENTARIOS, inventarios);
    }
}
