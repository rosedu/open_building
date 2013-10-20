//
//  ViewController.m
//  DrawPad
//
//  Created by Iulian-Bogdan Vlad on 10/14/13.
//  Copyright (c) 2013 Iulian-Bogdan Vlad. All rights reserved.
//

#import "ViewController.h"
#import "Line.h"
#import "Door.h"
#import "Shape.h"
#import "Stairs.h"
#import "Label.h"
#import "YouAreHere.h"

@interface ViewController ()

@property float current_screen_x1;
@property float current_screen_y1;
@property float current_screen_x2;
@property float current_screen_y2;

@property NSURLConnection *floorConnection;
@property (strong, nonatomic) NSMutableData *floorData;

@property (strong, nonatomic) Line *line;

@property (strong, nonatomic) Line *line2;

@property CGPoint lastPoint;

@property float scale;

// 0 = no wall
// 1 = a inceput
// 2 = e in curs de
@property int addingWall;

@property CGPoint wallStart;
@property CGPoint wallEnd;

@property (strong, nonatomic) id<Shape> moveShape;

@end

@implementation ViewController

-(NSMutableData *)floorData
{
    if( _floorData == nil )
    {
        _floorData = [NSMutableData data];
    }
    
    return _floorData;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    
    NSLog(@"%@ %@ %@",self.place.idPlace, self.place.longitude, self.place.latitude);
    
    self.myView.frame_x1 = 0;
    self.myView.frame_y1 = 0;
    self.myView.frame_x2 = self.myView.frame.size.width;
    self.myView.frame_y2 = self.myView.frame.size.height;
	self.myView.scale = 20;
    
    [self.enterLabelTextView setBackgroundColor:[[UIColor clearColor] colorWithAlphaComponent:0.5]];
    
    if( self.place.floors.count == 0 )
    {
        self.editView.frame = CGRectMake(self.editView.frame.origin.x,
                                         self.editView.frame.origin.y,
                                         self.editView.frame.size.width, self.editView.frame.size.height);
        self.myView.editMode = YES;
        self.editMode = YES;
    }
    else
    {
        self.editView.frame = CGRectMake(self.editView.frame.origin.x,
                                         self.editView.frame.origin.y + self.editView.frame.size.height,
                                         self.editView.frame.size.width, self.editView.frame.size.height);
        self.myView.editMode = NO;
        self.editMode = NO;
        
    
    }
    
    [self getFloorNumber:0];
    [self.myView refresh];
    
    UIPinchGestureRecognizer *pinchGesture = [[UIPinchGestureRecognizer alloc] initWithTarget:self action:@selector(pinch:)];
    [self.myView addGestureRecognizer:pinchGesture];
}

-(void)getFloorNumber:(int)floor
{
    NSDictionary *dictionary = [[NSDictionary alloc] initWithObjects:@[self.place.idPlace, @(floor)] forKeys:@[@"locationID",@"nr"]];
    
    NSError *error;
    NSData *postData = [NSJSONSerialization dataWithJSONObject:dictionary
                                                       options:NSJSONWritingPrettyPrinted
                                                         error:&error];
    
    NSString *postLength = [NSString stringWithFormat:@"%d", [postData length]];
    
    NSMutableURLRequest *request = [[NSMutableURLRequest alloc] init];
    [request setURL:[NSURL URLWithString:@"http://wyliodrin.com:8000/get_floor"]];
    [request setHTTPMethod:@"POST"];
    [request setValue:postLength forHTTPHeaderField:@"Content-Length"];
    [request setValue:@"application/json" forHTTPHeaderField:@"Content-Type"];
    [request setHTTPBody:postData];
    
    self.floorConnection = [[NSURLConnection alloc] initWithRequest:request delegate:self];
}

-(void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    
    NSLog(@"INTRA AICI");
    
    [self.navigationController setNavigationBarHidden:YES animated:YES];
}

float lastScale = 0;


-(void)pinch:(UIPinchGestureRecognizer *)pinch
{
    
    if( pinch.state == UIGestureRecognizerStateBegan )
    {
        lastScale = self.myView.scale;
    }
    
    if( pinch.state == UIGestureRecognizerStateChanged)
    {
        self.myView.scale = lastScale * pinch.scale;
    }
        
    [self.myView refresh];
}




