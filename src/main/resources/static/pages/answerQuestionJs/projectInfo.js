/**
 * Created by Amy on 2018/8/7.
 */
$(function () {
    isLoginFun();
    header();
    $("#ctl01_lblUserName").text(getCookie('userName'));
    getProjectInfo();
    getQuestionnaireInfo();
});

// 查看项目详细信息
function getProjectInfo() {
    var projectName = getCookie('projectName');
    var url = '/queryProjectList';
    var data = {
        "projectName": projectName
    };
    commonAjaxPost(true, url, data, function (result) {
        //console.log(result)
        getProjectInfoSuccess(result)
    });

}

// 查看项目详细信息成功
function getProjectInfoSuccess(result) {
    //console.log(result)
    if (result.code == "666") {
        var projectInfo = result.data[0];
        $("#projectNameSpan").text(projectInfo.project_name);
        $("#createTimeSpan").text(projectInfo.creation_date.replace(/-/g, '/'));
        $("#adminSpan").text(projectInfo.created_by);
        $("#projectContentSpan").text(projectInfo.project_content);

    } else if (result.code == "333") {
        layer.msg(result.message, {icon: 2});
        setTimeout(function () {
            window.location.href = 'login.html';
        }, 1000)
    } else {
        layer.msg(result.message, {icon: 2})
    }
}

function getQuestionnaireInfo(result) {
    var projectName = getCookie('projectName');
    var url = '/queryQuestionnaireListByProjectId';
    var data = {
        "projectName": projectName
    };
    commonAjaxPost(false, url, data, function (result) {
        if (result.code == "666") {
            $("#questTableBody").empty();
            var data = result.data;
            if (data.length) {
                var text = "";
                var i = 0;
                for (; i < data.length; i++) {
                    var questionnaireData = data[i];
                    var dataid=questionnaireData.dataId;
                    var type='';
                    // 在校生：2；毕业生：3；教师：4；用人单位：5
                    if(dataid=="2"){
                        type='在校生';
                    }
                    if(dataid=="3"){
                        type='毕业生';
                    }
                    if(dataid=="4"){
                        type='教师';
                    }
                    if(dataid=="5"){
                        type='公司';
                    }
                    //问卷状态
                    var status=questionnaireData.questionStop;
                    var statusText='';
                    if(status=="0"){
                        statusText='已过期';
                    }
                    if(status=="1"){
                        statusText='开启中';
                    }
                    if(status=="2"){
                        statusText='停止中';
                    }
                    if(status=="3"){
                        statusText='已发布';
                    }
                    if(status=="4"){
                        statusText='模板问卷';
                    }

                    text += "<tr>";
                    text += "<td>" + (i + 1) + "</td>";
                    text += "<td>" + questionnaireData.questionName + "</td>";
                    text += "<td>" + questionnaireData.creationDate + "</td>";
                    text += "<td>" + type + "</td>";
                    text += "<td>" + statusText + "</td>";
                    text += "<td><a id=\"btnEdit" + i + "\"  href=\"javascript:void(0)\" onclick=\"editQuestion(" + "'" + questionnaireData.id + "'" + "," + "'" + questionnaireData.questionName + "'" + "," + "'"
                       + questionnaireData.questionContent + "'" + "," + "'" + questionnaireData.endTime + "'" + "," + "'" + questionnaireData.creationDate + "'" + "," + "'" + questionnaireData.dataId  + "'"+")\">编辑</a><td>";
                    //text += "<td><a id=\"btnEdit" + i + "\" href=\"javascript:void(0)\" onclick=\"editProject(" + "'" + questionnaireData.name + "'" + "," + "'" + projectName + "'" + "," + "'" + projectInfo.project_content + "'" + ")\"><i class=\"icon remind-icon\"></i>编辑</a><td>";
                    //text += "<td><a id=\"btnEdit\" href=\"javascript:void(0)\" onclick=\"exercise("+"'"+questionnaireData.id+"'"+"," + "'" + questionnaireData.questionName + "'"+ "," + "'"
                    //+ questionnaireData.endTime + "'" +")\">编辑2</a></td>";
                    text += "</tr>";
                }
                $("#questTableBody").append(text);
            }
        }
    });
}

//编辑问卷
function editQuestion(id, name, content, endTime, creationDate, dataId) {
    console.log(content)
    var data = {
        "id": id
    };
    commonAjaxPost(true, '/selectQuestionnaireStatus', data, function (result) {
        if (result.code == "666") {
            if (result.data == "3") {
                layer.msg('问卷已发布，不可修改', {icon: 2});
            } else if (result.data== "1"){
                layer.msg('请停止问卷后修改', {icon: 2});
            }else if (result.data== "0"){
                layer.msg('问卷已过期，不可修改', {icon: 2});
            } else {
                deleteCookie("questionId");
                deleteCookie("questionName");
                deleteCookie("questionContent");
                deleteCookie("endTime");
                setCookie("questionId", id);
                setCookie("questionName", name);
                setCookie("questionContent", content);
                setCookie("endTime", endTime);
                setCookie("creationDate", creationDate);
                setCookie("dataId", dataId);
                window.location.href = 'editQuestionnaire.html'
            }
        }
    });
}

