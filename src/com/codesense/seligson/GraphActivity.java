package com.codesense.seligson;

import java.text.SimpleDateFormat;

import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYStepMode;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;

public class GraphActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.graph);
        try {
        	Bundle bundle = this.getIntent().getExtras();
        	XYPlot plot = (XYPlot) findViewById(R.id.mySimpleXYPlot);
        	plot.disableAllMarkup();
        	plot.setRangeLabel("Arvo");
        	plot.setDomainLabel("Pvm");
        	plot.setDomainStep(XYStepMode.SUBDIVIDE, 6);
        	plot.setDomainValueFormat(new SimpleDateFormat("dd.MM"));
        	plot.setTitle(bundle.getString("title") + " 6kk");
        	ProgressDialog dialog = ProgressDialog.show(this, "", "Ladataan. Odota ole hyvä..", true);
        	CsvDownloaderTask task = new CsvDownloaderTask(plot, dialog);
        	task.execute(bundle.getString("url"));
        } catch (Exception e) {
        	e.printStackTrace();
        }
	}

}
