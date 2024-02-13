package com.example.mailclient.Client;

import com.example.mailclient.Model.Logger;
import com.example.mailclient.Server.GetAccount;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;

public class LoginController {

    @FXML
    TextField account;

    @FXML
    private Label alertLbl;

    @FXML
    private Button alertBtn;

    @FXML
    private Button login;

    private Socket socket;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;


    private void initSocket() throws IOException {
        if (socket == null || socket.isClosed()) {
            socket = new Socket("127.0.0.1", 8080);
            inputStream = new ObjectInputStream(socket.getInputStream());
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            outputStream.flush();
        }
    }
    @FXML
    protected void loginClick() throws IOException {
        if(!account.getText().matches("^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$")) {
            loadAlertStage("Sintassi email sbagliata!");
        } else {
            try {
                initSocket();
                outputStream.writeObject(new Logger(new Date(), account.getText(),"Tentativo di login", "login", null));
                outputStream.flush();
                if(inputStream.readBoolean()) {
                    Stage mailStage = new Stage();
                    FXMLLoader inboxLoader = new FXMLLoader(getClass().getResource("/com/example/mailclient/inbox.fxml"));
                    mailStage.setTitle("Mail Box");
                    mailStage.setScene(new Scene(inboxLoader.load()));
                    mailStage.show();
                    InboxController inboxController = inboxLoader.getController();
                    inboxController.setAccount(account.getText());
                    inboxController.setSocket(socket);
                    inboxController.initBox();
                    Stage logInStage = (Stage) login.getScene().getWindow();
                    logInStage.close();

                } else {
                    loadAlertStage("Account non esistente");
                    socket.close();
                }


            } catch (ConnectException e) {
                loadAlertStage("Server disconnesso");
            }
        }
    }

    public void loadAlertStage(String msg) throws IOException {
        Stage alertStage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/mailclient/alert.fxml"));
        alertStage.setScene(new Scene(fxmlLoader.load()));
        LoginController loginController = fxmlLoader.getController();
        loginController.setAlertLbl(msg);
        alertStage.show();
    }

    @FXML
    protected void onAlertClick() {
        Stage alertStage = (Stage) alertBtn.getScene().getWindow();
        alertStage.close();
    }

    public void setAlertLbl(String msg) {
        alertLbl.setText(msg);
    }
}
