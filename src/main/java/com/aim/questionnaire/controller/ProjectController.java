package com.aim.questionnaire.controller;

import com.aim.questionnaire.beans.HttpResponseEntity;
import com.aim.questionnaire.common.Constans;
import com.aim.questionnaire.dao.entity.ProjectEntity;
import com.aim.questionnaire.service.ProjectService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * Created by wln on 2018\8\6 0006.
 */
@RestController
public class ProjectController {

    private final Logger logger = LoggerFactory.getLogger(ProjectController.class);

    @Autowired
    private ProjectService projectService;


    /**
     * 查询全部项目的信息
     *
     * @param projectEntity
     * @return
     */
    @RequestMapping(value = "/queryProjectList", method = RequestMethod.POST, headers = "Accept=application/json")
    public HttpResponseEntity queryProjectList(@RequestBody ProjectEntity projectEntity) {
        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
        List<Map<String, Object>> list = projectService.queryProjectList(projectEntity);
        try{
            if (list.size() != 0) {
                httpResponseEntity.setCode(Constans.SUCCESS_CODE);
                httpResponseEntity.setData(list);
                httpResponseEntity.setMessage(Constans.STATUS_MESSAGE);
            } else {
                httpResponseEntity.setCode(Constans.EXIST_CODE);
                httpResponseEntity.setData(null);
                httpResponseEntity.setMessage(Constans.QUERYFAIL_MESSAGE);
            }
        }catch (Exception e){
            logger.info("addUserInfo 查询问卷的基本信息>>>>>>>>>>>" + e.getLocalizedMessage());
            httpResponseEntity.setCode(Constans.EXIST_CODE);
            httpResponseEntity.setMessage(Constans.EXIST_MESSAGE);
        }
        return httpResponseEntity;
    }

    /**
     * 根据id删除项目
     *
     * @param projectEntity
     * @return
     */
    @RequestMapping(value = "/deleteProjectById", method = RequestMethod.POST, headers = "Accept=application/json")
    public HttpResponseEntity deleteProjectById(@RequestBody ProjectEntity projectEntity) {
        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
        try {
            int result = projectService.deleteProjectById(projectEntity);
            //成功删除
            if(result ==1) {
                httpResponseEntity.setCode(Constans.SUCCESS_CODE);
                httpResponseEntity.setMessage(Constans.DELETE_MESSAGE);
            }else if(result==0){//存在进行中问卷
                httpResponseEntity.setCode(Constans.PROJECT_EXIST_CODE);
                httpResponseEntity.setMessage(Constans.PROJECT_EXIST_MESSAGE);
            }else
            {
                httpResponseEntity.setCode(Constans.LOGOUT_NO_CODE);
                httpResponseEntity.setMessage(Constans.PROJECT_EXIST_MESSAGE);
            }
        } catch (Exception e) {
            logger.info("deleteUserInfo 删除问卷的基本信息>>>>>>>>>>>" + e.getLocalizedMessage());
            httpResponseEntity.setCode(Constans.EXIST_CODE);
            httpResponseEntity.setMessage(Constans.EXIST_MESSAGE);
        }
        return httpResponseEntity;
    }

    /**
     * 添加项目
     *
     * @param projectEntity
     * @return
     */
    @RequestMapping(value = "/addProjectInfo", method = RequestMethod.POST, headers = "Accept=application/json")
    public HttpResponseEntity addProjectInfo(@RequestBody ProjectEntity projectEntity) {
        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
        try {
            int result = projectService.addProjectInfo(projectEntity, projectEntity.getCreatedBy());
            if(result != 1) {
                httpResponseEntity.setCode(Constans.EXIST_CODE);
                httpResponseEntity.setMessage(Constans.PROJECTNAME_EXIT_MESSAGE);
            }else{
                httpResponseEntity.setCode(Constans.SUCCESS_CODE);
                String projectId = projectService. queryProjectIdByName(projectEntity);
                httpResponseEntity.setData(projectId);
                httpResponseEntity.setMessage(Constans.ADD_MESSAGE);
            }
        } catch (Exception e) {
            logger.info("addUserInfo 创建问卷的基本信息>>>>>>>>>>>" + e.getLocalizedMessage());
            httpResponseEntity.setCode(Constans.EXIST_CODE);
            httpResponseEntity.setMessage(Constans.EXIST_MESSAGE);
        }
        return httpResponseEntity;
    }

    /**
     * 根据项目id修改项目
     *
     * @param projectEntity
     * @return
     */
    @RequestMapping(value = "/modifyProjectInfo", method = RequestMethod.POST, headers = "Accept=application/json")
    public HttpResponseEntity modifyProjectInfo(@RequestBody ProjectEntity projectEntity) {
        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
        try {
            int result = projectService.modifyProjectInfo(projectEntity,projectEntity.getLastUpdatedBy());
            if(result == 1) {
                httpResponseEntity.setCode(Constans.SUCCESS_CODE);
                httpResponseEntity.setMessage(Constans.QUESTION_COUNT_MESSAGE);
            }else if(result == 0) {
                httpResponseEntity.setCode(Constans.PROJECT_EXIST_CODE);
                httpResponseEntity.setMessage(Constans.QUESTION_EXIST_MESSAGE);
            }else
            {
                httpResponseEntity.setCode(Constans.EXIST_CODE);
                httpResponseEntity.setMessage(Constans.PROJECTNAME_EXIT_MESSAGE);
            }
        } catch (Exception e) {
            logger.info("addUserInfo 修改项目的基本信息>>>>>>>>>>>" + e.getLocalizedMessage());
            httpResponseEntity.setCode(Constans.EXIST_CODE);
            httpResponseEntity.setMessage(Constans.EXIST_MESSAGE);
        }
        return httpResponseEntity;
    }


    /**
     * 查询全部项目的名字接口
     *
     * @return
     */
    @RequestMapping(value = "/queryAllProjectName", method = RequestMethod.POST, headers = "Accept=application/json")
    public HttpResponseEntity queryAllProjectName() {
        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
        return httpResponseEntity;
    }
}
