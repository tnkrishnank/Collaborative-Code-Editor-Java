package colabcode.gui;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

public class DialogTemplate extends JDialog implements ActionListener {

    protected boolean bool = false;

    public DialogTemplate(Frame parent, String title) {
        super(parent, title, true);
    }

    protected void init() {
        JPanel panelButtons = this.drawButtonPanel();
        getContentPane().add(panelButtons, BorderLayout.SOUTH);
        setLocation((int) (Toolkit.getDefaultToolkit().getScreenSize().width / 4), (int) (Toolkit.getDefaultToolkit().getScreenSize().height / 4));
    }

    public JPanel drawButtonPanel() {
        JPanel panelButtons = new JPanel();
        JButton ok = new JButton("OK");
        JButton cancel = new JButton("Cancel");
        panelButtons.add(ok);
        panelButtons.add(cancel);
        ok.addActionListener(this);
        cancel.addActionListener(this);

        return panelButtons;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("OK")) {
            this.onOK();
        } else {
            this.onCancel();
        }
    }

    public boolean doModal() {
        show();
        return bool;
    }

    protected void onOK() {
        bool = true;
        dispose();
    }

    protected void onCancel() {
        bool = false;
        dispose();
    }
}
