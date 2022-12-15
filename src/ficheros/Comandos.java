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
        return help;
    }

    public void cd() {
        if (valores.length == 1) {
            System.out.println(System.getProperty("user.dir"));
        } else if (valores.length == 2) {
            switch (valores[1]) {
                case ".." -> ruta = ruta.getParent();
                case "/" -> rutaAbsoluta();
                default -> System.out.println(nombreDirectorio());
            }
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
                System.out.println("Espacio libre en la ruta: " + ruta.toFile().getFreeSpace());
                System.out.println("Espacio total en la ruta: " + ruta.toFile().getTotalSpace());
                System.out.println("Espacio utilizable en la ruta: " + ruta.toFile().getUsableSpace());
                System.out.println("Ultima modificacion: " + ruta.toFile().lastModified());
                System.out.println("Tamaño: " + ruta.toFile().length());
                System.out.println("Es un directorio: " + ruta.toFile().isDirectory());
                System.out.println("Es un fichero: " + ruta.toFile().isFile());
            }
        }
    }

    public void cat() {
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
    }

    public void top() {
        String nombre = valores[1];
        System.out.println("Introduce el numero de lineas que quieres mostrar: ");
        int lineas = sc.nextInt();
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
    }

    public void mkfile() {
        String nombre = valores[1];
        File f = new File(ruta.resolve(nombre).toString());
        if (f.exists()) {
            System.out.println("Ese fichero ya existe");
            System.out.println("Quieres borrarlo?");
            if (sc.nextLine().equals("Si")) {
                f.delete();
                System.out.println("Fichero borrado");
            }
        } else {
            try {
                if (f.createNewFile()) {
                    System.out.println("Introduce el texto que contendra el fichero");
                    String texto = sc.nextLine();
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
    }

    public void write() {
        String nombre = valores[1];
        String salida = "";
        File f = new File(ruta.resolve(nombre).toString());
        if (!f.exists()) {
            System.out.println("Ese fichero no existe");
        } else {
            try {
                System.out.println("Introduce el texto que contendra el fichero");
                String texto = sc.nextLine();
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
    }

    public void dir() {
        String nombre = valores[1];
        File f = new File(ruta.resolve(nombre).toString());
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

    public void delete(){
        String nombre = valores[1];
        File f = new File(ruta.resolve(nombre).toString());
        if (f.isDirectory()) {
            deleteDirectorio(f);
        } else {
            f.delete();
        }
    }

    public static void deleteDirectorio(File direc) {
        if (direc.exists()) {
            File[] files = direc.listFiles();
            if (files != null) {
                for (File hijo : files) {
                    if (hijo.isDirectory()) {
                        deleteDirectorio(hijo);
                    } else {
                        hijo.delete();
                    }
                }
            }
            direc.delete();
        }
    }

    public void close() {
        System.out.println("Cerrando programa...");
        System.exit(0);
    }

    public void clear() {
        for (int i = 0; i < 50; i++) {
            System.out.println("");
        }
    }
}