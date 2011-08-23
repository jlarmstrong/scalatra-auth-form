import org.scalatra.auth.{ScentryConfig, ScentrySupport}
import org.scalatra.ScalatraKernel
import org.scalatra.auth.strategy.{BasicAuthStrategy, BasicAuthSupport}
import org.scalatra.auth.{ScentrySupport, ScentryConfig}
import org.scalatra.{CookieSupport, ScalatraKernel, CookieOptions, Cookie}
import org.slf4j.{LoggerFactory}

trait AuthenticationSupport extends ScentrySupport[User] with BasicAuthSupport[User] { self: ScalatraKernel =>

  val logger = LoggerFactory.getLogger(getClass)

  val realm = "example"
  protected def contextPath = request.getContextPath

  protected def fromSession = { case id: String => User.find(id) openOr null  }
  protected def toSession   = { case usr: User => usr.id.toString() }

  protected val scentryConfig = (new ScentryConfig {}).asInstanceOf[ScentryConfiguration]

  override protected def registerAuthStrategies = {
    scentry.registerStrategy('UserPassword, app ⇒ new UserPasswordStrategy(app))
    scentry.registerStrategy('RememberMe, app ⇒ new RememberMeStrategy(app.asInstanceOf[ScalatraKernel with CookieSupport]))
  }

  override protected def configureScentry = {
    scentry.unauthenticated {
      scentry.strategies.get('UserPassword) foreach {
        _.unauthenticated()
      }
    }
  }


  def redirectIfAuthenticated {
    if (isAuthenticated) {
      logger.info("Auth: isAuth redirect["+scentryConfig.returnTo+"]")
      redirect(scentryConfig.returnTo)
    }
  }

  def redirectIfNotAuthenticated {
    if (!isAuthenticated) {
      logger.info("Auth: not isAuth redirect["+scentryConfig.returnTo+"]")
      logger.info("Auth: not isAuth redirect["+scentryConfig.returnTo+"]")
      redirect(scentryConfig.login)
    }
  }

}