<%@ page import="step.learning.dall.dto.CartItem" %>
<%@ page import="step.learning.models.CartPageModel" %>
<%@ page import="step.learning.dall.dto.Product" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    String context = request.getContextPath();
    //вилучаємо дані передані контролером
    //CartItem[] cartItems = (CartItem[]) request.getAttribute("cart");
    CartPageModel model = (CartPageModel) request.getAttribute("model");
%>

<div class="row">
    <div class="col s8">
        <div class="row">
            <% for(Product product : model.getProducts()) {%>
            <div class="col s12 m4">
                <div class="card small">
                    <div class="card-image">
                        <img src="<%=context%>/img/products/<%=product.getImage() == null ? "no-image.png" : product.getImage()%>" alt="Image">
                        <a data-product="<%=product.getId()%>" class="product-card-btn btn-floating halfway-fab waves-effect waves-light red"><i class="material-icons">shopping_cart</i></a>
                    </div>
                    <div class="card-content">
                        <span class="card-title"><%=product.getName()%></span>
                        <p><%=product.getDescription()%></p>
                    </div>
                </div>
            </div>
            <%}%>
        </div>
    </div>
    <div class="col s4">
        <h1>Ваш кошик</h1>
        <%-- Відображення даних--%>
        <% for(CartItem item : model.getCartItems()) {%>
        <div class="col s12 m7">
            <h2 class="header">Card</h2>
            <div class="card horizontal">
                <div class="card-image flex1"><img src="<%=context%>/img/no-image.png" alt="img"></div>
                <div class="card-stacked flex3">
                    <div class="card-content">
                        <p><%=item.getId()%></p>
                        <p><%=item.getProductId()%></p>
                        <p><%=item.getCount()%></p></div>
                    <div class="card-action"><a href="#">видалити з кошику</a></div>
                </div>
            </div>
        </div>
        <%}%>
    </div>
</div>