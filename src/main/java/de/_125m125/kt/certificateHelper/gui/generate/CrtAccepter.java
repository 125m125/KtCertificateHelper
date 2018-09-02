package de._125m125.kt.certificateHelper.gui.generate;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextPane;

public class CrtAccepter extends JPanel {
    private final JTextPane txtpnCrt;

    /**
     * Create the panel.
     */
    public CrtAccepter() {
        setLayout(new BorderLayout(0, 0));

        final JLabel lblInfo = new JLabel("Füge hier das erhaltene Zertifikat ein:");
        add(lblInfo, BorderLayout.NORTH);

        this.txtpnCrt = new JTextPane();
        add(this.txtpnCrt, BorderLayout.CENTER);

    }

    public JTextPane getTxtpnCrt() {
        return this.txtpnCrt;
    }
}
