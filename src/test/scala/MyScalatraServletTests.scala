
import org.scalatra.test.scalatest.ScalatraSuite
import org.scalatest.FunSuite

class MyScalatraServletTests extends ScalatraSuite
  with FunSuite
{
  addServlet(classOf[MyScalatraServlet], "/*")

  test("index page") {
    get("/") {
      status should equal(200)
      body should include("Hello, world!")
    }
  }

}