<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ include file="/resources/commons/header.jsp" %>

<body>
<div class="container">
    <!-- Top영역 -->
    <%@ include file="/resources/commons/top.jsp" %>
    <!-- Body영역 -->
    <div class="row">
        <!-- Left메뉴영역 -->
        <%@ include file="/resources/commons/left.jsp" %>
        <!-- Contents영역 -->
        <div class="col-9 pt-3">
            <h3>게시판 내용보기 - <small>자유게시판</small></h3>
            
            <form>
                <table class="table table-bordered">
                <colgroup>
                    <col width="20%"/>
                    <col width="30%"/>
                    <col width="20%"/>
                    <col width="*"/>
                </colgroup>
                <tbody>
                    <tr>
                        <th class="text-center" style="vertical-align:middle;">작성자</th>
                        <td>${viewRow.name }</td>	
                        <th class="text-center" style="vertical-align:middle;">작성일</th>
                        <td>${viewRow.postdate }</td>	
                    </tr>
                    <tr>
                        <th class="text-center" style="vertical-align:middle;">이메일</th>
                        <td>
                            nakjasabal@naver.com
                        </td>
                        <th class="text-center" style="vertical-align:middle;">조회수</th>
                        <td>${viewRow.hits }</td>
                    </tr>
                    <tr>
                        <th class="text-center" style="vertical-align:middle;">제목</th>
                        <td colspan="3">${viewRow.title }</td>	
                    </tr>
                    <tr>
                        <th class="text-center" style="vertical-align:middle;">내용</th>
                        <td colspan="3" style="height:150px;">
							${viewRow.contents }
						</td>
                    </tr>
                </tbody>
                </table>
				
                <!-- 각종버튼 -->
                <div class="row mb-3">
                    <div class="col d-flex justify-content-end">
                        <button type="button" class="btn btn-secondary" 
                        	onclick="location.href='./password.do?idx=${viewRow.idx}&mode=edit&nowPage=${param.nowPage }';">수정하기</button>
                        <button type="button" class="btn btn-success" 
                        	onclick="location.href='./password.do?idx=${viewRow.idx}&mode=delete&nowPage=${param.nowPage }';" >삭제하기</button>
                        <button type="button" class="btn btn-warning" 
                        	onclick="location.href='./list.do?nowPage=${param.nowPage}';">목록보기</button>
                    </div>
                </div>
            </form>
        </div>
    </div>
    <!-- Copyright영역 -->
    <%@ include file="/resources/commons/copyright.jsp" %>
</div>
</body>
</html>