package com.github.project.webapplication.services

import io.jsonwebtoken.Jwts
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.*
import javax.crypto.SecretKey

@Service
class JwtService(

    @Value("\${jwt.expiration}")
    private val expiration: Long,

    @Value("\${jwt.issuer}")
    private val issuer: String,

    private val secretKey: SecretKey

) {

    fun generateToken(username: String) = Jwts.builder()
        .subject(username)
        .issuedAt(Date())
        .issuer(issuer)
        .expiration(Date(System.currentTimeMillis() + expiration * 1000L))
        .signWith(secretKey)
        .compact()

    fun validateToken(token: String): Boolean {

        return try {
            Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
            true
        } catch (e: Exception) {
            false
        }

    }

    fun getUsername(token: String) = Jwts.parser()
        .verifyWith(secretKey)
        .build()
        .parseSignedClaims(token)
        .payload.subject

}