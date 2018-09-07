package mz.gerasoft.regulador_rodovia;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import org.json.JSONException;
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
EditText apelido,editnome;
    Spinner graduacao,typeuser;
    webMethodUrl wb = new webMethodUrl();

    String lastID,idgraduacao,idtipoUser,codigoAgente;
    String graduacaol[],tipouser[],lastid[];

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        ActionBar actionbar = getSupportActionBar();

        actionbar.setTitle("Cadastro de agente de transito");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_agente);

        apelido = (EditText) findViewById(R.id.apelido);
        editnome = (EditText) findViewById(R.id.editnome);


        graduacao = (Spinner) findViewById(R.id.graduacao);
        typeuser = (Spinner) findViewById(R.id.usertype);



//allw network in main thread
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());
        //Retrive
        getLastID();

      //  Toast.makeText(this, "---" +lastID, Toast.LENGTH_SHORT).show();
        getGraduacao();


       gettipoUser();




        //
//




    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(this, main.class);
        startActivity(i);
        super.onBackPressed();
    }


    public void onCadatro(View v) {

        // Toast.makeText(this, agenteid, Toast.LENGTH_SHORT).show();
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());


        AlertDialog  alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Cadastro Status: ");


        getidGraduacao();

     //   Toast.makeText(this, "idtipo-"+idgraduacao, Toast.LENGTH_SHORT).show();

        getidtipouser();

       // Toast.makeText(this, "idARtigo"+idtipoUser, Toast.LENGTH_SHORT).show();


        try {

           int x = Integer.parseInt(lastID) + 001;

            codigoAgente = String.valueOf(x);
            String nome = (editnome.getText().toString()) +" " + (apelido.getText().toString());
            String senha = "0000";

            URL url = new URL(wb.cadastro_agente.toString());
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            String post_data = URLEncoder.encode("codigoAgente", "UTF-8") + "=" + URLEncoder.encode(codigoAgente, "UTF-8") + "&"
                    + URLEncoder.encode("nome", "UTF-8") + "=" + URLEncoder.encode(nome, "UTF-8") + "&"
                    + URLEncoder.encode("senha", "UTF-8") + "=" + URLEncoder.encode(senha, "UTF-8") + "&"
                    + URLEncoder.encode("idgraduacao", "UTF-8") + "=" + URLEncoder.encode(idgraduacao, "UTF-8") + "&"
                    + URLEncoder.encode("idacesso", "UTF-8") + "=" + URLEncoder.encode(idtipoUser, "UTF-8");
            bufferedWriter.write(post_data);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
            String result = "";
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                result += line;
            }
            bufferedReader.close();
            inputStream.close();
            httpURLConnection.disconnect();

            if (result.equals("Criado novo utilizador:")) {
                alertDialog.setMessage(result);
                alertDialog.show();

                Intent i = new Intent(this.getApplicationContext(), cadastrarAgente.class);
                this.startActivity(i);
            } else {
                alertDialog.setMessage(result);
                alertDialog.show();

            }


        } catch (IOException e) {
            e.printStackTrace();
        }


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

        ArrayAdapter<String> adapter5 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, graduacaol);
        adapter5.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        graduacao.setAdapter(adapter5);

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

                ArrayAdapter<String> adapter6 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, tipouser);
        adapter6.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeuser.setAdapter(adapter6);


    }

    public void getidGraduacao() {

        String result="";
        String line="";
        String nomegraduacao = graduacao.getSelectedItem().toString();
       // Toast.makeText(this, "idtipo-"+nomegraduacao, Toast.LENGTH_SHORT).show();
        try {
            URL url = new URL(wb.address_idgraduacao.toString());
            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            String post_data = URLEncoder.encode("nomegraduacao","UTF-8")+"="+URLEncoder.encode(nomegraduacao,"UTF-8");
            bufferedWriter.write(post_data);
          //    Toast.makeText(this, post_data, Toast.LENGTH_SHORT).show();
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

        //    Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
            // bufferedReader.close();
            inputStream.close();



        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try{
           // Toast.makeText(this, "Helloo ", Toast.LENGTH_SHORT).show();
            JSONArray ja = new JSONArray(result);
            JSONObject jo= null;
            //Toast.makeText(this, "passou ", Toast.LENGTH_SHORT).show();
            // nomeconduto=jo.getString("nome");
            graduacaol = new String[ja.length()];

            jo=ja.getJSONObject(0);

            idgraduacao=(jo.getString("idgraduacao"));



        }catch (Exception e)
        {
            e.printStackTrace();
        }




    }

    public void getidtipouser (){

        String result="";
        String line="";
        String i = typeuser.getSelectedItem().toString();
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


        }catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    public void cleanSpace (View view){
       apelido.setText(null);
        editnome.setText(null);


    }

}
