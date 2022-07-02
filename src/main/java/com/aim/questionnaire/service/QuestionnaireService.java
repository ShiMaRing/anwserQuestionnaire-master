package com.aim.questionnaire.service;

import com.aim.questionnaire.common.utils.CommonUtils;
import com.aim.questionnaire.common.utils.DateUtil;
import com.aim.questionnaire.common.utils.UUIDUtil;
import com.aim.questionnaire.dao.ProjectEntityMapper;
import com.aim.questionnaire.dao.QuestionnaireEntityMapper;
import com.aim.questionnaire.dao.UserEntityMapper;
import com.aim.questionnaire.dao.entity.ProjectEntity;
import com.aim.questionnaire.dao.entity.QuestionnaireEntity;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by wln on 2018\8\6 0006.
 */
@Service
public class QuestionnaireService {

  @Autowired
  private QuestionnaireEntityMapper questionnaireEntityMapper;
  @Autowired
  private ProjectEntityMapper projectEntityMapper;
  @Autowired
  private UserEntityMapper userEntityMapper;


  public void sendByEmail(Map<String, Object> para) {

    List<Map<String, Object>> maps = (List<Map<String, Object>>) para.get("sendInfo");
    for (Map<String, Object> map : maps) {
      String emailTitle = (String) para.get("emailTitle");
      String textContext = (String) para.get("context");
      textContext.replace("【联系人姓名】", (String) map.get("answerName"));
      String url = urlCreate((String) para.get("questionId"));
      textContext.replace("【问卷地址】", url+map.get("answerEmail"));
      try {
        CommonUtils.sendEmail("3578379415", (String) map.get("answerEmail"), emailTitle,
            textContext);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  String urlCreate(String id) {
    String baseUrl = "http://localhost:8085/pages/previewQuestionnaire.html?";
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("id=").append(id);
    stringBuilder.append("&e");
    baseUrl += stringBuilder.toString();
    return baseUrl;
  }

  /**
   * 根据项目id查询问卷列表
   *
   * @param projectEntity
   * @return
   */
  public List<Map<String, Object>> queryQuestionnaireListById(ProjectEntity projectEntity) {
    String projectid = projectEntityMapper.selectProjectIdByName(projectEntity.getProjectName());
    List<Map<String, Object>> resultList = questionnaireEntityMapper.queryQuestionListByProjectId(
        projectid);
    //对查出的问卷列表进行处理，判断当前时间与列表时间
    long time = System.currentTimeMillis();
    for (Map<String, Object> map : resultList) {
      LocalDateTime endTime = (LocalDateTime) map.get("endTime");
      ZoneId zoneId = ZoneId.systemDefault();
      ZonedDateTime zdt = endTime.atZone(zoneId);
      Date date = Date.from(zdt.toInstant());
      long end = date.getTime();
      if (end < time) {
        map.put("questionStop", "0");
        questionnaireEntityMapper.modifyQuestionnaireStatus((HashMap<String, Object>) map);
      }
    }

    return resultList;
  }

  /**
   * 添加问卷
   *
   * @param questionnaireEntityEntity
   * @return
   */
  public int addQuestionnaireInfo(QuestionnaireEntity questionnaireEntityEntity, String user) {

    return 1;
  }

  /**
   * 根据问卷id查找问卷状态
   *
   * @param questionId
   * @return
   */
  public String queryQuestionnaireIsStopStatus(String questionId) {
    String status = questionnaireEntityMapper.queryQuestionnaireIsStopStatus(questionId);
    return status;
  }

  //取消问卷和项目的关联
  public int cancelQuestionnaireProject(String questionId) {
    int status = questionnaireEntityMapper.queryQuestionnaireStatusById(questionId);
    //问卷进行中
    if (status == 1) {
      return 0;
    }
    int result = questionnaireEntityMapper.cancelConnectionQuestionnaireProject(questionId);
    return 1;
  }

  //建立问卷和项目的关联
  public Map<String, Object> addQuestionnaireInfo(Map<String, Object> map) {

    Map<String, Object> result = new HashMap<>();
    String id = UUIDUtil.getOneUUID();

    result.put("id", id);
    map.put("id", id);
    //创建时间
    Date date = DateUtil.getCreateTime();
    map.put("creationDate", date);
    map.put("lastUpdateDate", date);
    //创建人
    map.put("createdBy", map.get("user"));
    //修改人
    map.put("lastUpdatedBy", map.get("user"));
    //前台传入的时间戳转换
    String startTimeStr = map.get("startTime").toString();
    String endTimeStr = map.get("endTime").toString();

    if (!Objects.equals(startTimeStr, "") && !Objects.equals(endTimeStr, "")) {
      Date startTime = DateUtil.getMyTime(startTimeStr);
      Date endTime = DateUtil.getMyTime(endTimeStr);
      map.put("startTime", startTime);
      map.put("endTime", endTime);
    } else {
      map.remove("startTime");
      map.remove("endTime");
    }

    int update = questionnaireEntityMapper.addQuestionnaire((HashMap<String, Object>) map);
    result.put("status", update);
    return result;
  }

  /**
   * 根据问卷id删除问卷
   *
   * @param questionId
   * @return
   */
  public int deleteQuestionnaireInfo(String questionId) {
    int status = questionnaireEntityMapper.queryQuestionnaireStatusById(questionId);
    //问卷进行中
    if (status == 1 || status == 3) {
      return 0;
    }
    return questionnaireEntityMapper.deleteByPrimaryKey(questionId);
  }

  /**
   * 修改问卷所属项目
   */
  public int modifyProjectInQuestionnaire(HashMap<String, Object> map) {
    return questionnaireEntityMapper.modifyProjectInQuestionnaire(map);
  }

  /**
   * 查询所有问卷的详细信息
   *
   * @param map
   * @return
   */
  public PageInfo queryQuestionnaireInfo(Map<String, Object> map) {
    //分页查询
    PageHelper.startPage(Integer.parseInt(map.get("pageNum").toString()),
        Integer.parseInt(map.get("pageSize").toString()));
    List<Map<String, Object>> mapList = questionnaireEntityMapper.queryAllQuestionnaireList(map);
    PageInfo<Map<String, Object>> objectPageInfo = new PageInfo<>(mapList);
    return objectPageInfo;
  }

  public int modifyQuestionnaireStatus(HashMap<String, Object> map) {
    return questionnaireEntityMapper.modifyQuestionnaireStatus(map);
  }


  public int addQuestionnaireProject(Map<String, Object> map) {
    return questionnaireEntityMapper.modifyQuestionnaire((HashMap<String, Object>) map);
  }


  public List<Map<String, Object>> queryHistoryQuestionnaire(Map<String, Object> map) {
    return questionnaireEntityMapper.queryHistoryQuestionnaire((HashMap<String, Object>) map);
  }

  public List<Map<String, Object>> queryQuestionnaireMould(Map<String, Object> map) {
    return questionnaireEntityMapper.queryQuestionnaireMould((String) map.get("dataId"));
  }

  public QuestionnaireEntity queryQuestionnaireAll(String questionId) {
    return questionnaireEntityMapper.queryQuestionnaireAll(questionId);
  }

  public int modifyQuestionnaire(HashMap<String, Object> map) {
    Object endTime = map.get("endTime");
    String endTimeStr = "";
    if (endTime != null) {
      endTimeStr = endTime.toString();
      Date end = DateUtil.getMyTime(endTimeStr);
      map.put("endTime", end);
    }
    map.put("questionList", map.get("questionList").toString());
    return questionnaireEntityMapper.modifyQuestionnaire(map);
  }


  public int modifyQuestionnaireInfo(QuestionnaireEntity questionnaireEntity) {
    return questionnaireEntityMapper.modifyQuestionnaireInfo(questionnaireEntity);
  }

  public Map<String, String> queryQuestionnaireById(Map<String, Object> map) {
    return questionnaireEntityMapper.queryQuestionnaireById(
        (HashMap<String, Object>) map);
  }

  public QuestionnaireEntity queryQuestContextEnd(Map<String, Object> map) {
    return questionnaireEntityMapper.queryQuestContextEnd(
        (String) map.get("id"));
  }

  public int addSendQuestionnaire(HashMap<String, Object> map) {
    return questionnaireEntityMapper.addSendQuestionnaire(map);
  }

  public int addAnswerQuestionnaire(Map<String, Object> map) {
    return questionnaireEntityMapper.addAnswerQuestionnaire(map);
  }

  public int isAnswerd(Map<String, Object> map) {
    return questionnaireEntityMapper.isAnswerd(map);
  }

  public void modifyAnswerCount(QuestionnaireEntity questionnaireEntity) {
     questionnaireEntityMapper.modifyAnswerCount(questionnaireEntity);
  }

  public List<Map<String, Object>> queryAllQuestionnaireByCreated(Map<String, Object> map) {
    return  questionnaireEntityMapper.queryAllQuestionnaireByCreated(map);
  }
}
