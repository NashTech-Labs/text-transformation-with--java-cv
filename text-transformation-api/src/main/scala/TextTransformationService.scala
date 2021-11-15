import com.lightbend.lagom.scaladsl.api.{Descriptor, Service, ServiceAcl}

trait TextTransformationService extends Service {

  override def descriptor: Descriptor = {
    import Service._
    named("HelloService")
      .withCalls()
      .withAcls(
        ServiceAcl(pathRegex = Some("/api/upload")))
  }

}
