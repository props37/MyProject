package ru.livetyping.zarina.data.user.local.database.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.livetyping.zarina.data.common.local.GenderEntity
import ru.livetyping.zarina.domain.common.Email
import ru.livetyping.zarina.domain.common.PhoneNumber
import ru.livetyping.zarina.domain.user.User
import java.time.LocalDate

@Entity(tableName = UserEntity.TABLE_NAME)
data class UserEntity(
    @PrimaryKey
    @ColumnInfo(name = FIELD_ID)
    val id: String,

    @ColumnInfo(name = FIELD_EMAIL)
    val email: String,

    @ColumnInfo(name = FIELD_PHONE)
    val phone: String?,

    @ColumnInfo(name = FIELD_FIRST_NAME)
    val firstName: String?,

    @ColumnInfo(name = FIELD_LAST_NAME)
    val lastName: String?,

    @ColumnInfo(name = FIELD_BIRTH_DATE)
    val birthDate: String?,

    @ColumnInfo(name = FIELD_GENDER)
    val gender: GenderEntity?,

    @Embedded
    val notificationSettings: NotificationSettings,
) {
    fun toUser(): User = User(
        id = User.Id(id),
        email = Email.create(email),
        phone = phone?.let { PhoneNumber.create(it) },
        firstName = firstName,
        lastName = lastName,
        birthDate = birthDate?.let { LocalDate.parse(it) },
        gender = gender?.toGender(),
        notificationSettings = notificationSettings.toNotificationSettings(),
    )

    data class NotificationSettings(
        @ColumnInfo(name = FIELD_RECEIVE_SMS)
        val receiveSms: Boolean,

        @ColumnInfo(name = FIELD_RECEIVE_EMAILS)
        val receiveEmails: Boolean,
    ) {
        fun toNotificationSettings(): User.NotificationSettings = User.NotificationSettings(
            receiveSms = receiveSms,
            receiveEmails = receiveEmails,
        )

        companion object {
            fun from(settings: User.NotificationSettings): NotificationSettings {
                return NotificationSettings(
                    receiveSms = settings.receiveSms,
                    receiveEmails = settings.receiveEmails,
                )
            }
        }
    }

    companion object {
        const val TABLE_NAME = "user"

        const val FIELD_ID = "user_id"
        const val FIELD_EMAIL = "user_email"
        const val FIELD_PHONE = "user_phone"
        const val FIELD_FIRST_NAME = "user_first_name"
        const val FIELD_LAST_NAME = "user_last_name"
        const val FIELD_BIRTH_DATE = "user_birth_date"
        const val FIELD_GENDER = "user_gender"

        const val FIELD_RECEIVE_SMS = "user_receive_sms"
        const val FIELD_RECEIVE_EMAILS = "user_receive_emails"

        fun from(user: User): UserEntity = UserEntity(
            id = user.id.value,
            email = user.email.value,
            phone = user.phone?.value,
            firstName = user.firstName,
            lastName = user.lastName,
            birthDate = user.birthDate?.toString(),
            gender = user.gender?.let { GenderEntity.from(it) },
            notificationSettings = NotificationSettings.from(user.notificationSettings),
        )
    }
}
