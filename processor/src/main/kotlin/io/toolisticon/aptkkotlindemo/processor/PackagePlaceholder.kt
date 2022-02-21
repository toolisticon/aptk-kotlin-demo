package io.toolisticon.aptkkotlindemo.processor

import io.toolisticon.aptk.annotationwrapper.api.AnnotationWrapper
import io.toolisticon.aptkkotlindemo.api.Service
import io.toolisticon.aptkkotlindemo.api.Services
import io.toolisticon.aptkkotlindemo.api.Spi
import io.toolisticon.aptkkotlindemo.api.SpiServiceLocator

@AnnotationWrapper(value = [Service::class, Services::class, SpiServiceLocator::class, Spi::class])
class PackagePlaceholder