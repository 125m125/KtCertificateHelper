package de._125m125.kt.certificateHelper.gui.generate;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JTextArea;

public class Success extends JPanel {

    /**
     * Create the panel.
     */
    public Success() {
        setLayout(new BorderLayout(0, 0));

        final JTextArea txtrInfo = new JTextArea();
        txtrInfo.setLineWrap(true);
        txtrInfo.setWrapStyleWord(true);
        txtrInfo.setText("Das Zertifikat wurde erfolgreich gespeichert.\r\n"
                + "Um das Zertifikat im Browser zu verwenden, muss dieses zuerst installiert werden. In Chrome geht das in den Einstellungen in der Kategorie \"Sicherheit und Datenschutz\" unter \"Zertifikate verwalten\". In Firefox befindet sich die Importfunktion in der Kategorie \"Datenschutz & Sicherheit\" in der Kategorie Zertifikate mit dem Button \"Zertifikate anzeigen...\". Das Zertifikat muss dort jeweils unter \"Eigene Zertifikate\" importiert werden.\r\n"
                + "Beim nächsten Besuch von Kadcontrade kann nun das Zertifikat ausgewählt werden. Eventuell muss der Browser vorher neu gestartet werden.");
        add(txtrInfo);
    }

}
