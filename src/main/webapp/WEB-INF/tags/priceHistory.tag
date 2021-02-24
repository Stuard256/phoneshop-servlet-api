<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@tag trimDirectiveWhitespaces="true" %>
<%@attribute name="product" required="true" type="com.es.phoneshop.model.product.entity.Product" %>
<c:set var="priceHistory" value=''/>
<c:forEach var="priceAndDate"
           items="${product.getPriceHistory()}">
    <c:set var="priceHistory"
           value='Since ${priceAndDate.getDate()}: ${priceAndDate.getPrice()} ${product.currency.symbol}\n${priceHistory}'/>
</c:forEach>
<button onclick="alert('${priceHistory}'); ">
    <fmt:formatNumber value='${product.price}' type="currency"
                      currencySymbol="${product.currency.symbol}"/>
</button>