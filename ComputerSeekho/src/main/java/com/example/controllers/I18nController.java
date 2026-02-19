package com.example.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Locale;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/i18n")
@CrossOrigin("*")
public class I18nController {

    private static final Logger logger = LoggerFactory.getLogger(I18nController.class);

    @Autowired
    private MessageSource messageSource;

    @GetMapping("/greet")
    public String greet(@RequestParam(name = "lang", required = false) String lang) {
        logger.info("I18nController: greet called with lang={}", lang);
        Locale locale = LocaleContextHolder.getLocale();
        if (lang != null && !lang.isEmpty()) {
            locale = new Locale(lang);
        }
        return messageSource.getMessage("greeting", null, locale);
    }

    @GetMapping("/messages")
    public java.util.Map<String, String> getMessages(@RequestParam(name = "lang", required = false) String lang) {
        logger.info("I18nController: getMessages called with lang={}", lang);
        Locale locale = java.util.Locale.ENGLISH;
        if (lang != null && !lang.isEmpty()) {
            locale = new Locale(lang);
        }

        java.util.Map<String, String> messages = new java.util.HashMap<>();
        String[] keys = {
                "nav.home", "nav.courses", "nav.placements", "nav.student", "nav.contact", "nav.login", "nav.dashboard",
                "nav.logout",
                "home.welcome", "home.subtitle", "home.explore", "home.announcement", "home.no_announcement",
                "home.campus", "home.labs", "home.community"
        };

        for (String key : keys) {
            try {
                messages.put(key, messageSource.getMessage(key, null, locale));
            } catch (Exception e) {
                messages.put(key, "MISSING_KEY: " + key);
            }
        }
        return messages;
    }
}
