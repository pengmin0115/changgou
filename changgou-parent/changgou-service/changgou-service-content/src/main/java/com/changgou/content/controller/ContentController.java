package com.changgou.content.controller;

import com.changgou.content.pojo.Content;
import com.changgou.content.service.ContentService;
import com.changgou.core.AbstractCoreController;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/****
 * @Author:admin
 * @Description:
 * @Date 2019/6/14 0:18
 *****/

@RestController
@RequestMapping("/content")
@CrossOrigin
public class ContentController extends AbstractCoreController<Content> {

    private ContentService contentService;

    @Autowired
    public ContentController(ContentService contentService) {
        super(contentService, Content.class);
        this.contentService = contentService;
    }

    @RequestMapping("/list/category/{id}")
    public Result<List<Content>> findByCategory(@PathVariable(name = "id")Long id){
        List<Content> contentList = contentService.findByCategory(id);
        return new Result<>(true, StatusCode.OK,"ok",contentList);
    }
}
