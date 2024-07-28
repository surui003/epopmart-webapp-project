// To save as "ebookshop\WEB-INF\classes\EmartQueryServlet.java".
import java.io.*;
import java.sql.*;
import jakarta.servlet.*;            // Tomcat 10
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
//import javax.servlet.*;            // Tomcat 9
//import javax.servlet.http.*;
//import javax.servlet.annotation.*;

@WebServlet("/emartquery")   // Configure the request URL for this servlet (Tomcat 7/Servlet 3.0 upwards)
public class EmartQueryServlet extends HttpServlet {

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
         // Step 3: Execute a SQL SELECT query
         String[] series = request.getParameterValues("series");  // Returns an array of Strings
         if (series == null) {
            out.println("<h2>No series selected. Please go back to select one or many series</h2><body></html>");
            return; // Exit doGet()
         }
         String sqlStr = "SELECT * FROM products WHERE series IN (";
         for (int i = 0; i < series.length; ++i) {
            if (i < series.length - 1) {
               sqlStr += "'" + series[i] + "', ";  // need a commas
            } else {
               sqlStr += "'" + series[i] + "'";    // no commas
            }
         }
         sqlStr += ") AND qty > 0 AND price < " + request.getParameter("price") + " ORDER BY series ASC, name ASC, price ASC";


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

         //out.println("<p>Your SQL statement is: " + sqlStr + "</p>"); // Echo for debugging
         ResultSet rset = stmt.executeQuery(sqlStr);  // Send the query to the server

         // Step 4: Process the query result
         // Print the <form> start tag
         out.println("<form method='get' action='emartorder'>");
         
         // For each row in ResultSet, print one checkbox inside the <form>

         out.println("<table class = 'table table-hover'>");
         out.println("<tr>");
         out.println("<th> </th>");
         out.println("<th>SERIES</th>");
         out.println("<th>NAME</th>");
         out.println("<th>PRICE</th>");
         out.println("</tr>");

         while(rset.next()) {
            out.println("<tr>");
            out.println("<td><input type='checkbox' name='id' value="
                  + "'" + rset.getString("id") + "' /></td>");
            out.println("<td>" + rset.getString("series") + "</td>");
            out.println("<td>" + rset.getString("name") + "</td>");
            out.println("<td>" + "$" + rset.getString("price") + "</td>");
            out.println("</tr>");
         }

         out.println("</table>");

        //print customer data entry
        out.println("<div class = m-3>");
        out.println("<p>Enter your Name: <input type='text' name='cust_name' required/></p>");
        out.println("<p>Enter your Email: <input type='email' name='cust_email' required/></p>");
        out.println("<p>Enter your Phone Number: <input type='text' name='cust_phone' required/></p>");
        out.println("<p>Enter your Username: <input type='text' name='username' required/></p>");
        out.println("</div>");

 
         // Print the submit button and </form> end-tag
         out.println("<div class = m-3>");
         out.println("<p><input type='submit' value='ORDER' />");
         out.println("</div>");
         out.println("</form>");
      } catch(Exception ex) {
         out.println("<p>Error: " + ex.getMessage() + "</p>");
         out.println("<p>Check Tomcat console for details.</p>");
         ex.printStackTrace();
      }  // Step 5: Close conn and stmt - Done automatically by try-with-resources (JDK 7)
 
      out.println("</body></html>");
      out.close();
   }
}