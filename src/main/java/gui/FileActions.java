package colabcode.gui;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import javax.swing.JTextArea;

public class FileActions {

    public boolean saveDocument(String fileName, JTextArea textArea) {
        boolean trueOrFalse = false;
        StringBuffer textBuffer = new StringBuffer(textArea.getText());
        try {
            PrintWriter outFile = new PrintWriter(new BufferedWriter(new FileWriter(fileName)));
            outFile.print(textBuffer.toString() + "\n");
            outFile.close();
            trueOrFalse = true;
        } catch (IOException e) {
            System.out.println("ERROR: FileActions::saveDocument()");
        }

        return trueOrFalse;
    }

    public StringBuffer openDocument(String fileName) {
        StringBuffer returnBuffer = new StringBuffer();
        try {
            BufferedReader in = new BufferedReader(new FileReader(fileName));
            try {
                String line;
                while ((line = in.readLine()) != null) {
                    returnBuffer.append(line).append("\n");
                }
                in.close();
            } catch (IOException e1) {
                System.out.println("ERROR: FileActions::openDocument()");
            }
        } catch (FileNotFoundException e) {
            System.out.println("ERROR: FileActions::openDocument()");
        }

        return returnBuffer;
    }
}
