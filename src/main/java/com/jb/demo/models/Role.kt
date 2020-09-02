package com.jb.demo.models

import com.jb.demo.models.enums.ERole
import javax.persistence.*

@Entity
@Table(name = "roles")
data class Role(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int = 1,

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    var name: ERole = ERole.ROLE_USER

)