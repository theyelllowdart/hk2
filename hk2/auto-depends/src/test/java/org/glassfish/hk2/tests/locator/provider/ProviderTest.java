/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2012 Oracle and/or its affiliates. All rights reserved.
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
package org.glassfish.hk2.tests.locator.provider;

import junit.framework.Assert;

import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.api.ServiceLocatorFactory;
import org.junit.Before;
import org.junit.Test;

/**
 * @author jwells
 *
 */
public class ProviderTest {
    public final static String MOBY_DICK = "Moby Dick";  // Big Whale
    public final static String ISHMAEL = "Ishmael";  // probably not his real name
    public final static String QUEEQUEG = "QueeQueg"; // Escape on his coffin
    
    public final static String EAGLES = "Eagles";  // Epic team
    public final static String SHADY = "LeShaun McCoy";  // Best back in NFL
    
    public final static String GIANTS = "Giants";  // Epic FAIL
    public final static String ELI = "Eli Manning";  // Jerk
    
            
    public final static String TEST_NAME = "ProviderTest";
    private ServiceLocator locator;
    
    @Before
    public void before() {
        locator = ServiceLocatorFactory.getInstance().create(TEST_NAME, new ProviderModule());
        if (locator == null) {
            locator = ServiceLocatorFactory.getInstance().find(TEST_NAME);   
        }
    }

    @Test
    public void testSimpleProvider() {
        ProviderInjected pi = locator.getService(ProviderInjected.class);
        Assert.assertNotNull(pi);
        
        Assert.assertFalse(InstantiationChecker.getIsInstantiated());
        
        pi.doTheGet();
        
        Assert.assertTrue(InstantiationChecker.getIsInstantiated());
    }
    
    @Test
    public void testSingleShotIterable() {
        Menagerie zoo = locator.getService(Menagerie.class);
        Assert.assertNotNull(zoo);
        
        zoo.validateAllEagles();
    }
    
    @Test
    public void testIterableQualifiedBy() {
        Menagerie zoo = locator.getService(Menagerie.class);
        Assert.assertNotNull(zoo);
        
        zoo.validateAllGiants();
    }
    
    @Test
    public void testIterableOfIterableAndHandleIterable() {
        Menagerie zoo = locator.getService(Menagerie.class);
        Assert.assertNotNull(zoo);
        
        zoo.validateQueequeg();
    }
    
    @Test
    public void testIterableOfType() {
        Menagerie zoo = locator.getService(Menagerie.class);
        Assert.assertNotNull(zoo);
        
        zoo.validateBookCharacters();
        
    }
    
    @Test
    public void testIterableNamed() {
        Menagerie zoo = locator.getService(Menagerie.class);
        Assert.assertNotNull(zoo);
        
        zoo.validateAllCharacters();
        
    }
}