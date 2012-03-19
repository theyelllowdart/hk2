/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2011 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */
package org.glassfish.hk2.tests;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Named;

import org.glassfish.hk2.api.Descriptor;
import org.glassfish.hk2.api.Factory;
import org.glassfish.hk2.api.Filter;
import org.glassfish.hk2.scopes.Singleton;
import org.glassfish.hk2.tests.contracts.AnotherContract;
import org.glassfish.hk2.tests.contracts.Red;
import org.glassfish.hk2.tests.contracts.SomeContract;
import org.glassfish.hk2.tests.services.AnotherService;
import org.glassfish.hk2.utilities.BuilderHelper;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author jwells
 *
 */
public class FilterBuilderTest {
	private final static String NAME = "hello";
	
	/**
	 * This predicate will only have an implementation and a contract
	 */
	@Test
	public void testSimpleFilter() {
		Descriptor predicate = BuilderHelper.link(FilterBuilderTest.class).to(SomeContract.class).build();
		
		Assert.assertNotNull(predicate);
		
		Assert.assertNotNull(predicate.getImplementation());
		Assert.assertEquals(predicate.getImplementation(), FilterBuilderTest.class.getName());
		
		Assert.assertNotNull(predicate.getAdvertisedContracts());
		Assert.assertTrue(predicate.getAdvertisedContracts().size() == 2);
		
		Assert.assertNotNull(predicate.getMetadata());
		Assert.assertTrue(predicate.getMetadata().size() == 0);
		
		Assert.assertNotNull(predicate.getQualifiers());
		Assert.assertTrue(predicate.getQualifiers().size() == 0);
		
		Assert.assertNull(predicate.getName());
		
		Assert.assertNull(predicate.getScope());
	}
	
	private final static String KEY_A = "keya";
	private final static String KEY_B = "keyb";
	private final static String VALUE_A = "valuea";
	private final static String VALUE_B1 = "valueb1";
	private final static String VALUE_B2 = "valueb2";
	
	/**
	 * This predicate will have two of those things which allow multiples and
	 * one thing of all other things
	 */
	@Test
	public void testFullFilter() {
		LinkedList<String> multiValue = new LinkedList<String>();
		multiValue.add(VALUE_B1);
		multiValue.add(VALUE_B2);
		
		
		Descriptor predicate = BuilderHelper.link(AnotherService.class.getName()).
				to(SomeContract.class).
				to(AnotherContract.class.getName()).
				in(Singleton.class.getName()).
				named(NAME).
				has(KEY_A, VALUE_A).
				has(KEY_B, multiValue).
				qualifiedBy(Red.class.getName()).
				build();
		
		Assert.assertNotNull(predicate);
		
		Assert.assertNotNull(predicate.getImplementation());
		Assert.assertEquals(predicate.getImplementation(), AnotherService.class.getName());
		
		HashSet<String> correctSet = new HashSet<String>();
		correctSet.add(SomeContract.class.getName());
		correctSet.add(AnotherContract.class.getName());
		correctSet.add(AnotherService.class.getName());
		
		Assert.assertNotNull(predicate.getAdvertisedContracts());
		Assert.assertTrue(predicate.getAdvertisedContracts().size() == 3);
		Assert.assertTrue(correctSet.containsAll(predicate.getAdvertisedContracts()));
		
		correctSet.clear();
		correctSet.add(Red.class.getName());
		correctSet.add(Named.class.getName());
		
		Assert.assertNotNull(predicate.getQualifiers());
		Assert.assertTrue(predicate.getQualifiers().size() == 2);  // One for @Named
		Assert.assertTrue(correctSet.containsAll(predicate.getQualifiers()));
		
		Assert.assertEquals(NAME, predicate.getName());
		
		Assert.assertNotNull(predicate.getScope());
		Assert.assertEquals(Singleton.class.getName(), predicate.getScope());
		
		Assert.assertNotNull(predicate.getMetadata());
		Assert.assertTrue(predicate.getMetadata().size() == 2);
		
		Map<String, List<String>> metadata = predicate.getMetadata();
		Set<String> keySet = metadata.keySet();
		
		correctSet.clear();
		correctSet.add(KEY_A);
		correctSet.add(KEY_B);
		
		Assert.assertTrue(correctSet.containsAll(keySet));
		
		List<String> aValue = metadata.get(KEY_A);
		Assert.assertNotNull(aValue);
		Assert.assertTrue(aValue.size() == 1);
		Assert.assertEquals(aValue.get(0), VALUE_A);
		
		List<String> bValue = metadata.get(KEY_B);
		Assert.assertNotNull(bValue);
		Assert.assertTrue(bValue.size() == 2);
		Assert.assertEquals(bValue.get(0), VALUE_B1);
		Assert.assertEquals(bValue.get(1), VALUE_B2);
	}
	
	@Test
	public void testEmptyFilter() {
		Descriptor predicate = BuilderHelper.link().build();
		
		Assert.assertNotNull(predicate);
		
		Assert.assertNull(predicate.getImplementation());
		
		Assert.assertNotNull(predicate.getAdvertisedContracts());
		Assert.assertTrue(predicate.getAdvertisedContracts().size() == 0);
		
		Assert.assertNotNull(predicate.getMetadata());
		Assert.assertTrue(predicate.getMetadata().size() == 0);
		
		Assert.assertNotNull(predicate.getQualifiers());
		Assert.assertTrue(predicate.getQualifiers().size() == 0);
		
		Assert.assertNull(predicate.getName());
		
		Assert.assertNull(predicate.getScope());
	}
	
	@Test
    public void testAllDescriptorFilter() {
        Descriptor predicate = BuilderHelper.link().build();
    
        Filter allFilter = BuilderHelper.allFilter();
    
        Assert.assertTrue(allFilter.matches(predicate));
    }
	
	@Test
	public void testFactoryFilter() {
	    Descriptor df = BuilderHelper.link(GoodFactory.class, false).to(Factory.class).build();
	    
	    Assert.assertTrue(df.getAdvertisedContracts().size() == 1);
	    Assert.assertTrue(df.getAdvertisedContracts().contains(Factory.class.getName()));
	    
	    Assert.assertEquals(String.class.getName(), df.getName());
	}
	
	@Test
    public void testFactoryWithName() {
        Descriptor df = BuilderHelper.link(AnotherGoodFactory.class).build();
        
        Assert.assertTrue(df.getAdvertisedContracts().size() == 1);
        Assert.assertTrue(df.getAdvertisedContracts().contains(Factory.class.getName()));
        
        Assert.assertEquals(List.class.getName(), df.getName());
    }
	
	@Test
    public void testLinkFactory() {
        Descriptor df = BuilderHelper.linkFactory(GoodFactory.class).to(String.class).build();
        
        Assert.assertTrue(df.getAdvertisedContracts().size() == 1);
        Assert.assertTrue(df.getAdvertisedContracts().contains(String.class.getName()));
        
        Assert.assertNull("Did not expect a name, but got " + df.getName(), df.getName());
    }
	
	@Test
    public void testAutoGenName() {
        Descriptor df = BuilderHelper.link(NamedService.class).build();
        
        Assert.assertTrue(df.getAdvertisedContracts().size() == 1);
        Assert.assertTrue(df.getAdvertisedContracts().contains(NamedService.class.getName()));
        
        Assert.assertEquals("NamedService", df.getName());
    }
}