package com.jb.demo.security

import com.jb.demo.security.jwt.AuthEntryPointJwt
import com.jb.demo.security.jwt.AuthTokenFilter
import com.jb.demo.security.services.UserDetailsServiceImpl
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
open class WebSecurityConfig(

    private val userDetailsService: UserDetailsServiceImpl,
    private val unauthorizedHandler: AuthEntryPointJwt

) : WebSecurityConfigurerAdapter() {

  @Bean
  @Throws(Exception::class)
  override fun authenticationManagerBean(): AuthenticationManager {
    return super.authenticationManagerBean()
  }

  @Bean
  open fun authTokenFilter(): AuthTokenFilter {
    return AuthTokenFilter()
  }

  @Bean
  open fun passwordEncoder(): PasswordEncoder {
    return BCryptPasswordEncoder()
  }

  @Throws(Exception::class)
  public override fun configure(authenticationManagerBuilder: AuthenticationManagerBuilder) {
    authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder())
  }

  @Throws(Exception::class)
  override fun configure(http: HttpSecurity) {
    http.cors().and()
        .csrf().disable()
        .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
        .authorizeRequests().antMatchers("/api/auth/**").permitAll()
        .antMatchers("/api/test/**").permitAll()
        .anyRequest().authenticated()

    http.addFilterBefore(authTokenFilter(), UsernamePasswordAuthenticationFilter::class.java)
  }
}