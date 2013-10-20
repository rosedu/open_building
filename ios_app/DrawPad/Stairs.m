//
//  Stairs.m
//  DrawPad
//
//  Created by Iulian-Bogdan Vlad on 10/19/13.
//  Copyright (c) 2013 Iulian-Bogdan Vlad. All rights reserved.
//

#import "Stairs.h"
#import "UIImage+Rotation.h"

@implementation Stairs
@synthesize scale;

#define IMAGE_URL @"scari_b.png"

-(float)height
{
    return 30;
}

-(float)width
{
    return 30;
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

-(id)initWithType:(StairsType)type
{
    if( (self = [super init]) )
    {
        self.stairsType = type;
        if( type == kStairsBoth )
        {
            self.image = [[UIImage imageNamed:@"scari_b.png"] imageRotatedByDegrees:M_PI];
        }
        else if( type == kStairsDown )
        {
            self.image = [[UIImage imageNamed:@"scari_jos.png"] imageRotatedByDegrees:M_PI];
        }
        else if( type == kStairsUp )
        {
            self.image =[[UIImage imageNamed:@"scari_sus.png"] imageRotatedByDegrees:M_PI];
        }
    }
    
    return self;
}

-(id)init
{
    if( (self = [super init]) )
    {
        self.image = [UIImage imageNamed:@"scari_b.png"];
        self.image = [self.image imageRotatedByDegrees:M_PI];
    }
    
    return self;
}

-(void) draw:(CGContextRef)context inCoordinatesX1:(float)x1 y1:(float)y1 x2:(float)x2 y2:(float)y2 scale:(float)scale2
{
    CGContextDrawImage(context, CGRectMake(self.centerPoint.x * scale - self.scale - x1, self.centerPoint.y * scale - self.scale * 3 / 2 - y1, self.scale * 2, self.scale * 3), self.image.CGImage);
}

@end
