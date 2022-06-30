package com.aim.questionnaire.dao.entity;

public class Student {
    private String id;
    private String student_number;
    private String school;
    private String major;
    private String classroom;
    private Boolean sex;
    private String wx;
    private String qq;
    private String phone;
    private String mail;

    public Student(String id, String student_number, String school, String major, String classroom, Boolean sex, String wx, String qq, String phone, String mail) {
        this.id = id;
        this.student_number = student_number;
        this.school = school;
        this.major = major;
        this.classroom = classroom;
        this.sex = sex;
        this.wx = wx;
        this.qq = qq;
        this.phone = phone;
        this.mail = mail;
    }
    public Student() {
        super();
    }

    public Boolean getSex() {
        return sex;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public void setSex(Boolean sex) {
        this.sex = sex;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStudent_number() {
        return student_number;
    }

    public void setStudent_number(String student_number) {
        this.student_number = student_number;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getClassroom() {
        return classroom;
    }

    public void setClassroom(String classroom) {
        this.classroom = classroom;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public void setWx(String wx) {
        this.wx = wx;
    }

    public String getWx() {
        return wx;
    }
    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        Student other = (Student) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getStudent_number() == null ? other.getStudent_number() == null : this.getStudent_number().equals(other.getStudent_number()))
                && (this.getPhone() == null ? other.getPhone() == null : this.getPhone().equals(other.getPhone()))
                && (this.getMajor() == null ? other.getMajor() == null : this.getMajor().equals(other.getMajor()))
                && (this.getQq() == null ? other.getQq() == null : this.getQq().equals(other.getQq()))
                && (this.getWx() == null ? other.getWx() == null : this.getWx().equals(other.getWx()))
                && (this.getClassroom() == null ? other.getClassroom() == null : this.getClassroom().equals(other.getClassroom()))
                && (this.getMail() == null ? other.getMail() == null : this.getMail().equals(other.getMail()))
                && (this.getSchool() == null ? other.getSchool() == null : this.getSchool().equals(other.getSchool()));

    }
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getStudent_number() == null) ? 0 : getStudent_number().hashCode());
        result = prime * result + ((getPhone() == null) ? 0 : getPhone().hashCode());
        result = prime * result + ((getMajor() == null) ? 0 : getMajor().hashCode());
        result = prime * result + ((getQq() == null) ? 0 : getQq().hashCode());
        result = prime * result + ((getWx() == null) ? 0 : getWx().hashCode());
        result = prime * result + ((getClassroom() == null) ? 0 : getClassroom().hashCode());
        result = prime * result + ((getMail() == null) ? 0 : getMail().hashCode());
        result = prime * result + ((getSchool() == null) ? 0 : getSchool().hashCode());
        return result;
    }
}
