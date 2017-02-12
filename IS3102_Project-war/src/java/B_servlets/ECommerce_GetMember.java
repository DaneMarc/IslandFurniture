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
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@WebServlet(name = "ECommerce_GetMember", urlPatterns = {"/ECommerce_GetMember"})
public class ECommerce_GetMember extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {

            HttpSession session = request.getSession();
            String memberEmail = session.getAttribute("memberEmail").toString();

            Client myClient = ClientBuilder.newClient();
            WebTarget myTarget = myClient
                    .target("http://localhost:8080/WebServices-Student/webresources/entity.memberentity")
                    .path("getMember")
                    .queryParam("email", memberEmail);
            Invocation.Builder invocationBuilder = myTarget.request(MediaType.APPLICATION_JSON);
            Response res = invocationBuilder.get();

            Member member = (Member) res.readEntity(Member.class);

            if (res.getStatus() == 200) {
                if (session.getAttribute("memberName") != null) {
                    if (session.getAttribute("member") != null) {
                        session.setAttribute("member", member);
                        session.removeAttribute("memberName");
                        session.setAttribute("memberName", member.getName());
                        String resultString = "Account updated successfully";
                        response.sendRedirect("/IS3102_Project-war/B/SG/memberProfile.jsp?goodMsg=" + resultString);
                    } else {
                        session.setAttribute("member", member);
                        response.sendRedirect("/IS3102_Project-war/B/SG/memberProfile.jsp");
                    }
                } else {
                    session.setAttribute("memberName", member.getName());
                    response.sendRedirect("/IS3102_Project-war/B/SG/index.jsp");

                }
            }
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
