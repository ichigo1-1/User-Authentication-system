package in.sb.backend;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/regform")
public class RegistrationServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = resp.getWriter();

        String myname = req.getParameter("name");
        String myemail = req.getParameter("email");
        String mypassword = req.getParameter("password");
        String mygender = req.getParameter("gender");
        String mycity = req.getParameter("city");

        Connection con = null;
        PreparedStatement ps = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/yt_demo", "root", "tiger");
            ps = con.prepareStatement("INSERT INTO register (name, email, password, gender, city) VALUES (?, ?, ?, ?, ?)");
            ps.setString(1, myname);
            ps.setString(2, myemail);
            ps.setString(3, mypassword); // Storing password in plain text
            ps.setString(4, mygender);
            ps.setString(5, mycity);

            int count = ps.executeUpdate();
            if (count > 0) {
                resp.setContentType("text/html");
                out.println("<h3 style='color:green'>User Registered Successfully!</h3>");
                RequestDispatcher rd = req.getRequestDispatcher("/register.jsp");
                rd.include(req, resp); // Include register.jsp
            }
        } catch (Exception e) {
            resp.setContentType("text/html");
            out.println("<h3 style='color:red'>Exception occurred: " + e.getMessage() + "</h3>");
            RequestDispatcher rd = req.getRequestDispatcher("/register.jsp");
            rd.include(req, resp); // Include register.jsp
        } finally {
            try {
                if (ps != null) ps.close();
                if (con != null) con.close();
            } catch (Exception e) {
                out.print("<h3 style='color:red'>Exception occurred while closing resources: " + e.getMessage() + "</h3>");
            }
            out.close();
        }
    }
}
