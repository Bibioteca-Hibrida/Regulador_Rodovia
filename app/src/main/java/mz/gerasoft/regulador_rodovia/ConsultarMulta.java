package mz.gerasoft.regulador_rodovia;

import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
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
import java.util.ArrayList;
import java.util.List;

public class ConsultarMulta extends AppCompatActivity {
    AutoCompleteTextView editText2;
    ArrayList<String> dados= new ArrayList<>();
    ListView Multas;
    TextView nrmultas;
    Spinner mes,ano,estado;
//    RadioButton radioButton4,radioButton5;
    RadioGroup radioGrup2;
    webMethodUrl wb=new webMethodUrl();
    String nrcarta[],nomeCondutor[];
    public static String Nrcarta,nomecondutor;
    String idco[],multa[],nrc[];
    String idC,nrC,paramMes,paramAno,paramEstado;
    ArrayAdapter<String> adapter1;
    String Mes []= {"Todos meses","Janeiro","Fevereiro","Março","Abril","Maio","Junho","Julho","Agosto","Setembro","Outubro","Novembro","Dezembro"};
    String Ano [] = {"Todos anos","2018","2017","2016"};
    String Estado[] = {"Todos estados","Transito","INATTER","Tribunal","Pago"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultar_multa);

        mes = ((Spinner) findViewById(R.id.spinner));
        ano = ((Spinner) findViewById(R.id.spinner2));
        estado = ((Spinner) findViewById(R.id.spinner3));
        Multas = ((ListView) findViewById(R.id.lista));
        editText2 = ((AutoCompleteTextView) findViewById(R.id.editText2));
//        radioButton4 = ((RadioButton) findViewById(R.id.radioButton4));
//        radioButton5 = ((RadioButton) findViewById(R.id.radioButton5));
        nrmultas = ((TextView) findViewById(R.id.nrMul));

        try {
            getnomeCondutor();
            getnrcarta();



//
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

mes.setEnabled(false);
        estado.setEnabled(false);
        ano.setEnabled(false);
        for(int i=0;i<nomeCondutor.length;i++){
            dados.add(nomeCondutor[i]);
        }
        for(int i=0;i<nrcarta.length;i++){
            dados.add(nrcarta[i]);
        }
        editText2.setHint("Digite o numero da carta/nome do condutor");
            ArrayAdapter<String> adapter1;
            adapter1 = new ArrayAdapter(this,android.R.layout.select_dialog_item,dados);
            editText2.setThreshold(2);
            editText2.setAdapter(adapter1);
        editText2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapter1, View view, int i, long l) {

                            Multas.setAdapter(null);
                            if (editText2.getText().toString().charAt(0) >= '0' && editText2.getText().toString().charAt(0) <= '9') {
                                getMultasbyNumber();
                                inicializarFiltros();
                            } else {
                                getNrCarta();
                                editText2.setText(nrC);
                                Multas.setAdapter(null);
                                getMultasbyNumber();
                                inicializarFiltros();

                        }
                    }
                });




