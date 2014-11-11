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

![alt tag](https://raw.githubusercontent.com/waveq/nosqlUG/master/screens/1A%20hdd.jpg?token=ABKxe1yjmn0aRp5LoKKymlg6uOE3tj45ks5Ua5NSwA%3D%3D)

![alt tag](https://raw.githubusercontent.com/waveq/nosqlUG/master/screens/1A%20cpu.jpg?token=ABKxe5-FInt7NepRp0BGUl39HguS5Qi2ks5Ua5NzwA%3D%3D)

![alt tag](https://raw.githubusercontent.com/waveq/nosqlUG/master/screens/1A%20RAM.jpg?token=ABKxexaoSMMzNxqZvHhHb9luHNh51Rgjks5Ua5OFwA%3D%3D)

### Zadanie 1B:
**Zliczenie zaimportowanych rekordów**
```
> db.train.count()
6034195
```
### Zadanie 1B:
Zamienić string zawierający tagi na tablicę napisów z tagami następnie zliczyć wszystkie tagi i wszystkie różne tagi. Napisać program, który to zrobi korzystając z jednego ze sterowników.

[1C.java](/scripts/1C.java)
Pamięć zapełniała się stopniowo by w końcu dojść do następującego poziomu "zapełnienia": 

![alt tag](https://raw.githubusercontent.com/waveq/nosqlUG/master/screens/1C%20ram.jpg?token=ABKxezPikvQ1Ndr05JYxayHCmlDlQrjeks5Ua5U9wA%3D%3D)

Całość trwała: xxx

