package com.example.mailclient.Client;

import com.example.mailclient.Model.*;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SplitPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;
import java.util.Date;
import java.util.List;

public class InboxController {

    @FXML
    private ImageView accountImage;

    @FXML
    private Label accountLbl;

    @FXML
    public Button exitBtn;
    @FXML
    public Button writeBtn;

    @FXML
    private ListView<Email> emailListView;
    @FXML
    private SplitPane windowSP;

    private Account account;

    private List<Email> newEmails;

    private ObjectInputStream inputStream;

    private ObjectOutputStream outputStream;
    private Socket socket;

    private WriteController writeController;

    public void setAccount(String email) {
        accountLbl.setText(email);
        account = new Account (email);
        //binding between emailListView and ObservableList<Email> in account
        emailListView.itemsProperty().bind(account.inboxProperty());
        emailListView.setOnMouseClicked(this::showSelectedEmail);
    }

    public void setSocket (Socket socket) {
        this.socket = socket;
    }

    public void initBox() {
        try {
            inputStream = new NoReadObjectInputStream(socket.getInputStream());
            outputStream = new NoWriteObjectOutputStream(socket.getOutputStream());
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        new Thread(()-> {
            boolean start = true;
            while(!socket.isClosed()) {
                try{
                    try{
                        Object input = inputStream.readObject();
                        if(input instanceof String) {
                            switch ((String) input) {
                                case "disconnected"-> {
                                    inboxHandler("dis");
                                    boolean tryReconnect = true;
                                    int times = 1;
                                    socket.shutdownOutput();
                                    while (tryReconnect && !socket.isClosed()) {
                                        try {
                                            Thread.sleep(5000);
                                            socket = new Socket("127.0.0.1", 8080); //Cerca di riconnettersi ogni 5 secondi
                                            tryReconnect = false;
                                            inputStream = new ObjectInputStream(socket.getInputStream());
                                            outputStream = new ObjectOutputStream(socket.getOutputStream());
                                            outputStream.flush();
                                            inboxHandler("con");
                                            System.out.println("reconnected");
                                            outputStream.writeObject(new Logger(new Date(), account.getEmailAddress(), "Riconnesso",
                                                    "reco", null));
                                            outputStream.flush();

                                            if (writeController != null)
                                                writeController.setSocket(socket);

                                        } catch (ConnectException | InterruptedException ce) {
                                            System.out.println("try " + times++);
                                        }
                                    }
                                }
                                case "notSend" -> inboxHandler("notSend");

                                case "yesSend" -> inboxHandler("yesSend");


                                }
                        } else {
                            if(input instanceof List) {
                                newEmails = (List<Email>) input;
                                if(start) {
                                    account.saveEmails(newEmails);
                                    start = false;
                                } else {
                                    inboxHandler("new");
                                }
                            }
                        }
                    } catch (SocketException e) {
                        System.out.println("Client si e' disconnesso.");
                    }
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }


            }
        }).start();
    }

    protected void showSelectedEmail(MouseEvent mouseEvent) {
        Email email = emailListView.getSelectionModel().getSelectedItem();
        if(email != null) {
            FXMLLoader emailLoader = new FXMLLoader(getClass().getResource("/com/example/mailclient/email.fxml"));
            try {

                if (windowSP.getItems().size() > 1) {
                    windowSP.getItems().set(1, emailLoader.load());
                } else {
                    windowSP.getItems().add(emailLoader.load());
                    windowSP.setDividerPositions(0.38);
                }

                EmailController emailController = emailLoader.getController();
                emailController.setParent(windowSP);
                emailController.setAccount(account);
                emailController.setSocket(socket);
                emailController.setEmail(email);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    protected void onWriteBtnClick() {
        FXMLLoader writeLoader = new FXMLLoader(getClass().getResource("/com/example/mailclient/write.fxml"));

        try {

            if(windowSP.getItems().size() > 1) {
                windowSP.getItems().set(1, writeLoader.load());
            }
            else {
                windowSP.getItems().add(writeLoader.load());
                windowSP.setDividerPositions(0.38);
            }

            writeController = writeLoader.getController();
            writeController.setParent(windowSP);
            writeController.setAccount(account);
            writeController.setSocket(socket);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @FXML
    protected void onExitBtnClick(){
        try{
            //so close also the thread
            if (!socket.isOutputShutdown()) { //shutdown dal thread poiché server è disconnesso
                outputStream = new NoWriteObjectOutputStream(socket.getOutputStream());
                outputStream.writeObject(new Logger(new Date(), account.getEmailAddress(), "exit success", "exit", null));
            }
            socket.close();
        }catch (IOException e){e.printStackTrace();}
        Stage exitStage = (Stage) exitBtn.getScene().getWindow();
        exitStage.close();
    }



    private void inboxHandler(String msg) throws IOException {
        new Thread(()-> Platform.runLater(() -> {
            switch (msg) {
                case "dis" -> {
                    createPopup("Disconnessione dal server.");
                    writeBtn.setVisible(false);
                }
                case "con" -> {
                    createPopup("Riconnessione avvenuta con successo.");
                    writeBtn.setVisible(true);
                }
                case "notSend" -> createPopup("Alcuni destinatari non esistono.");
                case "yesSend" -> createPopup("Email Inviata");
            }
        })).start();
    }

    private void createPopup(String msg){
        Label label = new Label(msg);
        label.setStyle("-fx-background-color: rgba(140,140,140);" +
                "-fx-padding: 15;" +
                "-fx-border-radius: 25;" +
                "-fx-background-radius: 25;" +
                "-fx-font-size: 15;");
        label.setTextFill(Color.rgb(255,255,255));

        Stage stage = (Stage) accountImage.getScene().getWindow();
        Popup popup = new Popup();
        popup.setAutoFix(true);
        popup.setAutoHide(true);
        popup.setHideOnEscape(true);
        popup.getContent().add(label);
        popup.setOnShown(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent e) {
                popup.setX(stage.getX() + stage.getWidth()/2 - popup.getWidth()/2);
                popup.setY(stage.getY() + stage.getHeight()/2 - popup.getHeight()/2);
            }
        });
        popup.show(stage);
    }


}
