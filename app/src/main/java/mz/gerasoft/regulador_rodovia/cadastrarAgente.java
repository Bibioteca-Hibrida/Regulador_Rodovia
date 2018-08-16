package mz.gerasoft.regulador_rodovia;

import android.os.StrictMode;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
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

public class cadastrarAgente extends AppCompatActivity {
EditText apelido,editnome,editsenha,editsenha1;
    Spinner graduacao,typeuser;
    webMethodUrl wb = new webMethodUrl();

    String lastID,idgraduacao,idtipoUser;
    String graduacaol[],tipouser[],lastid[];

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        ActionBar actionbar = getSupportActionBar();

        actionbar.setTitle("Cadastro de agente de transito");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_agente);

        apelido = (EditText) findViewById(R.id.apelido);
        editnome = (EditText) findViewById(R.id.editnome);
        editsenha = (EditText) findViewById(R.id.editsenha);
        editsenha1 = (EditText) findViewById(R.id.editsenha1);

        graduacao = (Spinner) findViewById(R.id.graduacao);
        typeuser = (Spinner) findViewById(R.id.usertype);



//allw network in main thread
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());
        //Retrive
        getLastID();

       Toast.makeText(this, "---" +lastID, Toast.LENGTH_SHORT).show();
       getGraduacao();

        Toast.makeText(this, "idARtigo"+idgraduacao, Toast.LENGTH_SHORT).show();

//        gettipoUser();

        ArrayAdapter<String> adapter5 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, graduacaol);
        adapter5.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        graduacao.setAdapter(adapter5);

       graduacao.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapter5, View view, int i, long l) {

                String item = graduacao.getSelectedItem().toString();

                //Toast.makeText(cadastrarAgente.this, item, Toast.LENGTH_SHORT).show();

                getidGraduacao(item);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }


        });
//
//
//        ArrayAdapter<String> adapter6 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, tipouser);
//        adapter6.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        typeuser.setAdapter(adapter6);
//
//        typeuser.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapter6, View view, int i, long l) {
//
//                String item = typeuser.getSelectedItem().toString();
//
//
//                Toast.makeText(cadastrarAgente.this, item, Toast.LENGTH_SHORT).show();
//
//                getidtipouser(item);
//
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//
//
//        });


    }

    public void getLastID(){
        InputStream is = null;
        String line = null;
        String result = null;
        try {
            URL url = new URL(wb.address_lastIdagente.toString());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            is = new BufferedInputStream(conn.getInputStream());


        } catch (Exception e) {
            e.printStackTrace();
        }
        //Read
        try {

            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();

            while ((line = br.readLine()) != null) {
                sb.append(line + "\n");

            }
            is.close();
            result = sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

//parse Json data
        try {
            JSONArray ja = new JSONArray(result);
            JSONObject jo = null;

            lastid=new String[ja.length()];


                jo = ja.getJSONObject(0);

                lastID = jo.getString("lastid");


        } catch (Exception e) {
            e.printStackTrace();
        }



    }

    public void getGraduacao(){
        InputStream is = null;
        String line = null;
        String result = null;
        try {
            URL url = new URL(wb.address_graduacao.toString());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            is = new BufferedInputStream(conn.getInputStream());


        } catch (Exception e) {
            e.printStackTrace();
        }
        //Read
        try {

            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();

            while ((line = br.readLine()) != null) {
                sb.append(line + "\n");

            }
            is.close();
            result = sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

//parse Json data
        try {
            JSONArray ja = new JSONArray(result);
            JSONObject jo = null;

            graduacaol = new String[ja.length()];

            for (int i = 0; i < ja.length(); i++) {
                jo = ja.getJSONObject(i);

                graduacaol[i] = jo.getString("descricao");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }



    }

    public void gettipoUser (){

        InputStream is = null;
        String line = null;
        String result = null;
        try {
            URL url = new URL(wb.address_tipoUser.toString());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            is = new BufferedInputStream(conn.getInputStream());


        } catch (Exception e) {
            e.printStackTrace();
        }
        //Read
        try {

            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();

            while ((line = br.readLine()) != null) {
                sb.append(line + "\n");

            }
            is.close();
            result = sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

//parse Json data
        try {
            JSONArray ja = new JSONArray(result);
            JSONObject jo = null;

            tipouser = new String[ja.length()];

            for (int i = 0; i < ja.length(); i++) {
                jo = ja.getJSONObject(i);

                tipouser[i] = jo.getString("descricao");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void getidGraduacao(String i){
        String result="";
        String line="";
        try {
            URL url = new URL(wb.address_idgraduacao.toString());
            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            String post_data = URLEncoder.encode("i","UTF-8")+"="+URLEncoder.encode(i,"UTF-8");
            bufferedWriter.write(post_data);

            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));

            StringBuilder sb=new StringBuilder();

            while((line=bufferedReader.readLine())!=null){
                sb.append(line+"\n");

            }
            result=sb.toString();

            ;
            inputStream.close();



        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try{

            JSONArray ja = new JSONArray(result);
            JSONObject jo= null;

            graduacaol = new String[ja.length()];
            jo=ja.getJSONObject(0);
            idgraduacao=  (jo.getString("idgraduacao"));



        }catch (Exception e)
        {
            e.printStackTrace();
        }


    }

    public void getidtipouser (String i){

        String result="";
        String line="";
        try {
            URL url = new URL(wb.address_idacesso.toString());
            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            String post_data = URLEncoder.encode("i","UTF-8")+"="+URLEncoder.encode(i,"UTF-8");
            bufferedWriter.write(post_data);

            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));

            StringBuilder sb=new StringBuilder();

            while((line=bufferedReader.readLine())!=null){
                sb.append(line+"\n");

            }
            result=sb.toString();

            ;
            inputStream.close();



        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try{
            //Toast.makeText(this, "Helloo ", Toast.LENGTH_SHORT).show();
            JSONArray ja = new JSONArray(result);
            JSONObject jo= null;
            // Toast.makeText(this, "passou ", Toast.LENGTH_SHORT).show();
            // nomeconduto=jo.getString("nome");
            ///idart = new String[ja.length()];

            jo=ja.getJSONObject(0);
            idtipoUser=  (jo.getString("idacesso"));

            Toast.makeText(this, "idARtigo"+idtipoUser, Toast.LENGTH_SHORT).show();

        }catch (Exception e)
        {
            e.printStackTrace();
        }

    }

}
