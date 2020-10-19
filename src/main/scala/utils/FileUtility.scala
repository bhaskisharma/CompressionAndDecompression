package utils

import java.io.File
import java.nio.file.attribute.{BasicFileAttributeView, BasicFileAttributes}
import java.nio.file.{Files, Path, Paths}


/**
 * File Utility singleton object for different different directory operation
 * this singleton handles all the file utility operation in which the file get the path
 * here we are creating mega to byte and byte to mega to check the memory usage by JVM
 *
 * */
object FileUtility {

  /** MegaByte define the program to check the memory */
  val megaBytes: Long = 1000L * 1000L


  /**
   * it takes one path variable and return the file is directory or not
   * @param path path to check
   * @return Option[file]
   * */
  def pathToDir(path: Path): Option[File] = {
    val file = path.toFile
    if (file.isDirectory) {
      Some(file)
    } else {
      None
    }
  }

  /**
   * it takes one path string and returning the path
   * @param pathString to string
   * @return path
   * */
  def pathStringToPath(pathString: String): Path = Paths.get(pathString)


  /**
   * it takes one path string and returning the path
   * @param pathString path to string
   * @return Option[file]
   * */
  def pathStringToDir(pathString: String): Option[File] = pathToDir(pathStringToPath(pathString))


  /**
   * it takes one path string and returning the path
   * @param directory directory of the all files
   * @return List[File]
   * */
  def getAllFiles(directory : String) : List[File] = {
    val dirOption = pathStringToDir(directory)
    dirOption.fold(List.empty[File])(dir => dir.listFiles().toList)
  }

  /**
   * it takes directory
   * @param directory directory to create dir if not exist
   *
   * */
  def createDirIfNotExist(directory :String): Unit ={
    val file = new File(directory)
    if(!file.exists()) file.mkdir()
  }

  /**
   * validating the directory
   * @param directory to validate dir
   *
   * */
  def validateDri(directory : String): Boolean = {
    Files.isDirectory(Paths.get(directory))
  }


  /**
   * it takes bytes and convert to byte to MB
   * @param bytes number of bytes
   * @return Long
   * */
  def byteToMB(bytes: Long): Long ={
    bytes/megaBytes
  }

  /**
   * it takes MB and convert to MB to bytes
   * @param mb to bytes
   * @return Long
   * */
  def mbToBytes(mb : Double): Long =  (mb * megaBytes).toLong


  /**
   * checking the relative path
   * @param basePath base path
   * @param absolutePath absolutepath
   * @return List[File]
   * */
  def getRelativePath(basePath:String,absolutePath:String): String ={
    if(absolutePath.startsWith(basePath)){
      return absolutePath.substring(basePath.length())
    }
    absolutePath
  }

  /**
   * getting the list of all zip files
   * @param directory directory values
   * @return List[File]
   * */
  def getZipFiles(directory:String): List[File] ={
    val files : List[File]  = getAllFiles(directory).filter(files => files.getName.endsWith(".zip"))
    files.sortWith{ (f1,f2) =>
      val attr1 : BasicFileAttributes = Files.getFileAttributeView(f1.toPath,classOf[BasicFileAttributeView]).readAttributes()
      val attr2 : BasicFileAttributes = Files.getFileAttributeView(f2.toPath,classOf[BasicFileAttributeView]).readAttributes()
      attr1.creationTime().compareTo(attr2.creationTime()) < 0
    }
  }
}
