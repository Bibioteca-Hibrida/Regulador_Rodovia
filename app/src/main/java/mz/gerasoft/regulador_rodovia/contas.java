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
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
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
import java.util.ArrayList;

import static android.os.StrictMode.setThreadPolicy;

public class contas extends AppCompatActivity {
    AutoCompleteTextView editText2;
    ArrayList<String> dados = new ArrayList<>();
    ListView agentes;
    ArrayAdapter<String> adapter1;
    webMethodUrl wb = new webMethodUrl();
    String dadosAgente[];
    String nomeagente[];
    String codigoagente[];
    String item="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ActionBar actionbar = getSupportActionBar();
        actionbar.setTitle("Agentes de transito");

        setContentView(R.layout.activity_contas);
        super.onCreate(savedInstanceState);


        agentes = ((ListView) findViewById(R.id.listaAgentes));
        editText2 = ((AutoCompleteTextView) findViewById(R.id.numeronomeagente));

        //allw network in main thread
        setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());
        //Retrive
        try {

            getdadosagente();
              getdadosagente1();

        } catch (MalformedURLException e) {
            e.printStackTrace();

        }


        editText2.setHint("Digite o nome/codigo do agente");

        for(int i=0;i<nomeagente.length;i++){
            dados.add(nomeagente[i]);
        }
        for(int j=0;j<codigoagente.length;j++){
            dados.add(codigoagente[j]);
        }

        adapter1 = new ArrayAdapter(this, android.R.layout.select_dialog_item, nomeagente);
       //agentes.setBackgroundColor(1);
        agentes.setAdapter(adapter1);


//
        agentes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter1, View view, int i, long l) {
                item= adapter1.getItemAtPosition(i).toString();

                Toast.makeText(contas.this, verificar(item,"nome"), Toast.LENGTH_SHORT).show();

             if(verificar(item,"nome").equals("Bloquear")){

                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(contas.this);
                alertDialog.setTitle("Bloquear user")
                        .setMessage("Deseja bloquear o agente: " +item +" ?")
                        .setCancelable(false)
                        .setPositiveButton("SIM",new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                bloquearuser(item);
                                // /dialogInterface.cancel();
                            }

                        })
                .setNegativeButton("Não",new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        dialogInterface.cancel();
                    }

                });
                 AlertDialog alert = alertDialog.create();
                 alert.show();

                }else{
                 final AlertDialog.Builder alertDialog = new AlertDialog.Builder(contas.this);
                 alertDialog.setTitle("Desbloquear user")
                         .setMessage("Deseja desbloquear o agente: " +item +" ?")
                         .setCancelable(false)
                         .setPositiveButton("SIM",new DialogInterface.OnClickListener(){
                             @Override
                             public void onClick(DialogInterface dialogInterface, int i) {
                                // bloquearuser(item);
                                 // /dialogInterface.cancel();
                             }

                         })
                         .setNegativeButton("Não",new DialogInterface.OnClickListener(){
                             @Override
                             public void onClick(DialogInterface dialogInterface, int i) {

                                 dialogInterface.cancel();
                             }

                         });

                 AlertDialog alert = alertDialog.create();
                 alert.show();
                }


             }


        });





        ArrayAdapter<String> adapter2;
        adapter2 = new ArrayAdapter(this,android.R.layout.simple_list_item_single_choice,dados);
        editText2.setThreshold(2);
        editText2.setAdapter(adapter2);
        editText2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter1, View view, int i, long l) {

                agentes.setAdapter(null);
                if (editText2.getText().toString().charAt(0) >= '0' && editText2.getText().toString().charAt(0) <= '9') {
                    agentes.setAdapter(null);
                    getagentesbynumber(editText2.getText().toString());
                } else {
                    agentes.setAdapter(null);
                    getAgenteByname(editText2.getText().toString());


                }
            }
        });

    }

    public void bloquearuser(String agente){

    }

    public String verificar(String agente,String tipo ) {
        try {
            URL url = new URL(wb.verifBloqueio.toString());
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            String post_data = URLEncoder.encode("agente", "UTF-8") + "=" + URLEncoder.encode(agente, "UTF-8") + "&"
                    + URLEncoder.encode("tipo", "UTF-8") + "=" + URLEncoder.encode(tipo, "UTF-8");
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

            return result;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;










    }

    public void getAgenteByname(String nome) {
        ArrayList<String> aux = new ArrayList<>();

        for (int i = 0; i < dados.size(); i++) {
            if (nome.equals(dados.get(i).toString())) {
                aux.add(dados.get(i).toString());
            }
            ArrayAdapter<String> adapter2;
            adapter2 = new ArrayAdapter(this,android.R.layout.simple_list_item_single_choice,aux);
            agentes.setAdapter(adapter2);

        }


        ArrayAdapter<String> adapter2;
        adapter2 = new ArrayAdapter(this, android.R.layout.select_dialog_item, aux);
        agentes.setBackgroundColor(1);
        agentes.setAdapter(adapter2);
    }

    public void getagentesbynumber(String nr) {

        ArrayList<String> aux = new ArrayList<>();

        for (int i = 0; i < dados.size(); i++) {
            if (nr.equals(dados.get(i).toString())) {
                aux.add(dados.get(i).toString());
            }
            ArrayAdapter<String> adapter2;
            adapter2 = new ArrayAdapter(this, android.R.layout.simple_list_item_single_choice, aux);
            agentes.setAdapter(adapter2);
        }
    }


    public void getdadosagente() throws MalformedURLException {
        //Toast.makeText(this, "jdh", Toast.LENGTH_SHORT).show();
        InputStream is = null;
        String line = null;
        String result = null;
        try {
            URL url = new URL(wb.dadosagente.toString());
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
        try{
            JSONArray ja = new JSONArray(result);
            JSONObject jo=null;

            nomeagente = new String[ja.length()];

            for(int i=0;i<ja.length();i++){
                jo=ja.getJSONObject(i);
              nomeagente[i]=jo.getString("nome");
             //   Toast.makeText(this, nomeagente[0], Toast.LENGTH_SHORT).show();

            }

//

        }catch (Exception e)
        {
            e.printStackTrace();
        }

    }

        //



    private void getdadosagente1() throws MalformedURLException {

        InputStream is = null;
        String line = null;
        String result = null;
        try {
            URL url = new URL(wb.dadosagente.toString());
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
        try{
            JSONArray ja = new JSONArray(result);
            JSONObject jo=null;


            codigoagente=new String[ja.length()];
           // Toast.makeText(this, codigoagente[0], Toast.LENGTH_SHORT).show();
            for(int i=0;i<ja.length();i++){
                jo=ja.getJSONObject(i);
                codigoagente[i]=jo.getString("codigo");

            // Toast.makeText(this, "jdh", Toast.LENGTH_SHORT).show();
           }
           // Toast.makeText(this, codigoagente[0], Toast.LENGTH_SHORT).show();

//

        }catch (Exception e)
        {
            e.printStackTrace();
        }


    }

    public void novocadastro(View view){
        Intent i = new Intent(this, cadastrarAgente.class);
        startActivity(i);
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(this, main.class);
        startActivity(i);
        super.onBackPressed();
    }


}
