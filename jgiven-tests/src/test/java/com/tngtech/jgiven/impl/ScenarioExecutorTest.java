package com.tngtech.jgiven.impl;

import static org.assertj.core.api.Assertions.assertThat;

import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import com.tngtech.jgiven.JGivenTestConfiguration;
import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.Hidden;
import com.tngtech.jgiven.annotation.JGivenConfiguration;
import com.tngtech.jgiven.impl.ScenarioExecutorTest.TestSteps;
import com.tngtech.jgiven.junit.SimpleScenarioTest;
import com.tngtech.jgiven.report.model.ScenarioCaseModel;
import com.tngtech.jgiven.report.model.StepModel;
import com.tngtech.jgiven.tags.FeatureStepParameters;
import com.tngtech.jgiven.tags.Issue;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(DataProviderRunner.class)
@JGivenConfiguration(JGivenTestConfiguration.class)
public class ScenarioExecutorTest extends SimpleScenarioTest<TestSteps> {

    @Test
    public void methods_called_during_stage_construction_are_ignored_in_the_report() {
        given().some_stage_with_method_called_during_construction();
        then().the_method_does_not_appear_in_the_report(getScenario().getScenarioCaseModel());
    }

    @DataProvider
    public static Object[][] primitiveArrays() {
        return new Object[][] {
            {"byte", new byte[] {1, 2, 3}},
            {"char", new char[] {'a', 'b', 'c'}},
            {"short", new short[] {1, 2, 3}},
            {"int", new int[] {1, 2, 3}},
            {"long", new long[] {1, 2, 3}},
            {"double", new double[] {1, 2, 3}},
            {"float", new float[] {1, 2, 3}},
            {"boolean", new boolean[] {true, false}},
        };
    }

    @Test
    @UseDataProvider("primitiveArrays")
    @Issue("#1")
    @FeatureStepParameters
    public void step_methods_can_have_primitive_arrays_as_parameters(String type, Object array) {
        given().a_step_method_with_a_primitive_$_array_$_as_parameter(type, array);
        when().the_scenario_is_executed();
        then().no_exception_is_thrown();
    }

    @SuppressWarnings("UnusedReturnValue")
    static class TestSteps extends Stage<TestSteps> {
        String test = buildString();

        public String buildString() {
            return "testString";
        }

        TestSteps the_method_does_not_appear_in_the_report(@Hidden ScenarioCaseModel scenarioCaseModel) {
            StepModel stepModel = scenarioCaseModel.getFirstStep();
            assertThat(stepModel.getWords().get(1).getValue())
                .isNotEqualTo("buildString")
                .isEqualTo("some stage with method called during construction");
            return this;
        }

        TestSteps some_stage_with_method_called_during_construction() {
            return this;
        }

        TestSteps a_step_method_with_a_primitive_$_array_$_as_parameter(String type, Object array) {
            return this;
        }

        TestSteps the_scenario_is_executed() {
            return this;
        }

        TestSteps no_exception_is_thrown() {
            assertThat(true).as("no exception is thrown").isTrue();
            return this;
        }

        @SuppressWarnings("ConstantConditions")
        TestSteps a_step_failed() {
            assertThat(false).isTrue();
            return this;
        }
    }
}
