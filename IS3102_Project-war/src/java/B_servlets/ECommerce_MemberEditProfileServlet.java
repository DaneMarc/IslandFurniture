package B_servlets;

import HelperClasses.Member;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@WebServlet(name = "ECommerce_MemberEditProfileServlet", urlPatterns = {"/ECommerce_MemberEditProfileServlet"})
public class ECommerce_MemberEditProfileServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            Client client = ClientBuilder.newClient();
            HttpSession session = request.getSession();
            Member member = new Member();

            member.setName(request.getParameter("name"));
            member.setEmail(request.getParameter("email"));
            member.setPhone(request.getParameter("phone"));
            member.setAddress(request.getParameter("address"));
            member.setCity(request.getParameter("city"));
            member.setSecurityQuestion(Integer.parseInt(request.getParameter("securityQuestion")));
            member.setSecurityAnswer(request.getParameter("securityAnswer"));
            member.setAge(Integer.parseInt(request.getParameter("age")));
            member.setIncome(Integer.parseInt(request.getParameter("income")));

            WebTarget target = client
                    .target("http://localhost:8080/WebServices-Student/webresources/entity.memberentity")
                    .path("editMember")
                    .queryParam("email", request.getParameter("email"))
                    .queryParam("password", request.getParameter("password"));

            Invocation.Builder invocationBuilder = target.request(MediaType.APPLICATION_JSON);
            Response res = invocationBuilder.put(Entity.entity(member, MediaType.APPLICATION_JSON));
            Member newMember = (Member) res.readEntity(Member.class);
            String memberEmail = newMember.getEmail();
            session.setAttribute("member", member);
            session.setAttribute("memberEmail", memberEmail);

            response.sendRedirect("ECommerce_GetMember");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
