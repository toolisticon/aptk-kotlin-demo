package io.toolisticon.aptkkotlindemo.processor


import io.toolisticon.aptk.tools.corematcher.ValidationMessage

/**
 * SpiProcessorMessages used by annotation processors of Advice annotations.
 */
 enum class SpiProcessorMessages(code: String, message: String) : ValidationMessage {


    ERROR_SPI_ANNOTATION_MUST_BE_PLACED_ON_INTERFACE("SPI_ERROR_001", "Spi annotation must only be used on interfaces"),
    ERROR_COULD_NOT_CREATE_SERVICE_LOCATOR("SPI_ERROR_002", "Couldn't create Spi Service Locator : ${0}"),
    ERROR_SERVICE_LOCATOR_PASSED_SPI_CLASS_MUST_BE_AN_INTERFACE("SPI_ERROR_003", "Passed spi class in service locator annotation isn't an interface"),
    ;

    private var _code: String = code
    private var _message: String = message

    override fun getCode(): String = this._code

    override fun getMessage(): String = this._message

}
