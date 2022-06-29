package com.aim.questionnaire.controller;

import com.aim.questionnaire.beans.HttpResponseEntity;
import com.aim.questionnaire.common.Constans;
import com.aim.questionnaire.dao.entity.ProjectEntity;
import com.aim.questionnaire.dao.entity.QuestionnaireEntity;
import com.aim.questionnaire.service.ProjectService;

import com.aim.questionnaire.service.QuestionnaireService;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class QuestionnaireController {

    private final Logger logger = LoggerFactory.getLogger(QuestionnaireController.class);
    @Autowired
    QuestionnaireService questionnaireService;

    /**
     * 根据姓名查询所有问卷
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
            } else {
                responseEntity.setCode(Constans.EXIST_CODE);
                responseEntity.setData(null);
                responseEntity.setMessage(Constans.QUERYFAIL_MESSAGE);
            }
        } catch (Exception e) {
            logger.error("查询所有问卷异常>>>>>>>>>>>", e);
            responseEntity.setCode(Constans.EXIST_CODE);
            responseEntity.setMessage(Constans.EXIST_MESSAGE);
        }
        return responseEntity;
    }

    /**
     * 根据项目id查询对应问卷的信息
     * @param projectEntity
     * @return
     */
    @RequestMapping(value = "/queryQuestionnaireListByProjectId", method = RequestMethod.POST, headers = "Accept=application/json")
    public HttpResponseEntity queryProjectList(@RequestBody ProjectEntity projectEntity) {
        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
        List<Map<String, Object>> list = questionnaireService.queryQuestionnaireListById(projectEntity);
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
            logger.info("addUserInfo 根据项目id查询问卷的基本信息>>>>>>>>>>>" + e.getLocalizedMessage());
            httpResponseEntity.setCode(Constans.EXIST_CODE);
            httpResponseEntity.setMessage(Constans.EXIST_MESSAGE);
        }
        return httpResponseEntity;
    }

    /**
     * 查询问卷状态
     * @param questionnaireEntity
     * @return
     */
    @RequestMapping(value = "/selectQuestionnaireStatus", method = RequestMethod.POST, headers = "Accept=application/json")
    public HttpResponseEntity selectQuestionnaireStatus(@RequestBody QuestionnaireEntity questionnaireEntity) {
        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
        int status = Integer.parseInt(questionnaireService.queryQuestionnaireIsStopStatus(questionnaireEntity.getId()));
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
     * @param questionnaireEntity
     * @return
     */
    @RequestMapping(value = "/cancelQuestionnaireProject", method = RequestMethod.POST, headers = "Accept=application/json")
    public HttpResponseEntity cancelQuestionnaireProject(@RequestBody QuestionnaireEntity questionnaireEntity) {
        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
        String questionnaireId = questionnaireEntity.getId();
        int status = questionnaireService.cancelQuestionnaireProject(questionnaireId);
        try {
            if (status == 1) {
                httpResponseEntity.setCode(Constans.SUCCESS_CODE);
                httpResponseEntity.setMessage(Constans.CANCEL_PROJECT_MESSAGE);
            } else if(status == 0){
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

    /**
     * 根据问卷id删除问卷
     *
     * @param questionnaireEntity
     * @return
     */
@RequestMapping(value = "/deleteQuestionnaireById", method = RequestMethod.POST, headers = "Accept=application/json")
    public HttpResponseEntity deleteQuestionnaireById(@RequestBody QuestionnaireEntity questionnaireEntity) {
        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
        String questionnaireId = questionnaireEntity.getId();
        int status = questionnaireService.deleteQuestionnaireInfo(questionnaireId);
        try {
            if(status == 1){
                httpResponseEntity.setCode(Constans.SUCCESS_CODE);
                httpResponseEntity.setMessage(Constans.DELETE_MESSAGE);
            }else if(status == 0){
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
}
