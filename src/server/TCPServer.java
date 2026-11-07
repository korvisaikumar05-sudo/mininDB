package server;

import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer {
    private static int clientCount = 0;

    public static void main(String[] args) {
       
        try {
             
            ServerSocket serverSocket = new ServerSocket(9090);

            System.out.println("MiniDB Server Started");
            System.out.println("Listening on port 9090");

            while (true) {
                Socket clientSocket = serverSocket.accept();

                int clientId = ++clientCount;
                System.out.println("Client " + clientId + " Connected");

                ClientHandler handler = new ClientHandler(clientSocket);

                Thread thread = new Thread(handler);
                thread.start();
            }

        } catch (Exception e) {
            System.out.println("Server Error: " + e.getMessage());
        }
    }
}