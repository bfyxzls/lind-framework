$(function () {

    // init date tables
    var userListTable = $("#list").dataTable({
        "deferRender": true,
        "processing": true,
        "serverSide": true,
        "sAjaxDataProp":"records", //从records属性取数据，默认是data属性里取,{"records":[{},{}]}
        "ajax": {
            url: base_url + "/permission/pageList",
            type: "post",
            data: function (d) {
                var obj = {};
                obj.name = $('#name').val();
                obj.current = d.current;
                obj.size = d.size;
                return obj;
            }
        },
        "searching": false,
        "ordering": false,
        //"scrollX": true,	// scroll x，close self-adaption
        "columns": [
            {
                "data": 'id',
                "visible": false,
                "width": '10%'
            },
            {
                "data": 'name',
                "visible": true,
                "width": '20%'
            },
			{
				"data": 'permissionCode',
				"width": '10%',
				"visible": true
			},
            {
                "data": 'path',
                "width": '10%',
                "visible": true
            },
            {
                "data": 'type',
                "width": '10%',
                "visible": true
            },
            {
                "data": I18n.system_opt,
                "width": '15%',
                "render": function (data, type, row) {
                    return function () {
                        // html
                        tableData['key' + row.id] = row;
                        var html = '<p id="' + row.id + '" >' +
                            '<button class="btn btn-warning btn-xs update" type="button">' + I18n.system_opt_edit + '</button>  ' +
                            '<button class="btn btn-danger btn-xs delete" type="button">' + I18n.system_opt_del + '</button>  ' +
                            '</p>';

                        return html;
                    };
                }
            }
        ],
        "language": {
            "sProcessing": I18n.dataTable_sProcessing,
            "sLengthMenu": I18n.dataTable_sLengthMenu,
            "sZeroRecords": I18n.dataTable_sZeroRecords,
            "sInfo": I18n.dataTable_sInfo,
            "sInfoEmpty": I18n.dataTable_sInfoEmpty,
            "sInfoFiltered": I18n.dataTable_sInfoFiltered,
            "sInfoPostFix": "",
            "sSearch": I18n.dataTable_sSearch,
            "sUrl": "",
            "sEmptyTable": I18n.dataTable_sEmptyTable,
            "sLoadingRecords": I18n.dataTable_sLoadingRecords,
            "sInfoThousands": ",",
            "oPaginate": {
                "sFirst": I18n.dataTable_sFirst,
                "sPrevious": I18n.dataTable_sPrevious,
                "sNext": I18n.dataTable_sNext,
                "sLast": I18n.dataTable_sLast
            },
            "oAria": {
                "sSortAscending": I18n.dataTable_sSortAscending,
                "sSortDescending": I18n.dataTable_sSortDescending
            }
        }
    });

    // table data
    var tableData = {};

    // search btn
    $('#searchBtn').on('click', function () {
        userListTable.fnDraw();
    });

    // job operate
    $("#list").on('click', '.delete', function () {
        var id = $(this).parent('p').attr("id");

        layer.confirm(I18n.system_ok + I18n.system_opt_del + '?', {
            icon: 3,
            title: I18n.system_tips,
            btn: [I18n.system_ok, I18n.system_cancel]
        }, function (index) {
            layer.close(index);

            $.ajax({
                type: 'POST',
                url: base_url + "/permission/remove",
                data: {
                    "id": id
                },
                dataType: "json",
                success: function (data) {
                    if (data.code == 200) {
                        layer.msg(I18n.system_success);
                        userListTable.fnDraw(false);
                    } else {
                        layer.msg(data.msg || I18n.system_opt_del + I18n.system_fail);
                    }
                }
            });
        });
    });

    // add role
    $("#addModal .form input[name=role]").change(function () {
        var role = $(this).val();
        if (role == 1) {
            $("#addModal .form input[name=permission]").parents('.form-group').hide();
        } else {
            $("#addModal .form input[name=permission]").parents('.form-group').show();
        }
        $("#addModal .form input[name='permission']").prop("checked", false);
    });

    jQuery.validator.addMethod("myValid01", function (value, element) {
        var length = value.length;
        var valid = /^[a-z][a-z0-9_-]*$/;//小写开头，后面包含大写，小写，_或者-的字符
        return this.optional(element) || valid.test(value);
    }, I18n.user_username_valid);

    // add
    $(".add").click(function () {
        $('#addModal').modal({backdrop: false, keyboard: false}).modal('show');
    });
    var addModalValidate = $("#addModal .form").validate({
        errorElement: 'span',
        errorClass: 'help-block',
        focusInvalid: true,
        rules: {
            name: {
                required: true,
                rangelength: [4, 20]

            },
            path: {
                required: true
            },
            permissionCode:{
                required: true,
                myValid01: true
            }
        },
        messages: {
            name: {
                required: I18n.system_please_input + "name",
                rangelength: I18n.system_lengh_limit + "[4-20]"
            },
            path: {
                required: I18n.system_please_input + "path",
                rangelength: I18n.system_lengh_limit + "[4-20]"
            }
        },
        highlight: function (element) {
            $(element).closest('.form-group').addClass('has-error');
        },
        success: function (label) {
            label.closest('.form-group').removeClass('has-error');
            label.remove();
        },
        errorPlacement: function (error, element) {
            element.parent('div').append(error);
        },
        submitHandler: function (form) {

            var permissionArr = [];
            $("#addModal .form input[name=permission]:checked").each(function () {
                permissionArr.push($(this).val());
            });

            var paramData = {
                "name": $("#addModal .form input[name=name]").val(),
                "path": $("#addModal .form input[name=path]").val(),
                "permissionCode": $("#addModal .form input[name=permissionCode]").val(),
                "type": $("#addModal .form input[name=type]").val(),
            };

            $.post(base_url + "/permission/add", paramData, function (data, status) {
                if (data.code == "200") {
                    $('#addModal').modal('hide');

                    layer.msg(I18n.system_add_suc);
                    userListTable.fnDraw();
                } else {
                    layer.open({
                        title: I18n.system_tips,
                        btn: [I18n.system_ok],
                        content: (data.msg || I18n.system_add_fail),
                        icon: '2'
                    });
                }
            });
        }
    });
    $("#addModal").on('hide.bs.modal', function () {
        $("#addModal .form")[0].reset();
        addModalValidate.resetForm();
        $("#addModal .form .form-group").removeClass("has-error");
        $(".remote_panel").show();	// remote

        $("#addModal .form input[name=permission]").parents('.form-group').show();
    });

    // update role
    $("#updateModal .form input[name=role]").change(function () {
        var role = $(this).val();
        if (role == 1) {
            $("#updateModal .form input[name=permission]").parents('.form-group').hide();
        } else {
            $("#updateModal .form input[name=permission]").parents('.form-group').show();
        }
        $("#updateModal .form input[name='permission']").prop("checked", false);
    });

    // update
    $("#list").on('click', '.update', function () {

        var id = $(this).parent('p').attr("id");
        var row = tableData['key' + id];

        // base data
        $("#updateModal .form input[name='id']").val(row.id);
        $("#updateModal .form input[name='name']").val(row.name);
        $("#updateModal .form input[name='path']").val(row.path);
        $("#updateModal .form input[name='permissionCode']").val(row.permissionCode);
        $("#updateModal .form input[name='type'][value='"+ row.type +"']").click();
        var permissionArr = [];
        if (row.permission) {
            permissionArr = row.permission.split(",");
        }
        $("#updateModal .form input[name='permission']").each(function () {
            if ($.inArray($(this).val(), permissionArr) > -1) {
                $(this).prop("checked", true);
            } else {
                $(this).prop("checked", false);
            }
        });

        // show
        $('#updateModal').modal({backdrop: false, keyboard: false}).modal('show');
    });
    var updateModalValidate = $("#updateModal .form").validate({
        errorElement: 'span',
        errorClass: 'help-block',
        focusInvalid: true,
        highlight: function (element) {
            $(element).closest('.form-group').addClass('has-error');
        },
        success: function (label) {
            label.closest('.form-group').removeClass('has-error');
            label.remove();
        },
        errorPlacement: function (error, element) {
            element.parent('div').append(error);
        },
        submitHandler: function (form) {

            var permissionArr = [];
            $("#updateModal .form input[name=permission]:checked").each(function () {
                permissionArr.push($(this).val());
            });

            var paramData = {
                "id": $("#updateModal .form input[name=id]").val(),
                "name": $("#updateModal .form input[name=name]").val(),
                "path": $("#updateModal .form input[name=path]").val(),
                "type": $("#updateModal .form input[name=role]:checked").val(),
                "permissionCode":  $("#updateModal .form input[name=permissionCode]").val(),
            };

            $.post(base_url + "/permission/update", paramData, function (data, status) {
                if (data.code == "200") {
                    $('#updateModal').modal('hide');

                    layer.msg(I18n.system_update_suc);
                    userListTable.fnDraw();
                } else {
                    layer.open({
                        title: I18n.system_tips,
                        btn: [I18n.system_ok],
                        content: (data.msg || I18n.system_update_fail),
                        icon: '2'
                    });
                }
            });
        }
    });
    $("#updateModal").on('hide.bs.modal', function () {
        $("#updateModal .form")[0].reset();
        updateModalValidate.resetForm();
        $("#updateModal .form .form-group").removeClass("has-error");
        $(".remote_panel").show();	// remote

        $("#updateModal .form input[name=permission]").parents('.form-group').show();
    });

});
