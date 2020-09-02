package com.jb.demo.security.services

import com.jb.demo.repository.UserRepository
import com.jb.demo.security.services.UserDetailsImpl.Companion.build
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
open class UserDetailsServiceImpl(

    private val userRepository: UserRepository

) : UserDetailsService {

  @Transactional
  @Throws(UsernameNotFoundException::class)
  override fun loadUserByUsername(username: String): UserDetails {
    val user = userRepository
        .findByUsername(username)
        .orElseThrow { UsernameNotFoundException("User Not Found with username: $username") }

    return build(user)
  }

}