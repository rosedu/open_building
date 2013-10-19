var express = require('express');
var mongoose = require('mongoose');
var locations = require('./locations');

var app = express();


app.configure(function(){
  app.use('/media', express.static(__dirname + '/media'));
  app.use(express.static(__dirname + '/public'));
});

app.use(express.bodyParser());

app.listen(8000);
console.log('Listening on port 8000');


mongoose.connect('mongodb://openbuilding:openbuilding@ds051368.mongolab.com:51368/openbuilding');
var db = mongoose.connection;
db.on('error', console.error.bind(console, 'connection error:'));
db.once('open', function callback () {
   console.log ('datbase connection established');
});

locations.init(mongoose);

locations.routes(app);
