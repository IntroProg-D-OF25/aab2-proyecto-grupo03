import sys

# Registro de ventas: cada venta es una lista con: [película, sala, boletos vendidos, detalle de snack, monto total]
registro_ventas = []  # Usaremos una lista para almacenar las ventas

def main():
    # Cargar datos desde archivos de texto
    # Para peliculas se leen 5 campos: nombre, sala, horario, precio, clasificacion
    peliculas = cargar_archivo("peliculas.txt", 5)
    snacks = cargar_archivo("snacks.txt", 2)
    while True:
        print("====================================")
        print("             CineMas-Loja")
        print("====================================")
        print("1. Ver Cartelera")
        print("2. Ver Snacks")
        print("3. Comprar")
        print("5. Ver Facturación")
        print("6. Salir del Programa")
        try:
            opcion = int(input("Seleccione una opción: "))
        except ValueError:
            print("Opción inválida. Intente nuevamente.\n")
            continue
        if opcion == 1:
            ver_cartelera(peliculas, snacks)
        elif opcion == 2:
            ver_snacks(peliculas, snacks)
        elif opcion == 3:
            ver_lista_comprar(peliculas, snacks)
        elif opcion == 5:
            mostrar_registro_ventas()
        elif opcion == 6:
            print("Saliendo del programa...")
            sys.exit(0)
        else:
            print("Opción inválida. Intente nuevamente.\n")


def cargar_archivo(filename, num_campos):
    """
    Lee un archivo de texto y carga cada línea en una lista de listas.
    Separa cada línea usando la coma (,) y quita espacios en blanco.
    :param  filename:       Nombre del archivo a leer.
    :param  num_campos:     Número de campos esperados por línea.
    :return:                Lista de listas con los datos del archivo.
    """
    data = []
    try:
        with open(filename, 'r', encoding='utf-8') as file:
            for line in file:
                # Eliminar salto de línea y separar por comas
                partes = [parte.strip() for parte in line.strip().split(',')]
                # Si la línea tiene menos elementos de los esperados, se rellenan con cadenas vacías
                if len(partes) < num_campos:
                    partes.extend([""] * (num_campos - len(partes)))
                data.append(partes[:num_campos])
    except IOError as e:
        print(f"Error al leer el archivo {filename}: {e}")
    return data


def ver_cartelera(peliculas, snacks):
    """
    Muestra la cartelera de películas y pregunta si se desea comprar un boleto.
    """
    print("\n--- Cartelera de Películas ---")
    for i, pelicula in enumerate(peliculas):
        # Cada línea: nombre, sala, horario, precio y clasificación.
        print(f"{i + 1}. {pelicula[0]} - Sala: {pelicula[1]} - Horario: {pelicula[2]} - Precio: ${pelicula[3]} - Clasificación: {pelicula[4]}")
    print("------------------------------\n")
    resp = input("¿Desea comprar un boleto? (s/n): ")
    if resp.lower() == "s":
        compra(peliculas, snacks, 1)


def ver_snacks(peliculas, snacks):
    """
    Muestra la lista de snacks y pregunta si se desea comprar alguno.
    """
    print("\n--- Lista de Snacks ---")
    for i, snack in enumerate(snacks):
        print(f"{i + 1}. {snack[0]} - Precio: ${snack[1]}")
    print("-----------------------\n")

    resp = input("¿Desea comprar un snack? (s/n): ")
    if resp.lower() == "s":
        compra(peliculas, snacks, 2)


def ver_lista_comprar(peliculas, snacks):
    """
    Permite al usuario elegir si desea comprar primero, boletos o snacks, y luego ofrece la opción de agregar el otro producto.
    """
    print("\n--- Proceso de Compra ---")
    print("¿Qué desea comprar?")
    print("1. Boletos")
    print("2. Snacks")
    print("3. Volver al menu principal")
    try:
        opcion_inicial = int(input("Ingrese 1 o 2: "))
    except ValueError:
        print("Entrada inválida. Cancelando la compra.")
        return
    compra(peliculas, snacks, opcion_inicial)


def compra(peliculas, snacks, opcion_inicial):
    """
    Dependiendo de la opción inicial, permite comprar boletos y/o snacks.
    """
    # Variables para almacenar detalles de la compra
    pelicula_nombre = sala = boletos_vendidos = snack_detalle = "N/A"
    total_compra = 0.0
    if opcion_inicial == 1:
        # Compra de boletos primero.
        datos_boletos = comprar_boletos(peliculas)
        pelicula_nombre, sala, boletos_vendidos, total_b = datos_boletos
        total_compra += total_b

        agregar_snack = input("¿Desea agregar un snack a su compra? (s/n): ")
        if agregar_snack.lower() == "s":
            datos_snack = comprar_snacks(snacks)
            snack_detalle, total_s = datos_snack
            total_compra += total_s

    elif opcion_inicial == 2:
        # Compra de snack primero.
        datos_snack = comprar_snacks(snacks)
        snack_detalle, total_s = datos_snack
        total_compra += total_s

        agregar_boleto = input("¿Desea agregar boletos a su compra? (s/n): ")
        if agregar_boleto.lower() == "s":
            datos_boletos = comprar_boletos(peliculas)
            pelicula_nombre, sala, boletos_vendidos, total_b = datos_boletos
            total_compra += total_b
    elif opcion_inicial == 3:
        return
    else:
        print("Opción inválida en el proceso de compra.")
        return
    # Mostrar factura combinada
    print("\n--- Factura de Compra ---")
    if pelicula_nombre != "N/A":
        print(f"Película: {pelicula_nombre}")
        print(f"Sala: {sala}")
        print(f"Boletos comprados: {boletos_vendidos}")
    if snack_detalle != "N/A":
        print(f"Snack(s): {snack_detalle}")
    print(f"Total a pagar: ${total_compra}")
    print("-------------------------\n")
    # Registrar la venta
    registro_ventas.append([
        pelicula_nombre,
        sala,
        boletos_vendidos,
        snack_detalle,
        str(total_compra)
    ])


