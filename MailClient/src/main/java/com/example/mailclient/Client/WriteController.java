package com.example.mailclient.Client;

import com.example.mailclient.Model.Account;
import com.example.mailclient.Model.Email;
import com.example.mailclient.Model.Logger;
import com.example.mailclient.Model.NoWriteObjectOutputStream;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class WriteController {

    @FXML
    private BorderPane writeBorderPane;
    @FXML
    private TextField senderText;

    @FXML
    private TextField subjectText;

    @FXML
    private TextArea emailText;

    @FXML
    private Button cancelBtn;

    @FXML
    private SplitPane sp;

    private Account account;

    private Socket socket;

    public void setParent(SplitPane sp) {

        this.sp = sp;
    }

    public void setAccount(Account account) {

        this.account = account;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    @FXML
    public void onSendClick(MouseEvent event) throws IOException {
        List<String> receivers = new ArrayList<>(
                List.of(senderText.getText().replaceAll("[ ]", "").split(",")));

        for(String rec : receivers) {
            if( !rec.matches("^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$") ){
                new LoginController().loadAlertStage("Destination email syntax wrong!");
                return;
            }
        }

        if(subjectText.getText().equals("")) {
           new LoginController().loadAlertStage("Oggetto non può essere vuoto");
            return;
        }
        if(!socket.isOutputShutdown()) {
            NoWriteObjectOutputStream outputStream = new NoWriteObjectOutputStream(socket.getOutputStream());
            outputStream.flush();
            Email sendEmail = new Email(account.getEmailAddress(),receivers,subjectText.getText(),emailText.getText(),new Date());
            outputStream.writeObject(new Logger(new Date(), account.getEmailAddress(), "Sending email", "send", sendEmail));
            outputStream.flush();
        }
    }

    @FXML
    public void onNoSendClick(MouseEvent mouseEvent) {
        sp.getItems().remove(1);
    }


    public void replyEmail(Email email) {
        senderText.setText(email.getSender() + ",");
        subjectText.setText("Re: " + email.getSubject());
        emailText.setText("\n\n\n---------- Replied message ---------\n" + email.getText());
        senderText.setEditable(false);
        subjectText.setEditable(false);
    }

    public void replyAllEmail(Email email) {
        List<String> repliers = new ArrayList<>();
        for (String replier : email.getReceivers()) {
            if (!replier.equals(account.getEmailAddress())) {
                repliers.add(replier);
            }
        }
        senderText.setText(email.getSender() + ", ");
        subjectText.setText("Re: " + email.getSubject());
        emailText.setText("\n\n\n---------- Replied message ---------\n" + email.getText());
        senderText.setEditable(false);
        subjectText.setEditable(false);
    }

    public void forwardEmail(Email email){
        subjectText.setText("Fo: " + email.getSubject());
        emailText.setText("\n\r\n\r\n\r---------- Forwarded message ---------\n\r" + email.getText());
        subjectText.setEditable(false);
    }


}
