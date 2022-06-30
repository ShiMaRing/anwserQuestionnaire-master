package com.aim.questionnaire.dao;

import com.aim.questionnaire.dao.entity.Student;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface StudentMapper {

    void addStudent(Student sysUser);

    int updateStudentById(Student sysUser);

    int selectById(String id);
}