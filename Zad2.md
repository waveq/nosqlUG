# Szymon Rękawek
----
### Maszyna:
```
Windows 8.1 x64
Intel Core i3-2350M 
8 GB RAM
```

### Mongo:
```
$ mongo --version
MongoDB shell version: 2.8.0-rc0
```
Uruchomione z opcją
```
--storageEngine wiredtiger
```
### Zadanie 2:

Import pliku getglue_smaple:
```
mongoimport -d mydb -c getglue < getglue_sample.json
```

Czas trwania:
```
28m
```
![alt tag](https://raw.githubusercontent.com/waveq/nosqlUG/master/screens/2%20HDD.jpg?token=ABKxe8maqj0J08R5YJJ6XKVolkeAyF1yks5UdOmMwA%3D%3D)

![alt tag](https://raw.githubusercontent.com/waveq/nosqlUG/master/screens/2%20CPU.jpg?token=ABKxe4KL4RglMSUG7ksDhTP6mhhDUsAIks5UdOl9wA%3D%3D)

![alt tag](https://raw.githubusercontent.com/waveq/nosqlUG/master/screens/2%20RAM.jpg?token=ABKxe4G6kgZPtmeFr7UMsJDrKJH6yPriks5UdOmWwA%3D%3D)

### Zapytania:

**[Klasa z rozwiązaniem w javie](/scripts/2.java)**

**[Reżyserzy mający imię na literę 'H', którzy mają na koncie najwięcej filmów](/scripts/s1.js)**


### JS
```js
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
    { $limit : 5 } );
```

### JAVA

```java
public static void directorsWithMostMovies(DBCollection coll) {
	DBObject Xmatch = (DBObject) JSON.parse("{ $match: {'director': { $regex: '^H.*'}} }");
	DBObject Xmatch2 = (DBObject) JSON.parse("{ $match: {'modelName': 'movies'} }");
	DBObject Xgroup = (DBObject) JSON.parse("{ $group: {_id: {'Typ programu':'$modelName', 'Reżyser': '$director'}, count: {$sum: 1}}");
	DBObject Xsort = (DBObject) JSON.parse(" { $sort: {count: -1} }");
	DBObject Xlimit = (DBObject) JSON.parse("{ $limit : 10}");
		
	AggregationOutput output = coll.aggregate(Xmatch, Xmatch2, Xgroup, Xsort, Xlimit);
	System.out.println(output.results());
}
```
|     Reżyser    | Typ programu | Ilość wystąpień |
|:--------------:|:------------:|:---------------:|
|  Henry Selick  |    movies    |        70       |
|  Hayo Miyazaki |    movies    |        44       |
|  Harold Ramis  |    movies    |        12       |
|  Herbert Ross  |    movies    |        7        |
| Hamilton Luske |    movies    |        5        |

---

**[Najczęściej występujące filmy na literkę 'M'.](/scripts/s2.js)**

### JS

```js
db.getglue.aggregate(
    { $match: {"title": { $regex: '^M.*'}} },
    { $group: 
        {_id: {Tytuł : "$title"}, 
        count: {$sum: 1}} 
    },
    { $sort: {count: -1} },
    { $limit : 5 } );
```

### JAVA

```java
public static void mostCommonMovieNames(DBCollection coll) {
	DBObject Xmatch = (DBObject) JSON.parse("{ $match: {'title': { $regex: '^M.*'}} }");
	DBObject Xgroup = (DBObject) JSON.parse("{ $group: {_id: {'Tytuł' : '$title'}, count: {$sum: 1}} }");
	DBObject Xsort = (DBObject) JSON.parse("{ $sort: {count: -1} }");
	DBObject Xlimit = (DBObject) JSON.parse("{ $limit : 10}");
		
	AggregationOutput output = coll.aggregate(Xmatch, Xgroup, Xsort, Xlimit);
	System.out.println(output.results());
} 
```
|         Tytuł         |  Wystąpienia |
|:---------------------:|:------------:|
|     Modern Family     |     74294    |
| Marvel's The Avengers |     64356    |
|        Mad Men        |     42611    |
|      MythBusters      |     29089    |
|     Monsters, Inc     |     19805    |

---

**[Filmy, które mają najwięcej komentarzy.](/scripts/s3.js)**

### JS

```js
db.getglue.aggregate( 
	{ $match: 
		{ action: "Comment" }
	},
	{ $group: 
		{ _id: { Tytuł : "$title" }, count: {"$sum": 1} } 
	}, 
	{ $sort: 
		{ "count": -1 } 
	}, 
	{ $limit: 5 } );
```

### Java

```java
public static void mostCommentedMovies(DBCollection coll) {
	DBObject Xmatch = (DBObject) JSON.parse("{ $match: { action: 'Comment' }}");
	DBObject Xgroup = (DBObject) JSON.parse("{ $group: { _id: { Tytuł : '$title' }, 'count': {'$sum': 1} } }");
	DBObject Xsort = (DBObject) JSON.parse("{ $sort: { 'count': -1 } }");
	DBObject Xlimit = (DBObject) JSON.parse("{ $limit: 10 }");
		
	AggregationOutput output = coll.aggregate(Xmatch, Xgroup, Xsort, Xlimit);
	System.out.println(output.results());
	}
```
|                     Tytuł                     | Komentarze |
|:---------------------------------------------:|:----------:|
|              Slumdog Millionaire              |     30     |
|                   True Blood                  |     15     |
| Harry Potter and the Deathly Hallows: Part II |     13     |
|                     Avatar                    |     11     |
|                The Dark Knight                |     11     |

---

**[Najczęściej lajkujący użytkownicy.](/scripts/s4.js)**

### JS

```js
db.sgetglue.aggregate( 
	{ $match: 
		{ action: "Liked" }
	},
	{ $group: 
		{ _id: { Użytkownik : "$userId" }, count: {"$sum": 1} } 
	}, 
	{ $sort: 
		{ "count": -1 } 
	}, 
	{ $limit: 5 } );
```

### JAVA

```java
public static void mostLikingUsers(DBCollection coll) {
	DBObject Xmatch = (DBObject) JSON.parse("{ $match:{ action: 'Liked' }}");
	DBObject Xgroup = (DBObject) JSON.parse("{ $group: { _id: { Użytkownik : '$userId' }, 'count': {'$sum': 1} } }");
	DBObject Xsort = (DBObject) JSON.parse("{ $sort: { 'count': -1 } }");
	DBObject Xlimit = (DBObject) JSON.parse("{ $limit: 10 }");
		
	AggregationOutput output = coll.aggregate(Xmatch, Xgroup, Xsort, Xlimit);
	System.out.println(output.results());
}
```
|     Użytkownik    |  Lajki |
|:-----------------:|:------:|
| jesusvarelaacosta |  13562 |
|    gluemanblues   |  12932 |
|       s3v3ns      |  11520 |
|    johnnym2001    |  11436 |
|      bangwid      |  9237  |


