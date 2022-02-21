package io.toolisticon.aptkkotlindemo.processor.tests;

import io.toolisticon.aptkkotlindemo.api.Service;
import io.toolisticon.aptkkotlindemo.api.Services;
import io.toolisticon.aptkkotlindemo.processor.serviceprocessortest.AnotherTestSpi;
import io.toolisticon.aptkkotlindemo.processor.serviceprocessortest.TestSpi;

@Services({
        @Service(TestSpi.class),
        @Service(AnotherTestSpi.class)
})

public class TestcaseMultipleServices implements TestSpi, AnotherTestSpi {
    public String doSomething() {
        return "";
    }

    public void anotherThingToDo() {

    }

}