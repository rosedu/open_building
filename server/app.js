
/**
 * Module dependencies.
 */

var express = require('express');
var routes = require('./routes');
var user = require('./routes/user');
var http = require('http');
var path = require('path');
var mongoose = require('mongoose');
var locations = require('./locations');
var floors = require('./floors')
var qrCode = require('./qr_code');

var app = express();

// all environments
//app.set('port', process.env.PORT || 3000);
app.set('views', path.join(__dirname, 'views'));
app.set('view engine', 'ejs');
app.use(express.favicon());
app.use(express.logger('dev'));
app.use(express.bodyParser());
app.use(express.methodOverride());
app.use(app.router);
app.use(express.static(path.join(__dirname, 'public')));

app.configure(function(){
  app.use('/media', express.static(__dirname + '/media'));
  app.use(express.static(__dirname + '/public'));
});

app.listen(8000);
console.log('Listening on port 9090');

mongoose.connect('mongodb://openbuilding:openbuilding@ds051368.mongolab.com:51368/openbuilding');
var db = mongoose.connection;
db.on('error', console.error.bind(console, 'connection error:'));
db.once('open', function callback () {
   console.log ('datbase connection established');
});

locations.init(mongoose);
locations.routes(app);

floors.init(mongoose);
floors.routes(app);

qrCode.init(mongoose);
qrCode.routes(app);

// development only
if ('development' == app.get('env')) {
  app.use(express.errorHandler());
}

app.get('/', routes.index);
app.get('/users', user.list);

app.options('/posts', function(req, res){
  console.log("writing headers only");
  res.header("Access-Control-Allow-Origin", "*");
  res.end('');
});

// http.createServer(app).listen(app.get('port'), function(){
//   console.log('Express server listening on port ' + app.get('port'));
// });
