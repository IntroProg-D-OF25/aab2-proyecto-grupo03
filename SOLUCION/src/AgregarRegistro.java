import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class AgregarRegistro {

    // Method genérico para agregar una línea a un archivo
    public static void agregarLinea(String archivo, String linea) {
        try (FileWriter fw = new FileWriter(archivo, true);
             PrintWriter pw = new PrintWriter(fw)) {
            pw.println(linea);
        } catch (IOException e) {
            System.out.println("Error al escribir en " + archivo + ": " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        // Ejemplo: Agregar una nueva película
        String nuevaPelicula = "La Aventura, 3, 12.50";
        agregarLinea("peliculas.txt", nuevaPelicula);

        // Ejemplo: Agregar un nuevo snack
        String nuevoSnack = "Palomitas Grandes, 5.00";
        agregarLinea("snacks.txt", nuevoSnack);

        // Ejemplo: Agregar un nuevo combo
        String nuevoCombo = "Combo Familiar, 3, 15.00";
        agregarLinea("combos.txt", nuevoCombo);

        System.out.println("Nuevos registros agregados correctamente.");
    }
}
