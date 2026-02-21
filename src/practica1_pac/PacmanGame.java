
package practica1_pac;

import java.util.Scanner; //leer lo ingresado por el usuario
import java.util.Random; //coloca premios paredes y fantasmas en lugares aleatorios

public class PacmanGame {
    static Scanner scanner = new Scanner(System.in); 
    static Random random = new Random();

   // Historial
    static String[] historialNombres = new String[100];
    static int[] historialPuntos = new int[100];
    static int historialCount = 0;


    static char[][] tablero;
    static int jugadorFila, jugadorColumna;
    static int vidas, puntaje;
    static int premiosRestantes;
//Menu principal del juego
    public static void main(String[] args) {
        int opcion;

        do {
            System.out.println("PAC-MAN CONSOLA");
            System.out.println("1. Iniciar Juego");
            System.out.println("2. Historial de Partidas");
            System.out.println("3. Salir");
            System.out.print("Seleccione una opcion: ");

            opcion = scanner.nextInt();
            scanner.nextLine();//utilizado para leer la accion ingresada por el ussuario (numeros)
//ejecuta la accion ingresada por el usuario y notifica si la accion no es valida
            switch (opcion) {
                case 1:
                    iniciarJuego();
                    break;
                case 2:
                    verHistorial();
                    break;
                case 3:
                    System.out.println("¡Gracias por jugar!");
                    break;
                default:
                    System.out.println("Opción no valida");
            }
        } while (opcion != 3);

        scanner.close();//Termina el scanner
    }
//Menu del historial 
    static void verHistorial() {
        System.out.println("HISTORIAL DE PARTIDAS");
        if (historialCount == 0) {
            System.out.println("No hay partidas guardadas");
        } else {
            for (int i = historialCount - 1; i >= 0; i--) { //Aqui se muestra el historial del ususario mas reciente al menos reciente
                System.out.println((historialCount - i) + ". " +
                        historialNombres[i] + " - " +
                        historialPuntos[i] + " puntos");// se muestra el nombre y el punteo obtenido del ususario
            }
        }
        System.out.println("Presione ENTER para continuar");
        scanner.nextLine();//Lee la linea completa de la accion
    }
//Inicio del juego
    static void iniciarJuego() {
        System.out.println("NUEVA PARTIDA");

        // Solicitar nombre
        System.out.print("Ingrese su nombre de usuario: ");
        String nombreUsuario = scanner.nextLine();// lee el nombre del ususario para guardarlo

        // Seleccionar tamaño
        char tamanoTablero;
        int filas = 0, columnas = 0;

        do {
            System.out.print("Seleccione tamaño (P=Pequeño 5x6, G=Grande 10x10): ");
            String entrada = scanner.nextLine();
            if (entrada.length() > 0) {
                tamanoTablero = entrada.toUpperCase().charAt(0);//Convierte mayuscula las minusculas para que no halla error

                if (tamanoTablero == 'P') {
                    filas = 5;
                    columnas = 6;
                    break;
                } else if (tamanoTablero == 'G') {
                    filas = 10;
                    columnas = 10;
                    break;
                    //valida que la opcion ingresada sea correcta
                } else {
                    System.out.println("Opción no valida. Use P o G");
                }
            }
        } while (true);//Opcion ingresada correcta

     //Calcula el limite de los elementos
        int totalEspacios = filas * columnas;
        int maxPremios = (int) (totalEspacios * 0.4);
        int maxParedes = (int) (totalEspacios * 0.2);
        int maxTrampas = (int) (totalEspacios * 0.2);

        
        int cantidadPremios = 0, cantidadParedes = 0, cantidadTrampas = 0;

        System.out.println("CONFIGURACION DE ELEMENTOS");
        System.out.println("Maximo premios: " + maxPremios + " (40%)");
        System.out.println("Maximo paredes: " + maxParedes + " (20%)");
        System.out.println("Maximo trampas: " + maxTrampas + " (20%)");

        do {
            System.out.print("Cantidad de premios (1-" + maxPremios + "): ");
            while (!scanner.hasNextInt()) {
                System.out.println("Por favor ingrese un número válido");
                scanner.next();//Eliminar lo que escribio si la opcion no es valida
            }
            cantidadPremios = scanner.nextInt();
            scanner.nextLine();
        } while (cantidadPremios < 1 || cantidadPremios > maxPremios);//Confirma que el numero ingresado no sea negativo y tampoco exceda el limite de premios permitidos

        do {
            System.out.print("Cantidad de paredes (1-" + maxParedes + "): ");
            while (!scanner.hasNextInt()) {
                System.out.println("Por favor ingrese un número válido");
                scanner.next();
            }
            cantidadParedes = scanner.nextInt();
            scanner.nextLine();
        } while (cantidadParedes < 1 || cantidadParedes > maxParedes);

        do {
            System.out.print("Cantidad de trampas (1-" + maxTrampas + "): ");
            while (!scanner.hasNextInt()) {
                System.out.println("Por favor ingrese un número válido");
                scanner.next();
            }
            cantidadTrampas = scanner.nextInt();
            scanner.nextLine();
        } while (cantidadTrampas < 1 || cantidadTrampas > maxTrampas);

  //Crear tablero
        crearTablero(filas, columnas, cantidadPremios, cantidadParedes, cantidadTrampas);

       
        System.out.println("TABLERO INICIAL");
        mostrarTablero();

        // Posicionar jugador
        posicionarJugador(filas, columnas);

 
        vidas = 3;
        puntaje = 0;

        
        System.out.println("INICIANDO PARTIDA");
        mostrarPanel(nombreUsuario);
        mostrarTablero();

        System.out.println("Presione ENTER para comenzar a jugar");
        scanner.nextLine();

       
        boolean partidaTerminada = false;

        while (!partidaTerminada) {
            mostrarPanel(nombreUsuario);
            mostrarTablero();

            System.out.print("Ingrese movimiento: ");
            String entrada = scanner.nextLine();

            
            if (entrada.equalsIgnoreCase("F")) {
                System.out.println("JUEGO PAUSADO");
                System.out.println("1. Regresar al juego");
                System.out.println("2. Terminar partida");
                System.out.print("Seleccione opción: ");

                String opcionPausa = scanner.nextLine();

                if (opcionPausa.equals("2")) {
                    partidaTerminada = true;
                    System.out.println("Partida terminada por el jugador");
                }
                continue;
            }

            
            if (entrada.length() == 1 && "8456".contains(entrada)) {
                procesarMovimiento(entrada.charAt(0));

                if (vidas <= 0) {
                    System.out.println("e quedaste sin vidas.");
                    partidaTerminada = true;
                } else if (premiosRestantes <= 0) {
                    System.out.println("Recogiste todos los premios.");
                    partidaTerminada = true;
                }
            } else {
                System.out.println("Tecla no válida. Use 8,5,4,6 o F");
            }
        }

       
        if (historialCount < historialNombres.length) {
            historialNombres[historialCount] = nombreUsuario;
            historialPuntos[historialCount] = puntaje;
            historialCount++;

            System.out.println("Partida guardada en el historial");
        } else {
            System.out.println("No se pudo guardar: historial lleno");
        }

        System.out.println("Puntaje final: " + puntaje);
        System.out.println("Presione ENTER para volver al menú...");
        scanner.nextLine();
    }
//crear el tablero
    static void crearTablero(int filas, int columnas, int cantPremios,
                            int cantParedes, int cantTrampas) {

        tablero = new char[filas][columnas];// crear la matriz
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                tablero[i][j] = ' ';
            }
        }

 //colocar premios       
        int premiosColocados = 0;
        while (premiosColocados < cantPremios) {
            int f = random.nextInt(filas); //fila aleatoria
            int c = random.nextInt(columnas);// columna aleatoria

            if (tablero[f][c] == ' ') {//verfica que la casilla este vacia
                if (random.nextInt(100) < 70) {// para que hayan mas presmios simples que especiales
                    tablero[f][c] = '0';//premio simple
                } else {
                    tablero[f][c] = '$';//premio especial
                }
                premiosColocados++;
            }
        }

 
        int paredesColocadas = 0;
        while (paredesColocadas < cantParedes) {
            int f = random.nextInt(filas);
            int c = random.nextInt(columnas);

            if (tablero[f][c] == ' ') {
                tablero[f][c] = 'X';// pared
                paredesColocadas++;
            }
        }

       
        int trampasColocadas = 0;
        while (trampasColocadas < cantTrampas) {
            int f = random.nextInt(filas);
            int c = random.nextInt(columnas);

            if (tablero[f][c] == ' ') {
                tablero[f][c] = '@';//fantasma
                trampasColocadas++;
            }
        }

        premiosRestantes = cantPremios;
    }
