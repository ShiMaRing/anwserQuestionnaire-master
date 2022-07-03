/**
 * Created by Amy on 2018/8/6.
 */
var flag = true;
var projectName = '';
var projectContent = '';

$(function () {
  isLoginFun();
  //$("#btnCreatProjectSuccess").attr("style", "display:none;");
  header();
  //$("#ctl01_lblQuestionnairename").text(getCookie('userName'));
  var oTable = new QuestionnaireTableInit();
  oTable.Init();

  var hidden = getCookie('hidden');
  if (hidden === 'true') {
    $("#projectName").attr("disabled", "disabled");
    //显示添加问卷
    $("#projectName").val(getCookie('projectName'));
    $("#projectContextBox").css("display", "none");
    $("#btnCreatProject").css("display", "none");
    $("#addQuestionnaireBox").attr("style", "display:block;");
    //显示完成按钮
    $("#btnCreatProjectSuccess").attr("style", "display:block;");
    setCookie('hidden', 'false')
  }

});

//回车事件
$(document).keydown(function (event) {
  if (event.keyCode == 13) {
    getQuestionnaireList();
  }
});

$('#questionList').on("keydown", function (event) {
  var keyCode = event.keyCode || event.which;
  if (keyCode == "13") {
    event.preventDefault();
  }
});

function getQuestionnaireList() {
  $("#questionnaireTable").bootstrapTable('refresh');
}

//点击“立即创建”，创建项目
function createProject() {
  projectName = $("#projectName").val();
  projectContent = $("#inputIntro").val();
  createProjectRight();
}

function createProjectRight() {
  if (flag == true) {
    if (projectName.trim() == '') {
      layer.msg('请完整填写项目名称')
    } else if (projectContent.trim() == '') {
      layer.msg('请完整填写项目描述')
    } else {
      var userName = getCookie("userName");
      var url = '/addProjectInfo';
      var data = {
        "projectName": projectName,
        "projectContent": projectContent,
        "createdBy": userName,
        "lastUpdatedBy": userName
      };
      commonAjaxPost(false, url, data, function (result) {
        //console.log(result)
        if (result.code == "666") {
          layer.msg('创建项目成功，请添加问卷', {icon: 1});
          //使项目名称输入框失效
          $("#projectName").attr("disabled", "disabled");
          //显示添加问卷
          $("#projectContextBox").css("display", "none");
          $("#btnCreatProject").css("display", "none");
          $("#addQuestionnaireBox").attr("style", "display:block;");
          //显示完成按钮
          $("#btnCreatProjectSuccess").attr("style", "display:block;");
          //清除项目id缓存
          deleteCookie("projectId");
          //设置项目id缓存
          setCookie("projectId", result.data);
          setCookie("projectName", projectName);
          //setTimeout(function () {
          // window.location.href = "myQuestionnaires.html";
          //}, 700)
        } else if (result.code == "333") {
          layer.msg(result.message, {icon: 2});
          setTimeout(function () {
            window.location.href = 'login.html';
          }, 1000)
        } else {
          layer.msg(result.message, {icon: 2})
        }
      });
    }
  }
}

