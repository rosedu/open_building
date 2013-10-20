//
//  YouAreHere.m
//  DrawPad
//
//  Created by Iulian-Bogdan Vlad on 10/20/13.
//  Copyright (c) 2013 Iulian-Bogdan Vlad. All rights reserved.
//

#import "YouAreHere.h"
#import "UIImage+Rotation.h"

@interface YouAreHere()

@property (strong, nonatomic) UIImage *image;

@end

@implementation YouAreHere
@synthesize scale;

-(UIImage *)image
{
    return [[UIImage imageNamed:@"you_are_here.png"] imageRotatedByDegrees:M_PI];
}

-(void)setCenterPoint_pixels:(CGPoint)centerPoint_pixels
{
    _centerPoint = CGPointMake((int)centerPoint_pixels.x / scale, (int)centerPoint_pixels.y / scale);
    _centerPoint_pixels = centerPoint_pixels;
}

-(void)setCenterPoint:(CGPoint)centerPoint
{
    _centerPoint_pixels = CGPointMake(centerPoint.x * scale, centerPoint.y * scale);
    _centerPoint = centerPoint;
}

-(void) draw:(CGContextRef)context inCoordinatesX1:(float)x1 y1:(float)y1 x2:(float)x2 y2:(float)y2 scale:(float)scale2
{
    NSLog(@"%f, %f",self.centerPoint.x * scale - self.scale - x1,self.centerPoint.y * scale - self.scale - y1);
    
    CGContextDrawImage(context, CGRectMake(self.centerPoint.x * scale - self.scale / 2 - x1, self.centerPoint.y * scale - self.scale / 2 - y1, self.scale * 2, self.scale * 2), self.image.CGImage);
}

@end
