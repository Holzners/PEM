package com.team3.pem.pem.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.team3.pem.pem.R;
import com.team3.pem.pem.adapters.SwitchExportAdapter;
import com.team3.pem.pem.mSQLite.FeedReaderDBHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

//Importing stuff for PDF Export


public class ExportActivity extends ActionBarActivity {

    Button exportButton;
    EditText nameField;
    Switch mailSwitch, exportAll;
    boolean switchOn = false;
    public boolean allChecked = false;
    File file;
    FeedReaderDBHelper dbHelper;
    List<String> enabledSymptoms;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export);

        dbHelper = FeedReaderDBHelper.getInstance();

        exportButton = (Button) findViewById(R.id.exportbutton);
        nameField = (EditText) findViewById(R.id.nameField);
        mailSwitch = (Switch) findViewById(R.id.mailswitch);
        exportAll = (Switch) findViewById(R.id.exportSwitch);
        listView = (ListView) findViewById(R.id.symptomListView);

        final SwitchExportAdapter adapter = new SwitchExportAdapter(this, R.layout.switch_row_layout, dbHelper.getFactors());
        listView.setAdapter(adapter);
        enabledSymptoms = new ArrayList<String>();

        exportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String filename = nameField.getText().toString();
                if(createPDF(filename)){
                    if(switchOn) {
                        Intent email = new Intent(Intent.ACTION_SEND);

                        Uri uri = Uri.fromFile(file);
                        email.putExtra(Intent.EXTRA_STREAM, uri);

                        email.setType("message/rfc822");
                        startActivity(Intent.createChooser(email, "Choose an email client!"));
                    }else{
                        Toast.makeText(getApplicationContext(), filename + ".pdf created", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "Could not create pdf", Toast.LENGTH_LONG).show();
                }
            }
        });

        mailSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    switchOn = true;
                } else {
                    switchOn = false;
                }
            }
        });

        exportAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    allChecked = true;
                    enabledSymptoms = dbHelper.getFactors();
                } else {
                    allChecked = false;
                    enabledSymptoms.clear();
                }
                adapter.notifyDataSetChanged();
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

        return super.onOptionsItemSelected(item);
    }

    public void switchSymptom(boolean isEnabled, String symptom){
        if(isEnabled)
            enabledSymptoms.add(symptom);
        else{
            if(enabledSymptoms.contains(symptom))
                enabledSymptoms.remove(symptom);
        }
        exportAll.setChecked(false);
    }

    private boolean createPDF(String name){
        try{
            String filePath = Environment.getExternalStorageDirectory().getPath() + "/" + name + ".pdf";
            file = new File(filePath);
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
           // PdfPTableEvent event = new AlternatingBackground();
            PdfPTable table = getTable();
            //table.setTableEvent(event);
            document.add(table);
            document.close();
            return true;
        }catch(IOException e){
            Log.i("PDF", "IOException");
            return false;
        }catch(DocumentException e){
            Log.i("PDF", "DocumentException");
            return false;
        }
    }

    private PdfPTable getTable(){
        int size = 0;
        for(String symptom : enabledSymptoms){
            size++;
        }
        Log.i("EnabledSymptoms", "" + size);
        PdfPTable table = new PdfPTable(size+2);
        table.setWidthPercentage(100f);
        table.getDefaultCell().setPadding(3);
        table.getDefaultCell().setUseAscender(true);
        table.getDefaultCell().setUseDescender(true);
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell("Datum");
        for(int i = 0; i < size; i++) {
            table.addCell(enabledSymptoms.get(i));
        }
        table.addCell("Notiz");

        return table;
    }
}
