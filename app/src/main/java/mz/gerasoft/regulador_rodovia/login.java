package mz.gerasoft.regulador_rodovia;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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
            username = codigoAgente.getText().toString();
            String password = senhaAgente.getText().toString();
            String type = "login";
            BackgroundWorkerLogin bk = new BackgroundWorkerLogin(this);
            bk.execute(type, username, password);
        }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }



    }

