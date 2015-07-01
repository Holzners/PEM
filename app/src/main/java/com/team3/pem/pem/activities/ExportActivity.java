package com.team3.pem.pem.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.html.WebColors;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.team3.pem.pem.R;
import com.team3.pem.pem.adapters.SwitchExportAdapter;
import com.team3.pem.pem.mSQLite.FeedReaderDBHelper;
import com.team3.pem.pem.utili.DayEntry;
import com.team3.pem.pem.utili.RatingToColorHelper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

import hirondelle.date4j.DateTime;

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
    TextView loadingText;

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
        loadingText = (TextView) findViewById(R.id.loadingText);

        final SwitchExportAdapter adapter = new SwitchExportAdapter(this, R.layout.switch_row_layout, dbHelper.getFactors());
        listView.setAdapter(adapter);
        enabledSymptoms = new ArrayList<String>();

        exportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exportButton.setVisibility(View.INVISIBLE);
                loadingText.setVisibility(View.VISIBLE);
                final String filename = nameField.getText().toString();

                try {
                    createPDF(filename);
                    if (switchOn) {
                        Intent email = new Intent(Intent.ACTION_SEND);

                        Uri uri = Uri.fromFile(file);
                        email.putExtra(Intent.EXTRA_STREAM, uri);

                        email.setType("message/rfc822");
                        startActivity(Intent.createChooser(email, "Choose an email client!"));

                    } else {

                    }
                } catch (IOException | DocumentException e) {
                    Toast.makeText(getApplicationContext(), "Could not create pdf file", Toast.LENGTH_SHORT).show();
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

    private boolean createPDF(String name) throws IOException, DocumentException{
        final Handler handler = new Handler();
        final String filePath = Environment.getExternalStorageDirectory().getPath() + "/" + name + ".pdf";
        file = new File(filePath);
        if (file.exists()) {
            Toast.makeText(getApplicationContext(), "The file already exists", Toast.LENGTH_LONG).show();
            return false;
        }else{
            file.createNewFile();
        }

        new Thread(){
            public void run(){
                handler.post(new Runnable(){
                    public void run() {
                        try {
                            Document document = new Document();
                            PdfWriter.getInstance(document, new FileOutputStream(file));
                            document.open();
                            for (String symptom : enabledSymptoms) {
                                PdfPTable table = getTable(symptom);
                                document.add(table);
                                document.newPage();
                            }
                            document.close();
                            Toast.makeText(getApplicationContext(), filePath + " created", Toast.LENGTH_SHORT).show();
                            exportButton.setVisibility(View.VISIBLE);
                            loadingText.setVisibility(View.GONE);
                        }catch(FileNotFoundException | DocumentException e) {
                            e.getStackTrace();
                        }
                    }
                });
            }
        }.start();
        return true;
    }

    private PdfPTable getTable(String symptom){
        PdfPTable table = new PdfPTable(new float[] {2, 2, 2, 5});
        table.setWidthPercentage(100f);
        //Header
        table.getDefaultCell().setPadding(3);
        table.getDefaultCell().setUseAscender(true);
        table.getDefaultCell().setUseDescender(true);
        table.getDefaultCell().setBackgroundColor(BaseColor.ORANGE);
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell("Datum");
        PdfPCell cellHead = new PdfPCell(new Phrase(symptom));
        cellHead.setBackgroundColor(BaseColor.ORANGE);
        cellHead.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellHead.setColspan(2);
        table.addCell(cellHead);
        table.addCell("Notiz");
        //Zeilen aus DB einfuegen
        List<String> list = new ArrayList<String>();
        list.add(symptom);
        HashMap<DateTime, DayEntry> dbEntries = dbHelper.getDatabaseEntries(list);
        TreeMap<DateTime, DayEntry> dbEntriesTree = new TreeMap<DateTime, DayEntry>(dbEntries);

        table.getDefaultCell().setBackgroundColor(null);
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
        for(TreeMap.Entry<DateTime, DayEntry> entry : dbEntriesTree.entrySet()){
            table.addCell(entry.getKey().format("DD.MM.YYYY"));
            HashMap<String, Integer> ratings = entry.getValue().ratings;
            PdfPCell cell = new PdfPCell(new Phrase(""));
            int colorID = RatingToColorHelper.ratingToColor(symptom, ratings.get(symptom).intValue());
            String hexColor =  String.format("#%06X", (0xFFFFFF & getResources().getColor(colorID)));

            cell.setBackgroundColor(WebColors.getRGBColor(hexColor));
            table.addCell(cell);

            String staerke = "";
            switch(ratings.get(symptom).toString()){
                case "0":
                    staerke = "Nicht definiert";
                    break;
                case "1":
                    staerke = "Sehr gut";
                    break;
                case "2":
                    staerke = "Gut";
                    break;
                case "3":
                    staerke = "In Ordnung";
                    break;
                case "4":
                    staerke = "Schlecht";
                    break;
                case "5":
                    staerke = "Sehr Schlecht";
                    break;
            }
            table.addCell(staerke);
            table.addCell(entry.getValue().description);
        }

        return table;
    }
}
