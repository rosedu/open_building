package com.openbuilding;


import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Text implements myObject {
	 public double x,y;

	String text;
	
	  
	  Paint p;
	  
	



		public Text(double x, double y, String text) {
		super();
		this.x = x;
		this.y = y;
		this.text = text;
		p=new Paint();
		p.setColor(Color.argb(255, 0, 0, 0));
		
	}







		@Override
		public Canvas draw(Canvas c, Cadru cadru, double scaleFactor) {
			
			p.setTextSize((int)(scaleFactor/2));
		    
	       
			
			
			c.drawText(text, (float)((x-cadru.x1)*scaleFactor), (float)((y-cadru.y1)*scaleFactor), p);
			
			
			return c;
		}
	

}
