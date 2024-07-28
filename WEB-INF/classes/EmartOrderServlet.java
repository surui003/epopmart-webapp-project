// To save as "ebookshop\WEB-INF\classes\EmartOrderServlet.java".
import java.io.*;
import java.sql.*;
import jakarta.servlet.*;            // Tomcat 10
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
//import javax.servlet.*;            // Tomcat 9
//import javax.servlet.http.*;
//import javax.servlet.annotation.*;

@WebServlet("/emartorder")   // Configure the request URL for this servlet (Tomcat 7/Servlet 3.0 upwards)
public class EmartOrderServlet extends HttpServlet {

   // The doGet() runs once per HTTP GET request to this servlet.
   @Override
   public void doGet(HttpServletRequest request, HttpServletResponse response)
               throws ServletException, IOException {
      // Set the MIME type for the response message
      response.setContentType("text/html");
      // Get a output writer to write the response message into the network socket
      PrintWriter out = response.getWriter();
      // Print an HTML page as the output of the query
      out.println("<html>");
      out.println("<head><title>Query Response</title></head>");
      out.println("<body>");

      try (
         // Step 1: Allocate a database 'Connection' object
         Connection conn = DriverManager.getConnection(
               "jdbc:mysql://localhost:3306/epopmart?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC",
               "myuser", "xxxx");   // For MySQL
               // The format is: "jdbc:mysql://hostname:port/databaseName", "username", "password"

         // Step 2: Allocate a 'Statement' object in the Connection
         Statement stmt = conn.createStatement();
      ) {
         // Step 3 & 4: Execute a SQL SELECT query and Process the query result
         // Retrieve the products' id. Can order more than one product.
         String[] ids = request.getParameterValues("id");
         if (ids != null) {
            String sqlStr;
            int count;
 
            // Process each of the products
            for (int i = 0; i < ids.length; ++i) {
               // Update the qty of the table products
               sqlStr = "UPDATE products SET qty = qty - 1 WHERE id = " + ids[i];
               //out.println("<p>" + sqlStr + "</p>");  // for debugging
               count = stmt.executeUpdate(sqlStr);
               //out.println("<p>" + count + " record updated.</p>");
 
               // Create a transaction record
               sqlStr = "INSERT INTO order_records VALUES ("
                     + ids[i] + ", 1, " + "'" + request.getParameter("cust_name") + "', " + "'" + request.getParameter("cust_email") + "', " + "'" 
                     + request.getParameter("cust_phone") + "', '" + request.getParameter("username") + "')";
               //out.println("<p>" + sqlStr + "</p>");  // for debugging
               count = stmt.executeUpdate(sqlStr);
               //out.println("<p>" + count + " record inserted.</p>");
            }

            //navigation bar
            out.println("<script src='https://unpkg.com/boxicons@2.1.4/dist/boxicons.js'></script>");

            out.println("<meta charset='utf-8'>");
            out.println("<meta name='viewport' content='width=device-width, initial-scale=1, shrink-to-fit=no'>");

    
            out.println("<link rel='stylesheet' href='https://cdn.jsdelivr.net/npm/bootstrap@4.4.1/dist/css/bootstrap.min.css' integrity='sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh' crossorigin='anonymous'>");
            out.println("<link rel='styleindex' href='./style.css'>");

            out.println("<nav class='navbar navbar-expand-lg navbar-light bg-light'>");
            out.println("<a class='navbar-brand' href='emartindex.html'><img src='epopmart_logo.png' alt='popmart_logo' width='160' height='60'></a>");
            out.println("<button class='navbar-toggler' type='button' data-toggle='collapse' data-target='#navbarSupportedContent' aria-controls='navbarSupportedContent'     aria-expanded='false' aria-label='Toggle navigation'>");
            out.println("<span class='navbar-toggler-icon'></span>");
            out.println("</button>");
      
            out.println("<div class='collapse navbar-collapse' id='navbarSupportedContent'>");
            out.println("<ul class='navbar-nav mr-auto'>");
            out.println("<li class='nav-item'>");
            out.println("<a class='nav-link' href='emartaboutus.html'>About Us</a>");
            out.println("</li>");
            out.println("</ul>");
        
            out.println("<form class='form-inline my-2 my-lg-0'>");
            out.println("<a class='nav-link my-2 my-sm-0' href='emartwelcome'>Login <span class='sr-only'>(current)</span></a>");
            out.println("<a class='nav-link my-2 my-sm-0' href='emartreglogin'>Sign up <span class='sr-only'>(current)</span></a>");
            out.println("<a class='nav-link my-2 my-sm-0' href='emartcontact.html'>Contact us <span class='sr-only'>(current)</span></a>");
            out.println("<box-icon name='cart' size='md'></box-icon>");
            out.println("</form>"); 
            out.println("</div>");
            out.println("</nav>");

            //thank you message
            out.println("<br>");
            out.println("<h2><p class='text-center'>Thank You!</p></h2>");
            out.println("<h3><p class='text-center'>Your order has been confirmed.</p></h3>");
   
         } else { // No product selected
            //navigation bar
            out.println("<script src='https://unpkg.com/boxicons@2.1.4/dist/boxicons.js'></script>");

            out.println("<meta charset='utf-8'>");
            out.println("<meta name='viewport' content='width=device-width, initial-scale=1, shrink-to-fit=no'>");

    
            out.println("<link rel='stylesheet' href='https://cdn.jsdelivr.net/npm/bootstrap@4.4.1/dist/css/bootstrap.min.css' integrity='sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh' crossorigin='anonymous'>");
            out.println("<link rel='styleindex' href='./style.css'>");

            out.println("<nav class='navbar navbar-expand-lg navbar-light bg-light'>");
            out.println("<a class='navbar-brand' href='emartindex.html'><img src='epopmart_logo.png' alt='popmart_logo' width='160' height='60'></a>");
            out.println("<button class='navbar-toggler' type='button' data-toggle='collapse' data-target='#navbarSupportedContent' aria-controls='navbarSupportedContent'     aria-expanded='false' aria-label='Toggle navigation'>");
            out.println("<span class='navbar-toggler-icon'></span>");
            out.println("</button>");
      
            out.println("<div class='collapse navbar-collapse' id='navbarSupportedContent'>");
            out.println("<ul class='navbar-nav mr-auto'>");
            out.println("<li class='nav-item'>");
            out.println("<a class='nav-link' href='emartaboutus.html'>About Us</a>");
            out.println("</li>");
            out.println("</ul>");
        
            out.println("<form class='form-inline my-2 my-lg-0'>");
            out.println("<a class='nav-link my-2 my-sm-0' href='emartwelcome'>Login <span class='sr-only'>(current)</span></a>");
            out.println("<a class='nav-link my-2 my-sm-0' href='emartreglogin'>Sign up <span class='sr-only'>(current)</span></a>");
            out.println("<a class='nav-link my-2 my-sm-0' href='emartcontact.html'>Contact us <span class='sr-only'>(current)</span></a>");
            out.println("<box-icon name='cart' size='md'></box-icon>");
            out.println("</form>"); 
            out.println("</div>");
            out.println("</nav>");

            //error message
            out.println("<br>");
            out.println("<h3><p class='text-center'>Please go back and select a product!</p></h3>");
         }
      } catch(Exception ex) {
         out.println("<p>Error: " + ex.getMessage() + "</p>");
         out.println("<p>Check Tomcat console for details.</p>");
         ex.printStackTrace();
      }  // Step 5: Close conn and stmt - Done automatically by try-with-resources (JDK 7)
 
      out.println("</body></html>");
      out.close();
   }
}