var conn = new Mongo();
var db = conn.getDB('mydb');
var collection = 
db.getglue.aggregate( 
 	{ $match: {"director": { $regex: '^H.*'}} },
	{ $match: {"modelName": "movies"} },
    { $group: 
    	{
    		_id: {"Typ programu":"$modelName", "Reżyser": "$director"}, 
    		count: {$sum: 1}
    	}
    },
    { $sort: {count: -1} },
    { $limit : 5}
    ).toArray();

print(JSON.stringify(collection, null, 4));

// agregacja, która zwraca Reżyserów o imieniu na literę H, którzy mają najwięcej movies