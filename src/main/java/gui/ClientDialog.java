package colabcode.gui;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JPasswordField;

import colabcode.exception.*;

public class ClientDialog extends DialogTemplate {

    public String ip;
    public int port;
    public String name;
    public String password;

    private JTextField fieldIp;
    private JTextField fieldPort;
    private JTextField fieldName;
    private JPasswordField fieldPassword;

    public ClientDialog(Frame parent) {
        super(parent, "CONNECT");
        this.ip = null;
        this.port = 0;
        this.name = null;
        this.password = null;
        try {
            init();
        } catch (Exception e) {
            System.out.println("ERROR: ClientDialog::ClientDialog()");
        }
    }

    @Override
    protected void init() {
        super.init();

        JPanel mainPanel = new JPanel(new GridLayout(4, 4));

        mainPanel.add(new JLabel("      IP/HOSTNAME"));
        fieldIp = new JTextField(10);
        mainPanel.add(fieldIp);

        mainPanel.add(new JLabel("      PORT"));
        fieldPort = new JTextField(4);
        fieldPort.setText("2305");
        mainPanel.add(fieldPort);

        mainPanel.add(new JLabel("      USERNAME"));
        fieldName = new JTextField(15);
        mainPanel.add(fieldName);

        mainPanel.add(new JLabel("      PASSWORD"));
        fieldPassword = new JPasswordField(15);
        mainPanel.add(fieldPassword);

        getContentPane().add(mainPanel, BorderLayout.NORTH);

        this.setSize(400, 200);
    }

    @Override
    protected void onOK() {
        try {
            this.ip = fieldIp.getText();
            this.port = Integer.parseInt(fieldPort.getText());
            this.password = new String(fieldPassword.getPassword());
            if (this.port > 65535) {
                NumberFormatException ex = new NumberFormatException("PORT TOO LARGE : " + this.port);
                throw ex;
            }
            this.name = fieldName.getText();
            if (name.length() < 1) {
                BadUsernameException ex = new BadUsernameException("EMPTY USERNAME");
                throw ex;
            }
            this.bool = true;
            dispose();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "PORT OUT OF RANGE (1 - 65535)");
        } catch (BadUsernameException e) {
            JOptionPane.showMessageDialog(null, "EMPTY USERNAME");
        } catch (Exception e) {
            System.out.println("ERROR: ClientDialog::onOK()");
        }
    }
}
