package Compression


/**
 * trait for compression type and implementing in zipper scala source directory
 * */
trait Compression {

  /**
   * It takes three parameter and compress the given files
   * @param inputDir
   *  @param outputDir
   *  @param fileSize
   * */
  def compress(inputDir : String, outputDir:String, fileSize: Long)


  /**
   * It takes three parameter and compress the given files
   * @param inputDir
   *  @param outputDir
   * */
  def decompress(inputDir: String, outputDir:String)

}
