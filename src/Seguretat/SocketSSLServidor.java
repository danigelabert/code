package Seguretat;

import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SocketSSLServidor {

    //Crear clau    keytool -genkey -alias keyStore -keyalg RSA -keystore ServerKeyStore.jks -keysize 2048


    public static SSLServerSocket srvSocket;
    //EXERCICI 4
    public  SocketSSLServidor() {
        System.setProperty("javax.net.ssl.keyStore", "C:\\Users\\Dani Gelabert\\Documents\\DAM\\DAM 2\\SIP\\UF1\\preExamen\\clau\\ServerKeyStore.jks");
        System.setProperty("javax.net.ssl.keyStorePassword", "123456");

        SSLServerSocketFactory sslFactory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();

        try {

            srvSocket = (SSLServerSocket) sslFactory.createServerSocket(4043);

        } catch (IOException ex){
            Logger.getLogger(SocketSSLServidor.class.getName()).log(Level.SEVERE, null, ex);
        }

    }


    //EXERCICI 5
    public void rebreDades() {
        int numClient = 1;
        while (true) {
            try {
                SSLSocket cliSocket = (SSLSocket) srvSocket.accept();
                Scanner reader = new Scanner(cliSocket.getInputStream());
                String text = reader.nextLine();
                while (!text.equals("<<FI>>")) {
                    System.out.println("[Client "+numClient+"] "+text);
                    System.out.flush();
                    text = reader.nextLine();
                }
                System.out.println("[Client "+numClient+"] Tancant connexió...");
                cliSocket.close();
                numClient++;
            } catch (IOException ex){
                Logger.getLogger(SocketSSLServidor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
