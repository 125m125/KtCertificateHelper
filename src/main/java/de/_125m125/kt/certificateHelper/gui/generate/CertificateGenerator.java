package de._125m125.kt.certificateHelper.gui.generate;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.security.KeyPair;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.pkcs.PKCSException;

import de._125m125.kt.certificateHelper.CertificateUtils;
import de._125m125.kt.certificateHelper.Permission;
import de._125m125.kt.certificateHelper.gui.CertificateHelper;
import de._125m125.kt.certificateHelper.gui.P12LocationChooser;

public class CertificateGenerator extends JPanel {

    private int                     current     = -1;
    private final JPanel[]          panels      = { new ModeSelection(), new ApiPermissions(),
            new CsrPresenter(), new CrtAccepter(), new P12LocationChooser(true), new Success(), };
    boolean                         changedCsr  = true;
    boolean                         changedP12  = true;
    private JPanel                  last        = null;
    private JButton                 btnBack;
    private JButton                 btnNext;
    private JProgressBar            progressBar;
    private JPanel                  center;

    private int                     mode;
    private Set<Permission>         permissions = new HashSet<>();
    private String                  userid;
    private String                  certName;
    private String                  crtText;
    private X509Certificate         crt;
    private char[]                  password;
    private File                    file;
    private KeyPair                 keyPair;

    private final CertificateUtils  helper      = new CertificateUtils();
    private final CertificateHelper certificateHelper;

    /**
     * Create the application.
     * 
     * @param certificateHelper
     * 
     * @wbp.parser.entryPoint
     */
    public CertificateGenerator(final CertificateHelper certificateHelper) {
        this.certificateHelper = certificateHelper;
        initialize();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        setLayout(new BorderLayout());

        final JPanel panel_1 = new JPanel();
        this.add(panel_1, BorderLayout.NORTH);
        panel_1.setLayout(new BorderLayout(0, 0));

        this.btnBack = new JButton("Zurück");
        this.btnBack.addActionListener(this::previous);
        panel_1.add(this.btnBack, BorderLayout.WEST);

        this.btnNext = new JButton("Weiter");
        this.btnNext.addActionListener(this::next);
        panel_1.add(this.btnNext, BorderLayout.EAST);

        this.progressBar = new JProgressBar();
        this.progressBar.setMaximum(6);
        panel_1.add(this.progressBar, BorderLayout.CENTER);

        this.center = new JPanel();
        for (final JPanel p : this.panels) {
            this.center.add(p);
            p.setVisible(false);
        }
        this.add(this.center);
    }

