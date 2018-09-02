package de._125m125.kt.certificateHelper.gui.generate;

import java.awt.BorderLayout;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

public class CsrPresenter extends JPanel {
    private final JTextPane txtpnCsr;

    /**
     * Create the panel.
     */
    public CsrPresenter() {
        setLayout(new BorderLayout(0, 0));

        final JLabel lblInfo = new JLabel("Füge folgenden CSR in den Einstellungen ein:");
        add(lblInfo, BorderLayout.NORTH);

        final JScrollPane scrollPane = new JScrollPane();
        add(scrollPane, BorderLayout.CENTER);

        this.txtpnCsr = new JTextPane();
        scrollPane.setViewportView(this.txtpnCsr);
        this.txtpnCsr.setEditable(false);
        this.txtpnCsr.addFocusListener(new FocusListener() {

            @Override
            public void focusLost(final FocusEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void focusGained(final FocusEvent e) {
                CsrPresenter.this.txtpnCsr.setSelectionStart(0);
                CsrPresenter.this.txtpnCsr.setSelectionEnd(CsrPresenter.this.txtpnCsr.getText().length());
            }
        });
    }

    public JTextPane getTxtpnCsr() {
        return this.txtpnCsr;
    }
}
