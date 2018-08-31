package mz.gerasoft.regulador_rodovia;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;

import static android.os.Environment.getExternalStorageDirectory;

public class pdf extends AppCompatActivity {
    String filename = "meu_documento.pdf";
    File path = new File(getExternalStorageDirectory(), "PDFGerados");
    File pdffile = new File(path, filename);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf);

        try
        {
            Document doc=new Document();
            PdfWriter.getInstance(doc, new FileOutputStream(pdffile));
            doc.open();//abrir o documento
/*
      Criar instância de Parágrafo
      Podemos criar instância de simplesmente parâmetro para texto ou para texto e estilos de fonte
    */
            //criar parágrafos
            Paragraph texto_simples = new Paragraph("Codingo Dojo Angola");
            Paragraph texto_estilo = new Paragraph("Ensinar quem sabe menos e aprender com quem sabe mais".toUpperCase(), FontFactory.getFont(FontFactory.HELVETICA, 20, Font.BOLD, BaseColor.RED));
//            //criar imagem
//            Image img = Image.getInstance("caminho/da/imagem.png");
//            img.scaleAbsolute(30, 30);
            //adicionar os parágrafos e imagem do documento
            try {
                doc.add(texto_simples);
            } catch (DocumentException e) {
                e.printStackTrace();
            }
            //criar nova página
            doc.newPage();
            //adicionar parágrafo na nova página
            try {
                doc.add(texto_estilo);
            } catch (DocumentException e) {
                e.printStackTrace();
            }
            //adicionar imagem
           // doc.add(img);
            //adicionar informações de autoria do documento
            doc.addAuthor("Jonas Apolo");
            //depois de tudo fechamos o documento
            doc.close();
        }catch(Exception e){
        e.printStackTrace();
    }


        //pegar o ficheiro
        File file = new File(getExternalStorageDirectory().getPath()+"/PDFGerados"+"/"+filename);
//criar o intent para visualização do documento
        Uri caminho = Uri.fromFile(file);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(caminho, "application/pdf");
        startActivity(intent);


    }
}
