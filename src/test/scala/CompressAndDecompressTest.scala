import java.io.File
import java.util.zip.ZipEntry
import  org.junit.Assert.assertTrue;

import Compression.Zipper
import org.apache.commons.io.FileUtils
import org.apache.commons.io.output.CountingOutputStream
import org.junit.Test
import utils.FileUtility


class CompressAndDecompressTest {

  def areDirEqual(inputDir: String, decompressDir: String): Boolean = {
    val listOfFile1 = FileUtility.getAllFiles(inputDir)
    val listOfFile2 = FileUtility.getAllFiles(decompressDir)
    if(listOfFile1.size != listOfFile2.size) {
      return false
    }
    for(i <- 0 until listOfFile1.size){
      if (listOfFile1(i).length() != listOfFile2(i).length()) {
        return false;
      }
    }
    true
  }

  @Test
  def compressAndDecompress(): Unit = {
    val inputDir = "src/test/resources/testData"
    val outputDir = "src/test/resources/compressedTestDir/"
    val decompressDir = "src/test/resources/extractedTestDir/"
    val maxSize = FileUtility.mbToBytes(5.0)

    val outFile = new File(outputDir)
    val decompressFile = new File(decompressDir)

    if (!outFile.exists()) outFile.mkdir()

     if (!decompressFile.exists()) decompressFile.mkdir()


    FileUtils.cleanDirectory(outFile)
    FileUtils.cleanDirectory(decompressFile)

    val compressionAndDecompression = new Zipper {
      override var outStream: CountingOutputStream = _
      override var zipEntry: ZipEntry = _
    }

//    compressionAndDecompression.compress(inputDir,outputDir,maxSize)

    compressionAndDecompression.decompress(outputDir,decompressDir)


    assertTrue(areDirEqual(inputDir,decompressDir))

  }

}
