package io.toolisticon.aptkkotlindemo.processor.tests;

import io.toolisticon.aptkkotlindemo.api.Service;
import io.toolisticon.aptkkotlindemo.processor.serviceprocessortest.TestSpi;

@Service(TestcaseValueAttributeMustOnlyContainInterfaces.class)
public class TestcaseValueAttributeMustOnlyContainInterfaces implements TestSpi {
    public String doSomething() {
        return "";
    }
}