package com.javarush.task.task32.task3209;

import javax.swing.filechooser.FileFilter;
import java.io.File;

public class HTMLFileFilter extends FileFilter {
    @Override
    public boolean accept(File f) {
        if(f.isDirectory()){
            return true;
        }
        String extension = f.getName().substring(f.getName().lastIndexOf(".") + 1);
        return extension.equalsIgnoreCase("html") || extension.equalsIgnoreCase("htm");
    }

    @Override
    public String getDescription() {
        return "HTML и HTM файлы";
    }
}
