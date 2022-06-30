package com.aim.questionnaire.service;

import com.aim.questionnaire.common.utils.DateUtil;
import com.aim.questionnaire.common.utils.UUIDUtil;
import com.aim.questionnaire.dao.ProjectEntityMapper;
import com.aim.questionnaire.dao.QuestionnaireEntityMapper;
import com.aim.questionnaire.dao.UserEntityMapper;
import com.aim.questionnaire.dao.entity.ProjectEntity;
import com.aim.questionnaire.dao.entity.QuestionnaireEntity;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by wln on 2018\8\6 0006.
 */
@Service
public class ProjectService {

  @Autowired
  private ProjectEntityMapper projectEntityMapper;

  @Autowired
  private QuestionnaireEntityMapper questionnaireEntityMapper;
  @Autowired
  private UserEntityMapper userEntityMapper;


  /**
   * 添加项目
   *
   * @param projectEntity
   * @return
   */
  public int addProjectInfo(ProjectEntity projectEntity, String user) {
    if (projectEntity.getProjectName() != null) {
      int userResult = projectEntityMapper.queryExistProject(projectEntity);
      if (userResult != 0) {
        //项目名已经存在
        return 0;
      }
    }
    String id = UUIDUtil.getOneUUID();
    projectEntity.setId(id);
    //查找userID，输入到project中
    String userID = userEntityMapper.selectIdByName(user);
    projectEntity.setUserId(userID);
    //创建人
    projectEntity.setCreatedBy(user);
    //创建时间
    Date date = DateUtil.getCreateTime();
    projectEntity.setCreationDate(date);
    //最后修改时间
    projectEntity.setLastUpdateDate(date);
    //最后修改人
    projectEntity.setLastUpdatedBy(user);
    int result = projectEntityMapper.insert(projectEntity);
    return 1;
  }

  /**
   * 修改项目
   *
   * @param projectEntity
   * @return
   */
  public int modifyProjectInfo(ProjectEntity projectEntity, String user) {
    QuestionnaireEntity questionnaireEntity = new QuestionnaireEntity();
    questionnaireEntity.setProjectId(projectEntity.getId());
    int status = questionnaireEntityMapper.queryReleaseQuestionnaireCount(questionnaireEntity);
    //存在发布中问卷
    if (status > 0) {
      return 0;
    } else if (projectEntity.getProjectName() != null) {
      int projectResult = projectEntityMapper.queryExistProject(projectEntity);
      if (projectResult != 0) {
        //项目名已经存在
        return 2;
      }
    }
    //跟新时间
    Date date = DateUtil.getCreateTime();
    projectEntity.setLastUpdateDate(date);
    //最后修改人
    projectEntity.setLastUpdatedBy(user);
    return projectEntityMapper.updateByPrimaryKey(projectEntity);
  }

  /**
   * 删除项目
   *
   * @param projectEntity
   * @return
   */
  public int deleteProjectById(ProjectEntity projectEntity) {
    QuestionnaireEntity questionnaireEntity = new QuestionnaireEntity();
    questionnaireEntity.setProjectId(projectEntity.getId());
    int status = questionnaireEntityMapper.queryReleaseQuestionnaireCount(questionnaireEntity);
    //有正在进行中的问卷
    //这里需要补全权限信息的判定（比如：如果权限status是1就不能删除，返回其他数字，然后在controller中进行异常信息的抛出）
    if (status > 0) {
      return 0;
    } else {
      projectEntityMapper.deleteProjectById(projectEntity.getId());
      return 1;
    }
  }

  /**
   * 查询项目列表
   *
   * @param projectEntity
   * @return
   */
  public List<Map<String, Object>> queryProjectList(ProjectEntity projectEntity) {
    List<Map<String, Object>> resultList = projectEntityMapper.queryProjectList(projectEntity);
    return resultList;
  }

  /**
   * 查询全部项目的名字接口
   *
   * @return
   */
  public List<Map<String, Object>> queryAllProjectName() {
    List<Map<String, Object>> resultList = projectEntityMapper.queryAllProjectName();
    return resultList;
  }

  /**
   * 根据项目名称查询项目id
   */
  public String queryProjectIdByName(ProjectEntity projectEntity) {
    String projectid = projectEntityMapper.selectProjectIdByName(projectEntity.getProjectName());
    return projectid;
  }

}
