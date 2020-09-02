package com.jb.demo.security.jwt

import com.jb.demo.config.SecurityConfig.JWT_EXPIRATION
import com.jb.demo.config.SecurityConfig.JWT_SECRET
import com.jb.demo.security.services.UserDetailsImpl
import io.jsonwebtoken.*
import org.slf4j.LoggerFactory
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import java.util.*

@Component
class JwtUtils {
  private val logger = LoggerFactory.getLogger(JwtUtils::class.java)
  
  fun generateJwtToken(authentication: Authentication): String {
    val userPrincipal = authentication.principal as UserDetailsImpl
    return Jwts.builder()
        .setSubject(userPrincipal.username)
        .setIssuedAt(Date())
        .setExpiration(Date(Date().time + JWT_EXPIRATION))
        .signWith(SignatureAlgorithm.HS512, JWT_SECRET)
        .compact()
  }

  fun getUserNameFromJwtToken(token: String?): String {
    return Jwts.parser()
        .setSigningKey(JWT_SECRET)
        .parseClaimsJws(token)
        .body.subject
  }

  fun validateJwtToken(authToken: String?): Boolean {
    try {
      Jwts.parser()
          .setSigningKey(JWT_SECRET)
          .parseClaimsJws(authToken)

      return true
    } catch (e: SignatureException) {
      logger.error("Invalid JWT signature: {}", e.message)
    } catch (e: MalformedJwtException) {
      logger.error("Invalid JWT token: {}", e.message)
    } catch (e: ExpiredJwtException) {
      logger.error("JWT token is expired: {}", e.message)
    } catch (e: UnsupportedJwtException) {
      logger.error("JWT token is unsupported: {}", e.message)
    } catch (e: IllegalArgumentException) {
      logger.error("JWT claims string is empty: {}", e.message)
    }

    return false
  }
}