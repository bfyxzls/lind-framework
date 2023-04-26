<!DOCTYPE html>
<html>
<head>
  	<#import "../common/common.macro.ftl" as netCommon>
	<@netCommon.commonStyle />
	<!-- DataTables -->
  	<link rel="stylesheet" href="${request.contextPath}/static/adminlte/bower_components/datatables.net-bs/css/dataTables.bootstrap.min.css">
    <title>${I18n.admin_name}</title>
</head>
<body class="hold-transition skin-blue sidebar-mini <#if cookieMap?exists && cookieMap["xxljob_adminlte_settings"]?exists && "off" == cookieMap["xxljob_adminlte_settings"].value >sidebar-collapse</#if>">
<div class="wrapper">
	<!-- header -->
	<@netCommon.commonHeader />
	<!-- left -->
	<@netCommon.commonLeft "user" />

	<!-- Content Wrapper. Contains page content -->
	<div class="content-wrapper">
		<!-- Content Header (Page header) -->
		<section class="content-header">
			<h1>${I18n.permission_manage}</h1>
		</section>

		<!-- Main content -->
	    <section class="content">

	    	<div class="row">

                <div class="col-xs-3">
                    <div class="input-group">
                        <span class="input-group-addon">名称</span>
                        <input type="text" class="form-control" id="name" autocomplete="on" >
                    </div>
                </div>
	            <div class="col-xs-1">
	            	<button class="btn btn-block btn-info" id="searchBtn">${I18n.system_search}</button>
	            </div>
	            <div class="col-xs-2">
	            	<button class="btn btn-block btn-success add" type="button">${I18n.permission_add}</button>
	            </div>
          	</div>

			<div class="row">
				<div class="col-xs-12">
					<div class="box">
			            <div class="box-body" >
			              	<table id="list" class="table table-bordered table-striped" width="100%" >
				                <thead>
					            	<tr>
                                        <th name="id">ID</th>
                                        <th name="name">名称</th>
					                  	<th name="permissionCode">授权码</th>
                                        <th name="path">路径</th>
					                  	<th name="type">类型</th>
					                  	<th>${I18n.system_opt}</th>
					                </tr>
				                </thead>
				                <tbody></tbody>
				                <tfoot></tfoot>
							</table>
						</div>
					</div>
				</div>
			</div>
	    </section>
	</div>

	<!-- footer -->
	<@netCommon.commonFooter />
</div>

<!-- 新增.模态框 -->
<div class="modal fade" id="addModal" tabindex="-1" role="dialog"  aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
            	<h4 class="modal-title" >${I18n.permission_add}</h4>
         	</div>
			<div class="modal-body">
				<form class="form-horizontal form" role="form" >
					<div class="form-group">
						<label for="lastname" class="col-sm-2 control-label">名称<font color="red">*</font></label>
						<div class="col-sm-8"><input type="text" class="form-control" name="name" placeholder="${I18n.system_please_input}" maxlength="20" ></div>
					</div>
					<div class="form-group">
						<label for="lastname" class="col-sm-2 control-label">路径<font color="red">*</font></label>
						<div class="col-sm-8"><input type="text" class="form-control" name="path" placeholder="${I18n.system_please_input}" maxlength="20" ></div>
					</div>
					<div class="form-group">
						<label for="lastname" class="col-sm-2 control-label">类型<font color="red">*</font></label>
						<div class="col-sm-10">
							<input type="radio" name="type" value="0" />菜单
							&nbsp;&nbsp;&nbsp;&nbsp;
							<input type="radio" name="type" value="1" />按钮
						</div>
					</div>
					<div class="form-group">
						<label for="lastname" class="col-sm-2 control-label">授权码<font color="red">*</font></label>
						<div class="col-sm-8"><input type="text" class="form-control" name="permissionCode" placeholder="${I18n.system_please_input}" maxlength="20" ></div>
					</div>

					<div class="form-group">
						<label for="lastname" class="col-sm-2 control-label">父级ID<font color="red">*</font></label>
						<div class="col-sm-8"><input type="text" class="form-control" name="parentId" placeholder="${I18n.system_please_input}" maxlength="20" ></div>
					</div>
					<hr>
					<div class="form-group">
						<div class="col-sm-offset-3 col-sm-6">
							<button type="submit" class="btn btn-primary"  >${I18n.system_save}</button>
							<button type="button" class="btn btn-default" data-dismiss="modal">${I18n.system_cancel}</button>
							<input type="hidden" name="id" >
						</div>
					</div>

				</form>
			</div>
		</div>
	</div>
</div>

<!-- 更新.模态框 -->
<div class="modal fade" id="updateModal" tabindex="-1" role="dialog"  aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
            	<h4 class="modal-title" >${I18n.permission_update}</h4>
         	</div>
         	<div class="modal-body">
				<form class="form-horizontal form" role="form" >
                    <div class="form-group">
                        <label for="lastname" class="col-sm-2 control-label">名称<font color="red">*</font></label>
                        <div class="col-sm-8"><input type="text" class="form-control" name="name" placeholder="${I18n.system_please_input}" maxlength="20" ></div>
                    </div>
                    <div class="form-group">
                        <label for="lastname" class="col-sm-2 control-label">路径<font color="red">*</font></label>
                        <div class="col-sm-8"><input type="text" class="form-control" name="path" placeholder="${I18n.system_please_input}" maxlength="20" ></div>
                    </div>
                    <div class="form-group">
                        <label for="lastname" class="col-sm-2 control-label">类型<font color="red">*</font></label>
                        <div class="col-sm-10">
                            <input type="radio" name="type" value="0" />菜单
                            &nbsp;&nbsp;&nbsp;&nbsp;
                            <input type="radio" name="type" value="1" />按钮
                        </div>
                    </div>
					<div class="form-group">
						<label for="lastname" class="col-sm-2 control-label">授权码<font color="red">*</font></label>
						<div class="col-sm-8"><input type="text" class="form-control" name="permissionCode" placeholder="${I18n.system_please_input}" maxlength="20" ></div>
					</div>

					<div class="form-group">
						<label for="lastname" class="col-sm-2 control-label">父级ID<font color="red">*</font></label>
						<div class="col-sm-8"><input type="text" class="form-control" name="parentId" placeholder="${I18n.system_please_input}" maxlength="20" ></div>
					</div>
					<hr>
					<div class="form-group">
                        <div class="col-sm-offset-3 col-sm-6">
							<button type="submit" class="btn btn-primary"  >${I18n.system_save}</button>
							<button type="button" class="btn btn-default" data-dismiss="modal">${I18n.system_cancel}</button>
                            <input type="hidden" name="id" >
						</div>
					</div>

				</form>
         	</div>
		</div>
	</div>
</div>

<@netCommon.commonScript />
<!-- DataTables -->
<script src="${request.contextPath}/static/adminlte/bower_components/datatables.net/js/jquery.dataTables.min.js"></script>
<script src="${request.contextPath}/static/adminlte/bower_components/datatables.net-bs/js/dataTables.bootstrap.min.js"></script>
<script src="${request.contextPath}/static/js/permission.index.1.js"></script>
</body>
</html>
