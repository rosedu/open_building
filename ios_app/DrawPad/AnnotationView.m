//
//  AnnotationView.m
//  DrawPad
//
//  Created by Iulian-Bogdan Vlad on 10/20/13.
//  Copyright (c) 2013 Iulian-Bogdan Vlad. All rights reserved.
//

#import "AnnotationView.h"

@implementation AnnotationView
@synthesize coordinate;
@synthesize title;
@synthesize index;

- (id)initWithCoordinate:(CLLocationCoordinate2D)coordinate2 addressDictionary:(NSDictionary *)addressDictionary
{
    if ((self = [super initWithCoordinate:coordinate2 addressDictionary:addressDictionary]))
    {
        self.coordinate = coordinate;
    }
    return self;
}

@end
