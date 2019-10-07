package fr.project.mlignereux.base.util.exception

import com.crashlytics.android.Crashlytics
import javax.inject.Inject

class CrashlyticsExceptionLogger @Inject constructor() : ExceptionLogger {

    override fun logException(throwable: Throwable) {
        Crashlytics.logException(throwable)
    }
}