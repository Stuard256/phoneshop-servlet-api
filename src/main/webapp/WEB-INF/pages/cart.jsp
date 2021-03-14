<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="cart" type="com.es.phoneshop.entity.cart.Cart" scope="request"/>
<tags:master pageTitle="Cart">
<form method="post" action="${pageContext.servletContext.contextPath}/cart">
    <div>
        Cart: ${cart} , total Quantity = ${cart.totalQuantity}
    </div>
    <c:if test="${not empty param.message}">
       <div class="success">
         ${param.message}
       </div>
    </c:if>
    <c:if test="${not empty errors}">
    <div class="error">
      There were some errors
    </div>
  </c:if>
  <table>
    <thead>
      <tr>
        <td>Image</td>
        <td>Description </td>
        <td>
            Price
        </td>
        <td>
            Stock
        </td>
        <td>
             Quantity
        </td>
        <td>
        </td>
      </tr>
    </thead>
    <c:forEach var="item" items="${cart.items}" varStatus="status">
      <tr>
        <td>
          <img class="product-tile" src="https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/${item.product.imageUrl}">
        </td>
        <td><a href="products/${item.product.id}">${item.product.description} </a></td>
        <td class="price">
            <fmt:formatNumber value='${item.product.price}' type="currency" currencySymbol="${item.product.currency.symbol}"/>
        </td>
        <td>
            ${item.product.stock}
        </td>
        <td>
           <c:set var="error" value="${errors[item.product.id]}"></c:set>
           <input name="quantity" value="${not empty error ? paramValues['quantity'][status.index] : item.quantity }" class="quantity"/>
             <c:if test="${not empty error}">
                <div class="error">
                    ${errors[item.product.id]}
                </div>
             </c:if>
           <input name="productId" type="hidden" value="${item.product.id}"/>
        </td>
        <td>
            <button form="deleteCartItem"
                    formaction="${pageContext.servletContext.contextPath}/cart/deleteCartItem/${item.product.id}"> X </button>
        </td>
      </tr>
    </c:forEach>
    <tr>
        <td>
            Total cost
        </td>
        <td>
            ${cart.totalCost}
        </td>
    </tr>
  </table>
  <p>
      <button>
       Update
      </button>
  </p>
</form>
<form id="deleteCartItem" method="post">
</form>
<form action="${pageContext.servletContext.contextPath}/checkout">
    <button> Checkout </button>
</form>

</tags:master>