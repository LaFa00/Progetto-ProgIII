package com.example.mailclient.Model;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.LinkedList;
import java.util.List;

public class Account {


        private final ListProperty<Email> inbox;
        private final ObservableList<Email> inboxContent;
        private final String emailAddress;

        /**
         * Costruttore della classe.
         *
         * @param emailAddress indirizzo email
         */

        public Account (String emailAddress) {
            this.inboxContent = FXCollections.observableList(new LinkedList<>());
            this.inbox = new SimpleListProperty<>();
            this.inbox.set(inboxContent);
            this.emailAddress = emailAddress;
        }

        /**
         * @return lista di email
         */
        public ListProperty<Email> inboxProperty() {
            return inbox;
        }



        /**
         * @return elimina l'email specificata
         */
        public void deleteEmail(Email email) {
            inboxContent.remove(email);
        }

        /**
         * Riceve una lista di email e la salva nella casella
         */

        public void saveEmails(List<Email> emails) {
            inboxContent.addAll(emails);
        }

        /**
         *
         */

        public String getEmailAddress() {
            return new String(emailAddress);
        }
    }

