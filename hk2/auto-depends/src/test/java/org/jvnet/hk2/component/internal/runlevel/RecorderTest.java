package org.jvnet.hk2.component.internal.runlevel;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.jvnet.hk2.annotations.Inject;
import org.jvnet.hk2.component.ComponentException;
import org.jvnet.hk2.component.Habitat;
import org.jvnet.hk2.component.Inhabitant;
import org.jvnet.hk2.component.MultiMap;
import org.jvnet.hk2.component.RunLevelState;
import org.jvnet.hk2.component.InhabitantListener.EventType;
import org.jvnet.hk2.junit.Hk2Runner;
import org.jvnet.hk2.test.contracts.Simple;
import org.jvnet.hk2.test.impl.TwoSimple;
import org.jvnet.hk2.test.runlevel.RandomContract;
import org.jvnet.hk2.test.runlevel.RunLevelFiveService;
import org.jvnet.hk2.test.runlevel.RunLevelTenService;
import org.jvnet.hk2.test.runlevel.RunLevelTwentyService;

import com.sun.hk2.component.Holder;
import com.sun.hk2.component.Inhabitants;
import com.sun.hk2.component.TestRunLevelInhabitant;
import com.sun.hk2.component.TestRunLevelState;

/**
 * Recorder Test
 * 
 * @author Jeff Trent
 */
@SuppressWarnings("unchecked")
@RunWith(Hk2Runner.class)
public class RecorderTest {

  @Inject
  Habitat h;
  
  @Test
  public void releaseAffects() {
    List<Inhabitant<?>> list = new ArrayList<Inhabitant<?>>();
    Recorder recorder = new Recorder(list, 0);
    RunLevelState rlState = new TestRunLevelState(0, 0);

    Holder.Impl cl = new Holder.Impl(getClass().getClassLoader());

    Inhabitant<?> delegate = Inhabitants.createInhabitant(h, cl,
        TwoSimple.class.getName(), new MultiMap(), null,
        Collections.singleton(Simple.class.getName()));
    TestRunLevelInhabitant i1 = new TestRunLevelInhabitant(delegate, 0, rlState, null);
    
    recorder.inhabitantChanged(EventType.INHABITANT_RELEASED, i1);
    
    assertEquals(0, list.size());
  }

  @Test
  public void nonRunLevelActivateAffects() {
    List<Inhabitant<?>> list = new ArrayList<Inhabitant<?>>();
    Recorder recorder = new Recorder(list, 0);
    RunLevelState rlState = new TestRunLevelState(0, 0);

    Holder.Impl cl = new Holder.Impl(getClass().getClassLoader());

    Inhabitant<?> delegate = Inhabitants.createInhabitant(h, cl,
        TwoSimple.class.getName(), new MultiMap(), null,
        Collections.singleton(Simple.class.getName()));
    TestRunLevelInhabitant i1 = new TestRunLevelInhabitant(delegate, 0, rlState, null);
    
    i1.get();
    recorder.inhabitantChanged(EventType.INHABITANT_ACTIVATED, i1);
    
    assertEquals("not a RunLevel service", 0, list.size());
  }
  
  @Test
  public void activateReleaseAffects() {
    List<Inhabitant<?>> list = new ArrayList<Inhabitant<?>>();
    Recorder recorder = new Recorder(list, 5);
    RunLevelState rlState = new TestRunLevelState(5, 5);

    Holder.Impl cl = new Holder.Impl(getClass().getClassLoader());

    Inhabitant<?> delegate = Inhabitants.createInhabitant(h, cl,
        RunLevelFiveService.class.getName(), new MultiMap(), null,
        Collections.singleton(Simple.class.getName()));
    TestRunLevelInhabitant i1 = new TestRunLevelInhabitant(delegate, 0, rlState, null);
    
    i1.get();
    recorder.inhabitantChanged(EventType.INHABITANT_ACTIVATED, i1);

    assertEquals(1, list.size());
    assertSame(i1, list.get(0));
    
    recorder.release();

    assertEquals(0, list.size());
    assertFalse(i1.isInstantiated());
  }
  
  @Test
  public void affectsOfInvalidRunLevelActivations() {
    List<Inhabitant<?>> list = new ArrayList<Inhabitant<?>>();
    Recorder recorder = new Recorder(list, 10);
    RunLevelState rlState = new TestRunLevelState(10, 10);

    Holder.Impl cl = new Holder.Impl(getClass().getClassLoader());

    Inhabitant<?> delegateLow = Inhabitants.createInhabitant(h, cl,
        RunLevelFiveService.class.getName(), new MultiMap(), null,
        Collections.singleton(RandomContract.class.getName()));
    TestRunLevelInhabitant low = new TestRunLevelInhabitant(delegateLow, 0, rlState, null);
    
    Inhabitant<?> delegateCorrect = Inhabitants.createInhabitant(h, cl,
        RunLevelTenService.class.getName(), new MultiMap(), null,
        Collections.singleton(RandomContract.class.getName()));
    TestRunLevelInhabitant correct = new TestRunLevelInhabitant(delegateCorrect, 0, rlState, null);

    Inhabitant<?> delegateHigh = Inhabitants.createInhabitant(h, cl,
        RunLevelTwentyService.class.getName(), new MultiMap(), null,
        Collections.singleton(RandomContract.class.getName()));
    TestRunLevelInhabitant high = new TestRunLevelInhabitant(delegateHigh, 0, rlState, null);

    low.get();
    recorder.inhabitantChanged(EventType.INHABITANT_ACTIVATED, low);

    correct.get();
    recorder.inhabitantChanged(EventType.INHABITANT_ACTIVATED, correct);

    high.get();
    try {
      recorder.inhabitantChanged(EventType.INHABITANT_ACTIVATED, high);
      fail("Exception expected");
    } catch (ComponentException e) {
      // expected
    }

    assertEquals("we should record even during exceptions", 3, list.size());
    
    recorder.release();

    assertEquals(0, list.size());
    assertFalse(low.isInstantiated());
    assertFalse(correct.isInstantiated());
    assertFalse(high.isInstantiated());
  }
  
}