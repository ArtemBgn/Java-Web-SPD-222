<%
    String context = request.getContextPath();
    //вилучаємо дані передані контролером
    ShareItem[] shareItems = (ShareItem[]) request.getAttribute("share");
%>
<%@ page import="step.learning.dall.dto.ShareItem" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<h1>АКЦІЯ!!!!!!!!!!!!!!!</h1>
<% for(ShareItem item : shareItems) {%>
<div class="col s12 m7">
    <h2 class="header">акція</h2>
    <div class="card horizontal">
        <div class="card-image flex1"><img src="<%=context%>/img/no-image.png" alt="img"></div>
        <div class="card-stacked flex3">
            <div class="card-content">
                <p><%=item.getProductId()%></p>
                <p><%=item.getCount()%></p>
            </div>
        </div>
    </div>
</div>
<%}%>
<span class="material-symbols-outlined">
production_quantity_limits
</span>