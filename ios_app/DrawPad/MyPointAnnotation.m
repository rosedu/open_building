//
//  MyPointAnnotation.m
//  DrawPad
//
//  Created by Iulian-Bogdan Vlad on 10/20/13.
//  Copyright (c) 2013 Iulian-Bogdan Vlad. All rights reserved.
//

#import "MyPointAnnotation.h"

@implementation MyPointAnnotation
@synthesize title;
@synthesize coordinate;

- (id)initWithTitle:(NSString *)ttl andCoordinate:(CLLocationCoordinate2D)c2d
{
    if((self = [super init]))
    {
        title = ttl;
        coordinate = c2d;
    }
	return self;
}

@end
