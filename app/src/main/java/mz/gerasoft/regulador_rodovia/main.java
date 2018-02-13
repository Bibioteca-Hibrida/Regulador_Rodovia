package mz.gerasoft.regulador_rodovia;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

public class main extends AppCompatActivity {

    private TextView mTextMessage;

    @Override
    public void onBackPressed() {
        Intent i = new Intent(this, main.class);
        startActivity(i);
        super.onBackPressed();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.CaMulta:
                    Intent i = new Intent(main.this, CadastrarMulta.class);
                    startActivity(i);
                    mTextMessage.setText("Multa");
                    return true;
                case R.id.CoMulta:
                    Intent j = new Intent(main.this, ConsultarMulta.class);
                    startActivity(j);
                    mTextMessage.setText("Consultar Multa");
                    return true;
                case R.id.CoArtigo:
                    Intent k = new Intent(main.this, ConsultarArtigo.class);
                    startActivity(k);
                    mTextMessage.setText("Consultar Artigo");
                    return true;
                case R.id.Grelatorio:
                    Intent l = new Intent(main.this, GerarRelatorio.class);
                    startActivity(l);
                    mTextMessage.setText("Gerar Relatorio");
                    return true;

            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

}
