package com.jb.demo.config

object SecurityConfig {

  @JvmField
  var JWT_SECRET = "adada"

  @JvmField
  var JWT_TYPE = "Bearer"

  @JvmField
  var JWT_EXPIRATION = 60 * 24 * 10

}