# Szymon Rękawek

# 20.01.15r.
- Poprawione opisy
- Kod z funkcji map reduce został rozbity na kilka funkcji by był bardziej czytelny.
- Dodane diagramy 
- Dodane najczęściej występujące słowa w tytułach na wikipedii(3.B.1)
- Dodana funkcja map reduce na mojej kolekcji z czarnymi punktami na polskich drogach(3.B.3)

----

### Maszyna:
```
Windows 8.1 x64
Intel Core i3-2350M 
8 GB RAM DDR3
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

Czas trwania:
```
real    0m0.944s
```

Najczęściej występujące słowa w tytułach artykułów:

**Map**
```js
var map = function()
{
    var splitWords = this.word.split("").sort().join("");
    emit(splitWords, this.word);
};
```

**Reduce**
```js
var reduce = function(key, values) 
{
    result = 
    {
        "anagrams1": values,
        "count": values.length
    };
    return result;
};
```

**Finalize**
```js
var finalize = function(key, values) 
{
    if (values.count >= 1)
        return values;
};
```

**MapReduce**
```js
db.words.mapReduce(
    map,
    reduce, {
        out: "anagrams1",
        finalize: finalize
 
    }
);
```

**Przykładowe anagramy**
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

----

### 3.B 
Przygotować funkcje map i reduce, które:
wyszukają najczęściej występujące słowa z Wikipedia data PL.

[Plik do pobrania](http://dumps.wikimedia.org/plwiki/latest/plwiki-latest-pages-articles-multistream.xml.bz2)

By zaimportować plik do bazy wykorzystałem  [skrypt.php](/scripts/3B.php) 


Czas trwania:
```
real    92m23.729s
```


### 3.B.1 Najczęściej występujące słowa w tytułach artykułów

**Map**
```js
var map = function() {
  var alpha = this.title.match(/[a-ząśżźęćńół]+/gi);
  if (alpha) {
    for (alpha i = 0; i < alpha.length; i++)
      emit(alpha[i], 1)
  }
};
```

**Reduce**
```js
var reduce = function(key, values) {
  var count = 0;
  values.forEach(function(v) {
    count += v;
  });
  return count;
};
```

**MapReduce**
```js
db.pages.mapReduce(map, reduce, {out: "titleCount"});
```
Czas trwania:
```
real    32m41.581s
```

**Najczęstrze słowa w tytułach artykułów**
```JSON
{ "_id": "Kategoria", "value": 140494 } 
{ "_id": "w", "value": 118729 } 
{ "_id": "Szablon", "value": 49402 } 
{ "_id": "Wikipedia", "value": 41634 } 
{ "_id": "na", "value": 35152 } 
{ "_id": "powiat", "value": 21085 } 
{ "_id": "województwo", "value": 18952 } 
{ "_id": "i", "value": 18419} 
{ "_id": "Gmina", "value": 15672} 
{ "_id": "Poczekalnia", "value": 14943}
```

![alt tag](https://github.com/waveq/nosqlUG/blob/master/screens/chart3b1.png)

----

### 3.B.2 Najczęściej występujące słowa w tekście

**Map**
```js
var map = function() {
    var alpha = this.revision.text.match(/[a-ząśżźęćńół]+/gi);
    if (alpha) {
        for (var i = 0; i < alpha.length; i++)
            emit(alpha[i], 1)
    }
};
```

**Reduce**
```js
var reduce = function(key, values) {    
    var count = 0;    
    values.forEach(function(v) {            
        count +=v;    
    });
    return count;
}
```

**MapReduce**
```js
db.pages.mapReduce(map, reduce, {out: "wordCount"});
```

Czas trwania:
```
Około 17 godzin.
```


**Najczęściej występujące słowa w tekście**
```json
{ "_id" : "w", "value" : 13324479 }
{ "_id" : "i", "value" : 5701710 }
{ "_id" : "align", "value" : 4910641 }
{ "_id" : "na", "value" : 4495496 }
{ "_id" : "z", "value" : 4420915 }
{ "_id" : "ref", "value" : 4264256 }
{ "_id" : "data", "value" : 3495165 }
{ "_id" : "Kategoria", "value" : 3169267 }
{ "_id" : "do", "value" : 2824854 }
{ "_id" : "center", "value" : 2719355 }
{ "_id" : "się", "value" : 2572812 }
{ "_id" : "http", "value" : 2324495 }
{ "_id" : "br", "value" : 2221883 }
{ "_id" : "W", "value" : 2092043 }
{ "_id" : "www", "value" : 2053473 }
{ "_id" : "left", "value" : 2038389 }
{ "_id" : "tytuł", "value" : 1668414 }
{ "_id" : "a", "value" : 1552049 }
{ "_id" : "roku", "value" : 1512300 }
{ "_id" : "small", "value" : 1466686 }
```

![alt tag](https://github.com/waveq/nosqlUG/blob/master/screens/chart3b2.png)

Jak widać druzgocąco zwycięża słowo 'w', cóż to za niespodziewany zwrot akcji!

----

### 3.B.3 W miejscowościach na jaką literkę najczęściej występują [czarne punkty drogowe](/things/blackpoints.json)


**Map**
```js
var map = function() {
	var first = this.miejscowosc.charAt(0);
    emit(first, 1);
}
```

**Reduce**
```js
var reduce = function(key, values) {
  var count = 0;
  values.forEach(function(v) {
    count += v;
  });
  return count;
};
```

**MapReduce**
```js
db.blackpoints.mapReduce(map, reduce, {out: "cityCount"});
```

```JSON
{ "_id" : "S", "value" : 14 }
{ "_id" : "P", "value" : 7 }
{ "_id" : "G", "value" : 6 }
{ "_id" : "M", "value" : 6 }
{ "_id" : "K", "value" : 5 }
{ "_id" : "C", "value" : 4 }
{ "_id" : "D", "value" : 4 }
{ "_id" : "N", "value" : 4 }
{ "_id" : "O", "value" : 4 }
{ "_id" : "T", "value" : 4 }
{ "_id" : "W", "value" : 4 }
{ "_id" : "Z", "value" : 4 }
{ "_id" : "L", "value" : 3 }
{ "_id" : "R", "value" : 3 }
{ "_id" : "Ł", "value" : 3 }
{ "_id" : "B", "value" : 2 }
{ "_id" : "E", "value" : 2 }
{ "_id" : "J", "value" : 2 }
{ "_id" : "A", "value" : 1 }
```

![alt tag](https://github.com/waveq/nosqlUG/blob/master/screens/chart3b3.png)


Radzę omijać miejscowości na literkę S.
