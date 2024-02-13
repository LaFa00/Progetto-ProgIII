package com.example.mailclient.Server;

import com.example.mailclient.Client.LoginController;
import com.example.mailclient.Model.*;
import javafx.application.Platform;
import javafx.scene.control.TextArea;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class ServerHandler implements Runnable {
    private Socket socket;

    private TextArea logText;

    private List<Runnable> runnableList;

    private String client;

    public ServerHandler(Socket socket, TextArea logTxt, List<Runnable> runnableList) {
        this.socket = socket;
        this.logText = logTxt;
        this.runnableList = runnableList;
    }

    public String getAccount(){
        return client;
    }

    public Socket getSocket(){
        return socket;
    }


    public void run() {
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            outputStream.flush();
            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
            while(!socket.isClosed()) {
                Logger log = (Logger) inputStream.readObject();
                appendToTxtArea(log);
                switch (log.getOp()) {
                    case "login" -> {
                        loginHandler(outputStream, inputStream, log);
                    }

                    case "send" -> {
                        sendHandler(outputStream, log);
                    }

                    case "delete" -> {
                        deleteHandler(log,log.getEmail());
                    }

                    case "exit" -> {
                        socket.close();
                    }
                }
            }

        } catch (IOException | ClassNotFoundException e) {
            try {
                socket.close();
            } catch (IOException err) {
                err.getMessage();
            }
        }

    }

    private void loginHandler(ObjectOutputStream outputStream, ObjectInputStream inputStream, Logger log) throws IOException, ClassNotFoundException {
        GetAccount accounts = new GetAccount();
        ArrayList<String> user = accounts.getAllAccounts();
        if(!user.contains(log.getClient())) {
            outputStream.writeBoolean(false);
            outputStream.flush();
            appendToTxtArea(new Logger(new Date(), "SERVER", log.getClient() + " Non esiste",
                    null, null));
            inputStream.readObject(); //garbage
            //return;
        } else {
            DataBase db = new DataBase();
            db.addAccount(log.getClient());
            outputStream.writeBoolean(true);
            outputStream.flush();
            appendToTxtArea(new Logger(new Date(), "SERVER", log.getClient() + " Login eseguito con successo",
                        null, null));
            outputStream.writeObject(db.getEmails(log.getClient()));
            outputStream.flush();

        }

    }

    private void sendHandler(ObjectOutputStream outputStream, Logger log) throws IOException {
        Email emailToSend = log.getEmail();
        List<String> receivers = emailToSend.getReceivers();
        for(String r: receivers) {
            GetAccount account = new GetAccount();
            ArrayList<String> user = account.getAllAccounts();
            DataBase db = new DataBase();
            if(db.findAccount(r) == false) {
                appendToTxtArea(new Logger(new Date(), "SERVER", log.getClient() + " Invio fallito perché alcuni destinatari non esistono.",
                        null, null));
                outputStream.writeObject(new String("notSend"));
                outputStream.flush();
                return;
            }
        }
        if(emailToSend != null) {
            DataBase db = new DataBase();
            for(String receiver: emailToSend.getReceivers()) {
                db.addEmail(emailToSend, receiver);
            }
            appendToTxtArea(new Logger(new Date(), "SERVER", log.getClient() + "Invio avvenuto con successo.",
                    null, null));
            outputStream.writeObject(new String("yesSend"));
            outputStream.flush();
        }
    }

    private void deleteHandler(Logger log, Email email) {
        try {
            DataBase db = new DataBase();
            db.deleteEmail(log.getClient(), email);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void appendToTxtArea(Logger log) {
        new Thread(()-> Platform.runLater(() -> {
            logText.appendText(log.toString());
        })).start();
    }




}
