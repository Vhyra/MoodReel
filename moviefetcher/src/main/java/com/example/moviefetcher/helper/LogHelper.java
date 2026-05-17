package com.example.moviefetcher.helper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.io.File;

public class LogHelper {

    private String filePath = "log" + File.separator + "log.txt";
    private static LogHelper instance;
    private static final DateTimeFormatter FORMATTER = 
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private LogHelper(){
        try {
            Files.createDirectories(Path.of(filePath).getParent());
        } catch (IOException e) {
            System.err.println("Errore creazione cartella log: " + e.getMessage());
        }
    }

    public static LogHelper getInstance(){
        if(instance == null){
            instance = new LogHelper();
        }
        return instance;
    }

    public void writeLog(String log){
        String line = LocalDateTime.now().format(FORMATTER) + " ------ " + log;
        try{
            Files.writeString(Path.of(filePath), line+"\n", StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        }catch(Exception e){
            System.err.println("IO error: " + e.toString());
        }
    }

    
}
