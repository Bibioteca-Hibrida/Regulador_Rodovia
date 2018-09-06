package mz.gerasoft.regulador_rodovia;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static android.os.Environment.getExternalStorageDirectory;

public class pdf extends AppCompatActivity {
//    String filename = "meu_documento.pdf";
//    File path = new File(Environment.getExternalStorageDirectory(), "PDFGerados");
//    File pdffile = new File(path, filename);
final String ARQUIVO = "arquivo.txt";
    private TextView txtRoot;
    private TextView txtNomeArq;
    private TextView txtSalvar;
    private TextView txtLer;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        try {
            setContentView(R.layout.activity_pdf);

            txtRoot = (TextView) findViewById(R.id.txtRoot2);
            txtNomeArq = (TextView) findViewById(R.id.edtNomeArq);
            txtSalvar = (TextView) findViewById(R.id.edtSalvar);
            txtLer = (TextView) findViewById(R.id.edtLer);


            txtRoot.append(GetRoot());

        } catch (Exception e) {
            trace("Erro : " + e.getMessage());
        }
    }

    public void click_Salvar(View v)
    {
        SalvarArquivo();
    }

    public void click_Ler(View v)
    {
        LerArquivo();
    }

    public void click_Limpar(View v)
    {
        txtNomeArq.setText("teste.txt");
        txtSalvar.setText("");
        txtLer.setText("");
    }

    //Exibir o diretório de armazenamento externo do Android
    private String GetRoot()
    {
        File root = android.os.Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        return root.toString();
    }

    private void SalvarArquivo()
    {
        String lstrNomeArq;
        File arq;
        byte[] dados;
        try
        {
            //pega o nome do arquivo a ser gravado
            lstrNomeArq = txtNomeArq.getText().toString();

            arq = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES), lstrNomeArq);
            FileOutputStream fos;

            //transforma o texto digitado em array de bytes
            dados = txtSalvar.getText().toString().getBytes();

            fos = new FileOutputStream(arq);

            //escreve os dados e fecha o arquivo
            fos.write(dados);
            fos.flush();
            fos.close();
        } catch (Exception e) {
            trace("Erro : " + e.getMessage());
        }
    }

    private void LerArquivo()
    {
        String lstrNomeArq;
        File arq;
        String lstrlinha;
        try
        {
            //pega o nome do arquivo a ser lido
            lstrNomeArq = txtNomeArq.getText().toString();

            //limpa a caixa de texto que irá receber os dados do arquivo
            txtLer.setText("");


            arq = new File(Environment.getExternalStorageDirectory(), lstrNomeArq);
            BufferedReader br = new BufferedReader(new FileReader(arq));
            //efetua uma leitura linha a linha do arquivo a carrega
            //a caixa de texto com a informação lida
            while ((lstrlinha = br.readLine()) != null) {
                if (!txtLer.getText().toString().equals(""))
                {
                    txtLer.append("\n");
                }
                txtLer.append(lstrlinha);
            }

        } catch (Exception e) {
            trace("Erro : " + e.getMessage());
        }
    }

    public void toast (String msg)
    {
        Toast.makeText (getApplicationContext(), msg, Toast.LENGTH_SHORT).show ();
    }

    private void trace (String msg)
    {
        toast (msg);
    }
}