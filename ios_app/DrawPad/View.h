//
//  View.h
//  DrawPad
//
//  Created by Iulian-Bogdan Vlad on 10/17/13.
//  Copyright (c) 2013 Iulian-Bogdan Vlad. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "Line.h"
#import "Door.h"

@interface View : UIView

@property (strong, nonatomic) UIImage *bitmap;

@property (strong, nonatomic) UIImage *tempBitmap;

@property (strong, nonatomic) UIImage *gridBitmap;

@property (strong, nonatomic) NSMutableArray *tempItems;

@property (strong, nonatomic) NSMutableArray *shapes;

@property int currentLevel;

@property bool editMode;

-(void)setYourPosition:(CGPoint)point;

-(void)addTempShape:(id<Shape>)shape;
-(void)clearTempShapeBuffer;
-(void)refreshTempImage;
-(void)addTempShapesToBitmap;

-(void)addObject:(Line *)line inCoordinatesX1:(float)x1 y1:(float)y1 x2:(float)x2 y2:(float)y2 scale:(float)scale;
-(void)addObject:(id<Shape>)line;
-(void)refresh;
-(void)initView;
-(void)removeShape:(id<Shape>)line;
-(Line *)returnShapeAtCoordinates:(CGPoint)point;

-(id<Shape>)returnWallAtCoordinates:(CGPoint)point;

@property float frame_x1;
@property float frame_y1;
@property float frame_x2;
@property float frame_y2;
@property float scale;

//util functions
+ (BOOL)isLineSegment:(Line *)line withinRadius:(CGFloat)radius fromPoint:(CGPoint)point;
+ (BOOL) isDoor:(Door *)door withinRadius:(CGFloat)radius fromPoint:(CGPoint)point;

@end
