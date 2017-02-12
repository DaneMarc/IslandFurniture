package B_servlets;

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
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@WebServlet(name = "ECommerce_AddFurnitureToListServlet", urlPatterns = {"/ECommerce_AddFurnitureToListServlet"})
public class ECommerce_AddFurnitureToListServlet extends HttpServlet {

    ArrayList<ShoppingCartLineItem> shoppingCart = new ArrayList<>();
    private String result;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            HttpSession session = request.getSession();
            String SKU = request.getParameter("SKU");
            boolean contains = false;
            Long countryID = null;
            int itemId = 0;
            String countryIDstring = session.getAttribute("countryID").toString();
            //<editor-fold defaultstate="collapsed" desc="check countryID and SKU validity">
            if ((countryIDstring == null || countryIDstring.equals("")) && (SKU == null || SKU.equals(""))) {
                response.sendRedirect("/IS3102_Project-war/B/SG/index.jsp");
            } else if (countryIDstring == null || countryIDstring.equals("")) {
                response.sendRedirect("/IS3102_Project-war/B/SG/furnitureProductDetails.jsp?sku=" + SKU);
            }
            //</editor-fold>
            countryID = Long.parseLong(countryIDstring);
            int itemQty = getQuantity(countryID, SKU);
            
            for (int i = 0; i < shoppingCart.size(); i++) {
                if (shoppingCart.get(i).getSKU().equals(SKU)) {
                    contains = true;
                    itemId = i;
                }
            }

            if (contains) {
                if (shoppingCart.get(itemId).getQuantity() < itemQty){
                    System.out.println("here1");
                    shoppingCart.get(itemId).setQuantity(shoppingCart.get(itemId).getQuantity() + 1);
                    session.setAttribute("shoppingCart", shoppingCart);
                    result = "Item successfully added into the cart!";
                    response.sendRedirect("/IS3102_Project-war/B/SG/shoppingCart.jsp?goodMsg=" + result);
                }
                else {
                    System.out.println("here2");
                    result = "Item not added to cart, not enough quantity available";
                    response.sendRedirect("/IS3102_Project-war/B/SG/shoppingCart.jsp?errMsg=" + result);
                }
            }
            else {
                if (itemQty != 0) {
                    ShoppingCartLineItem item = new ShoppingCartLineItem();
                    item.setCountryID(countryID);
                    item.setId(Long.parseLong(request.getParameter("id")));
                    item.setImageURL(request.getParameter("imageURL"));
                    item.setName(request.getParameter("name"));
                    item.setPrice(Double.parseDouble(request.getParameter("price")));
                    item.setQuantity(1);
                    item.setSKU(SKU);
                    shoppingCart.add(item);

                    session.setAttribute("shoppingCart", shoppingCart);
                    result = "Item successfully added into the cart!";
                    response.sendRedirect("/IS3102_Project-war/B/SG/shoppingCart.jsp?goodMsg=" + result);
                }
                else {
                    result = "Item not added to cart, not enough quantity available";
                    response.sendRedirect("/IS3102_Project-war/B/SG/shoppingCart.jsp?errMsg=" + result);
                }
            }
        }
    }

    public int getQuantity(Long countryID, String SKU) {
        try {
            System.out.println("getQuantity() SKU: " + SKU + ", countryID: " + countryID);
            Client client = ClientBuilder.newClient();
            WebTarget target = client
                    .target("http://localhost:8080/WebServices-Student/webresources/entity.countryentity")
                    .path("getQuantity")
                    .queryParam("countryID", countryID)
                    .queryParam("SKU", SKU);
            Invocation.Builder invocationBuilder = target.request(MediaType.APPLICATION_JSON);
            Response response = invocationBuilder.get();
            System.out.println("status: " + response.getStatus());
            if (response.getStatus() != 200) {
                return 0;
            }
            String result = (String) response.readEntity(String.class);
            System.out.println("Result returned from ws: " + result);
            return Integer.parseInt(result);

        } catch (Exception e) {
            e.printStackTrace();
            return 0;
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
