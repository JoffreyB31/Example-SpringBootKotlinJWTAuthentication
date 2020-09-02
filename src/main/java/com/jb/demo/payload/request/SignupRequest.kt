package com.jb.demo.payload.request

import com.jb.demo.models.enums.ERole
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

data class SignupRequest(

    @NotBlank @Size(min = 3, max = 20)
    var username: String,

    var roles: Set<ERole> = emptySet(),

    @NotBlank @Size(min = 6, max = 40)
    var password: String

)