package com.javarush.task.task32.task3209;

import javafx.stage.FileChooser;
import javafx.stage.Window;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import java.io.*;

public class Controller {
    private View view;
    private HTMLDocument document;
    private File currentFile;

    public HTMLDocument getDocument() {
        return document;
    }

    public void init(){
        createNewDocument();
    }

    public void exit(){
        System.exit(0);
    }

    public Controller(View view) {
        this.view = view;
    }

    public void resetDocument(){
        if(document != null) {
            document.removeUndoableEditListener(view.getUndoListener());
        }
        HTMLEditorKit htmlEditorKit = new HTMLEditorKit();
        Document defaultDocument = htmlEditorKit.createDefaultDocument();
        document = (HTMLDocument) defaultDocument;
        document.addUndoableEditListener(view.getUndoListener());
        view.update();

    }

    public void setPlainText(String text){
        resetDocument();
        StringReader stringReader = new StringReader(text);
        try {
            new HTMLEditorKit().read(stringReader, document, 0);
        } catch (Exception e) {
            ExceptionHandler.log(e);
        }
    }

    public String getPlainText() {
        StringWriter stringWriter = new StringWriter();
        try{
            new HTMLEditorKit().write(stringWriter, document, 0, document.getLength());

        }catch (Exception e){
            ExceptionHandler.log(e);
        }
        return stringWriter.toString();
    }

    public static void main(String[] args) {
        View view = new View();
        Controller controller = new Controller(view);
        view.setController(controller);
        view.init();
        controller.init();

    }

    public void createNewDocument() {
        view.selectHtmlTab();
        resetDocument();
        view.setTitle("HTML редактор");
        currentFile = null;
    }

    public void openDocument() {
        view.selectHtmlTab();
        JFileChooser jFileChooser = new JFileChooser();
        jFileChooser.setFileFilter(new HTMLFileFilter());
        jFileChooser.setDialogTitle("Open File");
        int choose = jFileChooser.showOpenDialog(view);
        if(choose == JFileChooser.APPROVE_OPTION){
            currentFile = jFileChooser.getSelectedFile();
            resetDocument();
            view.setTitle(currentFile.getName());
            try(FileReader fileReader = new FileReader(currentFile)){
                new HTMLEditorKit().read(fileReader, document, 0);
                view.resetUndo();
            }catch (IOException | BadLocationException e){
                ExceptionHandler.log(e);
            }
        }
    }

    public void saveDocument() {
        view.selectHtmlTab();
        if(currentFile != null){
                try(FileWriter fileWriter = new FileWriter(currentFile)){
                    new HTMLEditorKit().write(fileWriter, document, 0, document.getLength());
                }catch (IOException | BadLocationException e){
                    ExceptionHandler.log(e);
                }
        }else {
            saveDocumentAs();
        }
    }

    public void saveDocumentAs() {
        view.selectHtmlTab();
        JFileChooser jfileChooser = new JFileChooser();
        jfileChooser.setFileFilter(new HTMLFileFilter());
        jfileChooser.setDialogTitle("Save File");
        int choose = jfileChooser.showSaveDialog(view);
        if(choose == JFileChooser.APPROVE_OPTION){
            currentFile = jfileChooser.getSelectedFile();
            view.setTitle(currentFile.getName());
            try {
                FileWriter fileWriter = new FileWriter(currentFile);
                new HTMLEditorKit().write(fileWriter, document, 0, document.getLength());
                fileWriter.flush();
                fileWriter.close();
            } catch (IOException | BadLocationException e) {
                ExceptionHandler.log(e);
            }
        }
    }
}
