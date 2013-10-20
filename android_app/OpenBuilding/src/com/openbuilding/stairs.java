package com.openbuilding;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class stairs implements myObject {
	 public double x,y;

	 Bitmap door_bitmap;
	  
	  
	  
		public stairs(double x, double y, Bitmap stairs_bitmap) {
		super();
		this.x = x;
		this.y = y;
		
		this.door_bitmap = stairs_bitmap;
	}



		@Override
		public Canvas draw(Canvas c, Cadru cadru, double scaleFactor) {
			
			Bitmap target = Bitmap.createScaledBitmap(door_bitmap, (int)(2*scaleFactor),(int)(3* scaleFactor), false);
			
		
			c.drawBitmap(target, (float)((x-cadru.x1)*scaleFactor-target.getWidth()/2), (float)((y-cadru.y1)*scaleFactor-target.getHeight()/2), null);
			
					return c;
		}
	

}
