//package mz.gerasoft.regulador_rodovia;
//
//import android.content.Intent;
//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;
//import android.view.View;
//import android.view.ViewStub;
//import android.widget.ImageButton;
//
//public class Menu_principal extends AppCompatActivity {
//ImageButton imageButton5, imageButton6,imageButton7,imageButton8,imageButton9;
//    ViewStub cadastrarMulta1,consultarMulta1,relatorio,consultarArtigo1;
//    String carta[] = {
//            "123",
//            "245",
//            "567"
//    };
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_menu_principal);
//
//
//
//
//
//
//
//
//        cadastrarMulta1 = ((ViewStub) findViewById(R.id.cadastrarMulta1));
//        consultarMulta1 = ((ViewStub) findViewById(R.id.consultarMulta1));
//        relatorio = ((ViewStub) findViewById(R.id.relatorio));
//        consultarArtigo1 = ((ViewStub) findViewById(R.id.consultarArtigo1));
//
//         imageButton5 = (ImageButton) findViewById(R.id.imageButton5);
//        imageButton6 = (ImageButton) findViewById(R.id.imageButton6);
//        imageButton7 = (ImageButton) findViewById(R.id.imageButton7);
//        imageButton8 = (ImageButton) findViewById(R.id.imageButton8);
//        imageButton9 = (ImageButton) findViewById(R.id.imageButton9);
//
//        imageButton5.setOnClickListener(new View.OnClickListener(){
//
//                    public void onClick(View v){
//
//                      Intent i = new Intent(Menu_principal.this, CadastrarMulta.class);
//                        startActivity(i);
//
//                 //   View  cadastrarMulta1 = ((ViewStub) findViewById(R.id.cadastrarMulta1)).inflate();
//
//                    /* consultarArtigo1.setVisibility(View.GONE);
//                        relatorio.setVisibility(View.GONE);
//                        consultarMulta1.setVisibility(View.GONE);
//
//                        //cadastrarMulta1.buildLayer();
//                        cadastrarMulta1.setVisibility(View.VISIBLE);*/
//
//
//
//
//            }
//        });
//
//
//        imageButton6.setOnClickListener(new View.OnClickListener(){
//
//            public void onClick(View v){
//
//
//               consultarArtigo1.setVisibility(View.GONE);
//                relatorio.setVisibility(View.GONE);
//                cadastrarMulta1.setVisibility(View.GONE);
//                consultarMulta1.setVisibility(View.VISIBLE);
//            }
//        });
//
//        imageButton7.setOnClickListener(new View.OnClickListener(){
//
//            public void onClick(View v){
//
//                consultarMulta1.setVisibility(View.GONE);
//                relatorio.setVisibility(View.GONE);
//                cadastrarMulta1.setVisibility(View.GONE);
//                consultarArtigo1.setVisibility(View.VISIBLE);
//
//            }
//        });
//
//        imageButton8.setOnClickListener(new View.OnClickListener(){
//
//            public void onClick(View v){
//
//                consultarMulta1.setVisibility(View.GONE);
//                consultarArtigo1.setVisibility(View.GONE);
//                cadastrarMulta1.setVisibility(View.GONE);
//                relatorio.setVisibility(View.VISIBLE);
//
//            }
//        });
//
//
//
//    }
//
//}
