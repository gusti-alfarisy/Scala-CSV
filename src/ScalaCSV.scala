import java.io.{File, PrintWriter}

import scala.collection.mutable.ArrayBuffer
import scala.io.Source

/**
  * Created by gusti on 03/08/2016.
  */

object ScalaCSV {

  var splitRegex = ","

  def readByIndex[T : Manifest](fileName: String, indexPattern: Array[String]): Array[T]= {
    var arrData = new ArrayBuffer[T]()
    var newObj = manifest[T].erasure.newInstance().asInstanceOf[T]
    val fields = manifest[T].erasure.newInstance().asInstanceOf[T].getClass.getDeclaredFields()
    fields.foreach(_.setAccessible(true))

    var arrTemp = readWithoutHeader(fileName)

    arrTemp.foreach(x => {
      newObj = manifest[T].erasure.newInstance().asInstanceOf[T]
      for( (d, iD) <- x.zipWithIndex ){
        indexPattern(iD) match {
          case "Double" => fields(iD).set(newObj, d.toDouble)
          case "Int" => fields(iD).set(newObj, d.toInt)
          case "String" => fields(iD).set(newObj, d)
        }
      }
      arrData += newObj
    })

    arrData.toArray
  }

  //First Index as key
  def readToMap[T : Manifest](fileName: String, indexPattern: Array[String]): Map[String, T]= {
    var arrTemp = readWithoutHeader(fileName)
    var map:Map[String, T]  = Map()

    var newObj = manifest[T].erasure.newInstance().asInstanceOf[T]
    val fields = manifest[T].erasure.newInstance().asInstanceOf[T].getClass.getDeclaredFields()
    fields.foreach(_.setAccessible(true))

    arrTemp.foreach(x => {
      for( (d, iD) <- x.zipWithIndex ){
        if(iD > 0){
          newObj = manifest[T].erasure.newInstance().asInstanceOf[T]
          indexPattern(iD-1) match {
            case "Double" => fields(iD-1).set(newObj, d.toDouble)
            case "Int" => fields(iD-1).set(newObj, d.toInt)
            case "String" => fields(iD-1).set(newObj, d)
          }
        }
      }

      map += (x(0) -> newObj)
    })

    map
  }

  def readToSelfMap [T : Manifest](fileName: String, indexPattern: Array[String]): Array[Map[String, T]] = {
    var arrData = new ArrayBuffer[Map[String, T]]()
    var newObj = manifest[T].erasure.newInstance().asInstanceOf[T]
    val fields = manifest[T].erasure.newInstance().asInstanceOf[T].getClass.getDeclaredFields()
    fields.foreach(_.setAccessible(true))

    val arrTemp = read(fileName)
    val headers = arrTemp(0)
    val data = for(iT <- 1 to arrTemp.length-1) yield arrTemp(iT)

    data.foreach(x => {
      var m: Map[String, T]= Map()
      for( (d, iD) <- x.zipWithIndex ){
          newObj = manifest[T].erasure.newInstance().asInstanceOf[T]
          fields(0).set(newObj, headers(iD))
          indexPattern(1) match {
            case "Double" => fields(1).set(newObj, d.toDouble)
            case "Int" => fields(1).set(newObj, d.toInt)
            case "String" => fields(1).set(newObj, d)
          }
        m += (headers(iD) -> newObj)
      }

      arrData += m

    })

    arrData.toArray
  }

  def read(fileName: String) : Array[Array[String]]= {
    val source = Source.fromFile(fileName)
    val result = source.getLines().toArray.map(_.split(splitRegex))
    source.close()
    return result
  }

  def readWithoutHeader(fileName: String) : Array[Array[String]]= {
    val source = Source.fromFile(fileName)
    val result = source.getLines().drop(1).toArray.map(_.split(splitRegex))
    source.close()
    return result
  }

  def write[T: Manifest](fileName: String, data: Array[T]) : Unit = {
    val pw = new PrintWriter(new File(fileName))
    val fields = data(0).getClass.getDeclaredFields()
    fields.foreach(x => pw.write(s"${x.getName}${splitRegex}"))
    pw.write("\n")
    data.foreach(x => {
      x.getClass.getDeclaredFields.foreach(y => {
        y.setAccessible(true)
        pw.write(y.get(x).toString + splitRegex)
      })
      pw.write("\n")
    })
    pw.close()
  }

}
