package mz.gerasoft.regulador_rodovia;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Message;
import android.os.StrictMode;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
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

public class ConsultarArtigo extends AppCompatActivity {
    webMethodUrl wb=new webMethodUrl();
    Spinner artReferencia;
    ListView Listaart;
    String referencia[];
    String infracao[];
    String numero_artigo="";
    String item="";

    String disposto[];



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ActionBar actionbar = getSupportActionBar();

        actionbar.setTitle("Consulta de artigo");
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_consultar_artigo);
        artReferencia = (Spinner) findViewById(R.id.Artigos);
        Listaart = (ListView) findViewById(R.id.listaArtigos);


        //allw network in main thread
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());
        //Retrive
        try {

            getreferencia();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        ArrayAdapter<String> adapter5 = new ArrayAdapter(this,android.R.layout.simple_spinner_item,referencia);
        adapter5.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
       artReferencia.setAdapter(adapter5);

        artReferencia.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapter5, View view, int i, long l) {

                String item= artReferencia.getSelectedItem().toString();

              //  Toast.makeText(ConsultarArtigo.this,item, Toast.LENGTH_SHORT).show();
                getinfracao(view);



            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }


        });



    }
    public void getreferencia() throws MalformedURLException {
        InputStream is = null;
        String line=null;
        String result=null;
        try{
            URL url = new URL(wb.address_referencia.toString());
            HttpURLConnection conn= (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            is=new BufferedInputStream(conn.getInputStream());


        }catch (Exception e)
        {
            e.printStackTrace();
        }
        //Read
        try{

            BufferedReader br=new BufferedReader(new InputStreamReader(is));
            StringBuilder sb=new StringBuilder();

            while((line=br.readLine())!=null){
                sb.append(line+"\n");

            }
            is.close();
            result=sb.toString();
        }catch (Exception e)
        {
            e.printStackTrace();
        }

//parse Json data
        try{
            JSONArray ja = new JSONArray(result);
            JSONObject jo=null;

            referencia = new String[ja.length()];

            for(int i=0;i<ja.length();i++){
                jo=ja.getJSONObject(i);

                referencia[i]=jo.getString("descricao");
            }

        }catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    private void getinfracao(View v)  {
        InputStream is = null;
        String line=null;
        String result=null;
        String referenciaMulta= artReferencia.getSelectedItem().toString();
        try{
            URL url = new URL(wb.address_showinfracao.toString());
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            String post_data = URLEncoder.encode("referenciaMulta", "UTF-8") + "=" + URLEncoder.encode(referenciaMulta, "UTF-8");
            bufferedWriter.write(post_data);
            //  Toast.makeText(this, post_data, Toast.LENGTH_SHORT).show();
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

            //Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
            // bufferedReader.close();
            inputStream.close();


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

//parse Json data
        try{
            JSONArray ja = new JSONArray(result);
            JSONObject jo=null;

            infracao = new String[ja.length()];

            for(int i=0;i<ja.length();i++){
                jo=ja.getJSONObject(i);
                infracao[i]= "Artigo nr: " + jo.getString("nr_artigo") + " - " + jo.getString("descricao");
            }

            ArrayAdapter<String> adapter3 = new ArrayAdapter(this,android.R.layout.select_dialog_item,infracao);
            Listaart.setAdapter(adapter3);

            Listaart.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapter1, View view, int i, long l) {
                     item= adapter1.getItemAtPosition(i).toString();
                numero_artigo="";
                    for(int j=0;j<item.length();j++){
                        if(item.charAt(j)>='0' && item.charAt(j)<='9'){
                            numero_artigo+=item.charAt(j);
                        }
                    }


        findDisposto();

                }
            });


        }catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    public void findDisposto(){
        String result="";
        String line="";



        try {
            URL url = new URL(wb.address_disposto1.toString());
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            String post_data = URLEncoder.encode("numero_artigo", "UTF-8") + "=" + URLEncoder.encode(numero_artigo, "UTF-8");
            bufferedWriter.write(post_data);
            //  Toast.makeText(this, post_data, Toast.LENGTH_SHORT).show();
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

            //Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
            // bufferedReader.close();
            inputStream.close();


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            //Toast.makeText(this, "Helloo ", Toast.LENGTH_SHORT).show();
            JSONArray ja = new JSONArray(result);
            JSONObject jo = null;
            //Toast.makeText(this, "passou ", Toast.LENGTH_SHORT).show();
            // nomeconduto=jo.getString("nome");
            String dispostos="";
            disposto = new String[ja.length()];
            for (int i = 0; i < ja.length(); i++) {
                jo = ja.getJSONObject(i);
               // disposto[i] = jo.getString("descricao");
                dispostos+=jo.getString("numero_disposto") + " - " +jo.getString("descricao") + " - " +
                        jo.getString("valor") +"Mts" +"\n";
            }

            final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setTitle(item)
                    .setMessage(dispostos)
                    .setCancelable(false)
                    .setPositiveButton("OK",new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
            AlertDialog alert = alertDialog.create();
            alert.show();





        } catch (Exception e) {
            e.printStackTrace();
        }



    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(this, main.class);
        startActivity(i);
        super.onBackPressed();
    }


    }




