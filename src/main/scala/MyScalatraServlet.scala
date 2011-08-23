import org.scalatra._
import java.net.URL
import scalate.ScalateSupport

import net.liftweb.mongodb._
import net.liftweb.json._
import net.liftweb.mongodb.record.MongoRecord
import javax.servlet._


class MyScalatraServlet extends ScalatraServlet 
 with AuthenticationSupport
 with FlashMapSupport
 {
  
  override def initialize(config: ServletConfig): Unit = {
    MongoDB.defineDb(DefaultMongoIdentifier, MongoAddress(MongoHost("127.0.0.1", 27017), "scalatra-auth"))
    
    super.initialize(config)
  }
  
  before() {
    if (!isAuthenticated) {
      scentry.authenticate('RememberMe)
    }
  }


  get("/") {
    <html>
      <body>
        <h1>Hello, world!</h1>
        Say <a href="/login">hello to Scalate</a>.
        Test Auth <a href="register">Register</a>.
      </body>
    </html>
  }

  get("/login") {
    redirectIfAuthenticated
    
    contentType = "text/html"

    <html>
    <body>
      <h1>Login</h1>
      <form method="post" action="/login">
      <div><label>User:</label><input type="text" name="userName" value="test@test.com" /></div>
      <div><label>Password:</label><input type="password" name="password" /></div>
      <div><label>Remember Me:</label><input type="checkbox" name="rememberMe" value="true" /></div>
      <div><input type="submit" value="submit" /></div>
      </form>
    </body>
    </html>
  }

  post("/login") {
    scentry.authenticate('UserPassword)
    
    if (isAuthenticated) {
      redirect("/loggedin")
    }else{
      flash += ("error" -> "login failed")
      redirect("/login")
    }
  }

  get("/loggedin") {
    redirectIfNotAuthenticated
    
    contentType = "text/html"
    
    <html>
      <body>
      <h1>Hello, world!</h1>
      Welcome {user.username} you are logged in. <a href="/logout">Logout</a>
      </body>
    </html>
  }

  get("/register") {
    contentType = "text/html"

    <html>
    <body>
      <h1>Register</h1>
      <form method="post" action="/register">
      <div><label>User</label><input name="userName" value="test@test.com" /></div>
      <div><label>password</label><input name="password" /></div>
      <div><input type="submit" value="submit" /></div>
      </form>
    </body>
    </html>	
  }

  post("/register") {
    val u = User.createRecord
      .username(params("userName"))
      .password(params("password"))
      
    //save
    u.save
    
    redirect("/login")
  }
  
  get("/logout/?") {
    logOut
    
    redirect("/logout_step")
  }
  
  // this step is used to verify the cookies are erased
  get("/logout_step/?") {
    redirect("/login")
  }

  notFound {
          response.sendError(404)
  }
}