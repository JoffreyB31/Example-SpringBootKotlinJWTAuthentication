package com.jb.demo

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
open class SpringBootSecurityJwtApplication

fun main(args: Array<String>) {
  SpringApplication.run(SpringBootSecurityJwtApplication::class.java, *args)
}


