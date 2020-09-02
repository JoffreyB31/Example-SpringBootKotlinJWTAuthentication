package com.jb.demo.controllers

import com.jb.demo.models.Role
import com.jb.demo.models.User
import com.jb.demo.models.enums.ERole
import com.jb.demo.payload.request.LoginRequest
import com.jb.demo.payload.request.SignupRequest
import com.jb.demo.payload.response.JwtResponse
import com.jb.demo.payload.response.MessageResponse
import com.jb.demo.repository.RoleRepository
import com.jb.demo.repository.UserRepository
import com.jb.demo.security.jwt.JwtUtils
import com.jb.demo.security.services.UserDetailsImpl
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.*
import java.util.stream.Collectors
import javax.validation.Valid

@CrossOrigin(origins = ["*"], maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
class AuthController(

    private val authenticationManager: AuthenticationManager,
    private val userRepository: UserRepository,
    private val roleRepository: RoleRepository,
    private val encoder: PasswordEncoder,
    private val jwtUtils: JwtUtils

) {

  @PostMapping("/login")
  fun login(@RequestBody loginRequest: @Valid LoginRequest): ResponseEntity<*> {
    val authentication = authenticationManager
        .authenticate(UsernamePasswordAuthenticationToken(loginRequest.username, loginRequest.password))

    SecurityContextHolder.getContext().authentication = authentication
    val jwt = jwtUtils.generateJwtToken(authentication)
    val userDetails = authentication.principal as UserDetailsImpl
    val roles = userDetails.authorities.stream()
        .map { obj: GrantedAuthority -> obj.authority }
        .map { name: String -> ERole.valueOf(name) }
        .collect(Collectors.toList())

    return ResponseEntity.ok(JwtResponse(userDetails.username, jwt))
  }

  @PostMapping("/register")
  fun register(@RequestBody signUpRequest: @Valid SignupRequest): ResponseEntity<*> {
    if (userRepository.existsByUsername(signUpRequest.username)) {

      return ResponseEntity
          .badRequest()
          .body(MessageResponse("Error: Username is already taken!"))
    }

    val roles = signUpRequest.roles
        .stream()
        .map { x: ERole ->
          roleRepository
              .findByName(x)
              .orElse(Role(name = x))
        }
        .collect(Collectors.toSet())

    val user = User(signUpRequest.username, encoder.encode(signUpRequest.password), roles)
    userRepository.save(user)

    return ResponseEntity.ok(MessageResponse("User registered successfully!"))
  }
}