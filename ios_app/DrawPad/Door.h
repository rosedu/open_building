//
//  Door.h
//  DrawPad
//
//  Created by Iulian-Bogdan Vlad on 10/19/13.
//  Copyright (c) 2013 Iulian-Bogdan Vlad. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "Shape.h"

typedef enum {
    kOutsideDoor,
    kInsideDoor
} DoorType;


@interface Door : NSObject<Shape>


@property (nonatomic) CGPoint centerPoint;
@property (nonatomic) CGPoint centerPoint_pixels;
@property DoorType doorType;

@property (nonatomic) float width;
@property (nonatomic) float height;


-(id)initWithDoorType:(DoorType)doorType;

-(void)rotateImageWithDegrees:(float)rotateDegree;

@end