#pragma mark Buttons

-(IBAction)wallButtonPressed:(id)sender
{
    Line *line = [[Line alloc] init];
    
    line.scale = self.myView.scale;

    line.x1 = (int) (120 + self.current_screen_x1) / 20;
    line.y1 = (int) (134 + self.current_screen_y1) / 20;
    line.x2 = (int) (241 + self.current_screen_x1) / 20;
    line.y2 = (int) (321 + self.current_screen_y1) / 20;
    
    [self.myView addObject:line];
}

-(IBAction)doorButtonPressed:(id)sender
{
    Door *door = [[Door alloc] initWithDoorType:kInsideDoor];
    door.scale = self.myView.scale;
    door.centerPoint_pixels = CGPointMake(200 + self.current_screen_x1, 200 + self.current_screen_y1);

    [self.myView addObject:door];
}

-(IBAction)exitDoorButtonPressed:(id)sender
{
    Door *door = [[Door alloc] initWithDoorType:kOutsideDoor];
    door.scale = self.myView.scale;
    door.centerPoint_pixels = CGPointMake(200 + self.current_screen_x1, 200 + self.current_screen_y1);
    
    [self.myView addObject:door];
}

-(IBAction)stairsButtonPressed:(id)sender
{
    Stairs *stairs = [[Stairs alloc] initWithType:kStairsBoth];
    stairs.scale = self.myView.scale;
    stairs.centerPoint_pixels = CGPointMake(200 + self.current_screen_x1, 200 + self.current_screen_y1);
    [self.myView addObject:stairs];
}

-(IBAction)stairsUpButtonPressed:(id)sender
{
    Stairs *stairs = [[Stairs alloc] initWithType:kStairsUp];
    stairs.scale = self.myView.scale;
    stairs.centerPoint_pixels = CGPointMake(200 + self.current_screen_x1, 200 + self.current_screen_y1);
    [self.myView addObject:stairs];
}

-(IBAction)stairsDownButtonPressed:(id)sender
{
    Stairs *stairs = [[Stairs alloc] initWithType:kStairsDown];
    stairs.scale = self.myView.scale;
    stairs.centerPoint_pixels = CGPointMake(200 + self.current_screen_x1, 200 + self.current_screen_y1);
    [self.myView addObject:stairs];
}

-(IBAction)commuteEditModeButton:(id)sender
{
    if(self.editMode)
    {
        self.editMode = NO;
        [UIView animateWithDuration:0.5 animations:^()
         {
             self.editView.frame = CGRectMake(self.editView.frame.origin.x,
                                              self.editView.frame.origin.y + self.editView.frame.size.height,
                                              self.editView.frame.size.width, self.editView.frame.size.height);
             self.myView.editMode = NO;
             [self.myView refresh];
         }];
    }
    else
    {
        self.editMode = YES;
        [UIView animateWithDuration:0.5 animations:^()
         {
             self.editView.frame = CGRectMake(self.editView.frame.origin.x,
                                              self.editView.frame.origin.y - self.editView.frame.size.height,
                                              self.editView.frame.size.width, self.editView.frame.size.height);
            self.myView.editMode = YES;
            [self.myView refresh];
         }];
    }
}

-(IBAction)textButtonPressed:(id)sender
{
    [self.view bringSubviewToFront:self.enterLabelTextTextView];
    self.enterLabelTextView.hidden = NO;
}

-(BOOL)textFieldShouldReturn:(UITextField *)textField
{
    Label *label = [[Label alloc] init];
    label.scale = self.myView.scale;
    label.centerPoint_pixels = CGPointMake(200 + self.current_screen_x1, 200 + self.current_screen_y1);
    label.stringLabel = [NSString stringWithFormat:@"%@",textField.text];
    
    NSLog(@"%@",label.stringLabel);
    
    [self.myView addObject:label];
    [self.view sendSubviewToBack:self.enterLabelTextView];
    self.enterLabelTextView.hidden = YES;
    [textField resignFirstResponder];
    
    return NO;
}

