var app = require('./app.js');

var QR = null;

function init(mongoose)
{
	var code = mongoose.Schema({
					"latitude":{type:String, index:true},
					"longitude":{type:String, index:true},
					"locationID":String,
					"nr":Number
	});
	code.index({latitude:1, longitude:1});
	QR = mongoose.model("QR", code);
}

function  routes(app)
{
	app.post('/qr_get', function(req, res){
		var latitude = req.body.latitude;
		var longitude = req.body.longitude;
		QR.find({latitude:latitude, longitude:longitude}, function(err, qr){
			if(qr.length > 0)
			{
				var r = {locationID:qr[0].locationID, nr: qr[0].nr};
				res.send(200,r);
			}
			else
			{
				res.send(200,{locationID:"0"});
			}
		});
	});
}

exports.init = init;
exports.routes = routes;