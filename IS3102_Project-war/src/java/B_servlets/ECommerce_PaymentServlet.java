/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package B_servlets;

import HelperClasses.LineItem;
import HelperClasses.Member;
import HelperClasses.SaleRecord;
import HelperClasses.ShoppingCartLineItem;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
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

/**
 *
 * @author user
 */
@WebServlet(name = "ECommerce_PaymentServlet", urlPatterns = {"/ECommerce_PaymentServlet"})
public class ECommerce_PaymentServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            HttpSession session;
            session = request.getSession();
            //--------------------------Session Data-----------------------------------
//            Member member = new Member();
//            member = (Member) session.getAttribute("member");
//            ArrayList<ShoppingCartLineItem> shoppingcart = (ArrayList<ShoppingCartLineItem>) session.getAttribute("shoppingCart");
//            long mid = member.getId();
//            double paid = 10.0;
//            long cid = Long.parseLong(session.getAttribute("countryID").toString());
            //--------------------------End of Session Data-----------------------------------

//            ---------------------------Test Data------------------------------------
            ArrayList<ShoppingCartLineItem> shoppingcart = new ArrayList<>();
            ShoppingCartLineItem s = new ShoppingCartLineItem();
            s.setId(71);
            s.setQuantity(10);
            s.setCountryID(25);
            s.setSKU("F_TD_02");
            shoppingcart.add(s);

            long mid = 8351;
            double paid = 10.0;
            long cid = 25;
//            ---------------------------End of Test Data------------------------------------

            SaleRecord sr = new SaleRecord(mid, paid, cid);
            Client client = ClientBuilder.newClient();
            WebTarget target = client
                    .target("http://localhost:8080/WebServices-Student/webresources/commerce")
                    .path("createECommerceTransactionRecord");

            Invocation.Builder invocationBuilder = target.request(MediaType.APPLICATION_JSON);
            Response res = invocationBuilder.put(Entity.entity(sr, MediaType.APPLICATION_JSON));

            String output = res.readEntity(String.class);
            long saleid = Long.parseLong(output);

            for (ShoppingCartLineItem sc : shoppingcart) {
                LineItem li = new LineItem(saleid, sc.getId(), sc.getQuantity(), sc.getCountryID(), sc.getSKU());
                Client lineclient = ClientBuilder.newClient();
                WebTarget linetarget = lineclient
                        .target("http://localhost:8080/WebServices-Student/webresources/commerce")
                        .path("createECommerceLineItemRecord");

                Invocation.Builder ib = linetarget.request(MediaType.APPLICATION_JSON);
                Response lineres = ib.put(Entity.entity(li, MediaType.APPLICATION_JSON));
                String result = "";
                if (lineres.getStatus() == 201) {

                    result = "Item successfully added into the cart!";
                    response.sendRedirect("/IS3102_Project-war/B/SG/shoppingCart.jsp?goodMsg=" + result);
                } else {

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
