package com.example.mailclient.Server;

import java.io.*;
import java.util.ArrayList;

public class GetAccount {


    ArrayList<String> accounts = new ArrayList<>();
    File file = new File("C:\\Users\\Daniele\\Documents\\PROGETTO_SPERUMA\\MailClient\\src\\main\\resources\\Accounts.txt");


    BufferedReader br;

    {
        try {
            br = new BufferedReader(new FileReader(file));
            String account;
            int i = 0;
            while ((account = br.readLine()) != null) {
                accounts.add(account);
                i++;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<String> getAllAccounts() {
        return accounts;
    }
}
