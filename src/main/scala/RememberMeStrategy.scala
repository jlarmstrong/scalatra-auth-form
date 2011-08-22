
import org.scalatra.auth.ScentryStrategy
import org.scalatra.{CookieSupport, ScalatraKernel, CookieOptions, Cookie}
import net.liftweb.common._
import javax.servlet.http.{Cookie ⇒ ServletCookie}
import net.liftweb.common.{Box, Empty, Failure, Full}



/**
 * Authentication strategy to authenticate a user from a cookie.
 */
class RememberMeStrategy(protected val app: ScalatraKernel with CookieSupport)
  extends ScentryStrategy[User]
{

  val COOKIE_KEY = "remember_me"
  private val oneWeek = 7 * 24 * 3600

  override def isValid = {
    app.cookies.get(COOKIE_KEY).isDefined
  }

  /**
   * After authentication, sets the remember-me cookie on the response.
   */
  override def afterAuthenticate(winningStrategy: Symbol, user: User) = {

    if (winningStrategy == 'RememberMe ||
      (winningStrategy == 'UserPassword && checkbox2boolean(app.params.get("remember_me").getOrElse("").toString))) {

      val token = user.rememberMe.value

      app.response.addHeader("Set-Cookie",
        Cookie(COOKIE_KEY, token)(CookieOptions(secure = false, maxAge = oneWeek, httpOnly = true)).toCookieString)
    }
  }

  /**
   * Authenticates a user by validating the remember-me cookie.
   */
  def authenticate = {

    val token: String = app.cookies.get(COOKIE_KEY) match {
      case Some(v) => v.toString
      case None => ""
    }

    User.validateRememberToken(token) match {
      case None => {
        None
      }
      case Some(usr) ⇒ {
        Some(usr)
      }
    }

  }

  /**
   * Clears the remember-me cookie for the specified user.
   */
  override def beforeLogout(user: User) = {
    if (user != null){
      user.forgetMe
    }
    app.cookies.get(COOKIE_KEY) foreach {
      _ => app.cookies.update(COOKIE_KEY, null)
    }
  }

  /**
  * Used to easily match a checkbox value
  */
  def checkbox2boolean(s: String): Boolean = {
    s match {
      case "yes" => true
      case "y" => true
      case "1" => true
      case "true" => true
      case _ => false
    }
  }
}