function QuestionnaireTableInit() {

  var oTableInit = new Object();
  //初始化Table
  oTableInit.Init = function () {
    $('#questionnaireTable').bootstrapTable({
      url: httpRequestUrl + '/queryAllQuestionnaire',         //请求后台的URL（*）
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
      }, {
        field: 'questionnaireName',
        title: '问卷名称',
        align: 'center',
        //width: '230px'
      }, {
        field: 'questionnaireStatus',
        title: '问卷状态',
        align: 'center'
      }, {
        field: 'startTime',
        title: '开始时间',
        align: 'center'
      }, {
        field: 'endTime',
        title: '结束时间',
        align: 'center'
      }, {
        field: 'operation',
        title: '操作',
        align: 'center',
        events: operateEvents,//给按钮注册事件
        formatter: addFunctionAlty//表格中增加按钮
      }],
      responseHandler: function (res) {
        //console.log(res);
        // console.log(this.curpage);
        ///
        if (res.code == "666") {
          //var questionnaireinfo = res.data.list;
          //var questionnaireinfo=JSON.parse('[{"password":"1","startTime":"2022-05-12T10:09:28","id":"1","endTime":"2022-05-12T10:09:30","username":"aa","status":"1"},{"password":"123","startTime":"2022-05-12T12:10:37","id":"290e08f3ea154e33ad56a18171642db1","endTime":"2022-06-11T12:10:37","username":"aaa","status":"1"},{"password":"1","startTime":"2018-10-24T09:49:00","id":"8ceeee2995f3459ba1955f85245dc7a5","endTime":"2025-11-24T09:49:00","username":"admin","status":"1"},{"password":"aa","startTime":"2022-05-16T12:01:54","id":"a6f15c3be07f42e5965bec199f7ebbe6","endTime":"2022-06-15T12:01:54","username":"aaaaa","status":"1"}]');
          // console.log(res.data);
          var jstr = JSON.stringify(res.data.list);
          var questionnaireinfo = JSON.parse(jstr);
          var NewData = [];
          if (questionnaireinfo.length) {
            for (var i = 0; i < questionnaireinfo.length; i++) {
              var dataNewObj = {
                'id': '',
                "questionnaireName": '',
                'questionnaireStatus': '',
                "startTime": '',
                'endTime': '',
              };
              dataNewObj.id = questionnaireinfo[i].id;
              dataNewObj.questionnaireName = questionnaireinfo[i].question_name;
              dataNewObj.questionnaireStatus = questionnaireinfo[i].question_stop;
              dataNewObj.startTime = questionnaireinfo[i].start_time;
              dataNewObj.endTime = questionnaireinfo[i].end_time;
              NewData.push(dataNewObj);
            }
          }
          var data = {
            total: res.data.total,
            rows: NewData
          };
          return data;
        } else {
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
    var questionnairename = $("#questionnairename").val();
    //console.log(questionnairename);
    var temp = {   //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
      ///
      pageNum: params.pageNumber,
      pageSize: params.pageSize,
      questionName: questionnairename
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
  var btnText = "";
  //if (row.questionnaireStatus == "1") {
  btnText += "<button type=\"button\"  id=\"btn_add" + row.id
      + "\" onclick=\"addQuestionnaire(" + "'" + row.id + "'"
      + ")\" class=\"btn btn-default-g ajax-link\">添加</button>&nbsp;&nbsp;";
  // } else if (row.status == "0") {
  //隐藏取消添加按钮
  btnText += "<button type=\"button\" disabled=\"disabled\" id=\"btn_cancel"
      + row.id + "\" onclick=\"cancelQuestionnaireConnection(" + "'" + row.id
      + "'"
      + ")\" class=\"btn btn-danger-g ajax-link\" >取消添加</button>&nbsp;&nbsp;"
  //}
  btnText += "<button type=\"button\" id=\"btn_modify" + row.id
      + "\" onclick=\"modifyQuestionnaireInfo(" + "'" + row.id + "'"
      + ")\" class=\"btn btn-default-g ajax-link\" >修改</button>&nbsp;&nbsp;"

  return btnText;
}

// 添加问卷关联
function addQuestionnaire(id) {
  deleteCookie('questionId');
  var projectId = getCookie("projectId");
  //添加问卷到项目中
  var data = {
    "questionId": id,
    "projectId": projectId
  }
  var url = "/addQuestionnaireProject";
  commonAjaxPost(false, url, data, function (result) {
    if (result.code == "666") {
      layer.msg(result.message, {icon: 1})
      //添加按钮变为取消添加按钮
      $("#btn_add" + id).attr("disabled", "disabled");
      //取消添加按钮显示
      $("#btn_cancel" + id).removeAttr("disabled");
    } else {
      alert("添加失败");
    }
  });
  // window.location.href = "addQuestionnaire.html";
}

//修改问卷起始时间及状态
//todo
function modifyQuestionnaireInfo(id) {
  setCookie("questionId", id)
  window.location.href = "modifyQuestionnaire.html";
}

//取消问卷关联
function cancelQuestionnaireConnection(id) {
  layer.confirm('您确认要取消与项目的关联吗？', {
    btn: ['确定', '取消'] //按钮
  }, function () {
    // console.log(id)
    var data = {
      "id": id
    }
    commonAjaxPost(true, '/cancelQuestionnaireProject', data,
        function (result) {
          console.log(result);
          //5：问卷未发布 1：问卷进行中
          if (result.code == "666") {
            //添加按钮可选择
            $("#btn_add" + id).removeAttr("disabled");
            //取消添加按钮不可选择
            $("#btn_cancel" + id).attr("disabled", "disabled");
            layer.msg(result.message, {icon: 1});
          } else if (result.code == "20001") {
            layer.msg(result.message, {icon: 2});
          } else {
            layer.msg(result.message, {icon: 2});
          }
        });
  }, function () {
  });
}

function backHome() {
  deleteCookie('questionId');
  deleteCookie('projectId');
  deleteCookie('hidden');
  deleteCookie('projectName');
  window.location.href = "myQuestionnaires.html";
}

//修改当前的问卷信息，然后返回对应的页面，需要持有问卷的id，以及新的信息

//需要先获取当前的问卷名等信息，然后渲染上去，设定好时间和状态后，执行修改，然后跳转回原本的添加页面

