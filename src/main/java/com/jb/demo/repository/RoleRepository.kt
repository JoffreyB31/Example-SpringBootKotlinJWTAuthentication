package com.jb.demo.repository

import com.jb.demo.models.Role
import com.jb.demo.models.enums.ERole
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface RoleRepository : JpaRepository<Role, Long> {
  fun findByName(name: ERole?): Optional<Role>
}