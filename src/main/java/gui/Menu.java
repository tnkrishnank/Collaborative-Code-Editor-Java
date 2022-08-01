package colabcode.gui;

import java.awt.Container;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JTextArea;

public class Menu extends javax.swing.JMenuBar {

    private MainFrame mainFrame;
    private javax.swing.JMenuItem[] fileItems;
    private javax.swing.JMenuItem[] editItems;
    private javax.swing.JMenuItem[] networkItems;
    private javax.swing.undo.UndoManager undoManager;

    private javax.swing.JMenu menuFile;
    private javax.swing.JMenuItem mNewFile;
    public static javax.swing.JMenuItem mNewTerminal;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JMenuItem mOpenFile;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    private javax.swing.JMenuItem mSave;
    private javax.swing.JMenuItem mSaveAs;
    private javax.swing.JPopupMenu.Separator jSeparator3;
    private javax.swing.JMenuItem mExit;
    private javax.swing.JMenu menuEdit;
    private javax.swing.JMenuItem mUndo;
    private javax.swing.JMenuItem mRedo;
    private javax.swing.JPopupMenu.Separator jSeparator4;
    private javax.swing.JMenuItem mCut;
    private javax.swing.JMenuItem mCopy;
    private javax.swing.JMenuItem mPaste;
    private javax.swing.JMenu menuNetwork;
    private javax.swing.JMenuItem mNewServer;
    private javax.swing.JMenuItem mConnect;
    private javax.swing.JPopupMenu.Separator jSeparator5;
    private javax.swing.JMenuItem mShareFile;
    private javax.swing.JMenuItem mDisconnect;
    private javax.swing.JMenu menuAbout;
    private javax.swing.JMenuBar tempMenuBar;

    private javax.swing.text.Document document;
    private javax.swing.event.DocumentListener documentListener;

    public Menu(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        try {
            tempMenuBar = initMenu();
            this.add(tempMenuBar);
        } catch (Exception e) {
            System.out.println("ERROR: Menu::Menu()");
        }
    }

