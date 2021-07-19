package com.burtaev.application.repository

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.ContactsContract
import com.burtaev.application.R
import com.burtaev.application.model.Contact
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class DataSource(private val context: Context) : ContactRepository {

    private val contentResolver: ContentResolver = context.contentResolver

    override fun getContactsList(query: String?): List<Contact> {
        val contactModels = mutableListOf<Contact>()
        var selectionParamContacts: String? = null
        if (query != null) {
            selectionParamContacts =
                ContactsContract.Contacts.DISPLAY_NAME_PRIMARY + " LIKE \'%" + query + "%\'"
        }
        val cursor = contentResolver.query(
            ContactsContract.Contacts.CONTENT_URI,
            null,
            selectionParamContacts,
            null,
            null)
        try {
            cursor?.let { it ->
                while (it.moveToNext()) {
                    val idContact =
                        it.getString(it.getColumnIndex(ContactsContract.Contacts._ID))
                    getContactDetails(idContact)?.let {
                        contactModels.add(it)
                    }
                }
            }
        } catch (e: Exception) {
        } finally {
            cursor?.close()
            return contactModels;
        }
    }

    override fun getContactDetails(id: String): Contact? {
        val cursor = contentResolver.query(
            ContactsContract.Contacts.CONTENT_URI,
            null,
            ContactsContract.Contacts._ID + " = " + id,
            null,
            null
        )
        var contact: Contact? = null
        try {
            cursor?.let {
                while (cursor.moveToNext()) {
                    val name =
                        (it.getString(it.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)))
                            ?: context.getString(R.string.unknown_name)
                    val phoneURI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI
                    val phoneMime = ContactsContract.CommonDataKinds.Phone.CONTACT_ID
                    val emailUri = ContactsContract.CommonDataKinds.Email.CONTENT_URI
                    val emailMime = ContactsContract.CommonDataKinds.Email.CONTACT_ID
                    val noteMime = ContactsContract.CommonDataKinds.Note.CONTENT_ITEM_TYPE
                    var phoneNum = context.getString(R.string.unknown_phone)
                    var phoneNum2 = ""
                    var email = ""
                    var email2 = ""
                    val listPhone = getCursorData(id, phoneURI, phoneMime)
                    if (listPhone.count() > 0) {
                        phoneNum = listPhone[0]
                        if (listPhone.count() > 1) {
                            phoneNum2 = listPhone[1]
                        }
                    }
                    val listEmail = getCursorData(id, emailUri, emailMime)
                    if (listEmail.count() > 0) {
                        email = listEmail[0]
                        if (listEmail.count() > 1) {
                            email2 = listEmail[1]
                        }
                    }
                    val strPhotoUri =
                        (it.getString(it.getColumnIndex(ContactsContract.Contacts.PHOTO_URI)))
                            ?: ""
                    val notes = getContactDataById(id,
                        noteMime)[0]
                    val dateBirthday = getContactBirthdayById(id)
                    contact = Contact(
                        id,
                        name,
                        phoneNum,
                        phoneNum2,
                        email,
                        email2,
                        notes,
                        if (strPhotoUri == "") null else Uri.parse(strPhotoUri),
                        dateBirthday)
                }
            }
        } catch (e: Exception) {
        } finally {
            cursor?.close()
            return contact
        }
    }

    private fun getCursorData(contactId: String, uri: Uri, mime: String): List<String> {
        val listValues = mutableListOf<String>()
        val cursor = contentResolver.query(uri, null,
            "$mime = $contactId", null, null)
        try {
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    listValues.add(cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)))
                }
            }
        } catch (e: Exception) {
        } finally {
            cursor?.close()
            return listValues;
        }
    }

    @SuppressLint("Recycle")
    private fun getCursor(contactID: String, mime: String): Cursor? {
        return try {
            contentResolver.query(
                ContactsContract.Data.CONTENT_URI,
                arrayOf(ContactsContract.Data.DATA1),
                "${ContactsContract.Data.CONTACT_ID} = ? AND ${ContactsContract.Data.MIMETYPE} = ?",
                arrayOf(contactID, mime),
                null
            )
        } catch (e: Exception) {
            null
        }
    }

    private fun getContactDataById(
        contactId: String,
        mime: String
    ): List<String> {
        val values = mutableListOf<String>()
        val cursor = getCursor(contactId, mime)
        try {
            if (cursor != null) {
                val dataColumnIndex = cursor.getColumnIndex(ContactsContract.Data.DATA1)
                while (cursor.moveToNext()) {
                    values.add(cursor.getString(dataColumnIndex) ?: "")
                }
            }
        } catch (e: Exception) {
        } finally {
            cursor?.close()
            while (values.size < 2)
                values.add("")
            return values
        }
    }

    private fun getContactBirthdayById(contactID: String): Calendar? {
        val birthdayString = getContactDataById(
            contactID,
            ContactsContract.CommonDataKinds.Event.CONTENT_ITEM_TYPE
        )
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date = try {
            sdf.parse(birthdayString[0])
        } catch (e: Exception) {
            null
        } ?: return null
        return Calendar.getInstance().apply { time = date }
    }
}