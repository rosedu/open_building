//
//  MapViewController.m
//  DrawPad
//
//  Created by Iulian-Bogdan Vlad on 10/19/13.
//  Copyright (c) 2013 Iulian-Bogdan Vlad. All rights reserved.
//

#import "MapViewController.h"
#import "ViewController.h"
#import "Place.h"
#import "myAnnotation.h"

@interface MapViewController ()

@property (strong, nonatomic) NSString *locationName;
@property (strong, nonatomic) NSMutableData *data;
@property (strong, nonatomic) NSURLConnection *addPlaceConnection;

@property (strong, nonatomic) NSNumber *temp_lat;
@property (strong, nonatomic) NSNumber *temp_long;

@end

@implementation MapViewController

bool addBuilding;

-(NSMutableArray *)places
{
    if( _places == nil )
    {
        _places = [[NSMutableArray alloc] init];
    }
    return _places;
}

-(NSMutableData *)data
{
    if( _data == nil )
    {
        _data = [NSMutableData data];
    }
    
    return _data;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    
    UITapGestureRecognizer *tapRecognizer = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(foundTap:)];
    
    tapRecognizer.numberOfTapsRequired = 1;
    
    tapRecognizer.numberOfTouchesRequired = 1;
    
    [self.mapView addGestureRecognizer:tapRecognizer];
    
    [self getBuildings];
}

-(void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    
    [self.locationNameView setBackgroundColor:[[UIColor clearColor] colorWithAlphaComponent:0.5]];
}

-(void)connectionDidFinishLoading:(NSURLConnection *)connection
{
    if( self.addPlaceConnection != connection )
    {
        NSError *jsonParsingError = nil;
        NSArray *publicTimeline = [NSJSONSerialization JSONObjectWithData:self.data
                                                              options:0 error:&jsonParsingError];
        for(id place in publicTimeline)
        {
            Place *newPlace = [[Place alloc] init];
            newPlace.name = [place valueForKey:@"name"];
            newPlace.idPlace = [place valueForKey:@"id"];
            newPlace.latitude = [place valueForKey:@"latitude"];
            newPlace.longitude = [place valueForKey:@"longitude"];
            newPlace.floors = [place valueForKey:@"floors"];
        
            [self.places addObject:newPlace];
        }
        [self putThePlacesOnMap];
    }
    

}

- (void)connection:(NSURLConnection *)connection didReceiveData:(NSData *)data
{
    if( self.addPlaceConnection ==  connection )
    {
        Place *place = [[Place alloc] init];
        place.name = self.locationName;
        place.latitude = self.temp_lat;
        place.longitude = self.temp_long;
        
        NSError *jsonParsingError = nil;
        NSArray *array = [NSJSONSerialization JSONObjectWithData:data
                                        options:0 error:&jsonParsingError];
        
        place.idPlace = [array valueForKey:@"id"];
        NSLog(@"creat nou %@",place.idPlace);
        [self.places addObject:place];
    }
    else
    {
        [self.data appendData:data];
    }
}

-(void)putThePlacesOnMap
{
    for(Place *place in self.places)
    {
        CLLocationCoordinate2D coordinate2;
        coordinate2.latitude = place.latitude.doubleValue;
        coordinate2.longitude = place.longitude.doubleValue;
        MyPointAnnotation *annotation2 = [[MyPointAnnotation alloc] initWithTitle:place.name andCoordinate:coordinate2];
        annotation2.index = [self.places indexOfObject:place];
        [self.mapView addAnnotation:annotation2];
    }
}

-(IBAction)foundTap:(UITapGestureRecognizer *)recognizer
{
    if( addBuilding )
    {
        CGPoint point = [recognizer locationInView:self.mapView];
    
        CLLocationCoordinate2D tapPoint = [self.mapView convertPoint:point toCoordinateFromView:self.view];
    
        MyPointAnnotation *point1 = [[MyPointAnnotation alloc] initWithTitle:self.locationName andCoordinate:CLLocationCoordinate2DMake(tapPoint.latitude,tapPoint.longitude)];
        
        self.temp_lat = @(tapPoint.latitude);
        self.temp_long = @(tapPoint.longitude);
        point1.index = self.places.count;
        
        [self.mapView addAnnotation:point1];
        
        NSDictionary *dictionary = [[NSDictionary alloc] initWithObjects:@[self.locationName, @(tapPoint.latitude),@(tapPoint.longitude)] forKeys:@[@"name",@"latitude",@"longitude"]];

        NSError *error;
        NSData *postData = [NSJSONSerialization dataWithJSONObject:dictionary
                                                           options:NSJSONWritingPrettyPrinted
                                                             error:&error];
        
        NSString *postLength = [NSString stringWithFormat:@"%d", [postData length]];
        
        NSMutableURLRequest *request = [[NSMutableURLRequest alloc] init];
        [request setURL:[NSURL URLWithString:@"http://wyliodrin.com:8000/add_location"]];
        [request setHTTPMethod:@"POST"];
        [request setValue:postLength forHTTPHeaderField:@"Content-Length"];
        [request setValue:@"application/json" forHTTPHeaderField:@"Content-Type"];
        [request setHTTPBody:postData];
        
        self.addPlaceConnection = [[NSURLConnection alloc] initWithRequest:request delegate:self];
        addBuilding = NO;
        self.infoLabel.hidden = YES;
    }
}

