package ch.cloudcoins;

import org.junit.rules.TestRule;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;

import java.util.ArrayList;
import java.util.List;

public class PersistenceTestRunner extends BlockJUnit4ClassRunner {

    public PersistenceTestRunner(Class<?> clazz) throws InitializationError {
        super(clazz);
    }

    @Override
    protected List<TestRule> getTestRules(Object target) {
        List<TestRule> result = new ArrayList<>(super.getTestRules(target));
        result.add(new PersistenceTestRule(target));
        return result;
    }
}
