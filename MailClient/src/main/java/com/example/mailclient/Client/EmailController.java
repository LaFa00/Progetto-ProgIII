package com.example.mailclient.Client;

import com.example.mailclient.Model.Account;
import com.example.mailclient.Model.Email;
import com.example.mailclient.Model.Logger;
import com.example.mailclient.Model.NoWriteObjectOutputStream;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.net.Socket;
import java.util.Date;

public class EmailController {

    @FXML
    private SplitPane windowSP;
    @FXML
    private Label fromLabel;
    @FXML
    private Label toLbl;
    @FXML
    private Label subjectLabel;
    @FXML
    private TextArea emailContentTxt;

    private Account account;

    private Email email;

    private Socket socket;

    public void setParent(SplitPane sp) {
        this.windowSP = sp;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public void setEmail(Email email) {
        this.email = email;
        if(email != null) {
            fromLabel.setText(email.getSender());
            toLbl.setText(String.join("; ", email.getReceivers()));
            subjectLabel.setText(email.getSubject());
            emailContentTxt.setText(email.getText());
        }
    }

    @FXML
    protected void onReplyClick() {
        try {
            WriteController writeController = writeStage();
            writeController.replyEmail(email);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @FXML
    protected void onReplyAllClick() {
        try {
            WriteController writeController = writeStage();
            writeController.replyAllEmail(email);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @FXML
    protected void onForwardClick() {
        try {
            WriteController writeController = writeStage();
            writeController.forwardEmail(email);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @FXML
    public void onDeleteClick(MouseEvent mouseEvent) throws IOException {
        if(!socket.isOutputShutdown()) {
            NoWriteObjectOutputStream outputStream = new NoWriteObjectOutputStream(socket.getOutputStream());
            outputStream.writeObject(new Logger(new Date(),account.getEmailAddress(),"Delete email id: " + email.getId(),"delete",email));
            account.deleteEmail(email);
            windowSP.getItems().remove(1);
        }
    }

    private WriteController writeStage() throws IOException {

        FXMLLoader replyLoader = new FXMLLoader(getClass().getResource("/com/example/mailclient/write.fxml"));
        windowSP.getItems().set(1, replyLoader.load());

        WriteController writeController = replyLoader.getController();
        writeController.setParent(windowSP);
        writeController.setAccount(account);
        writeController.setSocket(socket);
        return writeController;
    }






}
