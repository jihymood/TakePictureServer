package com.jspptd.postal.collectserver.restcontrol;

import com.jspptd.postal.collectserver.dao.CompanyMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


/**
 * Created by HASEE on 2017/12/5.
 */
@RestController
//@Controller
@RequestMapping("/company")
public class CompanyController {

    private Logger log = LoggerFactory.getLogger(CompanyController.class);

    @Autowired
    CompanyMapper companyMapper;

    /**
     * 测试控制台打印sql语句
     * @return
     */
    @RequestMapping(value = "/insert", method = RequestMethod.GET)
    public ResponseObj<String> insertData() {
        int i = companyMapper.insertData();
        if (i == 1)
            return new ResponseObj<>("成功", null);
        return new ResponseObj<>("失败", null);

    }


}
