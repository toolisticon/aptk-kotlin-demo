package io.toolisticon.aptkkotlindemo.api;

import kotlin.reflect.KClass


/**
 * Marks an implementation of one or more SPI.
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
@MustBeDocumented
annotation class Service(
    /**
     * The SPI interface implemented by the annotated class.
     */
    val value: KClass<*>,
    /**
     * This optional attribute is used to declare an identifier for this implementation.
     * If not set, the full qualified class name of the service implementation will be used as id.
     */
    val id: String = "",
    /**
     * This optional attribute is used to add a short description about the implementing class.
     */
    val description: String = "",
    /**
     * This optional attribute defines the order of the service implementations returned by the generated service allocator.
     * Lower value are defining a higher priority. Defaults to 0.
     */
    val priority: Int = 0
)
