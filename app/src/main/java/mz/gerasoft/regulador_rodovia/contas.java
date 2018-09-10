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
import android.widget.ListAdapter;
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
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import static android.os.StrictMode.setThreadPolicy;

public class contas extends AppCompatActivity {
    AutoCompleteTextView editText2;
    ArrayList<String> dados = new ArrayList<>();
    ListView agentes;
    String agenteN[];
    ArrayAdapter<String> adapter1;
    webMethodUrl wb = new webMethodUrl();
    String aux[];
    String aux1="";
    String nomeagente[];
    String codigoagente[];
    String item="",ag;
    login l = new login();

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
            findNameAgente();
            getdadosagente(ag,"nome");

            getdadosagente1(l.username,"nr");


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
            public void onItemClick(final AdapterView<?> adapter1, View view, int i, long l) {
                item= adapter1.getItemAtPosition(i).toString();

                //trabalhar o item

                for(int j=0;j<item.length();j++){
                    if(item.charAt(j)!='\n'){
                        aux1+=item.charAt(j);
                    }else{
                        break;
                    }
                }
               // Toast.makeText(contas.this,aux, Toast.LENGTH_SHORT).show();

               Toast.makeText(contas.this, verificar(aux1,"nome"), Toast.LENGTH_LONG).show();

             if(verificar(aux1,"nome").equals("Bloquear")){

                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(contas.this);
                 alertDialog.setTitle("Bloquear user")
                        .setMessage("Deseja bloquear o agente: " +aux1 +" ?")
                        .setCancelable(false)
                        .setPositiveButton("SIM",new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                Toast.makeText(contas.this, bloquearuser(aux1,"nome"), Toast.LENGTH_LONG).show();
                                dialogInterface.cancel();
                                //agentes.setAdapter(null);
                                //agentes.setAdapter((ListAdapter) adapter1);

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
                         .setMessage("Deseja desbloquear o agente: " +aux1 +" ?")
                         .setCancelable(false)
                         .setPositiveButton("SIM",new DialogInterface.OnClickListener(){
                             @Override
                             public void onClick(DialogInterface dialogInterface, int i) {
                               Toast.makeText(contas.this, desbloquearuser(aux1,"nome"), Toast.LENGTH_LONG).show();
                                 dialogInterface.cancel();
                                // agentes.setAdapter(null);
                                // agentes.setAdapter((ListAdapter) adapter1);
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

    public String bloquearuser(String agente, String tipo){

        try {
            URL url = new URL(wb.bloquear.toString());
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

            refresh();
            return result;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;


    }

    public String desbloquearuser(String agente, String tipo){

        try {
            URL url = new URL(wb.desbloquear.toString());
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

            refresh();

            return result;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;


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

            agentes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(final AdapterView<?> adapter1, View view, int i, long l) {
                    item= adapter1.getItemAtPosition(i).toString();

                    Toast.makeText(contas.this, verificar(item,"nome"), Toast.LENGTH_LONG).show();

                    if(verificar(item,"nome").equals("Bloquear")){

                        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(contas.this);
                        alertDialog.setTitle("Bloquear user")
                                .setMessage("Deseja bloquear o agente: " +item +" ?")
                                .setCancelable(false)
                                .setPositiveButton("SIM",new DialogInterface.OnClickListener(){
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                        Toast.makeText(contas.this, bloquearuser(item,"nome"), Toast.LENGTH_LONG).show();


                                     //   dialogInterface.cancel();

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
                                        Toast.makeText(contas.this, desbloquearuser(item,"nome"), Toast.LENGTH_LONG).show();
                                        refresh();
                                        dialogInterface.cancel();


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

        agentes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> adapter1, View view, int i, long l) {
                item= adapter1.getItemAtPosition(i).toString();

               // Toast.makeText(contas.this, verificar(item,"nr"), Toast.LENGTH_LONG).show();

                if(verificar(item,"nr").equals("Bloquear")){

                    final AlertDialog.Builder alertDialog = new AlertDialog.Builder(contas.this);
                    alertDialog.setTitle("Bloquear user")
                            .setMessage("Deseja bloquear o agente: " +item +" ?")
                            .setCancelable(false)
                            .setPositiveButton("SIM",new DialogInterface.OnClickListener(){
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    Toast.makeText(contas.this, bloquearuser(item,"nr"), Toast.LENGTH_LONG).show();
                                    dialogInterface.cancel();
                                    agentes.setAdapter(null);
                                    agentes.setAdapter((ListAdapter) adapter1);
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
                                    Toast.makeText(contas.this, desbloquearuser(item,"nr"), Toast.LENGTH_LONG).show();
                                    dialogInterface.cancel();
                                    agentes.setAdapter(null);
                                    agentes.setAdapter((ListAdapter) adapter1);
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
    }


    public void getdadosagente(String agente,String tipo) throws MalformedURLException {

        try {
            URL url = new URL(wb.dadosagente.toString());
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
//parse Json data
            try {
                JSONArray ja = new JSONArray(result);
                JSONObject jo = null;

                nomeagente = new String[ja.length()];

                for (int i = 0; i < ja.length(); i++) {
                    jo = ja.getJSONObject(i);
                    nomeagente[i] = jo.getString("nome") +"\n" +"\t" +"- " +jo.getString("codigo") +"\n" +"\t" +"\t"
                            +"- " +jo.getString("estado");
                    //   Toast.makeText(this, nomeagente[0], Toast.LENGTH_SHORT).show();

                }

//

            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
        //



    private void getdadosagente1(String agente,String tipo) throws MalformedURLException {

        try {
            URL url = new URL(wb.dadosagente.toString());
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

//parse Json data
            try {
                JSONArray ja = new JSONArray(result);
                JSONObject jo = null;


                codigoagente = new String[ja.length()];
                // Toast.makeText(this, codigoagente[0], Toast.LENGTH_SHORT).show();
                for (int i = 0; i < ja.length(); i++) {
                    jo = ja.getJSONObject(i);
                    codigoagente[i] = jo.getString("codigo");

                    // Toast.makeText(this, "jdh", Toast.LENGTH_SHORT).show();
                }
                // Toast.makeText(this, codigoagente[0], Toast.LENGTH_SHORT).show();

//

            } catch (Exception e) {
                e.printStackTrace();
            }


        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void novocadastro(View view){
        Intent i = new Intent(this, cadastrarAgente.class);
        startActivity(i);
    }

    public void refresh(){
        Intent p = new Intent(this,contas.class);
        startActivity(p);
    }
    @Override
    public void onBackPressed() {
        Intent i = new Intent(this, main.class);
        startActivity(i);
        super.onBackPressed();
    }


    public void findNameAgente() throws MalformedURLException {
        String result="";
        String line="";
        String cAgente = l.username ;
        try {
            URL url = new URL(wb.address_nomeAgente.toString());
            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            String post_data = URLEncoder.encode("cAgente","UTF-8")+"="+URLEncoder.encode(cAgente,"UTF-8");
            bufferedWriter.write(post_data);
            //  Toast.makeText(this, post_data, Toast.LENGTH_SHORT).show();
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

            //Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
            // bufferedReader.close();
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
            //Toast.makeText(this, "passou ", Toast.LENGTH_SHORT).show();
            // nomeconduto=jo.getString("nome");
            agenteN = new String[ja.length()];

            jo=ja.getJSONObject(0);
            // Toast.makeText(this, jo.getString("nome"), Toast.LENGTH_SHORT).show();
            ag=(jo.getString("nome"));

            //  condutor=nameCondutor.getText().toString();
            /*for(int i=0;i<ja.length();i++){
                jo=ja.getJSONObject(i);
                matricula[i]=jo.getString("matricula");
            }*/

        }catch (Exception e)
        {
            e.printStackTrace();
        }

    }


}
