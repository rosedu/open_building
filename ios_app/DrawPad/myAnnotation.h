



#import <Foundation/Foundation.h>
#import <MapKit/MapKit.h>

//3.1
@interface myAnnotation : NSObject
@property (strong, nonatomic) NSString *title;
@property (nonatomic,assign) CLLocationCoordinate2D coordinate;
@property int index;

-(id) initWithCoordinate:(CLLocationCoordinate2D)coordinate title:(NSString *)title;
@end