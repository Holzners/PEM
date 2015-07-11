package com.team3.pem.pem.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
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

    private Button exportButton;
    private EditText nameField;
    private Switch exportAll;
    private boolean switchOn = false;
    private boolean allChecked = false;
    private File file;
    private FeedReaderDBHelper dbHelper;
    private List<String> enabledSymptoms;
    private TextView loadingText;
    private String filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export);

        dbHelper = FeedReaderDBHelper.getInstance();

        exportButton = (Button) findViewById(R.id.button_export);
        nameField = (EditText) findViewById(R.id.nameField);
        Switch mailSwitch = (Switch) findViewById(R.id.switch_mail);
        exportAll = (Switch) findViewById(R.id.switch_export);
        ListView listView = (ListView) findViewById(R.id.symptomListView);
        loadingText = (TextView) findViewById(R.id.loadingText);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        final SwitchExportAdapter adapter = new SwitchExportAdapter(this, R.layout.row_switch_layout, dbHelper.getFactorList());
        listView.setAdapter(adapter);
        enabledSymptoms = new ArrayList<>();

        exportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(enabledSymptoms.isEmpty()){
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.noSymptom), Toast.LENGTH_LONG).show();
                    return;
                }
                exportButton.setVisibility(View.INVISIBLE);
                loadingText.setVisibility(View.VISIBLE);
                final String filename = nameField.getText().toString();
                createPDF(filename);
            }
        });

        mailSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                switchOn = isChecked;
            }
        });

        exportAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    allChecked = true;
                    enabledSymptoms = new ArrayList<>(dbHelper.getFactorList());
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

    /**
     * Add or remove the selected factor to/from the
     * <code>enabledSymptoms</code> list
     *
     * @param isEnabled
     * @param symptom
     */
    public void switchSymptom(boolean isEnabled, String symptom){
        if(isEnabled)
            enabledSymptoms.add(symptom);
        else{
            if(enabledSymptoms.contains(symptom))
                enabledSymptoms.remove(symptom);
        }
        exportAll.setChecked(false);
    }

    /**
     * Create the PDF file on the device
     *
     * @param name
     * @return <code>true</code> if the file was created successfully,
     *         or <code>false</code> if the creation of the file failed
     */
    private boolean createPDF(String name) {
        final Handler handler = new Handler();
        if(name.equals("")){
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.inputFileName), Toast.LENGTH_LONG).show();
            exportButton.setVisibility(View.VISIBLE);
            loadingText.setVisibility(View.GONE);
            return false;
        }
        filePath = Environment.getExternalStorageDirectory().getPath() + "/" + name + ".pdf";
        file = new File(filePath);
        if (file.exists()) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.pdfExists), Toast.LENGTH_LONG).show();
            exportButton.setVisibility(View.VISIBLE);
            loadingText.setVisibility(View.GONE);
            return false;
        }else{
            try {
                file.createNewFile();
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.pdfFailed), Toast.LENGTH_LONG).show();
                exportButton.setVisibility(View.VISIBLE);
                loadingText.setVisibility(View.GONE);
                return false;
            }
        }

        new Thread() {
            @Override
            public void run() {
                handler.post(new Runnable(){
                    public void run(){
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
                            Toast.makeText(getApplicationContext(), filePath + getResources().getString(R.string.created), Toast.LENGTH_LONG).show();
                            exportButton.setVisibility(View.VISIBLE);
                            loadingText.setVisibility(View.GONE);
                            if (switchOn) {
                                Intent email = new Intent(Intent.ACTION_SEND);
                                Uri uri = Uri.fromFile(new File(filePath));
                                email.putExtra(Intent.EXTRA_STREAM, uri);
                                email.setType("message/rfc822");
                                startActivity(Intent.createChooser(email, getResources().getString(R.string.emailClient)));
                            }
                        }catch(FileNotFoundException | DocumentException e) {
                            e.getStackTrace();
                        }
                    }
                });
            }
        }.start();
        return true;
    }

    /**
     * Create the table for the PDF file
     *
     * @param symptom
     * @return the created table
     */
    private PdfPTable getTable(String symptom){
        PdfPTable table = new PdfPTable(new float[] {2, 1, 2, 4, 4});
        table.setWidthPercentage(100f);
        //Header
        table.getDefaultCell().setPadding(3);
        table.getDefaultCell().setUseAscender(true);
        table.getDefaultCell().setUseDescender(true);
        table.getDefaultCell().setBackgroundColor(BaseColor.ORANGE);
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        table.getDefaultCell().setBorderColor(BaseColor.WHITE);
        table.addCell(getResources().getString(R.string.date));
        PdfPCell cellHead = new PdfPCell(new Phrase(symptom));
        cellHead.setBorderColor(BaseColor.WHITE);
        cellHead.setBackgroundColor(BaseColor.ORANGE);
        cellHead.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellHead.setColspan(2);
        table.addCell(cellHead);
        table.addCell(getResources().getString(R.string.note));
        table.addCell(getResources().getString(R.string.weatherPDF));
        //Zeilen aus DB einfuegen
        List<String> list = new ArrayList<>();
        list.add(symptom);
        HashMap<DateTime, DayEntry> dbEntries = dbHelper.getDatabaseEntries(list);
        TreeMap<DateTime, DayEntry> dbEntriesTree = new TreeMap<>(dbEntries);

        table.getDefaultCell().setBackgroundColor(null);
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
        for(TreeMap.Entry<DateTime, DayEntry> entry : dbEntriesTree.entrySet()){
            table.addCell(entry.getKey().format("DD.MM.YYYY"));
            HashMap<String, Integer> ratings = entry.getValue().ratings;
            PdfPCell cell = new PdfPCell(new Phrase(""));
            int colorID = RatingToColorHelper.ratingToColor(symptom, ratings.get(symptom).intValue());
            String hexColor =  String.format("#%06X", (0xFFFFFF & getResources().getColor(colorID)));

            cell.setBorderColor(BaseColor.WHITE);
            cell.setBackgroundColor(WebColors.getRGBColor(hexColor));
            table.addCell(cell);

            String staerke = getResources().getStringArray(R.array.rating)[ratings.get(symptom)];
            table.addCell(staerke);
            table.addCell(entry.getValue().description);
            String getWeatherData = dbHelper.getWeatherData(entry.getKey());
            if(!getWeatherData.equals("")){
                String[] splitWeather = getWeatherData.split("\n");
                table.addCell(splitWeather[0] + ", " + splitWeather[1] + ", " + splitWeather[4] + " C");
            }else{
                table.addCell("Keine Angabe");
            }
        }

        //Legend
        PdfPCell cellLegend = new PdfPCell(new Phrase(getResources().getString(R.string.skala)));
        cellLegend.setBorderColor(BaseColor.WHITE);
        cellLegend.setColspan(4);
        cellLegend.setPaddingTop(30);
        table.addCell(cellLegend);

        return table;
    }

    /**
     * Get the value of allChecked
     *
     * @return <code>allChecked</code>
     */
    public boolean getAllChecked(){
        return this.allChecked;
    }

    /**
     * Get the enabledSymptoms List
     *
     * @return <code>enabledSymptoms</code>
     */
    public List<String> getEnabledSymptoms(){
        return this.enabledSymptoms;
    }
}
