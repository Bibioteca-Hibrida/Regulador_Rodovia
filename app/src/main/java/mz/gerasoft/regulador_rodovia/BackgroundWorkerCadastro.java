package mz.gerasoft.regulador_rodovia;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

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

/**
 * Created by Miguel Jr on 11/13/2017.
 */

public class BackgroundWorkerCadastro extends AsyncTask<String,Void,String> {
    AlertDialog alertDialog;
    Context context;
    ProgressDialog pd;
    webMethodUrl wb=new webMethodUrl();

    BackgroundWorkerCadastro(Context ctx) {
        context = ctx;
    }
    @Override
    protected String doInBackground(String...params) {
        String type = params[0];
        alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle("Cadastro Status");
        alertDialog.show();

        if(type.equals("cadastro")) try {

            String idCondutor = params[1];
            String idArtigo = params[2];
            String idDisposto = params[3];
            String idVeiculo = params[4];
            String idDistrito = params[5];
            String idProvincia = params[6];
            String idAgente = params[7];
            String Descricao = params[8];
           String local_multa = params[9];
            String idtipomulta = params[10];



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

            return result;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle("Cadastro Status");

    }

    @Override
    protected void onPostExecute(String result) {
       if(result.equals("Multa cadastrada")) {
            alertDialog.setMessage(result);
            alertDialog.show();
            Intent i = new Intent(context.getApplicationContext(),Comprovativo_multa.class);
            context.startActivity(i);
        }else {
            alertDialog.setMessage(result);
            alertDialog.show();

        }

    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
       alertDialog.dismiss();

    }



}



