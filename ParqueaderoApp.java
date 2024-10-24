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
    private final boolean esDiscapacitado;

    public Vehiculo(String placa, String marca, String color, int espacioAsignado, boolean esDiscapacitado) {
        this.placa = placa.toUpperCase();
        this.marca = marca;
        this.color = color;
        this.espacioAsignado = espacioAsignado;
        this.tiempoEntrada = LocalDateTime.now();
        this.esDiscapacitado = esDiscapacitado;
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

    public boolean esDiscapacitado() {
        return esDiscapacitado;
    }
}

public class ParqueaderoApp {
    private final HashMap<String, Vehiculo> vehiculos;
    private final boolean[] espacios;
    private final JFrame frame;
    private final JPanel panelParqueadero;
    private final JTextArea[] espacioAreas;

    public ParqueaderoApp() {
        vehiculos = new HashMap<>();
        espacios = new boolean[22];  // 20 espacios regulares y 2 para discapacitados
        frame = new JFrame("Parqueadero");
        frame.setSize(600, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JPanel panelEntrada = new JPanel(new FlowLayout());
        JButton btnRegistrarEntrada = new JButton("Registrar Entrada");
        JButton btnRegistrarSalida = new JButton("Registrar Salida");

        panelEntrada.add(btnRegistrarEntrada);
        panelEntrada.add(btnRegistrarSalida);

        panelParqueadero = new JPanel(new GridLayout(4, 6, 5, 5));  // 22 espacios visualizados en el grid
        espacioAreas = new JTextArea[22];
        for (int i = 0; i < 22; i++) {
            if (i >= 20) {
                espacioAreas[i] = new JTextArea("Espacio Discapacitados " + (i - 19));
            } else {
                espacioAreas[i] = new JTextArea("Espacio " + (i + 1));
            }
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
        // Selección del país
        String[] opcionesPais = {"Colombiana", "Venezolana"};
        String pais = (String) JOptionPane.showInputDialog(frame, "Seleccione el país de la placa:",
                "País", JOptionPane.QUESTION_MESSAGE, null, opcionesPais, opcionesPais[0]);

        if (pais == null) return;

        // Ingreso de la placa
        String placa = JOptionPane.showInputDialog(frame, "Ingrese la placa del vehículo:");
        if (placa == null || placa.trim().isEmpty()) return;
        placa = placa.toUpperCase();  // Convertir a mayúsculas automáticamente

        // Validar formato de placa
        if (pais.equals("Colombiana")) {
            if (!placa.matches("[A-Z]{3}[0-9]{3}")) {
                JOptionPane.showMessageDialog(frame, "Placa colombiana inválida. Debe ser 3 letras y 3 números.");
                return;
            }
        } else if (pais.equals("Venezolana")) {
            if (!placa.matches("[A-Z]{2}[0-9]{3}[A-Z]{2}")) {
                JOptionPane.showMessageDialog(frame, "Placa venezolana inválida. Debe ser 2 letras, 3 números, y 2 letras.");
                return;
            }
        }

        if (vehiculos.containsKey(placa)) {
            JOptionPane.showMessageDialog(frame, "El vehículo ya está registrado.");
            return;
        }

        // Preguntar si es discapacitado
        int respuestaDiscapacidad = JOptionPane.showConfirmDialog(frame, "¿Es usted una persona discapacitada?", 
                "Discapacidad", JOptionPane.YES_NO_OPTION);
        boolean esDiscapacitado = (respuestaDiscapacidad == JOptionPane.YES_OPTION);

        // Selección de marca
        String[] marcasComunes = {
            "Toyota", "Chevrolet", "Mazda", "Nissan", "Hyundai", 
            "Kia", "Ford", "Volkswagen", "Renault", "Honda", 
            "BMW", "Mercedes-Benz", "Audi", "Peugeot", "Jeep", 
            "Fiat", "Subaru", "Mitsubishi", "Suzuki", "Land Rover"
        };
        String marca = (String) JOptionPane.showInputDialog(frame, "Seleccione la marca del vehículo:", 
                "Marca", JOptionPane.QUESTION_MESSAGE, null, marcasComunes, marcasComunes[0]);

        if (marca == null || marca.trim().isEmpty()) return;

        // Selección de color
        String[] coloresComunes = {"Rojo", "Azul", "Negro", "Blanco", "Plateado", 
                "Gris", "Verde", "Amarillo", "Naranja", "Marrón"};
        String color = (String) JOptionPane.showInputDialog(frame, "Seleccione el color del vehículo:",
                "Color", JOptionPane.QUESTION_MESSAGE, null, coloresComunes, coloresComunes[0]);

        if (color == null || color.trim().isEmpty()) return;

        // Asignar espacio según discapacidad
        int espacioLibre;
        if (esDiscapacitado) {
            espacioLibre = obtenerEspacioLibre(true);  // Buscar espacio para discapacitados
            if (espacioLibre == -1) {
                // Si no hay espacios para discapacitados, asignar espacio regular
                espacioLibre = obtenerEspacioLibre(false);
                if (espacioLibre == -1) {
                    JOptionPane.showMessageDialog(frame, "No hay espacios disponibles.");
                    return;
                }
                JOptionPane.showMessageDialog(frame, "No hay espacios disponibles para discapacitados, se le asignará un espacio regular.");
            }
        } else {
            espacioLibre = obtenerEspacioLibre(false);  // Buscar espacio regular
            if (espacioLibre == -1) {
                JOptionPane.showMessageDialog(frame, "No hay espacios regulares disponibles.");
                return;
            }
        }

        // Registrar el vehículo
        Vehiculo vehiculo = new Vehiculo(placa, marca, color, espacioLibre, esDiscapacitado);
        vehiculos.put(placa, vehiculo);
        espacios[espacioLibre] = true;
        espacioAreas[espacioLibre].setText("Placa: " + placa + "\nMarca: " + marca + "\nColor: " + color);
        espacioAreas[espacioLibre].setBackground(Color.RED);
    }

    private void registrarSalida() {
        // Pedir la placa en un diálogo
        String placa = JOptionPane.showInputDialog(frame, "Ingrese la placa del vehículo para registrar su salida:");
        if (placa == null || placa.trim().isEmpty()) return;
        placa = placa.toUpperCase();  // Convertir a mayúsculas automáticamente

        Vehiculo vehiculo = vehiculos.get(placa);
        if (vehiculo == null) {
            JOptionPane.showMessageDialog(frame, "No se encontró el vehículo.");
            return;
        }

        int espacioAsignado = vehiculo.getEspacioAsignado();
        espacios[espacioAsignado] = false;
        espacioAreas[espacioAsignado].setText(espacioAsignado >= 20 ? 
                "Espacio Discapacitados " + (espacioAsignado - 19) :
                "Espacio " + (espacioAsignado + 1));
        espacioAreas[espacioAsignado].setBackground(Color.GREEN);
        vehiculos.remove(placa);
    }

    private int obtenerEspacioLibre(boolean paraDiscapacitados) {
        int inicio = paraDiscapacitados ? 20 : 0;
        int fin = paraDiscapacitados ? 22 : 20;

        for (int i = inicio; i < fin; i++) {
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
