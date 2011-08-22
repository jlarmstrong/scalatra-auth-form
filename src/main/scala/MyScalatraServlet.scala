import org.scalatra._
import java.net.URL
import scalate.ScalateSupport

import net.liftweb.mongodb._
import net.liftweb.json._
import net.liftweb.mongodb.record.MongoRecord



class MyScalatraServlet extends ScalatraServlet with ScalateSupport 
 with AuthenticationSupport
 {
  
  before() {
    MongoDB.defineDb(DefaultMongoIdentifier, MongoAddress(MongoHost("127.0.0.1", 27017), "scalatra-auth"))
  }
  after() {
    MongoDB.close
  }

  get("/") {
    <html>
      <body>
        <h1>Hello, world!</h1>
        Say <a href="hello-scalate">hello to Scalate</a>.
        Test Auth <a href="register">Register</a>.
      </body>
    </html>
  }

  get("/login") {
    contentType = "text/html"

    <html>
    <body>
      <h1>Login</h1>
      <form method="post" action="/login">
      <div><label>User</label><input name="userName" value="test@test.com" /></div>
      <div><label>password</label><input name="password" /></div>
      <div><input type="submit" value="submit" /></div>
      </form>
    </body>
    </html>
  }

  post("/login") {
    //basicAuth
    
    redirect("/loggedin")
  }

  get("/loggedin") {
    //basicAuth
    contentType = "text/html"
    
    <html>
      <body>
      <h1>Hello, world!</h1>
      Welcome you are logged in.
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
    //logOut
    
    redirect("/logout_step")
  }
  
  // this step is used to verify the cookies are erased
  get("/logout_step/?") {
    redirect("/login")
  }

  notFound {
    // If no route matches, then try to render a Scaml template
    val templateBase = requestPath match {
      case s if s.endsWith("/") => s + "index"
      case s => s
    }
    val templatePath = "/WEB-INF/scalate/templates/" + templateBase + ".scaml"
    servletContext.getResource(templatePath) match {
      case url: URL => 
        contentType = "text/html"
        templateEngine.layout(templatePath)
      case _ => 
        response.sendError(404)
    } 
  }
}