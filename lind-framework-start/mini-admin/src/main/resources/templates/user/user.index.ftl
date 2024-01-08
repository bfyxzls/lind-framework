<#import "../common/layout.ftl" as layout>
<@layout.mainLayout;section>
    <#if section = "form">
        <!-- Content Header (Page header) -->
        <section class="content-header">
            <h1>${I18n.user_manage}</h1>
        </section>

        <!-- Main content -->
        <section class="content">
            <div class="row">
                <div class="col-xs-3">
                    <div class="input-group">
                        <span class="input-group-addon">${I18n.user_role}</span>
                        <select class="form-control" id="role">
                            <option value="-1">${I18n.system_all}</option>
                            <option value="1">${I18n.user_role_admin}</option>
                            <option value="0">${I18n.user_role_normal}</option>
                        </select>
                    </div>
                </div>
                <div class="col-xs-3">
                    <div class="input-group">
                        <span class="input-group-addon">${I18n.user_username}</span>
                        <input type="text" class="form-control" id="username" autocomplete="on">
                    </div>
                </div>
                <div class="col-xs-1">
                    <button class="btn btn-block btn-info" id="searchBtn">${I18n.system_search}</button>
                </div>
                <div class="col-xs-2">
                    <button class="btn btn-block btn-success add" type="button">${I18n.user_add}</button>
                </div>
            </div>

            <div class="row">
                <div class="col-xs-12">
                    <div class="box">
                        <div class="box-body">
                            <table id="user_list" class="table table-bordered table-striped" width="100%">
                                <thead>
                                <tr>
                                    <th name="id">ID</th>
                                    <th name="username">${I18n.user_username}</th>
                                    <th name="realName">真实姓名</th>
                                    <th name="password">${I18n.user_password}</th>
                                    <th name="role">${I18n.user_role}</th>
                                    <th name="permission">${I18n.user_permission}</th>
                                    <th name="email">email</th>
                                    <th name="phone">phone</th>
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



        <!-- 新增.模态框 -->
        <div class="modal fade" id="addModal" tabindex="-1" role="dialog" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h4 class="modal-title">${I18n.user_add}</h4>
                    </div>
                    <div class="modal-body">
                        <form class="form-horizontal form" role="form">
                            <div class="form-group">
                                <label for="lastname" class="col-sm-2 control-label">${I18n.user_username}<font
                                            color="red">*</font></label>
                                <div class="col-sm-8"><input type="text" class="form-control" name="username"
                                                             placeholder="${I18n.system_please_input}${I18n.user_username}"
                                                             maxlength="20"></div>
                            </div>
                            <div class="form-group">
                                <label for="password" class="col-sm-2 control-label">${I18n.user_password}<font
                                            color="red">*</font></label>
                                <div class="col-sm-8"><input type="text" class="form-control" name="password"
                                                             placeholder="${I18n.user_password_update_placeholder}"
                                                             maxlength="20"></div>
                            </div>
                            <div class="form-group">
                                <label for="email" class="col-sm-2 control-label">${I18n.user_email}<font
                                            color="red">*</font></label>
                                <div class="col-sm-8"><input type="text" class="form-control" name="email"
                                                             placeholder="${I18n.system_please_input}${I18n.user_email}"
                                                             maxlength="20"></div>
                            </div>
                            <div class="form-group">
                                <label for="phone" class="col-sm-2 control-label">${I18n.user_phone}<font
                                            color="red">*</font></label>
                                <div class="col-sm-8"><input type="text" class="form-control" name="phone"
                                                             placeholder="${I18n.system_please_input}${I18n.user_phone}"
                                                             maxlength="20"></div>
                            </div>
                            <div class="form-group">
                                <label for="lastname" class="col-sm-2 control-label">${I18n.user_role}<font color="red">*</font></label>
                                <div class="col-sm-10">
                                    <input type="radio" name="role" value="0" checked/>${I18n.user_role_normal}
                                    &nbsp;&nbsp;&nbsp;&nbsp;
                                    <input type="radio" name="role" value="1"/>${I18n.user_role_admin}
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="lastname" class="col-sm-2 control-label">${I18n.user_permission}<font
                                            color="black">*</font></label>
                                <div class="col-sm-10">
                                    <#if permissions?exists && permissions?size gt 0>
                                        <#list permissions as item>
                                            <input type="checkbox" name="permission"
                                                   value="${item.id}" />${item.name}(${item.permissionCode})<br>
                                        </#list>
                                    </#if>
                                </div>
                            </div>

                            <hr>
                            <div class="form-group">
                                <div class="col-sm-offset-3 col-sm-6">
                                    <button type="submit" class="btn btn-primary">${I18n.system_save}</button>
                                    <button type="button" class="btn btn-default"
                                            data-dismiss="modal">${I18n.system_cancel}</button>
                                </div>
                            </div>

                        </form>
                    </div>
                </div>
            </div>
        </div>
        <!-- 更新.模态框 -->
        <div class="modal fade" id="updateModal" tabindex="-1" role="dialog" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h4 class="modal-title">${I18n.user_update}</h4>
                    </div>
                    <div class="modal-body">
                        <form class="form-horizontal form" role="form">
                            <div class="form-group">
                                <label for="lastname" class="col-sm-2 control-label">${I18n.user_username}<font
                                            color="red">*</font></label>
                                <div class="col-sm-8"><input type="text" class="form-control" name="username"
                                                             placeholder="${I18n.system_please_input}${I18n.user_username}"
                                                             maxlength="20" readonly></div>
                            </div>
                            <div class="form-group">
                                <label for="password" class="col-sm-2 control-label">${I18n.user_password}<font
                                            color="red">*</font></label>
                                <div class="col-sm-8"><input type="text" class="form-control" name="password"
                                                             placeholder="${I18n.user_password_update_placeholder}"
                                                             maxlength="20"></div>
                            </div>
                            <div class="form-group">
                                <label for="email" class="col-sm-2 control-label">${I18n.user_email}<font
                                            color="red">*</font></label>
                                <div class="col-sm-8"><input type="text" class="form-control" name="email"
                                                             placeholder="${I18n.system_please_input}${I18n.user_email}"
                                                             maxlength="20"></div>
                            </div>
                            <div class="form-group">
                                <label for="phone" class="col-sm-2 control-label">${I18n.user_phone}<font
                                            color="red">*</font></label>
                                <div class="col-sm-8"><input type="text" class="form-control" name="phone"
                                                             placeholder="${I18n.system_please_input}${I18n.user_phone}"
                                                             maxlength="20"></div>
                            </div>
                            <div class="form-group">
                                <label for="lastname" class="col-sm-2 control-label">${I18n.user_role}<font color="red">*</font></label>
                                <div class="col-sm-10">
                                    <input type="radio" name="role" value="0"/>${I18n.user_role_normal}
                                    &nbsp;&nbsp;&nbsp;&nbsp;
                                    <input type="radio" name="role" value="1"/>${I18n.user_role_admin}
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="lastname" class="col-sm-2 control-label">${I18n.user_permission}<font
                                            color="black">*</font></label>
                                <div class="col-sm-10">
                                    <#if permissions?exists && permissions?size gt 0>
                                        <#list permissions as item>
                                            <input type="checkbox" name="permission"
                                                   value="${item.permissionCode}" />${item.name}(${item.permissionCode})
                                            <br>
                                        </#list>
                                    </#if>
                                </div>
                            </div>

                            <hr>
                            <div class="form-group">
                                <div class="col-sm-offset-3 col-sm-6">
                                    <button type="submit" class="btn btn-primary">${I18n.system_save}</button>
                                    <button type="button" class="btn btn-default"
                                            data-dismiss="modal">${I18n.system_cancel}</button>
                                    <input type="hidden" name="id">
                                </div>
                            </div>

                        </form>
                    </div>
                </div>
            </div>
        </div>
        <script src="${request.contextPath}/static/js/user.index.1.js"></script>

    </#if>
</@layout.mainLayout>
