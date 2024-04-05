package ru.livetyping.zarina.data.user.local.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = UserEntity.TABLE_NAME)
data class UserEntity(
    @PrimaryKey
    @ColumnInfo(name = FIELD_ID)
    val id: String,

    @ColumnInfo(name = FIELD_EMAIL)
    val email: String,

    @ColumnInfo(name = FIELD_PHONE)
    val phone: String,

    @ColumnInfo(name = FIELD_FIRST_NAME)
    val firstName: String,

    @ColumnInfo(name = FIELD_LAST_NAME)
    val lastName: String?,

    @ColumnInfo(name = FIELD_BIRTH_DATE)
    val birthDate: String,
) {
    companion object {
        const val TABLE_NAME = "user"

        const val FIELD_ID = "user_id"
        const val FIELD_EMAIL = "user_email"
        const val FIELD_PHONE = "user_phone"
        const val FIELD_FIRST_NAME = "user_first_name"
        const val FIELD_LAST_NAME = "user_last_name"
        const val FIELD_BIRTH_DATE = "user_birth_date"
    }
}
