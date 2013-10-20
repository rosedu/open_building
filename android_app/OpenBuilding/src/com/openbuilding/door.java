package com.openbuilding;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;

public class door implements myObject{
  public double x,y;
  public double rotation_angle;
 Bitmap door_bitmap;
  
  
  
	public door(double x, double y, double rotation_angle, Bitmap door_bitmap) {
	super();
	this.x = x;
	this.y = y;
	this.rotation_angle = rotation_angle;
	this.door_bitmap = door_bitmap;
}



	@Override
	public Canvas draw(Canvas c, Cadru cadru, double scaleFactor) {
		
		Bitmap target = Bitmap.createScaledBitmap(door_bitmap, (int)(scaleFactor),(int)(2* scaleFactor), false);
		
		Matrix matrix = new Matrix();
	
		matrix.setRotate((float)rotation_angle,target.getWidth()/2,target.getHeight()/2);
		target=Bitmap.createBitmap(target, 0, 0, target.getWidth(), target.getHeight(), matrix, false);
		
		//matrix.postTranslate((float)((x-cadru.x1)*scaleFactor), (float)((y-cadru.y1)*scaleFactor));
		c.drawBitmap(target, (float)((x-cadru.x1)*scaleFactor-target.getWidth()/2), (float)((y-cadru.y1)*scaleFactor-target.getHeight()/2), null);
		
				return c;
	}

}
