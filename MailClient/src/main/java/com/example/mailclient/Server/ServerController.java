package com.example.mailclient.Server;

import com.example.mailclient.Model.Logger;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.ServerSocket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ServerController {

    @FXML
    private Button exitBtn;
    @FXML
    private Button exportBtn;
    @FXML
    private Button connectBtn;
    @FXML
    private Button disconnectBtn;
    @FXML
    private TextArea logText;

    private ServerSocket serverSocket;

    @FXML
    public void initialize(){
        connectBtn.setVisible(true);
        disconnectBtn.setVisible(false);
    }

    @FXML
    protected void onConnectClick() {
        logText.appendText(new Logger(new Date(), "SERVER", "Connessione iniziata", null,null).toString());
        try{
            serverSocket = new ServerSocket(8080);
            Runnable r = new ServerThread(serverSocket, logText);
            new Thread(r).start();

        } catch (IOException e) {
            e.getMessage();

        }
        disconnectBtn.setVisible(true);
        connectBtn.setVisible(false);
    }

    @FXML
    protected void onDisconnectClick() {
        try{
            serverSocket.close();
        }catch (IOException e){
            e.printStackTrace();
        }
        connectBtn.setVisible(true);
        disconnectBtn.setVisible(false);
    }

    @FXML
    public void onExitClick() {
        if(serverSocket != null && !serverSocket.isClosed()) {
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Stage serverStage = (Stage)exitBtn.getScene().getWindow();
        serverStage.close();
    }

    @FXML
    public void onExportClick() throws IOException {
        DateFormat df = new SimpleDateFormat("dd_MM_yyyy");
        String path = "C:\\Users\\Daniele\\Documents\\PROGETTO_SPERUMA\\MailClient\\src\\main\\resources\\Log";
        File file = new File(path);
        FileWriter fw = new FileWriter(file + File.separator + df.format(new Date()) + ".log",true);
        fw.write(logText.getText());
        if (fw!=null)
            fw.close();
    }

}
