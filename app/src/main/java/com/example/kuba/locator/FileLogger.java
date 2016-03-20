package com.example.kuba.locator;


import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class FileLogger implements Logger {

    File file;
    OutputStreamWriter outputStreamWriter;

    public FileLogger(String filename) {
        String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/";
        file = new File(filePath + filename);
        try {
            if (file.exists()) {
                file.delete();

                file.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(file);

            outputStreamWriter = new OutputStreamWriter(fOut);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void log(String msg) {
        try {
            outputStreamWriter.append(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void flush() throws IOException {
        outputStreamWriter.close();
    }
}
