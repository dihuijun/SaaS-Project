<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ include file="../../base.jsp"%>
<!DOCTYPE html>
<html>

<head>
    <!-- 页面meta -->
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>数据 - AdminLTE2定制版</title>
    <meta name="description" content="AdminLTE2定制版">
    <meta name="keywords" content="AdminLTE2定制版">
    <!-- Tell the browser to be responsive to screen width -->
    <meta content="width=device-width,initial-scale=1,maximum-scale=1,user-scalable=no" name="viewport">
</head>
<body>
<div id="frameContent" class="content-wrapper" style="margin-left:0px;">
    <!-- 内容头部 -->
    <section class="content-header">
        <h1>
            货运管理
            <small>新增装箱单</small>
        </h1>
        <ol class="breadcrumb">
            <li><a href="all-admin-index.html"><i class="fa fa-dashboard"></i> 首页</a></li>
            <li><a href="all-order-manage-list.html">货运管理</a></li>
            <li class="active">新增装箱单</li>
        </ol>
    </section>
    <!-- 正文区域 -->
    <section class="content">
        <form action="/cargo/packing/edit.do" method="post">
            <div class="panel panel-default">
                <div class="panel-heading">对【${id}】装箱</div>
                <input type="hidden" name="exportIds" value="${id}">
                <div class="row data-type" style="margin: 0px">
                    <div class="col-md-2 title">卖方</div>
                    <div class="col-md-4 data">
                        <input type="text" class="form-control" placeholder="卖方" name="seller" value="${packing.seller}"/>
                    </div>

                    <div class="col-md-2 title">买方</div>
                    <div class="col-md-4 data">
                        <input type="text" class="form-control" placeholder="买方" name="buyer" value="${packing.buyer}"/>
                    </div>

                    <div class="col-md-2 title">发票号</div>
                    <div class="col-md-4 data">
                        <input type="text" class="form-control" placeholder="发票号" name="invoice_no" value="${packing.invoice_no}"/>
                    </div>

                    <div class="col-md-2 title">发票日期</div>
                    <div class="col-md-4 data">
                        <input type="text" name="invoice_date" class="form-control" placeholder="发票日期"value="${packing.invoice_date}"/>
                    </div>

                    <div class="col-md-2 title">唛头</div>
                    <div class="col-md-4 data">
                        <input type="text" name="marks" class="form-control" placeholder="唛头" value="${packing.marks}"/>
                    </div>

                    <div class="col-md-2 title">描述</div>
                    <div class="col-md-4 data">
                        <input type="text" name="descriptions" class="form-control" placeholder="描述" value="${packing.descriptions}">
                    </div>
                </div>
            </div>
            <!--工具栏-->
            <div class="box-tools text-center">
                <button type="submit"  class="btn bg-maroon">保存</button>
                <button type="button" class="btn bg-default" onclick="history.back(-1);">返回</button>
            </div>
        </form>
        <!--工具栏/-->
    </section>
</div>
<!-- 内容区域 /-->
</body>

</html>