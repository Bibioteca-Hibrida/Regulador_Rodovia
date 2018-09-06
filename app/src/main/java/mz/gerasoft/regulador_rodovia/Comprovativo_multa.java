package mz.gerasoft.regulador_rodovia;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;

public class Comprovativo_multa extends AppCompatActivity {
//    TextView autoTransgressaonr,avisonr,codagente,dataHora,prov,distr,agenteName,condutorNome,condutorCarta,categoriaCarta,
//            localEmis,dataEmis,estadoC,biNR,artigo,Infracao,nrdisposto,referenciaMulta,responsavelmulta,localMultaC,MarcaVia,MatriculaVia,ServicoV,notaM,valorM;
    String autoAviso[];
    String data[];
    String agenteN[];
    String carta[];
    login l = new login();
    String titulodatahora="";

    //codagente.setText(l.username);

    CadastrarMulta Cm = new CadastrarMulta();
    String multa="Auto de transgressão" +"\n";

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

//        autoTransgressaonr=(TextView) findViewById(R.id.autoTransgressaonr);
//        avisonr = (TextView) findViewById(R.id.avisonr);
//        codagente = (TextView) findViewById(R.id.codagente);
//        dataHora = (TextView) findViewById(R.id.dataHora);
//        prov = (TextView) findViewById(R.id.prov);
//        distr = (TextView) findViewById(R.id.distr);
//        agenteName = (TextView) findViewById(R.id.agenteName);
//        condutorNome = (TextView) findViewById(R.id.condutorNome);
//        condutorCarta = (TextView) findViewById(R.id.condutorCarta);
//        categoriaCarta = (TextView) findViewById(R.id.categoriaCarta);
//        localEmis = (TextView) findViewById(R.id.localEmis);
//        dataEmis = (TextView) findViewById(R.id.dataEmis);
//        estadoC = (TextView) findViewById(R.id.estadoC);
//        biNR = (TextView) findViewById(R.id.biNR);
//        artigo = (TextView) findViewById(R.id.artigo);
//        //Infracao = (TextView) findViewById(R.id.Infracao);
//        nrdisposto = (TextView) findViewById(R.id.nrdisposto);
//        referenciaMulta = (TextView) findViewById(R.id.referenciaMulta);
//        responsavelmulta = (TextView) findViewById(R.id.responsavelmulta);
//        localMultaC = (TextView) findViewById(R.id.localMultaC);
//        MarcaVia = (TextView) findViewById(R.id.MarcaVia);
//        MatriculaVia = (TextView) findViewById(R.id.MatriculaVia);
//        ServicoV = (TextView) findViewById(R.id.ServicoV);
//        notaM = (TextView) findViewById(R.id.notaM);
//        valorM = (TextView) findViewById(R.id.valorM);
//






//        prov.setText(Cm.Provincia);
//        distr.setText(Cm.Distrito);
//        condutorNome.setText(Cm.condutor.replaceAll("Condutor:",""));
//        condutorCarta.setText(Cm.Nrcarta);
//        artigo.setText(Cm.idA);
//        MatriculaVia.setText(Cm.viaturaMatricula);
//        if(Cm.detalhes.length()>25) {
//            notaM.setText(Cm.detalhes.substring(0, 10));
//        }else{
//            notaM.setText(Cm.detalhes);
//        }
//        valorM.setText(Cm.moneyMulta);
//        if(Cm.nomeReferencia.length()>25) {
//            referenciaMulta.setText(Cm.nomeReferencia.substring(0,10));
//        }else{
//            referenciaMulta.setText(Cm.nomeReferencia);
//        }
//
//        responsavelmulta.setText(Cm.nomeResponsavel);
//
//        if(Cm.nomeLocal.length()>25) {
//            localMultaC.setText(Cm.nomeLocal.substring(0,10));
//        }else{
//            localMultaC.setText(Cm.nomeLocal);
//        }