-(IBAction)qrCodeButtonPressed:(id)sender
{
    ZBarReaderViewController *reader = [ZBarReaderViewController new];
    reader.readerDelegate = self;
    reader.supportedOrientationsMask = ZBarOrientationMaskAll;
    
    ZBarImageScanner *scanner = reader.scanner;
    // TODO: (optional) additional reader configuration here
    
    // EXAMPLE: disable rarely used I2/5 to improve performance
    [scanner setSymbology: ZBAR_I25
                   config: ZBAR_CFG_ENABLE
                       to: 0];
    
    // present and release the controller
    [self presentModalViewController: reader
                            animated: YES];
}

- (void) imagePickerController: (UIImagePickerController*) reader
 didFinishPickingMediaWithInfo: (NSDictionary*) info
{
    id<NSFastEnumeration> results =
    [info objectForKey: ZBarReaderControllerResults];
    ZBarSymbol *symbol = nil;
    for(symbol in results)
        // EXAMPLE: just grab the first barcode
        break;
    
    // EXAMPLE: do something useful with the barcode data
    NSLog(@"%@", symbol.data);
    NSArray *array = [symbol.data componentsSeparatedByString:@";"];
    NSString *x = [array objectAtIndex:0];
    NSString *y = [array objectAtIndex:1];
    
    [self.myView setYourPosition:CGPointMake(x.intValue, y.intValue)];
    
    [reader dismissModalViewControllerAnimated: YES];
}

#pragma mark touchesMethods

int muta = 0;

-(void)touchesBegan:(NSSet *)touches withEvent:(UIEvent *)event
{
    UITouch *touch = [touches anyObject];
    
    CGPoint new_point = [touch locationInView:self.mainImage];
    
    self.lastPoint = new_point;
    
    CGPoint point = CGPointMake(self.lastPoint.x + self.current_screen_x1, self.lastPoint.y + self.current_screen_y1);
    
    if( self.editMode == YES )
    {
        id<Shape> shape = [self.myView returnShapeAtCoordinates:point];
        self.moveShape = shape;
    }
}

