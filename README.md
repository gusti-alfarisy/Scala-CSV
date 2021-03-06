# Scala-CSV

CSV Reader and Writer in Scala. This source can map the csv file into array of object or map and vice versa.

##Example

Copy the ScalaCSV.scala in src folder to your project. You can see the example in Demo.scala in src folder.

###CSV Delimiter

```scala
ScalaCSV.delimiter = ";"
```

###Reading CSV to Array[String]
```scala
val arrString = ScalaCSV.read("demo.csv")
```

###Reading CSV to Array[String] without header
```scala
val arrString2 = ScalaCSV.readWithoutHeader("demo.csv")
```
###Reading CSV to Array[Obj]

Defining the class first:
```scala
class Person(var name: String, var age: Int){
    def this() = {this("noname", 1)}
}
```
```scala
val arrObj = ScalaCSV.readByIndex[Person]("demo.csv", Array("String", "Int"))
```

###Reading CSV to Map[String, Obj]

First column will be the key.
```scala
val arrMap = ScalaCSV.readToMap[Person]("demoMap.csv", Array("String", "Int"))
```

###Write Array of Object to CSV
 ```scala
 val personArr = for (i <- 1 to 10) yield new Person(s"Name $i", 20+i)
 ScalaCSV.write[Person]("demo.csv", personArr.toArray)
 ```