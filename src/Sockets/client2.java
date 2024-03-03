package Sockets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class client2 {

    public static void main(String[] args) throws IOException {
//        socketUDP.multicastClient();


        String serverIP = "localhost";
        int serverPort = 12345;

        Socket socket = new Socket(serverIP, serverPort);

        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

        BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));

        String inputLine;
        String outputLine;

        while (true) {
            System.out.print("Tu: ");
            outputLine = userInput.readLine();
            out.println(outputLine);

            if (outputLine.equalsIgnoreCase("fi")) {
                System.out.println("Connexió tancada.");
                break;
            }

            inputLine = in.readLine();
            System.out.println("Servidor diu: " + inputLine);

            if (inputLine.equalsIgnoreCase("fi")) {
                System.out.println("Connexió tancada.");
                break;
            }
        }

        in.close();
        out.close();
        userInput.close();
        socket.close();


    }
}
