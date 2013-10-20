//
//  View.m
//  DrawPad
//
//  Created by Iulian-Bogdan Vlad on 10/17/13.
//  Copyright (c) 2013 Iulian-Bogdan Vlad. All rights reserved.
//

#import "View.h"
#import "Shape.h"
#import "Door.h"
#import "Stairs.h"
#import "Label.h"
#import "YouAreHere.h"

@interface View()

@property (strong, nonatomic) NSMutableArray *tempShapes;
@property (strong, nonatomic) YouAreHere *youAreHere;

@end

@implementation View

-(NSMutableArray *)tempShapes
{
    if( _tempShapes == nil)
    {
        _tempShapes = [[NSMutableArray alloc] init];
    }
    
    return _tempShapes;
}


-(NSMutableArray *)shapes
{
    if( _shapes == nil )
    {
        _shapes = [[NSMutableArray alloc] init];
    }
    
    return _shapes;
}

-(void)clearTempShapeBuffer
{
    [self.tempShapes removeAllObjects];
    self.tempBitmap = nil;
}

-(void)addTempShape:(Line *)shape
{
    [self.tempShapes addObject:shape];
}

-(void)addTempShapesToBitmap
{
    for(int i=0;i<self.tempShapes.count;i++)
    {
        [self.shapes addObject:[self.tempShapes objectAtIndex:i]];
    }
}

-(void)refreshTempImage
{
    
    CGColorSpaceRef colorSpace=CGColorSpaceCreateDeviceRGB();
    size_t bitsPerComponent=8;
    size_t bytesPerPixel=4;
    size_t bytesPerRow=(self.bounds.size.width*bitsPerComponent*bytesPerPixel+7)/8;
    CGContextRef context=CGBitmapContextCreate(NULL, self.bounds.size.width,self.bounds.size.height, bitsPerComponent,bytesPerRow,colorSpace,kCGImageAlphaPremultipliedLast|kCGBitmapByteOrder32Big);
    
    if( self.youAreHere )
    {
        
    }
    
    if([self.tempShapes count]>0)
    {
        for (int i=0;i<[self.tempShapes count];i++)
        {
            [[self.tempShapes objectAtIndex:i] draw:context inCoordinatesX1:self.frame_x1 y1:self.frame_y1 x2:self.frame_x2 y2:self.frame_y2 scale:self.scale];
        }
    }
    
    CGColorSpaceRelease(colorSpace);
    CGImageRef imageRef=CGBitmapContextCreateImage(context);
    self.tempBitmap=[UIImage imageWithCGImage:imageRef];
    CGImageRelease(imageRef);
    CGContextRelease(context);
    [self setNeedsDisplay];
}

-(void)addObjectToBuffer:(id<Shape>)line
{
    CGColorSpaceRef colorSpace=CGColorSpaceCreateDeviceRGB();
    size_t bitsPerComponent=8;
    size_t bytesPerPixel=4;
    size_t bytesPerRow=(self.bounds.size.width*bitsPerComponent*bytesPerPixel+7)/8;
    CGContextRef context=CGBitmapContextCreate(NULL, self.bounds.size.width,self.bounds.size.height, bitsPerComponent,bytesPerRow,colorSpace,kCGImageAlphaPremultipliedLast|kCGBitmapByteOrder32Big);
    
    [line draw:context inCoordinatesX1:self.frame_x1 y1:self.frame_y1 x2:self.frame_x2 y2:self.frame_y2 scale:self.scale];
    
    
    CGColorSpaceRelease(colorSpace);
    CGImageRef imageRef=CGBitmapContextCreateImage(context);
    self.tempBitmap = [UIImage imageWithCGImage:imageRef];
    CGImageRelease(imageRef);
    CGContextRelease(context);
    [self setNeedsDisplay];
}

-(void)removeShape:(id<Shape>)line
{
    [self.shapes removeObject:line];
}

-(void)addObject:(id<Shape>)shape
{
    shape.scale = self.scale;
    [self.shapes addObject:shape];
    [self updateBitmap];
}

-(void)drawGrid:(CGContextRef)context
{
    float i = 0;
    if( self.editMode )
    {
    
        if( self.frame_x1 > 0 )
        {
            i = (1 - (self.frame_x1  / self.scale - (int)(self.frame_x1 / self.scale))) * self.scale;
        }
        else
        {
            i = ((int)(self.frame_x1 / self.scale) - self.frame_x1 / self.scale) * self.scale;
        }
    
        float y = 0;
    
        if( self.frame_x1 > 0 )
        {
            y = (1 - (self.frame_y1 / self.scale - (int)(self.frame_y1 / self.scale))) * self.scale;
        }
        else
        {
            y = ((int)(self.frame_y1 / self.scale) - self.frame_y1 / self.scale) * self.scale;
        }
    
        for(;i<self.frame.size.width;i+= self.scale)
        {
            CGContextSetLineWidth(context, 0.3);
            CGContextSetStrokeColorWithColor(context, [UIColor blackColor].CGColor);
            CGContextMoveToPoint(context, i , 0 );
            CGContextAddLineToPoint(context, i , self.frame.size.height);
            CGContextStrokePath(context);
        }
    
        for(;y<self.frame.size.height;y+= self.scale)
        {
            CGContextSetLineWidth(context, 0.3);
            CGContextSetStrokeColorWithColor(context, [UIColor blackColor].CGColor);
            CGContextMoveToPoint(context, 0 , y );
            CGContextAddLineToPoint(context, self.frame.size.width , y);
            CGContextStrokePath(context);
        }
    }
}

