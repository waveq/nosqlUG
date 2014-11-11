var conn = new Mongo();
var db = conn.getDB('mydb');
var dataBase = db.blackpoints.find();
var count = 0;

dataBase.forEach(function (record) {
	if (!record.loc) {
		count += 1;

		var newRecord = {
			"_id": record._id,
			"miejscowosc": record.miejscowosc,
			"loc": {
				"type": "Point",
				"coordinates": [record.szerokosc, record.dlugosc]
			}
		}
		db.blackpoints.remove({"_id": record._id});
		db.blackpoints.insert(newRecord);
	}
});

print(count + " records changed");