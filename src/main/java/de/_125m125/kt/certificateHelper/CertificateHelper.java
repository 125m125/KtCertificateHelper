package de._125m125.kt.certificateHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x500.style.RFC4519Style;
import org.bouncycastle.asn1.x509.ExtendedKeyUsage;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.Extensions;
import org.bouncycastle.asn1.x509.KeyPurposeId;
import org.bouncycastle.openssl.jcajce.JcaPEMWriter;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.bouncycastle.pkcs.PKCS10CertificationRequestBuilder;
import org.bouncycastle.pkcs.PKCSException;
import org.bouncycastle.pkcs.jcajce.JcaPKCS10CertificationRequestBuilder;

public class CertificateHelper {
    public KeyPair generatekeyPair() throws NoSuchAlgorithmException {
        final KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");

        final SecureRandom random = new SecureRandom();
        generator.initialize(4096, random);

        return generator.generateKeyPair();
    }

    public String generateCsr(final KeyPair pair, final String certName, final String uid,
            final Collection<Permission> permissions)
            throws NoSuchAlgorithmException, IOException, OperatorCreationException {
        final X500Name subject = new X500NameBuilder(RFC4519Style.INSTANCE).addRDN(BCStyle.CN, certName)
                .addRDN(BCStyle.UID, uid).build();

        final List<KeyPurposeId> l = permissions.stream().map(Permission::getOid).map(ASN1ObjectIdentifier::new)
                .map(KeyPurposeId::getInstance).collect(Collectors.toCollection(() -> new ArrayList<>()));
        l.add(KeyPurposeId.id_kp_clientAuth);
        final PKCS10CertificationRequestBuilder p10Builder = new JcaPKCS10CertificationRequestBuilder(subject,
                pair.getPublic());
        p10Builder.addAttribute(PKCSObjectIdentifiers.pkcs_9_at_extensionRequest,
                new Extensions(new Extension(Extension.extendedKeyUsage, false,
                        new ExtendedKeyUsage(l.toArray(new KeyPurposeId[l.size()])).getEncoded())));
        final JcaContentSignerBuilder csBuilder = new JcaContentSignerBuilder("SHA256withRSA");
        final ContentSigner signer = csBuilder.build(pair.getPrivate());
        final PKCS10CertificationRequest build = p10Builder.build(signer);
        String k = null;
        try (StringWriter sw = new StringWriter()) {
            try (JcaPEMWriter pemWriter = new JcaPEMWriter(sw)) {
                pemWriter.writeObject(build);
            }
            k = sw.toString();
        }
        return k;
    }

    public void createPKCS12File(final File f, final PrivateKey key, final X509Certificate cert, final char[] passwd)
            throws NoSuchAlgorithmException, IOException, PKCSException, KeyStoreException, CertificateException {
        final KeyStore outStore = KeyStore.getInstance("PKCS12");
        outStore.load(null, passwd);
        outStore.setKeyEntry("mykey", key, passwd, new Certificate[] { cert });
        try (final OutputStream outputStream = new FileOutputStream(f)) {
            outStore.store(outputStream, passwd);
        }
    }
}