-(void) updateBitmap
{
    CGColorSpaceRef colorSpace=CGColorSpaceCreateDeviceRGB();
    size_t bitsPerComponent=8;
    size_t bytesPerPixel=4;
    size_t bytesPerRow=(self.bounds.size.width*bitsPerComponent*bytesPerPixel+7)/8;
    CGContextRef context=CGBitmapContextCreate(NULL, self.frame.size.width,self.frame.size.height, bitsPerComponent,bytesPerRow,colorSpace,kCGImageAlphaPremultipliedLast|kCGBitmapByteOrder32Big);
    
    if( self.youAreHere )
    {
        [self.youAreHere draw:context inCoordinatesX1:self.frame_x1 y1:self.frame_y1 x2:self.frame_x2 y2:self.frame_y2 scale:self.scale];
    }
    
    if([self.shapes count]>0)
    {
        for (int i=0;i<[self.shapes count];i++)
        {
            [[self.shapes objectAtIndex:i] draw:context inCoordinatesX1:self.frame_x1 y1:self.frame_y1 x2:self.frame_x2 y2:self.frame_y2 scale:self.scale];
        }
    }
    
    [self drawGrid:context];
    
    CGColorSpaceRelease(colorSpace);
    CGImageRef imageRef=CGBitmapContextCreateImage(context);
    self.bitmap=[UIImage imageWithCGImage:imageRef];
    CGImageRelease(imageRef);
    CGContextRelease(context);
    [self setNeedsDisplay];
}

-(id<Shape>)returnShapeAtCoordinates:(CGPoint)point
{    
    for(id<Shape> shape in self.shapes)
    {
        if([shape isKindOfClass:[Line class]])
        {
            if( [View isLineSegment:shape withinRadius:50 fromPoint:point] )
            {
                Line *line = shape;
                
                if( [self distanceFromPoint:CGPointMake(line.x1 * self.scale,line.y1 * self.scale) andPoint:point] < 30 ||
                   [self distanceFromPoint:CGPointMake(line.x2 * self.scale,line.y2 * self.scale) andPoint:point] < 30)
                {
                                    return shape;
                }


            }
        }
        else if( [shape isKindOfClass:[Door class]] )
        {
            if( [View isDoor:shape withinRadius:50 fromPoint:point] )
            {
                return shape;
            }
        }
        else if( [shape isKindOfClass:[Stairs class]] )
        {
            Stairs *stairs = shape;
            if( [View areStairs:stairs withinRadius:50 fromPoint:point] )
            {
                return shape;
            }
            
        }
        else if( [shape isKindOfClass:[Label class]] )
        {
            Label *label = shape;
            if( [View isLabel:label withinRadius:50 fromPoint:point])
            {
                return shape;
            }
        }
    }
    
    return nil;
}

-(void)setYourPosition:(CGPoint)point
{
    CGColorSpaceRef colorSpace=CGColorSpaceCreateDeviceRGB();
    size_t bitsPerComponent=8;
    size_t bytesPerPixel=4;
    size_t bytesPerRow=(self.bounds.size.width*bitsPerComponent*bytesPerPixel+7)/8;
    CGContextRef context=CGBitmapContextCreate(NULL, self.frame.size.width,self.frame.size.height, bitsPerComponent,bytesPerRow,colorSpace,kCGImageAlphaPremultipliedLast|kCGBitmapByteOrder32Big);
    
    self.youAreHere = [[YouAreHere alloc] init];
    self.youAreHere.scale = self.scale;
    self.youAreHere.centerPoint = CGPointMake(point.x, point.y);
    [self.youAreHere draw:context inCoordinatesX1:self.frame_x1 y1:self.frame_y1 x2:self.frame_x2 y2:self.frame_y2 scale:self.scale];
    
    if([self.shapes count]>0)
    {
        for (int i=0;i<[self.shapes count];i++)
        {
            [[self.shapes objectAtIndex:i] draw:context inCoordinatesX1:self.frame_x1 y1:self.frame_y1 x2:self.frame_x2 y2:self.frame_y2 scale:self.scale];
        }
    }
    
    [self drawGrid:context];
    

    CGColorSpaceRelease(colorSpace);
    CGImageRef imageRef=CGBitmapContextCreateImage(context);
    self.bitmap=[UIImage imageWithCGImage:imageRef];
    CGImageRelease(imageRef);
    CGContextRelease(context);
    [self setNeedsDisplay];
}

