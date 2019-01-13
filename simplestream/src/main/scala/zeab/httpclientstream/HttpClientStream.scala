package zeab.httpclientstream

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.headers.RawHeader
import akka.http.scaladsl.model.{HttpMethods, HttpRequest}
import akka.http.scaladsl.settings.ConnectionPoolSettings
import akka.stream.{ActorAttributes, ActorMaterializer, Supervision}
import zeab.akkatools.akkaconfigbuilder.AkkaConfigBuilder
import zeab.akkatools.akkahttpclient.HttpClient
import zeab.logging.Logging

import scala.concurrent.duration._
import scala.concurrent.ExecutionContext
import scala.concurrent.duration.Duration
import scala.util.{Failure, Success}

object HttpClientStream extends Logging{

  def main(args: Array[String]): Unit = {

    //Akka
    implicit val system:ActorSystem = ActorSystem("HttpClientStream", AkkaConfigBuilder.buildConfig())
    implicit val mat: ActorMaterializer = ActorMaterializer()
    implicit val ec: ExecutionContext = system.dispatcher
    val decider: Supervision.Decider = { ex =>
      log.info(ex.toString)
      Supervision.Resume
    }

    //Build the request
    val request = HttpRequest(
      uri = s"http://localhost:8080/actorassourceforhttpstream",
      method = HttpMethods.GET
    )

//    val accountId: String = "101-001-8063813-001"
//    val bearer: String = "e085e971e54552403b76ece5a01ebb34-1f44d6a8d47551404079432243feb751"
//
//    val instruments: String =
//      List(
//        "EUR_USD",
//        "USD_CAD",
//        "USD_JPY",
//        "GBP_USD",
//        "USD_CHF",
//        "AUD_USD")
//        .mkString("", "%2C", "")
//
//    val request =
//      HttpRequest(
//        uri = s"https://stream-fxpractice.oanda.com/v3/accounts/$accountId/pricing/stream?instruments=$instruments",
//        method = HttpMethods.GET,
//        headers =
//          List(
//            RawHeader("Authorization", s"Bearer $bearer")
//          )
//      )

//    HttpClient.invokeAsSource("http://localhost:8080/actorassourceforhttpstream").onComplete{
//      case Success(possibleSource) => possibleSource match {
//        case Right(source) =>
//          source
//            .runForeach(msg => log.info(msg.utf8String))
//        case Left(ex) => log.error(ex.toString)
//      }
//      case Failure(ex) => log.error(ex.toString)
//    }

    //Run the http client as a steam
    Http()
      .singleRequest(
        request,
        settings =
          ConnectionPoolSettings(system)
            .withIdleTimeout(Duration.Inf)
            .withResponseEntitySubscriptionTimeout(Duration.Inf)
      )
      .flatMap {
      _.entity
        .dataBytes.withAttributes(ActorAttributes.supervisionStrategy(decider)).idleTimeout(8.second)
        .map(_.utf8String.trim).withAttributes(ActorAttributes.supervisionStrategy(decider)).idleTimeout(8.second)
        .runForeach{
          system.eventStream.publish(_)
          logRaw.info
        }
    }.onComplete{
      case Success(x) => log.info(x.toString)
      case Failure(ex) => log.error(ex.toString)
    }

  }

}
