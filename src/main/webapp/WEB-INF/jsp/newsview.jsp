<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/head.jsp"%>

<div class="content">
    <div class="container-fluid">
        <nav class="pull-left">
            <strong>当前位置:</strong>&nbsp;&nbsp;&nbsp; <span>新闻管理 &gt; 新闻详情</span>
        </nav>
    </div>

    <div class="card container-fluid">
        <div class="header">
            <h4 class="title">新闻详情 <small>News Detail</small></h4>
        </div>
        <div class="content">
            <div class="row">
                <div class="col-md-12">
                    <h3 class="text-center">${news.title}</h3>
                    <p class="text-center text-muted">
                        <small>发布时间：<fmt:formatDate value="${news.creationDate}" pattern="yyyy-MM-dd HH:mm:ss"/></small>
                        <small>最后修改：<fmt:formatDate value="${news.modifyDate}" pattern="yyyy-MM-dd HH:mm:ss"/></small>
                    </p>
                    <hr>
                    <div class="news-content">
                        ${news.content}
                    </div>
                    <hr>
                </div>
            </div>

            <div class="row">
                <div class="col-md-12">
                    <a href="${pageContext.request.contextPath}/sys/news/modify/${news.id}" class="btn btn-primary btn-fill">修改</a>
                    <a href="${pageContext.request.contextPath}/sys/news/list.html" class="btn btn-default btn-fill">返回列表</a>
                </div>
            </div>
        </div>
    </div>
</div>

<%@ include file="/WEB-INF/jsp/common/foot.jsp"%>
<script type="text/javascript">
    $(".sidebar-wrapper .nav li:eq(7)").addClass("active");
</script>