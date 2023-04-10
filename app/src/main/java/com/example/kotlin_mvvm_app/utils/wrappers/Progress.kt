package com.example.kotlin_mvvm_app.utils.wrappers

class Progress(
    val importance: Importance? = Importance.Main,
    val pagination: Pagination? = null,
) {
    enum class Pagination {
        Start,
        End,
    }

    enum class Importance {
        /**
         * For regular indicators
         */
        Main,

        /**
         * Indicates, that current data is temp (might be outdated), moreover actual data is in progress (syncing via network).
         * Consider twitter's feed as example, which allows user to consume cached feed while loading fresh one (progress indicator at the top)
         */
        Cache
    }

    fun hasPagination() = pagination != null
}