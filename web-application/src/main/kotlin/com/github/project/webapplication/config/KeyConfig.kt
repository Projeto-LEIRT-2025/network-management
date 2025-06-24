package com.github.project.webapplication.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey

@Configuration
class KeyConfig {

    @Bean
    fun key(): SecretKey {

        val keyGenerator = KeyGenerator.getInstance("HmacSHA256")
        keyGenerator.init(256)

        return keyGenerator.generateKey()
    }

}