package com.burtaev.application

import java.util.GregorianCalendar

class DataSource : ContactsDataSource {
    private val contactModels = listOf(
        Contact(
            1,
            "Иванов Иван",
            "+79277894512",
            "+79277899685",
            "example@ex.ru",
            "example2@ex.ru",
            "Шиномонтаж",
            R.drawable.default_user_icon,
            GregorianCalendar(2020, 1, 29)
        )
    )

    override fun getAllContact() = contactModels

    override fun getContactById(id: Int) = contactModels.find { it.id == id }
}