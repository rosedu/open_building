//
//  Line.h
//  DrawPad
//
//  Created by Iulian-Bogdan Vlad on 10/16/13.
//  Copyright (c) 2013 Iulian-Bogdan Vlad. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "Shape.h"

@interface Line : NSObject
<Shape>

@property (nonatomic) float pixel_x1;
@property (nonatomic) float pixel_y1;
@property (nonatomic) float pixel_x2;
@property (nonatomic) float pixel_y2;

@property (nonatomic) int x1;
@property (nonatomic) int y1;
@property (nonatomic) int x2;
@property (nonatomic) int y2;

-(void)drawInView:(UIImageView *)imageView inCoordinatesX1:(float)x1 y1:(float)y1 x2:(float)x2 y2:(float)y2 scale:(float)scale;
-(void) draw:(CGContextRef)context;
-(void) draw:(CGContextRef)context inCoordinatesX1:(float)x1 y1:(float)y1 x2:(float)x2 y2:(float)y2 scale:(float)scale;

@end
