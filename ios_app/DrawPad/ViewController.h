//
//  ViewController.h
//  DrawPad
//
//  Created by Iulian-Bogdan Vlad on 10/14/13.
//  Copyright (c) 2013 Iulian-Bogdan Vlad. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "View.h"
#import "ZBarSDK.h"
#import "Place.h"

@interface ViewController : UIViewController
<ZBarReaderDelegate, NSURLConnectionDataDelegate, UITextFieldDelegate>
{
    CGPoint lastPoint;
    CGFloat red;
    CGFloat green;
    CGFloat blue;
    CGFloat brush;
    CGFloat opacity;
    
    BOOL mouseSwiped;
}

@property (strong, nonatomic) NSArray *test;

@property (weak ,nonatomic) IBOutlet UIImageView *mainImage;
@property (weak, nonatomic) IBOutlet UIImageView *tempDrawImage;

@property (weak, nonatomic) IBOutlet UIView *editView;

@property (weak, nonatomic) IBOutlet UIView *enterLabelTextView;
@property (weak, nonatomic) IBOutlet UITextField *enterLabelTextTextView;

@property (strong, nonatomic) Place *place;

-(IBAction)pencilPressed:(id)sender;

-(IBAction)resetPressed:(id)sender;
-(IBAction)settingsPressed:(id)sender;
-(IBAction)savePressed:(id)sender;

-(IBAction)wallButtonPressed:(id)sender;
-(IBAction)doorButtonPressed:(id)sender;
-(IBAction)stairsButtonPressed:(id)sender;
-(IBAction)stairsUpButtonPressed:(id)sender;
-(IBAction)stairsDownButtonPressed:(id)sender;
-(IBAction)textButtonPressed:(id)sender;
-(IBAction)backButtonPressed:(id)sender;
-(IBAction)exitDoorButtonPressed:(id)sender;

-(IBAction)commuteEditModeButton:(id)sender;
-(IBAction)qrCodeButtonPressed:(id)sender;

-(IBAction)goUpstairsButtonPressed:(id)sender;
-(IBAction)goDownstairsDownButtonPressed:(id)sender;

@property bool editMode;

@property (weak, nonatomic) IBOutlet View *myView;

@end
