//
//  Line.m
//  DrawPad
//
//  Created by Iulian-Bogdan Vlad on 10/16/13.
//  Copyright (c) 2013 Iulian-Bogdan Vlad. All rights reserved.
//

#import "Line.h"

@interface Line()


@end

@implementation Line
@synthesize scale;

-(void)setX1:(int)x1
{
    _pixel_x1 = x1 * self.scale;
    _x1 = x1;
}

-(void)setX2:(int)x2
{
    _pixel_x2 = x2 * self.scale;
    _x2 = x2;
}

-(void)setY1:(int)y1
{
    _pixel_y1 = y1 * self.scale;
    _y1 = y1;
}

-(void)setY2:(int)y2
{
    _pixel_y2 = y2 * self.scale;
    _y2 = y2;
}

-(void)setPixel_x1:(float)pixel_x1
{
    _pixel_x1 = pixel_x1;
    _x1 = (int) pixel_x1 / self.scale;
}

-(void)setPixel_x2:(float)pixel_x2
{
    _pixel_x2 = pixel_x2;
    _x2 = (int) pixel_x2 / self.scale;
}

-(void)setPixel_y2:(float)pixel_y2
{
    _pixel_y2 = pixel_y2;
    _y2 = (int) pixel_y2 / self.scale;
}

-(void)setPixel_y1:(float)pixel_y1
{
    _pixel_y1 = pixel_y1;
    _y1 = (int) pixel_y1 / self.scale;
}

-(void) draw:(CGContextRef)context
{
      CGContextSetLineWidth(context, 2);
        CGContextSetStrokeColorWithColor(context, [UIColor blackColor].CGColor);
        CGContextMoveToPoint(context, self.x1, self.y1);
        CGContextAddLineToPoint(context,self.x2,self.y2);
        CGContextStrokePath(context);
}

-(void) draw:(CGContextRef)context inCoordinatesX1:(float)x1 y1:(float)y1 x2:(float)x2 y2:(float)y2 scale:(float)scale2
{
    CGContextSetLineWidth(context, 2);
    CGContextSetStrokeColorWithColor(context, [UIColor blackColor].CGColor);
//    CGContextMoveToPoint(context,(int)(self.pixel_x1 / scale) * scale - x1, (int)(self.pixel_y1 / scale) * scale - y1);
//    CGContextAddLineToPoint(context, (int)(self.pixel_x2 / scale) * scale - x1 , (int)(self.pixel_y2 / scale) * scale -y1 );
        CGContextMoveToPoint(context, self.x1 *scale2 - x1, self.y1 * scale2 - y1);
        CGContextAddLineToPoint(context, self.x2 * scale2  - x1 , self.y2 * scale2  -y1 );

    CGContextStrokePath(context);

}

-(CGPoint)normalizeX:(float)x Y:(float)y
{
    int dif_x = (int)x % 20;
    int dif_y = (int)y % 20;
    
    if( dif_x < 10 && dif_y < 10 ) return CGPointMake(x - dif_x, y - dif_y);
    else if( dif_x < 10 && dif_y > 10 ) return CGPointMake(x - dif_x, y + (20 - dif_y));
    else if( dif_x > 10 && dif_y < 10 ) return CGPointMake(x + (20 - dif_x), y - dif_y);
    else return CGPointMake(x - dif_x, y - dif_y);
}

//-(void)drawLineInView:(UIImageView *)imageView inCoordinatesX1:(float)x1 y1:(float)y1 x2:(float)x2 y2:(float)y2 scale:(float)scale
//{
//    
//    UIImageView *newImageView = [[UIImageView alloc] initWithFrame:imageView.frame];
//    
//    UIGraphicsBeginImageContext(newImageView.frame.size);
//    
//    [newImageView.image drawInRect:CGRectMake(0, 0, newImageView.frame.size.width, newImageView.frame.size.height)];
//    CGContextMoveToPoint(UIGraphicsGetCurrentContext(), x1 + self.x1 * scale, y1 + self.y1 * scale);
//    CGContextAddLineToPoint(UIGraphicsGetCurrentContext(), x1 + self.x2 * scale, y1 + self.y2 * scale);
//    CGContextSetLineCap(UIGraphicsGetCurrentContext(), kCGLineCapRound);
//    CGContextSetLineWidth(UIGraphicsGetCurrentContext(), 10 );
//    CGContextSetRGBStrokeColor(UIGraphicsGetCurrentContext(), 1, 0, 0, 1.0);
//    CGContextSetBlendMode(UIGraphicsGetCurrentContext(),kCGBlendModeNormal);
//    
//    CGContextStrokePath(UIGraphicsGetCurrentContext());
//    imageView.image = UIGraphicsGetImageFromCurrentImageContext();
//    UIGraphicsEndImageContext();
//}

@end
