package io.toolisticon.aptkkotlindemo.api;

import kotlin.reflect.KClass

/**
 * Generates a service locator for a service interface.
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
@MustBeDocumented
annotation class SpiServiceLocator (

    /**
     * The service interface to create the service locator class for.
     */
    val value: KClass<*>

)
