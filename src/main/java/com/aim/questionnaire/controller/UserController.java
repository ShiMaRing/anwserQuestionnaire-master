package com.aim.questionnaire.controller;

import com.aim.questionnaire.beans.HttpResponseEntity;
import com.aim.questionnaire.common.Constans;
import com.aim.questionnaire.dao.entity.UserEntity;
import com.aim.questionnaire.service.UserService;
import com.github.pagehelper.PageInfo;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.http.codec.json.Jackson2CodecSupport;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


/**
 * Created by wln on 2018\8\9 0009.
 */
@RestController
@RequestMapping("/admin")
public class UserController {

  private final Logger logger = LoggerFactory.getLogger(UserController.class);

  @Autowired
  private UserService userService;

  /**
   * 用户登录
   *
   * @param userEntity
   * @return
   */
  @RequestMapping(value = "/userLogins", method = RequestMethod.POST, headers = "Accept=application/json")
  public HttpResponseEntity userLogin(@RequestBody UserEntity userEntity) {
    HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
    try {
      List<UserEntity> hasUser = userService.selectUserInfo(userEntity);
      if (CollectionUtils.isEmpty(hasUser)) {
        httpResponseEntity.setCode(Constans.EXIST_CODE);
        httpResponseEntity.setData(null);
        httpResponseEntity.setMessage(Constans.LOGIN_USERNAME_PASSWORD_MESSAGE);
      } else {
        httpResponseEntity.setCode(Constans.SUCCESS_CODE);
        httpResponseEntity.setData(userEntity);
        httpResponseEntity.setMessage(Constans.LOGIN_MESSAGE);
      }

    } catch (Exception e) {
      logger.info("userLogin 用户登录>>>>>>>>>>>" + e.getLocalizedMessage());
      httpResponseEntity.setCode(Constans.EXIST_CODE);
      httpResponseEntity.setMessage(Constans.EXIST_MESSAGE);
    }
    return httpResponseEntity;
  }



  /*  coll2pt.innerText = "在校生";
    coll2pt.value = "2";
    belongType.appendChild(coll2pt);
    coll3pt.innerText = "毕业生";
    coll3pt.value = "3";
    belongType.appendChild(coll3pt);
    coll4pt.innerText = "教师";
    coll4pt.value = "4";
    belongType.appendChild(coll4pt);
    coll5pt.innerText = "用人单位";
    coll5pt.value = "5";*/
  @RequestMapping(value = "/queryAllDataType", method = RequestMethod.POST, headers = "Accept=application/json")
  public HttpResponseEntity queryAllDataType(@RequestBody UserEntity userEntity) {
    HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
    List<Map<String, Object>> maps = new ArrayList<>();
    HashMap<String, Object> objectObjectHashMap1 = new HashMap<>();
    objectObjectHashMap1.put("name","在校生");
    objectObjectHashMap1.put("id","2");
    HashMap<String, Object> objectObjectHashMap2 = new HashMap<>();
    objectObjectHashMap2.put("name","毕业生");
    objectObjectHashMap2.put("id","3");
    HashMap<String, Object> objectObjectHashMap3 = new HashMap<>();
    objectObjectHashMap3.put("name","教师");
    objectObjectHashMap3.put("id","4");
    HashMap<String, Object> objectObjectHashMap4 = new HashMap<>();
    objectObjectHashMap4.put("name","用人单位");
    objectObjectHashMap4.put("id","5");
    maps.add(objectObjectHashMap1);
    maps.add(objectObjectHashMap2);
    maps.add(objectObjectHashMap3);
    maps.add(objectObjectHashMap4);

      httpResponseEntity.setCode(Constans.SUCCESS_CODE);
      httpResponseEntity.setData(maps);
    return httpResponseEntity;
  }

  /**
   * 查询用户列表（模糊搜索）
   *
   * @param map
   * @return
   */
  @RequestMapping(value = "/queryUserList", method = RequestMethod.POST, headers = "Accept=application/json")
  public HttpResponseEntity queryUserList(@RequestBody Map<String, Object> map) {
    HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
    List<UserEntity> userEntityList = new ArrayList<>();
    try {
      PageInfo pageInfo = userService.queryUserList(map);
      if (pageInfo.getList().size() == 0) {
        httpResponseEntity.setCode(Constans.EXIST_CODE);
        httpResponseEntity.setData(null);
        httpResponseEntity.setMessage(Constans.EXIST_MESSAGE);
      } else {
        httpResponseEntity.setCode(Constans.SUCCESS_CODE);
        httpResponseEntity.setData(pageInfo);
        httpResponseEntity.setMessage(Constans.STATUS_MESSAGE);
      }

    } catch (Exception e) {
//logger.info("userLogin 用户登录>>>>>>>>>>>" + e.getLocalizedMessage());
      httpResponseEntity.setCode(Constans.EXIST_CODE);
      httpResponseEntity.setMessage(Constans.EXIST_MESSAGE);
    }
    return httpResponseEntity;
  }

