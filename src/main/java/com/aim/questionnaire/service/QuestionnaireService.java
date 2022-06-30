package com.aim.questionnaire.service;

import com.aim.questionnaire.common.utils.DateUtil;
import com.aim.questionnaire.common.utils.UUIDUtil;
import com.aim.questionnaire.dao.ProjectEntityMapper;
import com.aim.questionnaire.dao.QuestionnaireEntityMapper;
import com.aim.questionnaire.dao.UserEntityMapper;
import com.aim.questionnaire.dao.entity.ProjectEntity;
import com.aim.questionnaire.dao.entity.QuestionnaireEntity;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

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

    /**
     * 根据项目id查询问卷列表
     *
     * @param projectEntity
     * @return
     */
    public List<Map<String, Object>> queryQuestionnaireListById(ProjectEntity projectEntity) {
        String projectid = projectEntityMapper.selectProjectIdByName(projectEntity.getProjectName());
        List<Map<String, Object>> resultList = questionnaireEntityMapper.queryQuestionListByProjectId(projectid);
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
    public int addQuestionnaireProject(Map<String, Object> map) {
        return questionnaireEntityMapper.addConnectionQuestionnaireProject(map);
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
        if (status == 1||status == 3) {
            return 0;
        }
        return  questionnaireEntityMapper.deleteByPrimaryKey(questionId);
    }

    /**
     * 修改问卷所属项目
     */
    public int modifyProjectInQuestionnaire(HashMap<String, Object> map) {
        return questionnaireEntityMapper.modifyProjectInQuestionnaire(map);
    }

    /**
     * 查询所有问卷的详细信息
     * @param map
     * @return
     */
    public PageInfo queryQuestionnaireInfo(Map<String, Object> map) {
        //分页查询
        PageHelper.startPage(Integer.parseInt(map.get("pageNum").toString()), Integer.parseInt(map.get("pageSize").toString()));
        List<Map<String, Object>> mapList = questionnaireEntityMapper.queryAllQuestionnaireList(map);
        PageInfo<Map<String,Object>> objectPageInfo = new PageInfo<>(mapList);
        return objectPageInfo;
    }

    public int modifyQuestionnaireStatus(HashMap<String, Object> map) {
       return questionnaireEntityMapper.modifyQuestionnaireStatus(map);
    }
}
