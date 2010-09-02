package org.jvnet.hk2.junit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.Suite;

/**
 * A suite of tests for testing {link @Before} and {link @After} test
 * annotations.
 * 
 */
@RunWith(Suite.class)
@Suite.SuiteClasses( { Hk2RunnerBeforeAndAfterTest.PositiveTest.class,
        Hk2RunnerBeforeAndAfterTest.ExceptionTest.class })
public class Hk2RunnerBeforeAndAfterTest {

    /**
     * Test for correct behavior when a method annotated with {link @Before}
     * or {link @Before} throws an exception
     * 
     */
    public static class ExceptionTest {

        /**
         * Test that an exception thrown in a @Before method propagates and a
         * test failure event is fired
         */
        @Test
        public void testBeforeMethodThrowsException() {
            RunNotifier notifier = new RunNotifier();

            final List<Failure> failures = new ArrayList<Failure>();

            RunListener rl = new RunListener() {

                @Override
                public void testFailure(Failure failure) throws Exception {
                    failures.add(failure);
                }

            };

            notifier.addListener(rl);

            try {
                new Hk2Runner(TestClassWithBeforeException.class).run(notifier);
                fail("Expected exception due to exception in @Before method");
            } catch (Exception ex) {
                // expected
            }
            
            assertEquals("Should be one test failure event", 1, failures.size());
        }

        /**
         * Test that an exception thrown in a {@link @After} method propagates
         * and a test failure event is fired
         */
        @Test
        public void testAfterMethodThrowsException() throws Throwable {
            RunNotifier notifier = new RunNotifier();

            final List<Failure> failures = new ArrayList<Failure>();

            RunListener rl = new RunListener() {

                @Override
                public void testFailure(Failure failure) throws Exception {
                    failures.add(failure);
                }

            };

            notifier.addListener(rl);

            try {
                new Hk2Runner(TestClassWithBeforeException.class).run(notifier);
                fail("Expected exception due to exception in @After method");
            } catch (Exception ex) {
                // expected
            }

            assertEquals("Should be one test failure event", 1, failures.size());
            
        }

        /**
         * A simple test class that throws an exception in a @Before method
         */
        public static class TestClassWithBeforeException {
            @Before
            public void beforeException() throws Exception {
                throw new Exception("exception in @Before method");
            }

            @Test
            public void test() throws Exception {
                fail("Should never reach this test");
            }
        }

        /**
         * A simple test class that throws an exception in an {@link @After}
         * method
         */
        public static class TestClassWithAfterException {
            @After
            public void afterException() throws Exception {
                throw new Exception("exception in @After method");
            }

            @Test
            public void test() throws Exception {
            }
        }
    }

    /**
     * Test class to ensure all {@link @Before} methods are called prior to each
     * test and all {@link @After} methods are called after each test
     */
    @RunWith(Hk2Runner.class)
    public static class PositiveTest {

        String beforeTestString = "";
        String afterTestString = "";

        @Before
        public void before1() {
            beforeTestString += " b1 ";
        }

        @Before
        void before2() {
            beforeTestString += " b2 ";
        }

        @Before
        @Ignore
        void beforeIgnored() {
            beforeTestString += " Ignore ";
        }

        @After
        void after1() {
            afterTestString += " a1 ";
        }

        @After
        void after2() {
            afterTestString += " a2 ";
        }

        @After
        @Ignore
        void afterIgnored() {
            afterTestString += " Ignore ";
        }

        /**
         * Test that the methods annotated with {@link @Before} have run
         */
        @Test
        public void testBeforeRan() {
            assertTrue("Missing @Before call", beforeTestString.contains("b1"));
            assertTrue("Missing @Before call", beforeTestString.contains("b2"));
            assertFalse("Unexpected call to @Before with @Ignore", beforeTestString.contains("Ignore"));
        }

        /**
         * Test that the methods annotated with {@link @After} have run
         */
        @Test
        public void testAfterRan() {
            assertTrue("Missing @After call", afterTestString.contains("a1"));
            assertTrue("Missing @After call", afterTestString.contains("a2"));
            assertFalse("Unexpected call to @After with @Ignore", afterTestString.contains("Ignore"));
        }
    }
}