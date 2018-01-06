/*
 * Copyright (C) 2017. Uber Technologies
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.uber.autodispose.error.prone.checker;

import com.google.errorprone.CompilationTestHelper;
import java.util.Arrays;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
@SuppressWarnings("CheckTestExtendsBaseClass")
public class MissingAutoDisposeErrorCheckerTest {

  @Rule public TemporaryFolder temporaryFolder = new TemporaryFolder();

  private CompilationTestHelper compilationHelper;

  @Before
  public void setup() {
    compilationHelper =
        CompilationTestHelper
            .newInstance(MissingAutoDisposeErrorChecker.class, getClass());
    compilationHelper.setArgs(
        Arrays.asList(
            "-d",
            temporaryFolder.getRoot().getAbsolutePath(),
            "-XepOpt:AutoDisposeLeakCheck"
                + "=com.uber.autodispose.error.prone.checker.ComponentWithLifeCycle"));
  }

  @Test
  public void test_autodisposePositiveCases() {
    compilationHelper.addSourceFile("MissingAutoDisposeErrorPositiveCases.java").doTest();
  }

  @Test
  public void test_autodisposePositiveCases2() {
    compilationHelper.addSourceFile("MissingAutoDisposeErrorPositiveCases2.java").doTest();
  }

  @Test
  public void test_autodisposeNegativeCases() {
    compilationHelper.addSourceFile("MissingAutoDisposeErrorNegativeCases.java").doTest();
  }
}