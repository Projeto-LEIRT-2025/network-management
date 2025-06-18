package com.github.project.webapplication.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver


@Configuration
class ThymeleafConfig {

    @Bean
    fun jsTemplateResolver(): ClassLoaderTemplateResolver {
        val jsTemplateResolver = ClassLoaderTemplateResolver()
        jsTemplateResolver.prefix = "templates/"
        jsTemplateResolver.suffix = ".js"
        jsTemplateResolver.setTemplateMode("JAVASCRIPT")
        jsTemplateResolver.characterEncoding = "UTF-8"
        jsTemplateResolver.order = 1
        jsTemplateResolver.checkExistence = true
        return jsTemplateResolver
    }

}