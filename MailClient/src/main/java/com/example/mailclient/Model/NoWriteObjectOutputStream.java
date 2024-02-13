package com.example.mailclient.Model;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

/**
 * Classe ObjectOutputStream che non scrive l'header nello stream quando lo apre
 * */
public class NoWriteObjectOutputStream extends ObjectOutputStream {

    public NoWriteObjectOutputStream(OutputStream outStream) throws IOException {
        super(outStream);
    }

    @Override
    protected void writeStreamHeader(){
    }
}
