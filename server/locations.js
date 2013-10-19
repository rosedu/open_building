var Location = null;
function init(mongoose)
{
	var loc = mongoose.Schema({
								name:String,
								latitude:Number,
								longitude:Number,
								floors:[Number]
							},{autoIndex:true}, {id:true});
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
  console.log(locations);
  res.send(200,locations);
});
});

app.post('/add', function(req, res){
  console.log('add loc');
  l=req.body;
  addLocation(l.name,l.latitude,l.longitude);
});
}

function addLocation(name,latitude,longitude)
{
	var loc = new Location({name:name,latitude:latitude,longitude:longitude});
	loc.save(function (err, loc) {
  if (err) // TODO handle the error
  console.log('save error'+loc);
});
}
exports.init = init;
exports.routes = routes;