<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/head.jsp"%>

<div class="content">
  <div class="container-fluid">
    <nav class="pull-left">
      <strong>当前位置:</strong>&nbsp;&nbsp;&nbsp; <span>新闻管理</span>
    </nav>
  </div>

  <div class="card container-fluid">
    <div class="header">
      <h4 class="title">新闻列表 <small> News List</small></h4>
    </div>
    <div class="content">
      <form method="get" action="${pageContext.request.contextPath}/sys/news/list.html">
        <div class="row">
          <div class="col-md-6 form-group">
            <label for="queryTitle">标题：</label>
            <input type="text" name="queryTitle" id="queryTitle" value="${queryTitle}" class="form-control border-input" placeholder="请输入新闻标题">
          </div>
          <div class="col-md-6 form-group">
            <label>&nbsp;</label>
            <button type="submit" class="btn btn-info btn-fill btn-wd">查询</button>
            <a href="${pageContext.request.contextPath}/sys/news/add.html" class="btn btn-success btn-fill btn-wd">添加新闻</a>
          </div>
        </div>
      </form>

      <hr>

      <div class="table-responsive">
        <table class="table table-bordered table-hover">
          <thead>
          <tr>
            <th width="10%">编号</th>
            <th width="50%">标题</th>
            <th width="20%">发布时间</th>
            <th width="20%">操作</th>
          </tr>
          </thead>
          <tbody>
          <c:forEach var="news" items="${pageInfo.list}" varStatus="status">
            <tr>
              <td>${news.id}</td>
              <td><a href="${pageContext.request.contextPath}/sys/news/view/${news.id}">${news.title}</a></td>
              <td><fmt:formatDate value="${news.creationDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
              <td>
                <a href="${pageContext.request.contextPath}/sys/news/view/${news.id}" class="btn btn-xs btn-info">
                  <i class="ti-eye"></i> 查看
                </a>
                <a href="${pageContext.request.contextPath}/sys/news/modify/${news.id}" class="btn btn-xs btn-primary">
                  <i class="ti-pencil"></i> 修改
                </a>
                <a href="javascript:;" newsId="${news.id}" class="btn btn-xs btn-danger deleteNews">
                  <i class="ti-trash"></i> 删除
                </a>
              </td>
            </tr>
          </c:forEach>
          </tbody>
        </table>
      </div>

      <%@ include file="/WEB-INF/jsp/rollpage.jsp"%>
    </div>
  </div>
</div>

<%@ include file="/WEB-INF/jsp/common/foot.jsp"%>
<script type="text/javascript">
  $(".sidebar-wrapper .nav li:eq(7)").addClass("active");

  $(".deleteNews").on("click", function(){
    var newsId = $(this).attr("newsId");
    if(confirm("确定要删除该新闻吗？")){
      var $this = $(this);
      $this.html('<i class="ti-reload"></i> 删除中...').attr('disabled', true);
      $.ajax({
        type: "GET",
        url: "${pageContext.request.contextPath}/sys/news/delete.json",
        data: {id: newsId},
        dataType: "json",
        cache: false,  // 禁用缓存
        success: function(data){
          if(data.delResult == "true"){
            alert("删除成功！");
            window.location.href = "${pageContext.request.contextPath}/sys/news/list.html";
          }else if(data.delResult == "false"){
            alert("删除失败！");
            $this.html('<i class="ti-trash"></i> 删除').attr('disabled', false);
          }else{
            alert("系统错误，请稍后重试！");
            $this.html('<i class="ti-trash"></i> 删除').attr('disabled', false);
          }
        },
        error: function(xhr, status, error){
          console.error("删除请求错误:", status, error);
          console.log("响应文本:", xhr.responseText);
          alert("系统错误，请稍后重试！");
          $this.html('<i class="ti-trash"></i> 删除').attr('disabled', false);
        }
      });
    }
  });
</script>
