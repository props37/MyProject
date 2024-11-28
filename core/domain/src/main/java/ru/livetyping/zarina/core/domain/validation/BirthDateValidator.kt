package ru.livetyping.zarina.core.domain.validation

import ru.livetyping.zarina.core.domain.model.user.User
import ru.livetyping.zarina.core.domain.model.user.exception.BirthDateException
import ru.livetyping.zarina.core.domain.model.user.exception.InvalidBirthDateException
import java.time.LocalDate

/**
 * @throws BirthDateException
 */
public class BirthDateValidator : Validator<LocalDate> {
    override fun validate(input: LocalDate) {
        val currentDate = LocalDate.now()
        when {
            input > currentDate -> throw InvalidBirthDateException()
            input < User.BIRTH_DATE_MIN_VALUE -> throw InvalidBirthDateException()
        }
    }
}
