package com.openbuilding;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

public class SearchActivity extends Activity {

	
	EditText e1;
	String string;
	ArrayList<String> elemente=new ArrayList<String>();
	ArrayList<Integer> nr=new ArrayList<Integer>();
	ArrayList<String> id=new ArrayList<String>();
	StableArrayAdapter adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		elemente.add("your results will be displayed here");
		adapter=new StableArrayAdapter(this, android.R.layout.simple_list_item_1,elemente);
		ListView l=(ListView) findViewById(R.id.listView1);
		e1=(EditText)findViewById(R.id.editText1);
		e1.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				string=s.toString();
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					 HttpClient httpclient = new DefaultHttpClient();
					    HttpPost httppost = new HttpPost("http://wyliodrin.com:8000/find_locations");
					httppost.setHeader("Content-Type", "application/json");
					//httppost.setHeader("Accepelementet", "application/json");
					    try {
					        // Add your data
					    	JSONObject holder;
							try {
								holder = new JSONObject("{\"text\":\""+string+"\"}");
							
					    	
					    	StringEntity se = new StringEntity(holder.toString());

					        // Execute HTTP Post Request
					    	httppost.setEntity(se);
					        HttpResponse response = httpclient.execute(httppost);
					        String result = EntityUtils.toString(response.getEntity());
					System.out.println("result="+result);
					JSONArray ja=new JSONArray(result);
					elemente.clear();
					nr.clear();
					id.clear();
					for (int i=0;i<ja.length();i++)
					{JSONObject rj=ja.getJSONObject(i);
					elemente.add(rj.getString("text"));
					nr.add(rj.getInt("nr"));
					id.add(rj.getString("locationID"));
					
					
					}
					runOnUiThread(new Runnable() {
						public void run() {
							adapter.resetAdapter(elemente);
						}
					});
					
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
					    } catch (ClientProtocolException e) {
					        // TODO Auto-generated catch block
					    } catch (IOException e) {
					        // TODO Auto-generated catch block
					    }
					
				}
			}).start();
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
		
		
		l.setAdapter(adapter);
		l.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				System.out.println("clicked on "+arg2);
				Intent i=new Intent(SearchActivity.this,DrawingActivity.class);
				i.putExtra("id", id.get(arg2));
				i.putExtra("etaje", "[0]");
				startActivity(i);
			}
		});
		
		
	}

	

}



 class StableArrayAdapter extends ArrayAdapter<String> {

  HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();

  public StableArrayAdapter(Context context, int textViewResourceId,
      List<String> objects) {
    super(context, textViewResourceId, objects);
    for (int i = 0; i < objects.size(); ++i) {
      mIdMap.put(objects.get(i), i);
    }
  }

  public void resetAdapter(List<String> objects)
  { mIdMap.clear();
	  for (int i = 0; i < objects.size(); ++i) {
      mIdMap.put(objects.get(i), i);
    }
	notifyDataSetChanged();  
  }
  
  @Override
  public long getItemId(int position) {
    String item = getItem(position);
    return mIdMap.get(item);
  }

  @Override
  public boolean hasStableIds() {
    return true;
  }

}

 
