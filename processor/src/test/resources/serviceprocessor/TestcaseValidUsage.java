package io.toolisticon.aptkkotlindemo.processor.tests;

import io.toolisticon.aptkkotlindemo.api.Service;
import io.toolisticon.aptkkotlindemo.processor.serviceprocessortest.TestSpi;

@Service(TestSpi.class)
public class TestcaseValidUsage implements TestSpi {
    public String doSomething() {
        return "";
    }
}