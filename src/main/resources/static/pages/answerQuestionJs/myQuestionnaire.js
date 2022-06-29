/**
 * Created by Amy on 2018/8/7.
 */
var questIdModal = '';
$(function () {
    isLoginFun();
    var userName = getCookie('userName');
    header();
    $("#ctl01_lblUserName").html(userName);
    getProjectQuest();
});

//回车事件
$(document).keydown(function (event) {
    if (event.keyCode == 13) {
        getProjectQuest();
    }
});

// 查看项目及其包含的问卷列表
function getProjectQuest() {
    var keyWord = $("#keyWord").val();
    var userName = getCookie("userName");
    var url = '/queryProjectList';
    var data = {
        "projectName": keyWord,
        "createdBy": userName
    };
    //console.log(data);
    commonAjaxPost(true, '/queryProjectList', data, function (result) {
        //console.log(result)
        getProjectQuestSuccess(result)
    })
}

//var rr = JSON.parse('{"code":"666","data":[{"projectContent":"项目名","id":"708a580ce83c49c0a0cfc65b151d690e","projectName":"静态测试数据","createDate":"2022-05-13T15:05:40"}],"message":null}')


// 查看项目及其包含的问卷列表成功回调
function getProjectQuestSuccess(result) {
    if (result.code == "666") {
        var data = result.data;
        $("#panel-23802").empty();
        //遍历多个项目
        var text = "";
        if (data.length) {
            for (var i = 0; i < data.length; i++) {
                var projectInfo = data[i];
                var projectName = projectInfo.project_name;
                if (projectName.length >= 25) {
                    projectName = projectName.substring(0, 26) + "...";
                }
                text += " <div class=\"panel panel-default\" id=\"projectOne" + i + "\" >";
                text += "     <div class=\"panel-heading\">";
                text += "         <a class=\"panel-title\" data-toggle=\"collapse\" id=\"projcetNumber" + i + "\" data-parent=\"#panel-23802\" href=\"#panel-element-" + projectInfo.id + "\">" + projectName + "</a>";
                text += "";
                text += "         <div class=\"operation-box pull-right\" style=\"font-size: 16px;\">";
                text += "             <a class=\"pull-left release-items\" title=\"创建问卷\" onclick=\"createGetProjectInfo(" + "'" + projectInfo.id + "'" + "," + "'" + projectName + "'" + ")\">";
                text += "                 <i class=\"icon release-icon\"></i>创建问卷</a>";
                text += "             <a href=\"javascript:void(0)\" id=\"projcetShow" + i + "\" class=\"pull-left copy-items\" onclick=\"getProjectInfo(" + "'" + projectName + "'" + ")\"><i class=\"icon copy-icon\"></i>查看</a>";
                text += "             <a class=\"pull-left item-remind\" id=\"projcetUpdate" + i + "\" href=\"javascript:void(0)\" onclick=\"editProject(" + "'" + projectInfo.id + "'" + "," + "'" + projectName + "'" + "," + "'" + projectInfo.project_content + "'" + ")\"><i class=\"icon remind-icon\"></i>编辑</a>";
                text += "             <a href=\"javascript:void(0)\" class=\"pull-left cutout-items\" title=\"删除此项目\" onclick=\"deleteProject(" + "'" + projectInfo.id + "'" + ")\"><i class=\"icon cutout-icon\"></i>删除 </a>";
                text += "         </div>";
                text += "";
                text += "     </div>";

                if (i == 0) {
                    text += "     <div id=\"panel-element-" + projectInfo.id + "\" class=\"panel-collapse collapse in\">";

                } else {
                    text += "     <div id=\"panel-element-" + projectInfo.id + "\" class=\"panel-collapse collapse\">";
                }
                text += "         <div class=\"panel-body\">";
                text += "";

                //这里项目下问卷列表的表长未实现自适应变化
                text += "<table className=\"fillet-content\"  id=\"ctl01_ContentPlaceHolder1\"width=\"1140px\">"
                text += "   <thead>"
                text += "   <tr align=\"center\" valign=\"middle\"style=\"color:Gray;background-color:#F7F8F9;border-color:#D7D8D9;font-size:14px;height:36px;\">"
                text += "    <th style=\"text-align:center;width:10%\" scope=\"col\">序号</th>"
                text += "   <th style=\"text-align:center; width: 40%\" scope=\"col\">问卷名称</th>"
                text += "    <th style=\"text-align:center;width:10%\" scope=\"col\">状态</th>"
                text += "   <th style=\"text-align:center;width: 40%\" scope=\"col\">操作</th>"
                text += " </tr>"
                text += "  </thead>"
                text += "   <tbody id=\"List" + projectName + "\">"
                getQuestionnaireInfoByProjectId(projectName, result)
                text += "  </tbody>"
                text += "</table>"
                text += "         </div>";
                text += "     </div>";
                text += " </div>";
                // }
            }
            //for循环结束
            $("#panel-23802").append(text);
        } else {
            layer.msg("暂无符合条件的项目", {icon: 0})
        }

    } else if (result.code == "333") {
        layer.msg(result.message, {icon: 2});
        setTimeout(function () {
            window.location.href = 'login.html';
        }, 1000)
    } else {
        layer.msg(result.message, {icon: 2})
    }

}

