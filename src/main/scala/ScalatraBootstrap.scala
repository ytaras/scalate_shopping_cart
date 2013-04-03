import com.softserve.cart._
import org.scalatra._
import javax.servlet.ServletContext
import com.softserve.cart.infrastructure.DbInit

class ScalatraBootstrap extends LifeCycle with DbInit {
  override def init(context: ServletContext) {
    org.slf4j.LoggerFactory.getLogger(getClass).warn("Started")
    configureDb
    context.mount(new MyScalatraServlet, "/*")
  }

  override def destroy(context: ServletContext) {
    closeConnection
  }
}
