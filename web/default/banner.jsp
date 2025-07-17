<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!-- CSS riêng cho banner -->
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/banner.css" />

<section class="hero-banner">
    <div class="hero-content">
        <div class="hero-overlay"></div>
        <img class="hero-image" src="${pageContext.request.contextPath}/images/Banner.jpg" alt="Banner">
        <div class="hero-text">
            <c:choose>
                <c:when test="${not empty param.bannerTitle}">
                    <h1>${param.bannerTitle}</h1>
                </c:when>
                <c:otherwise>
                    <h1>REGISTRATION LIST</h1>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</section>

