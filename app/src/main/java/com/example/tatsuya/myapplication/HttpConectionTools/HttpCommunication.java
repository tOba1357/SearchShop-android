package com.example.tatsuya.myapplication.HttpConectionTools;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author Tatsuya Oba
 */
public class HttpCommunication {
    private final String TAG = HttpGet.class.getSimpleName();
    private URL url;
    private HttpURLConnection urlConnection;
    private String postData;

    public HttpCommunication() {
    }

    public HttpCommunication(String url) throws MalformedURLException {
        this.url = new URL(url);
    }

    public void setUrl(String url) throws MalformedURLException {
        this.url = new URL(url);
    }

    public void connection() throws IOException {
        urlConnection = (HttpURLConnection) url.openConnection();
    }

    public void setPostData(String data) {
        this.postData = data;
    }

    public String getGetResult() throws Exception {
        String result = "";
        try {
            InputStream inputStream = urlConnection.getInputStream();
            result = readStream(inputStream);
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new Exception();
        } finally {
            urlConnection.disconnect();
        }
        return result;
    }

    public String getPostResult() throws Exception {
        String result = "";
        try {
            urlConnection.setDoOutput(true);
            urlConnection.setChunkedStreamingMode(0);
            OutputStream outputStream = new BufferedOutputStream(urlConnection.getOutputStream());
            outputStream.write(postData.getBytes("UTF8"));
            outputStream.close();

            InputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());
            result = readStream(inputStream);
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new Exception();
        } finally {
            urlConnection.disconnect();
        }
        return result;
    }

    private String readStream(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder builder = new StringBuilder();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            builder.append(line);
        }
        bufferedReader.close();
        return builder.toString();
    }

    public boolean checkNetWork(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        if (info == null) {
            return false;
        }
        return info.isConnected();
    }
}
