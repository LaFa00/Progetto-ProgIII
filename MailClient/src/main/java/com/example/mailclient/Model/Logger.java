package com.example.mailclient.Model;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Logger implements Serializable {

    private Date date;

    private String client;

    private String info;

    private String op;

    private Email email;


    /**
     * @param date momento del log
     * @param client utente dal quale viene effettuato il log
     * @param info informazioni del log
     * @param op tipo di operazioni effettuata
     * @param email email considerata dal log
     * */


    public Logger(Date date, String client, String info, String op, Email email) {
        this.date = date;
        this.client = client;
        this.info = info;
        this.op = op;
        this.email = email;
        File logFolder = new File("C:\\Users\\Daniele\\Documents\\PROGETTO_SPERUMA\\MailClient\\src\\main\\resources");
        String path = File.separator + logFolder.getAbsolutePath() + File.separator + "Log";
        File fileAccount = new File(path);
        try {
            if(!fileAccount.exists() && !fileAccount.mkdirs()) {
                throw new IOException();
            }
        } catch (IOException e) {
            System.exit(1);
        }
    }

    public Date getDate() {
        return date;
    }

    public String getClient() {
        return client;
    }

    public String getInfo() {
        return info;
    }

    public String getOp() {
        return op;
    }

    public Email getEmail() {
        return email;
    }

    @Override
    public String toString() {
        DateFormat df = new SimpleDateFormat("[dd/MM/yyyy HH:mm:ss]");
        return String.join("  -  ", List.of(df.format(this.date), this.client, this.info)) + "\n";
    }
}
