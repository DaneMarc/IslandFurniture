<%@page import="HelperClasses.Member"%>
<%@page import="EntityManager.CountryEntity"%>
<%@page import="EntityManager.LoyaltyTierEntity"%>
<%@page import="EntityManager.SalesRecordEntity"%>
<%@page import="java.text.DecimalFormat"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List;"%>
<jsp:include page="checkCountry.jsp" />
<html> 
    <jsp:include page="header.html" />
    <body>
        <script>
            function validatePassword() {
                var password = document.getElementById("password").value;
                var repassword = document.getElementById("repassword").value;
                var ok = true;
                if ((password != null && repassword != null) || (password != "" && repassword != "")) {
                    if (password != repassword) {
                        //alert("Passwords Do not match");
                        document.getElementById("password").style.borderColor = "#E34234";
                        document.getElementById("repassword").style.borderColor = "#E34234";
                        alert("Passwords do not match. Please key again.");
                        ok = false;
                    } else if (password == repassword) {
                        if (password.length != 0 && password.length < 8) {
                            alert("Passwords too short. At least 8 characters.");
                            ok = false;
                        }
                    }
                } else {
                    return ok;
                }
                return ok;
            }

            function pdpaWindow() {
                var myWindow = window.open("pdpa.html");
            }
        </script>
        <jsp:include page="menu2.jsp" />
        <div role="main" class="main">
            <section class="page-top">
                <div class="container">
                    <div class="row">
                        <div class="col-md-12">
                            <h2>User Profile</h2>
                        </div>
                    </div>
                </div>
            </section>
            <div class="container">
                <%%>
                <jsp:include page="/displayMessageLong.jsp" />
                <!-- /.warning -->
                <div class="col-md-12">
                    <%
                        try {
                            Member member = (Member) session.getAttribute("member");
                            DecimalFormat df = new DecimalFormat("#.##");
                    %>
                    <div class="row" style="min-height: 500px;">
                        <div class="tabs">
                            <ul class="nav nav-tabs">
                                <li class="active">
                                    <a href="#overview" data-toggle="tab"><i class="icon icon-user"></i> Overview</a>
                                </li>
                                <li>
                                    <a href="#loyaltyProgram" data-toggle="tab">Loyalty Program</a>
                                </li>
                                <li>
                                    <a href="#salesHistory" data-toggle="tab">Sales History</a>
                                </li>
                            </ul>
                            <div class="tab-content">
                                <div id="overview" class="tab-pane active">
                                    <%if (member != null) {%>
                                    <form role="form" action="../../ECommerce_MemberEditProfileServlet" onsubmit="return validatePassword()">
                                        <h4>Personal Information</h4>
                                        <div class="form-group">
                                            <label>Name</label>
                                            <input class="form-control" name="name" type="text" value="<%=member.getName()%>">
                                        </div>
                                        <div class="form-group">
					    <label>E-mail Address</label>
                                            <input class="form-control" required="true" value="<%=member.getEmail()%>" disabled/>
                                        </div>
                                        <div class="form-group">
                                            <label>Phone</label>
                                            <input class="form-control" type="text" name="phone" value="<%=member.getPhone()%>">
                                        </div>
                                        <div class="form-group">
                                            <label>Country</label>
                                            <%if (member.getCity() != null && member.getCity() != "") {%>
                                            <select name="country" class="bfh-countries">
                                                <option value="<%=member.getCity()%>"><%=member.getCity()%></option>
                                                <%} else {%>
                                                <select name="country" class="bfh-countries" data-country="SG" disabled>
                                                    <%}%>
                                                </select>
                                        </div>
                                        <div class="form-group">
                                            <label>Address</label>
                                            <input class="form-control" type="text" required="true" name="address" value="<%=member.getAddress()%>">
                                        </div>
                                        <div class="form-group">
                                            <label>Set Challenge Question</label>
                                            <select name="securityQuestion">
                                                <%int securityQn = 0;
                                                    if (member.getSecurityQuestion() == null) {
                                                        securityQn = 0;
                                                    } else {
                                                        securityQn = member.getSecurityQuestion();
                                                    }%>
                                                <option value="1" <%if (securityQn == 1) {
                                                        out.println("selected");
                                                    }%>>What is your mother's maiden name?</option>
                                                <option value="2" <%if (securityQn == 2) {
                                                        out.println("selected");
                                                    }%>>What is your first pet's name?</option>
                                                <option value="3" <%if (securityQn == 3) {
                                                        out.println("selected");
                                                    }%>>What is your favourite animal?</option>
                                            </select>
                                            <input class="form-control" type="text" required="true" name="securityAnswer" value="<%if (member.getSecurityAnswer() == null) {
                                                    out.println("");
                                                } else {
                                                    out.println(member.getSecurityAnswer());
                                                }%>">
                                        </div>
                                        <div class="form-group">
                                            <label>Age</label>
                                            <input class="form-control" name="age" step="1" type="number" min="1" max="150" value="<%=member.getAge()%>">
                                        </div>
                                        <div class="form-group">
                                            <label>Income per annum (in USD)</label>
                                            <input class="form-control" name="income" step="1" type="number" min="0" max="2147483646" value="<%=df.format(member.getIncome())%>">
                                        </div>
                                        <div class="form-group">
                                            <input type="checkbox" name="serviceLevelAgreement"> Allow us to use your particulars to serve you better?<br/>Checking the box above indicates that you agree to our <a onclick="pdpaWindow()">personal data protection policy.</a>
                                        </div>
                                        <hr class="more-spaced "/>
                                        <h4>Change Password</h4>
                                        <div class="form-group">
                                            <label>New Password (leave blank unless setting a new password).<br/>Password to be at least 8 characters long.</label>
                                            <input class="form-control" type="password" name="password" id="password">
                                        </div>
                                        <div class="form-group">
                                            <label>Re-enter New Password</label>
                                            <input class="form-control" type="password"  name="repassword" id="repassword">
                                        </div>
                                        <div class="panel-footer" style="padding-bottom: 0px;">
                                            <div class="row">
                                                <div class="form-group">
                                                    <input type="submit" value="Submit" class="btn btn-primary"/>
                                                    <input type="reset" value="Reset" class="btn btn-primary"/>
                                                </div>
                                                <input type="hidden" value="<%=member.getEmail()%>" name="email"/>
                                            </div>
                                        </div>
                                    </form>
                                    <%}%>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <%
                        session.removeAttribute("member");
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        response.sendRedirect("index.jsp");
                    }%>
            </div>
        </div>
    </div>
    
    <!-- Current Page JS -->
    <script src="../../vendor/formhelpers/bootstrap-formhelpers-countries.js"></script>
    <script src="../../vendor/formhelpers/bootstrap-formhelpers.min.js"></script>
    <jsp:include page="footer.html" />
</body>
</html>