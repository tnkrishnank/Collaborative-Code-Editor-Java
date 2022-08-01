package colabcode.gui;

import java.util.ArrayList;
import java.awt.Component;
import java.awt.Container;
import java.util.List;

import colabcode.connect.ClientManager;
import colabcode.connect.ConnectionManager;
import colabcode.connect.ServerManager;

public class MainFrame extends javax.swing.JFrame {

    public static int untitledId = 1;
    public static int terminalId = 1;
    public static int noFileOpen = 0;
    public static int noTerminalOpen = 0;

    private javax.swing.JPanel pBody;
    private javax.swing.JPanel pLeft;
    private javax.swing.JPanel pRight;
    private javax.swing.JScrollPane pScrollConnection;

    public static javax.swing.JTabbedPane tabEditor;
    public static javax.swing.JTabbedPane tabTerminal;

    private javax.swing.JTextArea text;
    private javax.swing.JTextArea textConsole;

    private static javax.swing.JButton btnClose;
    private static javax.swing.JPanel panelTab;
    private static java.awt.GridBagConstraints gbc;

    public javax.swing.text.Document document;
    public javax.swing.event.DocumentListener documentListener;
    private UserList userList;
    public ConnectionManager connectionManager;
    private Menu menuBar;
    private javax.swing.JMenuItem[] fileItems;
    private javax.swing.JMenuItem[] editItems;
    private javax.swing.JMenuItem[] networkItems;
    private javax.swing.undo.UndoManager undoManager;

    public static List<String> filePath = new ArrayList<>();

    public MainFrame() {
        super();
        try {
            userList = new UserList();
            menuBar = new Menu(this);
            fileItems = menuBar.getfileItems();
            editItems = menuBar.geteditItems();
            networkItems = menuBar.getNetworkItems();
            undoManager = menuBar.getUndoManager();
            this.init();
        } catch (Exception e) {
        }
    }

