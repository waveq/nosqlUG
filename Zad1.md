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
MongoDB shell version: 2.6.5
```
### Zadanie 1A:
**Polega na zaimportowaniu, do systemów baz danych uruchomionych na swoim komputerze, danych z pliku Train.csv bazy MongoDB i PostgreSQL**

**Mongo**

Format pliku csv do znośnej przez mongo formy:
```
cat Train.csv | tr "\n" " " | tr "\r" "\n" | head -n 6034196 > correct_train.csv
```


Import poprawionego pliku csv:
```
mongoimport -d mydb -c train -type csv -file correct_train.csv --headerline
```

![alt tag](https://raw.githubusercontent.com/waveq/nosqlUG/master/screens/1A%20hdd.jpg?token=ABKxe2E9CbYd4PW1a8pxbRiRPK00aNaVks5UdNC4wA%3D%3D)

![alt tag](https://raw.githubusercontent.com/waveq/nosqlUG/master/screens/1A%20cpu.jpg?token=ABKxe4zbr0YkQStkTZOed7iDhhxHVbuvks5UdNDXwA%3D%3D)

![alt tag](https://raw.githubusercontent.com/waveq/nosqlUG/master/screens/1A%20RAM.jpg?token=ABKxe36ROhz8zaisxt15IV3wLAN9Y2f5ks5UdNDkwA%3D%3D)

**PostgreSQL**

```sql
CREATE TABLE train(
   id TEXT PRIMARY KEY	NOT NULL,
   Title           	TEXT,
   Body            	TEXT,
   Tags        		TEXT
)
```

``` 
copy train(Id,Title,Body,Tags) from 'C:\Train.csv' with delimiter ',' csv header
```

![alt tag](https://raw.githubusercontent.com/waveq/nosqlUG/master/screens/1A%20PG%20HDD.jpg?token=ABKxe0wA3HanH85e7aDdsPG4xPDfyQZ_ks5Uc6BZwA%3D%3D)

![alt tag](https://raw.githubusercontent.com/waveq/nosqlUG/master/screens/1a%20pg%20cpu.jpg?token=ABKxe7TBtw4M-PqUzH2QKApCy_XtjRQFks5Uc6BnwA%3D%3D)

![alt tag](https://raw.githubusercontent.com/waveq/nosqlUG/master/screens/1a%20pg%20ram.jpg?token=ABKxe4Faj3iNL0yF6mxqWPimiQ50YX73ks5Uc6B1wA%3D%3D)

--
### Czas trwania importów:

|   Mongo  | PostgreSQL |
|:--------:|:----------:|
| 28 minut |  35 minut  |

---

### Zadanie 1B:
**Zliczenie zaimportowanych rekordów**
```
> db.train.count()
6034195
```

Czas trwania:
```
0.224s
```
---

### Zadanie 1C:
Zamienić string zawierający tagi na tablicę napisów z tagami następnie zliczyć wszystkie tagi i wszystkie różne tagi. Napisać program, który to zrobi korzystając z jednego ze sterowników.

[1C.java](/scripts/1C.java)

Pamięć zapełniała się stopniowo by w końcu dojść do następującego poziomu "zapełnienia": 

![alt tag](https://raw.githubusercontent.com/waveq/nosqlUG/master/screens/1C%20ram.jpg?token=ABKxe9l7pBqISk3Hd4MrmjAhlGeHeWqjks5UdNEdwA%3D%3D)

```
Przetwarzanie trwało: 58 min.
Przetworzono: 6032934/6034195 rekordów.
Wszystkie różne tagi: 42060.
Wszystkie tagi: 17408685.
```

---

### Zadanie 1D:
Wyszukać w sieci dane zawierające obiekty GeoJSON. Następnie dane zapisać w bazie MongoDB.

JSONy pobrałem ze strony: http://www.poipoint.pl/ jest to [lista czarnych punktów na drogach](http://www.poipoint.pl/poi_loading.php?poi=Czarne%20punkty&id=garmin) 

Import pliku csv:
```
mongoimport -d mydb -c blackpoints --type csv --file Czarne.csv --headerline
```

Czas trwania:
```
0.499s
```

Przykładowy rekord wygląda następująco:
```js
{
        "_id" : ObjectId("5462783c587be495784093ea"),
        "#szerokosc" : 16.785888,
        "#dlugosc" : 50.958284,
        "#typ" : "Czarne punkty",
        "#miejscowosc" : "Mirosławice"
}
```
Za pomocą [skryptu](/scripts/correctBlackPoints.js) zmieniłem je do formatu:
```js
{
        "_id" : ObjectId("54627fc9587be49578409447"),
        "miejscowosc" : "Pobiednik Wielki",
        "loc" : {
                "type" : "Point",
                "coordinates" : [
                        20.193543,
                        50.081929
                ]
        }
}
```

**[Polygon](/maps/polygon.geojson)**

Czarne punkty między miejscowościami: Gdańsk, Olsztyn, Bydgoszcz:
```js
var obszar = {
   "type": "Polygon",
   "coordinates": [[
       [18.64, 54.34],
       [20.47, 53.78],
       [18.00, 53.11],
       [18.64, 54.34]
   ]]
}
db.blackpoints.find({loc: {$geoWithin: {$geometry: obszar}}}).toArray()
[
        {
                "_id" : ObjectId("54627fc9587be49578409461"),
                "miejscowosc" : "Łęgowo",
                "loc" : {
                        "type" : "Point",
                        "coordinates" : [
                                18.639603,
                                54.227259
                        ]
                }
        },
        {
                "_id" : ObjectId("54627fc9587be49578409463"),
                "miejscowosc" : "Miłobądz",
                "loc" : {
                        "type" : "Point",
                        "coordinates" : [
                                18.720703,
                                54.137997
                        ]
                }
        }
]
```
**[Near](/maps/near.geojson)**

Czarne punkty w promieniu 50km od Warszawy:
```js
db.blackpoints.find({ loc: {$near: {$geometry: punkt, $maxDistance: 50000} }  }).toArray()
[
        {
                "_id" : ObjectId("54627fc9587be49578409458"),
                "miejscowosc" : "Wólka Radzymińska",
                "loc" : {
                        "type" : "Point",
                        "coordinates" : [
                                21.082645,
                                52.410921
                        ]
                }
        },
        {
                "_id" : ObjectId("54627fc9587be49578409477"),
                "miejscowosc" : "Nowa Wieś",
                "loc" : {
                        "type" : "Point",
                        "coordinates" : [
                                20.551673,
                                52.201294
                        ]
                }
        },
        {
                "_id" : ObjectId("54627fc9587be49578409442"),
                "miejscowosc" : "Nowa Wieś",
                "loc" : {
                        "type" : "Point",
                        "coordinates" : [
                                20.535043,
                                52.202243
                        ]
                }
        },
        {
                "_id" : ObjectId("54627fc9587be4957840943f"),
                "miejscowosc" : "Stara Wieś",
                "loc" : {
                        "type" : "Point",
                        "coordinates" : [
                                21.627526,
                                52.08769
                        ]
                }
        }
]
```

**[LineString](/maps/lineString.geojson)**

Czarne punkty pomiędzy punktem w Łęgowie a Warszawą:
```js
var linia = {
  "type": "LineString",
  "coordinates": [[18.720703, 54.137997], [21.03, 52.23]]
}

