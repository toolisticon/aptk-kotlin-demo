package io.toolisticon.aptkkotlindemo.processor

import io.toolisticon.aptk.tools.*
import io.toolisticon.aptkkotlindemo.api.OutOfService
import io.toolisticon.aptkkotlindemo.api.Service
import io.toolisticon.aptkkotlindemo.api.Services
import java.io.IOException
import java.io.StringWriter
import java.util.*
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement
import javax.tools.StandardLocation
import kotlin.collections.HashMap

/**
 * Annotation Processor for {@link Service} and {@link Services}.
 * Creates the service descriptor file in "/META-INF/services/fqnOfSpi".
 * Additionally it creates a property file containing all additional metadata used by the ServiceLocator (like order, id and description)
 * to get rid of the api library as run time dependency.
 */
class ServiceProcessor : AbstractAnnotationProcessor() {

    private val SUPPORTED_ANNOTATIONS = createSupportedAnnotationSet(Services::class.java, Service::class.java)

    private val serviceImplHashMap = ServiceImplMap()

    /**
     * Map to store service provider implementations.
     */
    private class ServiceImplMap : HashMap<String, MutableSet<String>>() {

        /**
         * Convenience method to add a value.
         * Creates a Set if no entry for passedkey can be found.
         *
         * @param key   the key to look for
         * @param value the value to store in keys set
         * @return the keys set
         */
        fun put(key: String, value: String): Set<String> {
            var set : MutableSet<String> = mutableSetOf<String>()
            if (this.contains(key)) {
                set = this.get(key)!!
            } else {
                this[key] = set
            }

            set.add(value)
            return set
        }

    }

    override fun getSupportedAnnotationTypes(): Set<String> = SUPPORTED_ANNOTATIONS

    override fun processAnnotations(annotations: Set<TypeElement>, roundEnv: RoundEnvironment): Boolean {

        if (!roundEnv.processingOver()) {
            processAnnotationsInternally(roundEnv)
        } else {
            writeConfigurationFiles()
        }

        return false

    }

    private fun processAnnotationsInternally(roundEnv: RoundEnvironment) {

        // process Services annotation
        for (element: Element in roundEnv.getElementsAnnotatedWith(Services::class.java)) {

            // Check for OutOfService annotation
            if (checkSkipProcessingBecauseOfOutOfServiceAnnotation(element)) {
                continue
            }

            // read annotation
            val servicesAnnotationWrapper = ServicesWrapper.wrap(element)

            for (serviceWrapper: ServiceWrapper in servicesAnnotationWrapper.value()) {
                processAnnotation(serviceWrapper, element)
            }

        }

        // process Service annotation
        for (element: Element in roundEnv.getElementsAnnotatedWith(Service::class.java)) {

            // Check for OutOfService annotation
            if (checkSkipProcessingBecauseOfOutOfServiceAnnotation(element)) {
                continue
            }

            // read annotation
            val serviceAnnotationWrapper = ServiceWrapper.wrap(element)

            processAnnotation(serviceAnnotationWrapper, element)

        }


    }

    private fun checkSkipProcessingBecauseOfOutOfServiceAnnotation(element: Element): Boolean {
        // Check for OutOfService annotation
        if (element.getAnnotation(OutOfService::class.java) != null) {
            // skip processing of element
            MessagerUtils.info(
                element,
                ServiceProcessorMessages.INFO_SKIP_ELEMENT_ANNOTATED_AS_OUT_OF_SERVICE,
                ElementUtils.CastElement.castClass(element).qualifiedName
            )
            return true
        }
        return false
    }

    private fun processAnnotation(serviceAnnotationWrapper: ServiceWrapper?, annotatedElement: Element) {

        // check if it is placed on Class
        if (!ElementUtils.CheckKindOfElement.isClass(annotatedElement)) {
            MessagerUtils.error(annotatedElement, ServiceProcessorMessages.ERROR_SPI_ANNOTATION_MUST_BE_PLACED_ON_CLASS)
            return
        }

        // Now create the service locator

        val typeElement = ElementUtils.CastElement.castClass(annotatedElement)

        if (serviceAnnotationWrapper != null) {

            val spiInterfaces = mutableSetOf<String>()
            val spiAttributeTypes = serviceAnnotationWrapper.valueAsFqn()
            if (spiAttributeTypes != null) {
                spiInterfaces.add(spiAttributeTypes)
            }

            for (fqTypeName: String in spiInterfaces) {

                val typeMirror = TypeUtils.TypeRetrieval.getTypeMirror(fqTypeName)
                val typeMirrorTypeElement = ProcessingEnvironmentUtils.getTypes().asElement(typeMirror) as TypeElement

                //check if type is interface
                if (!ElementUtils.CheckKindOfElement.isInterface(typeMirrorTypeElement)) {
                    MessagerUtils.error(
                        annotatedElement,
                        ServiceProcessorMessages.ERROR_VALUE_ATTRIBUTE_MUST_ONLY_CONTAIN_INTERFACES,
                        typeMirrorTypeElement.qualifiedName.toString()
                    )
                    break
                }

                // check if annotatedElement is assignable to spi interface == implements the spi interface
                if (!TypeUtils.TypeComparison.isAssignableTo(typeElement, typeMirror)) {
                    MessagerUtils.error(
                        annotatedElement,
                        ServiceProcessorMessages.ERROR_ANNOTATED_CLASS_MUST_IMPLEMENT_CONFIGURED_INTERFACES,
                        typeMirrorTypeElement.qualifiedName.toString()
                    )
                    break
                }

                // Add configuration property file
                writePropertiesFile(typeElement, serviceAnnotationWrapper)

                // put service provider implementations in map by using the SPI fqn as key
                serviceImplHashMap.put(fqTypeName, typeElement.qualifiedName.toString())


            }

        }
    }

