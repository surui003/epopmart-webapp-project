// To save as "ebookshop\WEB-INF\classes\EmartRegistrationServlet.java".
import java.io.*;
import java.sql.*;
import jakarta.servlet.*;            // Tomcat 10
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
//import javax.servlet.*;            // Tomcat 9
//import javax.servlet.http.*;
//import javax.servlet.annotation.*;

@WebServlet("/emartregistration")   // Configure the request URL for this servlet (Tomcat 7/Servlet 3.0 upwards)
public class EmartRegistrationServlet extends HttpServlet {

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

        //check if account already exists
         String sqlStr_account = "SELECT * FROM login_info WHERE username = '" + request.getParameter("username") + "' AND password = '" 
         + request.getParameter("password") + "'";
         
         //out.println("<p>Your SQL statement is: " + sqlStr_account + "</p>"); // Echo for debugging
         ResultSet rset_account = stmt.executeQuery(sqlStr_account);  // Send the query to the server

         //if account exists, redirect to login page. if account does not exist, create account and redirect to login page.
         if (rset_account.next()) {
            response.sendRedirect("emartlogin_haveaccount.html");
         } else {
            //add account into login information
            String sqlStr_add = "INSERT INTO login_info VALUES ('"
                     + request.getParameter("username") + "', " + "'" + request.getParameter("password") + "')";
               //out.println("<p>" + sqlStr_add + "</p>");  // for debugging
               stmt.executeUpdate(sqlStr_add);
            response.sendRedirect("emartlogin_createaccount.html");
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