-(void)touchesMoved:(NSSet *)touches withEvent:(UIEvent *)event
{
    UITouch *touch = [touches anyObject];
    
    CGPoint point = [touch locationInView:self.mainImage];
    
    if( self.moveShape == nil )
    {
        //POSIBIL BUG aici
        self.current_screen_x1 -= point.x - self.lastPoint.x ;
    
        self.current_screen_y1 -= point.y - self.lastPoint.y;
    
        self.current_screen_x2 = 320 + self.current_screen_x1;
        self.current_screen_y2 = 568 + self.current_screen_y1;
        
        self.myView.frame_x1 = self.current_screen_x1;
        self.myView.frame_y1 = self.current_screen_y1;
        self.myView.frame_x2 = self.current_screen_x2;
        self.myView.frame_y2 = self.current_screen_y2;
        
        [self.myView refresh];
    }
    else if( touches.count < 2 )
    {
        //an object was moved
        //now we must determine which one and what type it has
        if( [self.moveShape isKindOfClass:[Line class]] )
        {
            //it's a line
            Line *moveLine = self.moveShape;
            
            CGPoint myRefPoint = CGPointMake(point.x + self.current_screen_x1, point.y + self.current_screen_y1);
        
            [self.myView clearTempShapeBuffer];
            CGPoint point1 = CGPointMake(moveLine.x1 * moveLine.scale, moveLine.y1 * moveLine.scale );
            CGPoint point2 = CGPointMake(moveLine.x2 * moveLine.scale, moveLine.y2 * moveLine.scale );
        
            if( muta == 1 )
            {
                Line *tempLine = [[Line alloc] init];
            
            
                tempLine.x1 = (self.current_screen_x1 + point.x) / self.myView.scale;
                tempLine.y1 = (self.current_screen_y1 + point.y) / self.myView.scale;
                tempLine.x2 = moveLine.x2;
                tempLine.y2 = moveLine.y2;
            
                [self.myView addTempShape:tempLine];
                [self.myView refreshTempImage];
            
                [self.myView removeShape:moveLine];
                [self.myView refresh];
            }
            else if( muta == 2 )
            {
                Line *tempLine = [[Line alloc] init];
            
            
                tempLine.x1 = moveLine.x1; //point.x / self.myView.scale;
                tempLine.y1 = moveLine.y1; //point.y / self.myView.scale;
                tempLine.x2 = (point.x + self.current_screen_x1) / self.myView.scale;// self.moveShape.x2;
                tempLine.y2 = (point.y +self.current_screen_y1) / self.myView.scale;// self.moveShape.y2;
            
                [self.myView addTempShape:tempLine];
                [self.myView refreshTempImage];
            
                [self.myView removeShape:self.moveShape];
                [self.myView refresh];
                
            }
            else
            {
                
                
                if( [self distanceFromPoint:point1 andPoint:myRefPoint] < 30)
                {
                    muta = 1;
                }
                else if( [self distanceFromPoint:point2 andPoint:myRefPoint] < 30 )
                {
                    muta = 2;
                
                }
            }
        }
        else if( [self.moveShape isKindOfClass:[Door class]] )
        {
            
            //it's a door
            Door *moveDoor = self.moveShape;
            
            muta = 1;
            [self.myView clearTempShapeBuffer];
            
            DoorType doorType = moveDoor.doorType;
            Door *tempDoor = [[Door alloc] initWithDoorType:doorType];
            tempDoor.scale = self.myView.scale;
            
            
            tempDoor.centerPoint_pixels = CGPointMake(point.x + self.current_screen_x1, point.y + self.current_screen_y1);
            
            
            [self.myView addTempShape:tempDoor];
            [self.myView refreshTempImage];
            
            [self.myView removeShape:moveDoor];
            [self.myView refresh];
        }
        else if( [self.moveShape isKindOfClass:[Stairs class]] )
        {
            //self.moveShape is stairs
            Stairs *stairs = self.moveShape;
            
            muta = 1;
            [self.myView clearTempShapeBuffer];
            
            Stairs *tempStairs = [[Stairs alloc] initWithType:stairs.stairsType];
            tempStairs.scale = self.myView.scale;
            
            
            tempStairs.centerPoint_pixels = CGPointMake(point.x + self.current_screen_x1, point.y + self.current_screen_y1);
            
            
            [self.myView addTempShape:tempStairs];
            [self.myView refreshTempImage];
            
            [self.myView removeShape:stairs];
            [self.myView refresh];
        }
        else if( [self.moveShape isKindOfClass:[Label class]] )
        {
            Label *label = self.moveShape;
            
            muta = 1;
            [self.myView clearTempShapeBuffer];
            
            Label *tempLabel = [[Label alloc] init];
            tempLabel.scale = self.myView.scale;
            tempLabel.stringLabel = label.stringLabel;
            
            tempLabel.centerPoint_pixels = CGPointMake(point.x + self.current_screen_x1, point.y + self.current_screen_y1);
            
            
            [self.myView addTempShape:tempLabel];
            [self.myView refreshTempImage];
            
            [self.myView removeShape:label];
            [self.myView refresh];
        }
        
        
    }
    
    self.lastPoint = point;
}

-(void)touchesEnded:(NSSet *)touches withEvent:(UIEvent *)event
{
    if( muta == 1 || muta == 2 )
    {
        muta = 0;
        [self.myView addTempShapesToBitmap];
        [self.myView clearTempShapeBuffer];
        [self.myView refresh];
        [self.myView refreshTempImage];
        self.moveShape = nil;
    }
}

-(float)distanceFromPoint:(CGPoint)point andPoint:(CGPoint)point2
{
    double dx = (point2.x-point.x);
    double dy = (point2.y-point.y);
    double dist = sqrt(dx*dx + dy*dy);
    
    return dist;
}

