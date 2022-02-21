package io.toolisticon.aptkkotlindemo.processor

import io.toolisticon.aptk.tools.corematcher.ValidationMessage
import io.toolisticon.aptkkotlindemo.api.OutOfService

/**
 * SpiProcessorMessages used by annotation processors of Advice annotations.
 */
enum class ServiceProcessorMessages(code: String, message: String) : ValidationMessage {


    ERROR_SPI_ANNOTATION_MUST_BE_PLACED_ON_CLASS(
        "SERVICE_ERROR_001",
        "Service annotation must only be used on Classes"
    ),
    ERROR_VALUE_ATTRIBUTE_MUST_ONLY_CONTAIN_INTERFACES(
        "SERVICE_ERROR_002",
        "Service annotation only accepts interfaces - \${0} is no interface"
    ),
    ERROR_ANNOTATED_CLASS_MUST_IMPLEMENT_CONFIGURED_INTERFACES(
        "SERVICE_ERROR_003",
        "Service doesn't implement the \${0} interface"
    ),
    ERROR_COULD_NOT_CREATE_SERVICE_LOCATOR_FILE(
        "SERVICE_ERROR_004",
        "Cannot open spi service location file for writing : \${0}"
    ),
    ERROR_COULD_NOT_APPEND_TO_SERVICE_LOCATOR_FILE(
        "SERVICE_ERROR_005",
        "Cannot append to spi service location file : \${0}"
    ),
    INFO_SKIP_ELEMENT_ANNOTATED_AS_OUT_OF_SERVICE(
        "SERVICE_INFO_001",
        "Skipped processing for service(s) class \${0} annotated with ${OutOfService::class.simpleName} annotation"
    );


    private var _code: String = code
    private var _message: String = message

    override fun getCode(): String = this._code

    override fun getMessage(): String = this._message

}
