/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package abibtommasooracolo;

/**
 *
 * @author abib.tommaso
 */
public class frmOracolo extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(frmOracolo.class.getName());

    /**
     * Creates new form frmOracolo
     */
    // indirizzo e porta del server
    private static final String SERVER_IP = "localhost";
    private static final int PORT = 5555;

    // oggetti per la comunicazione con il server
    private java.net.Socket socket;
    private java.io.BufferedReader in;
    private java.io.PrintWriter out;

    // indica se il nickname e la modalità sono già stati inviati
    private boolean inizializzato = false;

    // impedisce di chiudere più volte la connessione
    private boolean chiusuraRichiesta = false;

    /**
     * apre la connessione con il server e avvia la ricezione dei messaggi.
     */
    private void connettiAlServer() {
        try {
            socket = new java.net.Socket(SERVER_IP, PORT);

            in = new java.io.BufferedReader(
                new java.io.InputStreamReader(socket.getInputStream())
            );

            out = new java.io.PrintWriter(
                socket.getOutputStream(), true
            );

            txt_Output.append("connessione al server riuscita.\n");

            Thread threadRicezione = new Thread(() -> {
                try {
                    String messaggio;

                    while ((messaggio = in.readLine()) != null) {
                        final String risposta = messaggio;

                        javax.swing.SwingUtilities.invokeLater(() -> {
                            txt_Output.append(risposta + "\n");
                            txt_Output.setCaretPosition(
                                txt_Output.getDocument().getLength()
                            );

                            // chiude la finestra dopo il messaggio finale
                            if (risposta.startsWith("Arrivederci ")) {
                                chiusuraRichiesta = true;
                                chiudiConnessione();
                                dispose();
                            }
                        });
                    }

                } catch (java.io.IOException e) {
                    javax.swing.SwingUtilities.invokeLater(() -> {
                        if (!chiusuraRichiesta) {
                            txt_Output.append(
                                "connessione con il server terminata.\n"
                            );
                        }
                    });
                }
            });

            threadRicezione.setDaemon(true);
            threadRicezione.start();

        } catch (java.io.IOException e) {
            txt_Output.append(
                "impossibile connettersi al server: "
                + e.getMessage() + "\n"
            );

            btn_Invia.setEnabled(false);
        }
    }

    /**
     * invia il nickname e la modalità al primo utilizzo.
     * successivamente invia le domande al server.
     */
    private void inviaMessaggio() {
        if (out == null || socket == null || socket.isClosed()) {
            txt_Output.append("server non connesso.\n");
            return;
        }

        String messaggio = txt_Input.getText().trim();

        if (messaggio.isEmpty()) {
            return;
        }

        if (!inizializzato) {
            // il primo invio contiene il nickname
            out.println(messaggio);

            // invia il numero della modalità selezionata
            int modalita = cmb_Personalita.getSelectedIndex() + 1;
            out.println(modalita);

            inizializzato = true;

            cmb_Personalita.setEnabled(false);
            txt_Input.setText("");
            txt_Input.setToolTipText("scrivi la tua domanda...");
            txt_Input.requestFocus();

            txt_Output.append("nickname inviato: " + messaggio + "\n");
            txt_Output.append("modalità selezionata: " + modalita + "\n");

            return;
        }

        // mostra la domanda nella cronologia
        txt_Output.append("tu: " + messaggio + "\n");

        // invia la domanda al server
        out.println(messaggio);

        txt_Input.setText("");

        if (messaggio.equalsIgnoreCase("EXIT")) {
            chiusuraRichiesta = true;
            btn_Invia.setEnabled(false);
            txt_Input.setEnabled(false);
        }
    }

    /**
     * chiude il socket quando la finestra viene chiusa.
     */
    private void chiudiConnessione() {
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        } catch (java.io.IOException e) {
            logger.warning("errore durante la chiusura della connessione.");
        }
    }
    /**
    * crea la finestra e inizializza i componenti grafici
    * e la connessione con il server.
    */
    public frmOracolo() {
        initComponents();

        setTitle("oracolo supremo");
        setLocationRelativeTo(null);

        cmb_Personalita.setModel(
            new javax.swing.DefaultComboBoxModel<>(
                new String[] {
                    "selciarini",
                    "normale",
                    "maranza"
                }
            )
        );

        cmb_Personalita.setSelectedIndex(1);

        txt_Output.setEditable(false);
        txt_Output.setText("");

        txt_Input.setText("");
        txt_Input.setToolTipText("inserisci il nickname");

        btn_Invia.setText("invia");
        btn_Help.setText("?");

        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent evt) {
                chiusuraRichiesta = true;
                chiudiConnessione();
            }
        });

        connettiAlServer();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btn_Invia = new javax.swing.JButton();
        txt_Input = new javax.swing.JTextField();
        cmb_Personalita = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        txt_Output = new javax.swing.JTextArea();
        btn_Help = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        btn_Invia.setText("jButton1");
        btn_Invia.addActionListener(this::btn_InviaActionPerformed);

        txt_Input.setText("jTextField1");
        txt_Input.addActionListener(this::txt_InputActionPerformed);

        cmb_Personalita.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmb_Personalita.addActionListener(this::cmb_PersonalitaActionPerformed);

        txt_Output.setColumns(20);
        txt_Output.setRows(5);
        jScrollPane1.setViewportView(txt_Output);

        btn_Help.setText("jButton2");
        btn_Help.addActionListener(this::btn_HelpActionPerformed);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(47, 47, 47)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 264, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 59, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btn_Invia, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(btn_Help, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(cmb_Personalita, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txt_Input, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(58, 58, 58))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(cmb_Personalita, javax.swing.GroupLayout.DEFAULT_SIZE, 46, Short.MAX_VALUE)
                    .addComponent(txt_Input))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 142, Short.MAX_VALUE)
                        .addComponent(btn_Help)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_Invia, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(35, 35, 35)
                        .addComponent(jScrollPane1)))
                .addGap(37, 37, 37))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cmb_PersonalitaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmb_PersonalitaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmb_PersonalitaActionPerformed

    /**
    * invia il testo quando viene premuto invio sulla tastiera.
    */
    private void txt_InputActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_InputActionPerformed
        // TODO add your handling code here:
        inviaMessaggio();
    }//GEN-LAST:event_txt_InputActionPerformed

    /**
    * gestisce il clic sul pulsante invia.
    */
    private void btn_InviaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_InviaActionPerformed
        // TODO add your handling code here:
        inviaMessaggio();
    }//GEN-LAST:event_btn_InviaActionPerformed

    private void btn_HelpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_HelpActionPerformed
        // TODO add your handling code here:
        javax.swing.JOptionPane.showMessageDialog(
            this,
            "benvenuto nell'oracolo!\n\n"
            + "1. inserisci il nickname.\n"
            + "2. scegli una personalità.\n"
            + "3. premi invia.\n"
            + "4. scrivi le tue domande.\n"
            + "5. scrivi EXIT per uscire.",
            "aiuto",
            javax.swing.JOptionPane.INFORMATION_MESSAGE
        );
    }//GEN-LAST:event_btn_HelpActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new frmOracolo().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_Help;
    private javax.swing.JButton btn_Invia;
    private javax.swing.JComboBox<String> cmb_Personalita;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField txt_Input;
    private javax.swing.JTextArea txt_Output;
    // End of variables declaration//GEN-END:variables
}
