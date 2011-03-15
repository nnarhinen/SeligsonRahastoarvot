package com.codesense.seligson;



import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class SeligsonRahastoArvot extends ListActivity {
	public static final String softwareName = "Seligson Android Client 1.0";
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final String[] fundNames = getResources().getStringArray(R.array.fundNames);
        final String[] fundUrls = getResources().getStringArray(R.array.fundUrls);
        setListAdapter(new ArrayAdapter<String>(this, R.layout.list_item, fundNames));
        
        ListView lv = getListView();
        lv.setOnItemClickListener(new OnItemClickListener() {
        	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        		Bundle bundle = new Bundle();
        		bundle.putString("url", fundUrls[position]);
        		bundle.putString("title", fundNames[position]);
        		Intent intent = new Intent(view.getContext(), GraphActivity.class);
        		intent.putExtras(bundle);
        		startActivityForResult(intent, 0);
        	}
        });
    }
}