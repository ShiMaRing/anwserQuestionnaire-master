package com.aim.questionnaire.controller;

import com.aim.questionnaire.service.ExcelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/namedQuestionnaire")
public class ExcelController {

    @Autowired
    private ExcelService ExcelService;

    @PostMapping("/import")
    public boolean addUser(@RequestParam("file") MultipartFile file) {
        boolean a = false;
        String fileName = file.getOriginalFilename();
        try {
            a = ExcelService.batchImport(fileName, file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  a;
    }

}
