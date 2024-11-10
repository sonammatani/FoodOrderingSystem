package com.project.app.controller;

import com.project.app.service.MenuItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/menu")
public class MenuController {

    @Autowired
    private MenuItemService menuItemService;

    @GetMapping("/allMenuItems")
    public List<String> getAllMenuItems() {
        return menuItemService.getMenuItems();
    }
}

