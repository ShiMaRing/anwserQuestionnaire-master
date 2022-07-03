package com.aim.questionnaire.controller;

import com.aim.questionnaire.beans.HttpResponseEntity;
import com.aim.questionnaire.common.Constans;
import com.aim.questionnaire.common.utils.DateUtil;
import com.aim.questionnaire.dao.entity.ProjectEntity;
import com.aim.questionnaire.dao.entity.QuestionnaireEntity;
import com.aim.questionnaire.service.QuestionnaireService;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class QuestionnaireController {

  private final Logger logger = LoggerFactory.getLogger(QuestionnaireController.class);
  @Autowired
  QuestionnaireService questionnaireService;

  /**
   * 根据姓名查询所有问卷
   *
   * @param map
   * @return
   */
  @RequestMapping(value = "/queryAllQuestionnaire", method = RequestMethod.POST, headers = "Accept=application/json")
  public HttpResponseEntity queryAllQuestionnaire(@RequestBody Map<String, Object> map) {
    HttpResponseEntity responseEntity = new HttpResponseEntity();
    try {
      PageInfo questionnaireEntityList = questionnaireService.queryQuestionnaireInfo(map);
      if (questionnaireEntityList.getList().size() != 0) {
        responseEntity.setCode(Constans.SUCCESS_CODE);
        responseEntity.setData(questionnaireEntityList);
        responseEntity.setMessage(Constans.STATUS_MESSAGE);
      }
//      else {
//        responseEntity.setCode(Constans.EXIST_CODE);
//        responseEntity.setData(null);
//        responseEntity.setMessage(Constans.QUERYFAIL_MESSAGE);
//      }
    } catch (Exception e) {
      logger.error("查询所有问卷异常:", e);
      responseEntity.setCode(Constans.EXIST_CODE);
      responseEntity.setMessage(Constans.EXIST_MESSAGE);
    }
    return responseEntity;
  }

  /**
   * 根据项目id查询对应问卷的信息
   *
   * @param projectEntity
   * @return
   */
  @RequestMapping(value = "/queryQuestionnaireListByProjectId", method = RequestMethod.POST, headers = "Accept=application/json")
  public HttpResponseEntity queryProjectList(@RequestBody ProjectEntity projectEntity) {
    HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
    List<Map<String, Object>> alist = questionnaireService.queryQuestionnaireListById(
        projectEntity);
    try {
      if (alist.size() != 0) {
        httpResponseEntity.setCode(Constans.SUCCESS_CODE);
        httpResponseEntity.setData(alist);
        httpResponseEntity.setMessage(Constans.STATUS_MESSAGE);
      }
//      else {
//        httpResponseEntity.setCode(Constans.EXIST_CODE);
//        httpResponseEntity.setData(null);
//        httpResponseEntity.setMessage(Constans.QUERYFAIL_MESSAGE);
//      }
    } catch (Exception e) {
      logger.info("addUserInfo 根据项目id查询问卷的基本信息:" + e.getLocalizedMessage());
      httpResponseEntity.setCode(Constans.EXIST_CODE);
      httpResponseEntity.setMessage(Constans.EXIST_MESSAGE);
    }
    return httpResponseEntity;
  }

  @RequestMapping(value = "/queryQuestionnaireMould", method = RequestMethod.POST, headers = "Accept=application/json")
  public HttpResponseEntity queryQuestionnaireMould(@RequestBody Map<String, Object> map) {
    HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
    List<Map<String, Object>> list = questionnaireService.queryQuestionnaireMould(map);
    try {
      if (list.size() != 0) {
        httpResponseEntity.setCode(Constans.SUCCESS_CODE);
        httpResponseEntity.setData(list);
        httpResponseEntity.setMessage(Constans.STATUS_MESSAGE);
      } else {
        httpResponseEntity.setCode(Constans.EXIST_CODE);
        httpResponseEntity.setData(null);
        httpResponseEntity.setMessage(Constans.QUERYFAIL_MESSAGE);
      }
    } catch (Exception e) {
      httpResponseEntity.setCode(Constans.EXIST_CODE);
      httpResponseEntity.setMessage(Constans.EXIST_MESSAGE);
    }
    return httpResponseEntity;
  }

  @RequestMapping(value = "/queryQuestionnaireAll", method = RequestMethod.POST, headers = "Accept=application/json")
  public HttpResponseEntity queryQuestionnaireAll(@RequestBody Map<String, Object> map) {
    HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
    Object questionId = map.get("questionId");
    QuestionnaireEntity questionnaireEntity = questionnaireService.queryQuestionnaireAll(
        (String) questionId);

    try {
      if (questionnaireEntity != null) {
        httpResponseEntity.setCode(Constans.SUCCESS_CODE);
        httpResponseEntity.setData(questionnaireEntity);
        httpResponseEntity.setMessage(Constans.STATUS_MESSAGE);
      } else {
        httpResponseEntity.setCode(Constans.EXIST_CODE);
        httpResponseEntity.setData(questionnaireEntity);
        httpResponseEntity.setMessage(Constans.QUERYFAIL_MESSAGE);
      }
    } catch (Exception e) {
      httpResponseEntity.setCode(Constans.EXIST_CODE);
      httpResponseEntity.setMessage(Constans.EXIST_MESSAGE);
    }
    return httpResponseEntity;
  }


  /**
   * 查询问卷状态
   *
   * @param questionnaireEntity
   * @return
   */
  @RequestMapping(value = "/selectQuestionnaireStatus", method = RequestMethod.POST, headers = "Accept=application/json")
  public HttpResponseEntity selectQuestionnaireStatus(
      @RequestBody QuestionnaireEntity questionnaireEntity) {
    HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
    int status = Integer.parseInt(
        questionnaireService.queryQuestionnaireIsStopStatus(questionnaireEntity.getId()));
    try {
      httpResponseEntity.setCode(Constans.SUCCESS_CODE);
      httpResponseEntity.setData(status);
    } catch (Exception e) {
      logger.info("addUserInfo 查询问卷状态的基本信息>>>>>>>>>>>" + e.getLocalizedMessage());
      httpResponseEntity.setCode(Constans.LOGOUT_NO_CODE);
      httpResponseEntity.setMessage(Constans.EXIST_MESSAGE);
    }
    return httpResponseEntity;
  }

  /**
   * 取消问卷和项目的关联
   *
   * @param questionnaireEntity
   * @return
   */
  @RequestMapping(value = "/cancelQuestionnaireProject", method = RequestMethod.POST, headers = "Accept=application/json")
  public HttpResponseEntity cancelQuestionnaireProject(
      @RequestBody QuestionnaireEntity questionnaireEntity) {
    HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
    String questionnaireId = questionnaireEntity.getId();
    int status = questionnaireService.cancelQuestionnaireProject(questionnaireId);
    try {
      if (status == 1) {
        httpResponseEntity.setCode(Constans.SUCCESS_CODE);
        httpResponseEntity.setMessage(Constans.CANCEL_PROJECT_MESSAGE);
      } else if (status == 0) {
        httpResponseEntity.setCode(Constans.EXIST_CODE);
        httpResponseEntity.setMessage(Constans.COPY_EXIT_UPDATE_MESSAGE);
      }
    } catch (Exception e) {
      logger.info("addUserInfo 取消问卷和项目的关联>>>>>>>>>>>" + e.getLocalizedMessage());
      httpResponseEntity.setCode(Constans.LOGOUT_NO_CODE);
      httpResponseEntity.setMessage(Constans.EXIST_MESSAGE);
    }
    return httpResponseEntity;
  }


  @RequestMapping(value = "/queryQuestionnaireById", method = RequestMethod.POST, headers = "Accept=application/json")
  public HttpResponseEntity queryQuestionnaireById(@RequestBody Map<String, Object> map) {
    HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
    Object questionId = map.get("questionId");
    Map<String, String> questionnaireEntity = questionnaireService.queryQuestionnaireById(
        map);
    try {
      if (questionnaireEntity != null) {
        httpResponseEntity.setCode(Constans.SUCCESS_CODE);
        httpResponseEntity.setData(questionnaireEntity);
        httpResponseEntity.setMessage(Constans.STATUS_MESSAGE);
      } else {
        httpResponseEntity.setCode(Constans.EXIST_CODE);
        httpResponseEntity.setData(questionnaireEntity);
        httpResponseEntity.setMessage(Constans.QUERYFAIL_MESSAGE);
      }
    } catch (Exception e) {
      httpResponseEntity.setCode(Constans.EXIST_CODE);
      httpResponseEntity.setMessage(Constans.EXIST_MESSAGE);
    }
    return httpResponseEntity;
  }


  @RequestMapping(value = "/modifyQuestionnaireInfo", method = RequestMethod.POST, headers = "Accept=application/json")
  public HttpResponseEntity modifyQuestionnaireInfo(
      @RequestBody QuestionnaireEntity questionnaireEntity) {
    HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
    int status = questionnaireService.modifyQuestionnaireInfo(questionnaireEntity);
    try {
      if (status == 1) {
        httpResponseEntity.setCode(Constans.SUCCESS_CODE);
        httpResponseEntity.setMessage("编辑成功");
      } else if (status == 0) {
        httpResponseEntity.setCode(Constans.EXIST_CODE);
        httpResponseEntity.setMessage(Constans.COPY_EXIT_UPDATE_MESSAGE);
      }
    } catch (Exception e) {
      httpResponseEntity.setCode(Constans.LOGOUT_NO_CODE);
      httpResponseEntity.setMessage(Constans.EXIST_MESSAGE);
    }
    return httpResponseEntity;
  }


  /**
   * 建立问卷和项目的关联
   */
  @RequestMapping(value = "/addQuestionnaireProject", method = RequestMethod.POST, headers = "Accept=application/json")
  public HttpResponseEntity addQuestionnaireProject(@RequestBody Map<String, Object> map) {
    HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
    int status = questionnaireService.addQuestionnaireProject(map);
    try {
      if (status == 1) {
        httpResponseEntity.setCode(Constans.SUCCESS_CODE);
        httpResponseEntity.setMessage(Constans.ADD_MESSAGE);
      } else {
        httpResponseEntity.setCode(Constans.EXIST_CODE);
        httpResponseEntity.setMessage(Constans.COPY_EXIT_UPDATE_MESSAGE);
      }
    } catch (Exception e) {
      logger.info("addUserInfo 建立问卷和项目的关联>>>>>>>>>>>" + e.getLocalizedMessage());
      httpResponseEntity.setCode(Constans.LOGOUT_NO_CODE);
      httpResponseEntity.setMessage(Constans.EXIST_MESSAGE);
    }
    return httpResponseEntity;
  }


  @RequestMapping(value = "/getShortUrlForLink", method = RequestMethod.POST, headers = "Accept=application/json")
  public HttpResponseEntity getShortUrlForLink(@RequestBody Map<String, String> map) {
    HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
    Map<String, String> res = new HashMap<>();
    String id = map.get("id");
    String link = map.get("link");
    String baseUrl = "http://localhost:8085/pages/previewQuestionnaire.html?";
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("id=").append(id);
    if (link != null && !link.equals("")) {
      stringBuilder.append("&l");
    } else {
      stringBuilder.append("&zzz");
    }
    baseUrl += stringBuilder.toString();
    res.put("tinyurl", baseUrl);
    httpResponseEntity.setData(JSON.toJSONString(res));
    httpResponseEntity.setCode(Constans.SUCCESS_CODE);
    return httpResponseEntity;
  }


  //找到所有过期模板
  @RequestMapping(value = "/queryQuestContextEnd", method = RequestMethod.POST, headers = "Accept=application/json")
  public HttpResponseEntity queryQuestContextEnd(@RequestBody Map<String, Object> map) {
    HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
    QuestionnaireEntity questionnaireEntity = questionnaireService.queryQuestContextEnd(
        map);
    try {
      if (questionnaireEntity != null) {
        httpResponseEntity.setCode(Constans.SUCCESS_CODE);
        httpResponseEntity.setData(questionnaireEntity);
        httpResponseEntity.setMessage(Constans.STATUS_MESSAGE);
      } else {
        httpResponseEntity.setCode(Constans.EXIST_CODE);
        httpResponseEntity.setData(questionnaireEntity);
        httpResponseEntity.setMessage(Constans.QUERYFAIL_MESSAGE);
      }
    } catch (Exception e) {
      httpResponseEntity.setCode(Constans.EXIST_CODE);
      httpResponseEntity.setMessage(Constans.EXIST_MESSAGE);
    }
    return httpResponseEntity;
  }


  @RequestMapping(value = "/addQuestionnaire", method = RequestMethod.POST, headers = "Accept=application/json")
  public HttpResponseEntity addQuestionnaire(@RequestBody Map<String, Object> map) {
    HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
    Map<String, Object> map1 = questionnaireService.addQuestionnaireInfo(map);
    //还需要返回一下问卷的Id,用来进行下一步编辑
    int status = (int) map1.get("status");
    httpResponseEntity.setData(map1);
    try {
      if (status == 1) {
        httpResponseEntity.setCode(Constans.SUCCESS_CODE);
        httpResponseEntity.setMessage(Constans.ADD_MESSAGE);
      } else {
        httpResponseEntity.setCode(Constans.EXIST_CODE);
        httpResponseEntity.setMessage(Constans.COPY_EXIT_UPDATE_MESSAGE);
      }
    } catch (Exception e) {
      logger.info("addQuestionnaireInfo 添加问卷>>>>>>>>>>>" + e.getLocalizedMessage());
      httpResponseEntity.setCode(Constans.LOGOUT_NO_CODE);
      httpResponseEntity.setMessage(Constans.EXIST_MESSAGE);
    }
    return httpResponseEntity;
  }

  //找到所有过期模板
  @RequestMapping(value = "/queryHistoryQuestionnaire", method = RequestMethod.POST, headers = "Accept=application/json")
  public HttpResponseEntity queryHistoryQuestionnaire(@RequestBody Map<String, Object> map) {
    HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
    //找到所有过期模板
    httpResponseEntity.setData(questionnaireService.queryHistoryQuestionnaire(map));
    httpResponseEntity.setCode(Constans.SUCCESS_CODE);
    return httpResponseEntity;
  }

  /**
   * 根据问卷id删除问卷
   *
   * @param questionnaireEntity
   * @return
   */
  @RequestMapping(value = "/deleteQuestionnaireById", method = RequestMethod.POST, headers = "Accept=application/json")
  public HttpResponseEntity deleteQuestionnaireById(
      @RequestBody QuestionnaireEntity questionnaireEntity) {
    HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
    String questionnaireId = questionnaireEntity.getId();
    int status = questionnaireService.deleteQuestionnaireInfo(questionnaireId);
    try {
      if (status == 1) {
        httpResponseEntity.setCode(Constans.SUCCESS_CODE);
        httpResponseEntity.setMessage(Constans.DELETE_MESSAGE);
      } else if (status == 0) {
        httpResponseEntity.setCode(Constans.EXIST_CODE);
        httpResponseEntity.setMessage(Constans.COPY_EXIT_DELETE_MESSAGE);
      }
    } catch (Exception e) {
      logger.info("addUserInfo 根据问卷id删除问卷>>>>>>>>>>>" + e.getLocalizedMessage());
      httpResponseEntity.setCode(Constans.LOGOUT_NO_CODE);
      httpResponseEntity.setMessage(Constans.EXIST_MESSAGE);
    }
    return httpResponseEntity;
  }

  @RequestMapping(value = "/modifyQuestionnaireStatus", method = RequestMethod.POST, headers = "Accept=application/json")
  public HttpResponseEntity modifyQuestionnaireStatus(@RequestBody HashMap<String, Object> map) {
    HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
    String questionnaireId = (String) map.get("id");
    int status = questionnaireService.modifyQuestionnaireStatus(map);
    try {
      if (status == 1) {
        httpResponseEntity.setCode(Constans.SUCCESS_CODE);
        //httpResponseEntity.setData(status);
        httpResponseEntity.setMessage(Constans.UPDATE_STATUS_MESSAGE);
      }
    } catch (Exception e) {
      logger.info("addUserInfo 根据问卷id删除问卷>>>>>>>>>>>" + e.getLocalizedMessage());
      httpResponseEntity.setCode(Constans.LOGOUT_NO_CODE);
      httpResponseEntity.setMessage(Constans.EXIST_MESSAGE);
    }
    return httpResponseEntity;
  }


  @RequestMapping(value = "/modifyQuestionnaire", method = RequestMethod.POST, headers = "Accept=application/json")
  public HttpResponseEntity modifyQuestionnaire(@RequestBody HashMap<String, Object> map) {
    HttpResponseEntity httpResponseEntity = new HttpResponseEntity();

    int status = questionnaireService.modifyQuestionnaire(map);
    try {
      if (status == 1) {
        httpResponseEntity.setCode(Constans.SUCCESS_CODE);
        //httpResponseEntity.setData(status);
        httpResponseEntity.setMessage("编辑成功");
      }
    } catch (Exception e) {
      logger.info("addUserInfo 根据问卷id删除问卷>>>>>>>>>>>" + e.getLocalizedMessage());
      httpResponseEntity.setCode(Constans.LOGOUT_NO_CODE);
      httpResponseEntity.setMessage(Constans.EXIST_MESSAGE);
    }
    return httpResponseEntity;
  }


  //用邮件发布信息
  @RequestMapping(value = "/addSendQuestionnaire", method = RequestMethod.POST, headers = "Accept=application/json")
  public HttpResponseEntity addSendQuestionnaire(@RequestBody HashMap<String, Object> map) {
    HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
    //最后的核心方法，根据传递的map判断发送的类型，还要拿到最后发送的数据，
    String sendType = (String) map.get("sendType");
    //根据发送的类型数据，给出不同的解决方案
    int status = 0;
    switch (sendType) {
      case "0":
        Object releaseTime = map.get("releaseTime");
        if (releaseTime.toString().equals("")) {
          Date currentDate = DateUtil.getCurrentDate();
          map.put("releaseTime", currentDate);
        } else {
          map.put("releaseTime", new Date((long) releaseTime));
        }
        status = questionnaireService.addSendQuestionnaire(map);
        break;
      case "1":
        //邮箱发送方式
        map.put("releaseTime", DateUtil.getCurrentDate());
        status = questionnaireService.addSendQuestionnaire(map);
        questionnaireService.sendByEmail(map);
        break;
      case "2":
        //链接发送方式
        Date currentDate = DateUtil.getCurrentDate();
        map.put("releaseTime", currentDate);
        status = questionnaireService.addSendQuestionnaire(map);
        break;
      default:
        break;
    }
    //不管如何发送，最后问卷状态都调整为已发送
    if (status == 1) {
      map.put("id", map.get("questionId"));
      map.put("questionStop", "3");
      questionnaireService.modifyQuestionnaireStatus(map);
      httpResponseEntity.setCode(Constans.SUCCESS_CODE);
      httpResponseEntity.setMessage("发送成功");
    }
    return httpResponseEntity;
  }

  @RequestMapping(value = "/selSum", method = RequestMethod.POST, headers = "Accept=application/json")
  public int addSendQuestionnaire() {
    return 100;
  }
/*
* answerEmail: "150840779@qq.com"
answerList: ["0"]
answerSource: "email"
answerTime: 1656767600000
endTime: 1656767756000
ipAddress: "39.184.4.17"
questionId: "f1a52b2369c9425987a1133d471dd187"*/

  //将用户的回答进行存储
  @RequestMapping(value = "/addAnswerQuestionnaire", method = RequestMethod.POST, headers = "Accept=application/json")
  public HttpResponseEntity addAnswerQuestionnaire(
      @RequestBody Map<String, Object> map) {
    //逻辑，先查一下数据库里面有没有已经回答过这个项目的ip地址，如果有的话就返回错误，如果没有的话就添加到数据表，还有给问卷回答人数+1，还要检查一下是不是在规定时间里
    HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
    String questionId = map.get("questionId").toString();
    QuestionnaireEntity questionnaireEntity = questionnaireService.queryQuestionnaireAll(
        questionId);
    long answerTime = (long) map.get("answerTime");
    long endTime = (long) map.get("endTime");
    if (answerTime > endTime) {
      httpResponseEntity.setCode(Constans.EXIST_CODE);
      httpResponseEntity.setMessage("问卷已经截止提交");
      return httpResponseEntity;
    }
    Date startTime = questionnaireEntity.getStartTime();
    long time = startTime.getTime();
    if (time > answerTime) {
      httpResponseEntity.setCode(Constans.EXIST_CODE);
      httpResponseEntity.setMessage("问卷暂未开放");
      return httpResponseEntity;
    }

    if (!questionnaireEntity.getQuestionStop().equals("3")) {
      httpResponseEntity.setCode(Constans.EXIST_CODE);
      httpResponseEntity.setMessage("问卷未发送，请勿作答");
      return httpResponseEntity;
    }
    //接下来判断是不是已经回答过了，
    int count = questionnaireService.isAnswerd(map);

    if (count == 1) {
      httpResponseEntity.setCode(Constans.EXIST_CODE);
      httpResponseEntity.setMessage("请不要重复提交问卷");
      return httpResponseEntity;
    }
    //给数量+1
    String answerTotal = questionnaireEntity.getAnswerTotal();
    int i = Integer.parseInt(answerTotal) + 1;
    questionnaireEntity.setAnswerTotal("" + i);

    questionnaireService.modifyAnswerCount(questionnaireEntity);

    map.put("answerList", map.get("answerList").toString());
    map.put("answerTime", new Date(answerTime));
    map.put("endTime", new Date(endTime));

    int status = questionnaireService.addAnswerQuestionnaire(map);

    if (status == 1) {
      httpResponseEntity.setCode(Constans.SUCCESS_CODE);
      httpResponseEntity.setMessage(questionnaireEntity.getQuestionEndContent());
    } else if (status == 0) {
      httpResponseEntity.setCode(Constans.EXIST_CODE);
      httpResponseEntity.setMessage("提交失败");
    }
    return httpResponseEntity;
  }


  @RequestMapping(value = "/queryQuestionnaireList", method = RequestMethod.POST, headers = "Accept=application/json")
  public HttpResponseEntity queryQuestionnaireList(@RequestBody Map<String, Object> map) {
    HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
    if (map.get("questionName") == null || map.get("questionName").equals("")) {
      map.remove("questionName");
    }
    List<Map<String, Object>> status = questionnaireService.queryQuestionnaireList(map);
    try {
      if (status != null && status.size() > 0) {
        httpResponseEntity.setData(status);
        httpResponseEntity.setCode(Constans.SUCCESS_CODE);
        httpResponseEntity.setMessage("查询成功");
      }
    } catch (Exception e) {
      httpResponseEntity.setCode(Constans.LOGOUT_NO_CODE);
      httpResponseEntity.setMessage(Constans.EXIST_MESSAGE);
    }
    return httpResponseEntity;
  }


  @RequestMapping(value = "/modifyHistoryQuestionnaireStatus", method = RequestMethod.POST, headers = "Accept=application/json")
  public HttpResponseEntity modifyHistoryQuestionnaireStatus(@RequestBody Map<String, Object> map) {
    HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
    int status = questionnaireService.modifyHistoryQuestionnaireStatus(map);
    try {
      if (status > 0) {
        httpResponseEntity.setData(status);
        httpResponseEntity.setCode(Constans.SUCCESS_CODE);
        httpResponseEntity.setMessage("修改成功");
      }
    } catch (Exception e) {
      httpResponseEntity.setCode(Constans.LOGOUT_NO_CODE);
      httpResponseEntity.setMessage(Constans.EXIST_MESSAGE);
    }
    return httpResponseEntity;
  }


  @RequestMapping(value = "/queryAllQuestionnaireByCreated", method = RequestMethod.POST, headers = "Accept=application/json")
  public HttpResponseEntity queryAllQuestionnaireByCreated() {
    HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
    Map<String, Object> map = new HashMap<>();
    List<Map<String, Object>> status = questionnaireService.queryAllQuestionnaireByCreated(map);
    try {
      if (status != null && status.size() > 0) {
        httpResponseEntity.setData(status);
        httpResponseEntity.setCode(Constans.SUCCESS_CODE);
        httpResponseEntity.setMessage("查询成功");
      }
    } catch (Exception e) {
      httpResponseEntity.setCode(Constans.LOGOUT_NO_CODE);
      httpResponseEntity.setMessage(Constans.EXIST_MESSAGE);
    }
    return httpResponseEntity;
  }


}
