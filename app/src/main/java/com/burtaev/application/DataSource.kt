package com.burtaev.application

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
            R.drawable.default_user_icon
        )
    )

    override fun getAllContact() = contactModels
    override fun getContactById(id: Long) = contactModels.find { it.id == id }
}