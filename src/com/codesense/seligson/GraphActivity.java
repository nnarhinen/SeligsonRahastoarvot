package com.codesense.seligson;

import java.text.SimpleDateFormat;

import com.androidplot.series.XYSeries;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYStepMode;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class GraphActivity extends Activity {
	private CsvDownloaderTask task;
	private XYPlot plot;
	private String title;
	private ProgressDialog progressDialog;
	private String url;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.graph);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Ladataan. Odota ole hyvä..");
        progressDialog.setIndeterminate(true);
        progressDialog.show();
        plot = (XYPlot) findViewById(R.id.mySimpleXYPlot);
        Bundle bundle = this.getIntent().getExtras();
        title = bundle.getString("title");
        url = bundle.getString("url");
        try {
        	plot.disableAllMarkup();
        	plot.setRangeLabel("Arvo");
        	plot.setDomainLabel("Pvm");
        	plot.setDomainStep(XYStepMode.SUBDIVIDE, 6);
        	plot.setDomainValueFormat(new SimpleDateFormat("dd.MM.yyyy"));
        	plot.setTitle(title + " 6kk");
        	task = new CsvDownloaderTask(progressDialog, plot, url);
        	task.execute();
        } catch (Exception e) {
        	e.printStackTrace();
        }
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.graph_menu, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.itemInterval120Months:
			return handleIntervalItemClick(item, title + " 10v", 120);
		case R.id.itemInterval12Months:
			return handleIntervalItemClick(item, title + " 1v", 12);
		case R.id.itemInterval1Month:
			return handleIntervalItemClick(item, title + " 1kk", 1);
		case R.id.itemInterval60Months:
			return handleIntervalItemClick(item, title + " 5v", 60);
		case R.id.itemInterval6Months:
			return handleIntervalItemClick(item, title + " 6kk", 6);
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private boolean handleIntervalItemClick(MenuItem item, String plotTitle,
			int months) {
		if (!item.isChecked()) {
			item.setChecked(true);
			plot.setTitle(plotTitle);
			for (XYSeries ser : plot.getSeriesSet())
				plot.removeSeries(ser);
			task = new CsvDownloaderTask(progressDialog, plot, url);
			task.setMonths(months);
			task.execute();
			progressDialog.show();
		}
		return true;
	}

}
