package com.github.project.webapplication.model

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

@Entity
@Table(name = "users")
data class User(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @get:JvmName("username")
    val username: String = "",

    @get:JvmName("password")
    val password: String = ""

) : UserDetails {

    override fun getAuthorities() = emptyList<GrantedAuthority>()

    override fun getPassword() = password

    override fun getUsername() = username

}