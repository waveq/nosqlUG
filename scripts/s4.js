var conn = new Mongo();
var db = conn.getDB('mydb');
var collection = 
db.getglue.aggregate( 
	{ $match: 
		{ action: "Liked" }
	},
	{ $group: 
		{ _id: { Użytkownik : "$userId" }, count: {"$sum": 1} } 
	}, 
	{ $sort: 
		{ "count": -1 } 
	}, 
	{ $limit: 5 } ).toArray();

print(JSON.stringify(collection, null, 4));

// Najczęściej lajkujący użytkownicy.