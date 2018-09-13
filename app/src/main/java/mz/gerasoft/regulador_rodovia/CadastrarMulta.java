package mz.gerasoft.regulador_rodovia;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ListView;
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
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;

public class CadastrarMulta extends AppCompatActivity {
    AlertDialog alertDialog;

    /*TextView numeroCarta,nomeCondutor,nameInfra,namedisposto,idprovincia,iddistrito,idmatricula,idmarca,
    idtipoV,idclasseV,idcor,idservico,idnomeP,idnota,idvalor;
AutoCompleteTextView multiAutoCompleteCarta,multiAutoinfracao,multiAutodisposto,multiAutoprovincia,multiAutodistrito,multiAutomarca,
    multiAutotipoV,multiAutoclasse,multiAutocor,multiAutoservico;
    EditText nameCondutor,matriculaV,nomeP,nota,valor;*/
    public static String condutor;
    AutoCompleteTextView multiAutoCompleteCarta, multiAutoinfracao, multiAutodistrito, multiAutoprovincia, matriculaV;
    EditText nameCondutor, valor, nota, LocalInfacao;
    Spinner referenciaInfracao, multiAutodisposto, tipoMulta;
    ListView artigosLista;
    String idcontravensao;
    String marca, servico, tipo, cor;


    webMethodUrl wb = new webMethodUrl();
    // String address_agenteDados =  "http://192.168.43.37/agente.php";

    public static String viaturaMatricula, nomeDisposto, moneyMulta, detalhes, nomeArtigo, nomeReferencia, nomeResponsavel, nomeLocal;

    String nrcarta[], viatura[];
    public static String Nrcarta;
    String infracao[];
    String provincia[];
    public static String Provincia;
    String matricula[];
    String nomeconduto[];
    String valorMulta[];
    String disposto[];
    String distrito[];
    public static String Distrito;
    String idco[], nrmultas[];
    String idC;
    String idart[];
    public static String idA;
    String iddisp[];
    public static String idDisp;
    String idveiculo[], idtipo_multa[];
    String id_Veiculo, idTipo_multa;
    String iddistri[];
    String referencia[];
    String Referencia;
    String TipoMUlta[];
    String tipo_multa;
    String idDistri;
    String idProv;
    String agenteid;
    String item, nrMultas;


    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ActionBar actionbar = getSupportActionBar();

