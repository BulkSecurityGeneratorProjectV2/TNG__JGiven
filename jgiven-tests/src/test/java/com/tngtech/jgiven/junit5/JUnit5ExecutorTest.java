package com.tngtech.jgiven.junit5;

import com.tngtech.jgiven.GivenScenarioTest;
import com.tngtech.jgiven.JGivenScenarioTest;
import com.tngtech.jgiven.tags.FeatureJUnit5;
import com.tngtech.jgiven.tags.Issue;
import com.tngtech.jgiven.testframework.ThenTestFramework;
import com.tngtech.jgiven.testframework.WhenTestFramework;
import org.junit.Test;

@FeatureJUnit5
public class JUnit5ExecutorTest
    extends JGivenScenarioTest<GivenScenarioTest<?>, WhenTestFramework<?>, ThenTestFramework<?>> {


    @Test
    public void tests_with_scenario_modifications_in_after_method() {
        given().junit5_tests_with_scenario_modifications_in_after_method();
        when().the_test_class_is_executed_with_JUnit5();
        then().each_scenario_contains_$_cases(2);
    }

    @Test
    public void tests_with_a_per_class_lifecycle() {
        given().junit5_test_class_with_a_per_class_lifecycle();
        when().the_test_class_is_executed_with_JUnit5();
        then().the_test_fails_with_message("JGiven does not support keeping a test instance over multiple scenarios");
    }

    @Test
    @Issue("#25")
    public void a_valid_report_is_generated_for_classes_that_are_disabled() {
        given().a_test_class_with_all_tests_ignored();
        when().the_test_class_is_executed_with_JUnit5();
        then().the_report_model_is_either_null_or_empty()
            .and().has_a_valid_class_name_if_it_is_not_null();
    }

    @Test
    @Issue("#49")
    public void exception_in_scenario_is_not_hidden_by_exception_in_JUnit_after_method() {
        given().a_test_class_with_a_failing_scenario_and_a_failing_after_stage();
        when().the_test_class_is_executed_with_JUnit5();
        then().the_test_fails_with_message("assertion failed in test step");
    }

    @Test
    public void steps_following_failing_steps_are_reported_as_skipped() {
        given().a_failing_test_with_$_steps(3)
            .and().step_$_fails(1);
        when().the_test_is_executed_with_JUnit5();
        then().step_$_is_reported_as_failed(1)
            .and().step_$_is_reported_as_skipped(2)
            .and().step_$_is_reported_as_skipped(3);
    }

    @Test
    public void after_stage_methods_of_stages_following_failing_stages_are_ignored() {
        given().a_failing_test_with_$_steps(2)
            .and().the_test_has_$_failing_stages(2)
            .and().stage_$_has_a_failing_after_stage_method(2)
            .and().step_$_fails(1);
        when().the_test_is_executed_with_JUnit5();
        then().the_test_fails()
            .and().step_$_is_reported_as_failed(1)
            .and().step_$_is_reported_as_skipped(2);
    }

    @Test
    public void all_steps_of_stages_following_failing_stages_are_ignored() {
        given().a_failing_test_with_$_steps(2)
            .and().the_test_has_$_failing_stages(2)
            .and().step_$_fails(1);
        when().the_test_is_executed_with_JUnit();
        then().the_test_fails()
            .and().step_$_is_reported_as_failed(1)
            .and().step_$_is_reported_as_skipped(2);
    }
}
