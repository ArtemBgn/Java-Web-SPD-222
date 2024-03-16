<%@ page import="step.learning.dall.dto.CartItem" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    String context = request.getContextPath();
    //вилучаємо дані передані контролером
    CartItem[] cartItems = (CartItem[]) request.getAttribute("cart");
%>

<h1>Ваш кошик</h1>
<%-- Відображення даних--%>
<% for(CartItem item : cartItems) {%>
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