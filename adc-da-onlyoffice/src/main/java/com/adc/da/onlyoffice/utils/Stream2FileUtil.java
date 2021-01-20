package com.adc.da.onlyoffice.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class Stream2FileUtil {

    public static void doSave(InputStream stream, File savedFile) throws Exception{
        try (FileOutputStream out = new FileOutputStream(savedFile)) {
            int read;
            final byte[] bytes = new byte[1024];
            while ((read = stream.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
            out.flush();
        }
    }
}
