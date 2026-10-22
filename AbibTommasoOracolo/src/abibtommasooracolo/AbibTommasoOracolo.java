package abibtommasooracolo;

/**
 * classe di avvio del client grafico dell'oracolo.
 */
public class AbibTommasoOracolo {

    /**
     * avvia la finestra principale del client.
     *
     * @param args argomenti passati all'avvio del programma
     */
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            new frmOracolo().setVisible(true);
        });
    }
}