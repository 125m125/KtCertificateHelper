package de._125m125.kt.certificateHelper.gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import de._125m125.kt.certificateHelper.gui.check.CertificateChecker;
import de._125m125.kt.certificateHelper.gui.generate.CertificateGenerator;

public class CertificateHelper {

    /**
     * Launch the application.
     */
    public static void main(final String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                final CertificateHelper window = new CertificateHelper();
                window.frame.setVisible(true);
            } catch (final Exception e) {
                e.printStackTrace();
            }
        });
    }

    private final JFrame frame;
    private JPanel       selectionpanel;

    public CertificateHelper() {
        this.frame = new JFrame();
        this.frame.setBounds(100, 100, 450, 300);
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.getContentPane().setLayout(new BorderLayout(0, 0));

        generateSelectionPanel();
        this.frame.getContentPane().add(this.selectionpanel, BorderLayout.CENTER);
    }

    private void generateSelectionPanel() {
        this.selectionpanel = new JPanel();

        final JButton generate = new JButton("Zertifikat generieren");
        generate.addActionListener(e -> {
            this.frame.getContentPane().removeAll();
            final CertificateGenerator comp = new CertificateGenerator(this);
            this.frame.getContentPane().add(comp, BorderLayout.CENTER);
            EventQueue.invokeLater(() -> {
                comp.next(null);
            });
            this.frame.revalidate();
        });
        this.selectionpanel.add(generate);

        final JButton check = new JButton("Zertifikat prüfen");
        check.addActionListener(e -> {
            this.frame.getContentPane().removeAll();
            final CertificateChecker comp = new CertificateChecker(this);
            this.frame.getContentPane().add(comp, BorderLayout.CENTER);
            EventQueue.invokeLater(() -> {
                comp.next(null);
            });
            this.frame.revalidate();
        });
        this.selectionpanel.add(check);

    }

    public void home() {
        System.out.println("home...");
        this.frame.getContentPane().removeAll();
        this.frame.getContentPane().add(this.selectionpanel, BorderLayout.CENTER);
        this.frame.revalidate();
        this.frame.repaint();
    }
}