db.blackpoints.find({loc: {$geoIntersects: {$geometry: linia}}}).toArray()
[
        {
                "_id" : ObjectId("54627fc9587be49578409463"),
                "miejscowosc" : "Miłobądz",
                "loc" : {
                        "type" : "Point",
                        "coordinates" : [
                                18.720703,
                                54.137997
                        ]
                }
        }
]
```

**[Box](/maps/box.geojson)**

Czarne punkty w prostokącie między Krakowem i Lublinem:
```js
db.blackpoints.find({
	loc: { $geoWithin: {$box: [[ 19.95, 50.05 ], [ 22.55, 51.25 ]] } }
	}).toArray()
[
        {
                "_id" : ObjectId("54627fc9587be49578409447"),
                "miejscowosc" : "Pobiednik Wielki",
                "loc" : {
                        "type" : "Point",
                        "coordinates" : [
                                20.193543,
                                50.081929
                        ]
                }
        },
        {
                "_id" : ObjectId("54627fc9587be49578409448"),
                "miejscowosc" : "Pobiednik Wielki",
                "loc" : {
                        "type" : "Point",
                        "coordinates" : [
                                20.208933,
                                50.083553
                        ]
                }
        },
        {
                "_id" : ObjectId("54627fc9587be49578409441"),
                "miejscowosc" : "Michałowice",
                "loc" : {
                        "type" : "Point",
                        "coordinates" : [
                                19.978632,
                                50.164962
                        ]
                }
        },
        {
                "_id" : ObjectId("54627fc9587be4957840945a"),
                "miejscowosc" : "Lublin",
                "loc" : {
                        "type" : "Point",
                        "coordinates" : [
                                22.45909,
                                51.218987
                        ]
                }
        },
        {
                "_id" : ObjectId("54627fc9587be4957840947b"),
                "miejscowosc" : "Strzeszkowice",
                "loc" : {
                        "type" : "Point",
                        "coordinates" : [
                                22.40408,
                                51.15494
                        ]
                }
        },
        {
                "_id" : ObjectId("54627fc9587be4957840947e"),
                "miejscowosc" : "Jędrzejów",
                "loc" : {
                        "type" : "Point",
                        "coordinates" : [
                                20.29134,
                                50.62867
                        ]
                }
        },
        {
                "_id" : ObjectId("54627fc9587be49578409483"),
                "miejscowosc" : "Barcza",
                "loc" : {
                        "type" : "Point",
                        "coordinates" : [
                                20.70974,
                                50.95562
                        ]
                }
        },
        {
                "_id" : ObjectId("54627fc9587be49578409484"),
                "miejscowosc" : "Gózd",
                "loc" : {
                        "type" : "Point",
                        "coordinates" : [
                                20.7621,
                                50.98183
                        ]
                }
        }
]
```

**[Bardziej złożony polygon](/maps/bigPolygon.geojson)**

Czarne punkty w figurze między Bydgoszczem i Lublinem z wyłączeniem Warszawy: 

```js
db.blackpoints.find({loc: {$geoWithin: {$geometry: obszar}}}).toArray()
[
        {
                "_id" : ObjectId("54627fc9587be49578409472"),
                "miejscowosc" : "Otłoszyn",
                "loc" : {
                        "type" : "Point",
                        "coordinates" : [
                                18.716669,
                                52.913949
                        ]
                }
        },
        {
                "_id" : ObjectId("54627fc9587be4957840945e"),
                "miejscowosc" : "Otłoczyn",
                "loc" : {
                        "type" : "Point",
                        "coordinates" : [
                                18.721196,
                                52.907316
                        ]
                }
        },
        {
                "_id" : ObjectId("54627fc9587be4957840945f"),
                "miejscowosc" : "Skępe",
                "loc" : {
                        "type" : "Point",
                        "coordinates" : [
                                19.345207,
                                52.876311
                        ]
                }
        },
        {
                "_id" : ObjectId("54627fc9587be4957840943f"),
                "miejscowosc" : "Stara Wieś",
                "loc" : {
                        "type" : "Point",
                        "coordinates" : [
                                21.627526,
                                52.08769
                        ]
                }
        },
        {
                "_id" : ObjectId("54627fc9587be49578409456"),
                "miejscowosc" : "Siemiatycze",
                "loc" : {
                        "type" : "Point",
                        "coordinates" : [
                                22.863777,
                                52.42722
                        ]
                }
        },
        {
                "_id" : ObjectId("54627fc9587be49578409455"),
                "miejscowosc" : "Łuszczów",
                "loc" : {
                        "type" : "Point",
                        "coordinates" : [
                                22.734167,
                                51.304728
                        ]
                }
        },
        {
                "_id" : ObjectId("54627fc9587be4957840945a"),
                "miejscowosc" : "Lublin",
                "loc" : {
                        "type" : "Point",
                        "coordinates" : [
                                22.45909,
                                51.218987
                        ]
                }
        },
        {
                "_id" : ObjectId("54627fc9587be49578409480"),
                "miejscowosc" : "Jastków",
                "loc" : {
                        "type" : "Point",
                        "coordinates" : [
                                22.4461,
                                51.29703
                        ]
                }
        },
        {
                "_id" : ObjectId("54627fc9587be49578409481"),
                "miejscowosc" : "Garbów",
                "loc" : {
                        "type" : "Point",
                        "coordinates" : [
                                22.30439,
                                51.3623
                        ]
                }
        },
        {
                "_id" : ObjectId("54627fc9587be49578409482"),
                "miejscowosc" : "Życzyn",
                "loc" : {
                        "type" : "Point",
                        "coordinates" : [
                                22.07312,
                                51.4949
                        ]
                }
        },
        {
                "_id" : ObjectId("54627fc9587be4957840947b"),
                "miejscowosc" : "Strzeszkowice",
                "loc" : {
                        "type" : "Point",
                        "coordinates" : [
                                22.40408,
                                51.15494
                        ]
                }
        }
]
```

**[Near + regex](/maps/nearRegex.geojson)**

Czarne punkty w miejscowościach na literę "M", położone w promieniu 100km od Wrocławia: 

```js
var wroc = {
  "type": "Point", 
  "coordinates": [17.00, 51.11],
}
db.blackpoints.find({ loc: {$near: {$geometry: wroc, $maxDistance: 100000} },
miejscowosc: { $regex: 'M.*'}}).toArray()
[
        {
                "_id" : ObjectId("54627fc9587be4957840944f"),
                "miejscowosc" : "Marcinkowice",
                "loc" : {
                        "type" : "Point",
                        "coordinates" : [
                                17.20947,
                                50.99181
                        ]
                }
        },
        {
                "_id" : ObjectId("54627fc9587be4957840948e"),
                "miejscowosc" : "Marcinkowice",
                "loc" : {
                        "type" : "Point",
                        "coordinates" : [
                                17.233743,
                                50.977669
                        ]
                }
        },
        {
                "_id" : ObjectId("54627fc8587be4957840943e"),
                "miejscowosc" : "Mirosławice",
                "loc" : {
                        "type" : "Point",
                        "coordinates" : [
                                16.785888,
                                50.958284
                        ]
                }
        }
]
```
