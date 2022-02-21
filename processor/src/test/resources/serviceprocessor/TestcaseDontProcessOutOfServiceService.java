package io.toolisticon.aptkkotlindemo.processor.tests;

import io.toolisticon.aptkkotlindemo.api.OutOfService;
import io.toolisticon.aptkkotlindemo.api.Service;
import io.toolisticon.aptkkotlindemo.api.Services;
import io.toolisticon.aptkkotlindemo.processor.serviceprocessortest.AnotherTestSpi;
import io.toolisticon.aptkkotlindemo.processor.serviceprocessortest.TestSpi;

@OutOfService
@Services({
        @Service(TestSpi.class),
        @Service(AnotherTestSpi.class)
})
public class TestcaseDontProcessOutOfServiceService implements TestSpi, AnotherTestSpi {
    public String doSomething() {
        return "";
    }

    public void anotherThingToDo() {

    }

}