//        radioButton4.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                editText2.setText(null);
//
//                editText2.setHint("Digite o nome do condutor");
//                //allw network in main thread
//                StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());
//                //Retrive
//                try {
//                    getnomeCondutor();
//
//
//                } catch (MalformedURLException e) {
//                    e.printStackTrace();
//                }
//                editText2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(AdapterView<?> adapter1, View view, int i, long l) {
//                        getNrCarta();
//                        editText2.setText(nrC);
//                        Multas.setAdapter(null);
//                        getMultasbyNumber();
//
//                    }
//                });
//            }
//
//
//            });
//
//        radioButton5.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                editText2.setText(null);
//
//                editText2.setHint("Digite o numero da carta do condutor");
//
//                //allw network in main thread
//                StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());
//                //Retrive
//                try {
//                    getnrcarta();
//
//
//                } catch (MalformedURLException e) {
//                    e.printStackTrace();
//                }
//
//                editText2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(AdapterView<?> adapter1, View view, int i, long l) {
//                        Multas.setAdapter(null);
//                        getMultasbyNumber();
//                    }
//                });
//
//            }
//
//
//        });
    }

    public void getNrCarta(){
        String result="";
        String line="";
        String nomeC = editText2.getText().toString();
        try {
            URL url = new URL(wb.address_shownrcarta);
            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            String post_data = URLEncoder.encode("nomeC","UTF-8")+"="+URLEncoder.encode(nomeC,"UTF-8");
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
            JSONArray ja = new JSONArray(result);
            JSONObject jo= null;
           nrc = new String[ja.length()];


                jo=ja.getJSONObject(0);
                nrC=jo.getString("nrcarta");



        }catch (Exception e)
        {
            e.printStackTrace();
        }




    }

    private void inicializarFiltros(){
        mes.setEnabled(false);
        estado.setEnabled(true);
        ano.setEnabled(false);

        ArrayAdapter<String> adapter5 = new ArrayAdapter(this,android.R.layout.simple_spinner_item,Mes);
        adapter5.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mes.setAdapter(adapter5);

        mes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapter5, View view, int i, long l) {

                paramMes= mes.getSelectedItem().toString();


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }


        });


        ArrayAdapter<String> adapter6 = new ArrayAdapter(this,android.R.layout.simple_spinner_item,Ano);
        adapter5.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ano.setAdapter(adapter6);
        ano.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapter6, View view, int i, long l) {

                paramAno= ano.getSelectedItem().toString();


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }


        });

        ArrayAdapter<String> adapter7 = new ArrayAdapter(this,android.R.layout.simple_spinner_item,Estado);
        adapter5.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        estado.setAdapter(adapter7);


        estado.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapter7, View view, int i, long l) {

                paramEstado= estado.getSelectedItem().toString();
                if(!paramEstado.equals("Todos estados")) {
                    Multas.setAdapter(null);
                    getMultasbyNumberFilter(paramEstado);
                }else{
                    getMultasbyNumber();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }


        });
    }



    public void getDadoscondutor(){
        String result="";
        String line="";
        String nrcarta = editText2.getText().toString();
        try {
            URL url = new URL(wb.address_idcondutor.toString());
            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            String post_data = URLEncoder.encode("nrcarta","UTF-8")+"="+URLEncoder.encode(nrcarta,"UTF-8");
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
            idco = new String[ja.length()];

            jo=ja.getJSONObject(0);

            idC=(jo.getString("idcondutor"));
            Toast.makeText(this, "idcondutor" +idC, Toast.LENGTH_SHORT).show();


        }catch (Exception e)
        {
            e.printStackTrace();
        }



    }

    private void getMultasbyNumber() {
        getDadoscondutor();
        InputStream is = null;
        String line=null;
        String result=null;
        String idcondutor=idC;
        try{
            URL url = new URL(wb.address_showMultaCondutor2.toString());
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            String post_data = URLEncoder.encode("idcondutor","UTF-8") + "=" + URLEncoder.encode(idcondutor,"UTF-8");
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


            multa = new String[ja.length()];

            for (int i = 0; i < ja.length(); i++) {
                jo = ja.getJSONObject(i);

                //if((!paramMes.equals("Todos meses")) && (!paramAno.equals("Todos anos")) && (!paramEstado.equals("Todos estados"))){
                    //multa[i] = "Artigo numero: " + jo.getString("nr_artigo") + "\n" + "Disposto numero: " + jo.getString("numero_disposto") + "\n"
                    //        + "Estado da multa: " + jo.getString("estado") + "\n" + "Data e Hora: " + jo.getString("datamulta") + "\n"
                  //          + "Valor da multa: " + jo.getString("valor") + "Mts";
                //}else if((paramMes.equals(jo.getString("datamulta").substring(5,6))) && (paramAno.equals(jo.getString("datamulta").substring(0,3))) && (paramEstado.equals(jo.getString("estado")))
                    //|| (paramMes.equals(jo.getString("datamulta").substring(5,6))) && (!paramAno.equals(jo.getString("datamulta").substring(0,3))) && (!paramEstado.equals(jo.getString("estado"))) ||
                  //      (!paramMes.equals(jo.getString("datamulta").substring(5,6))) && (paramAno.equals(jo.getString("datamulta").substring(0,3))) && (!paramEstado.equals(jo.getString("estado"))) ||
                //(!paramMes.equals(jo.getString("datamulta").substring(5,6))) && (!paramAno.equals(jo.getString("datamulta").substring(0,3))) && (paramEstado.equals(jo.getString("estado")))) {
                    multa[i] = "Artigo numero: " + jo.getString("nr_artigo") + "\n" + "Disposto numero: " + jo.getString("numero_disposto") + "\n"
                            + "Estado da multa: " + jo.getString("estado") + "\n" + "Data e Hora: " + jo.getString("datamulta") + "\n"
                            + "Valor da multa: " + jo.getString("valor") + "Mts";
                //}
            }

            ArrayAdapter<String> adapter2;
            adapter2 = new ArrayAdapter(this, android.R.layout.select_dialog_item, multa);
            Multas.setAdapter(adapter2);
            nrmultas.setText("Numero de multa/as: " + ja.length());

            Multas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapter1, View view, int i, long l) {
                   String item= adapter1.getItemAtPosition(i).toString();
                    String numero_artigo="";
                    for(int j=0;j<item.length();j++){
                        if(item.charAt(j)=='D' && item.charAt(j+1)=='a' && item.charAt(j+2)=='t'&& item.charAt(j+3)=='a'
                                && item.charAt(j+4)==' ' && item.charAt(j+5)=='e' && item.charAt(j+6)==' ' && item.charAt(j+7)=='H'
                                && item.charAt(j+8)=='o' && item.charAt(j+9)=='r' && item.charAt(j+10)=='a') {
                            j=j+12;
                            numero_artigo="";
                            for (int k=j;k<j+20;k++){
                                numero_artigo+=item.charAt(k);
                            }break;

                        }
                    }

                    Toast.makeText(ConsultarMulta.this, numero_artigo, Toast.LENGTH_SHORT).show();




                }
            });


        }catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    public void getMultasbyNumberFilter(String param) {
        getDadoscondutor();
        InputStream is = null;
        String line=null;
        String result=null;
        String idcondutor=idC;
        try{
            URL url = new URL(wb.address_showMultaCondutor1 .toString());
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            String post_data = URLEncoder.encode("idcondutor","UTF-8")+"="+URLEncoder.encode(idcondutor,"UTF-8")+"&"
                    +URLEncoder.encode("param","UTF-8")+"="+URLEncoder.encode(param,"UTF-8");
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


            multa = new String[ja.length()];

            for (int i = 0; i < ja.length(); i++) {
                jo = ja.getJSONObject(i);


                multa[i] = "Artigo numero: " + jo.getString("nr_artigo") + "\n" + "Disposto numero: " + jo.getString("numero_disposto") + "\n"
                        + "Estado da multa: " + jo.getString("estado") + "\n" + "Data e Hora: " + jo.getString("datamulta") + "\n"
                        + "Valor da multa: " + jo.getString("valor") + "Mts";

            }

            ArrayAdapter<String> adapter2;
            adapter2 = new ArrayAdapter(this, android.R.layout.select_dialog_item, multa);
            Multas.setAdapter(adapter2);
            nrmultas.setText("Numero de multa/as: " + ja.length());

            Multas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapter1, View view, int i, long l) {
                    String item= adapter1.getItemAtPosition(i).toString();
                    String numero_artigo="";
                    for(int j=0;j<item.length();j++){
                        if(item.charAt(j)=='D' && item.charAt(j+1)=='a' && item.charAt(j+2)=='t'&& item.charAt(j+3)=='a'
                                && item.charAt(j+4)==' ' && item.charAt(j+5)=='e' && item.charAt(j+6)==' ' && item.charAt(j+7)=='H'
                                && item.charAt(j+8)=='o' && item.charAt(j+9)=='r' && item.charAt(j+10)=='a') {
                            j=j+12;
                            numero_artigo="";
                            for (int k=j;k<j+20;k++){
                                numero_artigo+=item.charAt(k);
                            }break;

                        }
                    }

                    Toast.makeText(ConsultarMulta.this, numero_artigo, Toast.LENGTH_SHORT).show();




                }
            });


        }catch (Exception e)
        {
            e.printStackTrace();
        }

    }
    private void getnomeCondutor() throws MalformedURLException {
        InputStream is = null;
        String line=null;
        String result=null;
        try{
            URL url = new URL(wb.address_shownomeCondutor.toString());
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

            nomeCondutor = new String[ja.length()];
            for(int i=0;i<ja.length();i++){
                jo=ja.getJSONObject(i);

               nomeCondutor[i]=jo.getString("nome");
            }

//            ArrayAdapter<String> adapter1;
//            adapter1 = new ArrayAdapter(this,android.R.layout.select_dialog_item,nomeCondutor);
//            editText2.setThreshold(4);
//            editText2.setAdapter(adapter1);


        }catch (Exception e)
        {
            e.printStackTrace();
        }

    }
    private void getnrcarta() throws MalformedURLException {
        InputStream is = null;
        String line=null;
        String result=null;
        try{
            URL url = new URL(wb.address_showcarta.toString());
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

            nrcarta = new String[ja.length()];

            for(int i=0;i<ja.length();i++){
                jo=ja.getJSONObject(i);
                nrcarta[i]=jo.getString("nrcarta");

            }

//            adapter1 = new ArrayAdapter(this,android.R.layout.select_dialog_item,nrcarta);
//            editText2.setThreshold(4);
//            editText2.setAdapter(adapter1);

        }catch (Exception e)
        {
            e.printStackTrace();
        }

    }
}
