//
//  MyPointAnnotation.h
//  DrawPad
//
//  Created by Iulian-Bogdan Vlad on 10/20/13.
//  Copyright (c) 2013 Iulian-Bogdan Vlad. All rights reserved.
//

#import <MapKit/MapKit.h>
#import <MapKit/MapKit.h>

@interface MyPointAnnotation : NSObject<MKAnnotation>

@property int index;

- (id)initWithTitle:(NSString *)ttl andCoordinate:(CLLocationCoordinate2D)c2d;

@end
