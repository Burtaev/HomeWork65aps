package com.burtaev.application

import android.net.Uri
import java.util.Calendar

data class Contact(
    val id: String,
    val name: String,
    val phoneNum: String,
    val phoneNum2: String,
    val email: String,
    val email2: String,
    val description: String,
    val img: Uri?,
    val dateBirthday: Calendar?
)

