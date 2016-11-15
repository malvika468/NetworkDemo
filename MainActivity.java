package com.example.user.networkdemo;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.DropBoxManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.TextView;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    TextView textView;
    TextView textView2;
    String urlText="https://www.iiitd.ac.in/about";
    String contentAsString = "";
    String title = "";
    String desc="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView=(TextView)findViewById(R.id.textView);
        textView2=(TextView)findViewById(R.id.textView2);
        if (savedInstanceState != null) {
       /*When rotation occurs
         */
            String state = savedInstanceState.getString("myString");
            textView2.setText(state);
            String state1 = savedInstanceState.getString("myString1");
            textView.setText(state1);
        }


    }
    public void download(View view)
   {
        String stringUrl = urlText;
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            new LoadWebTask().execute(stringUrl);
        } else {
           /// textView.setText("No network connection available.");
        }
    }



    private class LoadWebTask extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... urls) {
// params comes from the execute() call: params[0] is the url.
            try {
                downloadUrl(urls[0]);
                Document document = Jsoup.connect(urls[0]).get();
                Log.d("Html",document.toString());
                title=document.title();
                Elements elements = document.select("p");
                String temp=elements.text();
                String sub=temp.substring(0,750);
                desc=sub;

            } catch (IOException e) {

            }
          return null;
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            textView.setText(desc);
            textView2.setText(title);
        }






        private void downloadUrl(String myurl) throws IOException {
            InputStream is = null;

// Only display the first 500 characters of the retrieved
// web page content.
            int MAX = 10000;
            try {
                URL url = new URL(myurl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
// Starts the query
                conn.connect();
                int response = conn.getResponseCode();
                Log.d("DEBUG_TAG", "The response is: " + response);
                is = conn.getInputStream();
// Convert the InputStream into a string
                contentAsString = readIt(is, MAX);

                Log.d("Html",contentAsString);

// Makes sure that the InputStream is closed after the app is
// finished using it.
            } finally {
                if (is != null) {
                    is.close();
                }
            }
        }


        public String readIt(InputStream stream, int MAX)
                throws IOException, UnsupportedEncodingException {
            Reader reader = null;
            reader = new InputStreamReader(stream, "UTF-8");
            char[] buffer = new char[MAX];
            reader.read(buffer);
            return new String(buffer);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putCharSequence("myString",title);
        savedInstanceState.putCharSequence("myString1",desc);

    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        title = savedInstanceState.getString("myString");
         desc = savedInstanceState.getString("myString1");

    }



    }


















