package Sockets;

import java.io.IOException;
import java.net.*;

public class socketUDP {
    int portEnviar;
    int portRebre;

    public socketUDP(int portEnviar, int portRebre) {
        this.portEnviar = portEnviar;
        this.portRebre = portRebre;
    }

    public int getPortEnviar() {
        return portEnviar;
    }

    public int getPortRebre() {
        return portRebre;
    }

    public void setPortEnviar(int portEnviar) {
        this.portEnviar = portEnviar;
    }

    public void setPortRebre(int portRebre) {
        this.portRebre = portRebre;
    }

    public static void enviarMissatge(String missatge, String ipDesti){
        try{
            byte[] bytesMissatge = missatge.getBytes();
            InetAddress desti = InetAddress.getByName(ipDesti);
            DatagramPacket packet = new DatagramPacket(bytesMissatge, missatge.length(), desti, 5555);
            DatagramSocket socket = new DatagramSocket();
            socket.send(packet);
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        } catch (SocketException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void rebreMissatge() {
        try {
            int portEscolta = 5555;
            byte[] buffer = new byte[12];
            DatagramSocket socket = new DatagramSocket(portEscolta);

            while (true) {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);
                String data = new String(packet.getData());
                System.out.println("IP del client: " + packet.getSocketAddress());
                System.out.println("Missatge: " + data);
            }
        } catch (SocketException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void rebreMissatgeEx7() {
        try {
            int portEscolta = 5555;
            byte[] buffer = new byte[12];
            DatagramSocket socket = new DatagramSocket(portEscolta);

            while (true) {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);
                String data = new String(packet.getData());
                System.out.println("IP del client: " + packet.getSocketAddress());
                System.out.println("Missatge: " + data);
                buffer = new byte[12];
                if (data.contains("/")) {
                    break;
                }
            }
        } catch (SocketException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void rebreMissatgeEx8() {
        try {
            int portEscolta = 5555;
            byte[] buffer = new byte[12];
            DatagramSocket socket = new DatagramSocket(portEscolta);

            while (true) {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                try {
                    socket.receive(packet);
                    String data = new String(packet.getData());
                    System.out.println("IP del client: " + packet.getSocketAddress());
                    System.out.println("Missatge: " + data);
                    buffer = new byte[12];
                    if (data.contains("/")) {
                        break;
                    }
                }  catch (SocketTimeoutException e) {
                    System.out.println("No s'ha rebut cap missatge els ultims 3 segons'");
                    socket.close();
                    break;
                }
                socket.setSoTimeout(3000);
            }
        } catch (SocketException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void rebreMissatgeEx7S2() {
        try {
            int portEscolta = 5556;
            byte[] buffer = new byte[12];
            DatagramSocket socket = new DatagramSocket(portEscolta);

            while (true) {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);
                String data = new String(packet.getData());
                System.out.println("IP del client: " + packet.getSocketAddress());
                System.out.println("Missatge: " + data);
                if (data.contains("/")) {
                    break;
                }
            }
        } catch (SocketException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void enviarMissatgeEx7(String missatge, String ipDesti){
        try{
            byte[] bytesMissatge = missatge.getBytes();
            InetAddress desti = InetAddress.getByName(ipDesti);
            DatagramPacket packet = new DatagramPacket(bytesMissatge, missatge.length(), desti, 5555);
            DatagramSocket socket = new DatagramSocket();
            socket.send(packet);
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        } catch (SocketException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void multicastServer(){
        try {
            String missatge = "Hola, client multicast!";
            int port = 12345;
            InetAddress grupo = InetAddress.getByName("230.0.0.1");

            MulticastSocket socket = new MulticastSocket();
            socket.joinGroup(grupo);

            DatagramPacket packet = new DatagramPacket(missatge.getBytes(), missatge.length(), grupo, port);
            socket.send(packet);

            System.out.println("Missatge enviat: " + missatge);

            socket.leaveGroup(grupo);
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void multicastClient(){
        try {
            int puerto = 12345;
            InetAddress grupo = InetAddress.getByName("230.0.0.1");

            MulticastSocket socket = new MulticastSocket(puerto);
            socket.joinGroup(grupo);

            byte[] buffer = new byte[1024];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

            socket.receive(packet);
            String mensajeRecibido = new String(packet.getData(), 0, packet.getLength());

            System.out.println("Misstge rebut: " + mensajeRecibido);

            socket.leaveGroup(grupo);
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
