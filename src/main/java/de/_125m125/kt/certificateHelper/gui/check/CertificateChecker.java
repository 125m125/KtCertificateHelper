package de._125m125.kt.certificateHelper.gui.check;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import org.bouncycastle.asn1.x509.ExtendedKeyUsage;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.KeyPurposeId;
import org.bouncycastle.cert.jcajce.JcaX509CertificateHolder;

import de._125m125.kt.certificateHelper.Permission;
import de._125m125.kt.certificateHelper.gui.CertificateHelper;
import de._125m125.kt.certificateHelper.gui.P12LocationChooser;

public class CertificateChecker extends JPanel {

    private int                     current    = -1;
    private final JPanel[]          panels     = { new P12LocationChooser(),
            new CertificateInspectionResult() };
    boolean                         changedCsr = true;
    boolean                         changedP12 = true;
    private JPanel                  last       = null;
    private JButton                 btnBack;
    private JButton                 btnNext;
    private JProgressBar            progressBar;
    private JPanel                  center;

    private final CertificateHelper certificateHelper;

    /**
     * Create the panel.
     */
    public CertificateChecker(final CertificateHelper certificateHelper) {
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

    private boolean extractData() {
        switch (this.current) {
        case -1:
            return true;
        case 0:
            final char[] password = ((P12LocationChooser) this.panels[0]).getTxtPassword()
                    .getPassword();

            try (InputStream keyInput = new FileInputStream(
                    ((P12LocationChooser) this.panels[0]).getTxtLocation().getText())) {
                final KeyStore keyStore = KeyStore.getInstance("PKCS12");
                keyStore.load(keyInput, password);
                final Enumeration<String> aliases = keyStore.aliases();
                while (aliases.hasMoreElements()) {
                    final Certificate certificate = keyStore.getCertificate(aliases.nextElement());
                    if (certificate == null || !(certificate instanceof X509Certificate)) {
                        continue;
                    }
                    final JcaX509CertificateHolder jcaX509CertificateHolder = new JcaX509CertificateHolder(
                            (X509Certificate) certificate);
                    final Extension ext = jcaX509CertificateHolder
                            .getExtension(Extension.extendedKeyUsage);
                    if (ext == null) {
                        continue;
                    }
                    final ExtendedKeyUsage extKey = ExtendedKeyUsage
                            .getInstance(ext.getParsedValue());
                    final Set<Permission> permissions = Arrays.stream(extKey.getUsages())
                            .map(KeyPurposeId::getId).map(Permission::ofOID)
                            .filter(Objects::nonNull).collect(Collectors.toSet());
                    ((CertificateInspectionResult) this.panels[1]).setPermissions(permissions);
                    return true;
                }
            } catch (final FileNotFoundException e) {
                e.printStackTrace();
            } catch (final IOException | NoSuchAlgorithmException | CertificateException
                    | KeyStoreException e) {
                e.printStackTrace();
            }
            return false;
        default:
            return false;
        }
    }

    public void next(final ActionEvent e) {
        if (this.current == this.panels.length - 1) {
            this.certificateHelper.home();
            return;
        }
        if (!extractData()) {
            return;
        }
        this.current++;

        manageButtonState();
    }

    public void previous(final ActionEvent e) {
        if (this.current == 0) {
            this.certificateHelper.home();
            return;
        }
        this.current--;

        manageButtonState();
    }

    public void manageButtonState() {
        if (this.last != null) {
            this.last.setVisible(false);
        }
        this.panels[this.current].setVisible(true);
        this.panels[this.current].setPreferredSize(this.center.getSize());
        this.last = this.panels[this.current];
        this.btnBack.setText(this.current == 0 ? "Home" : "Zurück");
        this.btnNext.setText(this.current == this.panels.length - 1 ? "Home" : "Weiter");
        this.progressBar.setValue(this.current + 1);
        revalidate();
    }
}
