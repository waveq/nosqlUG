var conn = new Mongo();
var db = conn.getDB('mydb');
var collection = 
db.sgetglue.aggregate( 
	{ $match: 
		{ action: "Comment" }
	},
	{ $group: 
		{ _id: { Tytuł : "$title" }, count: {"$sum": 1} } 
	}, 
	{ $sort: 
		{ "count": -1 } 
	}, 
	{ $limit: 10 } ).toArray();

print(JSON.stringify(collection, null, 4));
// najczesciej skomentowane filmy