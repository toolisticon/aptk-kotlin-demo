package io.toolisticon.aptkkotlindemo.api;

/**
 * Binder for supporting several spis in a single class.
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
@MustBeDocumented
annotation class  Services (
    val value : Array<Service>
)