def comprar_boletos(peliculas):
    """
    Procesa la compra de boletos.
    Muestra la cartelera, permite seleccionar la película, ingresar la cantidad y aplicar promoción si corresponde.
    :return:    Tuple (película, sala, cantidad boletos, total a pagar) y el total como float.
    """
    print("\n--- Compra de Boletos ---")
    for i, pelicula in enumerate(peliculas):
        print(f"{i + 1}. {pelicula[0]} - Sala: {pelicula[1]} - Horario: {pelicula[2]} - Precio: ${pelicula[3]} - Clasificación: {pelicula[4]}")
    print("---------------------------")
    try:
        seleccion = int(input("Seleccione el número de la película: "))
    except ValueError:
        print("Selección inválida. Cancelando compra de boletos.")
        return "N/A", "N/A", "N/A", 0.0
    if seleccion < 1 or seleccion > len(peliculas):
        print("Selección inválida. Cancelando compra de boletos.")
        return "N/A", "N/A", "N/A", 0.0

    pelicula_seleccionada = peliculas[seleccion - 1]
    nombre_pelicula = pelicula_seleccionada[0]
    sala = pelicula_seleccionada[1]
    horario = pelicula_seleccionada[2]
    try:
        precio_boleto = float(pelicula_seleccionada[3])
    except ValueError:
        precio_boleto = 0.0
    clasificacion = pelicula_seleccionada[4]

    try:
        cantidad_boletos = int(input("Ingrese la cantidad de boletos a comprar: "))
    except ValueError:
        print("Cantidad inválida. Se cancelará la compra de boletos.")
        return "N/A", "N/A", "N/A", 0.0
    promo = input("¿Aplica promoción especial? (s/n): ")
    descuento = 0.10 if promo.lower() == "s" else 0.0
    subtotal = cantidad_boletos * precio_boleto
    total_boletos = subtotal - (subtotal * descuento)

    # Mostrar detalle de la compra
    print("\n--- Detalle de Boletos ---")
    print(f"Película: {nombre_pelicula}")
    print(f"Sala: {sala}")
    print(f"Horario: {horario}")
    print(f"Clasificación: {clasificacion}")
    print(f"Cantidad de boletos: {cantidad_boletos}")
    print(f"Subtotal: ${subtotal}")
    if descuento > 0:
        print(f"Descuento aplicado: -${subtotal * descuento}")
    print(f"Total a pagar: ${total_boletos}")
    print("--------------------------\n")

    return nombre_pelicula, sala, str(cantidad_boletos), total_boletos


def comprar_snacks(snacks):
    """
    Procesa la compra de snacks.
    Muestra la lista de snacks, permite seleccionar uno y la cantidad, y calcula el total.
    :return:    Tuple (detalle snack, total a pagar) y el total como float.
    """
    print("\n--- Compra de Snacks ---")
    for i, snack in enumerate(snacks):
        print(f"{i + 1}. {snack[0]} - Precio: ${snack[1]}")
    try:
        seleccion = int(input("Seleccione el número del snack: "))
    except ValueError:
        print("Selección inválida. Cancelando compra de snacks.")
        return "N/A", 0.0
    if seleccion < 1 or seleccion > len(snacks):
        print("Selección inválida. Cancelando compra de snacks.")
        return "N/A", 0.0

    snack_seleccionado = snacks[seleccion - 1]
    nombre_snack = snack_seleccionado[0]
    try:
        precio_snack = float(snack_seleccionado[1])
    except ValueError:
        precio_snack = 0.0
    try:
        cantidad_snack = int(input("Ingrese la cantidad deseada: "))
    except ValueError:
        print("Cantidad inválida. Cancelando compra de snacks.")
        return "N/A", 0.0
    total_snack = cantidad_snack * precio_snack

    # Mostrar detalle de la compra de snacks
    print("\n--- Detalle de Snacks ---")
    print(f"Snack: {nombre_snack}")
    print(f"Cantidad: {cantidad_snack}")
    print(f"Total a pagar: ${total_snack}")
    print("-------------------------\n")

    detalle = f"{nombre_snack} x {cantidad_snack}"
    return detalle, total_snack

def mostrar_registro_ventas():
    """
    Muestra el registro de todas las ventas realizadas.
    """
    print("\n--- Registro de Ventas ---")
    if not registro_ventas:
        print("No se han realizado ventas.")
    else:
        print(f"{'Película':<20} {'Sala':<10} {'Boletos':<10} {'Snack/Combo':<25} {'Total':<10}")
        for venta in registro_ventas:
            # Cada venta es una lista con 5 elementos
            print(f"{venta[0]:<20} {venta[1]:<10} {venta[2]:<10} {venta[3]:<25} {venta[4]:<10}")
    print("---------------------------\n")

if __name__ == "__main__":
    main()