        //allw network in main thread
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());
        //Retrive
        try {
            getavisoAuto();
            findNameAgente();
            getData();
            multa+="Data e hora: " +titulodatahora +"\n"
                    +"Provincia: " +Cm.Provincia +"\n"
                    +"Distrito: " +Cm.Distrito +"\n"
                    +"Nome condutor: " +(Cm.condutor.replaceAll("Condutor:","")) +"\n"
                    +"Numero de carta: " +(Cm.Nrcarta) +"\n";
            finddadosCarta();
            getidDisposto();
            multa+="Referenciao da transgressão: " +Cm.nomeReferencia +"\n"
                    +"Responsavel da multa: " +(Cm.nomeResponsavel) +"\n"
                    +"Local da transgressão: " +(Cm.nomeLocal) +"\n";

            finddadosVeiculo();

            Toast.makeText(this,titulodatahora, Toast.LENGTH_LONG).show();

            int i;
            String aux="";
            for(i=0;i<titulodatahora.length();i++){
             if((titulodatahora.charAt(i)=='-') || ((titulodatahora.charAt(i)==':'))) {
                 aux+='_';
             }else{
                 aux+=titulodatahora.charAt(i);
             }

            }

           escrever(multa,aux);




            final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setTitle("Recibo da multa")
                    .setMessage(multa)
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
            AlertDialog alert = alertDialog.create();
            alert.show();


        } catch (IOException e) {
            e.printStackTrace();
        }
        //adptative



    }


    public void escrever(String msg,String titulo) throws IOException {

        URL url = null;
        try {
            url = new URL(wb.escrever.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        HttpURLConnection httpURLConnection = null;
        try {
            httpURLConnection = (HttpURLConnection)url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        httpURLConnection.setRequestMethod("POST");
        httpURLConnection.setDoOutput(true);
        httpURLConnection.setDoInput(true);
        OutputStream outputStream = httpURLConnection.getOutputStream();
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
        String post_data = URLEncoder.encode("titulo","UTF-8")+"="+URLEncoder.encode(titulo,"UTF-8")+"&"
                +URLEncoder.encode("msg","UTF-8")+"="+URLEncoder.encode(msg,"UTF-8");
        bufferedWriter.write(post_data);
        bufferedWriter.flush();
        bufferedWriter.close();
        outputStream.close();
        InputStream inputStream = httpURLConnection.getInputStream();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
        String result="";
        String line="";
        while((line = bufferedReader.readLine())!= null) {
            result += line;
        }
        bufferedReader.close();
        inputStream.close();
        httpURLConnection.disconnect();


    }

    public String ler(String titulo) throws IOException {

        URL url = null;
        try {
            url = new URL(wb.ler.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        HttpURLConnection httpURLConnection = null;
        try {
            httpURLConnection = (HttpURLConnection)url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        httpURLConnection.setRequestMethod("POST");
        httpURLConnection.setDoOutput(true);
        httpURLConnection.setDoInput(true);
        OutputStream outputStream = httpURLConnection.getOutputStream();
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
        String post_data = URLEncoder.encode("titulo","UTF-8")+"="+URLEncoder.encode(titulo,"UTF-8");
        bufferedWriter.write(post_data);
        bufferedWriter.flush();
        bufferedWriter.close();
        outputStream.close();
        InputStream inputStream = httpURLConnection.getInputStream();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
        String result="";
        String line="";
        while((line = bufferedReader.readLine())!= null) {
            result += line;
        }
        bufferedReader.close();
        inputStream.close();
        httpURLConnection.disconnect();

        return result;



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

           // nrdisposto.setText((jo.getString("numero_disposto")));
            multa+="Disposto nr: " +(jo.getString("numero_disposto")) +"\n";

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
//            autoTransgressaonr.setText(jo.getString("MAX(nr_auto)"));
//            avisonr.setText(jo.getString("MAX(nr_aviso)"));
            multa+="Auto de transgressão nr: " +(jo.getString("MAX(nr_auto)")) +"\n"
                    +"Aviso nr: " +(jo.getString("MAX(nr_aviso)") +"\n"
                    +"Codigo agente nr: " +l.username +"\n");

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
            titulodatahora=(jo.getString("MAX(datamulta)"));



        }catch (Exception e)
        {
            e.printStackTrace();
        }

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
            multa+="Nome agente: " +(jo.getString("nome")) +"\n";

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
        String numeroCarta =Cm.Nrcarta;
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
          //  localEmis.setText(jo.getString("provinciaDescricao"));
            //categoriaCarta.setText(jo.getString("categoriaCarta"));
           // dataEmis.setText(jo.getString("emissao"));
            //estadoC.setText(jo.getString("estadoCivil"));
            //biNR.setText(jo.getString("nr_bi"));

            multa+="Categoria da carta: " +(jo.getString("categoriaCarta")) +"\n"
                    +"Local de emissão: " +(jo.getString("provinciaDescricao")) +"\n"
                    +"Data de emissão: " + (jo.getString("emissao")) +"\n"
                    +"Estado civil: " +(jo.getString("estadoCivil")) +"\n"
                    +"B.I. nr: "    + (jo.getString("nr_bi"))+"\n"
            +"Artigo nr: " +Cm.idA +"\n";




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
        String matricula =Cm.viaturaMatricula;
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
//           MarcaVia.setText(jo.getString("marcaveiculo"));
//           ServicoV.setText(jo.getString("servicoVeiculo"));

            multa+="Marca da viatura: " +(jo.getString("marcaveiculo")) +"\n"
                    +"Matricula da viatura: " +(Cm.viaturaMatricula) +"\n"
                    +"Serviço da viatura: " +(jo.getString("servicoVeiculo")) +"\n"
                    +"Descrição do auto de transgressão: " +(Cm.detalhes) +"\n"
                    +"Valor da multa: " +(Cm.moneyMulta) +"\n";
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
