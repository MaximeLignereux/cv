package fr.project.mlignereux.base.util.exception

interface ExceptionLogger {

    fun logException(throwable: Throwable)
}