//mostrar tablero
    static void mostrarTablero() {
        System.out.println();
        
       //borde superior 
        System.out.print("+");
        for (int j = 0; j < tablero[0].length; j++) {
            System.out.print("---");
        }
        System.out.println("+");

  //filas del tablero   
        for (int i = 0; i < tablero.length; i++) {
            System.out.print("|");
            for (int j = 0; j < tablero[0].length; j++) {
                System.out.print(" " + tablero[i][j] + " ");
            }
            System.out.println("|");
        }
        
//borde inferior    
        System.out.print("+");
        for (int j = 0; j < tablero[0].length; j++) {
            System.out.print("---");
        }
        System.out.println("+");
    }
//Posicionar al jugador
    static boolean posicionarJugador(int filas, int columnas) {
        System.out.println("POSICION DE INICIO");
        System.out.println("Las posiciones van de 1 a " + filas + " para filas");
        System.out.println("y de 1 a " + columnas + " para columnas");

        do {
            System.out.print("Ingrese fila (1-" + filas + "): ");//validacion numerica
            while (!scanner.hasNextInt()) {
                System.out.println("Por favor ingrese un número válido");
                scanner.next();
            }
            int filaIngresada = scanner.nextInt();

            System.out.print("Ingrese columna (1-" + columnas + "): ");//validacion numerica
            while (!scanner.hasNextInt()) {
                System.out.println("Por favor ingrese un número válido");
                scanner.next();
            }
            int columnaIngresada = scanner.nextInt();
            scanner.nextLine();

//convertir lo ingresado por el usuario para que el programa lo entienda  
            jugadorFila = filaIngresada - 1;
            jugadorColumna = columnaIngresada - 1;
//validaciones 
            if (jugadorFila < 0 || jugadorFila >= filas ||
                    jugadorColumna < 0 || jugadorColumna >= columnas) {
                System.out.println("Posicion fuera del tablero");
            } else if (tablero[jugadorFila][jugadorColumna] != ' ') {
                System.out.println("Esa casilla no está vacía");
            } else {
                tablero[jugadorFila][jugadorColumna] = '<';
                System.out.println("Pac-Man colocado en fila " + filaIngresada +
                        ", columna " + columnaIngresada);
                return true;
            }
        } while (true);
    }
