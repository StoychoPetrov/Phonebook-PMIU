package com.example.stoycho.phonebook.communicationClasses;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by stoycho.petrov on 17/02/2017.
 */

public class HttpRequest extends AsyncTask<URL,Void,String> {

    @Override
    protected String doInBackground(URL... urls) {

        if(urls.length == 0)
            return null;

        String dataGetChanges = "";
        URL urlForData  = urls[0];

        try {

            HttpURLConnection urlConnectionGetChanges = (HttpURLConnection) urlForData.openConnection();
            BufferedReader bufferedReaderGetChanges = new BufferedReader(new InputStreamReader(urlConnectionGetChanges.getInputStream()));
            String nextGetChanges = null;

            nextGetChanges = bufferedReaderGetChanges.readLine();

            while (nextGetChanges != null) {
                dataGetChanges += nextGetChanges;
                nextGetChanges = bufferedReaderGetChanges.readLine();
            }
            urlConnectionGetChanges.disconnect();

            return dataGetChanges;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
