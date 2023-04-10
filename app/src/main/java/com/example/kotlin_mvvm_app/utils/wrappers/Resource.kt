package com.example.kotlin_mvvm_app.utils.wrappers

data class Resource<T>(
    val data: T? = null,
    val error: TextResource? = null,
    val progress: Progress? = null,

    // State when both Data and Error should be handled, i.e. noncritical error.
    val isWarning: Boolean = false,
) {

    companion object {
        fun <T> progress() = Resource<T>(progress = Progress())
    }

    fun peek(
        onProgress: ((progress: Progress) -> Unit)? = null,
        onError: ((error: TextResource) -> Unit)? = null,
        // Don't move(reorder) onData block from the end (to support trailing lambda)
        onData: ((data: T) -> Unit)? = null,
    ): Resource<T> {
        if (isData() && onData != null) {
            onData.invoke(data!!)
        }

        if (error != null && onError != null) {
            onError.invoke(error)
        }

        if (progress != null && onProgress != null) {
            onProgress.invoke(progress)
        }

        return this
    }


    //==============================================================================================
    // *** newInstance ***
    //==============================================================================================
    fun newData(lambda: (value: T) -> T = { data!! }) = this.copy(
        error = null,
        data = lambda.invoke(data!!),
        progress = null,
        isWarning = false,
    )

    fun newError(lambda: (oldData: T?) -> TextResource?) = this.copy(
        error = lambda.invoke(data),
        data = data,
        progress = null,
        isWarning = false,
    )

    fun newProgress(lambda: (oldData: T?) -> Progress = { Progress() }) = this.copy(
        error = null,
        data = data,
        progress = lambda.invoke(data),
        isWarning = false,
    )

    fun newWarning(
        newData: ((value: T) -> T?) = { data },
        newError: ((oldData: T?) -> TextResource?) = { error }
    ) = this.copy(
        error = newError.invoke(data!!),
        data = newData.invoke(data),
        progress = null,
        isWarning = true,
    )

    /**
     * Just emmit last [data]
     */
    fun hideProgress() = newData()


    //==============================================================================================
    // *** Utils ***
    //==============================================================================================
    override fun toString(): String {
        if (isWarning) {
            return "[Warning] ${data.toString()}\n${error.toString()}"
        }
        if (isData()) {
            return data.toString()
        }
        if (hasError()) {
            return error.toString()
        }

        if (hasProgress()) {
            return "Progress"
        }

        return "null"
    }

    fun hasError() = error != null

    fun hasProgress() = progress != null

    fun hasPaginationProgress() = hasProgress() && progress!!.hasPagination()

    /**
     * Data often persisted along with other states (data != null),
     * so we do inverted check, i.e. that state != all other states
     *
     * Note: [isWarning] is exclusion, it means nonCritical error
     * therefore has no restriction on [isData] check
     */
    fun isData(): Boolean {
        if (hasProgress()) return false
        if (!isWarning && hasError()) return false

        return data != null
    }
}