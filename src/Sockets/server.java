package Sockets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class server {

    public static void main(String[] args) throws IOException {
//        socketUDP.multicastServer();
        ServerSocket serverSocket = new ServerSocket(12345);

        System.out.println("Servidor a l'escolta al port 12345...");

        Socket clientSocket = serverSocket.accept();
        System.out.println("Client connectat des de: " + clientSocket.getInetAddress());

        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

        BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));

        String inputLine;
        String outputLine;

        while (true) {
            inputLine = in.readLine();
            if (inputLine.equalsIgnoreCase("fi")) {
                System.out.println("Connexió tancada.");
                break;
            }
            System.out.println("Client diu: " + inputLine);

            System.out.print("Tu: ");
            outputLine = userInput.readLine();
            out.println(outputLine);

            if (outputLine.equalsIgnoreCase("fi")) {
                System.out.println("Connexió tancada.");
                break;
            }
        }

        in.close();
        out.close();
        userInput.close();
        clientSocket.close();
        serverSocket.close();
    }
}
