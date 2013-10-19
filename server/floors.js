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
						name:String
					},
			wallInfo:{
						x1:Number,
						y1:Number,
						x2:Number,
						y2:Number
					},
			stairInfo:{
						x:Number,
						y:Number
					},
			labelInfo:{
						x:Number,
						y:Number,
						text:String
					}
		}]
	});
	floor.index({locationID;1, nr:1});
	Floor = mongoose.model("Floor", floor);
}