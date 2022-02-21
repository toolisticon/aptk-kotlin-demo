package io.toolisticon.aptkkotlindemo.processor

import io.toolisticon.aptk.tools.AbstractAnnotationProcessor
import io.toolisticon.aptk.tools.ElementUtils
import io.toolisticon.aptk.tools.FilerUtils
import io.toolisticon.aptk.tools.MessagerUtils
import io.toolisticon.aptk.tools.generators.SimpleJavaWriter
import io.toolisticon.aptkkotlindemo.api.Spi
import io.toolisticon.aptkkotlindemo.api.SpiServiceLocator
import java.io.IOException
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind
import javax.lang.model.element.PackageElement
import javax.lang.model.element.TypeElement

/**
 * Annotation Processor for {@link Spi} and {@link SpiServiceLocator}.
 */
class SpiProcessor : AbstractAnnotationProcessor() {

    private val SUPPORTED_ANNOTATIONS: Set<String> =
        createSupportedAnnotationSet(Spi::class.java, SpiServiceLocator::class.java)


    override fun getSupportedAnnotationTypes(): Set<String> = SUPPORTED_ANNOTATIONS

    override fun processAnnotations(annotations: Set<TypeElement>, roundEnv: RoundEnvironment): Boolean {

        handleSpiAnnotation(roundEnv)
        handleSpiServiceLocatorAnnotation(roundEnv)

        return false
    }

    private fun handleSpiAnnotation(roundEnv: RoundEnvironment) {
        // handle Spi annotation
        for (element: Element in roundEnv.getElementsAnnotatedWith(Spi::class.java)) {

            MessagerUtils.info(element, "Process : ${element.simpleName} annotated with Spi annotation")


            // check if it is place on interface
            if (!ElementUtils.CheckKindOfElement.isInterface(element)) {
                MessagerUtils.error(element, SpiProcessorMessages.ERROR_SPI_ANNOTATION_MUST_BE_PLACED_ON_INTERFACE)
                continue
            }


            // Generate service locator depending on settings configured in Spi annotation
            val spiAnnotation = SpiWrapper.wrap(element)
            if (spiAnnotation.generateServiceLocator()) {

                // Now create the service locator
                val typeElement = ElementUtils.CastElement.castInterface(element)
                val packageElement: PackageElement =
                    ElementUtils.AccessEnclosingElements.getFirstEnclosingElementOfKind(
                        typeElement,
                        ElementKind.PACKAGE
                    )

                // generate service locator
                generateServiceLocator(element, packageElement, typeElement)
            }

        }

    }

    private fun handleSpiServiceLocatorAnnotation(roundEnv: RoundEnvironment) {

        // handle Spi annotation
        for (element: Element in roundEnv.getElementsAnnotatedWith(SpiServiceLocator::class.java)) {

            MessagerUtils.info(
                element,
                "Process : ${element.simpleName} annotated with SpiServiceLocator annotation"
            )

            // get type from annotation
            val annotationWrapper = SpiServiceLocatorWrapper.wrap(element)
            val typeMirror = annotationWrapper.valueAsTypeMirror()
            if (typeMirror == null) {
                MessagerUtils.error(element, "Couldn't get type from annotations attributes")
                continue
            }

            val serviceLocatorInterfaceElement: TypeElement =
                annotationWrapper.valueAsTypeMirrorWrapper().typeElement

            // check if it is place on interface
            if (!ElementUtils.CheckKindOfElement.isInterface(serviceLocatorInterfaceElement)) {
                MessagerUtils.error(
                    element,
                    SpiProcessorMessages.ERROR_SERVICE_LOCATOR_PASSED_SPI_CLASS_MUST_BE_AN_INTERFACE
                )
                continue
            }


            // Now create the service locator
            val typeElement: TypeElement = ElementUtils.CastElement.castInterface(serviceLocatorInterfaceElement)
            val packageElement: PackageElement = ElementUtils.AccessEnclosingElements.getFirstEnclosingElementOfKind<PackageElement>(element, ElementKind.PACKAGE)


            // generate service locator
            generateServiceLocator(element, packageElement, typeElement)

        }

    }


    private fun generateServiceLocator(
        annotatedElement: Element,
        packageElement: PackageElement,
        typeElement: TypeElement
    ) {

        // create Model
        val model = HashMap<String, Any>()

        model["package"] = packageElement.qualifiedName.toString()
        model["canonicalName"] = typeElement.qualifiedName.toString()
        model["simpleName"] = typeElement.simpleName.toString()

        val constants = HashMap<String, Any>()
        constants["id"] = Constants.propertyKeyId
        constants["description"] = Constants.propertyKeyDescription
        constants["priority"] = Constants.propertyKeyPriority
        constants["outOfService"] = Constants.propertyKeyOutOfService
        model["constants"] = constants

        val filePath: String = "${packageElement.qualifiedName.toString()}.${typeElement.simpleName.toString()}ServiceLocator"

        try {
            val javaWriter: SimpleJavaWriter = FilerUtils.createSourceFile(filePath, annotatedElement)
            javaWriter.writeTemplate("/ServiceLocator.tpl", model)
            javaWriter.close()
        } catch (e: IOException) {
            MessagerUtils.error(
                annotatedElement,
                SpiProcessorMessages.ERROR_COULD_NOT_CREATE_SERVICE_LOCATOR,
                filePath
            )
        }

    }

}
