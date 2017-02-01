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

@WebServlet(name = "ECommerce_RemoveItemFromListServlet", urlPatterns = {"/ECommerce_RemoveItemFromListServlet"})
public class ECommerce_RemoveItemFromListServlet extends HttpServlet {

    ArrayList<ShoppingCartLineItem> shoppingCart = new ArrayList<>();
    private String result;
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            HttpSession session = request.getSession();
            int count = 0, id = 0;
            String[] SKUs = request.getParameterValues("delete");
            System.out.println("size: " + SKUs.length);
            shoppingCart = (ArrayList<ShoppingCartLineItem>)session.getAttribute("shoppingCart");
            
            for (String SKU : SKUs){
                for (ShoppingCartLineItem item : shoppingCart){
                    if (item.getSKU().equals(SKU))
                        id = shoppingCart.indexOf(item);
                }
                shoppingCart.remove(id);
                count++;
            }
            
            session.setAttribute("shoppingCart", shoppingCart);
            result = "Successfully removed: " + count + " record(s)";
            response.sendRedirect("/IS3102_Project-war/B/SG/shoppingCart.jsp?goodMsg=" + result);
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
