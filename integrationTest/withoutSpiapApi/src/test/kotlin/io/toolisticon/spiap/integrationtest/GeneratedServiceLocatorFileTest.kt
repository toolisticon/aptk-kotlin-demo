package io.toolisticon.spiap.integrationtest

import io.toolisticon.example.spiapexample.api.DecimalCalculationOperation
import io.toolisticon.example.spiapexample.api.DecimalCalculationOperationServiceLocator
import io.toolisticon.example.spiapexample.service.AdditionDecimalOperationImpl
import io.toolisticon.example.spiapexample.service.DivisionDecimalOperationImpl
import io.toolisticon.example.spiapexample.service.MultiplicationDecimalOperationImpl
import io.toolisticon.example.spiapexample.service.SubtractionDecimalOperationImpl
import io.toolisticon.spiap.interationtest.SquareDecimalOperationServiceImpl
import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers
import org.junit.Test

/**
 * Test to check if service locator file contains all implementations.
 */
 class GeneratedServiceLocatorFileTest {


    @Test
    fun getServiceImplementations_forDecimalCalculationOperation() {

        val operationServiceKeys = DecimalCalculationOperationServiceLocator.getServiceImplementations()

        MatcherAssert.assertThat(operationServiceKeys, Matchers.hasSize(5))

        val ids = mutableSetOf<String>()
        for ( sk: DecimalCalculationOperationServiceLocator.ServiceImplementation in operationServiceKeys) {
            ids.add(sk.id)
        }

        // check if id referenced and fqn referenced service implementations have been found
        MatcherAssert.assertThat(ids, Matchers.containsInAnyOrder("SQUARE", "MULTIPLICATION", "ADDITION", "SUBTRACTION", DivisionDecimalOperationImpl::class.qualifiedName.toString()))


    }


    @Test
    fun locateAll_locateAllServices() {

        val services: List<DecimalCalculationOperation> = DecimalCalculationOperationServiceLocator.locateAll()

        val serviceIds: MutableSet<String> = mutableSetOf()

        for ( service: DecimalCalculationOperation in services) {
            service::class.qualifiedName?.let { serviceIds.add(it) }
        }

        // check if id referenced and fqn referenced service implementations have been found
        MatcherAssert.assertThat(serviceIds, Matchers.containsInAnyOrder(SquareDecimalOperationServiceImpl::class.qualifiedName, MultiplicationDecimalOperationImpl::class.qualifiedName, AdditionDecimalOperationImpl::class.qualifiedName, SubtractionDecimalOperationImpl::class.qualifiedName, DivisionDecimalOperationImpl::class.qualifiedName))

    }

    @Test
    fun locate_getOneService() {

        MatcherAssert.assertThat(DecimalCalculationOperationServiceLocator.locate(), Matchers.notNullValue())

    }

    @Test
    fun locateById_locateServiceById() {

        val result: DecimalCalculationOperation = DecimalCalculationOperationServiceLocator.locateById("MULTIPLICATION")

        MatcherAssert.assertThat(result, Matchers.notNullValue())
        MatcherAssert.assertThat(result, Matchers.instanceOf(MultiplicationDecimalOperationImpl::class.java))

    }

    @Test(expected = DecimalCalculationOperationServiceLocator.ImplementationNotFoundException::class)
        fun locateById_locateServiceById_withNullValue() {

        DecimalCalculationOperationServiceLocator.locateById(null)

    }

    @Test(expected = DecimalCalculationOperationServiceLocator.ImplementationNotFoundException::class)
    fun locateById_locateServiceById_withNonExistingId() {

        DecimalCalculationOperationServiceLocator.locateById("XXX")

    }


    @Test
    fun locateAll_checkOrder() {

        val result: List<DecimalCalculationOperation> = DecimalCalculationOperationServiceLocator.locateAll()

        MatcherAssert.assertThat(result, Matchers.hasSize(5))

        MatcherAssert.assertThat(result[0], Matchers.instanceOf(SquareDecimalOperationServiceImpl::class.java))
        MatcherAssert.assertThat(result[1], Matchers.instanceOf(AdditionDecimalOperationImpl::class.java))
        MatcherAssert.assertThat(result[2], Matchers.instanceOf(SubtractionDecimalOperationImpl::class.java))
        MatcherAssert.assertThat(result[3], Matchers.instanceOf(DivisionDecimalOperationImpl::class.java))
        MatcherAssert.assertThat(result[4], Matchers.instanceOf(MultiplicationDecimalOperationImpl::class.java))


    }


    @Test
    fun getServiceKeys_checkOrder() {

        val result: List<DecimalCalculationOperationServiceLocator.ServiceImplementation> = DecimalCalculationOperationServiceLocator.getServiceImplementations()

        MatcherAssert.assertThat(result, Matchers.hasSize(5))

        MatcherAssert.assertThat(result[0].id, Matchers.`is`("SQUARE"))
        MatcherAssert.assertThat(result[1].id, Matchers.`is`("ADDITION"))
        MatcherAssert.assertThat(result[2].id, Matchers.`is`("SUBTRACTION"))
        MatcherAssert.assertThat(result[3].id, Matchers.`is`("io.toolisticon.example.spiapexample.service.DivisionDecimalOperationImpl"))
        MatcherAssert.assertThat(result[4].id, Matchers.`is`("MULTIPLICATION"))


    }


}
