package io.toolisticon.aptkkotlindemo.api

/**
 * Annotation to deactivate a service implementation in the generated service locator classes and during annotation processing of {@link Services} and {@link Service} annotations.
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.TYPE)
@MustBeDocumented
annotation class OutOfService()
