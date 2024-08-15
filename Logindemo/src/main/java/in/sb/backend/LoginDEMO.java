package in.sb.backend;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/loginser")
public class LoginDEMO extends HttpServlet {

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("text/html");
        PrintWriter out = res.getWriter();

        String myemail = req.getParameter("email");
        String mypassword = req.getParameter("password");

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/yt_demo", "root", "tiger");
            ps = con.prepareStatement("SELECT * FROM register WHERE email=? AND password=?");
            ps.setString(1, myemail);
            ps.setString(2, mypassword);

            rs = ps.executeQuery();
            if (rs.next()) {
                HttpSession session = req.getSession();
                session.setAttribute("session_name", rs.getString("name"));
                RequestDispatcher rd = req.getRequestDispatcher("/profile.jsp");
                rd.forward(req, res); // Forward to profile.jsp
            } else {
                res.setContentType("text/html");
                out.println("<h3 style='color:red'>Email and password didn't match!</h3>");
                RequestDispatcher rd = req.getRequestDispatcher("/login.jsp");
                rd.include(req, res); // Include login.jsp
            }
        } catch (Exception e) {
            res.setContentType("text/html");
            out.println("<h3 style='color:red'>Exception occurred: " + e.getMessage() + "</h3>");
            RequestDispatcher rd = req.getRequestDispatcher("/login.jsp");
            rd.include(req, res); // Include login.jsp
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (con != null) con.close();
            } catch (Exception e) {
                out.print("<h3 style='color:red'>Exception occurred while closing resources: " + e.getMessage() + "</h3>");
            }
            out.close();
        }
    }
}
