<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<nav aria-label="Page navigation" class="text-right">
	<ul class="pagination">
		<li><a href="javascript:page_nav(document.getElementById('newsSearchForm'),${pi.pageNum==1?1:pi.prePage});"
			aria-label="Previous"> <span aria-hidden="true">&laquo;</span> </a>
		</li>

		<c:if test="${pi.pageNum>4}">
			<li><a aria-label="Previous" href="javascript:page_nav(document.getElementById('newsSearchForm'),1);" > 
				<span aria-hidden="true">1</span> </a></li>
		</c:if>
		<c:if test="${pi.pageNum>5}">
			<li><a aria-label="Previous"> <span aria-hidden="true">...</span>
			</a></li>
		</c:if>
		<c:forEach begin="${pi.pageNum-3<=0?1:pi.pageNum-3}"
			end="${pi.pageNum+3>pi.pages ? pi.pages : pi.pageNum+3}"
			var="i">
			<li <c:if test="${pi.pageNum==i}">class="active"</c:if>><a
			    href="javascript:page_nav(document.getElementById('newsSearchForm'),${i});" >${i}</a>
			</li>
		</c:forEach>

		<c:if test="${pi.pageNum+4<pi.pages}">
			<li><a aria-label="Previous"> <span aria-hidden="true">...</span>
			</a></li>
		</c:if>
		<c:if test="${pi.pageNum+3<pi.pages}">
			<li><a aria-label="Previous"
			href="javascript:page_nav(document.getElementById('newsSearchForm'),${pi.pages});" > <span
					aria-hidden="true">${pi.pages}</span> </a></li>
		</c:if>
		<li><a href="javascript:page_nav(document.getElementById('newsSearchForm'),${pi.pageNum==pi.pages?pi.pages:pi.nextPage});"
			aria-label="Next"> <span aria-hidden="true">&raquo;</span> </a></li>
	</ul>
</nav>

<script type="text/javascript">
function page_nav(frm, num) {
    if(frm) {
        frm.pageIndex.value = num;
        frm.submit();
    }
}
</script>
