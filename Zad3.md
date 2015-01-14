# Szymon Rękawek

# 01.14.15r. Poprawione opisy, kod z funkcji map reduce został rozbity na kilka funkcji by był bardziej czytelny, dodany diagramik.
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

### 3.A
Przygotować funkcje map i reduce, które:
wyszukają wszystkie anagramy w pliku [word_list.txt](http://wbzyl.inf.ug.edu.pl/nosql/doc/data/word_list.txt)


Wczytanie pliku ze słowami:
```
mongoimport -c words --type csv --file word_list.txt -f "word"
```

Map:
```js
map = function()
{
    var splitWords = this.word.split("").sort().join("");
    emit(splitWords, this.word);
};
```

Reduce
```js
reduce = function(key, values) 
{
    result = 
    {
        "anagrams1": values,
        "count": values.length
    };
    return result;
};
```
Finalize
```js
finalize = function(key, values) 
{
    if (values.count >= 1)
        return values;
};
```

MapReduce
```js
db.words.mapReduce(
    map,
    reduce, {
        out: "anagrams1",
        finalize: finalize
 
    }
);
```

Przykładowe anagramy:
```sh
{"_id":"aabdor","value":{"anagramsList":["abroad","aboard"],"count":2}}
{"_id":"aablst","value":{"anagramsList":["basalt","tablas"],"count":2}}
{"_id":"aabmnt","value":{"anagramsList":["bantam","batman"],"count":2}}
{"_id":"aacetv","value":{"anagramsList":["caveat","vacate"],"count":2}}
{"_id":"aacimn","value":{"anagramsList":["caiman","maniac"],"count":2}}
{"_id":"aaclrs","value":{"anagramsList":["rascal","scalar"],"count":2}}
{"_id":"aaclsu","value":{"anagramsList":["casual","causal"],"count":2}}
{"_id":"aadmrs","value":{"anagramsList":["madras","dramas"],"count":2}}
{"_id":"aaffir","value":{"anagramsList":["affair","raffia"],"count":2}}
```


[Plik z anagramami](/things/myAnagramList.json)






### 3.B 
Przygotować funkcje map i reduce, które:
wyszukają najczęściej występujące słowa z Wikipedia data PL.

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
    var alpha = this.revision.text.match(/[a-ząśżźęćńół]+/gi);
    if (alpha) {
        for (var i = 0; i < alpha.length; i++)
            emit(alpha[i], 1)
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
```

MapReduce:
```js
db.test.mapReduce(map, reduce, {out: "wordCount"})
```


**Czas trwania:** za pierwszym razem: **17 godzin**, za drugim razem **14 godzin** - pewnie dlatego, że za pierwszym razem próbowałem komputera używać również do innych celów.


Najczęściej występujące słowa:
```json
{ "_id" : "w", "value" : { "count" : 20292468  } }
{ "_id" : "i", "value" : { "count" : 5780516  } }
{ "_id" : "a", "value" : { "count" : 5407771  } }
```

![alt tag](https://github.com/waveq/nosqlUG/blob/master/screens/chart3b.png)

Jak widać druzgocąco zwycięża słowo 'w', cóż to za niespodziewany zwrot akcji!
