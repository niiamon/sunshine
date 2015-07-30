package com.example.android.sunshine.test;

import android.test.suitebuilder.TestSuiteBuilder;

import junit.framework.Test;

/**
 * Created by niiamon on 22/07/2015.
 */
public class FullTestSuite {
  public static Test suite() {
    return new TestSuiteBuilder(FullTestSuite.class)
        .includeAllPackagesUnderHere().build();
  }

  public FullTestSuite() {
    super();
  }
}
