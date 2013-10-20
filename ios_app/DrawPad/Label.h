//
//  Label.h
//  DrawPad
//
//  Created by Iulian-Bogdan Vlad on 10/20/13.
//  Copyright (c) 2013 Iulian-Bogdan Vlad. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "Shape.h"

@interface Label : NSObject<Shape>

@property (nonatomic) CGPoint centerPoint;
@property (nonatomic) CGPoint centerPoint_pixels;

@property (strong, nonatomic) NSString *stringLabel;

@end
