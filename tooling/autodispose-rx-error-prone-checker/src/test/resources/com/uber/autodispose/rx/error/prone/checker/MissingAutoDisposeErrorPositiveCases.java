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

package com.uber.autodispose.rx.error.prone.checker;

import com.uber.autodispose.LifecycleEndedException;
import com.uber.autodispose.LifecycleScopeProvider;
import com.uber.autodispose.TestLifecycleScopeProvider;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.annotations.CheckReturnValue;
import io.reactivex.functions.Function;
import io.reactivex.subjects.BehaviorSubject;
import javax.annotation.Nullable;

/**
 * Cases that don't use autodispose and should fail the MissingAutodisposeError check.
 */
public class MissingAutoDisposeErrorPositiveCases
    implements LifecycleScopeProvider<TestLifecycleScopeProvider.TestLifecycle> {

  private final BehaviorSubject<TestLifecycleScopeProvider.TestLifecycle> lifecycleSubject =
      BehaviorSubject.create();

  /**
   * @return a sequence of lifecycle events.
   */
  @CheckReturnValue public Observable<TestLifecycleScopeProvider.TestLifecycle> lifecycle() {
    return lifecycleSubject.hide();
  }

  /**
   * @return a sequence of lifecycle events. It's recommended to back this with a static instance to
   * avoid unnecessary object allocation.
   */
  @CheckReturnValue
  public Function<TestLifecycleScopeProvider.TestLifecycle, TestLifecycleScopeProvider.TestLifecycle> correspondingEvents() {
    return new Function<TestLifecycleScopeProvider.TestLifecycle, TestLifecycleScopeProvider.TestLifecycle>() {
      @Override public TestLifecycleScopeProvider.TestLifecycle apply(
          TestLifecycleScopeProvider.TestLifecycle testLifecycle) {
        switch (testLifecycle) {
          case STARTED:
            return TestLifecycleScopeProvider.TestLifecycle.STOPPED;
          case STOPPED:
            throw new LifecycleEndedException();
          default:
            throw new IllegalStateException("Unknown lifecycle event.");
        }
      }
    };
  }

  /**
   * @return the last seen lifecycle event, or {@code null} if none.
   */
  @Nullable public TestLifecycleScopeProvider.TestLifecycle peekLifecycle() {
    return lifecycleSubject.getValue();
  }

  public void observable_subscribeWithoutAutodispose() {
    // BUG: Diagnostic contains: Always apply an Autodispose scope before subscribing
    Observable.empty().subscribe();
  }

  public void single_subscribeWithoutAutodispose() {
    // BUG: Diagnostic contains: Always apply an Autodispose scope before subscribing
    Single.just(true).subscribe();
  }

  public void completable_subscribeWithoutAutodispose() {
    // BUG: Diagnostic contains: Always apply an Autodispose scope before subscribing
    Completable.complete().subscribe();
  }

  public void maybe_subscribeWithoutAutodispose() {
    // BUG: Diagnostic contains: Always apply an Autodispose scope before subscribing
    Maybe.empty().subscribe();
  }

  public void flowable_subscribeWithoutAutodispose() {
    // BUG: Diagnostic contains: Always apply an Autodispose scope before subscribing
    Flowable.empty().subscribe();
  }
}
