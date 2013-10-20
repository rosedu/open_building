package com.openbuilding;



import java.io.IOException;
import java.util.ArrayList;

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
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.openbuilding.mySurfaceView.OnGestureListener;

public class DrawingActivity extends Activity implements Callback {
ArrayList<myObject> objects= new ArrayList<myObject>();
Cadru cadru;
//double cadru.x1=9;
//double cadru.x2=15;
double scaleFactor;
int curent_level=0;
//double cadru.y1=5;
SurfaceHolder holder;
boolean drawing=false;
myDrawingSurface drawingSurface;
ImageView done;
int screen_height,screen_width;
boolean edit=true;
ImageView qr;
Bitmap me;

String id;
ArrayList<Integer> nivele;


public Bitmap door_original;
public Bitmap door_outside;
public Bitmap stairs_up,stairs_down,stairs_both;
LinearLayout toolBar;
boolean useless=false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.drawing_activity);
		
		toolBar=(LinearLayout)findViewById(R.id.tool);
		qr=(ImageView)findViewById(R.id.imageQr);
		
		qr.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
			if(event.getAction()==event.ACTION_DOWN) qr.setImageResource(R.drawable.where_am_i_apasat);
			else if(event.getAction()==event.ACTION_UP) {qr.setImageResource(R.drawable.where_am_i);
			
			try {
			    Intent intent = new Intent("com.google.zxing.client.android.SCAN");
			    intent.putExtra("SCAN_MODE", "QR_CODE_MODE"); // "PRODUCT_MODE for bar codes

			    startActivityForResult(intent, 0);
			} catch (Exception e) {    
			    Uri marketUri = Uri.parse("market://details?id=com.google.zxing.client.android");
			    Intent marketIntent = new Intent(Intent.ACTION_VIEW,marketUri);
			    startActivity(marketIntent);
			}
			
			
			}
				return true;
			}
		});
		
		
		Intent i=getIntent();
		
		id=i.getStringExtra("id");
		try {
			JSONArray jsn=new JSONArray(i.getStringExtra("etaje"));
           nivele=new ArrayList<Integer>();			
			for(int j=0;j<jsn.length();j++)
			{try {
				nivele.add(jsn.getInt(j));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}}
			
			
			if(nivele.size()>0) if (nivele.contains(0)){ edit=false; findViewById(R.id.imageSave).setVisibility(View.INVISIBLE);toolBar.setVisibility(View.INVISIBLE); new Thread(new Runnable() {
				
				@Override
				public void run() {
					reinitialise_array(0);
					
				}
			}) .start();     }
			else qr.setVisibility(View.INVISIBLE);
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		System.out.println("size="+nivele.size());
	
		
		
		RelativeLayout r=(RelativeLayout)findViewById(R.id.rel);
		door_original=BitmapFactory.decodeResource(getResources(), R.drawable.door);
		door_outside=BitmapFactory.decodeResource(getResources(), R.drawable.usa_iesire);
		stairs_both=BitmapFactory.decodeResource(getResources(), R.drawable.scari_b);
		stairs_down=BitmapFactory.decodeResource(getResources(), R.drawable.scari_jos);
		stairs_up=BitmapFactory.decodeResource(getResources(), R.drawable.scari_sus);
		me=BitmapFactory.decodeResource(getResources(), R.drawable.you_are_here);
		
		DisplayMetrics displaymetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		screen_height =  displaymetrics.widthPixels;
		 screen_width =displaymetrics.heightPixels;
		
		 cadru=new Cadru();
		 cadru.x1=0;
		 cadru.x2=20;
		 cadru.y1=0;
		mySurfaceView s= new mySurfaceView(this);
		s.getHolder().addCallback(this);
	
		r.addView(s);
		
		
		
		drawingSurface= new myDrawingSurface(this,screen_height,screen_width);
		
		
		drawingSurface.getHolder().addCallback(new Callback() {
			
			@Override
			public void surfaceDestroyed(SurfaceHolder holder) {
			
				
			}
			
			@Override
			public void surfaceCreated(SurfaceHolder holder) {
				// TODO Auto-generated method stub
				if(!useless){
					useless=true;
				Intent i=new Intent(DrawingActivity.this,UselessActivity.class);
				startActivity(i);}
			}
			
			@Override
			public void surfaceChanged(SurfaceHolder holder, int format, int width,
					int height) {
				// TODO Auto-generated method stub
				
			}
		});
		
		drawingSurface.setOnGestureListener(new OnGestureListener() {
			
			@Override
			public void onZoom(int x, int y, double ratio) {
				double unit= cadru.x2-cadru.x1;
				cadru.x1-=(double)x/scaleFactor;
				cadru.y1-=(double)y/scaleFactor;
				
				cadru.x2=cadru.x1+unit/ratio;
				cadru.y2=cadru.y1+(cadru.x2-cadru.x1)/screen_height*screen_width;
				scaleFactor= screen_width/(cadru.x2-cadru.x1);
				
				drawingSurface.ScaleFactor=scaleFactor;
				drawingSurface.cadru=cadru;
				
				draw();
				
			}
			
			@Override
			public void onDrag(int x, int y) {
				cadru.x1-=(double)x/scaleFactor;
				cadru.x2-=(double)x/scaleFactor;
				cadru.y1-=(double)y/scaleFactor;
				draw();				
			}
			
			@Override
			public void onClick(float x, float y) {
			
			
			}
		});
		
		drawingSurface.setVisibility(View.INVISIBLE);
		r.addView(drawingSurface); 
		
		s.setOnGestureListener(new OnGestureListener() {
			
			@Override
			public void onZoom(int x, int y, double ratio) {
				
				
				double unit= cadru.x2-cadru.x1;
				cadru.x1-=(double)x/scaleFactor;
				cadru.y1-=(double)y/scaleFactor;
				
				cadru.x2=cadru.x1+unit/ratio;
				cadru.y2=cadru.y1+(cadru.x2-cadru.x1)/screen_height*screen_width;
				scaleFactor= screen_width/(cadru.x2-cadru.x1);
				
				
				draw();}
			
			@Override
			public void onDrag(int x, int y) {
				
				
			
				cadru.x1-=(double)x/scaleFactor;
				cadru.x2-=(double)x/scaleFactor;
				cadru.y1-=(double)y/scaleFactor;
				draw();
					
			}
			
			@Override
			public void onClick(float x, float y) {
				if(edit){
				myObject w=closest_obj(x/scaleFactor+cadru.x1, y/scaleFactor+cadru.y1, scaleFactor);
				System.out.println("onclick");
				if(w!=null){
			drawing=true;
			  drawingSurface.setVisibility(View.VISIBLE);
			  objects.remove(w);
			  draw();
			    drawingSurface.setType(getObjectType(w), cadru, scaleFactor,DrawingActivity.this,w);}
				 done.setVisibility(View.VISIBLE);
				 toolBar.setVisibility(View.INVISIBLE);}
			}
		});
		
		
		
		
		
		RelativeLayout.LayoutParams par=new RelativeLayout.LayoutParams(200, 70);
		
		
		
		
	done=(ImageView)findViewById(R.id.imageDone);
	if(!edit){done.setImageResource(R.drawable.edit_build);
	done.setVisibility(View.VISIBLE);}
	done.setOnTouchListener(new OnTouchListener() {
		
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if(edit){
			if(event.getAction()==MotionEvent.ACTION_DOWN) done.setImageResource(R.drawable.done_p);
			else if(event.getAction()==MotionEvent.ACTION_UP) {done.setImageResource(R.drawable.done);
			button_done();
			done.setVisibility(View.INVISIBLE);
			toolBar.setVisibility(View.VISIBLE);}
			
			
			
			}
			else{if(event.getAction()==MotionEvent.ACTION_DOWN) done.setImageResource(R.drawable.edit_build_p);
			else if(event.getAction()==MotionEvent.ACTION_UP) {done.setImageResource(R.drawable.edit_build);
			edit=true;
			qr.setVisibility(View.INVISIBLE);
			findViewById(R.id.imageSave).setVisibility(View.VISIBLE);
			done.setImageResource(R.drawable.done);
			done.setVisibility(View.INVISIBLE);
			toolBar.setVisibility(View.VISIBLE);
		
			}
			}
			return true;
		}
	});
		
		
	qr.bringToFront();
		done.bringToFront();
		toolBar.bringToFront();
		ImageView save=(ImageView)findViewById(R.id.imageSave);
		save.bringToFront();
		
	}

	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void surfaceCreated(SurfaceHolder arg0) {//first time, cand userul intra in app
		holder=arg0;
	cadru.y2=cadru.y1+(cadru.x2-cadru.x1)/screen_height*screen_width;
	scaleFactor= screen_width/(cadru.x2-cadru.x1);    
draw();
		
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {
		
	}

	
	public void draw()
	{try {
		
	if(holder!=null){
		Canvas c= holder.lockCanvas();
	
		c.drawARGB(255, 255, 255, 255);
		
	for(int i=0;i<objects.size();i++)
		objects.get(i).draw(c, cadru, scaleFactor);
	
	holder.unlockCanvasAndPost(c);
	}
	} catch (NullPointerException e) {
		// TODO: handle exception
	}}

	
	public void button_done()
	{drawing=false;
	drawingSurface.setVisibility(View.INVISIBLE);
	objects.add(drawingSurface.getCurrentObject());
	draw();
	
	}
	

	public void wall(View v)
	{	drawing=true;
    drawingSurface.setVisibility(View.VISIBLE);
    drawingSurface.setType(1, cadru, scaleFactor,this,null);
    done.setVisibility(View.VISIBLE);
    toolBar.setVisibility(View.INVISIBLE);
}
	
	public void door(View v)
	{drawing=true;
	  drawingSurface.setVisibility(View.VISIBLE);
	    drawingSurface.setType(2, cadru, scaleFactor,this,null);	
		
	    done.setVisibility(View.VISIBLE);
	    toolBar.setVisibility(View.INVISIBLE);
	}
	
	public void exit(View v)
	{drawing=true;
	  drawingSurface.setVisibility(View.VISIBLE);
	    drawingSurface.setType(3, cadru, scaleFactor,this,null);	
	    done.setVisibility(View.VISIBLE);
	    toolBar.setVisibility(View.INVISIBLE);
		
	}
	
	

	public void up(View v)
	{	drawing=true;
    drawingSurface.setVisibility(View.VISIBLE);
    drawingSurface.setType(4, cadru, scaleFactor,this,null);
    done.setVisibility(View.VISIBLE);
    toolBar.setVisibility(View.INVISIBLE);
}
	

	public void down(View v)
	{	drawing=true;
    drawingSurface.setVisibility(View.VISIBLE);
    drawingSurface.setType(5, cadru, scaleFactor,this,null);
    done.setVisibility(View.VISIBLE);
    toolBar.setVisibility(View.INVISIBLE);
}
	
	

	public void both(View v)
	{	drawing=true;
    drawingSurface.setVisibility(View.VISIBLE);
    drawingSurface.setType(6, cadru, scaleFactor,this,null);
    done.setVisibility(View.VISIBLE);
    toolBar.setVisibility(View.INVISIBLE);
}
	
	

	public void text(View v)
	{	Intent i=new Intent(DrawingActivity.this,DialogAct.class);
	startActivityForResult(i, 100);	
	
    
    
}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	if(requestCode==100){
		if(resultCode==1){text_aux=data.getStringExtra("text");
		drawing=true;
	    drawingSurface.setVisibility(View.VISIBLE);
	    drawingSurface.setType(7, cadru, scaleFactor,this,null);
	    done.setVisibility(View.VISIBLE);
	    toolBar.setVisibility(View.INVISIBLE);}
		
	
	}
		
	else   if (requestCode == 0) {
        if (resultCode == RESULT_OK) {
           try{
        	String contents = data.getStringExtra("SCAN_RESULT");
        System.out.println("contents="+contents);
       String[] a = contents.split(";");
        stairs s=new stairs(Integer.parseInt(a[0]), Integer.parseInt(a[1]), me);
        double aux=-cadru.x1+cadru.x2;
        double auy=cadru.y2-cadru.y1;
        cadru.x1=s.x-((double)screen_width/scaleFactor)/2d;
        cadru.x2=cadru.x1+aux;
        cadru.y1=s.y-((double)screen_height/scaleFactor)/2d;
        cadru.y2=cadru.y1+auy;
        if(aux_index!=-1)
        {objects.remove(aux_index);}
        objects.add(s);
        aux_index=objects.size()-1;
draw();}catch(NumberFormatException e){}
        }
       
    }	
	};
	String text_aux;
	int aux_index=-1;
	
	
	
	
	
	
	public wall closest_wall(double x, double y, double scale)
	{double min=60;
	wall w=null;
	System.out.println("x="+x);
	System.out.println("y="+y);
		for(int i=0;i<objects.size();i++)
		{try {
			
		
		 		{wall aux=(wall)objects.get(i);
		 		double dist=distanceFromPointToWall(aux, x, y, scale);
		 		System.out.println("dist to wall"+i+" = "+dist);
		 		if(dist<min)
		 		{min=dist;
		 		w=aux;}
		 		}
		} catch (ClassCastException e) {
			// TODO: handle exception
		}
		}
		
		
		return w;}
	
	public myObject closest_obj(double x, double y, double scale)
	{double min=100;
	myObject obj=null;
	System.out.println("x="+x);
	System.out.println("y="+y);
		for(int i=0;i<objects.size();i++)
		{double dist=150;
			try {
			
		
		 		{wall aux=(wall)objects.get(i);
		 		 dist=distanceFromPointToWall(aux, x, y, scale);
		 		
		 		
		 		}
		} catch (ClassCastException e) {
			// TODO: handle exception
		}
		
		
		
		try{door aux=(door)objects.get(i);
			dist=distance(x, y, aux.x, aux.y);} catch (ClassCastException e) {}
		try{stairs aux=(stairs)objects.get(i);
		dist=distance(x, y, aux.x, aux.y);} catch (ClassCastException e) {}
		try{Text aux=(Text)objects.get(i);
		dist=distance(x, y, aux.x+(aux.p.measureText(aux.text)/scaleFactor), aux.y);} catch (ClassCastException e) {}
		if(dist<min)
 		{min=dist;
 		obj=objects.get(i);}
		}
		
		
		return obj;}
	
	

	public double distanceFromPointToWall(wall w, double x, double y, double scale)
	{//w.x1=0; w.x2=5;
	//w.y1=2;  w.y2=2;
	//x=3;
	//y=5;
	//scale=100;
	
	
	double x1=w.x2*scale-w.x1*scale;
	double y1=w.y2*scale-w.y1*scale;//v
	
	System.out.println("x1="+x1);
	System.out.println("y1="+y1);
	
	double px=x*scale-w.x1*scale;
	double py=y*scale-w.y1*scale;//w
	System.out.println("px="+px);
	System.out.println("py="+py);
	double c1=dotproduct(px, py, x1, y1);
	double c2=dotproduct(x1, y1, x1, y1);
	System.out.println("c1="+c1);
	System.out.println("c2="+c2);
	double d=0;
	if(c1<=0)
	{d=distance(x*scale,y*scale,w.x1*scale,w.y1*scale);}
	else if(c2<=c1)
		d=distance(x*scale, y*scale, w.x2*scale,w.y2*scale);
	else {double b=c1/c2;
	double pbx=w.x1*scale+b*x1;
	double pby=w.y1*scale+b*y1;
	System.out.println("pbx="+pbx);
	System.out.println("pby"+pby);
	d=distance(x*scale,y*scale,pbx,pby);
	System.out.println("d="+d);
	}
	
	return d;}
	
	public double dotproduct(double x1,double y1,double x2,double y2)
	{return x1*x2+y1*y2;}
	
	public double distance(double x1,double y1,double x2,double y2)
	{return Math.sqrt((x1-x2)*(x1-x2)+(y1-y2)*(y1-y2));}
	
	
	public int getObjectType(myObject obj)
	{try {wall a=(wall) obj; return 1;} catch (ClassCastException e) {	
	try {door a=(door) obj;
	if(a.door_bitmap==door_original) return 2;
	else return 3;
	
	}catch (ClassCastException e2) {
		try{
	stairs a=(stairs) obj;
	if(a.door_bitmap==stairs_up) return 4;
	else if(a.door_bitmap==stairs_down) return 5;
	else if(a.door_bitmap==stairs_both) return 6;
	else return 10;
		}
	catch (ClassCastException e3) {return 7;}
		
	}}}
	
	
	public void reinitialise_array(int level)
	{
		request_floor(id, level);
		
		
		
	}
	
	public void request_floor(String id, int number)
	{   HttpClient httpclient = new DefaultHttpClient();
    HttpPost httppost = new HttpPost("http://wyliodrin.com:8000/get_floor");
httppost.setHeader("Content-Type", "application/json");
//httppost.setHeader("Accept", "application/json");
    try {
        // Add your data
    	JSONObject holder;
		try {
			holder = new JSONObject("{locationID:"+id+",nr:"+number+"}");
		
    	
    	StringEntity se = new StringEntity(holder.toString());

        // Execute HTTP Post Request
    	httppost.setEntity(se);
        HttpResponse response = httpclient.execute(httppost);
        String result = EntityUtils.toString(response.getEntity());
        
        objects.clear();
     JSONArray ja = ( (JSONObject) (new JSONArray(result)).get(0)).getJSONArray("items");
     
     for(int i=0;i<ja.length();i++)
     {JSONObject jobj=(JSONObject) ja.get(i);
     myObject obj=null;
     
     if(jobj.getString("objType").equalsIgnoreCase("wall"))
     {JSONObject aux= jobj.getJSONObject("wallInfo");
     obj= new wall(aux.getInt("x1"), aux.getInt("x2"), aux.getInt("y1"), aux.getInt("y2"));
     }
     else if(jobj.getString("objType").equalsIgnoreCase("door"))
     {JSONObject aux= jobj.getJSONObject("doorInfo");
     if(aux.getString("direction").equalsIgnoreCase("inside"))
     obj=new door(aux.getInt("x"), aux.getInt("y"), aux.getInt("angle"), door_original);
     else  obj=new door(aux.getInt("x"), aux.getInt("y"), aux.getInt("angle"), door_outside);}
     else if(jobj.getString("objType").equalsIgnoreCase("stair")){
    	 JSONObject aux= jobj.getJSONObject("stairInfo");
    	 if(aux.getString("stairType").equalsIgnoreCase("up"))
    		 obj=new stairs(aux.getInt("x"), aux.getInt("y"), stairs_up);
    	 else if(aux.getString("stairType").equalsIgnoreCase("down"))
    		 obj=new stairs(aux.getInt("x"), aux.getInt("y"), stairs_down);
    	 else  obj=new stairs(aux.getInt("x"), aux.getInt("y"), stairs_both);
     } 
     else if(jobj.getString("objType").equalsIgnoreCase("label")){
    	 JSONObject aux= jobj.getJSONObject("labelInfo");
    	 obj= new Text(aux.getInt("x"), aux.getInt("y"), aux.getString("text"));
    	 
    	 
     }
     
   if(obj!=null)  objects.add(obj);
     }
     runOnUiThread(new Runnable() {
		
		@Override
		public void run() {
			draw();
			
		}
	});
        System.out.println("result="+result);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    } catch (ClientProtocolException e) {
        // TODO Auto-generated catch block
    } catch (IOException e) {
        // TODO Auto-generated catch block
    }}
	
	public void save(View v)
	{new Thread(new Runnable() {
		
		@Override
		public void run() {
		String json="{\"locationID\":\""+id+"\",\"nr\":"+curent_level+",\"items\":[";
			for (int i=0;i<objects.size();i++)
			{if(getObjectType(objects.get(i))==1)
			{wall aux=(wall)objects.get(i);
			json+="{\"objType\":\"wall\", \"wallInfo\":{\"x1\":"+aux.x1+",	\"y1\":"+aux.y1+",\"x2\":"+aux.x2+",\"y2\":"+aux.y2+"}}";

			}
			else if(getObjectType(objects.get(i))==2)
					{door aux=(door)objects.get(i);
					
					json+="{\"objType\":\"door\",	\"doorInfo\":{\"direction\":\"inside\",  \"x\":"+aux.x+",\"y\":"+aux.y+",	\"angle\":"+aux.rotation_angle+"}}";}
			else if(getObjectType(objects.get(i))==3)
			{door aux=(door)objects.get(i);
					json+="{\"objType\":\"door\",	\"doorInfo\":{\"direction\":\"outside\",  \"x\":"+aux.x+",\"y\":"+aux.y+",	\"angle\":"+aux.rotation_angle+"}}";
					}
			else if(getObjectType(objects.get(i))==4)
			{stairs aux=(stairs)objects.get(i);
			   json+="{\"objType\":\"stair\",\"stairInfo\":{\"x\":"+aux.x+",\"y\":"+aux.y+",\"stairType\":\"up\"}}";
				}
			else if(getObjectType(objects.get(i))==5)
			{stairs aux=(stairs)objects.get(i);
			   json+="{\"objType\":\"stair\",\"stairInfo\":{\"x\":"+aux.x+",\"y\":"+aux.y+",\"stairType\":\"down\"}}";
				}
			else if(getObjectType(objects.get(i))==6)
			{stairs aux=(stairs)objects.get(i);
			   json+="{\"objType\":\"stair\",\"stairInfo\":{\"x\":"+aux.x+",\"y\":"+aux.y+",\"stairType\":\"both\"}}";
				}
			else if(getObjectType(objects.get(i))==7)
			{Text aux=(Text)objects.get(i);
			   json+="{\"objType\":\"label\",\"labelInfo\":{\"x\":"+aux.x+",\"y\":"+aux.y+",\"text\":\""+aux.text+"\"}}";
				}
			if(i<objects.size()-1&&getObjectType(objects.get(i))!=10) json+=",";
			}
				
			json+="]}";
			
			
			
			
			
			
			System.out.println("json="+json);
			
			
			
			
			
			
			 HttpClient httpclient = new DefaultHttpClient();
			    HttpPost httppost = new HttpPost("http://wyliodrin.com:8000/add_floor");
			httppost.setHeader("Content-Type", "application/json");
			//httppost.setHeader("Accept", "application/json");
			    try {
			        // Add your data
			    	JSONObject holder;
					try {
						holder = new JSONObject(json);
					
			    	
			    	StringEntity se = new StringEntity(holder.toString());

			        // Execute HTTP Post Request
			    	httppost.setEntity(se);
			        HttpResponse response = httpclient.execute(httppost);
			        String result = EntityUtils.toString(response.getEntity());
			System.out.println("result="+result);
			
			
			
			
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
		
		
		
		v.setVisibility(View.INVISIBLE);
		edit=false;
		qr.setVisibility(View.VISIBLE);
		done.setVisibility(View.VISIBLE);
		done.setImageResource(R.drawable.edit_build);
		toolBar.setVisibility(View.INVISIBLE);
	}
	
	
	
}
