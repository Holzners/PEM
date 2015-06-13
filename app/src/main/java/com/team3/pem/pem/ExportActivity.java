package com.team3.pem.pem;

import android.content.Context;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

//Importing stuff for PDF Export
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


public class ExportActivity extends ActionBarActivity {

    Button exportButton;
    EditText nameField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export);

        exportButton = (Button) findViewById(R.id.exportbutton);
        nameField = (EditText) findViewById(R.id.nameField);

        exportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String filename = nameField.getText().toString();
                if(createPDF(filename)){
                    Toast.makeText(getApplicationContext(), filename + ".pdf created", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(), "Could not create pdf", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_export, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean createPDF(String name){
        try{
            String filePath = Environment.getExternalStorageDirectory().getPath() + "/" + name + ".pdf";
            File file = new File(filePath);
            // If file exists, stop working
            if (file.exists()) {
                Toast.makeText(getApplicationContext(), "The file already exists", Toast.LENGTH_LONG).show();
                return false;
            }else{
                file.createNewFile();
            }
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(file));
            document.open();
            //TODO: ADD DATA
            //document.add(new Paragraph("Hello World!"));
            document.close();
            return true;
        }catch(IOException e){
            e.getMessage();
            return false;
        }catch(DocumentException e){
            e.getMessage();
            return false;
        }
    }
}
