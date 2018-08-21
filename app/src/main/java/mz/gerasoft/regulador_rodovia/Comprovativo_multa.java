package mz.gerasoft.regulador_rodovia;

import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
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

public class Comprovativo_multa extends AppCompatActivity {
    TextView autoTransgressaonr,avisonr,codagente,dataHora,prov,distr,agenteName,condutorNome,condutorCarta,categoriaCarta,
            localEmis,dataEmis,estadoC,biNR,artigo,Infracao,nrdisposto,referenciaMulta,responsavelmulta,localMultaC,MarcaVia,MatriculaVia,ServicoV,notaM,valorM;
    String autoAviso[];
    String data[];
    String agenteN[];
    String carta[];

    @Override
    public void onBackPressed() {
        Intent i = new Intent(this, CadastrarMulta.class);
        startActivity(i);
        super.onBackPressed();
    }

    String viatura[],dispost[];
    webMethodUrl wb= new webMethodUrl();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ActionBar actionbar = getSupportActionBar();

        actionbar.setTitle("Comprovativo da multa");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comprovativo_multa);

        autoTransgressaonr=(TextView) findViewById(R.id.autoTransgressaonr);
        avisonr = (TextView) findViewById(R.id.avisonr);
        codagente = (TextView) findViewById(R.id.codagente);
        dataHora = (TextView) findViewById(R.id.dataHora);
        prov = (TextView) findViewById(R.id.prov);
        distr = (TextView) findViewById(R.id.distr);
        agenteName = (TextView) findViewById(R.id.agenteName);
        condutorNome = (TextView) findViewById(R.id.condutorNome);
        condutorCarta = (TextView) findViewById(R.id.condutorCarta);
        categoriaCarta = (TextView) findViewById(R.id.categoriaCarta);
        localEmis = (TextView) findViewById(R.id.localEmis);
        dataEmis = (TextView) findViewById(R.id.dataEmis);
        estadoC = (TextView) findViewById(R.id.estadoC);
        biNR = (TextView) findViewById(R.id.biNR);
        artigo = (TextView) findViewById(R.id.artigo);
        //Infracao = (TextView) findViewById(R.id.Infracao);
        nrdisposto = (TextView) findViewById(R.id.nrdisposto);
        referenciaMulta = (TextView) findViewById(R.id.referenciaMulta);
        responsavelmulta = (TextView) findViewById(R.id.responsavelmulta);
        localMultaC = (TextView) findViewById(R.id.localMultaC);
        MarcaVia = (TextView) findViewById(R.id.MarcaVia);
        MatriculaVia = (TextView) findViewById(R.id.MatriculaVia);
        ServicoV = (TextView) findViewById(R.id.ServicoV);
        notaM = (TextView) findViewById(R.id.notaM);
        valorM = (TextView) findViewById(R.id.valorM);





        login l = new login();

        codagente.setText(l.username);

        CadastrarMulta Cm = new CadastrarMulta();
        prov.setText(Cm.Provincia);
        distr.setText(Cm.Distrito);
        condutorNome.setText(Cm.condutor.replaceAll("Condutor:",""));
        condutorCarta.setText(Cm.Nrcarta);
        artigo.setText(Cm.idA);
        MatriculaVia.setText(Cm.viaturaMatricula);
        if(Cm.detalhes.length()>25) {
            notaM.setText(Cm.detalhes.substring(0, 10));
        }else{
            notaM.setText(Cm.detalhes);
        }
        valorM.setText(Cm.moneyMulta);
        if(Cm.nomeReferencia.length()>25) {
            referenciaMulta.setText(Cm.nomeReferencia.substring(0,10));
        }else{
            referenciaMulta.setText(Cm.nomeReferencia);
        }

        responsavelmulta.setText(Cm.nomeResponsavel);

        if(Cm.nomeLocal.length()>25) {
            localMultaC.setText(Cm.nomeLocal.substring(0,10));
        }else{
            localMultaC.setText(Cm.nomeLocal);
        }






        //allw network in main thread
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());
        //Retrive
        try {
            getavisoAuto();
            getData();
            findNameAgente();
            finddadosCarta();
            finddadosVeiculo();
            getidDisposto();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        //adptative

    }

    public void getidDisposto()throws MalformedURLException {

        String result = "";
        String line = "";
        String nomeDisposto = CadastrarMulta.idDisp;
        try {
            URL url = new URL(wb.address_nrdisposto.toString());
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

            Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
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
            dispost = new String[ja.length()];

            jo = ja.getJSONObject(0);

            nrdisposto.setText((jo.getString("numero_disposto")));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getavisoAuto() throws MalformedURLException {
        InputStream is = null;
        String line=null;
        String result=null;
        try{
            URL url = new URL(wb.address_autoaviso.toString());
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

            autoAviso = new String[ja.length()];


            jo=ja.getJSONObject(0);
            autoTransgressaonr.setText(jo.getString("MAX(nr_auto)"));
            avisonr.setText(jo.getString("MAX(nr_aviso)"));

        }catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    private void getData() throws MalformedURLException {
        InputStream is = null;
        String line=null;
        String result=null;
        try{
            URL url = new URL(wb.address_data.toString());
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

            data = new String[ja.length()];


            jo=ja.getJSONObject(0);
            dataHora.setText(jo.getString("MAX(datamulta)"));

        }catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    public void findNameAgente() throws MalformedURLException {
        String result="";
        String line="";
        String cAgente = codagente.getText().toString();
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
            agenteName.setText(jo.getString("nome"));

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

    public void goMenu(View v){
        Intent i = new Intent(this,main.class);
        startActivity(i);
    }

    public void finddadosCarta() throws MalformedURLException {
        String result="";
        String line="";
        String numeroCarta =condutorCarta.getText().toString();
        try {
            URL url = new URL(wb.address_dadosCarta.toString());
            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            String post_data = URLEncoder.encode("numeroCarta","UTF-8")+"="+URLEncoder.encode(numeroCarta,"UTF-8");
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
            carta = new String[ja.length()];

            jo=ja.getJSONObject(0);
            // Toast.makeText(this, jo.getString("nome"), Toast.LENGTH_SHORT).show();
            localEmis.setText(jo.getString("provinciaDescricao"));
            categoriaCarta.setText(jo.getString("categoriaCarta"));
            dataEmis.setText(jo.getString("emissao"));
            estadoC.setText(jo.getString("estadoCivil"));
            biNR.setText(jo.getString("nr_bi"));


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

    public void finddadosVeiculo() throws MalformedURLException {
        String result="";
        String line="";
        String matricula =MatriculaVia.getText().toString();
        try {
            URL url = new URL(wb.address_dadosVeiculo.toString());
            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            String post_data = URLEncoder.encode("matricula","UTF-8")+"="+URLEncoder.encode(matricula,"UTF-8");
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
            viatura = new String[ja.length()];

            jo=ja.getJSONObject(0);
            // Toast.makeText(this, jo.getString("nome"), Toast.LENGTH_SHORT).show();
           MarcaVia.setText(jo.getString("marcaveiculo"));
           ServicoV.setText(jo.getString("servicoVeiculo"));


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
