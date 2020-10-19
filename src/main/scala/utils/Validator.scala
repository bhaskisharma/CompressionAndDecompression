package utils

import java.util.zip.ZipEntry

import Compression.Zipper
import Exception.InvalidCommandException
import constants.Constant
import org.apache.commons.io.output.CountingOutputStream

/**
 * Validating the command line argument and running the process of decompression and compression
 * */
class Validator{

    /**
     * @param command line argument and process the running
     * */
  def validateArgs(command: Array[String]): Unit = {
    if (command.length < 3) {
      throw InvalidCommandException("Incorrect number of arguments provided")
    }
    if (command(0).equalsIgnoreCase(Constant.COMPRESS)) {
      if (command.length != 4) {
        throw InvalidCommandException("Incorrect number of arguments")
      }
//      if (!FileUtility.validateDri(command(1))) {
//        throw InvalidCommandException("Input directory is invalid")
//      }
//      if (!FileUtility.validateDri(command(2))) {
//        throw InvalidCommandException("Output directory is invalid")
//      }
      else {
        val inputDir = command(1)
        val outputDir = command(2)

        val fileSize = FileUtility.mbToBytes(command(3).toDouble)

        val compression =  new Zipper {
          override var outStream: CountingOutputStream = _
          override var zipEntry: ZipEntry = _
        }

        println("Starting to compress " + inputDir + " to " + outputDir + " using " +
          "ZIP" + " compression")

        val startTime = System.nanoTime()
        compression.compress(inputDir,outputDir,fileSize)
        println("Compression finished in " + (System.nanoTime() - startTime) / 1000000 + " ms")

        println("Maximum memory used (MB):" + FileUtility.byteToMB(Runtime.getRuntime.totalMemory()))
      }
    } else if (command(0).equalsIgnoreCase(Constant.EXTRACT)) {
      if (command.length != 3) {
        throw InvalidCommandException("Incorrect number of arguments")
      } else {
        val inputDir = command(1)
        val outputDir = command(2)

        println("Starting to compress " + inputDir + " to " + outputDir + " using " +
          "ZIP" + " compression")
        val startTime = System.nanoTime()

        val extract =  new Zipper {
          override var outStream: CountingOutputStream = _
          override var zipEntry: ZipEntry = _
        }
        extract.decompress(inputDir,outputDir)
        println("Compression finished in " + (System.nanoTime() - startTime) / 1000000 + " ms")

        println("Maximum memory used (MB):" + FileUtility.byteToMB(Runtime.getRuntime.totalMemory()))
      }
    }
    else {
      println("This command is not supported")
    }
  }


}
