package mz.gerasoft.regulador_rodovia;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class login extends AppCompatActivity {
    EditText codigoAgente, senhaAgente;
    Button btniniciar;
    String resultado;
    public static String username;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
       ActionBar actionbar = getSupportActionBar();

        actionbar.setTitle("Inicio de sessão");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btniniciar = (Button)findViewById(R.id.btniniciar);
        codigoAgente = (EditText) findViewById(R.id.codigoAgente);
        senhaAgente = (EditText) findViewById(R.id.senhaAgente);



        }

    public void onLogin(View v){
//        if(isInternetConnected()) {
            username = codigoAgente.getText().toString();
            String password = senhaAgente.getText().toString();
            String type = "login";
            BackgroundWorkerLogin bk = new BackgroundWorkerLogin(this);
            bk.execute(type, username, password);
//        }else{
//            Toast.makeText(this, "Sem rede, tente mais tarde", Toast.LENGTH_LONG).show();
//        }
        }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }


    public boolean isInternetConnected() {

        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(this.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();



        if (activeNetworkInfo != null){

            return true;

        }

        else{

            return false;

        }

    }


    }

