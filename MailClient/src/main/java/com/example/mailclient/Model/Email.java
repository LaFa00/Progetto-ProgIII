package com.example.mailclient.Model;

import java.io.Serializable;
import java.util.*;

public class Email implements Serializable {

    private final UUID id = UUID.randomUUID();

    //private int id;
    private String sender;
    private String subject;

    private String text;

    private List<String> receivers;

    private Date date;

    //private final Date date;


    /**
     *
     * @param sender    Mittente della email
     * @param subject   Oggetto della mail
     * @param text      Testo della mail
     * @param receivers Destinatari della mail
     */
    public Email(String sender, List<String> receivers, String subject, String text, Date date) {
        this.sender = sender;
        this.subject = subject;
        this.text = text;
        this.receivers = new ArrayList<>(receivers);
        this.date = date;
    }

    public UUID getId() {
        return id;
    }

    public String getSender() {
        return sender;
    }

    public List<String> getReceivers() {
        return receivers;
    }

    public String getSubject() {
        return subject;
    }

    public String getText() {
        return text;
    }


    @Override
    public int hashCode() {
        return Objects.hash(sender, receivers, subject, text, date);
    }

    /**
     * @return      stringa composta dagli indirizzi e-mail del mittente più destinatari
     */
    @Override
    public String toString() {
        return String.join(" - ", List.of(this.sender, this.subject));
    }
}
