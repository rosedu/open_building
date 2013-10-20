//
//  Door.m
//  DrawPad
//
//  Created by Iulian-Bogdan Vlad on 10/19/13.
//  Copyright (c) 2013 Iulian-Bogdan Vlad. All rights reserved.
//

#import "Door.h"
#import "UIImage+Rotation.h"

@interface Door()

@property (strong, nonatomic) UIImage *image;

@end

@implementation Door
@synthesize scale;


#define IMAGE_URL @"usa2.png"

-(id)initWithDoorType:(DoorType)doorType
{
    if( (self = [super init]))
    {
        self.doorType = doorType;
        if( doorType == kOutsideDoor )
        {
            self.image = [[UIImage imageNamed:@"usa_iesire"] imageRotatedByDegrees:M_PI];
        }
        else
            self.image = [[UIImage imageNamed:@"door.png"] imageRotatedByDegrees:M_PI];
    }
    
    return self;
}

-(id)init
{
    if( (self = [super init])  )
    {
        self.image = [UIImage imageNamed:@"usa2.png"];
    }
    
    return  self;
}

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

-(void)rotateImageWithDegrees:(float)rotateDegree
{
    self.image = [self.image imageRotatedByDegrees:rotateDegree];
}

-(UIImage *)image
{
    return _image;
}

-(void) draw:(CGContextRef)context inCoordinatesX1:(float)x1 y1:(float)y1 x2:(float)x2 y2:(float)y2 scale:(float)scale2
{

    CGContextDrawImage(context, CGRectMake(self.centerPoint.x * scale - self.scale / 2 - x1, self.centerPoint.y * scale - self.scale - y1, self.scale, self.scale * 2), self.image.CGImage);
}

@end