//procesar los movimientos ingresados por el usuario
    static void procesarMovimiento(char direccion) {
        int nuevaFila = jugadorFila;
        int nuevaColumna = jugadorColumna;
//calcula la posicion segun el comando ingresado por el usuario
        switch (direccion) {
            case '8':
                nuevaFila--;
                break;
            case '5':
                nuevaFila++;
                break;
            case '4':
                nuevaColumna--;
                break; 
            case '6':
                nuevaColumna++;
                break; 
        }

 //bordes infinitos       
        if (nuevaFila < 0)
            nuevaFila = tablero.length - 1;
        if (nuevaFila >= tablero.length)
            nuevaFila = 0;
        if (nuevaColumna < 0)
            nuevaColumna = tablero[0].length - 1;
        if (nuevaColumna >= tablero[0].length)
            nuevaColumna = 0;

 //verifica la pared       
        if (tablero[nuevaFila][nuevaColumna] == 'X') {
            System.out.println("Hay una pared, no puedes moverte");
            return;
        }

 //mueve vacia la posicion anterior   
        tablero[jugadorFila][jugadorColumna] = ' ';

//actualiza la posicion del jugador 
        jugadorFila = nuevaFila;
        jugadorColumna = nuevaColumna;

      
        char casilla = tablero[jugadorFila][jugadorColumna];//procesa lo que hay en la casilla que se mueve el jugador

        switch (casilla) {
            case '0':
                puntaje += 10;
                premiosRestantes--;
                System.out.println("+10 puntos");
                break;
            case '$':
                puntaje += 15;
                premiosRestantes--;
                System.out.println("+15 puntos");
                break;
            case '@':
                vidas--;
                System.out.println("Te atrapo un fantasma Vidas restantes: " + vidas);
                break;
            default:
                System.out.println("Te moviste a una casilla vacia");
                break;
        }

        
        tablero[jugadorFila][jugadorColumna] = '<';//coloca al jugador en la nueva posicion
        
 
        System.out.println("Nueva posición: fila " + (jugadorFila + 1) + 
                          ", columna " + (jugadorColumna + 1));
    }
//mostrar panel en tiempo real de lo que sucede durante la parida
    static void mostrarPanel(String nombreUsuario) {
        System.out.println("PANEL DE CONTROL");
        System.out.println("Jugador: " + nombreUsuario);
        System.out.println("Puntaje: " + puntaje);
        System.out.println("Vidas: " + vidas);
        System.out.println("Premios restantes: " + premiosRestantes);
        System.out.println("Posición actual: fila " + (jugadorFila + 1) +
                ", columna " + (jugadorColumna + 1));
        System.out.println("Controles: 8(Arriba) 5(Abajo) 4(Izq) 6(Der) F(Pausa)");
    }
}
