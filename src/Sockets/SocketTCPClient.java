package Sockets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketTCPClient {
    public static void main(String[] args) {
        int port = 123456;

        try {
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Client TCP esperant conexions en el puerto " + port);

            // Espera a que un cliente se conecte
            Socket clientSocket = serverSocket.accept();
            System.out.println("Client conectat des de " + clientSocket.getInetAddress());

            BufferedReader entrada = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            String mensajeCliente = entrada.readLine();
            System.out.println("Mensaje del cliente: " + mensajeCliente);

            entrada.close();
            clientSocket.close();
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
