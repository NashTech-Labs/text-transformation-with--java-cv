import com.lightbend.lagom.scaladsl.akka.discovery.AkkaDiscoveryComponents
import com.lightbend.lagom.scaladsl.api.{Descriptor, ServiceLocator}
import com.lightbend.lagom.scaladsl.server.{LagomApplication, LagomApplicationContext, LagomApplicationLoader, LagomServer}
import com.softwaremill.macwire.wire
import play.api.libs.ws.ahc.AhcWSComponents

class TextTransformationLoader extends LagomApplicationLoader{

  override def load(context: LagomApplicationContext): LagomApplication = {
    new TextTransformationApplication(context) with AkkaDiscoveryComponents
  }
  override def loadDevMode(context: LagomApplicationContext): LagomApplication = {
    new TextTransformationApplication(context)  {
      override def serviceLocator: ServiceLocator = ServiceLocator.NoServiceLocator
    }
  }
  override def describeService: Option[Descriptor] = Some(readDescriptor[TextTransformationService])


}


abstract class TextTransformationApplication(context: LagomApplicationContext)
  extends LagomApplication(context)
    with AhcWSComponents {
  override lazy val lagomServer: LagomServer = serverFor[TextTransformationService](wire[TextTransformationImpl]).additionalRouter(wire[FileUploadRouter].router)
}