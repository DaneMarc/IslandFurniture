<%@page import="HelperClasses.RetailProduct"%>
<%@page import="EntityManager.PromotionEntity"%>
<%@page import="EntityManager.Item_CountryEntity"%>
<%@page import="java.util.List"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:include page="checkCountry.jsp" />
<html> <!--<![endif]-->
    <jsp:include page="header.html" />
    <body>
        <%
            List<RetailProduct> retailProducts = (List<RetailProduct>) (session.getAttribute("retailProducts"));
            //System.out.println("test");
        %>
        <div class="body">
            <jsp:include page="menu2.jsp" />
            <div class="body">
                <div role="main" class="main">
                    <section class="page-top">
                        <div class="container">
                            <div class="row">
                                <div class="col-md-12">
                                    <h2>Retail Products</h2>
                                </div>
                            </div>
                        </div>
                    </section>
                    <div class="container">
                        <div class="row">
                            <div class="col-md-6">
                                <h2 class="shorter"><strong>All Retail Products</strong></h2>
                            </div>
                        </div>
                        <div class="row">
                            <ul class="products product-thumb-info-list" data-plugin-masonry>
                                <%
                                    try {
                                %>
                                <li class="col-md-3 col-sm-6 col-xs-12 product">
                                    <span class="product-thumb-info">
                                        <span class="product-thumb-info-image">
                                            <img alt="" class="img-responsive" src="../../..<%=retailProducts.get(0).getImageUrl()%>">
                                        </span>
                                        <span class="product-thumb-info-content">
                                            <h4><%=retailProducts.get(0).getName()%></h4>
                                            <%
                                                String normalPrice = "$" + retailProducts.get(0).getPrice() + "0";
                                            %>
                                            <span class="product-thumb-info-act-left"><em>Price: <%=normalPrice%></em></span>
                                            <br/>
                                            <form action="retailProductDetails.jsp">
                                                <input type="hidden" name="sku" value="<%=retailProducts.get(0).getSKU()%>"/>
                                                <input type="submit" class="btn btn-primary btn-block" value="More Details"/>
                                            </form>
                                        </span>
                                    </span>
                                </li>
                                <%
                                    } catch (Exception ex) {
                                        System.out.println(ex);
                                    }
                                %>

                            </ul>
                        </div>
                        <hr class="tall">
                    </div>
                </div>
            </div>
            <jsp:include page="footer.html" />
        </div>
    </body>
</html>
