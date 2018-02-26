package mz.gerasoft.regulador_rodovia;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class main extends AppCompatActivity {
    webMethodUrl wb = new webMethodUrl();

    private TextView mTextMessage;

    @Override
    public void onBackPressed() {
        Intent i = new Intent(this, main.class);
        startActivity(i);
        super.onBackPressed();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.CaMulta:
                    Intent i = new Intent(main.this, CadastrarMulta.class);
                    startActivity(i);
                    mTextMessage.setText("Multa");
                    return true;
                case R.id.CoMulta:
                    Intent j = new Intent(main.this, ConsultarMulta.class);
                    startActivity(j);
                    mTextMessage.setText("Consultar Multa");
                    return true;
                case R.id.CoArtigo:
                    Intent k = new Intent(main.this, ConsultarArtigo.class);
                    startActivity(k);
                    mTextMessage.setText("Consultar Artigo");
                    return true;
                case R.id.Grelatorio:
                    Intent l = new Intent(main.this, GerarRelatorio.class);
                    startActivity(l);
                    mTextMessage.setText("Gerar Relatorio");
                    return true;

            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //allw network in main thread
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());
        //Retrive
        updateMultas1();
        updateMultas2();
        updateMultas3();
    }

    private void updateMultas1() {
        InputStream is = null;
        String line = null;
        String result = " ";
        try {
            URL url = new URL(wb.updt1.toString());
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));

            StringBuilder sb = new StringBuilder();

            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line + "\n");

            }
            result = sb.toString();

            Toast.makeText(this, result.toString(), Toast.LENGTH_SHORT).show();
             bufferedReader.close();
            inputStream.close();


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }



    }
    private void updateMultas2() {
        InputStream is = null;
        String line = null;
        String result = " ";
        try {
            URL url = new URL(wb.updt2.toString());
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));

            StringBuilder sb = new StringBuilder();

            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line + "\n");

            }
            result = sb.toString();

            Toast.makeText(this, result.toString(), Toast.LENGTH_SHORT).show();
            bufferedReader.close();
            inputStream.close();


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void updateMultas3() {
        InputStream is = null;
        String line = null;
        String result = " ";
        try {
            URL url = new URL(wb.updt3.toString());
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));

            StringBuilder sb = new StringBuilder();

            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line + "\n");

            }
            result = sb.toString();

            Toast.makeText(this, result.toString(), Toast.LENGTH_SHORT).show();
            bufferedReader.close();
            inputStream.close();


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    }