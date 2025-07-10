<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!-- CSS riêng cho banner -->
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/banner.css" />

<!-- Hero Banner -->
<section class="hero-banner">
    <div class="hero-content">
        <div class="hero-overlay"></div>
        <img class="hero-image" src="${pageContext.request.contextPath}/images/Banner.jpg" alt="Banner">
        <div class="hero-text">
            <h1>${bannerTitle != null ? bannerTitle : "REGISTRATION LIST"}</h1>
        </div>
    </div>
</section>
