//
//  Stairs.h
//  DrawPad
//
//  Created by Iulian-Bogdan Vlad on 10/19/13.
//  Copyright (c) 2013 Iulian-Bogdan Vlad. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "Shape.h"

@interface Stairs : NSObject <Shape>

typedef enum
{
    kStairsUp,
    kStairsDown,
    kStairsBoth
}StairsType;

@property StairsType stairsType;

@property (nonatomic) CGPoint centerPoint;
@property (nonatomic) CGPoint centerPoint_pixels;

@property (nonatomic) float width;
@property (nonatomic) float height;

@property (strong, nonatomic) UIImage *image;

-(id)initWithType:(StairsType)type;

@end
