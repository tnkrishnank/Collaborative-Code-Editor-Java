package colabcode.gui;

import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;

import colabcode.connect.ConnectionManager;

public class DocListener implements DocumentListener {

    ConnectionManager connectionManager;
    JTextArea text;

    public DocListener(ConnectionManager cm, JTextArea textArea) {
        connectionManager = cm;
        text = textArea;
    }

    @Override
    public void insertUpdate(DocumentEvent docEvent) {
        try {
            connectionManager.sendInsert(docEvent.getOffset(), text.getText(docEvent.getOffset(), docEvent.getLength()));
        } catch (BadLocationException e) {
            System.out.println("ERROR: DocListener::insertUpdate()");
        }
    }

    @Override
    public void removeUpdate(DocumentEvent docEvent) {
        try {
            connectionManager.sendRemove(docEvent.getOffset(), docEvent.getLength());
        } catch (Exception e) {
            System.out.println("ERROR: DocListener::removeUpdate()");
        }
    }

    @Override
    public void changedUpdate(DocumentEvent docEvent) {
        try {

        } catch (Exception e) {
            System.out.println("ERROR: DocListener::changedUpdate()");
        }
    }
}
