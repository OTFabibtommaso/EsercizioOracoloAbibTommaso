package abibtommasoserveroracolo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

/**
 * gestisce il server dell'oracolo e le connessioni dei client.
 * ogni client viene gestito da un thread indipendente.
 */
public class AbibTommasoServerOracolo {

    private static final int PORT = 5555;

    /**
     * risposte della modalità selciarini.
     */
    private static final String[] RISPOSTE_SELCIARINI = {
        "se cocco",
        "no mi cocco",
        "c'è caso ma nte saprei di",
        "to cocco dormi",
        "lascia sta è meglio si ve a magna al coco pizza"
    };

    /**
     * risposte della modalità normale.
     */
    private static final String[] RISPOSTE_NORMALI = {
        "Sì, certamente.",
        "No, assolutamente no.",
        "Forse...",
        "Le stelle sono incerte, riprova.",
        "Molto probabile.",
        "Ho dei forti dubbi."
    };

    /**
     * risposte della modalità maranza.
     */
    private static final String[] RISPOSTE_MARANZA = {
        "fra si wallhai billhai!",
        "no frere fais belek",
        "normale",
        "npt"
    };

    /**
     * avvia il server e attende le connessioni dei client.
     *
     * @param args argomenti passati all'avvio del programma
     */
    public static void main(String[] args) {
        System.out.println(
            "Server Oracolo avviato sulla porta " + PORT + "..."
        );

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {

            while (true) {
                Socket clientSocket = serverSocket.accept();

                System.out.println(
                    "Nuovo client connesso: "
                    + clientSocket.getInetAddress()
                );

                Thread threadClient = new Thread(
                    () -> gestisciClient(clientSocket)
                );

                threadClient.start();
            }

        } catch (IOException e) {
            System.err.println(
                "Errore nel server: " + e.getMessage()
            );
        }
    }

    /**
     * gestisce la comunicazione con un singolo client.
     * riceve nickname e modalità prima di elaborare le domande.
     *
     * @param socket socket del client collegato
     */
    private static void gestisciClient(Socket socket) {
        try (
            Socket client = socket;
            BufferedReader reader = new BufferedReader(
                new InputStreamReader(client.getInputStream())
            );
            PrintWriter writer = new PrintWriter(
                client.getOutputStream(), true
            )
        ) {
            writer.println("== BENVENUTO NELL'ORACOLO SUPREMO ==");
            writer.println("Scrivi EXIT in qualsiasi momento per uscire.");
            writer.println("Inserisci il tuo NICKNAME per iniziare:");

            String nickname = reader.readLine();

            if (nickname == null || nickname.trim().isEmpty()) {
                nickname = "Anonimo";
            }

            writer.println(
                "Ciao " + nickname + "! Hai scelto la modalità:"
            );

            String sceltaDifficolta = reader.readLine();
            int difficolta = 2;

            try {
                if (sceltaDifficolta != null) {
                    int scelta = Integer.parseInt(
                        sceltaDifficolta.trim()
                    );

                    if (scelta >= 1 && scelta <= 3) {
                        difficolta = scelta;
                    } else {
                        writer.println(
                            "Scelta non valida. Verrà usata la modalità Normale."
                        );
                    }
                }
            } catch (NumberFormatException e) {
                writer.println(
                    "Scelta non valida. Verrà usata la modalità Normale."
                );
            }

            String nomeModalita;

            switch (difficolta) {
                case 1:
                    nomeModalita = "Selciarini";
                    break;
                case 3:
                    nomeModalita = "Maranza";
                    break;
                default:
                    nomeModalita = "Normale";
                    break;
            }

            writer.println("Modalità " + nomeModalita + " impostata.");
            writer.println(
                "L'Oracolo è pronto! Fai la tua prima domanda:"
            );

            Random random = new Random();
            String domanda;

            while ((domanda = reader.readLine()) != null) {

                if (domanda.equalsIgnoreCase("EXIT")) {
                    writer.println(
                        "Arrivederci " + nickname
                        + "! Che il destino ti sia favorevole."
                    );
                    break;
                }

                if (domanda.trim().isEmpty()) {
                    writer.println(
                        "L'Oracolo non risponde al silenzio... Fai una domanda!"
                    );
                    continue;
                }

                String[] mazzoRisposte;

                switch (difficolta) {
                    case 1:
                        mazzoRisposte = RISPOSTE_SELCIARINI;
                        break;
                    case 3:
                        mazzoRisposte = RISPOSTE_MARANZA;
                        break;
                    default:
                        mazzoRisposte = RISPOSTE_NORMALI;
                        break;
                }

                String risposta = mazzoRisposte[
                    random.nextInt(mazzoRisposte.length)
                ];

                writer.println(
                    "🔮 L'Oracolo scuote la palla magica... " + risposta
                );

                writer.println(
                    "Fai un'altra domanda (oppure scrivi EXIT)."
                );
            }

        } catch (IOException e) {
            System.out.println(
                "Un client si è disconnesso."
            );
        }
    }
}