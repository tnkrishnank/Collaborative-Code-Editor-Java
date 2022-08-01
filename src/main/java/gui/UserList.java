package colabcode.gui;

import javax.swing.DefaultListModel;
import javax.swing.JList;

public class UserList extends JList {

    private static DefaultListModel listModel = new DefaultListModel();

    public UserList() {
        super(listModel);
    }

    public void addClient(String name) {
        listModel.addElement(name);
    }

    public void removeClient(String name) {
        listModel.removeElement(name);
    }

    public void removeAllClients() {
        listModel.removeAllElements();
    }
}
