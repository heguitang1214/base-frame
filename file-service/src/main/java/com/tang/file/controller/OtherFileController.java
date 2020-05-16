package com.tang.file.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;


/**
 * 其他的一些文件操作
 */
@RestController
@RequestMapping("/api/file")
public class OtherFileController {


    /**
     * 大文件的断点续传
     *
     * @return 视图
     */
    @RequestMapping("/range")
    public ModelAndView rangeTest() {
        ModelAndView mv = new ModelAndView("byteRangeViewRender");
        mv.addObject("file", new File("D:\\test\\aaa.mp4"));
        mv.addObject("contentType", "video/mp4");
        return mv;
    }


}


