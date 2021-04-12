package com.burtaev.application

import java.util.Calendar

data class Contact(
    val id: Int,
    val name: String,
    val phoneNum: String,
    val phoneNum2: String,
    val email: String,
    val email2: String,
    val description: String,
    val img: Int,
    val dateBirthday: Calendar?
)

