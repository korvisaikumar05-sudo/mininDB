
package client;

import java.net.Socket;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class TCPClient {

    public static void main(String[] args) {

        try {
            Socket socket = new Socket("localhost", 9090);

            BufferedReader input = new BufferedReader(
                new InputStreamReader(socket.getInputStream())
            );

            PrintWriter output = new PrintWriter(
                socket.getOutputStream(), true
            );

            BufferedReader keyboard = new BufferedReader(
                new InputStreamReader(System.in)
            );

            System.out.println("Connected to MiniDB");

            String request;

            while (true) {

                System.out.print("MiniDB> ");
                request = keyboard.readLine();

                if (request == null) {
                    break;
                }

                output.println(request);

                String response = input.readLine();

                if (response == null) {
                    System.out.println("Server disconnected");
                    break;
                }

                System.out.println(
                    response.replace("\\n", System.lineSeparator())
                );

                if (request.equalsIgnoreCase("exit")) {
                    break;
                }
            }

            socket.close();

        } catch (Exception e) {
            System.out.println("Client Error: " + e.getMessage());
        }
    }
}
