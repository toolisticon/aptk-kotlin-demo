package io.toolisticon.aptkkotlindemo.processor.tests;

import io.toolisticon.aptkkotlindemo.api.Spi;

@Spi(generateServiceLocator = false)
public interface TestcaseGenerateNoServiceLocator {
    String doSomething();
}