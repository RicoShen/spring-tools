package com.ricoandilet.commons.utils;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.StringTemplateResolver;
import org.thymeleaf.templateresolver.UrlTemplateResolver;

import java.util.Map;

/**
 * @author: rico
 * @date: 2022/11/24
 **/
public class HtmlUtil {

    /**
     * @param templatePath *.html in resources/templates/*
     * @param params
     * @return html
     */
    public static String generateByTemplate(String templatePath, Map<String, Object> params) {

        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        UrlTemplateResolver resolver = new UrlTemplateResolver();
        resolver.setPrefix("classpath:/templates/");
        resolver.setSuffix(".html");
        templateEngine.setTemplateResolver(resolver);
        return templateEngine.process(templatePath, new Context(null, params));
    }

    /**
     * @param html   html dom
     * @param params
     * @return html
     */
    public static String generateByHtml(String html, Map<String, Object> params) {

        StringTemplateResolver resolver = new StringTemplateResolver();
        resolver.setOrder(1);
        resolver.setTemplateMode(TemplateMode.HTML);

        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(resolver);
        return templateEngine.process(html, new Context(null, params));
    }

}
