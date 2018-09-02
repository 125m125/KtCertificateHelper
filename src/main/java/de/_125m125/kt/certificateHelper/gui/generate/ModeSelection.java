package de._125m125.kt.certificateHelper.gui.generate;

import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;

public class ModeSelection extends JPanel {
    private final ButtonGroup  buttonGroup = new ButtonGroup();
    private final JRadioButton rdbtnTfaMode;
    private final JRadioButton rdbtnApimode;
    private final JLabel       lblIdinfo;
    private final JTextField   txtUserid;
    private final JLabel       lblNameinfo;
    private final JTextField   txtName;

    /**
     * Create the panel.
     */
    public ModeSelection() {
        setLayout(new FormLayout(
                new ColumnSpec[] { ColumnSpec.decode("max(50dlu;default):grow"),
                        ColumnSpec.decode("max(50dlu;default):grow"), ColumnSpec.decode("max(50dlu;default):grow"), },
                new RowSpec[] { FormSpecs.LINE_GAP_ROWSPEC, RowSpec.decode("23px"), FormSpecs.RELATED_GAP_ROWSPEC,
                        FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC,
                        FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC,
                        FormSpecs.DEFAULT_ROWSPEC, }));

        this.lblIdinfo = new JLabel("Benutzerid");
        add(this.lblIdinfo, "1, 4");

        this.txtUserid = new JTextField();
        add(this.txtUserid, "2, 4, 2, 1, fill, default");
        this.txtUserid.setColumns(10);

        final JLabel chooseMode = new JLabel("Verwendungszweck");
        this.add(chooseMode, "1, 6");

        this.rdbtnTfaMode = new JRadioButton("2FA");
        this.buttonGroup.add(getRdbtnTfaMode());
        this.add(getRdbtnTfaMode(), "2, 6");

        this.rdbtnApimode = new JRadioButton("API");
        this.buttonGroup.add(this.rdbtnApimode);
        this.add(this.rdbtnApimode, "3, 6");

        this.lblNameinfo = new JLabel("Name des Zertifikates");
        add(this.lblNameinfo, "1, 8");

        this.txtName = new JTextField();
        add(this.txtName, "2, 8, 2, 1, fill, default");
        this.txtName.setColumns(10);
    }

    public JRadioButton getRdbtnApimode() {
        return this.rdbtnApimode;
    }

    public JRadioButton getRdbtnTfaMode() {
        return this.rdbtnTfaMode;
    }

    public JTextField getTxtUserid() {
        return this.txtUserid;
    }

    public JTextField getTxtName() {
        return this.txtName;
    }
}
