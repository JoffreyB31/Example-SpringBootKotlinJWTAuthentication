package com.jb.demo.models

import java.util.*
import javax.persistence.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

@Entity
@Table(name = "users")
data class User(

    @Column(unique = true)
    var username: @NotBlank @Size(max = 20) String = "",

    var password: @NotBlank @Size(max = 120) String = "",

    @ManyToMany(fetch = FetchType.LAZY, cascade = [CascadeType.PERSIST, CascadeType.MERGE])
    @JoinTable(name = "user_roles", joinColumns = [JoinColumn(name = "user_id")], inverseJoinColumns = [JoinColumn(name = "role_id")])
    var roles: Set<Role> = emptySet()

) {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  var id: Long = 1

}