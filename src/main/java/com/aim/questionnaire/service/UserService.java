package com.aim.questionnaire.service;

import com.aim.questionnaire.common.utils.DateUtil;
import com.aim.questionnaire.common.utils.UUIDUtil;
//import com.aim.questionnaire.config.shiro.SysUserService;
//import com.aim.questionnaire.config.shiro.entity.UserOnlineBo;
import com.aim.questionnaire.dao.QuestionnaireEntityMapper;
import com.aim.questionnaire.dao.UserEntityMapper;
import com.aim.questionnaire.dao.entity.UserEntity;
//import com.alibaba.fastjson.JSONArray;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by wln on 2018\8\9 0009.
 */
@Service
public class UserService {

    @Autowired
    private UserEntityMapper userEntityMapper;

    //@Autowired
    //private SysUserService sysUserService;

    @Autowired
    private QuestionnaireEntityMapper questionnaireEntityMapper;

    /**
     * 查询用户列表（模糊搜索）
     * @param map
     * @return
     */
    public PageInfo queryUserList(Map<String,Object> map) {
        PageHelper.startPage(Integer.parseInt(map.get("pageNum").toString()), Integer.parseInt(map.get("pageSize").toString()));
        List<Map<String, Object>> mapList = userEntityMapper.queryUserList(map);
        PageInfo<Map<String,Object>> objectPageInfo = new PageInfo<>(mapList);
        return objectPageInfo;
    }

    /**
     * 创建用户的基本信息
     * @param map
     * @return
     */
    public int addUserInfo(Map<String,Object> map) {
        //检测
        if(map.get("username") != null) {
            //去搜索
            int userResult  =  userEntityMapper.queryExistUser(map);

            if(userResult != 0) {
                //用户名已经存在
                return 3;
            }
        }
        String id = UUIDUtil.getOneUUID();
        map.put("id",id);
        //创建时间
        Date date = DateUtil.getCreateTime();
        map.put("creationDate",date);
        map.put("lastUpdateDate",date);
        //创建人
//        String createduserid = map.get("id").toString();
//        Map<String,Object> querymap = new JSONObject();
//        querymap.put("id",createduserid);
//        Map<String,Object> queryresult = userEntityMapper.queryUserById(querymap);
//        String user = queryresult.get("username").toString();
        String user = map.get("opusername").toString();

        map.put("createdBy",user);
        map.put("lastUpdatedBy",user);
        //前台传入的时间戳转换
        String startTimeStr = map.get("startTime").toString();
        String endTimeStr = map.get("stopTime").toString();
        Date startTime = DateUtil.getMyTime(startTimeStr);
        Date endTime = DateUtil.getMyTime(endTimeStr);
        map.put("startTime",startTime);
        map.put("stopTime",endTime);

        int result = userEntityMapper.addUserInfo(map);
        return result;
    }

    /**
     * 编辑用户的基本信息
     * @param map
     * @return
     */
    public int modifyUserInfo(Map<String, Object> map) {
        if(map.get("username") != null) {
            int userResult = userEntityMapper.queryExistUser(map);
            if(userResult != 0) {
                //用户名已经存在
                return 3;
            }
        }
        //通过工具类UUIDUtil生成一个唯一id
//        String id = UUIDUtil.getOneUUID();
//        map.put("id",id);
        //创建时间
        Date date = DateUtil.getCreateTime();
//        map.put("creationDate",date);
        map.put("lastUpdateDate",date);
        //编辑人
//        String createduserid = map.get("id").toString();
//        Map<String,Object> querymap = new JSONObject();
//        querymap.put("id",createduserid);
//        Map<String,Object> queryresult = userEntityMapper.queryUserById(querymap);
        String user = map.get("opusername").toString();
//        map.put("createdBy",user);
        map.put("lastUpdatedBy",user);
        //前台传入的时间戳转换
        String startTimeStr = map.get("startTime").toString();
        String endTimeStr = map.get("stopTime").toString();
        Date startTime = DateUtil.getMyTime(startTimeStr);
        Date endTime = DateUtil.getMyTime(endTimeStr);
        map.put("startTime",startTime);
        map.put("stopTime",endTime);

        int result = userEntityMapper.modifyUserInfo(map);
        return result;
    }

    /**
     * 修改用户状态
     * @param map
     * @return
     */
    public int modifyUserStatus(Map<String, Object> map) {
        String id = (String) map.get("id");
        int curstatus = Integer.parseInt(userEntityMapper.queryUserRole(id));
        Map<String,Object> map1 = new JSONObject();
        map1.put("id",id);
        if(curstatus==1){
            int nextstatus = 0;
            map1.put("status",nextstatus);
        }else {
            int nextstatus = 1;
            map1.put("status",nextstatus);
        }
        userEntityMapper.modifyUserStatus(map1);
        return 1;
    }

    /**
     * 根据id查询用户信息
     * @param userEntity
     * @return
     */
    public Map<String,Object> selectUserInfoById(UserEntity userEntity) {
        Map<String,Object> result = userEntityMapper.selectUserInfoById(userEntity);
        return result;
    }


    /**
     * 询用户信息
     * @param userEntity
     * @return
     */
    public List<UserEntity> selectUserInfo(UserEntity userEntity) {
        List<UserEntity> result = userEntityMapper.selectUserInfo(userEntity);
        return result;
    }

    /**
     * 删除用户信息
     * @param userEntity
     * @return
     */
    public int deteleUserInfoById(UserEntity userEntity) {
        //这里需要补全权限信息的判定（比如：如果权限status是1就不能删除，返回其他数字，然后在controller中进行异常信息的抛出）
        userEntityMapper.deteleUserInfoById(userEntity);
        return 0;
    }
    //测试pull
}
