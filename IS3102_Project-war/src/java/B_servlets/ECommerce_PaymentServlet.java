/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package B_servlets;

import HelperClasses.Currency_Storeid;
import HelperClasses.LineItem;
import HelperClasses.Member;
import HelperClasses.SaleRecord;
import HelperClasses.ShoppingCartLineItem;
import com.stripe.Stripe;
import com.stripe.exception.APIConnectionException;
import com.stripe.exception.AuthenticationException;
import com.stripe.exception.CardException;
import com.stripe.exception.InvalidRequestException;
import com.stripe.exception.RateLimitException;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;

import com.stripe.net.RequestOptions;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
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
            String result = "";
            HttpSession session;
            session = request.getSession();
            Member member = new Member();
            member = (Member) session.getAttribute("member");
            ArrayList<ShoppingCartLineItem> shoppingcart = (ArrayList<ShoppingCartLineItem>) session.getAttribute("shoppingCart");
            long mid = member.getId();
            double paid = Double.parseDouble(request.getParameter("finalPrice"));
            long cid = Long.parseLong(session.getAttribute("countryID").toString());

            Client csClient = ClientBuilder.newClient();
            WebTarget csTarget = csClient
                    .target("http://localhost:8080/WebServices-Student/webresources/commerce")
                    .path("getCurrencyAndStoreId")
                    .queryParam("countryId", cid);
            Invocation.Builder csIb = csTarget.request(MediaType.APPLICATION_JSON);
            Response csRes = csIb.get();
            Currency_Storeid csid = csRes.readEntity(Currency_Storeid.class);

            //--------------------------Credit card charge-----------------
            String SECRET_KEY = "sk_test_Ay5rGtpsfSjr3JpWcTOX6XyB";
            Stripe.apiKey = SECRET_KEY;

            String token = request.getParameter("stripeToken");
            int paidInCents = (int) paid * 100;
            Map<String, Object> params = new HashMap<>();
            params.put("amount", paidInCents);
            params.put("currency", csid.getCurrency());
            params.put("description", "SEP CA5 - Charge");
            params.put("source", token);

            RequestOptions options = RequestOptions
                    .builder()
                    .setIdempotencyKey(token)
                    .build();

            Charge charge = Charge.create(params, options);
            if (charge.getPaid()) {
                System.out.println("charge successful");

                //------------End of Charging--------------
                SaleRecord sr = new SaleRecord(mid, paid, cid, csid.getCurrency(), csid.getSaleRecordId());
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

                    if (lineres.getStatus() == 201) {

                        result = "Thank you for shopping at Island Furniture. You have checkout successfully!";

                    }
                }
                shoppingcart.clear();
                session.setAttribute("shoppingCart", shoppingcart);
                response.sendRedirect("/IS3102_Project-war/B/SG/shoppingCart.jsp?goodMsg=" + result);
            } else {
                result = "Error. Transaction was not approved. Please try again later";
                response.sendRedirect("/IS3102_Project-war/B/SG/shoppingCart.jsp?errMsg=" + result);

            }

        } catch (CardException e) {
            // Since it's a decline, CardException will be caught
            System.out.println("Status is: " + e.getCode());
            System.out.println("Message is: " + e.getMessage());
        } catch (RateLimitException e) {
            // Too many requests made to the API too quickly
            System.out.println("Too many payment request is made at once.");
        } catch (InvalidRequestException e) {
            // Invalid parameters were supplied to Stripe's API
            System.out.println("Invalid Stripe Parameters.");
        } catch (AuthenticationException e) {
            // Authentication with Stripe's API failed
            // (maybe you changed API keys recently)
            System.out.println("API Authentication Error.");
        } catch (APIConnectionException e) {
            // Network communication with Stripe failed
            System.out.println("Network communication with Stripe failed.");
        } catch (StripeException e) {
            // Display a very generic error to the user, and maybe send
            // yourself an email
            System.out.println("We encountered difficulties processing your request. \nPlease try again.");
        } catch (Exception e) {
            // Something else happened, completely unrelated to Stripe
            System.out.println("Unexpected error occured!");
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
