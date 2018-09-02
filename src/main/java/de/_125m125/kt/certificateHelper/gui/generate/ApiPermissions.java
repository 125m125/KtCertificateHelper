package de._125m125.kt.certificateHelper.gui.generate;

import java.awt.Component;
import java.awt.GridLayout;
import java.util.HashMap;
import java.util.Map;

import javax.swing.Box;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import de._125m125.kt.certificateHelper.Permission;

public class ApiPermissions extends JPanel {
    private final JCheckBox                  chckbxReadmessages;
    private final JCheckBox                  chckbxReadpayouts;
    private final JCheckBox                  chckbxWritepayouts;
    private final JCheckBox                  chckbxReaditemlist;
    private final JCheckBox                  chckbxWriteorders;
    private final JCheckBox                  chckbxReadorders;

    private final Map<JCheckBox, Permission> checkBoxMap = new HashMap<>();

    /**
     * Create the panel.
     */
    public ApiPermissions() {
        setLayout(new GridLayout(5, 3, 0, 0));

        final Component glue = Box.createGlue();
        add(glue);

        final JLabel lblRead = new JLabel("Auslesen");
        add(lblRead);

        final JLabel lblWrite = new JLabel("Bearbeiten");
        add(lblWrite);

        final JLabel lblItemlist = new JLabel("Itemliste");
        add(lblItemlist);

        this.chckbxReaditemlist = new JCheckBox("");
        this.chckbxReaditemlist.setToolTipText("Itemliste auslesen");
        add(this.chckbxReaditemlist);
        this.getCheckboxMap().put(this.chckbxReaditemlist, Permission.READ_ITEMLIST_PERMISSION);

        final Component glue_2 = Box.createGlue();
        add(glue_2);

        final JLabel lblMessages = new JLabel("Nachrichten");
        add(lblMessages);

        this.chckbxReadmessages = new JCheckBox("");
        this.chckbxReadmessages.setToolTipText("Nachrichten auslesen");
        add(this.chckbxReadmessages);
        this.getCheckboxMap().put(this.chckbxReadmessages, Permission.READ_MESSAGES_PERMISSION);

        final Component glue_1 = Box.createGlue();
        add(glue_1);

        final JLabel lblOrders = new JLabel("Orders");
        add(lblOrders);

        this.chckbxReadorders = new JCheckBox("");
        this.chckbxReadorders.setToolTipText("Orders auslesen");
        add(this.chckbxReadorders);
        this.getCheckboxMap().put(this.chckbxReadorders, Permission.READ_ORDERS_PERMISSION);

        this.chckbxWriteorders = new JCheckBox("");
        this.chckbxWriteorders.setToolTipText("Orders bearbeiten");
        add(this.chckbxWriteorders);
        this.getCheckboxMap().put(this.chckbxWriteorders, Permission.WRITE_ORDERS_PERMISSION);

        final JLabel lblPayouts = new JLabel("Auszahlungen");
        add(lblPayouts);

        this.chckbxReadpayouts = new JCheckBox("");
        this.chckbxReadpayouts.setToolTipText("Auszahlungen auslesen");
        add(this.chckbxReadpayouts);
        this.getCheckboxMap().put(this.chckbxReadpayouts, Permission.READ_PAYOUTS_PERMISSION);

        this.chckbxWritepayouts = new JCheckBox("");
        this.chckbxWritepayouts.setToolTipText("Auszahlungen bearbeiten");
        add(this.chckbxWritepayouts);
        this.getCheckboxMap().put(this.chckbxWritepayouts, Permission.WRITE_PAYOUTS_PERMISSION);

    }

    public Map<JCheckBox, Permission> getCheckboxMap() {
        return checkBoxMap;
    }

}
