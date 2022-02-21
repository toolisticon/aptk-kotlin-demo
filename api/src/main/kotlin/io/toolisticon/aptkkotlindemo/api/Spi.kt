package io.toolisticon.aptkkotlindemo.api;


/**
 * Marks an interface that should be used as an SPI.
 * By default, this annotation will trigger the creation of the SPI service locator.
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
@MustBeDocumented
annotation class Spi (

    /**
     * Should the service locator be generated?
     * @return the configured value or true as a default value
     */
    val generateServiceLocator: Boolean = true

)
