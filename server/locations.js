var app = require('./app.js');
var uuid = require('node-uuid');
var Location = null;
function init(mongoose)
{
	var loc = mongoose.Schema({
								name:String,
                id:{type:String, index:true},
								latitude:String,
								longitude:String,
								floors:[Number]
							},{autoIndex:true});
	Location = mongoose.model('Location',loc);
}

function routes(app)
{
	app.post('/get_locations', function(req, res){
  console.log('get');
  Location.find(function (err, locations) {
  if (err) 
  {
    console.err(err);
  }
  res.send(200,locations);
});
});

app.post('/add_location', function(req, res){
  console.log('add loc');
  l=req.body;
  console.log("body = "+l);
  var id  = addLocation(l.name,l.latitude,l.longitude, function (id)
    {
      console.log(l.name);
      console.log("lat = "+l.latitude);
      console.log("long = "+l.longitude);
      var idJ = {id:id};
      console.log (idJ);
      res.send(200,idJ);
    });
  
});
}



function addLocation(name,latitude,longitude, done)
{
  var id = uuid.v1();
	var loc = new Location({name:name,id:id,latitude:latitude,longitude:longitude,floors:[]});
	loc.save(function (err, loc) {
  if (err)   console.log('save error'+err);
  done (id);
});
}

function addFloor(id,nr)
{
  console.log("id = "+id);
  console.log('add floor');
  Location.find({id:id}, function(err, locations)
  {
      console.log("locations[0]="+locations[0]);
      var loc = locations[0];
      console.log("loc[0]="+loc);
      loc.floors.push(nr);
      loc.save(function (err, loc) {
      if (err)       console.log('save error'+err);
    });
  });
}

function modifyLocation(location,name)
{
    location[1].name = name;
    location.save(function(err) {
    if (err) throw err;
  });
}
exports.init = init;
exports.routes = routes;
exports.addFloor = addFloor;
