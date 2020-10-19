package Compression

import java.io.{File, FileInputStream, FileOutputStream, IOException}
import java.util.zip.{ZipEntry, ZipInputStream, ZipOutputStream}

import org.apache.commons.io.output.CountingOutputStream
import utils.FileUtility
import scala.collection.immutable.HashMap


/**
 * class that uses the trait to implement compress and decompress method
 * creating abstract based class and using it for compression files and decompression files

 * */
abstract class Zipper extends Compression {

  var partFileNumber: Int = 1

  val ZIP_HEADER_SIZE: Long = FileUtility.mbToBytes(0.02)

  var outStream : CountingOutputStream
  var zipEntry : ZipEntry

  /** Monitoring the part file values*/
  def getPartFileName: String = {
    partFileNumber = partFileNumber+1
    "compressed-part-" + partFileNumber + ".zip"
  }

  /**
   * it provide the available bytes in stream
   * @param maxFileSizeBytes max file size bytes
   *  @return Long
   * */
  def getAvailableBytesInStream( maxFileSizeBytes : Long) : Long = {
     maxFileSizeBytes - this.outStream.getByteCount
  }


  /**
   * It taking three parameter and compressing file into zip
   * @param inputDir input directory for the compression
   * @param outputDir output directory for the compression
   *  @param fileSize filesize directory for the compression
   *
   * */
  override def compress(inputDir: String, outputDir: String, fileSize: Long): Unit = {
    val files = FileUtility.getAllFiles(inputDir)

    val outFile = new File(outputDir)
    if (!outFile.exists()) outFile.mkdir()

    var fos = new FileOutputStream(outputDir + getPartFileName)
    this.outStream = new CountingOutputStream(fos)
    var zipOut: ZipOutputStream = new ZipOutputStream(outStream)
    for(file <- files){
      try{
        val fis = new FileInputStream(file)
        zipOut.putNextEntry(new ZipEntry(FileUtility.getRelativePath(inputDir,file.getPath)))

        val bytes = new Array[Byte](1024)

        Iterator
          .continually (fis.read(bytes))
          .takeWhile (_ >= 0)
          .foreach {read=>
            if(getAvailableBytesInStream(fileSize ) - ZIP_HEADER_SIZE > read){
              zipOut.write(bytes, 0, read)
            }else{
              zipOut.closeEntry()
              zipOut.finish()
              this.outStream.close()
              fos = new FileOutputStream(outputDir + getPartFileName)
              this.outStream = new CountingOutputStream(fos)
              zipOut = new ZipOutputStream(this.outStream)
              zipOut.putNextEntry(new ZipEntry(FileUtility.getRelativePath(inputDir, file.getPath)))
              zipOut.write(bytes, 0, read)
            }
          }
        fis.close()
      }catch {
        case e: IOException  => e.printStackTrace()
      }
    }
    zipOut.close()
    this.outStream.close()
    fos.close()
  }


  /**
   * It taking three parameter and decompressing file into zip
   * @param inputDir input directory for the decompression
   * @param outputDir output directory for the decompression
   *
   * */
  override def decompress(inputDir: String, outputDir: String): Unit = {
    val zipFiles: List[File] = FileUtility.getZipFiles(inputDir)
    var fosMap : HashMap[String,FileOutputStream] = new HashMap()
    FileUtility.createDirIfNotExist(outputDir)

    for(file <- zipFiles){
        try{
          val fileInputStream: FileInputStream = new FileInputStream(file)
          val zipIn = new ZipInputStream(fileInputStream)
          var fos :FileOutputStream = null
          while ({zipEntry = zipIn.getNextEntry;  zipEntry!= null}) {
            val newFile = new File(outputDir + zipEntry.getName)
            if(zipEntry.isDirectory){
              newFile.mkdir()
            }else {
              newFile.getParentFile.mkdir()
              newFile.createNewFile()

              if(fosMap.contains(zipEntry.getName)){
                fos = fosMap(zipEntry.getName)
              }else {
                fos = new FileOutputStream(newFile)
                fosMap += (zipEntry.getName -> fos)
              }

              val bytes = new Array[Byte](1024)

              Iterator
                .continually (zipIn.read(bytes))
                .takeWhile (_ >= 0)
                .foreach (read=>fos.write(bytes,0,read))
              fos.close()
            }
          }
          zipIn.close()
        }catch {
          case e: IOException  => e.printStackTrace()
        }
    }

  }
}
