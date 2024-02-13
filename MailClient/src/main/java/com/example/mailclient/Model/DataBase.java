package com.example.mailclient.Model;

import com.example.mailclient.Server.GetAccount;
import com.google.gson.GsonBuilder;
import javafx.scene.chart.PieChart;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import com.google.gson.Gson;

import static java.lang.System.exit;
import static java.util.Objects.*;

public class DataBase {
    GetAccount account = new GetAccount();
   // private final String dbPath;
   File folder;


    public DataBase() {
        folder = new File("C:\\Users\\Daniele\\Documents\\PROGETTO_SPERUMA\\MailClient\\src\\main\\resources");
        String path = File.separator + folder.getAbsolutePath() + File.separator + "Account";
        File fileAccount = new File(path);
        try {
            if(!fileAccount.exists() && !fileAccount.mkdirs()) {
                throw new IOException();
            }
        } catch (IOException e) {
            System.exit(1);
        }

    }


    public boolean addAccount(String account) {
        File folderAccount = new File("C:\\Users\\Daniele\\Documents\\PROGETTO_SPERUMA\\MailClient\\src\\main\\resources\\Account");
        String path = File.separator + folderAccount.getAbsolutePath() + File.separator + account;
        File fileAccount = new File(path);
        if (!fileAccount.mkdir()) {
            return false;

        } else {
            return true;
        }

    }

    public boolean findAccount(String account) {
        File folderAccount = new File("C:\\Users\\Daniele\\Documents\\PROGETTO_SPERUMA\\MailClient\\src\\main\\resources\\Account");
        String path = File.separator + folderAccount.getAbsolutePath() + File.separator + account;
        File fileAccount = new File(path);
        if(fileAccount.exists()) {
            return true;
        } else {
            return false;
        }

    }

    private String findEmailPath(Email email, String user){
        UUID id = email.getId();
        String dbPath = "C:\\Users\\Daniele\\Documents\\PROGETTO_SPERUMA\\MailClient\\src\\main\\resources\\Account";
        File f = new File(dbPath + File.separator + user +  File.separator +  id + ".txt");
        return f.getAbsolutePath();
    }

    public boolean addEmail(Email email,String account) {
            boolean result;
            try {
                String path = findEmailPath(email,account);
                FileOutputStream fout;
                fout = new FileOutputStream(path);
                ObjectOutputStream out = new ObjectOutputStream(fout);
                out.writeObject(email);
                out.flush();
                out.close();
                fout.close();
                result = true;


            } catch (IOException e) {
                e.printStackTrace();
                result = false;
            }
            return result;
    }

    public boolean deleteEmail(String account, Email email) throws FileNotFoundException {
        String path = findEmailPath(email,account);
        File f = new File(path);
        if(!f.exists())
            throw new FileNotFoundException();
        f.delete();
        return true;
    }

    public ArrayList<Email> getEmails(String account) {
        String dbPath = "C:\\Users\\Daniele\\Documents\\PROGETTO_SPERUMA\\MailClient\\src\\main\\resources\\Account";
        File emails = new File(dbPath + File.separator + account);
        File[] emailsFiles = new File(dbPath + File.separator + account).listFiles();
        FileInputStream fin;
        ObjectInputStream obj;
        ArrayList<Email> emails1 = new ArrayList<>();
        try{
            if(emailsFiles != null) {
                int i = 0;
                for(File file : Objects.requireNonNull(emailsFiles)) {
                    fin = new FileInputStream(file);
                    obj = new ObjectInputStream(fin);
                    emails1.add((Email) obj.readObject());
                    obj.close();
                    fin.close();
                }
                return emails1;
            }

        } catch (ClassNotFoundException | IOException |
                 NullPointerException e) {
            e.printStackTrace();
        }
        return emails1;
    }







}
