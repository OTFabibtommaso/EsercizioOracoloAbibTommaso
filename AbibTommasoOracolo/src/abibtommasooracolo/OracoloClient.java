/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package abibtommasooracolo;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
/**
 *
 * @author abib.tommaso
 */
public class OracoloClient {
    

    private static final String SERVER_IP = "localhost";
    private static final int PORT = 12345;

    public static void main(String[] args) {
        try (
            Socket socket = new Socket(SERVER_IP, PORT);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            Scanner scanner = new Scanner(System.in)
        ) {
            Thread listenerThread = new Thread(() -> {
                try {
                    String rispostaServer;
                    while ((rispostaServer = in.readLine()) != null) {
                        System.out.println(rispostaServer);
                    }
                } catch (IOException e) {
                    System.out.println("Connessione con il server chiusa.");
                }
            });
            listenerThread.start();

            while (scanner.hasNextLine()) {
                String messaggio = scanner.nextLine();
                out.println(messaggio);

                if (messaggio.equalsIgnoreCase("EXIT")) {
                    break;
                }
            }

        } catch (IOException e) {
            System.err.println("Impossibile connettersi al server: " + e.getMessage());
        }
    }
}