-(NSData *)buildJSON
{
    if( self.myView.shapes.count == 0 ) return nil;
    
    NSDictionary *itemParams;
    
    NSMutableArray *items = [[NSMutableArray alloc] init];
    
    for (id<Shape> shape in self.myView.shapes)
    {
        if( [shape isKindOfClass:[Line class]] )
        {
            Line *lineCast = shape;
            NSDictionary *wallInfo = [[NSDictionary alloc] initWithObjects:@[@(lineCast.x1),@(lineCast.y1),@(lineCast.x2),@(lineCast.y2)] forKeys:@[@"x1",@"y1",@"x2",@"y2"]];
            itemParams = [[NSDictionary alloc] initWithObjects:@[@"wall",wallInfo] forKeys:@[@"objType",@"wallInfo"]];
        }
        else if( [shape isKindOfClass:[Door class]] )
        {
            Door *door = shape;
            NSString *direction;
            
            if( door.doorType == kOutsideDoor )
            {
                direction = @"outside";
            }
            else direction = @"inside";
            
            NSDictionary *wallInfo = [[NSDictionary alloc] initWithObjects:@[@(door.centerPoint.x),@(door.centerPoint.y),direction,@(0)] forKeys:@[@"x",@"y",@"direction",@"angle"]];
            itemParams = [[NSDictionary alloc] initWithObjects:@[@"door",wallInfo] forKeys:@[@"objType",@"doorInfo"]];
        }
        else if( [shape isKindOfClass:[Stairs class]] )
        {
            Stairs *stairs = shape;
            
            NSString *type;
            
            if( stairs.stairsType == kStairsBoth ) type = @"both";
                else if( stairs.stairsType == kStairsDown ) type = @"down";
                    else if( stairs.stairsType == kStairsUp ) type = @"up";
            
            
            NSDictionary *wallInfo = [[NSDictionary alloc] initWithObjects:@[@(stairs.centerPoint.x),@(stairs.centerPoint.y),type] forKeys:@[@"x",@"y",@"stairType"]];
            itemParams = [[NSDictionary alloc] initWithObjects:@[@"stair",wallInfo] forKeys:@[@"objType",@"stairInfo"]];
        }
        else if( [shape isKindOfClass:[Label class]] )
        {
            Label *label = shape;
            
            NSLog(@"%f %f %f %@",label.scale,label.centerPoint.x, label.centerPoint.y, label.stringLabel);
            
            NSDictionary *wallInfo = [[NSDictionary alloc] initWithObjects:@[@(label.centerPoint.x),@(label.centerPoint.y),label.stringLabel] forKeys:@[@"x",@"y",@"text"]];
            itemParams = [[NSDictionary alloc] initWithObjects:@[@"label",wallInfo] forKeys:@[@"objType",@"labelInfo"]];
        }
        
        if( itemParams != nil )
        [items addObject:itemParams];
    }
    
    if( items != nil )
    {
        NSDictionary *dictionary = [[NSDictionary alloc] initWithObjects:@[self.place.idPlace,@(self.myView.currentLevel),items] forKeys:@[@"locationID",@"nr",@"items",]];
    
        NSError *error;
        NSData *postData = [NSJSONSerialization dataWithJSONObject:dictionary
                                                       options:NSJSONWritingPrettyPrinted
                                                                            error:&error];
        return postData;
    }
    else return nil;
    

}

-(IBAction)backButtonPressed:(id)sender
{
    NSData *postData = [self buildJSON];
    
    NSLog(@"JSON %@",[[NSString alloc] initWithData:postData encoding:NSUTF8StringEncoding]);
    
    if( postData)
    {
        NSString *postLength = [NSString stringWithFormat:@"%d", [postData length]];
    
        NSMutableURLRequest *request = [[NSMutableURLRequest alloc] init];
        [request setURL:[NSURL URLWithString:@"http://wyliodrin.com:8000/add_floor"]];
        [request setHTTPMethod:@"POST"];
        [request setValue:postLength forHTTPHeaderField:@"Content-Length"];
        [request setValue:@"application/json" forHTTPHeaderField:@"Content-Type"];
        [request setHTTPBody:postData];
        [request setTimeoutInterval:5];
    
        [[NSURLConnection alloc] initWithRequest:request delegate:self];
    }
    else
    {
        [self.navigationController popViewControllerAnimated:YES];
    }
}

