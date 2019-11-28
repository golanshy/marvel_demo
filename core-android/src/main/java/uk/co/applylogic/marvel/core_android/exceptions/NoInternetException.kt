package uk.co.applylogic.marvel.core_android.exceptions

import android.content.Context
import uk.co.applylogic.marvel.core_android.R
import java.io.IOException

class NoInternetException(private val context: Context) : IOException() {
    override val message: String
        get() = context.getString(R.string.no_internet_available)
}