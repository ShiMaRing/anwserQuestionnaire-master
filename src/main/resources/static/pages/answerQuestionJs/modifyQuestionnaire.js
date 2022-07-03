/**
 * Created by Amy on 2018/8/6.
 */
var question = '';
var questionTitle = '';
var da1 = {};
$(function () {
});

// 创建时间选择器
function createDtePicker() {
  var nowTime = getFormatDateSecond();
  var startTime = GetDateStr(1);
  var date = new Date();
  var milliseconds = date.getTime() + 1000 * 60 * 60 * 24 * 8;  //n代表天数,加号表示未来n天的此刻时间,减号表示过去n天的此刻时间   n:7
  //getTime()方法返回Date对象的毫秒数,但是这个毫秒数不再是Date类型了,而是number类型,所以需要重新转换为Date对象,方便格式化
  var newDate = new Date(milliseconds);
  var dateAfterNow = timeFormat(newDate);
  $('#config-demo').daterangepicker({
    // "autoApply": true,
    "minDate": nowTime,
    "startDate": startTime,
    "endDate": dateAfterNow,
    "timePicker": true,
    "timePicker24Hour": true,
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
    // //console.log('New date range selected: ' + start.format('YYYY-MM-DD') + ' to ' + end.format('YYYY-MM-DD') + ' (predefined range: ' + label + ')');
  });
}


window.operateEvents = {
    //编辑
    'click #btn_count': function (e, value, row, index) {
        id = row.id;
        $.cookie('questionnaire', id);
    }
};

function modifyQuestuinnairePage(opusername, value,id) {
    if(id != ''){
        deleteCookie("userId");
        setCookie("userId",id);
    }
    console.log(getCookie("opusername"));
    window.location.href = 'createNewUser.html';
}