-(void)connection:(NSURLConnection *)connection didReceiveData:(NSData *)data
{
    NSLog(@"%@",[[NSString alloc] initWithData:data encoding:NSUTF8StringEncoding]);
    if( self.floorConnection == connection )
    {
        [self.floorData appendData:data];
    }
}

-(void)connectionDidFinishLoading:(NSURLConnection *)connection
{
    if( self.floorConnection == connection )
    {
        NSError *jsonParsingError = nil;
        NSDictionary *params = [NSJSONSerialization JSONObjectWithData:self.floorData
                                                                  options:0 error:&jsonParsingError];
        
        NSLog(@"FLOOR DATA %@", [[NSString alloc] initWithData:self.floorData encoding:NSUTF8StringEncoding]);
        
        if( ((NSArray *)[params valueForKey:@"items"]).count == 0) return;
        
        id items = [[params valueForKey:@"items"] objectAtIndex:0];
        
        for(id item in items)
        {
            if( [[item valueForKey:@"objType"] isEqualToString:@"wall"] )
            {
                Line *line = [[Line alloc] init];
                
                NSDictionary *dict = [item valueForKey:@"wallInfo"];
                line.x1 = ((NSNumber *)[dict valueForKey:@"x1"]).integerValue;
                line.y1 = ((NSNumber *)[dict valueForKey:@"y1"]).integerValue;
                line.x2 = ((NSNumber *)[dict valueForKey:@"x2"]).integerValue;
                line.y2 = ((NSNumber *)[dict valueForKey:@"y2"]).integerValue;
                
                [self.myView.shapes addObject:line];
            }
            else if ( [[item valueForKey:@"objType"] isEqualToString:@"door"] )
            {
                Door *door = [[Door alloc] init];
                
                NSDictionary *dict = [item valueForKey:@"doorInfo"];
                
                door.centerPoint = CGPointMake( ((NSNumber *)[dict valueForKey:@"x"]).integerValue, ((NSNumber *)[dict valueForKey:@"y"]).integerValue);
                NSLog(@"%@",[dict valueForKey:@"direction"]);
                if( [[dict valueForKey:@"direction"] isEqualToString:@"inside"] )
                {
                    door.doorType = kInsideDoor;
                }
                else
                {
                    door.doorType = kOutsideDoor;
                }
                
                [self.myView.shapes addObject:door];
            }
            else if( [[item valueForKey:@"objType"] isEqualToString:@"stair"] )
            {
                Stairs *stairs = [[Stairs alloc] init];
                
                NSDictionary *dict = [item valueForKey:@"stairInfo"];
                
                stairs.centerPoint = CGPointMake( ((NSNumber *)[dict valueForKey:@"x"]).integerValue, ((NSNumber *)[dict valueForKey:@"y"]).integerValue);
                if( [[dict valueForKey:@"stairType"] isEqualToString:@"up"] )
                {
                    stairs.stairsType = kStairsUp;
                }
                else if( [[dict valueForKey:@"stairType"] isEqualToString:@"down"] )
                {
                    stairs.stairsType = kStairsDown;
                }
                else
                {
                    stairs.stairsType = kStairsBoth;
                }
                
                [self.myView.shapes addObject:stairs];
            }
            else if( [[item valueForKey:@"objType"] isEqualToString:@"label"] )
            {
                Label *label = [[Label alloc] init];
                
                NSDictionary *dict = [item valueForKey:@"labelInfo"];
                
                label.centerPoint = CGPointMake( ((NSNumber *)[dict valueForKey:@"x"]).integerValue, ((NSNumber *)[dict valueForKey:@"y"]).integerValue);
                label.stringLabel = [dict valueForKey:@"text"];
                
                [self.myView.shapes addObject:label];
            }
        }
        
        [self.myView refresh];
    }
    else
    {
        [self.navigationController popViewControllerAnimated:YES];
    }
}

-(IBAction)goUpstairsButtonPressed:(id)sender
{
    [self.myView.shapes removeAllObjects];
    [self.myView refresh];
}

-(IBAction)goDownstairsDownButtonPressed:(id)sender
{
    [self.myView.shapes removeAllObjects];
    [self.myView refresh];
}

@end
