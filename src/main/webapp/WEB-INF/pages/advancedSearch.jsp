<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<tags:master pageTitle="Advanced Search">
  <c:if test="${not empty error}">
       <div class="error">
           ${error}
       </div>
       </br>
  </c:if>
  <form>
    <h2> Advanced Search </h2>
    <table>
            <tr>
                <td>Description:</td>
                <td><input name="query" value="${param.query}"/></td>
                <td>
                    <select name="searchOption">
                        <option>all words</option>
                        <option>any word</option>
                    </select>
                </td>
            </tr>
            <tr>
                <td>Min price:</td>
                <td><input name="minPrice" value="${param.minPrice}"/></td>
            </tr>
            <tr>
                 <td>Max price:</td>
                 <td><input name="maxPrice" value="${param.maxPrice}"/></td>
            </tr>
    </table>
    <button> Search </button>
  </form>
  <c:if test="${param.query != null && error == null}">
    <jsp:useBean id="products" type="java.util.ArrayList" scope="request"/>
      <table>
        <thead>
          <tr>
            <td>Image</td>
            <td>Description</td>
            <td >Price </td>
          </tr>
        </thead>
        <c:forEach var="product" items="${products}">
          <tr>
            <td>
              <img class="product-tile" src="https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/${product.imageUrl}">
            </td>
            <td><a href="products/${product.id}">${product.description} </a></td>
            <td class="price">
                <fmt:formatNumber value='${product.price}' type="currency" currencySymbol="${product.currency.symbol}"/>
            </td>
          </tr>
        </c:forEach>
      </table>
  </c:if>
</tags:master>