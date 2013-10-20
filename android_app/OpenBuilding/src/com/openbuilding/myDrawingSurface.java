package com.openbuilding;



import com.openbuilding.mySurfaceView.OnGestureListener;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.view.MotionEvent;
import android.view.SurfaceView;

public class myDrawingSurface extends SurfaceView {

int MINIMUM_DISTANCE=100;	
	
double grid_step=1;
Paint wall_paint;	
int type;
myObject aux_Object;
double Screen_width,Screen_height;
Cadru cadru;
double ScaleFactor;
Bitmap pylon;

Bitmap selected;

double x1_a,x2_a,y1_a,y2_a;
OnGestureListener listener;
DrawingActivity drawingActivity;
public void setOnGestureListener(OnGestureListener listener) {
    this.listener = listener;
}


public void setType(int type, Cadru cadru, double ScaleFactor, DrawingActivity MainAcc_refference,myObject obj )   //1=wall
{this.cadru=cadru;
this.ScaleFactor=ScaleFactor;
this.drawingActivity=MainAcc_refference;
	this.type=type;
	if(obj==null){
if(type==1) aux_Object=new wall(normalizeX((Screen_width*7d/10d)/ScaleFactor+cadru.x1), normalizeX((Screen_width*3d/10d)/ScaleFactor+cadru.x1), normalizeY((Screen_height/2d)/ScaleFactor+cadru.y1), normalizeY((Screen_height/2d)/ScaleFactor+cadru.y1));	
else if(type==2) aux_Object=new door((Screen_width/2d)/ScaleFactor+cadru.x1,(Screen_height/2d)/ScaleFactor+cadru.y1,0,MainAcc_refference.door_original);
else if(type==3) aux_Object=new door((Screen_width/2d)/ScaleFactor+cadru.x1,(Screen_height/2d)/ScaleFactor+cadru.y1,0,MainAcc_refference.door_outside);
else if(type==4) aux_Object=new stairs((Screen_width/2d)/ScaleFactor+cadru.x1,(Screen_height/2d)/ScaleFactor+cadru.y1,MainAcc_refference.stairs_up);
else if(type==5) aux_Object=new stairs((Screen_width/2d)/ScaleFactor+cadru.x1,(Screen_height/2d)/ScaleFactor+cadru.y1,MainAcc_refference.stairs_down);
else if(type==6) aux_Object=new stairs((Screen_width/2d)/ScaleFactor+cadru.x1,(Screen_height/2d)/ScaleFactor+cadru.y1,MainAcc_refference.stairs_both);
else if(type==7) aux_Object=new Text((Screen_width/2d)/ScaleFactor+cadru.x1,(Screen_height/2d)/ScaleFactor+cadru.y1,MainAcc_refference.text_aux);
	
	}
	
	else aux_Object=obj;
redraw();
}

public myObject getCurrentObject()
{return aux_Object;}

public myDrawingSurface(Context context, int Screen_width,int Screen_height) {
		super(context);
		wall_paint=new Paint();
		wall_paint.setColor(Color.argb(50, 0, 0, 255));
		wall_paint.setStrokeWidth(1);
		this.Screen_height=Screen_height;
		this.Screen_width=Screen_width;
		pylon=BitmapFactory.decodeResource(getResources(), R.drawable.pylon);
	pylon=Bitmap.createScaledBitmap(pylon, 40, 40, false);
		
		selected=BitmapFactory.decodeResource(getResources(), R.drawable.border);
		
	}


float StartX,StartY;
float x_aux, y_aux;
int mode=0;
boolean Multi_to_one=false;
float StartDistance=0;
	 public boolean onTouchEvent(MotionEvent event)
	 {if(event.getPointerCount()==1)
	 {
		 
		 if(event.getAction()==MotionEvent.ACTION_DOWN)
	 { if(type==1&&(Math.sqrt((event.getX()-x1_a)*(event.getX()-x1_a)+(event.getY()-y1_a)*(event.getY()-y1_a))<MINIMUM_DISTANCE)) mode=1;
	 else if(type==1&&(Math.sqrt((event.getX()-x2_a)*(event.getX()-x2_a)+(event.getY()-y2_a)*(event.getY()-y2_a))<MINIMUM_DISTANCE)) mode=2;	 
	 else if((type==2||type==3)&&(event.getX())/ScaleFactor+cadru.x1>((door)aux_Object).x-0.5&&(event.getX())/ScaleFactor+cadru.x1<((door)aux_Object).x+0.5&&(event.getY())/ScaleFactor+cadru.y1>((door)aux_Object).y-1&&(event.getY())/ScaleFactor+cadru.y1<((door)aux_Object).y+1) mode=4;
	 else if((type==4||type==5||type==6)&&(event.getX())/ScaleFactor+cadru.x1>((stairs)aux_Object).x-1&&(event.getX())/ScaleFactor+cadru.x1<((stairs)aux_Object).x+1&&(event.getY())/ScaleFactor+cadru.y1>((stairs)aux_Object).y-1.5&&(event.getY())/ScaleFactor+cadru.y1<((stairs)aux_Object).y+1.5) mode=5;
	 else if((type==7)&&(event.getX())/ScaleFactor+cadru.x1>((Text)aux_Object).x&&(event.getX())/ScaleFactor+cadru.x1<((Text)aux_Object).x+((Text)aux_Object).p.measureText(((Text)aux_Object).text)/ScaleFactor&&(event.getY())/ScaleFactor+cadru.y1>((Text)aux_Object).y-1&&(event.getY())/ScaleFactor+cadru.y1<((Text)aux_Object).y+1) mode=6;
	 
	 else{mode=0;	 StartX=event.getX(); StartY=event.getY();}
	System.out.println("type="+type+"mode="+mode);
	 }
	  else if(event.getAction()==MotionEvent.ACTION_MOVE)
	  {if(mode==0)
		  {listener.onDrag((int)(event.getX()-StartX), (int)(event.getY()-StartY));
		  StartX=event.getX();StartY=event.getY();}
		  
	  else if(mode==1) {
		  if(normalizeX(event.getX()/ScaleFactor+cadru.x1)==((wall)aux_Object).x2&&normalizeY(event.getY()/ScaleFactor+cadru.y1)==((wall)aux_Object).y2) mode=2;
		  else 
((wall)aux_Object).x1=normalizeX(event.getX()/ScaleFactor+cadru.x1); ((wall)aux_Object).y1=normalizeX(event.getY()/ScaleFactor+cadru.y1); 
		  }
	  else if(mode==2) {
		  if(normalizeX(event.getX()/ScaleFactor+cadru.x1)==((wall)aux_Object).x1&&normalizeY(event.getY()/ScaleFactor+cadru.y1)==((wall)aux_Object).y1) mode=1;
		  else ((wall)aux_Object).x2=normalizeX(event.getX()/ScaleFactor+cadru.x1); ((wall)aux_Object).y2=normalizeX(event.getY()/ScaleFactor+cadru.y1); }
		
	  else if (mode==4)
	  {((door)aux_Object).x=event.getX()/ScaleFactor+cadru.x1;((door)aux_Object).y=event.getY()/ScaleFactor+cadru.y1;
	  
	  wall w=drawingActivity.closest_wall(event.getX()/ScaleFactor+cadru.x1, event.getY()/ScaleFactor+cadru.y1, ScaleFactor);
	 if(w!=null) {double a=Math.atan(Math.abs(w.x1-w.x2)/Math.abs(w.y1-w.y2))-Math.PI/2;
      ((door)aux_Object).rotation_angle=a/Math.PI*180d;
	 }
	 }
	  else if (mode==5)
	  {((stairs)aux_Object).x=event.getX()/ScaleFactor+cadru.x1;((stairs)aux_Object).y=event.getY()/ScaleFactor+cadru.y1;}
	  else if(mode==6)
	  {((Text)aux_Object).x=event.getX()/ScaleFactor+cadru.x1;((Text)aux_Object).y=event.getY()/ScaleFactor+cadru.y1;}
		 
	  // System.out.println("x1="+((wall)aux_Object).x1);
	 }}
	 else if(event.getPointerCount()==2)
	 {Multi_to_one=true;
		 System.out.println("mode="+mode);

	 if(mode==0||mode==3){
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
	 }}
	 else { 
		  ((wall)aux_Object).x1=normalizeX(event.getX()/ScaleFactor+cadru.x1); ((wall)aux_Object).y1=normalizeX(event.getY()/ScaleFactor+cadru.y1); 
		
		  ((wall)aux_Object).x2=normalizeX(event.getX(1)/ScaleFactor+cadru.x1); ((wall)aux_Object).y2=normalizeX(event.getY(1)/ScaleFactor+cadru.y1); 
		
		 mode=1;
		 
	 }
	 }
	
