package com.openbuilding;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;


public class wall implements myObject {
	

		
	
		public wall(double x1, double x2, double y1, double y2) {
		super();
		this.x1 = x1;
		this.x2 = x2;
		this.y1 = y1;
		this.y2 = y2;
		paint = new Paint();
		paint.setColor(Color.argb(200, 31, 11, 1));
		paint.setStrokeWidth(4);
		
	}

		public wall(double x1, double x2, double y1, double y2, Paint paint) {
		super();
		this.x1 = x1;
		this.x2 = x2;
		this.y1 = y1;
		this.y2 = y2;
		this.paint = paint;
	}

		double x1,x2,y1,y2; //in metrii
		Paint paint;
		
		public Canvas draw(Canvas c,Cadru cadru, double scaleFactor)
		{
			//magic happens
			if(Math.min(x1, x2)<cadru.x2&&Math.max(x1, x2)>cadru.x1&&Math.min(y1, y2)<cadru.y2&&Math.max(y1, y2)>cadru.y1)
			{
				
		//double	x1_a=Math.min(Math.max(cadru.x1, x1),cadru.x2);
		//double	x2_a=Math.min(Math.max(cadru.x1, x2),cadru.x2);
		//double	y1_a=Math.min(Math.max(cadru.y1, y1),cadru.y2);
		//double	y2_a=Math.min(Math.max(cadru.y1, y2),cadru.y2);	
		
			
			c.drawLine((int)((x1-cadru.x1)*scaleFactor),(int)( (y1-cadru.y1)*scaleFactor),(int)((x2-cadru.x1)*scaleFactor), (int)((y2-cadru.y1)*scaleFactor), paint);
			}
				
			return c;
		}



}