    public void next(final ActionEvent e) {
        if (this.current == 5) {
            this.certificateHelper.home();
            return;
        }
        if (!extractData()) {
            return;
        }
        this.current++;
        if (this.current == 1 && this.mode != 2) {
            this.current++;
        }

        if (this.current == 2) {
            if (this.changedCsr) {
                try {
                    if (this.keyPair == null) {
                        this.keyPair = this.helper.generatekeyPair();
                    }
                    ((CsrPresenter) this.panels[2]).getTxtpnCsr().setText(this.helper.generateCsr(
                            this.keyPair, this.certName, this.userid, this.permissions));
                    this.changedCsr = false;
                } catch (NoSuchAlgorithmException | OperatorCreationException | IOException e1) {
                    e1.printStackTrace();
                    JOptionPane.showMessageDialog(null,
                            "CSR-Generierung ist fehlgeschlagen: " + e1.getMessage(), "Fehler",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        }
        if (this.current == 5) {
            if (this.changedP12) {
                try {
                    this.helper.createPKCS12File(this.file, this.keyPair.getPrivate(), this.crt,
                            this.password);
                } catch (NoSuchAlgorithmException | KeyStoreException | CertificateException
                        | IOException | PKCSException e1) {
                    e1.printStackTrace();
                    JOptionPane.showMessageDialog(null,
                            "Speichern ist fehlgeschlagen: " + e1.getMessage(), "Fehler",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        }
        manageButtonState();
    }

    public void previous(final ActionEvent e) {
        if (this.current == 0) {
            this.certificateHelper.home();
            return;
        }
        this.current--;
        if (this.current == 1 && this.mode != 2) {
            this.current--;
        }

        manageButtonState();
    }

    private boolean extractData() {
        switch (this.current) {
        case -1:
            return true;
        case 0:
            try {
                int m = 0;
                if (((ModeSelection) this.panels[0]).getRdbtnApimode().isSelected()) {
                    m = 2;
                } else if (((ModeSelection) this.panels[0]).getRdbtnTfaMode().isSelected()) {
                    m = 1;
                    this.permissions = new HashSet<>();
                    this.permissions.add(Permission.TWO_FA_PERMISSION);
                } else {
                    return false;
                }
                if (m != this.mode) {
                    this.mode = m;
                    this.changedCsr = true;
                }
                final String i = ((ModeSelection) this.panels[0]).getTxtUserid().getText();
                if (i.isEmpty()) {
                    return false;
                }
                if (!i.equals(this.userid)) {
                    this.userid = i;
                    this.changedCsr = true;
                }
                final String n = ((ModeSelection) this.panels[0]).getTxtName().getText();
                if (n.isEmpty()) {
                    return false;
                }
                if (!n.equals(this.certName)) {
                    this.certName = n;
                    this.changedCsr = true;
                }
            } catch (final NumberFormatException e) {
                return false;
            }
            return true;
        case 1:
            final Set<Permission> selectedPermissions = ((ApiPermissions) this.panels[1])
                    .getCheckboxMap().entrySet().stream().filter(e -> e.getKey().isSelected())
                    .map(Entry::getValue).collect(Collectors.toSet());
            if (selectedPermissions.isEmpty()) {
                return false;
            }
            if (!selectedPermissions.equals(this.permissions)) {
                this.permissions = selectedPermissions;
                this.changedCsr = true;
            }
            return true;
        case 2:
            return true;
        case 3:
            final String text = ((CrtAccepter) this.panels[3]).getTxtpnCrt().getText();
            if (text.equals(this.crtText)) {
                return true;
            }
            try (final Reader reader = new StringReader(text);
                    PEMParser parser = new PEMParser(reader)) {
                final Object readObject = parser.readObject();
                if (readObject instanceof X509CertificateHolder) {
                    this.crt = new JcaX509CertificateConverter()
                            .getCertificate((X509CertificateHolder) readObject);
                    this.crtText = text;
                    this.changedP12 = true;
                    return true;
                } else {
                    JOptionPane.showMessageDialog(null, "Ungültiges Zertifikat", "Fehler",
                            JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            } catch (IOException | CertificateException e1) {
                JOptionPane.showMessageDialog(null, "Ungültiges Zertifikat", "Fehler",
                        JOptionPane.ERROR_MESSAGE);
                return false;
            }
        case 4:
            final char[] password2 = ((P12LocationChooser) this.panels[4]).getTxtPassword()
                    .getPassword();
            if (!Arrays.equals(password2, this.password)) {
                this.password = password2;
                this.changedP12 = true;
            }
            String selectedFile = ((P12LocationChooser) this.panels[4]).getTxtLocation().getText();
            if (!selectedFile.endsWith(".p12")) {
                selectedFile += ".p12";
            }
            if (this.file == null || !selectedFile.equals(this.file.getAbsolutePath())) {
                this.file = new File(selectedFile);
                this.changedP12 = true;
            }
            return true;
        default:
            return false;
        }
    }

    public void manageButtonState() {
        if (this.last != null) {
            this.last.setVisible(false);
        }
        this.panels[this.current].setVisible(true);
        this.panels[this.current].setPreferredSize(this.center.getSize());
        this.last = this.panels[this.current];
        this.btnBack.setText(this.current == 0 ? "Home" : "Zurück");
        this.btnNext.setText(this.current == 5 ? "Home" : "Weiter");
        this.progressBar.setValue(this.current + 1);
        revalidate();
    }

}