//显示项目中的问卷列表
function getQuestionnaireInfoByProjectId(name, result) {
    var url = '/queryQuestionnaireListByProjectId';
    var data = {
        "projectName": name
    };
    commonAjaxPost(true, url, data, function (result) {
        if (result.code == "666") {
            var data2 = result.data;
            console.log(data2)
            $("#List" + name).empty();
            if (data2.length) {
                var text = "";
                for (var k = 0; k < data2.length; k++) {
                    var questionnaireData = data2[k];
                    var questionnaireStatus = questionnaireData.questionStop;
                    var status= "";
                    if (questionnaireStatus == "0") {
                        status = "已过期";
                    }
                    else if (questionnaireStatus == "1") {
                        status = "进行中";
                    }
                    else if (questionnaireStatus == "2") {
                        status = "已暂停";
                    }
                    else if (questionnaireStatus == "3") {
                        status = "未知";
                    }
                    else if (questionnaireStatus == "4") {
                        status = "历史问卷";
                    }
                    else if (questionnaireStatus == "5") {
                        status = "未发布";
                    }

                    text += "<tr  style=\"color:#333333;background-color:White;font-size:14px;height:36px;\">";
                    text += "    <td align=\"center\" >";
                    text += "        <span>" + (k + 1) + "</span>";
                    text += "    </td>";
                    text += "    <td align=\"center\" >";
                    text += "       <a id=\"btnView\" href=\"javascript:void(0)\" onclick=\"previewQuest(" + "'" + questionnaireData.id + "'" + ")\">" + questionnaireData.questionName + "</a>";
                    text += "    </td>";
                    text += "    <td align=\"center\">";
                    text += "        <span>" + status + "</span>";
                    text += "    </td>";
                    text += "    <td align=\"center\">";

                    //text += "        <a id=\"btnDisconnection\" href=\"javascript:void(0)\" onclick=\"cancelConnection(" + "'" + questionnaireData.id + "'" + ")\">取消关联</a>";
                    //text += "        <a id=\"btnDelete\" href=\"javascript:void(0)\" onclick=\"deleteQuestionnaire(" + "'" + questionnaireData.id + "'" + ")\">删除问卷</a>";
                    text += "<button type=\"button\" id=\"btn_look\" onclick=\"cancelConnection(" + "'" + questionnaireData.id + "')\" class=\"btn btn-default-g ajax-link\">取消关联</button>&nbsp;&nbsp;";
                    text += "<button type=\"button\" id=\"btn_look\" onclick=\"deleteQuestionnaire(" + "'" + questionnaireData.id + "')\" class=\"btn btn-danger-g ajax-link\">删除问卷</button>&nbsp;&nbsp;";

                    if (questionnaireStatus == "2") {
                        text += "<button type=\"button\" id=\"btn_look\" onclick=\"editUserPage(" + "'" + questionnaireData.id + "')\" class=\"btn btn-success-g ajax-link\">开启</button>&nbsp;&nbsp;";
                    } else if (questionnaireStatus == "5") {
                        text += "<button type=\"button\" id=\"btn_look\" onclick=\"sendQustionnaire(" + "'" + questionnaireData.id +"'"+ ","+"'"+questionnaireData.questionName+"'"+ ","+"'"+questionnaireData.dataId+"'"+")\" class=\"btn btn-default-g ajax-link\">发布></button>&nbsp;&nbsp;";
                    } else if (questionnaireStatus == "1") {
                        text += "<button type=\"button\" id=\"btn_look\" onclick=\"editUserPage(" + "'" + questionnaireData.id + "')\" class=\"btn btn-default-g ajax-link\">暂停</button>&nbsp;&nbsp;";
                    } else {
                        text += "<button type=\"button\" id=\"btn_look\" onclick=\"editUserPage(" + "'" + questionnaireData.id + "')\" class=\"btn btn-danger-g ajax-link\">关闭问卷</button>&nbsp;&nbsp;";
                    }
                    text += "    </td>";
                    text += "</tr>";
                }
                $("#List" + name).append(text)
            } else {
                var text = "";
                text += "<tr style=\"color:#d9534f;font-size:16px\">暂无调查问卷或问卷已过期 "
                //text += "<span >暂无调查问卷或问卷已过期</span>";
                text += "</tr>"
                $("#List" + name).append(text)
            }
        }
    });
}

