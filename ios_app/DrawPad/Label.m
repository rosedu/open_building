//
//  Label.m
//  DrawPad
//
//  Created by Iulian-Bogdan Vlad on 10/20/13.
//  Copyright (c) 2013 Iulian-Bogdan Vlad. All rights reserved.
//

#import "Label.h"


@implementation Label
@synthesize scale = _scale;

-(void)setScale:(float)scale
{
    NSLog(@"%f",scale);
    _scale = scale;
}

-(void)setStringLabel:(NSString *)stringLabel
{
    NSLog(@"string %@",stringLabel);
    _stringLabel = stringLabel;
}

-(void)setCenterPoint_pixels:(CGPoint)centerPoint_pixels
{
    NSLog(@"%f %f",self.scale,self.scale);
    _centerPoint = CGPointMake((int)centerPoint_pixels.x / self.scale, (int)centerPoint_pixels.y / self.scale);
    _centerPoint_pixels = centerPoint_pixels;
}

-(void)setCenterPoint:(CGPoint)centerPoint
{
    _centerPoint_pixels = CGPointMake(centerPoint.x * self.scale, centerPoint.y * self.scale);
    _centerPoint = centerPoint;
}

-(void) draw:(CGContextRef)context inCoordinatesX1:(float)x1 y1:(float)y1 x2:(float)x2 y2:(float)y2 scale:(float)scale2
{
    CGContextSetFillColorWithColor(context, [UIColor blackColor].CGColor);
    CGContextSelectFont(context, "Helvetica Neue Bold" , scale2 / 2, kCGEncodingMacRoman);
    CGContextSetTextMatrix(context, CGAffineTransformMakeScale(1, -1));
    CGContextSetShadowWithColor(context, CGSizeMake(0.0, 1.0), 1.0, [[UIColor whiteColor] CGColor]);
    
    NSLog(@"draw label %f %f",self.centerPoint.x,self.centerPoint.y * self.scale - y1);
    
    CGContextShowTextAtPoint(context, self.centerPoint.x * self.scale - x1, self.centerPoint.y * self.scale - y1, [self.stringLabel UTF8String], self.stringLabel.length);
    //CGContextDrawImage(context, CGRectMake(self.centerPoint.x * scale - self.scale / 2 - x1, self.centerPoint.y * scale - self.scale - y1, self.scale, self.scale * 2), self.image.CGImage);
}

@end
