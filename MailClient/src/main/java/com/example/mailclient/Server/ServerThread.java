package com.example.mailclient.Server;

import com.example.mailclient.Model.Logger;
import com.example.mailclient.Model.NoWriteObjectOutputStream;
import javafx.scene.control.TextArea;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ServerThread implements Runnable {



        private ServerSocket serverSocket;
        private TextArea logText;


        public ServerThread(ServerSocket serverSocket, TextArea logText) {
            this.serverSocket = serverSocket;
            this.logText = logText;
        }


        public void run() {
            int i = 1;
            List<Socket> socketList = new ArrayList<>();
            List<Runnable> runnableList = new ArrayList<>();
            try {
                while (!serverSocket.isClosed()) {
                    Socket socket = serverSocket.accept();
                    System.out.println("Client number " + i++);
                    Runnable r = new ServerHandler(socket, logText, runnableList);
                    socketList.add(socket);
                    new Thread(r).start();
                    runnableList.add(r);
                }
            } catch (IOException e) {
                try {
                    for (Socket closeSocket : socketList) {
                        if (!closeSocket.isClosed() && closeSocket.isConnected()) {
                            NoWriteObjectOutputStream outputStream = new NoWriteObjectOutputStream(closeSocket.getOutputStream());
                            outputStream.writeObject(new String("disconnected"));
                            outputStream.flush();
                            closeSocket.close();
                        }
                    }
                } catch (IOException err) {
                    err.getMessage();
                }
            }
            logText.appendText(new Logger(new Date(), "SERVER", "Disconnesso", null, null).toString());
        }
}

