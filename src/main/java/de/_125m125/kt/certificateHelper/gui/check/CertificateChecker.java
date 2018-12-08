package de._125m125.kt.certificateHelper.gui.check;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.io.File;
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

import org.bouncycastle.asn1.x500.RDN;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x500.style.IETFUtils;
import org.bouncycastle.asn1.x509.ExtendedKeyUsage;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.KeyPurposeId;
import org.bouncycastle.cert.jcajce.JcaX509CertificateHolder;

import de._125m125.kt.certificateHelper.Permission;
import de._125m125.kt.certificateHelper.gui.CertificateHelper;
import de._125m125.kt.certificateHelper.gui.P12LocationChooser;
import de._125m125.kt.ktapi.core.SingleUserKtRequester;
import de._125m125.kt.ktapi.core.entities.Permissions;
import de._125m125.kt.ktapi.core.results.Callback;
import de._125m125.kt.ktapi.core.users.CertificateUser;
import de._125m125.kt.ktapi.core.users.CertificateUserKey;
import de._125m125.kt.ktapi.core.users.KtUserStore;
import de._125m125.kt.ktapi.retrofit.KtRetrofit;

public class CertificateChecker extends JPanel {

    private int                     current    = -1;
    private final JPanel[]          panels     = { new P12LocationChooser(false),
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
        this.progressBar.setMaximum(this.panels.length);
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
            final File file = new File(
                    ((P12LocationChooser) this.panels[0]).getTxtLocation().getText());
            try (InputStream keyInput = new FileInputStream(file)) {
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
                    final RDN[] rdn = jcaX509CertificateHolder.getSubject().getRDNs(BCStyle.UID);
                    if (rdn.length != 1 || rdn[0].isMultiValued()) {
                        continue;
                    }
                    final String uid = IETFUtils.valueToString(rdn[0].getFirst().getValue());
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
                    if (permissions.isEmpty()) {
                        continue;
                    }
                    ((CertificateInspectionResult) this.panels[1]).setSerialNr(
                            ((X509Certificate) certificate).getSerialNumber().toString());
                    ((CertificateInspectionResult) this.panels[1]).setServerStatus(null);
                    if (!permissions.contains(Permission.TWO_FA_PERMISSION)) {
                        final CertificateUser user = new CertificateUser(uid, file, password);
                        final KtUserStore store = new KtUserStore(user);
                        final SingleUserKtRequester<CertificateUserKey> requester = KtRetrofit
                                .createClientCertificateRequester(store, user.getKey(), null);

                        requester.getPermissions().addCallback(new Callback<Permissions>() {
                            @Override
                            public void onSuccess(final int status, final Permissions result) {
                                ((CertificateInspectionResult) CertificateChecker.this.panels[1])
                                        .setServerStatus(true);
                            }

                            @Override
                            public void onFailure(final int status, final String message,
                                    final String humanReadableMessage) {
                                System.out.println(status + " " + humanReadableMessage);
                                ((CertificateInspectionResult) CertificateChecker.this.panels[1])
                                        .setServerStatus(false);

                            }

                            @Override
                            public void onError(final Throwable t) {
                                t.printStackTrace();
                                ((CertificateInspectionResult) CertificateChecker.this.panels[1])
                                        .setServerStatus(false);

                            }
                        });
                    }
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
