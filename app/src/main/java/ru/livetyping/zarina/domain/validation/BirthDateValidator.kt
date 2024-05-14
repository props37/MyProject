package ru.livetyping.zarina.domain.validation

import ru.livetyping.zarina.domain.common.exception.InvalidDateException
import java.time.LocalDate

class BirthDateValidator : Validator<LocalDate> {
    override fun validate(input: LocalDate) {
        val currentDate = LocalDate.now()
        if (input > currentDate) {
            throw InvalidDateException("Birth date can not be greater than the current date")
        }
    }
}
