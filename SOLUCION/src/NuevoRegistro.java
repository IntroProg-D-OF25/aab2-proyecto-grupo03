import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class NuevoRegistro {


    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int opcion;
        do {
            System.out.println("Seleccione una opcion: ");
            System.out.println("1. Agregar peliculas");
            System.out.println("2. Agregar snacks");
            System.out.println("3. Salir");
            opcion = sc.nextInt();
            sc.nextLine();
            switch (opcion) {

                case 1:
                    System.out.println("Ingrese el titulo: ");
                    String titulo = sc.nextLine();
                    System.out.println("Ingrese la sala de proyección: ");
                    String salaProyeccion = sc.nextLine();
                    System.out.println("Ingrese la hora de inicio: ");
                    String hora = sc.nextLine();
                    System.out.println("Ingrese el costo de la entrada: ");
                    String costo = sc.nextLine();
                    System.out.println("Ingrese la clasificacion de la pelicula: ");
                    String clasificacion = sc.nextLine();
                    agregarPeli(titulo, salaProyeccion, hora, costo, clasificacion);
                    System.out.println("Nuevos registros agregados correctamente.");
                    break;
                case 2:
                    System.out.println("Ingrese el nombre del snack: ");
                    String nombre = sc.nextLine();
                    System.out.println("Ingrese el costo del snack: ");
                    String precio = sc.nextLine();
                    agregarSnack(nombre, precio);
                    System.out.println("Nuevos registros agregados correctamente.");
                    break;
                case 3:
                    System.out.println("Saliendo del programa...");
                    break;
                default:
                    System.out.println("Opcion no valida.");
                    break;
            }
        } while (opcion != 3);
        sc.close();
    }

    // Method genérico para agregar una línea a un archivo
    public static void agregarLinea(String archivo, String linea) {
        try (FileWriter fw = new FileWriter(archivo, true);
             PrintWriter pw = new PrintWriter(fw)) {
            pw.println(linea);
        } catch (IOException e) {
            System.out.println("Error al escribir en " + archivo + ": " + e.getMessage());
        }
    }

    public static void agregarPeli(String nPeli, String sala, String hora, String precio, String clasificacion) {
        String nuevaPelicula = nPeli + "," + sala + "," + hora + "," + precio + "," + clasificacion;
        agregarLinea("peliculas.txt", nuevaPelicula);
    }

    public static void agregarSnack(String nSnack, String precio) {
        String nuevoSnack = nSnack + "," + precio;
        agregarLinea("snacks.txt", nuevoSnack);
    }
}