    /**
     * This method generates the property files of configured service annotation values.
     * This allows us not to get rid of spiap-api dependency at runtime.
     *
     * @param annotatedTypeElement the annotated element
     * @param serviceAnnotationWrapper     The Service annotations AnotationMirror
     */
    private fun writePropertiesFile(annotatedTypeElement: TypeElement, serviceAnnotationWrapper: ServiceWrapper) {


        val spiClassName = serviceAnnotationWrapper.valueAsFqn()

        val serviceClassName = annotatedTypeElement.qualifiedName.toString()
        val fileName = "META-INF/spiap/$spiClassName/$serviceClassName.properties"

        try {

            // write service provider file by using a template
            val propertiesFileObjectWriter = FilerUtils.createResource(StandardLocation.CLASS_OUTPUT, "", fileName)

            // Get properties
            val properties = Properties()

            // default to fqn of service if id is empty
            properties.setProperty(
                Constants.propertyKeyId,
                serviceAnnotationWrapper.id().ifEmpty { serviceClassName }
            )
            properties.setProperty(Constants.propertyKeyDescription, serviceAnnotationWrapper.description())
            properties.setProperty(Constants.propertyKeyPriority, "" + serviceAnnotationWrapper.priority())
            properties.setProperty(
                Constants.propertyKeyOutOfService,
                "" + (AnnotationUtils.getAnnotationMirror(annotatedTypeElement, OutOfService::class.java) != null)
            )

            val writer = StringWriter()
            properties.store(writer, "Property files used by the spi service locator")
            writer.flush()
            writer.close()

            propertiesFileObjectWriter.write(writer.toString())
            propertiesFileObjectWriter.close()


        } catch (e: IOException) {
            MessagerUtils.error(
                annotatedTypeElement,
                "Wasn't able to write service provider properties file for service ${0} for spi ${1}",
                serviceClassName,
                spiClassName
            )
            return
        }

    }


    private fun writeConfigurationFiles() {

        // Iterate through services that were detected by the annotation processor
        for (entry: Map.Entry<String, Set<String>> in serviceImplHashMap.entries) {

            // look for existing resource file and load already configured values
            val serviceProviderFile = "META-INF/services/" + entry.key

            val existingServiceImplementations = readServiceFile(serviceProviderFile)

            // check if we have found new services
            if (existingServiceImplementations.containsAll(entry.value)) {
                MessagerUtils.info(null, "All services implementations were already registered for \${0}", entry.key)
                return
            }

            val allServiceImplementations = HashSet(entry.value) as MutableSet<String>
            allServiceImplementations.addAll(existingServiceImplementations)

            try {

                // write service provider file by using a template
                val writer = FilerUtils.createResource(StandardLocation.CLASS_OUTPUT, "", serviceProviderFile)

                val model = HashMap<String, Any>() as MutableMap<String, Any>
                model["fqns"] = allServiceImplementations

                writer.writeTemplateString("!{for fqn: fqns}\${fqn}\n!{/for}", model)
                writer.close()


                MessagerUtils.info(
                    null,
                    "Written service provider registration file for \${0} containing \${1}",
                    serviceProviderFile,
                    allServiceImplementations
                )

            } catch (e: IOException) {
                MessagerUtils.error(null, "Wasn't able to write service provider registration file for \${0}", entry.key)
                return
            }


        }

    }

    /**
     * Read service file.
     * Get all already defined service providers.
     *
     * @param serviceProviderFile the service provider file
     * @return a set containing all service providers
     */
    private fun readServiceFile(serviceProviderFile: String): Set<String> {

        MessagerUtils.info(null, "Reading existing service file : \${0}", serviceProviderFile)

        val resultSet = HashSet<String>() as MutableSet<String>

        try {

            resultSet.addAll(
                FilerUtils.getResource(
                    StandardLocation.CLASS_OUTPUT, "",
                    serviceProviderFile
                ).readAsLines(true)
            )


        } catch (e: IOException) {
            MessagerUtils.info(null, "Wasn't able to open existing service file for \${0}", serviceProviderFile)
        }

        return resultSet
    }

}