  /**
   * 创建用户的基本信息
   *
   * @param map
   * @return
   */
  @RequestMapping(value = "/addUserInfo", method = RequestMethod.POST, headers = "Accept=application/json")
  public HttpResponseEntity addUserInfo(@RequestBody Map<String, Object> map) {
    HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
    try {
      int result = userService.addUserInfo(map);
      if (result == 3) {
        httpResponseEntity.setCode(Constans.USER_USERNAME_CODE);
        httpResponseEntity.setMessage(Constans.USER_USERNAME_MESSAGE);
      } else {
        httpResponseEntity.setCode(Constans.SUCCESS_CODE);
        httpResponseEntity.setMessage(Constans.ADD_MESSAGE);
      }
    } catch (Exception e) {
      logger.info("addUserInfo 创建用户的基本信息>>>>>>>>>>>" + e.getLocalizedMessage());
      httpResponseEntity.setCode(Constans.EXIST_CODE);
      httpResponseEntity.setMessage(Constans.EXIST_MESSAGE);
    }
    return httpResponseEntity;
  }

  /**
   * 编辑用户的基本信息
   *
   * @param map
   * @return
   */
  @RequestMapping(value = "/modifyUserInfo", method = RequestMethod.POST, headers = "Accept=application/json")
  public HttpResponseEntity modifyUserInfo(@RequestBody Map<String, Object> map) {
    HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
    try {
      int result = userService.modifyUserInfo(map);
      if (result == 3) {
        httpResponseEntity.setCode(Constans.USER_USERNAME_CODE);
        httpResponseEntity.setMessage(Constans.USER_USERNAME_MESSAGE);
      } else {
        httpResponseEntity.setCode(Constans.SUCCESS_CODE);
        httpResponseEntity.setMessage(Constans.ADD_MESSAGE);
      }
    } catch (Exception e) {
      logger.info("addUserInfo 编辑用户的基本信息>>>>>>>>>>>" + e.getLocalizedMessage());
      httpResponseEntity.setCode(Constans.EXIST_CODE);
      httpResponseEntity.setMessage(Constans.EXIST_MESSAGE);
    }
    return httpResponseEntity;
  }


  /**
   * 根据用户id查询用户基本信息
   *
   * @param userEntity
   * @return
   */
  @RequestMapping(value = "/selectUserInfoById", method = RequestMethod.POST, headers = "Accept=application/json")
  public HttpResponseEntity selectUserInfoById(@RequestBody UserEntity userEntity) {
    HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
    try {
      Map<String, Object> result = userService.selectUserInfoById(userEntity);
      if (result.get("id") == "" || result.get("id") == null) {
        httpResponseEntity.setCode(Constans.EXIST_CODE);
        httpResponseEntity.setMessage(Constans.LOGIN_USERNAME_MESSAGE);
      } else {
        httpResponseEntity.setCode(Constans.SUCCESS_CODE);
        httpResponseEntity.setData(result);
      }
    } catch (Exception e) {
      logger.info("addUserInfo 通过ID查询用户的基本信息>>>>>>>>>>>" + e.getLocalizedMessage());
      httpResponseEntity.setCode(Constans.EXIST_CODE);
      httpResponseEntity.setMessage(Constans.EXIST_MESSAGE);
    }
    return httpResponseEntity;
  }


  /**
   * 修改用户状态
   *
   * @param map
   * @return
   */
  @RequestMapping(value = "/modifyUserStatus", method = RequestMethod.POST, headers = "Accept=application/json")
  public HttpResponseEntity modifyUserStatus(@RequestBody Map<String, Object> map) {
    HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
    try {
      int result = userService.modifyUserStatus(map);
      if (result != 1) {
        //这种情况不存在 但为了应对将来出现管理员权限种类增加的情况需要加上这种判断
        httpResponseEntity.setCode(Constans.USER_USERNAME_CODE);
        httpResponseEntity.setMessage(Constans.USER_USERNAME_MESSAGE);
      } else {
        httpResponseEntity.setCode(Constans.SUCCESS_CODE);
        httpResponseEntity.setMessage(Constans.UPDATE_MESSAGE);
      }
    } catch (Exception e) {
      logger.info("deleteUserInfo 更新用户的权限>>>>>>>>>>>" + e.getLocalizedMessage());
      httpResponseEntity.setCode(Constans.EXIST_CODE);
      httpResponseEntity.setMessage(Constans.EXIST_MESSAGE);
    }
    return httpResponseEntity;
  }

  /**
   * 删除用户信息
   *
   * @param userEntity
   * @return
   */
  @RequestMapping(value = "/deleteUserInfoById", method = RequestMethod.POST, headers = "Accept=application/json")
  public HttpResponseEntity deteleUserInfoById(@RequestBody UserEntity userEntity) {
    HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
    try {
      int result = userService.deteleUserInfoById(userEntity);
      if (result == 3) {
        httpResponseEntity.setCode(Constans.USER_USERNAME_CODE);
        httpResponseEntity.setMessage(Constans.USER_USERNAME_MESSAGE);
      } else {
        httpResponseEntity.setCode(Constans.SUCCESS_CODE);
        httpResponseEntity.setMessage(Constans.ADD_MESSAGE);
      }
    } catch (Exception e) {
      logger.info("deleteUserInfo 删除用户的基本信息>>>>>>>>>>>" + e.getLocalizedMessage());
      httpResponseEntity.setCode(Constans.EXIST_CODE);
      httpResponseEntity.setMessage(Constans.EXIST_MESSAGE);
    }
    return httpResponseEntity;
  }

  /**
   * 用户没有权限
   *
   * @return
   */
  @RequestMapping(value = "/error")
  public HttpResponseEntity logout() {
    HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
    return httpResponseEntity;
  }
}