-(void)drawYouAreHere:(CGContextRef)context
{
}

-(id<Shape>)returnWallAtCoordinates:(CGPoint)point
{
    for(id<Shape> shape in self.shapes)
    {
        if([shape isKindOfClass:[Line class]])
        {
            if( [View isLineSegment:shape withinRadius:5 fromPoint:point] )
            {
                return shape;
            }
        }
    }
    
    return nil;
}

-(id<Shape>)returnStairsAtCoordinates:(CGPoint)point
{
    for(id<Shape> shape in self.shapes)
    {
        if([shape isKindOfClass:[Stairs class]])
        {
            if( [View isLineSegment:shape withinRadius:5 fromPoint:point] )
            {
                return shape;
            }
        }
    }
    
    return nil;
}

-(float)distanceFromPoint:(CGPoint)point andPoint:(CGPoint)point2
{
    double dx = (point2.x-point.x);
    double dy = (point2.y-point.y);
    double dist = sqrt(dx*dx + dy*dy);
    
    return dist;
}

#pragma mark isShapeWithinRadius

+(BOOL) isLabel:(Label *)label withinRadius:(CGFloat)radius fromPoint:(CGPoint)point
{
    if( distance(point, CGPointMake(label.centerPoint.x * label.scale, label.centerPoint.y * label.scale)) < radius )
    {
        return YES;
    }
    else return NO;
}

+(BOOL) isDoor:(Door *)door withinRadius:(CGFloat)radius fromPoint:(CGPoint)point
{
    if( distance(point, CGPointMake(door.centerPoint.x * door.scale, door.centerPoint.y * door.scale)) < radius )
    {
        return YES;
    }
    else return NO;
}

+(BOOL) areStairs:(Stairs *)stairs withinRadius:(CGFloat)radius fromPoint:(CGPoint)point
{
    if( distance(point, CGPointMake(stairs.centerPoint.x * stairs.scale, stairs.centerPoint.y * stairs.scale)) < radius )
    {
        return YES;
    }
    else return NO;
}


+ (BOOL)isLineSegment:(Line *)line withinRadius:(CGFloat)radius fromPoint:(CGPoint)point
{
    
    CGPoint v = CGPointMake(line.x2 * line.scale - line.x1 * line.scale, line.y2 * line.scale - line.y1 * line.scale);
    CGPoint w = CGPointMake(point.x - line.x1 * line.scale, point.y - line.y1 * line.scale);
    CGFloat c1 = dotProduct(w, v);
    CGFloat c2 = dotProduct(v, v);
    CGFloat d;
    if (c1 <= 0) {
        d = distance(point, CGPointMake(line.x1  * line.scale, line.y1  * line.scale));
    }
    else if (c2 <= c1) {
        d = distance(point, CGPointMake(line.x2 * line.scale, line.y2 * line.scale));
    }
    else {
        
        CGFloat b = c1 / c2;
        CGPoint Pb = CGPointMake(line.x1 * line.scale + b * v.x, line.y1 * line.scale + b * v.y);
        d = distance(point, Pb);
    }
    return d <= radius;
}

CGFloat distance(const CGPoint p1, const CGPoint p2) {
    return sqrt(pow(p2.x - p1.x, 2) + pow(p2.y - p1.y, 2));
}

CGFloat dotProduct(const CGPoint p1, const CGPoint p2) {
    return p1.x * p2.x + p1.y * p2.y;
}

-(CGPoint)normalizeX:(float)x Y:(float)y
{
    int dif_x = (int)x % 20;
    int dif_y = (int)y % 20;
    
    if( dif_x < 10 && dif_y < 10 ) return CGPointMake(x - dif_x, y - dif_y);
    else if( dif_x < 10 && dif_y > 10 ) return CGPointMake(x - dif_x, y + (20 - dif_y));
    else if( dif_x > 10 && dif_y < 10 ) return CGPointMake(x + (20 - dif_x), y - dif_y);
    else return CGPointMake(x - dif_x, y - dif_y);
}

-(void)refresh
{
    for(id<Shape> shape in self.shapes)
    {
        shape.scale = self.scale;
    }
    
    if( self.youAreHere ) self.youAreHere.scale = self.scale;
    
    [self updateBitmap];
}

// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect
{
    CGContextRef context = UIGraphicsGetCurrentContext();
    
    CGContextDrawImage(context, self.bounds, self.bitmap.CGImage);
    CGContextDrawImage(context, self.bounds, self.tempBitmap.CGImage);
    CGContextDrawImage(context, self.bounds, self.gridBitmap.CGImage);
}


@end
