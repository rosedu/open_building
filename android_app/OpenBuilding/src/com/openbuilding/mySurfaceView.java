package com.openbuilding;

import android.content.Context;
import android.view.MotionEvent;
import android.view.SurfaceView;

public class mySurfaceView extends SurfaceView {

	OnGestureListener listener;
	public static int MIN_DISTANCE=30;
	public boolean Multi_to_one=false;
	
	public void setOnGestureListener(OnGestureListener listener) {
	    this.listener = listener;
	}
	
	
	public mySurfaceView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	 
	 int mode=0;
	float StartX,StartY;
	float x_aux, y_aux;
	float StartDistance;
	 public boolean onTouchEvent(MotionEvent event)
	 {if(event.getPointerCount()==1)
	 {if(event.getAction()==MotionEvent.ACTION_DOWN)
	 { StartX=event.getX(); StartY=event.getY();
	 mode=1;
	 }
	  else if(event.getAction()==MotionEvent.ACTION_MOVE)
	  {System.out.println("moove");
		  if (mode==2||(event.getX()-StartX)*(event.getX()-StartX)+(event.getY()-StartY)*(event.getY()-StartY)>MIN_DISTANCE*MIN_DISTANCE)
		  {mode=2; 
		  if(Multi_to_one)Multi_to_one=false;
		  
		   else{listener.onDrag((int)(event.getX()-StartX), (int)(event.getY()-StartY));}
		  StartX=event.getX();StartY=event.getY();
		  }}
	 
	 }
		 
	 else if(event.getPointerCount()==2)
	 {Multi_to_one=true;
		 System.out.println("mode="+mode);

	 if(mode!=3)
	 {mode=3;
		 System.out.println("if");
	 StartX=(event.getX(0)+event.getX(1))/2;  StartY=(event.getY(0)+event.getY(1))/2;
	 StartDistance= (float) Math.sqrt( (event.getX(0)-event.getX(1))* (event.getX(0)-event.getX(1))+ (event.getY(0)-event.getY(1))* (event.getY(0)-event.getY(1)));
	 }
	 
	 else {System.out.println("else");
		float newX=(event.getX(0)+event.getX(1))/2; float newY=(event.getY(0)+event.getY(1))/2;
	//	System.out.println("newX="+newX+" newY="+newY);
		
		 float newDistance=  (float) Math.sqrt( (event.getX(0)-event.getX(1))* (event.getX(0)-event.getX(1))+ (event.getY(0)-event.getY(1))* (event.getY(0)-event.getY(1)));
		float ratio=newDistance/StartDistance;
		StartX*=ratio;
		StartY*=ratio;
		 listener.onZoom((int)(newX-StartX), (int)(newY-StartY), ratio);
			StartDistance=newDistance;
			StartX=newX; StartY=newY;
	 }
	 }
	 
	if(event.getAction()==MotionEvent.ACTION_UP)
		{System.out.println("mode="+mode);
		if (mode==1) listener.onClick(event.getX(),event.getY());
		 mode=0;
		 System.out.println(" mode=0;");}
	 
		 return true;}
	
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 public interface OnGestureListener {
	       public void onClick(float x, float y);	
	       public void onDrag(int x,int y);
	       public void onZoom(int x,int y, double ratio);
	}
}
