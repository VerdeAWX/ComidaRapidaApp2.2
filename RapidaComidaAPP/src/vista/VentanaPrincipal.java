package vista;

import dao.EntregaDAO;
import dao.PedidoDAO;
import dao.RepartidorDAO;

import modelo.Entrega;
import modelo.Pedido;
import modelo.Repartidor;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class VentanaPrincipal extends JFrame {

    // ==========================================
    // DAO
    // ==========================================

    private final RepartidorDAO repartidorDAO;
    private final PedidoDAO pedidoDAO;
    private final EntregaDAO entregaDAO;

    // ==========================================
    // REPARTIDORES
    // ==========================================

    private JTextField txtNombreRepartidor;

    private JTable tablaRepartidores;

    private DefaultTableModel modeloRepartidores;

    // ==========================================
    // PEDIDOS
    // ==========================================

    private JTextField txtDireccion;

    private JComboBox<String> comboTipoPedido;

    private JComboBox<String> comboEstadoPedido;

    private JComboBox<String> comboFiltroTipo;

    private JComboBox<String> comboFiltroEstado;

    private JTable tablaPedidos;

    private DefaultTableModel modeloPedidos;

    // ==========================================
    // ENTREGAS
    // ==========================================

    private JComboBox<Pedido> comboPedidoEntrega;

    private JComboBox<Repartidor> comboRepartidorEntrega;

    private JTextField txtFechaHora;

    private JTable tablaEntregas;

    private DefaultTableModel modeloEntregas;

    // ==========================================
    // CONSTRUCTOR
    // ==========================================

    public VentanaPrincipal() {

        repartidorDAO = new RepartidorDAO();

        pedidoDAO = new PedidoDAO();

        entregaDAO = new EntregaDAO();

        configurarVentana();

        crearInterfaz();

        cargarRepartidores();

        cargarPedidos();

        cargarCombosEntrega();

        cargarEntregas();
    }

    // ==========================================
    // CONFIGURACIÓN
    // ==========================================

    private void configurarVentana() {

        setTitle("SpeedFast - Sistema de Gestión");

        setSize(1000, 700);

        setLocationRelativeTo(null);

        setDefaultCloseOperation(
                JFrame.EXIT_ON_CLOSE
        );
    }

    // ==========================================
    // INTERFAZ PRINCIPAL
    // ==========================================

    private void crearInterfaz() {

        JPanel panelPrincipal =
                new JPanel(new BorderLayout());

        JLabel titulo =
                new JLabel(
                        "SPEEDFAST - Gestión de Pedidos",
                        SwingConstants.CENTER
                );

        titulo.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        24
                )
        );

        titulo.setBorder(
                BorderFactory.createEmptyBorder(
                        15,
                        10,
                        15,
                        10
                )
        );

        panelPrincipal.add(
                titulo,
                BorderLayout.NORTH
        );

        JTabbedPane pestañas =
                new JTabbedPane();

        pestañas.addTab(
                "Repartidores",
                crearPanelRepartidores()
        );

        pestañas.addTab(
                "Pedidos",
                crearPanelPedidos()
        );

        pestañas.addTab(
                "Entregas",
                crearPanelEntregas()
        );

        panelPrincipal.add(
                pestañas,
                BorderLayout.CENTER
        );

        add(panelPrincipal);
    }

    // =====================================================
    // PANEL REPARTIDORES
    // =====================================================

    private JPanel crearPanelRepartidores() {

        JPanel panel =
                new JPanel(new BorderLayout(10, 10));

        panel.setBorder(
                BorderFactory.createEmptyBorder(
                        10,
                        10,
                        10,
                        10
                )
        );

        // -----------------------------
        // FORMULARIO
        // -----------------------------

        JPanel formulario =
                new JPanel();

        formulario.add(
                new JLabel("Nombre:")
        );

        txtNombreRepartidor =
                new JTextField(20);

        formulario.add(
                txtNombreRepartidor
        );

        JButton btnRegistrar =
                new JButton("Registrar");

        JButton btnActualizar =
                new JButton("Actualizar");

        JButton btnEliminar =
                new JButton("Eliminar");

        formulario.add(btnRegistrar);

        formulario.add(btnActualizar);

        formulario.add(btnEliminar);

        panel.add(
                formulario,
                BorderLayout.NORTH
        );

        // -----------------------------
        // TABLA
        // -----------------------------

        modeloRepartidores =
                new DefaultTableModel(
                        new String[]{
                                "ID",
                                "Nombre"
                        },
                        0
                );

        tablaRepartidores =
                new JTable(modeloRepartidores);

        panel.add(
                new JScrollPane(
                        tablaRepartidores
                ),
                BorderLayout.CENTER
        );

        // -----------------------------
        // REGISTRAR
        // -----------------------------

        btnRegistrar.addActionListener(e -> {

            String nombre =
                    txtNombreRepartidor
                            .getText()
                            .trim();

            if (nombre.isEmpty()) {

                mostrarAdvertencia(
                        "Debe ingresar el nombre."
                );

                return;
            }

            Repartidor repartidor =
                    new Repartidor(nombre);

            if (repartidorDAO.create(
                    repartidor
            )) {

                mostrarMensaje(
                        "Repartidor registrado correctamente."
                );

                txtNombreRepartidor.setText("");

                cargarRepartidores();

                cargarCombosEntrega();

            } else {

                mostrarError(
                        "No se pudo registrar el repartidor."
                );
            }
        });

        // -----------------------------
        // ACTUALIZAR
        // -----------------------------

        btnActualizar.addActionListener(e -> {

            int fila =
                    tablaRepartidores
                            .getSelectedRow();

            if (fila == -1) {

                mostrarAdvertencia(
                        "Seleccione un repartidor."
                );

                return;
            }

            String nombre =
                    txtNombreRepartidor
                            .getText()
                            .trim();

            if (nombre.isEmpty()) {

                mostrarAdvertencia(
                        "Ingrese el nombre."
                );

                return;
            }

            int id =
                    (int) modeloRepartidores
                            .getValueAt(fila, 0);

            Repartidor repartidor =
                    new Repartidor(
                            id,
                            nombre
                    );

            if (repartidorDAO.update(
                    repartidor
            )) {

                mostrarMensaje(
                        "Repartidor actualizado."
                );

                cargarRepartidores();

                cargarCombosEntrega();

            } else {

                mostrarError(
                        "No se pudo actualizar."
                );
            }
        });

        // -----------------------------
        // ELIMINAR
        // -----------------------------

        btnEliminar.addActionListener(e -> {

            int fila =
                    tablaRepartidores
                            .getSelectedRow();

            if (fila == -1) {

                mostrarAdvertencia(
                        "Seleccione un repartidor."
                );

                return;
            }

            int id =
                    (int) modeloRepartidores
                            .getValueAt(fila, 0);

            int respuesta =
                    JOptionPane.showConfirmDialog(
                            this,
                            "¿Desea eliminar este repartidor?",
                            "Confirmar eliminación",
                            JOptionPane.YES_NO_OPTION
                    );

            if (respuesta ==
                    JOptionPane.YES_OPTION) {

                if (repartidorDAO.delete(id)) {

                    mostrarMensaje(
                            "Repartidor eliminado."
                    );

                    cargarRepartidores();

                    cargarCombosEntrega();

                } else {

                    mostrarError(
                            "No se pudo eliminar. "
                                    + "Puede tener entregas asociadas."
                    );
                }
            }
        });

        // -----------------------------
        // SELECCIONAR FILA
        // -----------------------------

        tablaRepartidores
                .getSelectionModel()
                .addListSelectionListener(e -> {

                    int fila =
                            tablaRepartidores
                                    .getSelectedRow();

                    if (fila >= 0) {

                        txtNombreRepartidor
                                .setText(
                                        modeloRepartidores
                                                .getValueAt(
                                                        fila,
                                                        1
                                                )
                                                .toString()
                                );
                    }
                });

        return panel;
    }

    // =====================================================
    // PANEL PEDIDOS
    // =====================================================

    private JPanel crearPanelPedidos() {

        JPanel panel =
                new JPanel(new BorderLayout(10, 10));

        panel.setBorder(
                BorderFactory.createEmptyBorder(
                        10,
                        10,
                        10,
                        10
                )
        );

        // ==========================================
        // FORMULARIO
        // ==========================================

        JPanel formulario =
                new JPanel(
                        new GridLayout(
                                4,
                                2,
                                5,
                                5
                        )
                );

        formulario.add(
                new JLabel("Dirección:")
        );

        txtDireccion =
                new JTextField();

        formulario.add(
                txtDireccion
        );

        formulario.add(
                new JLabel("Tipo:")
        );

        comboTipoPedido =
                new JComboBox<>(
                        new String[]{
                                "COMIDA",
                                "ENCOMIENDA",
                                "EXPRESS"
                        }
                );

        formulario.add(
                comboTipoPedido
        );

        formulario.add(
                new JLabel("Estado:")
        );

        comboEstadoPedido =
                new JComboBox<>(
                        new String[]{
                                "PENDIENTE",
                                "EN_REPARTO",
                                "ENTREGADO"
                        }
                );

        formulario.add(
                comboEstadoPedido
        );

        JButton btnRegistrar =
                new JButton("Registrar");

        JButton btnActualizar =
                new JButton("Actualizar");

        formulario.add(btnRegistrar);

        formulario.add(btnActualizar);

        panel.add(
                formulario,
                BorderLayout.NORTH
        );

        // ==========================================
        // TABLA
        // ==========================================

        modeloPedidos =
                new DefaultTableModel(
                        new String[]{
                                "ID",
                                "Dirección",
                                "Tipo",
                                "Estado"
                        },
                        0
                );

        tablaPedidos =
                new JTable(modeloPedidos);

        panel.add(
                new JScrollPane(
                        tablaPedidos
                ),
                BorderLayout.CENTER
        );

        // ==========================================
        // PARTE INFERIOR
        // ==========================================

        JPanel inferior =
                new JPanel(
                        new BorderLayout()
                );

        JPanel filtros =
                new JPanel();

        filtros.add(
                new JLabel("Filtrar Estado:")
        );

        comboFiltroEstado =
                new JComboBox<>(
                        new String[]{
                                "TODOS",
                                "PENDIENTE",
                                "EN_REPARTO",
                                "ENTREGADO"
                        }
                );

        filtros.add(
                comboFiltroEstado
        );

        filtros.add(
                new JLabel("Filtrar Tipo:")
        );

        comboFiltroTipo =
                new JComboBox<>(
                        new String[]{
                                "TODOS",
                                "COMIDA",
                                "ENCOMIENDA",
                                "EXPRESS"
                        }
                );

        filtros.add(
                comboFiltroTipo
        );

        JButton btnFiltrar =
                new JButton("Filtrar");

        JButton btnMostrarTodos =
                new JButton("Mostrar todos");

        JButton btnEliminar =
                new JButton("Eliminar");

        filtros.add(btnFiltrar);

        filtros.add(btnMostrarTodos);

        filtros.add(btnEliminar);

        inferior.add(
                filtros,
                BorderLayout.CENTER
        );

        panel.add(
                inferior,
                BorderLayout.SOUTH
        );

        // ==========================================
        // REGISTRAR
        // ==========================================

        btnRegistrar.addActionListener(e -> {

            if (!validarPedido()) {
                return;
            }

            Pedido pedido =
                    new Pedido(
                            txtDireccion
                                    .getText()
                                    .trim(),

                            comboTipoPedido
                                    .getSelectedItem()
                                    .toString(),

                            comboEstadoPedido
                                    .getSelectedItem()
                                    .toString()
                    );

            if (pedidoDAO.create(pedido)) {

                mostrarMensaje(
                        "Pedido registrado correctamente."
                );

                limpiarPedido();

                cargarPedidos();

                cargarCombosEntrega();

            } else {

                mostrarError(
                        "No se pudo registrar el pedido."
                );
            }
        });

        // ==========================================
        // ACTUALIZAR
        // ==========================================

        btnActualizar.addActionListener(e -> {

            int fila =
                    tablaPedidos.getSelectedRow();

            if (fila == -1) {

                mostrarAdvertencia(
                        "Seleccione un pedido."
                );

                return;
            }

            if (!validarPedido()) {
                return;
            }

            int id =
                    (int) modeloPedidos
                            .getValueAt(fila, 0);

            Pedido pedido =
                    new Pedido(
                            id,
                            txtDireccion
                                    .getText()
                                    .trim(),

                            comboTipoPedido
                                    .getSelectedItem()
                                    .toString(),

                            comboEstadoPedido
                                    .getSelectedItem()
                                    .toString()
                    );

            if (pedidoDAO.update(pedido)) {

                mostrarMensaje(
                        "Pedido actualizado correctamente."
                );

                limpiarPedido();

                cargarPedidos();

                cargarCombosEntrega();

            } else {

                mostrarError(
                        "No se pudo actualizar."
                );
            }
        });

        // ==========================================
        // ELIMINAR
        // ==========================================

        btnEliminar.addActionListener(e -> {

            int fila =
                    tablaPedidos.getSelectedRow();

            if (fila == -1) {

                mostrarAdvertencia(
                        "Seleccione un pedido."
                );

                return;
            }

            int id =
                    (int) modeloPedidos
                            .getValueAt(fila, 0);

            int respuesta =
                    JOptionPane.showConfirmDialog(
                            this,
                            "¿Desea eliminar este pedido?",
                            "Confirmar",
                            JOptionPane.YES_NO_OPTION
                    );

            if (respuesta ==
                    JOptionPane.YES_OPTION) {

                if (pedidoDAO.delete(id)) {

                    mostrarMensaje(
                            "Pedido eliminado."
                    );

                    limpiarPedido();

                    cargarPedidos();

                    cargarCombosEntrega();

                } else {

                    mostrarError(
                            "No se pudo eliminar el pedido. "
                                    + "Puede tener una entrega asociada."
                    );
                }
            }
        });

        // ==========================================
        // FILTRAR
        // ==========================================

        btnFiltrar.addActionListener(e ->
                cargarPedidos()
        );

        // ==========================================
        // MOSTRAR TODOS
        // ==========================================

        btnMostrarTodos.addActionListener(e -> {

            comboFiltroEstado
                    .setSelectedItem("TODOS");

            comboFiltroTipo
                    .setSelectedItem("TODOS");

            cargarPedidos();
        });

        // ==========================================
        // SELECCIONAR TABLA
        // ==========================================

        tablaPedidos
                .getSelectionModel()
                .addListSelectionListener(e -> {

                    int fila =
                            tablaPedidos
                                    .getSelectedRow();

                    if (fila >= 0) {

                        txtDireccion.setText(
                                modeloPedidos
                                        .getValueAt(
                                                fila,
                                                1
                                        )
                                        .toString()
                        );

                        comboTipoPedido
                                .setSelectedItem(
                                        modeloPedidos
                                                .getValueAt(
                                                        fila,
                                                        2
                                                )
                                );

                        comboEstadoPedido
                                .setSelectedItem(
                                        modeloPedidos
                                                .getValueAt(
                                                        fila,
                                                        3
                                                )
                                );
                    }
                });

        return panel;
    }

    // =====================================================
    // PANEL ENTREGAS
    // =====================================================

    private JPanel crearPanelEntregas() {

        JPanel panel =
                new JPanel(new BorderLayout(10, 10));

        panel.setBorder(
                BorderFactory.createEmptyBorder(
                        10,
                        10,
                        10,
                        10
                )
        );

        // ==========================================
        // FORMULARIO
        // ==========================================

        JPanel formulario =
                new JPanel(
                        new GridLayout(
                                4,
                                2,
                                5,
                                5
                        )
                );

        formulario.add(
                new JLabel("Pedido:")
        );

        comboPedidoEntrega =
                new JComboBox<>();

        formulario.add(
                comboPedidoEntrega
        );

        formulario.add(
                new JLabel("Repartidor:")
        );

        comboRepartidorEntrega =
                new JComboBox<>();

        formulario.add(
                comboRepartidorEntrega
        );

        formulario.add(
                new JLabel("Fecha y hora:")
        );

        txtFechaHora =
                new JTextField();

        txtFechaHora.setToolTipText(
                "Formato: yyyy-MM-dd HH:mm"
        );

        formulario.add(
                txtFechaHora
        );

        JButton btnRegistrar =
                new JButton("Registrar");

        JButton btnActualizar =
                new JButton("Actualizar");

        formulario.add(btnRegistrar);

        formulario.add(btnActualizar);

        panel.add(
                formulario,
                BorderLayout.NORTH
        );

        // ==========================================
        // TABLA
        // ==========================================

        modeloEntregas =
                new DefaultTableModel(
                        new String[]{
                                "ID",
                                "Pedido ID",
                                "Repartidor ID",
                                "Fecha y Hora"
                        },
                        0
                );

        tablaEntregas =
                new JTable(modeloEntregas);

        panel.add(
                new JScrollPane(
                        tablaEntregas
                ),
                BorderLayout.CENTER
        );

        // ==========================================
        // BOTÓN ELIMINAR
        // ==========================================

        JButton btnEliminar =
                new JButton("Eliminar");

        JPanel botones =
                new JPanel();

        botones.add(btnEliminar);

        panel.add(
                botones,
                BorderLayout.SOUTH
        );

        // ==========================================
        // REGISTRAR
        // ==========================================

        btnRegistrar.addActionListener(e -> {

            if (!validarEntrega()) {
                return;
            }

            Pedido pedido =
                    (Pedido)
                            comboPedidoEntrega
                                    .getSelectedItem();

            Repartidor repartidor =
                    (Repartidor)
                            comboRepartidorEntrega
                                    .getSelectedItem();

            LocalDateTime fechaHora =
                    convertirFechaHora();

            Entrega entrega =
                    new Entrega(
                            pedido.getId(),
                            repartidor.getId(),
                            fechaHora
                    );

            if (entregaDAO.create(entrega)) {

                mostrarMensaje(
                        "Entrega registrada correctamente."
                );

                txtFechaHora.setText("");

                cargarEntregas();

            } else {

                mostrarError(
                        "No se pudo registrar la entrega."
                );
            }
        });

        // ==========================================
        // ACTUALIZAR
        // ==========================================

        btnActualizar.addActionListener(e -> {

            int fila =
                    tablaEntregas
                            .getSelectedRow();

            if (fila == -1) {

                mostrarAdvertencia(
                        "Seleccione una entrega."
                );

                return;
            }

            if (!validarEntrega()) {
                return;
            }

            Pedido pedido =
                    (Pedido)
                            comboPedidoEntrega
                                    .getSelectedItem();

            Repartidor repartidor =
                    (Repartidor)
                            comboRepartidorEntrega
                                    .getSelectedItem();

            LocalDateTime fechaHora =
                    convertirFechaHora();

            int id =
                    (int) modeloEntregas
                            .getValueAt(
                                    fila,
                                    0
                            );

            Entrega entrega =
                    new Entrega(
                            id,
                            pedido.getId(),
                            repartidor.getId(),
                            fechaHora
                    );

            if (entregaDAO.update(entrega)) {

                mostrarMensaje(
                        "Entrega actualizada correctamente."
                );

                cargarEntregas();

            } else {

                mostrarError(
                        "No se pudo actualizar la entrega."
                );
            }
        });

        // ==========================================
        // ELIMINAR
        // ==========================================

        btnEliminar.addActionListener(e -> {

            int fila =
                    tablaEntregas
                            .getSelectedRow();

            if (fila == -1) {

                mostrarAdvertencia(
                        "Seleccione una entrega."
                );

                return;
            }

            int id =
                    (int) modeloEntregas
                            .getValueAt(
                                    fila,
                                    0
                            );

            int respuesta =
                    JOptionPane.showConfirmDialog(
                            this,
                            "¿Desea eliminar esta entrega?",
                            "Confirmar",
                            JOptionPane.YES_NO_OPTION
                    );

            if (respuesta ==
                    JOptionPane.YES_OPTION) {

                if (entregaDAO.delete(id)) {

                    mostrarMensaje(
                            "Entrega eliminada."
                    );

                    cargarEntregas();

                } else {

                    mostrarError(
                            "No se pudo eliminar."
                    );
                }
            }
        });

        return panel;
    }

    // =====================================================
    // CARGAR REPARTIDORES
    // =====================================================

    private void cargarRepartidores() {

        modeloRepartidores.setRowCount(0);

        for (Repartidor repartidor :
                repartidorDAO.readAll()) {

            modeloRepartidores.addRow(
                    new Object[]{
                            repartidor.getId(),
                            repartidor.getNombre()
                    }
            );
        }
    }

    // =====================================================
    // CARGAR PEDIDOS
    // =====================================================

    private void cargarPedidos() {

        modeloPedidos.setRowCount(0);

        String estado =
                comboFiltroEstado
                        .getSelectedItem()
                        .toString();

        String tipo =
                comboFiltroTipo
                        .getSelectedItem()
                        .toString();

        for (Pedido pedido :
                pedidoDAO.filtrar(
                        estado,
                        tipo
                )) {

            modeloPedidos.addRow(
                    new Object[]{
                            pedido.getId(),
                            pedido.getDireccion(),
                            pedido.getTipo(),
                            pedido.getEstado()
                    }
            );
        }
    }

    // =====================================================
    // CARGAR COMBOS DE ENTREGA
    // =====================================================

    private void cargarCombosEntrega() {

        if (comboPedidoEntrega == null ||
                comboRepartidorEntrega == null) {

            return;
        }

        comboPedidoEntrega.removeAllItems();

        for (Pedido pedido :
                pedidoDAO.readAll()) {

            comboPedidoEntrega.addItem(
                    pedido
            );
        }

        comboRepartidorEntrega.removeAllItems();

        for (Repartidor repartidor :
                repartidorDAO.readAll()) {

            comboRepartidorEntrega.addItem(
                    repartidor
            );
        }
    }

    // =====================================================
    // CARGAR ENTREGAS
    // =====================================================

    private void cargarEntregas() {

        modeloEntregas.setRowCount(0);

        DateTimeFormatter formato =
                DateTimeFormatter.ofPattern(
                        "yyyy-MM-dd HH:mm"
                );

        for (Entrega entrega :
                entregaDAO.readAll()) {

            String fecha = "";

            if (entrega.getFechaHora() != null) {

                fecha =
                        entrega
                                .getFechaHora()
                                .format(formato);
            }

            modeloEntregas.addRow(
                    new Object[]{
                            entrega.getId(),
                            entrega.getPedidoId(),
                            entrega.getRepartidorId(),
                            fecha
                    }
            );
        }
    }

    // =====================================================
    // VALIDAR PEDIDO
    // =====================================================

    private boolean validarPedido() {

        String direccion =
                txtDireccion
                        .getText()
                        .trim();

        if (direccion.isEmpty()) {

            mostrarAdvertencia(
                    "La dirección es obligatoria."
            );

            txtDireccion.requestFocus();

            return false;
        }

        if (direccion.length() < 5) {

            mostrarAdvertencia(
                    "La dirección debe tener al menos 5 caracteres."
            );

            txtDireccion.requestFocus();

            return false;
        }

        return true;
    }

    // =====================================================
    // VALIDAR ENTREGA
    // =====================================================

    private boolean validarEntrega() {

        if (comboPedidoEntrega
                .getSelectedItem() == null) {

            mostrarAdvertencia(
                    "Debe seleccionar un pedido."
            );

            return false;
        }

        if (comboRepartidorEntrega
                .getSelectedItem() == null) {

            mostrarAdvertencia(
                    "Debe seleccionar un repartidor."
            );

            return false;
        }

        if (txtFechaHora
                .getText()
                .trim()
                .isEmpty()) {

            mostrarAdvertencia(
                    "Debe ingresar la fecha y hora."
            );

            txtFechaHora.requestFocus();

            return false;
        }

        try {

            convertirFechaHora();

        } catch (Exception e) {

            mostrarAdvertencia(
                    "La fecha debe tener el formato:"
                            + "\n"
                            + "yyyy-MM-dd HH:mm"
            );

            return false;
        }

        return true;
    }

    // =====================================================
    // CONVERTIR FECHA
    // =====================================================

    private LocalDateTime convertirFechaHora() {

        DateTimeFormatter formato =
                DateTimeFormatter.ofPattern(
                        "yyyy-MM-dd HH:mm"
                );

        return LocalDateTime.parse(
                txtFechaHora
                        .getText()
                        .trim(),
                formato
        );
    }

    // =====================================================
    // LIMPIAR PEDIDO
    // =====================================================

    private void limpiarPedido() {

        txtDireccion.setText("");

        comboTipoPedido
                .setSelectedIndex(0);

        comboEstadoPedido
                .setSelectedIndex(0);

        tablaPedidos.clearSelection();
    }

    // =====================================================
    // MENSAJES
    // =====================================================

    private void mostrarMensaje(
            String mensaje
    ) {

        JOptionPane.showMessageDialog(
                this,
                mensaje,
                "SpeedFast",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void mostrarAdvertencia(
            String mensaje
    ) {

        JOptionPane.showMessageDialog(
                this,
                mensaje,
                "Validación",
                JOptionPane.WARNING_MESSAGE
        );
    }

    private void mostrarError(
            String mensaje
    ) {

        JOptionPane.showMessageDialog(
                this,
                mensaje,
                "Error",
                JOptionPane.ERROR_MESSAGE
        );
    }

    // =====================================================
    // MAIN
    // =====================================================

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            VentanaPrincipal ventana =
                    new VentanaPrincipal();

            ventana.setVisible(true);
        });
    }
}
