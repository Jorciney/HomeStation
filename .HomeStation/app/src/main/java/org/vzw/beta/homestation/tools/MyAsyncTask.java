package org.vzw.beta.homestation.tools;

import android.os.AsyncTask;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by user109 on 21/03/2016.
 */
public class MyAsyncTask extends AsyncTask <String, Void, String> {

    @Override
    protected String doInBackground(String... urls) {
        String response="";
        URL url;
        HttpURLConnection urlConnection=null;

        try{
            url=new URL(urls[0]);
            urlConnection=(HttpURLConnection)url.openConnection();
            InputStream inputStream=urlConnection.getInputStream();
            InputStreamReader inputStreamReader=new InputStreamReader(inputStream);
            int data=inputStreamReader.read();
            while(data!=-1){
                char current=(char)data;
                data=inputStreamReader.read();
                response+=current;
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return response;
    }
}
