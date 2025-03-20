package com.walab.nanuri.item.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/item")
public class ItemController {
    @PostMapping()
    public String createItem(){
        
    }
}
