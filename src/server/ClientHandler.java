
package server;

import java.net.Socket;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import executor.QueryExecutor;
import parser.Query;
import parser.SQLParser;
import parser.Tokenizer;

public class ClientHandler implements Runnable {

    private Socket socket;
    private QueryExecutor executor = new QueryExecutor();

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try {
            BufferedReader input = new BufferedReader(
                new InputStreamReader(socket.getInputStream())
            );

            PrintWriter output = new PrintWriter(
                socket.getOutputStream(), true
            );

            String request;

            while ((request = input.readLine()) != null) {

                if (request.equalsIgnoreCase("exit")) {
                    output.println("Connection closed");
                    break;
                }

                try {
                    SQLParser parser = new SQLParser(
                        Tokenizer.tokenize(request)
                    );

                    Query query = parser.parse();

                    String result = executor.execute(query);

                    output.println(
                        result.replace("\r", "").replace("\n", "\\n")
                    );

                } catch (Exception e) {
                    output.println("Error: " + e.getMessage());
                }
            }

            socket.close();

        } catch (Exception e) {
            System.out.println("Client Error: " + e.getMessage());
        }
    }
}