    private javax.swing.JMenuBar initMenu() throws Exception {
        tempMenuBar = new javax.swing.JMenuBar();
        undoManager = new javax.swing.undo.UndoManager();
        menuFile = new javax.swing.JMenu();
        mNewFile = new javax.swing.JMenuItem();
        mNewTerminal = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        mOpenFile = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        mSave = new javax.swing.JMenuItem();
        mSaveAs = new javax.swing.JMenuItem();
        jSeparator3 = new javax.swing.JPopupMenu.Separator();
        mExit = new javax.swing.JMenuItem();
        menuEdit = new javax.swing.JMenu();
        mUndo = new javax.swing.JMenuItem();
        mRedo = new javax.swing.JMenuItem();
        jSeparator4 = new javax.swing.JPopupMenu.Separator();
        mCut = new javax.swing.JMenuItem();
        mCopy = new javax.swing.JMenuItem();
        mPaste = new javax.swing.JMenuItem();
        menuNetwork = new javax.swing.JMenu();
        mNewServer = new javax.swing.JMenuItem();
        mConnect = new javax.swing.JMenuItem();
        jSeparator5 = new javax.swing.JPopupMenu.Separator();
        mShareFile = new javax.swing.JMenuItem();
        mDisconnect = new javax.swing.JMenuItem();
        menuAbout = new javax.swing.JMenu();

        menuFile.setText("File");

        mNewFile.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        mNewFile.setText("New File");
        mNewFile.addActionListener((java.awt.event.ActionEvent evt) -> {
            mNewFileActionPerformed(evt);
        });
        menuFile.add(mNewFile);

        mNewTerminal.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_T, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        mNewTerminal.setText("New Terminal");
        mNewTerminal.addActionListener((java.awt.event.ActionEvent evt) -> {
            mNewTerminalActionPerformed(evt);
        });
        menuFile.add(mNewTerminal);

        menuFile.add(jSeparator1);

        mOpenFile.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        mOpenFile.setText("Open File");
        mOpenFile.addActionListener((java.awt.event.ActionEvent evt) -> {
            mOpenFileActionPerformed(evt);
        });
        menuFile.add(mOpenFile);

        menuFile.add(jSeparator2);

        mSave.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        mSave.setText("Save");
        mSave.addActionListener((java.awt.event.ActionEvent evt) -> {
            mSaveActionPerformed(evt);
        });
        menuFile.add(mSave);

        mSaveAs.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.SHIFT_DOWN_MASK | java.awt.event.InputEvent.CTRL_DOWN_MASK));
        mSaveAs.setText("Save As...");
        mSaveAs.addActionListener((java.awt.event.ActionEvent evt) -> {
            mSaveAsActionPerformed(evt);
        });
        menuFile.add(mSaveAs);

        menuFile.add(jSeparator3);

        mExit.setText("Exit");
        mExit.addActionListener((java.awt.event.ActionEvent evt) -> {
            mExitActionPerformed(evt);
        });
        menuFile.add(mExit);

        tempMenuBar.add(menuFile);

        menuEdit.setText("Edit");

        mUndo.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Z, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        mUndo.setText("Undo");
        mUndo.addActionListener((java.awt.event.ActionEvent evt) -> {
            mUndoActionPerformed(evt);
        });
        menuEdit.add(mUndo);

        mRedo.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Y, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        mRedo.setText("Redo");
        mRedo.addActionListener((java.awt.event.ActionEvent evt) -> {
            mRedoActionPerformed(evt);
        });
        menuEdit.add(mRedo);

        menuEdit.add(jSeparator4);

        mCut.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_X, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        mCut.setText("Cut");
        mCut.addActionListener((java.awt.event.ActionEvent evt) -> {
            mCutActionPerformed(evt);
        });
        menuEdit.add(mCut);

        mCopy.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        mCopy.setText("Copy");
        mCopy.addActionListener((java.awt.event.ActionEvent evt) -> {
            mCopyActionPerformed(evt);
        });
        menuEdit.add(mCopy);

        mPaste.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_V, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        mPaste.setText("Paste");
        mPaste.addActionListener((java.awt.event.ActionEvent evt) -> {
            mPasteActionPerformed(evt);
        });
        menuEdit.add(mPaste);

        tempMenuBar.add(menuEdit);

        menuNetwork.setText("Network");

        mNewServer.setText("New Server");
        mNewServer.addActionListener((java.awt.event.ActionEvent evt) -> {
            mNewServerActionPerformed(evt);
        });
        menuNetwork.add(mNewServer);

        mConnect.setText("Connect");
        mConnect.addActionListener((java.awt.event.ActionEvent evt) -> {
            mConnectActionPerformed(evt);
        });
        menuNetwork.add(mConnect);

        menuNetwork.add(jSeparator5);

        mShareFile.setText("Share File");
        mShareFile.addActionListener((java.awt.event.ActionEvent evt) -> {
            mShareFileActionPerformed(evt);
        });
        menuNetwork.add(mShareFile);
        mShareFile.setEnabled(false);

        mDisconnect.setText("Disconnect");
        mDisconnect.addActionListener((java.awt.event.ActionEvent evt) -> {
            mDisconnectActionPerformed(evt);
        });
        menuNetwork.add(mDisconnect);
        mDisconnect.setEnabled(false);

        tempMenuBar.add(menuNetwork);

        menuAbout.setText("About");
        tempMenuBar.add(menuAbout);

        fileItems = new javax.swing.JMenuItem[6];
        fileItems[0] = mNewFile;
        fileItems[1] = mNewTerminal;
        fileItems[2] = mOpenFile;
        fileItems[3] = mSave;
        fileItems[4] = mSaveAs;
        fileItems[5] = mExit;

        editItems = new javax.swing.JMenuItem[6];
        editItems[0] = mUndo;
        editItems[1] = mRedo;
        editItems[2] = mCut;
        editItems[3] = mCopy;
        editItems[4] = mPaste;

        networkItems = new javax.swing.JMenuItem[4];
        networkItems[0] = mNewServer;
        networkItems[1] = mConnect;
        networkItems[2] = mShareFile;
        networkItems[3] = mDisconnect;

        return tempMenuBar;
    }

    private void mNewFileActionPerformed(java.awt.event.ActionEvent evt) {
        mainFrame.getDocument().removeUndoableEditListener(mainFrame.getUndoManager());
        MainFrame.untitledId += 1;
        String fName = "Untitled" + Integer.toString(MainFrame.untitledId);
        MainFrame.addNewTab(fName, "", "");
        mainFrame.getDocument().addUndoableEditListener(mainFrame.getUndoManager());
    }

    private void mNewTerminalActionPerformed(java.awt.event.ActionEvent evt) {
        mainFrame.getDocument().removeUndoableEditListener(mainFrame.getUndoManager());
        MainFrame.terminalId += 1;
        String fName = "Terminal" + Integer.toString(MainFrame.terminalId);
        MainFrame.addNewTerminal(fName);
        mainFrame.getDocument().addUndoableEditListener(mainFrame.getUndoManager());
        mNewTerminal.setEnabled(false);
    }

    private void mOpenFileActionPerformed(java.awt.event.ActionEvent evt) {
        final FileActions fileActions = new FileActions();
        final javax.swing.JFileChooser chooser = new javax.swing.JFileChooser();
        int returnVal = chooser.showOpenDialog(mainFrame);
        if (returnVal == javax.swing.JFileChooser.APPROVE_OPTION) {
            mainFrame.getDocument().removeUndoableEditListener(mainFrame.getUndoManager());
            String fName = chooser.getSelectedFile().getName();
            MainFrame.addNewTab(fName, fileActions.openDocument(chooser.getSelectedFile().getPath()).toString(), chooser.getSelectedFile().getPath());
            mainFrame.getDocument().addUndoableEditListener(mainFrame.getUndoManager());
        }
    }

    private void mSaveActionPerformed(java.awt.event.ActionEvent evt) {
        final FileActions fileActions = new FileActions();
        int index = MainFrame.tabEditor.getSelectedIndex();
        List<javax.swing.JTextArea> textAreaList = new ArrayList<>();
        MainFrame.getAllComponents(MainFrame.tabEditor, textAreaList);
        File f = new File(MainFrame.filePath.get(index));
        if ((!"".equals(MainFrame.filePath.get(index))) && f.exists()) {
            String fileName = MainFrame.filePath.get(index);
            fileActions.saveDocument(fileName, textAreaList.get(index));
        } else {
            mSaveAsActionPerformed(evt);
        }
    }

    private void mSaveAsActionPerformed(java.awt.event.ActionEvent evt) {
        final FileActions fileActions = new FileActions();
        final javax.swing.JFileChooser chooser = new javax.swing.JFileChooser();
        int returnVal = chooser.showSaveDialog(mainFrame);
        int index = MainFrame.tabEditor.getSelectedIndex();
        List<javax.swing.JTextArea> textAreaList = new ArrayList<>();
        MainFrame.getAllComponents(MainFrame.tabEditor, textAreaList);
        if (returnVal == javax.swing.JFileChooser.APPROVE_OPTION) {
            String fileName = chooser.getSelectedFile().getPath();
            if (!fileActions.saveDocument(fileName, textAreaList.get(index))) {
                javax.swing.JOptionPane.showMessageDialog(mainFrame, "UNABLE TO SAVE TO SPECIFIED LOCATION !");
            } else {
                MainFrame.filePath.set(index, fileName);
                String newFName = "";
                String oldFName = MainFrame.filePath.get(index);
                for (int k = (oldFName.length()) - 1; k >= 0; k--) {
                    if (oldFName.charAt(k) == '\\') {
                        break;
                    } else {
                        newFName = oldFName.charAt(k) + newFName;
                    }
                }
                MainFrame.tabEditor.setTitleAt(index, newFName);
                List<javax.swing.JLabel> labelList = new ArrayList<>();
                MainFrame.getAllLabels(MainFrame.tabEditor, labelList);
                labelList.get(index).setText(newFName);
            }
        }
    }

    private void mExitActionPerformed(java.awt.event.ActionEvent evt) {
        if (mainFrame.getManager() != null) {
            mainFrame.killConnectionManager();
        }
        System.exit(0);
    }

    private void mUndoActionPerformed(java.awt.event.ActionEvent evt) {
        if (undoManager.canUndo()) {
            undoManager.undo();
        }
    }

    private void mRedoActionPerformed(java.awt.event.ActionEvent evt) {
        if (undoManager.canRedo()) {
            undoManager.redo();
        }
    }

    private void mCutActionPerformed(java.awt.event.ActionEvent evt) {

    }

    private void mCopyActionPerformed(java.awt.event.ActionEvent evt) {

    }

    private void mPasteActionPerformed(java.awt.event.ActionEvent evt) {

    }

    private void mNewServerActionPerformed(java.awt.event.ActionEvent evt) {
        mainFrame.onServer();
    }

    private void mConnectActionPerformed(java.awt.event.ActionEvent evt) {
        mainFrame.onClient();
    }

    private void mShareFileActionPerformed(java.awt.event.ActionEvent evt) {
        Container focus = (Container) MainFrame.tabEditor.getSelectedComponent();
        List<JTextArea> textAreaList = new ArrayList<>();
        MainFrame.getAllComponents(focus, textAreaList);
        String defaultText = textAreaList.get(0).getText();
        document = textAreaList.get(0).getDocument();
        documentListener = new DocListener(mainFrame.connectionManager, textAreaList.get(0));
        document.addDocumentListener(documentListener);
        mainFrame.document = document;
        mainFrame.documentListener = documentListener;
        mainFrame.connectionManager.onShare(defaultText, document, documentListener);
        mShareFile.setEnabled(false);
    }

    private void mDisconnectActionPerformed(java.awt.event.ActionEvent evt) {
        mainFrame.killConnectionManager();
    }

    public javax.swing.JMenuItem[] getNetworkItems() {
        return networkItems;
    }

    public javax.swing.JMenuItem[] geteditItems() {
        return editItems;
    }

    public javax.swing.JMenuItem[] getfileItems() {
        return fileItems;
    }

    public javax.swing.undo.UndoManager getUndoManager() {
        return undoManager;
    }
}
