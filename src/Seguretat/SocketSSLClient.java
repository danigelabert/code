package Seguretat;

import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.IOException;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SocketSSLClient {
    public static SSLSocket cliSocket;

    public SocketSSLClient() {
        try {
            //EXERCICI 8

            System.setProperty("javax.net.ssl.keyStore", "C:\\Users\\Dani Gelabert\\Documents\\DAM\\DAM 2\\SIP\\UF1\\preExamen\\clau\\ServerKeyStore.jks");
            System.setProperty("javax.net.ssl.keyStorePassword", "123456");


            SSLSocketFactory sslFactory =(SSLSocketFactory) SSLSocketFactory.getDefault();
            cliSocket = (SSLSocket) sslFactory.createSocket("localhost", 4043);

        } catch (IOException ex){
            Logger.getLogger(SocketSSLClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }



    public void enviarDades() {
        PrintStream writer = null;
        try {
            Scanner reader = new Scanner(System.in);
            writer = new PrintStream(cliSocket.getOutputStream());
            System.out.println("Deixa una linia en blanc per acabar");
            String text = reader.nextLine();
            while (!text.equals("")){
                writer.println(text);
                writer.flush();
                text = reader.nextLine();
            }
            writer.println("<<FI>>");
            writer.flush();
            cliSocket.close();
        } catch (IOException ex){
            Logger.getLogger(SocketSSLClient.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            writer.close();
        }
    }


    public void ex10 (){
        SSLSocketFactory sslFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
        String[] v = sslFactory.getSupportedCipherSuites();

        for (int i=0; i<v.length; i++){
            System.out.println(v[i]);
        }
    }


    public void ex11(){
        try {
            SocketChannel socketChannel = createChanel();
            sendFile(socketChannel);
        } catch (IOException ex){
            Logger.getLogger(SocketSSLClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private SocketChannel createChanel() throws IOException{
        SocketChannel socketChannel = SocketChannel.open();
        SocketAddress socketAddr = new InetSocketAddress("localhost", 9000);
        socketChannel.connect(socketAddr);
        return socketChannel;
    }

    private void sendFile(SocketChannel socketChannel) throws IOException{
        String[] llines = new String[] {"Dani", "Gelabert", "Pol"};
        ByteBuffer buffer = ByteBuffer.allocate(100);
        for (String line: llines){
            buffer.put(line.getBytes());
            buffer.flip();
            while (buffer.hasRemaining()){
                try {
                    socketChannel.write(buffer);
                } catch (IOException ex){
                    Logger.getLogger(SocketSSLClient.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            buffer.clear();
        }
        socketChannel.close();
    }
}
