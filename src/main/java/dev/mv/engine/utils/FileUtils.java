package dev.mv.engine.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;

public class FileUtils {

    public static String loadShaderFile(URI file) {
        if (!(new File(file).exists())) {
            System.out.println("Could not load file: \"" + file  + "\"!");
            return null;
        }
        StringBuilder string = new StringBuilder();
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(new File(file)));
            String line;
            while((line = reader.readLine()) != null) {
                string.append(line);
                string.append("\n");
            }
            reader.close();
        }catch(IOException e) {
            e.printStackTrace();
        }
        //System.out.println(string.toString());
        return string.toString();
    }

}
