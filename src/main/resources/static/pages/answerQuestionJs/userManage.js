/**
 * Created by Amy on 2018/8/9.
 */
$(function () {
    isLoginFun();
    header();
    ///
    $("#ctl01_lblUserName").text(getCookie('userName'));
    ///
    var oTable = new TableInit();
    oTable.Init();
});

//回车事件
$(document).keydown(function (event) {
    if (event.keyCode == 13) {
        getUserList();
    }
});

$('#userManager').on("keydown", function (event) {
    var keyCode = event.keyCode || event.which;
    if (keyCode == "13") {
        event.preventDefault();
    }
});

function getUserList() {
    $("#userTable").bootstrapTable('refresh');
}

function TableInit() {

    var oTableInit = new Object();
    //初始化Table
    oTableInit.Init = function () {
        $('#userTable').bootstrapTable({
            url: httpRequestUrl + '/admin/queryUserList',         //请求后台的URL（*）
            method: 'POST',                      //请求方式（*）
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   //是否显示分页（*）
            sortOrder: "asc",                   //排序方式
            sortStable: true,                   //是否排序
            queryParamsType: '',
            dataType: 'json',
            paginationShowPageGo: true,
            showJumpto: true,
            pageNumber: 1, //初始化加载第一页，默认第一页
            queryParams: queryParams,//请求服务器时所传的参数
            sidePagination: 'server',//指定服务器端分页
            pageSize: 10,//单页记录数
            pageList: [10, 20, 30, 40],//分页步进值
            search: false, //是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
            silent: true,
            showRefresh: true,                  //是否显示刷新按钮
            showToggle: false,
            minimumCountColumns: 2,             //最少允许的列数
            uniqueId: "id",                     //每一行的唯一标识，一般为主键列
            // curpage: typeof getCurPage() == "undefined" ? 1 : getCurPage(),

            columns: [{
                checkbox: true,
                visible: false
            }, {
                field: 'id',
                title: '序号',
                align: 'center',
                formatter: function (value, row, index) {
                    return index + 1;
                }
            },
                {
                    field: 'username',
                    title: '用户账号',
                    align: 'center',
                    width: '230px'
                },
                {
                    field: 'password',
                    title: '用户密码',
                    align: 'center'
                }, {
                    field: 'startTime',
                    title: '开始时间',
                    align: 'center'
                }, {
                    field: 'endTime',
                    title: '结束时间',
                    align: 'center'
                },
                {
                    field: 'operation',
                    title: '操作',
                    align: 'center',
                    events: operateEvents,//给按钮注册事件
                    formatter: addFunctionAlty//表格中增加按钮
                }],
            responseHandler: function (res) {
                ///
                //console.log(res);
                // console.log(this.curpage);
                ///
                if(res.code == "666"){
                    //var userInfo = res.data.list;
                    //var userInfo=JSON.parse('[{"password":"1","startTime":"2022-05-12T10:09:28","id":"1","endTime":"2022-05-12T10:09:30","username":"aa","status":"1"},{"password":"123","startTime":"2022-05-12T12:10:37","id":"290e08f3ea154e33ad56a18171642db1","endTime":"2022-06-11T12:10:37","username":"aaa","status":"1"},{"password":"1","startTime":"2018-10-24T09:49:00","id":"8ceeee2995f3459ba1955f85245dc7a5","endTime":"2025-11-24T09:49:00","username":"admin","status":"1"},{"password":"aa","startTime":"2022-05-16T12:01:54","id":"a6f15c3be07f42e5965bec199f7ebbe6","endTime":"2022-06-15T12:01:54","username":"aaaaa","status":"1"}]');
                    // console.log(res.data);
                    var jstr = JSON.stringify(res.data.list);
                    var userInfo = JSON.parse(jstr);
                    var NewData = [];
                     if (userInfo.length) {

                         for(var i = 0;i < userInfo.length;i++){
                             var dataNewObj = {
                                 'id': '',
                                 "username": '',
                                 'password': '',
                                 "startTime": '',
                                 'endTime': '',
                                 'status': ''
                             };
                             dataNewObj.id = userInfo[i].id;
                             dataNewObj.username = userInfo[i].username;
                             dataNewObj.password = userInfo[i].password;
                             dataNewObj.startTime = userInfo[i].start_time;
                             dataNewObj.endTime = userInfo[i].stop_time;
                             dataNewObj.status = userInfo[i].status;
                             NewData.push(dataNewObj);
                         }
                    }
                    var data = {
                        total: res.data.total,
                        rows: NewData
                    };

                    return data;
                }else{
                    var NewData = [];
                    var data = {
                        total: 0,
                        rows: NewData
                    };

                    return data;
                }

            }

        });
    };

    // 得到查询的参数
    function queryParams(params) {
        var userName = $("#keyWord").val();
        ///
        var temp = {   //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
            ///
            pageNum: params.pageNumber,
            pageSize: params.pageSize,
            username: userName
        };
        return JSON.stringify(temp);
    }
    return oTableInit;
}


