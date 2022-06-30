package com.aim.questionnaire.service;


import com.aim.questionnaire.common.MyException;
import com.aim.questionnaire.dao.StudentMapper;
import com.aim.questionnaire.dao.entity.Student;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class ExcelServiceImpl implements ExcelService {

    @Autowired
    private StudentMapper studentMapper;


    @Transactional(readOnly = false,rollbackFor = Exception.class)
    @Override
    public boolean batchImport(String fileName, MultipartFile file) throws Exception {

        boolean notNull = false;
        List<Student> studentList = new ArrayList<Student>();
        if (!fileName.matches("^.+\\.(?i)(xls)$") && !fileName.matches("^.+\\.(?i)(xlsx)$")) {
            throw new MyException("上传文件格式不正确");
        }
        boolean isExcel2003 = true;
        if (fileName.matches("^.+\\.(?i)(xlsx)$")) {
            isExcel2003 = false;
        }
        InputStream is = file.getInputStream();
        Workbook wb = null;
        if (isExcel2003) {
            wb = new HSSFWorkbook(is);
        } else {
            wb = new XSSFWorkbook(is);
        }
        Sheet sheet = wb.getSheetAt(0);
        if(sheet!=null){
            notNull = true;
        }
        Student student;
        for (int r = 1; r <= sheet.getLastRowNum(); r++) {
            Row row = sheet.getRow(r);
            if (row == null){
                continue;
            }

            student = new Student();

            if( row.getCell(0).getCellType() !=1){
                throw new MyException("导入失败(第"+(r+1)+"行,id请设为文本格式)");
            }
            String id = row.getCell(0).getStringCellValue();

            if(id == null || id.isEmpty()){
                throw new MyException("导入失败(第"+(r+1)+"行,id未填写)");
            }

            row.getCell(1).setCellType(Cell.CELL_TYPE_STRING);
            String student_number = row.getCell(1).getStringCellValue();
            if(student_number ==null || student_number.isEmpty()){
                throw new MyException("导入失败(第"+(r+1)+"行,student_number未填写)");
            }
            String school = row.getCell(2).getStringCellValue();
            if(school ==null){
                throw new MyException("导入失败(第"+(r+1)+"行,不存在此school或school未填写)");
            }
            String major  = row.getCell(3).getStringCellValue();
            String classroom  = row.getCell(4).getStringCellValue();
            Boolean sex  = row.getCell(5).getBooleanCellValue();
            String wx = row.getCell(6).getStringCellValue();
            String qq = row.getCell(7).getStringCellValue();
            String phone = row.getCell(8).getStringCellValue();
            String mail = row.getCell(9).getStringCellValue();


            student.setMajor(major);
            student.setId(id);
            student.setSchool(school);
            student.setStudent_number(student_number);
            student.setClassroom(classroom);
            student.setSex(sex);
            student.setWx(wx);
            student.setQq(qq);
            student.setPhone(phone);
            student.setMail(mail);


            studentList.add(student);
        }
        for (Student studentResord : studentList) {
            String id = studentResord.getId();
            int cnt = studentMapper.selectById(id);
            if (cnt == 0) {
                studentMapper.addStudent(studentResord);
                System.out.println(" 插入 "+ studentResord);
            } else {
                studentMapper.updateStudentById(studentResord);
                System.out.println(" 更新 "+ studentResord);
            }
        }
        return notNull;
    }
}
