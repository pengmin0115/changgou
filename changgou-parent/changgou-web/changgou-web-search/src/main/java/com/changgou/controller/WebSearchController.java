package com.changgou.controller;

import com.changgou.search.feign.SearchSkuFeign;
import com.changgou.search.pojo.SkuInfo;
import entity.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.Map;

/**
 * @author pengmin
 * @date 2020/11/17 17:12
 */
@Controller
@RequestMapping("/search")
public class WebSearchController {

    @Autowired
    private SearchSkuFeign searchSkuFeign;

    @GetMapping("/list")
    public String search(@RequestParam Map<String,String> searchMap, Model model){
        Map<String, Object> resultMap = searchSkuFeign.search(searchMap);
        model.addAttribute("resultMap",resultMap);
        model.addAttribute("searchMap",searchMap);
        // 组装URL
        String url = "/search/list";
        if (searchMap !=null && searchMap.size()>0){
            url += "?";
            for (Map.Entry<String, String> stringStringEntry : searchMap.entrySet()) {
                String key = stringStringEntry.getKey();
                String value = stringStringEntry.getValue();
                if (!key.equals("pageNum")){
                    url += key + "=" + value + "&";
                }else{
                    continue;
                }
            }
            url = url.substring(0,url.length()-1);
        }
        model.addAttribute("url",url);
        Page<SkuInfo> page = new Page<SkuInfo>(
                Long.parseLong(resultMap.get("totalPages").toString()),
                Integer.parseInt(resultMap.get("pageNum").toString()),
                Integer.parseInt(resultMap.get("pageSize").toString())
        );
        model.addAttribute("page",page);
        return "search";
    }
}