// 删除项目
function deleteProject(projectId) {
    layer.confirm('您确认要删除此项目吗？', {
        btn: ['确定', '取消'] //按钮
    }, function () {
        var url = '/deleteProjectById';
        var data = {
            "id": projectId
        };
        commonAjaxPost(true, url, data, function (result) {
            // //console.log(result);
            if (result.code == "666") {
                layer.msg(result.message, {icon: 1});
                getProjectQuest();
            } else if (result.code == "222") {
                layer.msg(result.message, {icon: 2});
            } else if (result.code == "333") {
                layer.msg(result.message, {icon: 2});
                setTimeout(function () {
                    window.location.href = 'login.html';
                }, 1000);
            } else {
                layer.msg(result.message, {icon: 2});
            }
        });
    }, function () {
    });
}


// 编辑项目，在问卷未发布的状态下才可以编辑项目信息
function editProject(id, name, content) {
    var url = '/modifyProjectInfo';
    var data = {
        "id": id
    };
    commonAjaxPost(true, url, data, function (result) {
        //console.log(result);
        if (result.code == "666") {
            deleteCookie("projectId");
            deleteCookie("projectName");
            deleteCookie("projectContent");
            setCookie("projectId", id);
            setCookie("projectName", name);
            setCookie("projectContent", content);
            window.location.href = 'editProject.html'
        } else if (result.code == "222") {
            layer.msg(result.message, {icon: 2});
        } else if (result.code == "333") {
            layer.msg(result.message, {icon: 2});
            setTimeout(function () {
                window.location.href = 'login.html';
            }, 1000);
        } else {
            layer.msg(result.message, {icon: 2});
        }
    });

}

// 查看项目详细信息
function getProjectInfo(name) {
    deleteCookie("projectName");
    setCookie("projectName", name);
    window.location.href = 'projectInfo.html'
}

// 为了创建问卷而获取项目id、项目名称
function createGetProjectInfo(id, name) {
    //alert("创建问卷")
    deleteCookie("projectName");
    setCookie("projectName", name);
    deleteCookie("dataId");
    setCookie("dataId", id);
    window.location.href = "createQuestionnaire.html"
}

//取消问卷关联
function cancelConnection(id) {
    layer.confirm('您确认要取消与项目的关联吗？', {
        btn: ['确定', '取消'] //按钮
    }, function () {
        // console.log(id)
        var data = {
            "id": id
        }
        commonAjaxPost(true, '/cancelQuestionnaireProject', data, function (result) {
            console.log(result);
            //5：问卷未发布 1：问卷进行中
            if (result.code == "666") {
                layer.msg(result.message, {icon: 1});
                getProjectQuest();
            } else if (result.code == "20001") {
                layer.msg(result.message, {icon: 2});
            }else{
                layer.msg(result.message, {icon: 2});
            }
        });
    }, function () {
    });
}

//删除问卷
function deleteQuestionnaire(id,name) {
    layer.confirm('您确认要删除此问卷吗？', {
        btn: ['确定', '取消'] //按钮
    }, function () {
        var data = {
            "id": id
        };
        commonAjaxPost(true,  '/deleteQuestionnaireById', data, function (result) {
            console.log(result);
            if (result.code == "666") {
                layer.msg(result.message, {icon: 1});
                getProjectQuest();
            } else if (result.code == "20001") {
                layer.msg(result.message, {icon: 2});
            } else if (result.code == "333") {
                layer.msg(result.message, {icon: 2});
                setTimeout(function () {
                    window.location.href = 'login.html';
                }, 1000);
            } else {
                layer.msg(result.message, {icon: 2});
            }
        });
    }, function () {
    });
}

//发布问卷
function sendQustionnaire(id,name,dataId) {
    //发布问卷
    layer.confirm('您确认要发布此问卷吗？', {
        btn: ['确定', '取消'] //按钮
    },function () {
        deleteCookie("questionId");
        deleteCookie("nameOfQuestionnaire");
        deleteCookie("dataId");
        setCookie("questionId", id);
        setCookie("nameOfQuestionnaire", name);
        setCookie("dataId", dataId);
       window.location.href = "sendQuestionnaire.html"
    });
}



