package Serveis;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import java.io.*;
import java.lang.management.*;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Main {
    public static void main(String[] args) {
        FTPClient ftpClient = new FTPClient();
        Logger logger = Logger.getLogger("MyLog");
        Level info = Level.INFO;
        Level severe = Level.SEVERE;



        if (connectarFtp(ftpClient))
            System.out.println("Ha retornat true");
        else
            System.out.println("Ha retornat false");
//
//        listFilesOnServer(ftpClient);
//
//        System.out.println("Contingut htdocs");
//        listHtdocsContent(ftpClient);

//        readProvaTxtContent(ftpClient);

//        uploadTextFileToHtdocs(ftpClient);

//        createAndLogInfo("Primer registro de nivel INFO");

//        createLog();
//        guardarLog(logger, info, "Aqui un error info");
//        guardarLog(logger, severe, "Aqui un error severe");

        displaySystemInfo();
//
        if (desconnectarFtp(ftpClient))
            System.out.println("Ha retornat true");
        else
            System.out.println("Ha retornat false");
    }

    //EXERCICI 6
    public static boolean connectarFtp(FTPClient ftpClient){

        System.out.println("\n\nExercici 6:");
        boolean retVal = false;

        String server = "ftpupload.net";
        int port = 21;
        String user = "if0_35838292";
        String pass = "hpMsi04";


        try {
            ftpClient.connect(server, port);
            ;

            if (ftpClient.login(user, pass)){
                System.out.println("Conectado al servidor FTP.");
                retVal = true;
            }
            else
                retVal = false;
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return retVal;
    }

    public static boolean desconnectarFtp(FTPClient ftpClient){
        boolean retVal = false;
        try {
            if (ftpClient.logout()) {
                System.out.println("Desconnectat del servidor FTP");
                retVal = true;
            }
            else
                retVal = false;
            ftpClient.disconnect();

        } catch (IOException e){
            e.printStackTrace();
        }
        return retVal;
    }




    //EXERCICI 7
    public static void listFilesOnServer(FTPClient ftpClient) {
        System.out.println("\n\nExercici 7:");

        try {

            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

            FTPFile[] files = ftpClient.listFiles();

            System.out.println("Lista de archivos y carpetas en el servidor FTP:");

            for (FTPFile file : files) {
                String details = file.isDirectory() ? "Directorio" : "Archivo";
                details += " - " + file.getName();
                System.out.println(details);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //EXERCICI 8
    public static void listHtdocsContent(FTPClient ftpClient) {
        System.out.println("\n\nExercici 8:");

        try {


            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

            String htdocsDirectory = "htdocs";
            if (ftpClient.changeWorkingDirectory(htdocsDirectory)) {
                FTPFile[] files = ftpClient.listFiles();

                System.out.println("Contenido de la carpeta 'htdocs' en el servidor FTP:");

                for (FTPFile file : files) {
                    String details = file.isDirectory() ? "Directorio" : "Archivo";
                    details += " - " + file.getName();
                    System.out.println(details);
                }
            } else {
                System.out.println("No se pudo cambiar al directorio 'htdocs'.");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //EXERCICI 9
    public static void readProvaTxtContent(FTPClient ftpClient) {
        System.out.println("\n\nExercici 9:");

        try {
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

            String htdocsDirectory = "htdocs";
            if (ftpClient.changeWorkingDirectory(htdocsDirectory)) {
                String fileName = "prova.txt";

                InputStream inputStream = ftpClient.retrieveFileStream(fileName);

                if (inputStream != null) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    String line;

                    System.out.println("Contenido de 'prova.txt':");

                    while ((line = reader.readLine()) != null) {
                        System.out.println(line);
                    }

                    inputStream.close();
                    ftpClient.completePendingCommand();
                } else {
                    System.out.println("No se pudo descargar el archivo 'prova.txt'.");
                }
            } else {
                System.out.println("No se pudo cambiar al directorio 'htdocs'.");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    //EXERCICI 10
    public static void uploadTextFileToHtdocs(FTPClient ftpClient) {

        System.out.println("\n\nExercici 10:");
        String localFilePath = "C:\\Users\\Dani Gelabert\\Documents\\prova2.txt";
        try {

            System.out.println("Conectado al servidor FTP.");

            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

            String htdocsDirectory = "htdocs";
            if (ftpClient.changeWorkingDirectory(htdocsDirectory)) {
                String fileName = new File(localFilePath).getName();

                FileInputStream localFileStream = new FileInputStream(localFilePath);

                if (ftpClient.storeFile(fileName, localFileStream)) {
                    System.out.println("Archivo subido correctamente a 'htdocs'.");
                } else {
                    System.out.println("No se pudo subir el archivo a 'htdocs'.");
                }

                localFileStream.close();
            } else {
                System.out.println("No se pudo cambiar al directorio 'htdocs'.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    public static void createAndLogInfo(String message) {
//        // Configuración del sistema de logs (Logback en este caso)
//        System.setProperty("logback.configurationFile", "C:\\Users\\Dani Gelabert\\Documents\\DAM\\DAM 2\\SIP\\UF1\\code\\practica_serveis\\logback.xml"); // Reemplaza con la ruta adecuada
//
//        // Crear un logger
//        Logger logger = LoggerFactory.getLogger(LogExample.class);
//
//        // Log del mensaje de nivel INFO
//        logger.info(message);
//    }

    public static void displaySystemInfo() {
        // Obtener información sobre la memoria
        MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
        MemoryUsage heapMemoryUsage = memoryMXBean.getHeapMemoryUsage();

        // Mostrar información sobre la memoria utilizada
        System.out.println("Memoria utilizada:");
        System.out.println("  Memoria total: " + formatBytes(heapMemoryUsage.getMax()));
        System.out.println("  Memoria usada: " + formatBytes(heapMemoryUsage.getUsed()));
        System.out.println();

        // Obtener información sobre los hilos
        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();

        // Mostrar el número de hilos en ejecución
        System.out.println("Número de hilos en ejecución: " + threadMXBean.getThreadCount());
        System.out.println();

        // Mostrar información sobre cada hilo
        System.out.println("Detalles de los hilos:");

        long[] threadIds = threadMXBean.getAllThreadIds();
        ThreadInfo[] threadInfos = threadMXBean.getThreadInfo(threadIds);

        for (ThreadInfo threadInfo : threadInfos) {
            System.out.println("ID: " + threadInfo.getThreadId());
            System.out.println("Nombre: " + threadInfo.getThreadName());
            System.out.println("Estado: " + threadInfo.getThreadState());
            System.out.println("Prioridad: " + threadInfo.getPriority());
            System.out.println("--------------------------------------------------");
        }
    }

    private static String formatBytes(long bytes) {
        // Función para formatear bytes en una forma legible
        long kilobytes = bytes / 1024;
        long megabytes = kilobytes / 1024;
        return megabytes + " MB";
    }

    public static void createLog(){
        System.out.println("\n\nExercici 11:");
        Logger logger = Logger.getLogger("MyLog");
        FileHandler fh;

        try {
            fh = new FileHandler("C:\\Users\\Dani Gelabert\\Documents\\DAM\\DAM 2\\SIP\\UF1\\preExamen\\code\\log.txt");
            logger.addHandler(fh);
            SimpleFormatter formatter=new SimpleFormatter();
            fh.setFormatter(formatter);

            logger.info("Mi primer log");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public static void guardarLog(Logger log, Level lvl, String msg){
        System.out.println("\n\nExercici 12:");
        Logger logger = Logger.getLogger("MyLog");
        FileHandler fh;

        try {
            fh = new FileHandler("C:\\Users\\Dani Gelabert\\Documents\\DAM\\DAM 2\\SIP\\UF1\\preExamen\\code\\log.txt", true);
            logger.addHandler(fh);
            SimpleFormatter formatter=new SimpleFormatter();
            fh.setFormatter(formatter);

            switch (lvl.getName()){
                case "INFO":
                    log.info(msg);
                    break;
                case "SEVERE":
                    log.severe(msg);
                    break;
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
