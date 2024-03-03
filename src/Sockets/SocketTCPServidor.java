package Sockets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketTCPServidor {
    public static void main(String[] args) {
        int port = 12345;

        try {
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Servidor TCP esperant conexions en el port " + port);

            Socket clientSocket = serverSocket.accept();
            System.out.println("Client connectat des de " + clientSocket.getInetAddress());

            BufferedReader entrada = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter sortida = new PrintWriter(clientSocket.getOutputStream(), true);

            String missatgeClient = entrada.readLine();
            System.out.println("Missatge del client: " + missatgeClient);

            // Enviar una respuesta al cliente
            String respostaServidor = "Missatge rebut pel servidor: " + missatgeClient;
            sortida.println(respostaServidor);

            entrada.close();
            sortida.close();
            clientSocket.close();
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
