//
//  Place.h
//  DrawPad
//
//  Created by Iulian-Bogdan Vlad on 10/20/13.
//  Copyright (c) 2013 Iulian-Bogdan Vlad. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface Place : NSObject

@property (strong, nonatomic) NSString *name;
@property (strong, nonatomic) NSString *idPlace;
@property (strong, nonatomic) NSNumber *latitude;
@property (strong, nonatomic) NSNumber *longitude;
@property (strong, nonatomic) NSArray *floors;

@end
