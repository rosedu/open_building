//
//  AnnotationView.h
//  DrawPad
//
//  Created by Iulian-Bogdan Vlad on 10/20/13.
//  Copyright (c) 2013 Iulian-Bogdan Vlad. All rights reserved.
//

#import <MapKit/MapKit.h>

@interface AnnotationView : MKPlacemark

@property (nonatomic, readwrite, assign) CLLocationCoordinate2D coordinate;

@property (nonatomic, strong) NSString *title;
@property int index;

@end
