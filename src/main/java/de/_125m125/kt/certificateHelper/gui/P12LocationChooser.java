package de._125m125.kt.certificateHelper.gui;

import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;

public class P12LocationChooser extends JPanel {
    private final JPasswordField txtPassword;
    private final JTextField     txtLocation;

    /**
     * Create the panel.
     */
    public P12LocationChooser() {
        setLayout(new FormLayout(
                new ColumnSpec[] { FormSpecs.DEFAULT_COLSPEC, ColumnSpec.decode("default:grow"),
                        FormSpecs.DEFAULT_COLSPEC, },
                new RowSpec[] { FormSpecs.DEFAULT_ROWSPEC, FormSpecs.LINE_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC, }));

        final JLabel lblInfo = new JLabel("Speicherort");
        add(lblInfo, "1, 1, fill, fill");

        this.txtLocation = new JTextField();
        this.txtLocation.setText("certificate.p12");
        add(this.txtLocation, "2, 1, fill, fill");
        this.txtLocation.setColumns(10);

        final JButton btnSelect = new JButton("Wählen");
        add(btnSelect, "3, 1");
        btnSelect.addActionListener(e -> {
            final JFileChooser chooser = new JFileChooser(this.txtLocation.getText());
            chooser.setSelectedFile(new File(this.txtLocation.getText()));
            final int s = chooser.showSaveDialog(this);
            if (s == JFileChooser.APPROVE_OPTION) {
                this.txtLocation.setText(chooser.getSelectedFile().getAbsolutePath());
            }
        });

        final JLabel lblPassword = new JLabel("Passwort für Datei (nicht Kadcontrade)");
        add(lblPassword, "1, 3, 2, 1, fill, fill");

        this.txtPassword = new JPasswordField();
        add(this.txtPassword, "3, 3, fill, fill");
        this.txtPassword.setColumns(10);

    }

    public JPasswordField getTxtPassword() {
        return this.txtPassword;
    }

    public JTextField getTxtLocation() {
        return this.txtLocation;
    }
}
