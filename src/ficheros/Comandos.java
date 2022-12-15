package ficheros;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class Comandos {
    public static Path ruta = Paths.get(System.getProperty("user.dir"));
    Scanner sc = new Scanner(System.in);
    static String eleccion = "";
    static String[] valores = eleccion.split(" ");

    public static void main(String[] args) {
        Scanner elec = new Scanner(System.in);
        Comandos comandos = new Comandos();
        System.out.println("Bienvenido a la consola de comandos");
        while (valores[0] != "clear") {
            System.out.print(ruta.toAbsolutePath() + "> ");
            eleccion = elec.nextLine();
            valores = eleccion.split(" ");
            switch (valores[0]) {
                case "help" -> System.out.println(comandos.help());
                case "cd" -> comandos.cd();
                case "mkdir" -> comandos.mkdir();
                case "info" -> comandos.info();
                case "cat" -> comandos.cat();
                case "top" -> comandos.top();
                case "mkfile" -> comandos.mkfile();
                case "write" -> comandos.write();
                case "dir" -> comandos.dir();
                case "delete" -> comandos.delete();
                case "close" -> comandos.close();
                case "clear" -> comandos.clear();
                default -> System.out.println("Comando no reconocido");
            }
        }
    }

    public Comandos() {
    }

    public String help() {
        String help;
        help = "Comandos disponibles: \n";
        help += "help -> Lista los comandos con una breve definicion de lo que hacen \n";
        help += "cd -> Muestra el directorio actual\n";
        help += "\t[..] -> Accede al directorio padre\n";
        help += "\t[<nombre_directorio>] -> Accede a un directorio dentro del directorio actual\n";
        help += "\t[<ruta_absoluta>] -> Accede a la ruta absoluta del sistema\n";
        help += "mkdir <nombre_directorio> -> Crea un directorio en la ruta actual\n";
        help += "info <nombre> -> Muestra informacion del elemento\n";
        help += "cat <nombre_fichero> -> Muestra el contenido de un fichero\n";
        help += "top <numero_lineas> <nombre_fichero> -> Muestra las lineas especificadas de un fichero\n";
        help += "mkfile <nombre_fichero> <texto> -> Crea un fichero con ese nombre y el contenido de texto\n";
        help += "write <nombre_fichero> <texto> -> Añade 'texto' al final del fichero especificado\n";
        help += "dir -> Lista los archivos o directorios de la ruta actual\n";
        help += "\t[<nombre_directorio>] -> Lista los archivos o directorios dentro de ese directorio\n";
        help += "\t[<ruta_absoluta>] -> Lista los archivos o directorios dentro de esa ruta\n";
        help += "delete <nombre> -> Borra el fichero, si es un directorio borra todo su contenido y a si mismo\n";
        help += "close -> Cierra el programa\n";
        help += "clear -> Vacia la vista\n";
        if (valores.length == 1) {
            return help;
        } else {
            return "Demasiados argumentos";
        }
    }

    public void cd() {
        if (valores.length == 1) {
            System.out.println(ruta);
        } else if (valores.length == 2) {
            switch (valores[1]) {
                case ".." -> ruta = ruta.getParent();
                case "/" -> rutaAbsoluta();
                default -> System.out.println(nombreDirectorio());
            }
        } else if (valores.length >= 3) {
            System.out.println("Demasiados argumentos");
        }
    }

    public String nombreDirectorio() {
        String nuevo = valores[1];
        File d = new File(ruta.resolve(nuevo).toString());
        if (!d.exists()) {
            nuevo = "Este directorio no existe";
            return nuevo;
        } else {
            nuevo = d.getPath();
            ruta = Paths.get(nuevo);
            return nuevo;
        }
    }

    public void rutaAbsoluta() {
        Path rutaAbsoluta = Paths.get(valores[1]);
        File d = rutaAbsoluta.toFile();
        if (d.exists()) {
            ruta = Paths.get(d.toURI());
        } else {
            System.out.println("El directorio especificado no existe");
        }
    }

    public void mkdir() {
        if (valores.length == 2) {
            String nuevo = valores[1];
            File d = new File(ruta.resolve(nuevo).toString());
            if (!d.exists()) {
                if (d.mkdir()) {
                    System.out.println("Directorio creado");
                } else {
                    System.out.println("No se ha podido crear el directorio");
                }
            } else if (d.exists()) {
                System.out.println("El directorio ya existe");
                System.out.println("Quieres borrarlo?");
                if (sc.nextLine().equalsIgnoreCase("si")) {
                    deleteDirectorio(d);
                    System.out.println("Fichero borrado");
                }

            }
        } else if (valores.length == 1) {
            System.out.println("Falta el nombre del directorio");
        } else if (valores.length >= 3) {
            System.out.println("Demasiados argumentos");
        }
    }

    public void info() {
        if (valores.length == 1) {
            System.out.println("No has introducido ningun nombre");
        } else {
            String nuevo = valores[1];
            File d = new File(ruta.resolve(nuevo).toString());
            Path ruta = Paths.get(d.getPath());

            if (!d.exists()) {
                System.out.println("El fichero o directorio no existe");
            } else {
                System.out.println("Informacion del elemento:");
                System.out.println("Nombre: " + ruta.getFileName());
                System.out.println("Sistema creador de la ruta: " + ruta.getFileSystem());
                System.out.println("Ruta padre: " + ruta.getParent());
                System.out.println("Ruta absoluta: " + ruta.toAbsolutePath());
                System.out.println("Raiz de la ruta: " + ruta.getRoot());
                System.out.println("Numero de elementos de la ruta: " + ruta.getNameCount());
                System.out.println("Espacio libre en la ruta: " + ruta.toFile().getFreeSpace() + " bytes");
                System.out.println("Espacio total en la ruta: " + ruta.toFile().getTotalSpace() + " bytes");
                System.out.println("Espacio utilizable en la ruta: " + ruta.toFile().getUsableSpace() + " bytes");
                System.out.println("Tamaño: " + ruta.toFile().length());
                System.out.println("Es un directorio: " + ruta.toFile().isDirectory());
                System.out.println("Es un fichero: " + ruta.toFile().isFile());
            }
        }
    }

    public void cat() {
        if (valores.length == 2) {
            String nombre = valores[1];
            File f = new File(ruta.resolve(nombre).toString());
            if (!f.exists()) {
                System.out.println("Ese fichero no existe");
            } else {
                try {
                    FileReader fileLectura = new FileReader(f);
                    BufferedReader bufferLectura = new BufferedReader(fileLectura);
                    System.out.println("Leyendo fichero...");
                    int i;
                    while ((i = bufferLectura.read()) != -1) {
                        System.out.print((char) i);
                    }
                    bufferLectura.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else if (valores.length == 1) {
            System.out.println("Falta el nombre del fichero");
        } else if (valores.length >= 3) {
            System.out.println("Demasiados argumentos");
        }
    }

    public void top() {
        if (valores.length == 3) {
            String nombre = valores[2];
            int lineas = Integer.parseInt(valores[1]);
            File f = new File(ruta.resolve(nombre).toString());
            if (!f.exists()) {
                System.out.println("Ese fichero no existe");
            } else {
                try {
                    FileReader fileLectura = new FileReader(f);
                    BufferedReader bufferLectura = new BufferedReader(fileLectura);
                    System.out.println("Leyendo fichero...");
                    int i;
                    int contador = 1;
                    while ((i = bufferLectura.read()) != -1) {
                        if (contador <= lineas) {
                            System.out.print((char) i);
                            if ((char) i == "\n".charAt(0)) {
                                contador++;
                            }
                        }
                    }
                    bufferLectura.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else if (valores.length == 1) {
            System.out.println("Falta el nombre del fichero");
        } else if (valores.length == 2) {
            System.out.println("Falta el numero de lineas");
        } else if (valores.length >= 4) {
            System.out.println("Demasiados argumentos");
        }
    }

    public void mkfile() {
        if (valores.length == 3) {
            String nombre = valores[1];
            File f = new File(ruta.resolve(nombre).toString());
            if (f.exists()) {
                System.out.println("Ese fichero ya existe");
                System.out.println("Quieres borrarlo?");
                if (sc.nextLine().equalsIgnoreCase("si")) {
                    f.delete();
                    System.out.println("Fichero borrado");
                }
            } else {
                try {
                    if (f.createNewFile()) {
                        String texto = valores[2];
                        FileWriter file = new FileWriter(f);
                        BufferedWriter buffer = new BufferedWriter(file);
                        buffer.write(texto);
                        buffer.flush();
                        buffer.close();
                        System.out.println("Fichero creado");
                    } else {
                        System.out.println("No se ha podido crear el archivo");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else if (valores.length == 1) {
            System.out.println("Falta el nombre del fichero");
        } else if (valores.length == 2) {
            System.out.println("Falta el texto del fichero");
        } else if (valores.length >= 4) {
            System.out.println("Demasiados argumentos");
        }
    }

    public void write() {
        if (valores.length == 3) {
            String nombre = valores[1];
            String salida = "";
            File f = new File(ruta.resolve(nombre).toString());
            if (!f.exists()) {
                System.out.println("Ese fichero no existe");
            } else {
                try {
                    String texto = valores[2];
                    FileReader fileLectura = new FileReader(f);
                    BufferedReader bufferLectura = new BufferedReader(fileLectura);
                    System.out.println("Leyendo fichero...");
                    int i = 0;
                    while ((i = bufferLectura.read()) != -1) {
                        char c = (char) i;
                        salida += c + "";
                    }
                    FileWriter file = new FileWriter(f);
                    BufferedWriter buffer = new BufferedWriter(file);
                    buffer.write(salida + " " + texto);
                    buffer.flush();
                    buffer.close();
                    System.out.println("Fichero escrito");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else if (valores.length == 1) {
            System.out.println("Falta el nombre del fichero");
        } else if (valores.length == 2) {
            System.out.println("Falta el texto que quieres escribir");
        } else if (valores.length >= 4) {
            System.out.println("Demasiados argumentos");
        }
    }

    public void dir() {
        if (valores.length == 1) {
            File f = new File(ruta.toString());
            dirAccion(f);
        } else if (valores.length == 2) {
            switch (valores[1]) {
                case "/" -> {
                    Path rutaAbsoluta = Paths.get(valores[1]);
                    File f = rutaAbsoluta.toFile();
                    dirAccion(f);
                }
                default -> {
                    String nombre = valores[1];
                    File f = new File(ruta.resolve(nombre).toString());
                    dirAccion(f);
                }
            }
        }
        else if (valores.length >= 3) {
            System.out.println("Demasiados argumentos");
        }
    }

    private void dirAccion(File f) {
        File[] lista = f.listFiles();
        if (lista != null) {
            System.out.println("Contenido de la ruta " + ruta + ":");
            for (File archivo : lista) {
                System.out.println("Nombre: " + archivo.getName());
                System.out.println("Tamaño: " + archivo.length() + " bytes");
                System.out.println("Es un directorio: " + archivo.isDirectory());
                System.out.println("-------------------------");
            }
        } else {
            System.out.println("La ruta no existe o no es un directorio");
        }
    }

    public void delete() {
        if (valores.length == 2) {
            String nombre = valores[1];
            File f = new File(ruta.resolve(nombre).toString());
            if (f.exists()) {
                if (f.isDirectory()) {
                    deleteDirectorio(f);
                } else {
                    f.delete();
                }
            } else {
                System.out.println("Ese fichero no existe");
            }
        } else if (valores.length == 1) {
            System.out.println("Falta el nombre del fichero");
        } else if (valores.length >= 3) {
            System.out.println("Demasiados argumentos");
        }
    }

    public static void deleteDirectorio(File direct) {
        if (direct.exists()) {
            File[] files = direct.listFiles();
            if (files != null) {
                for (File hijo : files) {
                    if (hijo.isDirectory()) {
                        deleteDirectorio(hijo);
                    } else {
                        hijo.delete();
                    }
                }
            }
            direct.delete();
        }
    }

    public void close() {
        if (valores.length == 1) {
            System.out.println("Cerrando programa...");
            System.exit(0);
        } else if (valores.length >= 2) {
            System.out.println("Demasiados argumentos");
        }
    }

    public void clear() {
        if (valores.length == 1) {
            for (int i = 0; i < 50; i++) {
                System.out.println("");
            }
        } else if (valores.length >= 2) {
            System.out.println("Demasiados argumentos");
        }
    }
}