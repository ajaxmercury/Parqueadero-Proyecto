import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDateTime;
import java.util.HashMap;

class Vehiculo {
    private final String placa;
    private final String marca;
    private final String color;
    private final LocalDateTime tiempoEntrada;
    private final int espacioAsignado;

    public Vehiculo(String placa, String marca, String color, int espacioAsignado) {
        this.placa = placa;
        this.marca = marca;
        this.color = color;
        this.espacioAsignado = espacioAsignado;
        this.tiempoEntrada = LocalDateTime.now();
    }

    public String getPlaca() {
        return placa;
    }

    public String getMarca() {
        return marca;
    }

    public String getColor() {
        return color;
    }

    public int getEspacioAsignado() {
        return espacioAsignado;
    }
}

public class ParqueaderoApp {
    private final HashMap<String, Vehiculo> vehiculos;
    private final boolean[] espacios;
    private final JFrame frame;
    private final JTextField entradaPlacaField;
    private final JTextField salidaPlacaField;
    private final JPanel panelParqueadero;
    private final JTextArea[] espacioAreas;

    public ParqueaderoApp() {
        vehiculos = new HashMap<>();
        espacios = new boolean[20];
        frame = new JFrame("Parqueadero");
        frame.setSize(600, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JPanel panelEntrada = new JPanel(new FlowLayout());
        JLabel entradaPlacaLabel = new JLabel("Placa Vehículo (Entrada):");
        entradaPlacaField = new JTextField(10);
        JButton btnRegistrarEntrada = new JButton("Registrar Entrada");

        JLabel salidaPlacaLabel = new JLabel("Placa Vehículo (Salida):");
        salidaPlacaField = new JTextField(10);
        JButton btnRegistrarSalida = new JButton("Registrar Salida");

        panelEntrada.add(entradaPlacaLabel);
        panelEntrada.add(entradaPlacaField);
        panelEntrada.add(btnRegistrarEntrada);
        panelEntrada.add(salidaPlacaLabel);
        panelEntrada.add(salidaPlacaField);
        panelEntrada.add(btnRegistrarSalida);

        panelParqueadero = new JPanel(new GridLayout(4, 5, 5, 5));
        espacioAreas = new JTextArea[20];
        for (int i = 0; i < 20; i++) {
            espacioAreas[i] = new JTextArea("Espacio " + (i + 1));
            espacioAreas[i].setEditable(false);
            espacioAreas[i].setBorder(BorderFactory.createLineBorder(Color.BLACK));
            espacioAreas[i].setBackground(Color.GREEN);
            espacioAreas[i].setOpaque(true);
            espacioAreas[i].setLineWrap(true);
            panelParqueadero.add(espacioAreas[i]);
        }

        frame.add(panelEntrada, BorderLayout.NORTH);
        frame.add(panelParqueadero, BorderLayout.CENTER);

        btnRegistrarEntrada.addActionListener((ActionEvent e) -> registrarEntrada());
        btnRegistrarSalida.addActionListener((ActionEvent e) -> registrarSalida());

        frame.setVisible(true);
    }

    private void registrarEntrada() {
        String placa = entradaPlacaField.getText();
        if (vehiculos.containsKey(placa)) {
            JOptionPane.showMessageDialog(frame, "El vehículo ya está registrado.");
            return;
        }

        String marca = JOptionPane.showInputDialog(frame, "Ingrese la marca del vehículo:");
        if (marca == null || marca.trim().isEmpty()) return;

        String color = JOptionPane.showInputDialog(frame, "Ingrese el color del vehículo:");
        if (color == null || color.trim().isEmpty()) return;

        int espacioLibre = obtenerEspacioLibre();
        if (espacioLibre == -1) {
            JOptionPane.showMessageDialog(frame, "No hay espacios disponibles.");
            return;
        }

        Vehiculo vehiculo = new Vehiculo(placa, marca, color, espacioLibre);
        vehiculos.put(placa, vehiculo);
        espacios[espacioLibre] = true;
        espacioAreas[espacioLibre].setText("Placa: " + placa + "\nMarca: " + marca + "\nColor: " + color);
        espacioAreas[espacioLibre].setBackground(Color.RED);
        entradaPlacaField.setText("");
    }

    private void registrarSalida() {
        String placa = salidaPlacaField.getText();
        Vehiculo vehiculo = vehiculos.get(placa);
        if (vehiculo == null) {
            JOptionPane.showMessageDialog(frame, "No se encontró el vehículo.");
            return;
        }

        int espacioAsignado = vehiculo.getEspacioAsignado();
        espacios[espacioAsignado] = false;
        espacioAreas[espacioAsignado].setText("Espacio " + (espacioAsignado + 1));
        espacioAreas[espacioAsignado].setBackground(Color.GREEN);
        vehiculos.remove(placa);
        salidaPlacaField.setText("");
    }

    private int obtenerEspacioLibre() {
        for (int i = 0; i < espacios.length; i++) {
            if (!espacios[i]) {
                return i;
            }
        }
        return -1;
    }

    public static void main(String[] args) {
        new ParqueaderoApp();
    }
}