    private void init() throws Exception {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        this.setVisible(true);

        setJMenuBar(menuBar);

        text = new javax.swing.JTextArea();
        document = text.getDocument();
        pBody = new javax.swing.JPanel();
        pLeft = new javax.swing.JPanel();
        pScrollConnection = new javax.swing.JScrollPane(userList);
        pRight = new javax.swing.JPanel();
        tabEditor = new javax.swing.JTabbedPane();
        tabTerminal = new javax.swing.JTabbedPane();
        textConsole = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Colab Code");

        pBody.setBackground(new java.awt.Color(60, 60, 60));

        pLeft.setBackground(new java.awt.Color(60, 60, 60));
        pLeft.setLayout(new java.awt.GridLayout(1, 1));
        pLeft.add(pScrollConnection);

        pRight.setBackground(new java.awt.Color(60, 60, 60));

        tabEditor.setBackground(new java.awt.Color(60, 60, 60));
        tabEditor.setForeground(new java.awt.Color(250, 250, 250));
        tabEditor.setOpaque(true);

        addNewTab("Untitled1", "", "");

        tabTerminal.setBackground(new java.awt.Color(60, 60, 60));
        tabTerminal.setForeground(new java.awt.Color(250, 250, 250));
        tabTerminal.setOpaque(true);

        textConsole.setBackground(new java.awt.Color(53, 54, 58));
        textConsole.setColumns(20);
        textConsole.setFont(new java.awt.Font("Lucida Console", 0, 14));
        textConsole.setForeground(new java.awt.Color(250, 250, 250));
        textConsole.setRows(5);

        javax.swing.GroupLayout pBodyLayout = new javax.swing.GroupLayout(pBody);
        pBody.setLayout(pBodyLayout);
        pBodyLayout.setHorizontalGroup(
                pBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(pBodyLayout.createSequentialGroup()
                                .addComponent(pLeft, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(pRight, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pBodyLayout.setVerticalGroup(
                pBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(pLeft, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(pRight, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout pRightLayout = new javax.swing.GroupLayout(pRight);
        pRight.setLayout(pRightLayout);
        pRightLayout.setHorizontalGroup(
                pRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(tabTerminal)
                        .addComponent(tabEditor, javax.swing.GroupLayout.DEFAULT_SIZE, 720, Short.MAX_VALUE)
        );
        pRightLayout.setVerticalGroup(
                pRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(pRightLayout.createSequentialGroup()
                                .addComponent(tabEditor, javax.swing.GroupLayout.DEFAULT_SIZE, 201, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(tabTerminal, javax.swing.GroupLayout.DEFAULT_SIZE, 159, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(pBody, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(pBody, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        document.addUndoableEditListener(undoManager);

        pack();
    }

    public static javax.swing.JTextArea addNewTab(String fName, String text, String path) {
        int tabIndex;
        filePath.add(path);
        javax.swing.JTextArea newOpen = new javax.swing.JTextArea();
        newOpen.setBackground(new java.awt.Color(53, 54, 58));
        newOpen.setColumns(20);
        newOpen.setFont(new java.awt.Font("Lucida Console", 0, 14));
        newOpen.setForeground(new java.awt.Color(250, 250, 250));
        newOpen.setRows(5);
        newOpen.setText(text);
        tabEditor.addTab(fName, new javax.swing.JScrollPane(newOpen));
        tabEditor.setSelectedIndex(tabEditor.getTabCount() - 1);
        tabIndex = tabEditor.getSelectedIndex();
        panelTab = new javax.swing.JPanel(new java.awt.GridBagLayout());
        panelTab.setOpaque(false);
        btnClose = new javax.swing.JButton("X");
        btnClose.setName(Integer.toString(noFileOpen + 1));
        noFileOpen += 1;
        gbc = new java.awt.GridBagConstraints();
        gbc.gridx = 0;
        panelTab.add(new javax.swing.JLabel(fName), gbc);
        gbc.gridx++;
        panelTab.add(btnClose, gbc);
        tabEditor.setTabComponentAt(tabIndex, panelTab);
        btnClose.addActionListener((java.awt.event.ActionEvent event) -> {
            javax.swing.JButton b = (javax.swing.JButton) event.getSource();
            int index = Integer.valueOf(b.getName());
            tabEditor.remove(index - 1);
            filePath.remove(index - 1);
            noFileOpen -= 1;
            List<javax.swing.JButton> buttonList = new ArrayList<>();
            getAllButtons(tabEditor, buttonList);
            int k;
            for (int i = 0; i < buttonList.size(); i++) {
                if (!buttonList.get(i).getName().equals("ScrollBar.button")) {
                    k = Integer.parseInt(buttonList.get(i).getName());
                    if (k > index) {
                        buttonList.get(i).setName(Integer.toString(k - 1));
                    }
                }
            }
        });

        return newOpen;
    }

    public static javax.swing.JTextArea addNewTerminal(String fName) {
        int tabIndex;
        javax.swing.JTextArea newOpen = new javax.swing.JTextArea();
        newOpen.setBackground(new java.awt.Color(53, 54, 58));
        newOpen.setColumns(20);
        newOpen.setFont(new java.awt.Font("Lucida Console", 0, 14));
        newOpen.setForeground(new java.awt.Color(250, 250, 250));
        newOpen.setRows(5);
        newOpen.setText("");
        tabTerminal.addTab(fName, new javax.swing.JScrollPane(newOpen));
        tabTerminal.setSelectedIndex(tabTerminal.getTabCount() - 1);
        tabIndex = tabTerminal.getSelectedIndex();
        panelTab = new javax.swing.JPanel(new java.awt.GridBagLayout());
        panelTab.setOpaque(false);
        btnClose = new javax.swing.JButton("X");
        btnClose.setName(Integer.toString(noTerminalOpen + 1));
        noTerminalOpen += 1;
        gbc = new java.awt.GridBagConstraints();
        gbc.gridx = 0;
        panelTab.add(new javax.swing.JLabel(fName), gbc);
        gbc.gridx++;
        panelTab.add(btnClose, gbc);
        tabTerminal.setTabComponentAt(tabIndex, panelTab);
        btnClose.addActionListener((java.awt.event.ActionEvent event) -> {
            Menu.mNewTerminal.setEnabled(true);
            javax.swing.JButton b = (javax.swing.JButton) event.getSource();
            int index = Integer.valueOf(b.getName());
            tabTerminal.remove(index - 1);
            noTerminalOpen -= 1;
            Container focus = tabTerminal.getFocusCycleRootAncestor();
            List<javax.swing.JButton> buttonList = new ArrayList<>();
            getAllButtons(focus, buttonList);
            int k;
            for (int i = 0; i < buttonList.size(); i++) {
                if (!buttonList.get(i).getName().equals("ScrollBar.button")) {
                    k = Integer.parseInt(buttonList.get(i).getName());
                    if (k > index) {
                        buttonList.get(i).setName(Integer.toString(k - 1));
                    }
                }
            }
        });

        return newOpen;
    }

    public static void getAllLabels(final Container c, List<javax.swing.JLabel> labelList) {
        Component[] comps = c.getComponents();
        for (Component comp : comps) {
            if (comp instanceof Container) {
                getAllLabels((Container) comp, labelList);
            }
            if (comp instanceof javax.swing.JLabel) {
                labelList.add((javax.swing.JLabel) comp);
            }
        }
    }

    public static void getAllButtons(final Container c, List<javax.swing.JButton> buttonList) {
        Component[] comps = c.getComponents();
        for (Component comp : comps) {
            if (comp instanceof Container) {
                getAllButtons((Container) comp, buttonList);
            }
            if (comp instanceof javax.swing.JButton) {
                buttonList.add((javax.swing.JButton) comp);
            }
        }
    }

    public static void getAllPanels(final Container c, List<javax.swing.JPanel> panelList) {
        Component[] comps = c.getComponents();
        for (Component comp : comps) {
            if (comp instanceof Container) {
                getAllPanels((Container) comp, panelList);
            }
            if (comp instanceof javax.swing.JPanel) {
                panelList.add((javax.swing.JPanel) comp);
            }
        }
    }

    public static void getAllComponents(final Container c, List<javax.swing.JTextArea> textAreaList) {
        Component[] comps = c.getComponents();
        for (Component comp : comps) {
            if (comp instanceof Container) {
                getAllComponents((Container) comp, textAreaList);
            }
            if (comp instanceof javax.swing.JTextArea) {
                textAreaList.add((javax.swing.JTextArea) comp);
            }
        }
    }

    public void onServer() {
        ServerDialog sd = new ServerDialog(this);
        if (sd.doModal()) {
            connectionManager = new ServerManager(this, sd.port, sd.password, sd.name);

            networkItems[0].setEnabled(false);
            networkItems[1].setEnabled(false);
            networkItems[2].setEnabled(true);
            networkItems[3].setEnabled(true);
        }
    }

    public void onClient() {
        ClientDialog cd = new ClientDialog(this);
        if (cd.doModal()) {
            connectionManager = new ClientManager(this, cd.ip, cd.port, cd.password, cd.name);

            fileItems[0].setEnabled(false);
            fileItems[2].setEnabled(false);
            fileItems[3].setEnabled(false);
            networkItems[0].setEnabled(false);
            networkItems[1].setEnabled(false);
            networkItems[2].setEnabled(false);
            networkItems[3].setEnabled(true);
        }
    }

    public boolean insertText(int offset, String text, javax.swing.text.Document d, javax.swing.event.DocumentListener dl) {
        boolean trueOrFalse = false;
        try {
            d.removeDocumentListener(dl);
            d.insertString(offset, text, null);
            if (offset < this.text.getCaretPosition()) {
                this.text.setCaretPosition(this.text.getCaretPosition() + text.length());
            }
            trueOrFalse = true;
        } catch (Exception e) {
            System.out.println("ERROR: MainFrame::insertText()");
        } finally {
            d.addDocumentListener(dl);
        }

        return trueOrFalse;
    }

    public boolean removeText(int offset, int length, javax.swing.text.Document d, javax.swing.event.DocumentListener dl) {
        boolean trueOrFalse = false;
        try {
            d.removeDocumentListener(dl);
            d.remove(offset, length);
            if (offset < text.getCaretPosition()) {
                text.setCaretPosition(text.getCaretPosition() - length);
            }
            trueOrFalse = true;
        } catch (Exception e) {
            System.out.println("ERROR: MainFrame::removeText()");
        } finally {
            d.addDocumentListener(dl);
        }

        return trueOrFalse;
    }

    public void removeAllText(javax.swing.text.Document d, javax.swing.event.DocumentListener dl) {
        synchronized (d) {
            d.removeDocumentListener(dl);
            text.setText("");
            text.setCaretPosition(0);
            d.addDocumentListener(dl);
        }
    }

    public void killConnectionManager() {
        connectionManager.destroy();
        connectionManager = null;
        document.removeDocumentListener(documentListener);
        documentListener = null;
        userList.removeAllClients();
        fileItems[0].setEnabled(true);
        fileItems[2].setEnabled(true);
        fileItems[3].setEnabled(true);
        networkItems[0].setEnabled(true);
        networkItems[1].setEnabled(true);
        networkItems[2].setEnabled(false);
        networkItems[3].setEnabled(false);
    }

    public UserList getUserList() {
        return userList;
    }

    public javax.swing.JTextArea getText() {
        return text;
    }

    public javax.swing.text.Document getDocument() {
        return document;
    }

    public ConnectionManager getManager() {
        return connectionManager;
    }

    public javax.swing.undo.UndoManager getUndoManager() {
        return undoManager;
    }
}
