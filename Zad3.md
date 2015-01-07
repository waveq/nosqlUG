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

Uruchamiamy plik z zapisanymi instrukcjami:
```
mongo localhost:27017/test mapreduce.js
```
