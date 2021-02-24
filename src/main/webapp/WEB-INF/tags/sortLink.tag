<%@ tag trimDirectiveWhitespaces="true" %>
<%@ attribute name="field" required="true" %>
<%@ attribute name="order" required="true" %>

<a href="?query=${param.query}&field=${field}&order=${order}"
 style="${field eq param.field and order eq param.order ? 'font-weight: bold' : ''}">${order}</a>
