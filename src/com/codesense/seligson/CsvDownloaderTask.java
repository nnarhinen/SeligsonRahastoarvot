package com.codesense.seligson;

import java.io.BufferedReader;

import android.app.ProgressDialog;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;

import com.androidplot.series.XYSeries;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;

import android.graphics.Color;
import android.graphics.Paint;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;

public class CsvDownloaderTask extends AsyncTask<String, Void, List<SeligsonDayValue>> {
	
	private final WeakReference<XYPlot> xyPlotReference;
	private final WeakReference<ProgressDialog> dialogReference;
	private int months = 6;
	private String url;
	
	/**
	 * @param months the months to set
	 */
	public void setMonths(int months) {
		this.months = months;
	}

	/**
	 * @return the months
	 */
	public int getMonths() {
		return months;
	}
	
	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	public CsvDownloaderTask(ProgressDialog dialog, XYPlot plot, String url) {
		xyPlotReference = new WeakReference<XYPlot>(plot);
		dialogReference = new WeakReference<ProgressDialog>(dialog);
		this.url = url;
	}

	@Override
	protected List<SeligsonDayValue> doInBackground(String... params) {
		BufferedReader reader = null;
		List<SeligsonDayValue> ret = new ArrayList<SeligsonDayValue>();
		try {
			HttpClient client = AndroidHttpClient.newInstance(SeligsonRahastoArvot.softwareName);
	        HttpUriRequest request = new HttpGet(url);
			HttpResponse response = client.execute(request);
			reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line;
			DateFormat format = new SimpleDateFormat("dd.MM.yyyy");
			while ((line = reader.readLine()) != null) {
				String[] parts = line.split(";");
				Date date = format.parse(parts[0]);
				Calendar cal = Calendar.getInstance();
		        cal.setTime(new Date());
		        cal.add(Calendar.MONTH, -getMonths());
		        Date minDate = cal.getTime();
		        if (!date.before(minDate)) {
					double value = Double.parseDouble(parts[1]);
					ret.add(new SeligsonDayValue(date, value));
		        }
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (reader != null)
					reader.close();
			} catch (Exception e) { }
		}
		return ret;
	}
	
	@Override
	protected void onPostExecute(List<SeligsonDayValue> values) {
		if (isCancelled())
			values = null;
		if (xyPlotReference != null) {
			XYPlot plot = xyPlotReference.get();
			// Create a couple arrays of y-values to plot:
	        List<Double> numbers = new ArrayList<Double>();
	        for(SeligsonDayValue obj : values) {
	        	numbers.add((double)obj.getDay().getTime());
	        	numbers.add(obj.getValue());
	        }
	        // Turn the above arrays into XYSeries':
	        XYSeries series1 = new SimpleXYSeries(
	                numbers,          // SimpleXYSeries takes a List so turn our array into a List
	                SimpleXYSeries.ArrayFormat.XY_VALS_INTERLEAVED, // Y_VALS_ONLY means use the element index as the x value
	                "Arvo");                             // Set the display title of the series
	 
	        Paint fill = new Paint();
	        fill.setColor(Color.RED);
	        LineAndPointFormatter formatter = new LineAndPointFormatter(null, null, fill);
	        plot.addSeries(series1, formatter);
	 
	        // reduce the number of range labels
	        plot.setTicksPerRangeLabel(3);
	 
	        // by default, AndroidPlot displays developer guides to aid in laying out your plot.
	        // To get rid of them call disableAllMarkup():
	        dialogReference.get().hide();
	        plot.invalidate();
	        
		}
	}
	

}
