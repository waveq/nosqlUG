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

### MapReduce

Wczytanie pliku ze słowami:
```
mongoimport -c words --type csv --file word_list.txt -f "word"
```

Funkcja map reduce:
```js
db.words.mapReduce(
  function(){emit(Array.sum(this.word.split("").sort()), this.word);},
  function(key, values) {return values.toString()},
  {
    query: {},
    out: "anagramy"
  }
)
```


### Wikipedia

[Plik do pobrania](http://dumps.wikimedia.org/plwiki/latest/plwiki-latest-pages-articles-multistream.xml.bz2)

By skonwertować xml do pliku csv użyłem [XML2CSVGenericConverter](http://sourceforge.net/projects/xml2csvgenericconverter/files/?source=navbar)

```
java -jar XML2CSVGenericConverter_V1.0.0.jar -v -i /nosql/wiki.xml -o /nosql/
```

Czas trwania:
```
34 min
```

Import pliku do bazy:
```
mongoimport -d wikipedia -c wikipedia --type csv --file wiki.csv --headerline --ignoreBlanks
```

Czas trwania
```
118 min
```

Funkcja map:
```js
var map = function() {  
    var id = this.id;
    if (id) { 
        id = id.split(";"); 
        for (var i = id.length - 1; i >= 0; i--) {
            if (id[i])  {    
               emit(id[i], 1
            }
        }
    }
};
```
Funkcja reduce:
```js
var reduce = function( key, values ) {    
    var count = 0;    
    values.forEach(function(v) {            
        count +=v;    
    });
    return count;
}
db.test.mapReduce(map, reduce, {out: "word_count"})
```

Czas trwania
```
17 godzin
```

Najczęściej występujące słowa:
```
{ "_id" : "w", "value" : { "count" : 13324479 } }
{ "_id" : "i", "value" : { "count" : 5701710 } }
{ "_id" : "align", "value" : { "count" : 4910641 } }
```
