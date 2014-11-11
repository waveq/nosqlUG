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
**Polega na zaimportowaniu, do systemów baz danych uruchomionych na swoim komputerze, danych z pliku Train.csv bazy MongoDB.**

Format pliku csv do znośnej przez mongo formy:
```
cat Train.csv | tr "\n" " " | tr "\r" "\n" | head -n 6034196 > correct_train.csv
```


Import poprawionego pliku csv:
```
mongoimport -d mydb -c train -type csv -file correct_train.csv --headerline
```

Czas trwania:
```
28m
```

![alt tag](https://raw.githubusercontent.com/waveq/nosqlUG/master/screens/1A%20hdd.jpg?token=ABKxe1yjmn0aRp5LoKKymlg6uOE3tj45ks5Ua5NSwA%3D%3D)

![alt tag](https://raw.githubusercontent.com/waveq/nosqlUG/master/screens/1A%20cpu.jpg?token=ABKxe5-FInt7NepRp0BGUl39HguS5Qi2ks5Ua5NzwA%3D%3D)

![alt tag](https://raw.githubusercontent.com/waveq/nosqlUG/master/screens/1A%20RAM.jpg?token=ABKxexaoSMMzNxqZvHhHb9luHNh51Rgjks5Ua5OFwA%3D%3D)

### Zadanie 1B:
**Zliczenie zaimportowanych rekordów**
```
> db.train.count()
6034195
```
### Zadanie 1C:
Zamienić string zawierający tagi na tablicę napisów z tagami następnie zliczyć wszystkie tagi i wszystkie różne tagi. Napisać program, który to zrobi korzystając z jednego ze sterowników.

[1C.java](/scripts/1C.java)

Pamięć zapełniała się stopniowo by w końcu dojść do następującego poziomu "zapełnienia": 

![alt tag](https://raw.githubusercontent.com/waveq/nosqlUG/master/screens/1C%20ram.jpg?token=ABKxezPikvQ1Ndr05JYxayHCmlDlQrjeks5Ua5U9wA%3D%3D)
```
Wykonanie programu trwało: 58 min.

Przetworzono: 6032934/6034195 rekordów.
Wszystkie tagi: 42060.
```

### Zadanie 1D:
Wyszukać w sieci dane zawierające obiekty GeoJSON. Następnie dane zapisać w bazie MongoDB.

JSONy pobrałem ze strony: http://www.poipoint.pl/ jest to [lista czarnych punktów na drogach](http://www.poipoint.pl/poi_loading.php?poi=Czarne%20punkty&id=garmin) 

Import pliku csv:
```
mongoimport -d mydb -c czarnepunkty --type csv --file Czarne.csv --headerline
```

Czas trwania
```
0.499s
```

Przykładowy rekord wygląda następująco:
```
{
        "_id" : ObjectId("5462783c587be495784093ea"),
        "#szerokosc" : 16.785888,
        "#dlugosc" : 50.958284,
        "#typ" : "Czarne punkty",
        "#miejscowosc" : "Mirosławice"
}
```
Za pomocą [skryptu](/scripts/correctBlackPoints.js) zmieniłem je do formatu:
```
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
```
 > var obszar = {
...    "type": "Polygon",
...    "coordinates": [[
...        [18.64, 54.34],
...        [20.47, 53.78],
...        [18.00, 53.11],
...        [18.64, 54.34]
...    ]]
...  }
> db.blackpoints.find({loc: {$geoWithin: {$geometry: obszar}}}).toArray()
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
```
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


