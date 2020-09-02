package com.jb.demo.payload.response

import com.jb.demo.config.SecurityConfig.JWT_TYPE

data class JwtResponse(

    val username: String,
    val accessToken: String,
    val tokenType: String = JWT_TYPE

)