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
### Zadanie 1B:
**Zliczenie zaimportowanych rekordów**
```
> db.train.count()
6034195
```
