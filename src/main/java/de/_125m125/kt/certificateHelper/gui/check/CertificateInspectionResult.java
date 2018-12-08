package de._125m125.kt.certificateHelper.gui.check;

import java.awt.Component;
import java.awt.GridLayout;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.swing.Box;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import de._125m125.kt.certificateHelper.Permission;

public class CertificateInspectionResult extends JPanel {
    private final Map<Permission, JCheckBox> checkboxes = new HashMap<>();
    private JTextField                       mfaSupport;

    /**
     * Create the panel.
     */
    public CertificateInspectionResult() {
        setLayout(new GridLayout(7, 3, 0, 0));

        final JCheckBox chckbxMFA = new JCheckBox("2. Faktor");
        this.checkboxes.put(Permission.TWO_FA_PERMISSION, chckbxMFA);
        add(chckbxMFA);
        add(Box.createGlue());
        add(Box.createGlue());
        add(Box.createGlue());
        add(Box.createGlue());
        add(Box.createGlue());

        final Component glue = Box.createGlue();
        add(glue);

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

}
