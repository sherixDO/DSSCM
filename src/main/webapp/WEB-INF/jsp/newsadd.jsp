<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/head.jsp"%>

<div class="content">
  <div class="container-fluid">
    <nav class="pull-left">
      <strong>当前位置:</strong>&nbsp;&nbsp;&nbsp; <span>新闻管理 &gt; 添加新闻</span>
    </nav>
  </div>

  <div class="card container-fluid">
    <div class="header">
      <h4 class="title">添加新闻 <small>Add News</small></h4>
    </div>
    <div class="content">
      <form id="newsForm" name="newsForm" method="post" action="${pageContext.request.contextPath}/sys/news/addsave.html">
        <div class="row">
          <div class="form-group">
            <label for="title">新闻标题：</label>
            <input type="text" name="title" id="title" value="${news.title}" class="form-control border-input" placeholder="请输入新闻标题">
            <span class="help-block"></span>
          </div>
        </div>

        <div class="row">
          <div class="form-group">
            <label for="content">新闻内容：</label>
            <textarea name="content" id="content" rows="10" class="form-control border-input" placeholder="请输入新闻内容">${news.content}</textarea>
            <span class="help-block"></span>
          </div>
        </div>

        <div class="row">
          <div class="form-group">
            <button type="submit" class="btn btn-info btn-fill btn-wd">保存</button>
            <button type="button" class="btn btn-default btn-fill btn-wd" onclick="window.history.back();">返回</button>
          </div>
        </div>
      </form>
    </div>
  </div>
</div>

<%@ include file="/WEB-INF/jsp/common/foot.jsp"%>
<script type="text/javascript">
  $(".sidebar-wrapper .nav li:eq(7)").addClass("active");

  $(document).ready(function(){
    $("#newsForm").submit(function(){
      if($("#title").val() == ""){
        alert("请输入新闻标题");
        $("#title").focus();
        return false;
      }
      if($("#content").val() == ""){
        alert("请输入新闻内容");
        $("#content").focus();
        return false;
      }
      return true;
    });
  });
</script>