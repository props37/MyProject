package ru.zarina.zarina.di

object Qualifiers {
    object DataStore {
        const val PREFERENCES = "datastore-preferences"
        const val USER_CITY = "datastore-user-city"
    }

    object Dispatcher {
        const val IO = "dispatcher-io"
    }

    object Authorization {
        const val NONE = "authorization-none"
        const val TOKEN = "authorization-token"
        const val MINDBOX_SECRET = "authorization-mindbox-secret"
    }
}
