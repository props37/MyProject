package ru.zarina.zarina.di.old

@Deprecated(message = "Use Qualifiers instead.")
object Qualifiers {
    object DataStore {
        const val PREFERENCES = "datastore-preferences"
        const val USER_CITY = "datastore-user-city"
    }

    object Dispatcher {
        const val IO = "dispatcher-io"
    }

    object Api {
        const val ZARINA = "api-zarina"
        const val ZARINA_RESTRICTED = "api-zarina-restricted"
        const val MINDBOX_RESTRICTED = "api-mindbox-restricted"
        const val ANYQUERY_AUTOCOMPLETE = "api-anyquery-autocomplete"
        const val ANYQUERY_SEARCH = "api-anyquery-search"
    }
}
