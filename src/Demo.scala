/**
  * Created by gusti on 11/08/2016.
  */
object Demo {

  class Person(var name: String, var age: Int){
    def this() = {this("noname", 1)}
  }


  def main(args: Array[String]) : Unit = {
    ScalaCSV.delimiter = ";"

    val personArr = for (i <- 1 to 10) yield new Person(s"Name $i", 20+i)

    ScalaCSV.write[Person]("demo.csv", personArr.toArray)

    val arrString = ScalaCSV.read("demo.csv")

    val arrString2 = ScalaCSV.readWithoutHeader("demo.csv")

    val arrObj = ScalaCSV.readByIndex[Person]("demo.csv", Array("String", "Int"))

    val arrMap = ScalaCSV.readToMap[Person]("demoMap.csv", Array("String", "Int"))

  }




}
