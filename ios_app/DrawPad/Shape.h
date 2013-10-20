//
//  Shape.h
//  DrawPad
//
//  Created by Iulian-Bogdan Vlad on 10/19/13.
//  Copyright (c) 2013 Iulian-Bogdan Vlad. All rights reserved.
//

#import <Foundation/Foundation.h>

@protocol Shape<NSObject>

-(void) draw:(CGContextRef)context inCoordinatesX1:(float)x1 y1:(float)y1 x2:(float)x2 y2:(float)y2 scale:(float)scale;
@property (nonatomic) float scale;

@end
