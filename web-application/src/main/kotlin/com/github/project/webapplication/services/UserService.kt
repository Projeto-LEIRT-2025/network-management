package com.github.project.webapplication.services

import com.github.project.webapplication.exceptions.UserNotFoundException
import com.github.project.webapplication.repositories.UserRepository
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class UserService(

    private val userRepository: UserRepository

) : UserDetailsService {

    override fun loadUserByUsername(username: String) =
        userRepository.findByUsername(username) ?: throw UserNotFoundException()

}