window.operateEvents = {
    //编辑
    'click #btn_count': function (e, value, row, index) {
        id = row.id;
        $.cookie('questionId', id);
    }
};


// 表格中按钮
function addFunctionAlty(value, row, index) {
    var btnText = '';

    btnText += "<button type=\"button\" id=\"btn_look\" onclick=\"resetPassword(" + "'" + row.id + "'" + ")\" style='width: 77px;' class=\"btn btn-default-g ajax-link\">重置密码</button>&nbsp;&nbsp;";

    btnText += "<button type=\"button\" id=\"btn_look\" onclick=\"editUserPage(" + "'" + row.id + "')\" class=\"btn btn-default-g ajax-link\">编辑</button>&nbsp;&nbsp;";

    if (row.status == "1") {
        btnText += "<button type=\"button\" id=\"btn_stop" + row.id + "\" onclick=\"changeStatus(" + "'" + row.id + "'" + ")\" class=\"btn btn-danger-g ajax-link\">关闭</button>&nbsp;&nbsp;";
    } else if (row.status == "0") {
        btnText += "<button type=\"button\" id=\"btn_stop" + row.id + "\" onclick=\"changeStatus(" + "'" + row.id + "'" + ")\" class=\"btn btn-success-g ajax-link\">开启</button>&nbsp;&nbsp;"
    }
    btnText += "<button type=\"button\" id=\"btn_stop" + row.id + "\" onclick=\"deleteUser(" + "'" + row.id + "'" + ")\" class=\"btn btn-danger-g ajax-link\">删除</button>&nbsp;&nbsp;";

    return btnText;
}

//重置密码
function resetPassword(id) {
    if(confirm("是否重置用户密码？")){
        openCreateUserPage(getCookie('userName'),'编辑',id);
    }

}

// 打开创建用户页
function openCreateUserPage(opusername, value,id) {
    deleteCookie("userTitle");
    setCookie("userTitle", value);
    if (opusername != '') {
        // deleteCookie("userId");
        setCookie("opusername", opusername);
    }
    if(id != ''){
        deleteCookie("userId");
        setCookie("userId",id);
    }
    console.log(getCookie("opusername"));
    window.location.href = 'createNewUser.html';
}

function editUserPage(id) {
    if(confirm("是否编辑用户？")){
        openCreateUserPage(getCookie('userName'),'编辑',id);
    }

}
// 修改用户状态（禁用、开启）
function changeStatus(id) {
    // console.log(index);
    if(confirm("确认修改用户状态吗？")){
        var url = '/admin/modifyUserStatus';
        var data = {
            "id" : id,
        };
        //传输异步请求（异步和同步的区别自行百度）
        // 注意：一定要传输url而不是href链接，url可以找到项目中任意位置开放的端口（即controler层上注解的value，而href只能从当前地址开始向下找 自行百度url href src）
        commonAjaxPost(true, url, data, function (result) {
            if (result.code == "666") {
                layer.msg("用户状态修改成功", {icon: 1});
                $("#userTable").bootstrapTable('refresh');
            }else {
                layer.msg(result.message, {icon: 2});
            }
        })
    }

}

//删除用户
function deleteUser(id) {
    // if (id != '') {
    //     deleteCookie("userId");
    //     setCookie("userId", id);
    // }
    // window.location.url = 'admin/deleteUserInfoById';
    if(confirm("确认删除用户吗？")){
        var url = '/admin/deleteUserInfoById';
        var data = {
            "id": id,
        };
        commonAjaxPost(true, url, data, function (result) {
            if (result.code == "666") {
                layer.msg("用户删除成功", {icon: 1});
                // setTimeout(function () {
                //     window.location.href = 'userManage.html';
                // }, 1000)
                $("#userTable").bootstrapTable('refresh');
            }else {
                layer.msg(result.message, {icon: 2});
            }
        })
    }

}

