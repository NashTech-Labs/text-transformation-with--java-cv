import akka.stream.IOResult
import akka.stream.scaladsl.{FileIO, Sink}
import akka.util.ByteString
import net.sourceforge.tess4j.Tesseract
import play.api.libs.streams.Accumulator
import play.api.mvc.MultipartFormData.FilePart
import play.api.mvc.{DefaultActionBuilder, PlayBodyParsers, Results}
import play.api.routing.Router
import play.api.routing.sird._
import play.core.parsers.Multipart.{FileInfo, FilePartHandler}

import java.io.File
import java.util.UUID
import javax.imageio.ImageIO
import scala.concurrent.{ExecutionContext, Future}

class FileUploadRouter(action: DefaultActionBuilder, parser: PlayBodyParsers, implicit val exCtx: ExecutionContext) {

  private def handleUpload: FilePartHandler[File] = {
    case FileInfo(partName, fileName, contentType, _) =>
      val tempFile = {
        val f = new java.io.File("./target/file-upload-data/uploads", UUID.randomUUID().toString).getAbsoluteFile
        f.getParentFile.mkdirs()
        f
      }
      val sink: Sink[ByteString, Future[IOResult]] = FileIO.toPath(tempFile.toPath)
      val acc: Accumulator[ByteString, IOResult] = Accumulator(sink)
      acc.map {
        case akka.stream.IOResult(_, _) =>
          FilePart(partName, fileName, contentType, tempFile)
      }
  }

  val router = Router.from {
    case POST(p"/api/upload") =>
      action(parser.multipartFormData(handleUpload)) { request =>
        val files = request.body.files.map(_.ref.getAbsolutePath)

        val z = files.map { x =>
          val tesseract = new Tesseract
          val buffedImage = ImageIO.read(new File(x))
          tesseract.setDatapath("text-transformation-impl/src/main/resources/tessdata")
          val string = tesseract.doOCR(buffedImage)
          string
        }
        Results.Ok(z.toString)
      }
  }
}
