var app = require('./app.js');
var locations = require('./locations.js');
var Floor = null;
function init(mongoose)
{
	var floor = mongoose.Schema({
		locationID:{type:String, index:true},
		canModify:Boolean,
		nr:{type:Number, index:true},
		items:[{
			objType:String,
			doorInfo:{
						direction:String,
						x:Number,
						y:Number,
						angle:Number
					},
			wallInfo:{
						x1:Number,
						y1:Number,
						x2:Number,
						y2:Number
					},
			stairInfo:{
						x:Number,
						y:Number,
						stairType:String
					},
			labelInfo:{
						x:Number,
						y:Number,
						text:{type:String, index:true}
					}
		}]
	});
	floor.index({locationID:1, nr:1});
	Floor = mongoose.model("Floor", floor);
}

function createFloor(floor)
{
	var f = new Floor({
						locationID:floor.locationID,
						canModify:true,
						nr:floor.nr,
						items:[]
						});

	var len = floor.items.length;
	for (var i=0; (i<len && i <10); i++)
	{
		console.log("i="+i);
		var item = floor.items[i];
		var ot = item.objType;
		if(ot == "door")
		{
			var direction = item.doorInfo.direction;
			var x = item.doorInfo.x;
			var y = item.doorInfo.y;
			var angle = item.doorInfo.angle;
			f.items.push({objType:ot, doorInfo:{direction:direction, x:x, y:y, angle:angle}});
		}
		else if(ot == "wall")
		{
			var x1 = item.wallInfo.x1;
			var y1 = item.wallInfo.y1;
			var x2 = item.wallInfo.x2;
			var y2 = item.wallInfo.y2;
			f.items.push({objType:ot, wallInfo:{x1:x1, y1:y1, x2:x2, y2:y2}});
		}
		else if(ot == "stair")
		{
			var x = item.stairInfo.x;
			var y = item.stairInfo.y;
			var stairType = item.stairInfo.stairType;
			f.items.push({objType:ot, stairInfo:{x:x, y:y, stairType:stairType}});
		}
		else if (ot == "label")
		{
			var x = item.labelInfo.x;
			var y = item.labelInfo.y;
			var text = item.labelInfo.text;
			f.items.push({objType:ot, labelInfo:{x:x, y:y, text:text}});
		}
	}
	f.save(function (err, loc) {
		console.log('save');
	  if (err)	  console.log('save error'+err);
	});
	console.log('added floor'+floor.locationID);
	locations.addFloor(floor.locationID, floor.nr);
}

function modifyFloor(f,floor)
{
	console.log("modify");
	f.nr = floor.nr;
	var len = floor.items.length;
	var l = f.items.length;
	f.items.splice(0,l);
	for (var i=0; i<len; i++)
	{
		var item = floor.items[i];
		if(item != null)
		{
			var ot = item.objType;
			if(ot == "door")
			{
				var direction = item.doorInfo.direction;
				var x = item.doorInfo.x;
				var y = item.doorInfo.y;
				var angle = item.doorInfo.angle;
				f.items.push({objType:ot, doorInfo:{direction:direction, x:x, y:y, angle:angle}});
			}
			else if(ot == "wall")
			{
				var x1 = item.wallInfo.x1;
				var y1 = item.wallInfo.y1;
				var x2 = item.wallInfo.x2;
				var y2 = item.wallInfo.y2;
				f.items.push({objType:ot, wallInfo:{x1:x1, y1:y1, x2:x2, y2:y2}});
			}
			else if(ot == "stair")
			{
				var x = item.stairInfo.x;
				var y = item.stairInfo.y;
				var stairType = item.stairInfo.stairType;
				f.items.push({objType:ot, stairInfo:{x:x, y:y, stairType:stairType}});
			}
			else if (ot == "label")
			{
				var x = item.labelInfo.x;
				var y = item.labelInfo.y;
				var text = item.labelInfo.text;
				f.items.push({objType:ot, labelInfo:{x:x, y:y, text:text}});
			}
		}
	}
	f.save(function (err, loc) {
		console.log('save');
	  if (err) 
	  console.log('save error'+err);
	});
}

function addFloor(floor)
{
	var locationID = floor.locationID;
	var nr = floor.nr;
	Floor.find({locationID:locationID, nr:nr},function (err, f) {
		if(f.length == 0)
		{
			createFloor(floor);
		}
		else
		{
			modifyFloor(f[0],floor);
		}
	});
}



function routes(app)
{
	app.post('/add_floor', function(req, res){
		console.log('add floor /');
  var floor=req.body;
  addFloor(floor);
  res.send(200,{done:"done"});
});
	app.post('/get_floor', function(req, res){
  console.log('get');
  var f = req.body;
  var id = f.locationID;
  var flNr = f.nr;
  Floor.find({locationID:id, nr:flNr},function (err, floors) {
  if (err) 
  {
    console.err(err);
  }
  console.log(floors);
  res.send(200,floors);
});
});

	app.post('/find_locations', function(req, res){
		var text = req.body.text;
		var floorList=[];
		Floor.find({'items.labelInfo.text':new RegExp(text+'+')}, function(err,fl){

			for(var i=0; i<fl.length; i++)
			{
				var list = fl[i].items;
				
				for(var j=0; j<list.length; j++)
				{
					var item = list[j];
					if(item.objType == 'label')
					{
						var labelText = item.labelInfo.text;
						console.log(labelText);
						var matches = labelText.match(text+'+');
						if (matches)
						{
							var l = {locationID:fl[i].locationID, nr:fl[i].nr, text:labelText};
							floorList.push(l);
						}
					}
				}
			}
			res.send(200,floorList);
		});
	});
}
;


exports.init = init;
exports.routes = routes;
