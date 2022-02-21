import io.toolisticon.aptkkotlindemo.api.Service;
import io.toolisticon.aptkkotlindemo.processor.serviceprocessortest.TestSpiInterface;

@Service(TestSpiInterface.class)
public class TestcaseValidUseWithPlainInterface implements TestSpiInterface {
    public String doSomething() {
        return "";
    }
}