import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class Main {

    // Registro de ventas:
    // Cada fila tiene: [película, sala, boletos vendidos, detalle de snack/combos, monto total]
    static String[][] registroVentas = new String[120][5];
    static int registroCount = 0; // Contador de ventas

    public static void main(String[] args) {
        // Cargar datos desde archivos de texto
        // Para peliculas se leen 5 campos: nombre, sala, horario, precio, clasificacion
        String[][] peliculas = loadArchivo("peliculas.txt", 5);
        String[][] snacks = loadArchivo("snacks.txt", 2);

        Scanner sc = new Scanner(System.in);
        int opcion;
        do {
            // Menú principal
            System.out.println("====================================");
            System.out.println("             CineMas-Loja");
            System.out.println("====================================");
            System.out.println("1. Ver Cartelera");
            System.out.println("2. Ver Snacks");
            System.out.println("3. Comprar");
            System.out.println("5. Ver Facturación");
            System.out.println("6. Salir del Programa");
            System.out.print("Seleccione una opción: ");
            opcion = sc.nextInt();
            sc.nextLine(); // Consumir salto de línea

            switch (opcion) {
                case 1:
                    verCartelera(peliculas, snacks, sc);
                    break;
                case 2:
                    verSnacks(peliculas, snacks, sc);
                    break;
                case 3:
                    verListaComprar(peliculas, snacks, sc);
                    break;
                case 5:
                    mostrarRegistroVentas();
                    break;
                case 6:
                    System.out.println("Saliendo del programa...");
                    break;
                default:
                    System.out.println("Opción inválida. Intente nuevamente.");
            }
        } while (opcion != 6);

        sc.close();
    }

    /**
     * Lee un archivo de texto y carga cada línea en un arreglo bidimensional.
     * Separa cada línea usando la coma (,) y quita espacios en blanco.
     *
     * @param filename  Nombre del archivo a leer.
     * @param numCampos Número de campos esperados por línea.
     * @return Arreglo bidimensional con los datos del archivo.
     */
    public static String[][] loadArchivo(String filename, int numCampos) {
        // Este bloque "try" sirve para dar la cantidad de columnas de "data" (line 80)
        int count = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            while (br.readLine() != null) {
                count++;
            }
        } catch (IOException e) {
            System.out.println("Error al leer el archivo " + filename + ": " + e.getMessage());
        }

        String[][] data = new String[count][numCampos];
        int index = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                for (int i = 0; i < parts.length && i < numCampos; i++) {
                    data[index][i] = parts[i].trim();
                }
                index++;
            }
        } catch (IOException e) {
            System.out.println("Error al leer el archivo " + filename + ": " + e.getMessage());
        }
        return data;
    }

    /**
     * Muestra la cartelera de películas (incluyendo horario y clasificación) y pregunta
     * si se desea comprar un boleto.
     *
     * @param peliculas Arreglo bidimensional con los datos de las películas.
     * @param sc        Scanner para la entrada de datos.
     */
    public static void verCartelera(String[][] peliculas, String[][] snacks, Scanner sc) {
        System.out.println("\n--- Cartelera de Películas ---");
        for (int i = 0; i < peliculas.length; i++) {
            // Cada línea: nombre, sala, horario, precio y clasificación.
            System.out.println((i + 1) + ". " + peliculas[i][0]
                    + " - Sala: " + peliculas[i][1]
                    + " - Horario: " + peliculas[i][2]
                    + " - Precio: $" + peliculas[i][3]
                    + " - Clasificación: " + peliculas[i][4]);
        }
        System.out.println("------------------------------\n");

        // Preguntar si desea comprar un boleto
        System.out.print("¿Desea comprar un boleto? (s/n): ");
        String resp = sc.nextLine();
        if (resp.equalsIgnoreCase("s")) {
            int a = 1;
            compra(peliculas, snacks, sc, a);
        }
    }

    /**
     * Muestra la lista de snacks y pregunta si se desea comprar alguno.
     *
     * @param snacks Arreglo bidimensional con los datos de los snacks.
     * @param sc     Scanner para la entrada de datos.
     */
    public static void verSnacks(String[][] peliculas, String[][] snacks, Scanner sc) {
        System.out.println("\n--- Lista de Snacks ---");
        for (int i = 0; i < snacks.length; i++) {
            System.out.println((i + 1) + ". " + snacks[i][0] + " - Precio: $" + snacks[i][1]);
        }
        System.out.println("-----------------------\n");

        // Preguntar si desea comprar un snack
        System.out.print("¿Desea comprar un snack? (s/n): ");
        String resp = sc.nextLine();
        if (resp.equalsIgnoreCase("s")) {
            int a = 2;
            compra(peliculas, snacks, sc, a);
        }
    }

    /**
     * Función de compra combinada: permite al usuario elegir si desea comprar primero
     * boletos o snacks, y luego ofrece la opción de agregar el otro producto.
     *
     * @param peliculas Arreglo con la cartelera.
     * @param snacks    Arreglo con la lista de snacks.
     * @param sc        Scanner para la entrada de datos.
     */
    public static void verListaComprar(String[][] peliculas, String[][] snacks, Scanner sc) {
        System.out.println("\n--- Proceso de Compra ---");
        System.out.println("¿Qué desea comprar primero?");
        System.out.println("1. Boletos");
        System.out.println("2. Snacks");
        System.out.print("Ingrese 1 o 2: ");
        int opcionInicial = sc.nextInt();
        sc.nextLine();
        compra(peliculas, snacks, sc, opcionInicial);
    }

    public static void compra(String[][] peliculas, String[][] snacks, Scanner sc, int opcionInicial) {

        // Variables para almacenar detalles de la compra
        String pelicula = "N/A";
        String sala = "N/A";
        String boletosVendidos = "N/A";
        String snackDetalle = "N/A";
        double totalCompra = 0.0;


        if (opcionInicial == 1) {
            // Compra de boletos primero.
            String[] datosBoletos = comprarBoletos(peliculas, sc);
            pelicula = datosBoletos[0];
            sala = datosBoletos[1];
            boletosVendidos = datosBoletos[2];
            totalCompra += Double.parseDouble(datosBoletos[3]);

            // Preguntar si desea agregar un snack.
            System.out.print("¿Desea agregar un snack a su compra? (s/n): ");
            String agregarSnack = sc.nextLine();
            if (agregarSnack.equalsIgnoreCase("s")) {
                String[] datosSnack = comprarSnacks(snacks, sc);
                snackDetalle = datosSnack[0];
                totalCompra += Double.parseDouble(datosSnack[1]);
            }
        } else if (opcionInicial == 2) {
            // Compra de snack primero.
            String[] datosSnack = comprarSnacks(snacks, sc);
            snackDetalle = datosSnack[0];
            totalCompra += Double.parseDouble(datosSnack[1]);

            // Preguntar si desea agregar boletos.
            System.out.print("¿Desea agregar boletos a su compra? (s/n): ");
            String agregarBoleto = sc.nextLine();
            if (agregarBoleto.equalsIgnoreCase("s")) {
                String[] datosBoletos = comprarBoletos(peliculas, sc);
                pelicula = datosBoletos[0];
                sala = datosBoletos[1];
                boletosVendidos = datosBoletos[2];
                totalCompra += Double.parseDouble(datosBoletos[3]);
            }
        } else {
            System.out.println("Opción inválida en el proceso de compra.");
            return;
        }

        // Mostrar factura combinada
        System.out.println("\n--- Factura de Compra ---");
        if (!pelicula.equals("N/A")) {
            System.out.println("Película: " + pelicula);
            System.out.println("Sala: " + sala);
            System.out.println("Boletos comprados: " + boletosVendidos);
        }
        if (!snackDetalle.equals("N/A")) {
            System.out.println("Snack(s): " + snackDetalle);
        }
        System.out.println("Total a pagar: $" + totalCompra);
        System.out.println("-------------------------\n");

        // Registrar la venta
        registroVentas[registroCount][0] = pelicula;
        registroVentas[registroCount][1] = sala;
        registroVentas[registroCount][2] = boletosVendidos;
        registroVentas[registroCount][3] = snackDetalle;
        registroVentas[registroCount][4] = String.valueOf(totalCompra);
        registroCount++;
    }

    /**
     * Función para procesar la compra de boletos.
     * Muestra la cartelera (con horario y clasificación), permite seleccionar la película,
     * ingresar la cantidad y aplicar promoción si corresponde.
     *
     * @param peliculas Arreglo con la cartelera.
     * @param sc        Scanner para la entrada de datos.
     * @return Arreglo de String con datos: [película, sala, cantidad boletos, total a pagar]
     */
    public static String[] comprarBoletos(String[][] peliculas, Scanner sc) {
        String[] datos = new String[4];
        System.out.println("\n--- Compra de Boletos ---");
        // Mostrar cartelera con todos los datos (5 campos)
        for (int i = 0; i < peliculas.length; i++) {
            System.out.println((i + 1) + ". " + peliculas[i][0]
                    + " - Sala: " + peliculas[i][1]
                    + " - Horario: " + peliculas[i][2]
                    + " - Precio: $" + peliculas[i][3]
                    + " - Clasificación: " + peliculas[i][4]);
        }
        System.out.print("Seleccione el número de la película: ");
        int seleccion = sc.nextInt();
        sc.nextLine();
        if (seleccion < 1 || seleccion > peliculas.length) {
            System.out.println("Selección inválida. Cancelando compra de boletos.");
            datos[0] = "N/A";
            datos[1] = "N/A";
            datos[2] = "N/A";
            datos[3] = "0.0";
            return datos;
        }

        String nombrePelicula = peliculas[seleccion - 1][0];
        String sala = peliculas[seleccion - 1][1];
        String horario = peliculas[seleccion - 1][2];
        double precioBoleto = Double.parseDouble(peliculas[seleccion - 1][3]);
        String clasificacion = peliculas[seleccion - 1][4];

        System.out.print("Ingrese la cantidad de boletos a comprar: ");
        int cantidadBoletos = sc.nextInt();
        sc.nextLine();
        System.out.print("¿Aplica promoción especial? (s/n): ");
        String promo = sc.nextLine();
        double descuento = (promo.equalsIgnoreCase("s")) ? 0.10 : 0.0;
        double subtotal = cantidadBoletos * precioBoleto;
        double totalBoletos = subtotal - (subtotal * descuento);

        // Mostrar detalle incluyendo horario y clasificación
        System.out.println("\n--- Detalle de Boletos ---");
        System.out.println("Película: " + nombrePelicula);
        System.out.println("Sala: " + sala);
        System.out.println("Horario: " + horario);
        System.out.println("Clasificación: " + clasificacion);
        System.out.println("Cantidad de boletos: " + cantidadBoletos);
        System.out.println("Subtotal: $" + subtotal);
        if (descuento > 0) {
            System.out.println("Descuento aplicado: -$" + (subtotal * descuento));
        }
        System.out.println("Total a pagar: $" + totalBoletos);
        System.out.println("--------------------------\n");

        datos[0] = nombrePelicula;
        datos[1] = sala;
        datos[2] = String.valueOf(cantidadBoletos);
        datos[3] = String.valueOf(totalBoletos);
        return datos;
    }

    /**
     * Función para procesar la compra de snacks.
     * Muestra la lista de snacks, permite seleccionar uno y la cantidad, y calcula el total.
     *
     * @param snacks Arreglo con la lista de snacks.
     * @param sc     Scanner para la entrada de datos.
     * @return Arreglo de String con datos: [detalle snack (nombre x cantidad), total a pagar]
     */
    public static String[] comprarSnacks(String[][] snacks, Scanner sc) {
        String[] datos = new String[2];
        System.out.println("\n--- Compra de Snacks ---");
        // Mostrar lista de snacks
        for (int i = 0; i < snacks.length; i++) {
            System.out.println((i + 1) + ". " + snacks[i][0] + " - Precio: $" + snacks[i][1]);
        }
        System.out.print("Seleccione el número del snack: ");
        int seleccion = sc.nextInt();
        sc.nextLine();
        if (seleccion < 1 || seleccion > snacks.length) {
            System.out.println("Selección inválida. Cancelando compra de snacks.");
            datos[0] = "N/A";
            datos[1] = "0.0";
            return datos;
        }

        String nombreSnack = snacks[seleccion - 1][0];
        double precioSnack = Double.parseDouble(snacks[seleccion - 1][1]);

        System.out.print("Ingrese la cantidad deseada: ");
        int cantidadSnack = sc.nextInt();
        sc.nextLine();

        double totalSnack = cantidadSnack * precioSnack;

        // Mostrar detalle de la compra de snacks
        System.out.println("\n--- Detalle de Snacks ---");
        System.out.println("Snack: " + nombreSnack);
        System.out.println("Cantidad: " + cantidadSnack);
        System.out.println("Total a pagar: $" + totalSnack);
        System.out.println("-------------------------\n");

        datos[0] = nombreSnack + " x " + cantidadSnack;
        datos[1] = String.valueOf(totalSnack);
        return datos;
    }

    /**
     * Muestra el registro de todas las ventas realizadas.
     */
    public static void mostrarRegistroVentas() {
        System.out.println("\n--- Registro de Ventas ---");
        if (registroCount == 0) {
            System.out.println("No se han realizado ventas.");
        } else {
            System.out.printf("%-20s %-10s %-10s %-25s %-10s\n", "Película", "Sala", "Boletos", "Snack/Combo", "Total");
            for (int i = 0; i < registroCount; i++) {
                System.out.printf("%-20s %-10s %-10s %-25s %-10s\n",
                        registroVentas[i][0],
                        registroVentas[i][1],
                        registroVentas[i][2],
                        registroVentas[i][3],
                        registroVentas[i][4]);
            }
        }
        System.out.println("---------------------------\n");
    }
}
