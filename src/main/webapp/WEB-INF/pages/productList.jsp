<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="products" type="java.util.ArrayList" scope="request"/>
<tags:master pageTitle="Product List">
  <p>
    Welcome to Expert-Soft training!
  </p>
  <c:if test="${not empty param.error}">
       <div class="error">
           ${param.error}
       </div>
  </c:if>
  <form>
    <input name="query" value="${param.query}"/>
    <button> Search </button>
  </form>
  <table>
    <thead>
      <tr>
        <td>Image</td>
        <td>Description
            <tags:sortLink field="description" order="asc"/>
            <tags:sortLink field="description" order="desc"/>
        </td>
        <td class="price">Price
            <tags:sortLink field="price" order="asc"/>
            <tags:sortLink field="price" order="desc"/>
        </td>
        <c:if test="${not empty param.query}">
            <td>
               Quantity
            </td>
            <td>
                Action
            </td>
        </c:if>
      </tr>
    </thead>
    <c:forEach var="product" items="${products}">
      <tr>
        <td>
          <img class="product-tile" src="https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/${product.imageUrl}">
        </td>
        <td><a href="products/${product.id}">${product.description} </a></td>
        <td class="price">
            <tags:priceHistory product="${product}"/>
        </td>
        <c:if test="${not empty param.query}">
           <form method="post" action="${pageContext.servletContext.contextPath}/products/${product.id}">
           <td><input name="quantity" value="1" class="quantity">
           </td>
           <td>
               <button>Buy</button>
           </td>
           </form>
        </c:if>
      </tr>
    </c:forEach>
  </table>
</tags:master>