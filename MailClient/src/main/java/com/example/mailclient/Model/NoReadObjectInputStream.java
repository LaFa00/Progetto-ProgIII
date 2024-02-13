package com.example.mailclient.Model;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;

/**
 * Classe ObjectInputStream che non legge l'header dello stream quando lo apre
 * */
public class NoReadObjectInputStream extends ObjectInputStream {


    public NoReadObjectInputStream(InputStream inStream) throws IOException {
        super(inStream);
    }

    @Override
    protected void readStreamHeader(){
    }
}