-(void)getBuildings
{
    NSString *post = @"";
    NSData *postData = [post dataUsingEncoding:NSASCIIStringEncoding allowLossyConversion:YES];
    
    NSString *postLength = [NSString stringWithFormat:@"%d", [postData length]];
    
    NSMutableURLRequest *request = [[NSMutableURLRequest alloc] init];
    [request setURL:[NSURL URLWithString:@"http://wyliodrin.com:8000/get_locations"]];
    [request setHTTPMethod:@"POST"];
    [request setValue:postLength forHTTPHeaderField:@"Content-Length"];
    //    [request setValue:@"application/x-www-form-urlencoded" forHTTPHeaderField:@"Content-Type"];
    [request setHTTPBody:postData];
    
    [[NSURLConnection alloc] initWithRequest:request delegate:self];
}

bool userWasCentered = false;

-(void)mapView:(MKMapView *)mapView didUpdateUserLocation:(MKUserLocation *)userLocation
{
    if(userWasCentered == false)
    {
        MKCoordinateRegion mapRegion;
        mapRegion.center = mapView.userLocation.coordinate;
        mapRegion.span.latitudeDelta = 2;
        mapRegion.span.longitudeDelta = 2;
    
        [mapView setRegion:mapRegion animated: YES];
        userWasCentered = true;
    }
}

-(void)editScreenForPlace:(Place *)place
{
    UIStoryboard *storybard = [UIStoryboard storyboardWithName:@"Main" bundle:nil];
    ViewController *editVC = [storybard instantiateViewControllerWithIdentifier:@"editMode-vc"];
    
    editVC.place = place;
    editVC.test = self.places;
    [self.navigationController pushViewController:editVC animated:YES];
}


//- (MKAnnotationView *)mapView:(MKMapView *)mapView viewForAnnotation:(id <MKAnnotation>)annotation
//{
//    
//    if(annotation == mapView.userLocation)
//    {
//        return nil;
//    }
//    else
//    {
//        MKAnnotationView *annotationView = [[MKPinAnnotationView alloc] initWithAnnotation:annotation reuseIdentifier:@"loc"];
//        annotationView.canShowCallout = YES;
//        annotationView.rightCalloutAccessoryView = [UIButton buttonWithType:UIButtonTypeDetailDisclosure];
//    
//        return annotationView;
//    }
//}



- (MKAnnotationView *)mapView:(MKMapView *)mapView viewForAnnotation:(id)annotation {
    //7
    if([annotation isKindOfClass:[MKUserLocation class]])
        return nil;
    
    //8
    static NSString *identifier = @"myAnnotation";
    MKPinAnnotationView * annotationView = (MKPinAnnotationView*)[self.mapView dequeueReusableAnnotationViewWithIdentifier:identifier];
    if (!annotationView)
    {
        //9
        annotationView = [[MKPinAnnotationView alloc] initWithAnnotation:annotation reuseIdentifier:identifier];
        annotationView.pinColor = MKPinAnnotationColorPurple;
        annotationView.animatesDrop = YES;
        annotationView.canShowCallout = YES;
    }else {
        annotationView.annotation = annotation;
    }
    annotationView.rightCalloutAccessoryView = [UIButton buttonWithType:UIButtonTypeDetailDisclosure];
    return annotationView;
}

- (void)mapView:(MKMapView *)mapView annotationView:(MKAnnotationView *)view calloutAccessoryControlTapped:(UIControl *)control
{
    MyPointAnnotation *annotation = view.annotation;
    
    Place *place = [self.places objectAtIndex:annotation.index];
    
    [self editScreenForPlace:place];
}


-(IBAction)addButton:(id)sender
{
    addBuilding = YES;
    [self.view bringSubviewToFront:self.locationNameView];
    [self.locationNameTextField becomeFirstResponder];
}

- (BOOL)textFieldShouldReturn:(UITextField *)textField {
    
	[textField resignFirstResponder];
    
    if( textField == self.locationNameTextField )
    {
        [self.view sendSubviewToBack:self.locationNameView];
        if( textField.text.length != 0 )
        {
            self.locationName = self.locationNameTextField.text;
            self.infoLabel.hidden = NO;
        }
        else
        {
            addBuilding = NO;
        }
    }
    
	return YES;
}



@end