		 redraw();
		 //draw((float)normalizeX(event.getX()),(float)normalizeY(event.getY()));
		 return true;}
	 
	 
	 
	 void redraw()
	 {System.out.println("called");
		 this.getHolder().setFormat(PixelFormat.TRANSPARENT);
		 Canvas c=this.getHolder().lockCanvas();
	 c.drawColor(0, Mode.CLEAR);
	drawGrid(c);
	 
	aux_Object.draw(c, cadru, ScaleFactor);
	
	if(type==2||type==3) c.drawBitmap(Bitmap.createScaledBitmap(selected, (int)ScaleFactor,(int)(2*ScaleFactor) , false),(float)( (((door)aux_Object).x-cadru.x1)*ScaleFactor-ScaleFactor/2),(float)( (((door)aux_Object).y-cadru.y1)*ScaleFactor-ScaleFactor), null);
	else if(type==4||type==5||type==6) c.drawBitmap(Bitmap.createScaledBitmap(selected, (int)(2*ScaleFactor),(int)(3*ScaleFactor) , false),(float)( (((stairs)aux_Object).x-cadru.x1)*ScaleFactor-ScaleFactor),(float)( (((stairs)aux_Object).y-cadru.y1)*ScaleFactor-ScaleFactor*1.5), null);
	else if(type==7)  c.drawBitmap(Bitmap.createScaledBitmap(selected, (int)(((Text)aux_Object).p.measureText(((Text)aux_Object).text)),(int)(ScaleFactor) , false),(float)( (((Text)aux_Object).x-cadru.x1)*ScaleFactor),(float)( (((Text)aux_Object).y-cadru.y1)*ScaleFactor-ScaleFactor/2), null);
	
	if(type==1){
		x1_a=(Math.min(Math.max(cadru.x1, ((wall)aux_Object).x1),cadru.x2)-cadru.x1)*ScaleFactor;
	x2_a=(Math.min(Math.max(cadru.x1,  ((wall)aux_Object).x2),cadru.x2)-cadru.x1)*ScaleFactor;
	y1_a=(Math.min(Math.max(cadru.y1,  ((wall)aux_Object).y1),cadru.y2)-cadru.y1)*ScaleFactor;
		y2_a=(Math.min(Math.max(cadru.y1,  ((wall)aux_Object).y2),cadru.y2)-cadru.y1)*ScaleFactor;	
	c.drawBitmap(pylon, (float)(x1_a-pylon.getWidth()/2), (float)(y1_a-pylon.getHeight()/2), null);
	c.drawBitmap(pylon, (float)(x2_a-pylon.getWidth()/2), (float)(y2_a-pylon.getHeight()/2), null);
	}
	
	
	
	 this.getHolder().unlockCanvasAndPost(c);
	 this.refreshDrawableState();
	
	this.invalidate();
	
	 }
	 
	
	 public void drawGrid(Canvas c)
	 { double iterator=0;
	if(cadru.x1 >= 0) iterator=(1-(cadru.x1-(int)cadru.x1))*ScaleFactor;
	else iterator=((int)cadru.x1-cadru.x1)*ScaleFactor;
	for(;iterator<Screen_width;iterator+=ScaleFactor)
		c.drawLine((float)iterator, 0, (float)iterator, (float)Screen_height, wall_paint);
	
	if(cadru.y1 >= 0) iterator=(1-(cadru.y1-(int)cadru.y1))*ScaleFactor;
	else iterator=((int)cadru.y1-cadru.y1)*ScaleFactor;
	for(;iterator<Screen_height;iterator+=ScaleFactor)
		c.drawLine(0,(float)iterator, (float)Screen_width, (float)iterator,  wall_paint);
	
	
	 }
	 
	 
	 double normalizeX(double x)
	 {
	 double rest = x%grid_step;
	 if(rest>grid_step/2)
		 x+=grid_step-rest;
	 else x-=rest;
	 return x;
	 }
	 
	 double normalizeY(double y)
	 {
	 double rest = y%grid_step;
	 if(rest>grid_step/2)
		 y+=grid_step-rest;
	 else y-=rest;
	 return y;
	 }

}