        actionbar.setTitle("Cadastro de multa");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_multa);
        login l = new login();

        agenteid = l.username;

        //Toast.makeText(this, agenteid, Toast.LENGTH_SHORT).show();


        multiAutoCompleteCarta = (AutoCompleteTextView) findViewById(R.id.multiAutoCompleteCarta);
        multiAutoinfracao = (AutoCompleteTextView) findViewById(R.id.multiAutoinfracao);
        multiAutodisposto = (Spinner) findViewById(R.id.multiAutodisposto);
        matriculaV = (AutoCompleteTextView) findViewById(R.id.matriculaV);
        LocalInfacao = (EditText) findViewById(R.id.LocalInfacao);

        multiAutodistrito = (AutoCompleteTextView) findViewById(R.id.multiAutodistrito);
        multiAutoprovincia = (AutoCompleteTextView) findViewById(R.id.multiAutoprovincia);
        multiAutoprovincia.setText(null);
        nameCondutor = (EditText) findViewById(R.id.nameCondutor);
        valor = (EditText) findViewById(R.id.valor);
        nota = (EditText) findViewById(R.id.nota);
        referenciaInfracao = (Spinner) findViewById(R.id.referenciaInfracao);
        tipoMulta = (Spinner) findViewById(R.id.tipoMulta);


        //allw network in main thread
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());
        //Retrive
        try {
            getnrcarta();
            getreferencia();
            getTipo_multa();
            //getinfracao();
            getprovincia();
            getmatricula();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        //adptative
        ArrayAdapter<String> adapter1;

        adapter1 = new ArrayAdapter(this, android.R.layout.select_dialog_item, nrcarta);
        multiAutoCompleteCarta.setThreshold(4);
        multiAutoCompleteCarta.setAdapter(adapter1);
        multiAutoCompleteCarta.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter1, View view, int i, long l) {
                findNameDriver(view);
            }
        });


        ArrayAdapter<String> adapter5 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, referencia);
        adapter5.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        referenciaInfracao.setAdapter(adapter5);

        referenciaInfracao.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapter5, View view, int i, long l) {

                String item = referenciaInfracao.getSelectedItem().toString();

                //Toast.makeText(CadastrarMulta.this, item, Toast.LENGTH_SHORT).show();
                getinfracao(view);


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }


        });

        ArrayAdapter<String> adapter9 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, TipoMUlta);
        adapter9.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tipoMulta.setAdapter(adapter9);


        ArrayAdapter<String> adapter4 = new ArrayAdapter(this, android.R.layout.select_dialog_item, provincia);
        multiAutoprovincia.setThreshold(2);
        multiAutoprovincia.setAdapter(adapter4);
        multiAutoprovincia.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter1, View view, int i, long l) {

                findDistrito(view);

            }
        });


        ArrayAdapter<String> adapter6 = new ArrayAdapter(this, android.R.layout.select_dialog_item, matricula);
        matriculaV.setThreshold(3);
        matriculaV.setAdapter(adapter6);

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


        try {

            alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("Cadastro Status");
            getidArtigo();
            // Toast.makeText(this, "1", Toast.LENGTH_SHORT).show();
            getidDisposto();
            //Toast.makeText(this, "2", Toast.LENGTH_SHORT).show();
            getidDistritoProvincia();
            //Toast.makeText(this, "3", Toast.LENGTH_SHORT).show();
            getidVeiculo();
            //Toast.makeText(this, "4", Toast.LENGTH_SHORT).show();
            getDadoscondutor();
            // Toast.makeText(this, "5", Toast.LENGTH_SHORT).show();
            getidTipoMulta();
            //Toast.makeText(this, "6", Toast.LENGTH_SHORT).show();


            Provincia = multiAutoprovincia.getText().toString();
            Distrito = multiAutodistrito.getText().toString();
            Nrcarta = multiAutoCompleteCarta.getText().toString();
            viaturaMatricula = matriculaV.getText().toString();
            detalhes = nota.getText().toString();
            moneyMulta = valor.getText().toString();
            nomeLocal = LocalInfacao.getText().toString();
            nomeReferencia = referenciaInfracao.getSelectedItem().toString();
            nomeResponsavel = tipoMulta.getSelectedItem().toString();



          /* String type = "cadastro";
            BackgroundWorkerCadastro bkc = new BackgroundWorkerCadastro(this);
            bkc.execute(type,idC,idA,idDisp, id_Veiculo,idDistri,idProv,agenteid,nota.getText().toString(),LocalInfacao.getText().toString(),idTipo_multa);
*/


            //String cadastro_url = "http://192.168.43.37/cadastrarMulta.php";

            String idCondutor = idC;
            String idArtigo = idA;
            String idDisposto = idDisp;
            String idVeiculo = id_Veiculo;
            String idDistrito = idDistri;
            String idProvincia = idProv;
            String idAgente = agenteid;
            String Descricao = nota.getText().toString();
            String local_multa = LocalInfacao.getText().toString();
            String idtipomulta = idTipo_multa;


//            Toast.makeText(this, "- " +idCondutor +" - " +idArtigo +" - " +idDisposto +" - " +idVeiculo +" - " +idDistrito
//                    +" - " +idProvincia +" - " +idAgente +" - " +Descricao +" - " +local_multa +" - " +idtipomulta, Toast.LENGTH_SHORT).show();


                URL url = new URL(wb.cadastro_url.toString());
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("idCondutor", "UTF-8") + "=" + URLEncoder.encode(idCondutor, "UTF-8") + "&"
                        + URLEncoder.encode("idArtigo", "UTF-8") + "=" + URLEncoder.encode(idArtigo, "UTF-8") + "&"
                        + URLEncoder.encode("idDisposto", "UTF-8") + "=" + URLEncoder.encode(idDisposto, "UTF-8") + "&"
                        + URLEncoder.encode("idVeiculo", "UTF-8") + "=" + URLEncoder.encode(idVeiculo, "UTF-8") + "&"
                        + URLEncoder.encode("idDistrito", "UTF-8") + "=" + URLEncoder.encode(idDistrito, "UTF-8") + "&"
                        + URLEncoder.encode("idProvincia", "UTF-8") + "=" + URLEncoder.encode(idProvincia, "UTF-8") + "&"
                        + URLEncoder.encode("idAgente", "UTF-8") + "=" + URLEncoder.encode(idAgente, "UTF-8") + "&"
                        + URLEncoder.encode("Descricao", "UTF-8") + "=" + URLEncoder.encode(Descricao, "UTF-8") + "&"
                        + URLEncoder.encode("local_multa", "UTF-8") + "=" + URLEncoder.encode(local_multa, "UTF-8") + "&"
                        + URLEncoder.encode("idtipomulta", "UTF-8") + "=" + URLEncoder.encode(idtipomulta, "UTF-8");
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

                if (result.equals("Multa cadastrada")) {
                    alertDialog.setMessage(result);
                    alertDialog.show();
                    Intent i = new Intent(this.getApplicationContext(), Comprovativo_multa.class);
                    this.startActivity(i);
                } else {
                    alertDialog.setMessage(result);
                    alertDialog.show();

                }




        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private void getnrcarta() throws MalformedURLException {
        InputStream is = null;
        String line = null;
        String result = null;
        try {
            URL url = new URL(wb.address_showcarta.toString());
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

            nrcarta = new String[ja.length()];

            for (int i = 0; i < ja.length(); i++) {
                jo = ja.getJSONObject(i);
                nrcarta[i] = jo.getString("nrcarta");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void getinfracao(View v) {
        InputStream is = null;
        String line = null;
        String result = null;
        String referenciaMulta = referenciaInfracao.getSelectedItem().toString();
        try {
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
        try {
            JSONArray ja = new JSONArray(result);
            JSONObject jo = null;

            infracao = new String[ja.length()];

            for (int i = 0; i < ja.length(); i++) {
                jo = ja.getJSONObject(i);
                infracao[i] = jo.getString("descricao");
            }

            ArrayAdapter<String> adapter3 = new ArrayAdapter(this, android.R.layout.select_dialog_item, infracao);
            multiAutoinfracao.setThreshold(2);
            multiAutoinfracao.setAdapter(adapter3);
            multiAutoinfracao.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapter3, View view, int i, long l) {

                    findDisposto(view);

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void mostrarTodosArtigos(final View v) {
        ArrayAdapter<String> adapter3 = new ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, infracao);

        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(CadastrarMulta.this);
        AlertDialog.Builder builder = alertDialog;
        builder.setTitle("Artigos");
        builder.setAdapter(adapter3, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                multiAutoinfracao.setText(infracao[item].toString());
                findDisposto(v);
                dialog.cancel();
            }
        });

        AlertDialog alert = alertDialog.create();


        alert.show();

    }


    public void getprovincia() throws MalformedURLException {
        InputStream is = null;
        String line = null;
        String result = null;
        try {
            URL url = new URL(wb.address_provincia.toString());
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

            provincia = new String[ja.length()];

            for (int i = 0; i < ja.length(); i++) {
                jo = ja.getJSONObject(i);
                provincia[i] = jo.getString("descricao");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void getmatricula() throws MalformedURLException {
        InputStream is = null;
        String line = null;
        String result = null;
        try {
            URL url = new URL(wb.address_matriculaviatura.toString());
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

            matricula = new String[ja.length()];

            for (int i = 0; i < ja.length(); i++) {
                jo = ja.getJSONObject(i);
                matricula[i] = jo.getString("matricula");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void getreferencia() throws MalformedURLException {
        InputStream is = null;
        String line = null;
        String result = null;
        try {
            URL url = new URL(wb.address_referencia.toString());
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

            referencia = new String[ja.length()];

            for (int i = 0; i < ja.length(); i++) {
                jo = ja.getJSONObject(i);

                referencia[i] = jo.getString("descricao");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void findNameDriver(View v) {
        String result = "";
        String line = "";
        String nrcarta = multiAutoCompleteCarta.getText().toString();
        try {
            URL url = new URL(wb.address_nomecondutor);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            String post_data = URLEncoder.encode("nrcarta", "UTF-8") + "=" + URLEncoder.encode(nrcarta, "UTF-8");
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
            nomeconduto = new String[ja.length()];

            jo = ja.getJSONObject(0);
            // Toast.makeText(this, jo.getString("nome"), Toast.LENGTH_SHORT).show();
            nameCondutor.setText("Condutor: " + jo.getString("nome"));

            condutor = nameCondutor.getText().toString();
            /*for(int i=0;i<ja.length();i++){
                jo=ja.getJSONObject(i);
                matricula[i]=jo.getString("matricula");
            }*/

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void findDisposto(View v) {
        String result = "";
        String line = "";


        String nomeArtigo = multiAutoinfracao.getText().toString();

        try {
            URL url = new URL(wb.address_disposto.toString());
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            String post_data = URLEncoder.encode("nomeArtigo", "UTF-8") + "=" + URLEncoder.encode(nomeArtigo, "UTF-8");
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
            disposto = new String[ja.length()];

            for (int i = 0; i < ja.length(); i++) {
                jo = ja.getJSONObject(i);
                disposto[i] = jo.getString("descricao");

            }


            ArrayAdapter<String> adapter5 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, disposto);
            adapter5.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            multiAutodisposto.setAdapter(adapter5);

            multiAutodisposto.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapter5, View view, int i, long l) {
                    item = adapter5.getItemAtPosition(i).toString();


                    findMoneyMulta(view);

                }


                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }


            });



            /*multiAutodisposto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
               @Override
                public void onItemClick(AdapterView<?> adapter5, View view, int i, long l) {
                    String item = adapter5.getItemAtPosition(i).toString();

                    Toast.makeText(CadastrarMulta.this, "click", Toast.LENGTH_SHORT).show();
                    final AlertDialog.Builder alertDialog = new AlertDialog.Builder(CadastrarMulta.this);
                    alertDialog.setTitle("Disposto")
                            .setMessage(item)
                            .setCancelable(false)
                            .setPositiveButton("OK",new DialogInterface.OnClickListener(){
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                }
                            });
                    AlertDialog alert = alertDialog.create();
                    alert.show();

                }
            });*/


        } catch (
                Exception e)

        {
            e.printStackTrace();
        }


    }


    public void findDistrito(View v) {
        String result = "";
        String line = "";
        String nomeProvincia = multiAutoprovincia.getText().toString();
        try {
            URL url = new URL(wb.address_distrito.toString());
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            String post_data = URLEncoder.encode("nomeProvincia", "UTF-8") + "=" + URLEncoder.encode(nomeProvincia, "UTF-8");
            bufferedWriter.write(post_data);
            //Toast.makeText(this, post_data, Toast.LENGTH_SHORT).show();
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

            //  Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
            // bufferedReader.close();
            inputStream.close();


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            // Toast.makeText(this, "Helloo ", Toast.LENGTH_SHORT).show();
            JSONArray ja = new JSONArray(result);
            JSONObject jo = null;
            //Toast.makeText(this, "passou ", Toast.LENGTH_SHORT).show();
            // nomeconduto=jo.getString("nome");
            distrito = new String[ja.length()];
            for (int i = 0; i < ja.length(); i++) {
                jo = ja.getJSONObject(i);
                distrito[i] = jo.getString("descricao");
            }

            ArrayAdapter<String> adapter = new ArrayAdapter(this, android.R.layout.select_dialog_item, distrito);
            multiAutodistrito.setThreshold(1);
            multiAutodistrito.setAdapter(adapter);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void findMoneyMulta(View v) {
        String result = "";
        String line = "";


        String nomeDisposto = multiAutodisposto.getSelectedItem().toString();
        try {
            URL url = new URL(wb.address_valormulta.toString());
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            String post_data = URLEncoder.encode("nomeDisposto", "UTF-8") + "=" + URLEncoder.encode(nomeDisposto, "UTF-8");
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
            valorMulta = new String[ja.length()];

            jo = ja.getJSONObject(0);
            // Toast.makeText(this, jo.getString("nome"), Toast.LENGTH_SHORT).show();
            valor.setText(jo.getString("valor") + "Mts");
            /*for(int i=0;i<ja.length();i++){
                jo=ja.getJSONObject(i);
                matricula[i]=jo.getString("matricula");
            }*/

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void mostrarArtigo(View v) {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(CadastrarMulta.this);
        alertDialog.setTitle("Disposto")
                .setMessage(item)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        AlertDialog alert = alertDialog.create();
        alert.show();
    }


    public void mostrarDetalhesCondutor(View v) {
        try {
            getDadoscondutor();


        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        String qtdTransito, qtdInatter, qtdTribunal;
        getdadosmultasCondutor(idC, "Transito");
        qtdTransito = nrMultas;
        nrMultas = null;
        getdadosmultasCondutor(idC, "INATTER");
        qtdInatter = nrMultas;
        nrMultas = null;
        getdadosmultasCondutor(idC, "Tribunal");
        qtdTribunal = nrMultas;


        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(CadastrarMulta.this);
        alertDialog.setTitle("Estado do condutor")
                .setMessage(qtdTransito + "\n" + qtdInatter + "\n" + qtdTribunal)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        AlertDialog alert = alertDialog.create();
        alert.show();
    }


    public void getdadosmultasCondutor(String idcondutor, String op) {
        String result = "";
        String line = "";
        try {
            URL url = new URL(wb.address_detalhesMulta.toString());
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            String post_data = URLEncoder.encode("idcondutor", "UTF-8") + "=" + URLEncoder.encode(idcondutor, "UTF-8") + "&"
                    + URLEncoder.encode("op", "UTF-8") + "=" + URLEncoder.encode(op, "UTF-8");
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
            nrmultas = new String[ja.length()];

            jo = ja.getJSONObject(0);

            if (op.equals("Transito")) {

                nrMultas = "Multas Transito: " + (jo.getString("multas_transito"));// +"Multas INATTER: " +(jo.getString("multas_INATTER"))

            } else if (op.equals("INATTER")) {
                nrMultas = "Multas INATTER: " + (jo.getString("multas_INATTER"));

            } else if (op.equals("Tribunal")) {

                nrMultas = "Multas Tribunal: " + (jo.getString("multas_Tribunal"));
            }

//            //  +"Multas Tribunal: " + (jo.getString("multas_Tribunal"));
//            Toast.makeText(this, "numero multas: " +nrMultas, Toast.LENGTH_SHORT).show();


        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    public void getDadoscondutor() throws MalformedURLException {
        String result = "";
        String line = "";
        String nrcarta = multiAutoCompleteCarta.getText().toString();
        try {
            URL url = new URL(wb.address_idcondutor.toString());
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            String post_data = URLEncoder.encode("nrcarta", "UTF-8") + "=" + URLEncoder.encode(nrcarta, "UTF-8");
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
            idco = new String[ja.length()];

            jo = ja.getJSONObject(0);

            idC = (jo.getString("idcondutor"));
          //  Toast.makeText(this, "idcondutor" + idC, Toast.LENGTH_SHORT).show();


        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void getidArtigo() throws MalformedURLException {

        String result = "";
        String line = "";
        nomeArtigo = multiAutoinfracao.getText().toString();
        try {
            URL url = new URL(wb.address_idartigo.toString());
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            String post_data = URLEncoder.encode("nomeArtigo", "UTF-8") + "=" + URLEncoder.encode(nomeArtigo, "UTF-8");
            bufferedWriter.write(post_data);
            // Toast.makeText(this, post_data, Toast.LENGTH_SHORT).show();
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
            // Toast.makeText(this, "passou ", Toast.LENGTH_SHORT).show();
            // nomeconduto=jo.getString("nome");
            idart = new String[ja.length()];

            jo = ja.getJSONObject(0);
            idA = (jo.getString("idartigo"));

          //  Toast.makeText(this, "idARtigo" + idA, Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void getidDisposto() throws MalformedURLException {

        String result = "";
        String line = "";
        nomeDisposto = multiAutodisposto.getSelectedItem().toString();
        try {
            URL url = new URL(wb.address_iddisposto.toString());
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            String post_data = URLEncoder.encode("nomeDisposto", "UTF-8") + "=" + URLEncoder.encode(nomeDisposto, "UTF-8");
            bufferedWriter.write(post_data);
            // Toast.makeText(this, post_data, Toast.LENGTH_SHORT).show();
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

           // Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
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
            // Toast.makeText(this, "passou ", Toast.LENGTH_SHORT).show();
            // nomeconduto=jo.getString("nome");
            iddisp = new String[ja.length()];

            jo = ja.getJSONObject(0);

            idDisp = (jo.getString("iddisposto"));
            idcontravensao = (jo.getString("idcontravensao"));
           // Toast.makeText(this, "idisposto" + idDisp, Toast.LENGTH_LONG).show();

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void getidVeiculo() throws MalformedURLException {

        String result = "";
        String line = "";
        String veiculoMatricula = matriculaV.getText().toString();
        try {
            URL url = new URL(wb.address_idveiculo.toString());
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            String post_data = URLEncoder.encode("veiculoMatricula", "UTF-8") + "=" + URLEncoder.encode(veiculoMatricula, "UTF-8");
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
            idveiculo = new String[ja.length()];

            jo = ja.getJSONObject(0);

            id_Veiculo = (jo.getString("idveiculo"));
           // Toast.makeText(this, "idvei" + id_Veiculo, Toast.LENGTH_SHORT).show();


        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void getidDistritoProvincia() throws MalformedURLException {

        String result = "";
        String line = "";
        String distritoNome = multiAutodistrito.getText().toString();
        try {
            URL url = new URL(wb.address_iddistrito.toString());
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            String post_data = URLEncoder.encode("distritoNome", "UTF-8") + "=" + URLEncoder.encode(distritoNome, "UTF-8");
            bufferedWriter.write(post_data);
            //Toast.makeText(this, post_data, Toast.LENGTH_SHORT).show();
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
            iddistri = new String[ja.length()];

            jo = ja.getJSONObject(0);

            idDistri = (jo.getString("iddistrito"));
            idProv = (jo.getString("idprovincia"));
          //  Toast.makeText(this, idDistri + idProv, Toast.LENGTH_SHORT).show();
            //Toast.makeText(this, idProv, Toast.LENGTH_SHORT).show();


        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void getidagente(String idAgente) {

        agenteid = idAgente;
        //Toast.makeText(this, agenteid, Toast.LENGTH_SHORT).show();

    }

    public void cleanSpace(View view) {
        multiAutoCompleteCarta.setText(null);
        multiAutoinfracao.setText(null);
        multiAutodisposto.setEnabled(false);
        multiAutodistrito.setText(null);
        multiAutodistrito.setEnabled(false);
        multiAutoprovincia.setText(null);
        matriculaV.setText(null);
        nameCondutor.setText(null);
        valor.setText(null);
        nota.setText(null);


    }

    public void getTipo_multa() throws MalformedURLException {
        InputStream is = null;
        String line = null;
        String result = null;
        try {
            URL url = new URL(wb.address_tipo_multa.toString());
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

            TipoMUlta = new String[ja.length()];

            for (int i = 0; i < ja.length(); i++) {
                jo = ja.getJSONObject(i);

                TipoMUlta[i] = jo.getString("descricao");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void finddadosVeiculo(View view) {
        String result = "";
        String line = "";
        String matricula = matriculaV.getText().toString();
        try {
            URL url = new URL(wb.address_dadosVeiculo.toString());
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            String post_data = URLEncoder.encode("matricula", "UTF-8") + "=" + URLEncoder.encode(matricula, "UTF-8");
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
            viatura = new String[ja.length()];

            jo = ja.getJSONObject(0);
            // Toast.makeText(this, jo.getString("nome"), Toast.LENGTH_SHORT).show();


            marca = (jo.getString("marcaveiculo"));
            servico = (jo.getString("servicoVeiculo"));
            tipo = (jo.getString("tipo"));
            cor = (jo.getString("cor"));



        } catch (JSONException e1) {
            e1.printStackTrace();
        }

        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Dados do veiculo:")
                .setMessage("Marca: " + marca + "\n"
                        + "Cor: " + cor + "\n"
                        + "Tipo: " + tipo + "\n"
                        + "Serviço: " + servico)
                .setCancelable(false)
                .setPositiveButton("Entendi", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        AlertDialog alert = alertDialog.create();
        alert.show();
    }


        //  condutor=nameCondutor.getText().toString();
            /*for(int i=0;i<ja.length();i++){
                jo=ja.getJSONObject(i);
                matricula[i]=jo.getString("matricula");
            }*/





    public void getidTipoMulta() throws MalformedURLException {

        String result="";
        String line="";
        String nomeTipo_multa = tipoMulta.getSelectedItem().toString();
        try {
            URL url = new URL(wb.address_idtipo_multa.toString());
            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            String post_data = URLEncoder.encode("nomeTipo_multa","UTF-8")+"="+URLEncoder.encode(nomeTipo_multa,"UTF-8");
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
            idtipo_multa = new String[ja.length()];

            jo=ja.getJSONObject(0);

            idTipo_multa=(jo.getString("idtipo_multa"));
           //  Toast.makeText(this, "idtipo-"+idTipo_multa, Toast.LENGTH_SHORT).show();



        }catch (Exception e)
        {
            e.printStackTrace();
        }




    }









}





