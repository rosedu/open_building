//
//  MapViewController.h
//  DrawPad
//
//  Created by Iulian-Bogdan Vlad on 10/19/13.
//  Copyright (c) 2013 Iulian-Bogdan Vlad. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <MapKit/MapKit.h>
#import "MyPointAnnotation.h"

@interface MapViewController : UIViewController
<UITextFieldDelegate,MKMapViewDelegate, NSURLConnectionDataDelegate>

@property (weak, nonatomic) IBOutlet MKMapView *mapView;
@property (weak, nonatomic) IBOutlet UIButton *addButton;
@property (weak, nonatomic) IBOutlet UITextField *searchBox;

@property (weak, nonatomic) IBOutlet UIView *locationNameView;
@property (weak, nonatomic) IBOutlet UITextField *locationNameTextField;

@property (weak, nonatomic) IBOutlet UILabel *infoLabel;

@property (strong, nonatomic) NSMutableArray *places;

-(IBAction)addButton:(id)sender;

@end
