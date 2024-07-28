// To save as "ebookshop\WEB-INF\classes\EmartLoginServlet.java".
import java.io.*;
import java.sql.*;
import jakarta.servlet.*;            // Tomcat 10
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
//import javax.servlet.*;            // Tomcat 9
//import javax.servlet.http.*;
//import javax.servlet.annotation.*;

@WebServlet("/emartlogin")   // Configure the request URL for this servlet (Tomcat 7/Servlet 3.0 upwards)
public class EmartLoginServlet extends HttpServlet {

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
         //check if username and password are entered
         String[] username = request.getParameterValues("username");  // Returns an array of Strings
         if (username == null) {
            out.println("<h2>No username entered. Please go back to enter your username</h2><body></html>");
            return; // Exit doGet()
         }

         String[] password = request.getParameterValues("password");  // Returns an array of Strings
         if (password == null) {
            out.println("<h2>No password entered. Please go back to enter your password</h2><body></html>");
            return; // Exit doGet()
         }

        //check if record exists
         String sqlStr = "SELECT * FROM login_info WHERE username = '" + request.getParameter("username") + "' AND password = '" + request.getParameter("password") + "'";
         
         out.println("<p>Your SQL statement is: " + sqlStr + "</p>"); // Echo for debugging
         ResultSet rset = stmt.executeQuery(sqlStr);  // Send the query to the server

         //if record exists, redirect to query page. if record does not exist, redirect to login failure page.
         if (rset.next()) {
            response.sendRedirect("emartquery.html");
         } else {
            response.sendRedirect("emartloginfailure.html");
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