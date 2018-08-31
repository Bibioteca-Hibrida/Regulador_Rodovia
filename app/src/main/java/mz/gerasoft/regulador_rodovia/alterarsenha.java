package mz.gerasoft.regulador_rodovia;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
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
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;

public class alterarsenha extends AppCompatActivity {
    EditText senhaA,senhaN,senhaR;
    login l = new login();
    webMethodUrl wb = new webMethodUrl();
    Comprovativo_multa cm = new Comprovativo_multa();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ActionBar actionbar = getSupportActionBar();

        actionbar.setTitle("Alteração de senha");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alterarsenha);

      senhaA = (EditText) findViewById(R.id.editText5);
        senhaN = (EditText) findViewById(R.id.editText6);
        senhaR = (EditText) findViewById(R.id.editText7);





    }
    @Override
    public void onBackPressed() {
        Intent i = new Intent(this, main.class);
        startActivity(i);
        super.onBackPressed();
    }

    public void alterar(View v) throws IOException {
        //Toast.makeText(this, l.username.toString(), Toast.LENGTH_SHORT).show();

        if((!senhaN.getText().toString().equals(senhaR.getText().toString())) || (senhaA.getText().toString().isEmpty())){
            final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setTitle("Info: ")
                    .setMessage("Sem correspondencias nas senhas")
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
            AlertDialog alert = alertDialog.create();
            alert.show();
        } else{


            String user_name = l.username.toString();
            String password = senhaN.getText().toString();
            URL url = new URL(wb.alterarSenha.toString());
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
            String post_data = URLEncoder.encode("user_name","UTF-8")+"="+URLEncoder.encode(user_name,"UTF-8")+"&"
                    +URLEncoder.encode("password","UTF-8")+"="+URLEncoder.encode(password,"UTF-8");
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

            final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setTitle("Info: ")
                    .setMessage(result)
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
            AlertDialog alert = alertDialog.create();
            alert.show();

            Intent k = new Intent(this,main.class);
            startActivity(k);
    }






        }







    public void goMenu(View v){
        cm.goMenu(v);
    }
}
