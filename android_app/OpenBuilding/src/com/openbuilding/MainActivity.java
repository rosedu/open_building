package com.openbuilding;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends Activity {
	 GoogleMap mMap;
	 JSONArray ja;
	 ArrayList<myLocation> locations;
	 
	 LatLng aux_loc;
	 ImageView profil;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		profil=(ImageView)findViewById(R.id.imageProfile);
		EditText e1=(EditText)findViewById(R.id.editText1);
		
		e1.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(event.getAction()==MotionEvent.ACTION_DOWN) 
				{Intent i=new Intent(MainActivity.this,SearchActivity.class);
				startActivity(i);
				
				}
				return false;
			}
		});
		
		
		profil.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				
				if(event.getAction()==MotionEvent.ACTION_DOWN) profil.setImageResource(R.drawable.profile_p);
				else if(event.getAction()==MotionEvent.ACTION_UP) {profil.setImageResource(R.drawable.profile);
				
				
				Session.openActiveSession(MainActivity.this, true, new Session.StatusCallback() {

				    // callback when session changes state
				    @Override
				    public void call(Session session, SessionState state, Exception exception) {
				    	if (session.isOpened()) {
				    		Request.executeMeRequestAsync(session, new Request.GraphUserCallback() {

				    			  // callback after Graph API response with user object
				    			  @Override
				    			  public void onCompleted(GraphUser user, Response response) {
				    				  if (user != null) {
				    					  
				    					Toast.makeText(MainActivity.this,"Congrats, "+user.getFirstName()+"! You are now officialy an Open Builder!Please reward yourself with a cookie!", Toast.LENGTH_LONG).show();
				    					}
				    			  }
				    			});
				    	}
				    }
				  });
				
				}
				return true;
			}
		});
		
		mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
		
		mMap.setOnMapClickListener(new OnMapClickListener() {
			
			@Override
			public void onMapClick(LatLng arg0) {
			
			      if(text!=null)
			      {mMap.addMarker(new MarkerOptions()
					.icon(BitmapDescriptorFactory.fromResource(R.drawable.cladire_small))
			        .position(arg0 )
			        .title(text)) ;
			     
			      
			    aux_loc=arg0;  
			  new Thread(new Runnable() {
				
				@Override
				public void run() {
					addBuilding(text, aux_loc.latitude, aux_loc.longitude);
					text=null;
				}
			}) .start();   
			      
			  
			      
			      
			      }
				
			}
		});
		
		/*mMap.addMarker(new MarkerOptions()
		        .position(new LatLng(0, 0))
		        .title("Hello world"));*/
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				  HttpClient httpclient = new DefaultHttpClient();
				    HttpPost httppost = new HttpPost("http://wyliodrin.com:8000/get_locations");

				    try {
				        // Add your data
				        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
				       
				        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

				        // Execute HTTP Post Request
				        HttpResponse response = httpclient.execute(httppost);
				        
				        String result = EntityUtils.toString(response.getEntity());
				      System.out.println("result="+result);
				        ja=new JSONArray(result);
				        
				    locations=new ArrayList<myLocation>();
				    for (int i=0;i<ja.length();i++)
				    try {
						
						locations.add(new myLocation(((JSONObject)ja.get(i)).getString("name"), ((JSONObject)ja.get(i)).getString("id"), ((JSONObject)ja.get(i)).getJSONArray("floors")));				    
				    } catch (JSONException e) {
						// TODO: handle exception
					}
				    runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
				
							for(int i=0;i<ja.length();i++)
							{try {
								System.out.println("add, i="+i);
							JSONObject jo=(JSONObject) ja.get(i);
							mMap.addMarker(new MarkerOptions()
							.icon(BitmapDescriptorFactory.fromResource(R.drawable.cladire_small))
					        .position(new LatLng(Double.parseDouble(jo.getString("latitude")),Double.parseDouble(jo.getString("longitude")) ))
					        
							.title(jo.getString("name")));
								
								
								
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}catch (NumberFormatException e) {
								// TODO: handle exception
							}}
							
							
							
							
						}
					});
				    
				    } catch (ClientProtocolException e) {
				        // TODO Auto-generated catch block
				    } catch (IOException e) {
				        // TODO Auto-generated catch block
				    } catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				
			}
		}).start();
		
		mMap.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {
			
			@Override
			public void onInfoWindowClick(Marker arg0) {
				System.out.println("clicked on"+arg0.getTitle());
				for(int i=0;i<locations.size();i++)
				{if(locations.get(i).name.equalsIgnoreCase(arg0.getTitle()))
				{ 
				
				Intent in= new Intent(MainActivity.this,DrawingActivity.class);
				in.putExtra("id", locations.get(i).id);
				in.putExtra("name", locations.get(i).name);
				in.putExtra("etaje", locations.get(i).etaje.toString());
				
				
				startActivity(in);
				
				
				}			
				
				}
				
			}
		});
	
		
		 mMap.moveCamera(CameraUpdateFactory.newLatLngZoom( new LatLng(44.435908,26.046309),13));
		addItem=(ImageView) findViewById(R.id.imageView2);
			
			addItem.setOnTouchListener(new OnTouchListener() {
				
				@Override
				public boolean onTouch(View v, MotionEvent event) {
				
					if(event.getAction()==MotionEvent.ACTION_DOWN)  addItem.setImageResource(R.drawable.add_but_p);
				else if(event.getAction()==MotionEvent.ACTION_UP) { addItem.setImageResource(R.drawable.add_but);
				startActivityForResult(new Intent(MainActivity.this,DialogAct.class), 0);
				
				}
				
				
				return true;
				}
			});
			
	}
  
	
	
	ImageView addItem;
	String text;
	public void addBuilding(String name, double latitude, double longitude)
	{  HttpClient httpclient = new DefaultHttpClient();
    HttpPost httppost = new HttpPost("http://wyliodrin.com:8000/add_location");

	httppost.setHeader("Content-Type", "application/json");
	//httppost.setHeader("Accept", "application/json");
	try {
	        // Add your data
	    	JSONObject holder;
			String json="{\"name\":\""+name+"\", \"latitude\":"+latitude+",\"longitude\":"+longitude+"}";
				holder = new JSONObject(json);
			
	    	
	    	StringEntity se = new StringEntity(holder.toString());

	        // Execute HTTP Post Request
	    	httppost.setEntity(se);
    
    
        // Add your data
      

        // Execute HTTP Post Request
        HttpResponse response = httpclient.execute(httppost);
        
        String result = EntityUtils.toString(response.getEntity());
		System.out.println("result="+result);
		System.out.println("name="+name);
		locations.add(new myLocation(name, (new JSONObject(result)).getString("id"), new JSONArray("[]")));
		
        
        
    } catch (ClientProtocolException e) {
        // TODO Auto-generated catch block
    } catch (IOException e) {
        // TODO Auto-generated catch block
    } catch (JSONException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}}

	

	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode==0){
		if(resultCode==1)
			text=data.getStringExtra("text");}
		else {
			
			  Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
			}
		super.onActivityResult(requestCode, resultCode, data);
		
		
	}
	
}








class myLocation{
	
	
	public myLocation(String name, String id, JSONArray etaje) {
		super();
		this.name = name;
		this.id = id;
		this.etaje = etaje;
	}
	String name,id;
	JSONArray etaje;
	
	
	
}
