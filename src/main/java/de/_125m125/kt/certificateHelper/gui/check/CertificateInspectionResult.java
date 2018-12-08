package de._125m125.kt.certificateHelper.gui.check;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.swing.Box;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import de._125m125.kt.certificateHelper.Permission;

public class CertificateInspectionResult extends JPanel {
    private final Map<Permission, JCheckBox> checkboxes = new HashMap<>();
    private final JLabel                     serverAccepted;
    private final JLabel                     serialNr;

    /**
     * Create the panel.
     */
    public CertificateInspectionResult() {
        setLayout(new GridLayout(7, 3, 0, 0));

        add(new JLabel("Seriennummer: "));
        this.serialNr = new JLabel("", SwingConstants.RIGHT);
        add(this.serialNr);
        add(Box.createGlue());

        add(new JLabel("Zertifikat gültig: "));
        this.serverAccepted = new JLabel("loading...");
        add(this.serverAccepted);
        add(Box.createGlue());

        final JCheckBox chckbxMFA = new JCheckBox("2. Faktor");
        this.checkboxes.put(Permission.TWO_FA_PERMISSION, chckbxMFA);
        add(chckbxMFA);

        final JLabel lblRead = new JLabel("Auslesen");
        add(lblRead);

        final JLabel lblWrite = new JLabel("Bearbeiten");
        add(lblWrite);

        final JLabel lblItemlist = new JLabel("Itemliste");
        add(lblItemlist);

        final JCheckBox chckbxReaditemlist = new JCheckBox("");
        this.checkboxes.put(Permission.READ_ITEMLIST_PERMISSION, chckbxReaditemlist);
        chckbxReaditemlist.setToolTipText("Itemliste auslesen");
        add(chckbxReaditemlist);

        final Component glue_2 = Box.createGlue();
        add(glue_2);

        final JLabel lblMessages = new JLabel("Nachrichten");
        add(lblMessages);

        final JCheckBox chckbxReadmessages = new JCheckBox("");
        this.checkboxes.put(Permission.READ_MESSAGES_PERMISSION, chckbxReadmessages);
        chckbxReadmessages.setToolTipText("Nachrichten auslesen");
        add(chckbxReadmessages);

        final Component glue_1 = Box.createGlue();
        add(glue_1);

        final JLabel lblOrders = new JLabel("Orders");
        add(lblOrders);

        final JCheckBox chckbxReadorders = new JCheckBox("");
        this.checkboxes.put(Permission.READ_ORDERS_PERMISSION, chckbxReadorders);
        chckbxReadorders.setToolTipText("Orders auslesen");
        add(chckbxReadorders);

        final JCheckBox chckbxWriteorders = new JCheckBox("");
        this.checkboxes.put(Permission.WRITE_ORDERS_PERMISSION, chckbxWriteorders);
        chckbxWriteorders.setToolTipText("Orders bearbeiten");
        add(chckbxWriteorders);

        final JLabel lblPayouts = new JLabel("Auszahlungen");
        add(lblPayouts);

        final JCheckBox chckbxReadpayouts = new JCheckBox("");
        this.checkboxes.put(Permission.READ_PAYOUTS_PERMISSION, chckbxReadpayouts);
        chckbxReadpayouts.setToolTipText("Auszahlungen auslesen");
        add(chckbxReadpayouts);

        final JCheckBox chckbxWritepayouts = new JCheckBox("");
        this.checkboxes.put(Permission.WRITE_PAYOUTS_PERMISSION, chckbxWritepayouts);
        chckbxWritepayouts.setToolTipText("Auszahlungen bearbeiten");
        add(chckbxWritepayouts);

        this.checkboxes.values().forEach(b -> b.setEnabled(false));
    }

    public void setPermissions(final Set<Permission> permissions) {
        this.checkboxes.entrySet().forEach(e -> {
            e.getValue().setSelected(permissions.contains(e.getKey()));
        });
    }

    public void setServerStatus(final Boolean s) {
        if (Boolean.TRUE.equals(s)) {
            this.serverAccepted.setText("ja");
            this.serverAccepted.setForeground(Color.GREEN.darker());
        } else if (Boolean.FALSE.equals(s)) {
            this.serverAccepted.setText("nein");
            this.serverAccepted.setForeground(Color.RED);
        } else {
            this.serverAccepted.setText("unbekannt");
            this.serverAccepted.setForeground(Color.BLUE);
        }
    }

    public void setSerialNr(String nr) {
        if (nr.length() > 15) {
            nr = "..." + nr.substring(nr.length() - 15, nr.length());
        }
        this.serialNr.setText(nr);
    }

}
