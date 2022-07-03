/**
 * Created by Amy on 2018/8/8.
 */

var questionName = '';
var questionContent = '';
var questionId = '';
var endTime = '';
var startTime = '';
var dataId = '';
var ifEditQuestType = '';

$(function () {
  initData();
  isLoginFun();
  header();
  $("#ctl01_lblUserName").text(getCookie('userName'));
  createTimePicker();
  $("#questionName").val(questionName);
  $("#questionContent").val(questionContent);
  queryAllDataType();
  $("#questionStartEndTime").val(startTime + " ~ " + endTime);
  $("#ifRemand").css('display', 'none');

});

function initData() {
  var data = {
    "questionId": getCookie('questionId')
  };
  var url = '/queryQuestionnaireAll';

  commonAjaxPost(false, url, data, function (res) {
    result = res.data;
    questionId=result.id;
    questionName = result.questionName;
    questionContent = result.questionContent;
    endTime = result.endTime;
    startTime = result.startTime;
    dataId = result.dataId;
    ifEditQuestType = 'true'
  })

}

//铺调查类型
function queryAllDataType() {
  var url = '/admin/queryAllDataType';
  var da = {'parentId': '1'};
  commonAjaxPost(true, url, da, function (result) {
    result.code = "666"

    console.log(result);
    if (result.code == "666") {
      var belongType = document.getElementById('belongType');
      belongType.options.length = 0;
      for (var i = 0; i < result.data.length; i++) {
        var collOpt = document.createElement('option');
        collOpt.innerText = result.data[i].name;
        collOpt.value = result.data[i].id;
        belongType.appendChild(collOpt);
        $("#belongType").val(dataId);
      }
      if (ifEditQuestType == "false") {
        $("#belongType").attr("disabled", "disabled");
        $("#ifRemand").css('display', 'block');
      }
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

//点击“保存修改”，编辑问卷
function modifyQuest() {
  var questionNameInt = $("#questionName").val();
  var questionContentInt = $("#questionContent").val();
  var belongType = $("#belongType").val();
  var questionStarTimeInt = $("#questionStartEndTime").val().split(" ~ ")[0];
  var questionEnTimeInt = $("#questionStartEndTime").val().split(" ~ ")[1];

  var questionStarTimeIntTemp = dateChange(questionStarTimeInt);
  var questionEnTimeIntTemp = dateChange(questionEnTimeInt);

  var questionStop;
  var radio = document.getElementsByName("isOpen");
  for (i = 0; i < radio.length; i++) {
    if (radio[i].checked) {
      questionStop = radio[i].value
    }
  }
  if (questionNameInt.trim() == '') {
    layer.msg('请完整填写项目名称')
  } else if (questionContentInt.trim() == '') {
    layer.msg('请完整填写项目描述')
  } else {
    var url = '/modifyQuestionnaireInfo';
    var data = {
      "id": questionId,
      "questionName": questionNameInt,
      "questionContent": questionContentInt,
      "dataId": belongType,
      "startTime": questionStarTimeIntTemp,
      "endTime": questionEnTimeIntTemp,
      "questionStop": questionStop
    };
    commonAjaxPost(true, url, data, modifyQuestSuccess);
  }
}

//修改问卷信息成功
function modifyQuestSuccess(result) {
  if (result.code == '666') {
    layer.msg('修改成功', {icon: 1});
    setTimeout(function () {
      window.location.href = 'createProject.html';
      deleteCookie('hidden');
      setCookie('hidden','true');
    }, 500);
  } else if (result.code == "333") {
    layer.msg(result.message, {icon: 2});
    setTimeout(function () {
      window.location.href = 'login.html';
    }, 1000)
  } else {
    layer.msg(result.message, {icon: 2});
  }
}

// 创建时间区域选择
function createTimePicker() {
  var beginTimeStore = '';
  var endTimeStore = '';
  var nowTime = getFormatDateSecond();
  var start1 = startTime;
  $('#questionStartEndTime').daterangepicker({
    "minDate": nowTime,
    "startDate": startTime,
    "endDate": endTime,
    "timePicker": true,
    "timePicker12Hour": true,
    "linkedCalendars": false,
    "locale": {
      "resetLabel": "重置",
      "format": 'YYYY/MM/DD HH:mm:ss',
      "separator": " ~ ",//
      "applyLabel": "确定",
      "cancelLabel": "取消",
      "fromLabel": "起始时间",
      "toLabel": "结束时间'",
      "customRangeLabel": "自定义",
      "weekLabel": "W",
      "daysOfWeek": ["日", "一", "二", "三", "四", "五", "六"],
      "monthNames": ["一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月",
        "十一月", "十二月"],
      "firstDay": 1
    },
  }, function (start, end, label) {

  });

}
