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
package org.glassfish.hk2.tests.locator.locator;

import java.util.List;

import junit.framework.Assert;

import org.glassfish.hk2.api.Descriptor;
import org.glassfish.hk2.api.Filter;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.api.ServiceLocatorFactory;
import org.junit.Before;
import org.junit.Test;
import org.jvnet.hk2.internal.Pretty;

/**
 * @author jwells
 *
 */
public class LocatorTest {
    public final static String TEST_NAME = "LocatorTest";
    private ServiceLocator locator;
    
    @Before
    public void before() {
        locator = ServiceLocatorFactory.getInstance().create(TEST_NAME, new LocatorModule());
        if (locator == null) {
            locator = ServiceLocatorFactory.getInstance().find(TEST_NAME);   
        }
    }
    
    @Test
    public void testGetAllServices() {
        
        List<AdminCommand> handles = locator.getAllServices(AdminCommand.class);
        Assert.assertNotNull(handles);
        
        Assert.assertTrue("Expected all three handles, but got " + handles.size(), handles.size() == 3);
        
        // Now get the specific items that should be in the set, and verify they are all there
        BootCommand bootCommand = (BootCommand) locator.getService(AdminCommand.class, "BootCommand");
        GetStatisticsCommand statsCommand = (GetStatisticsCommand) locator.getService(AdminCommand.class, "GetStatisticsCommand");
        ShutdownCommand shutdownCommand = (ShutdownCommand) locator.getService(AdminCommand.class, "ShutdownCommand");
        
        Assert.assertNotNull(bootCommand);
        Assert.assertNotNull(statsCommand);
        Assert.assertNotNull(shutdownCommand);
        
        Assert.assertTrue("Returned services did not contain BootCommand " + Pretty.collection(handles), handles.contains(bootCommand));
        Assert.assertTrue("Returned services did not contain StatsCommand " + Pretty.collection(handles), handles.contains(statsCommand));
        Assert.assertTrue("Returned services did not contain ShtudownCommand " + Pretty.collection(handles), handles.contains(shutdownCommand));
    }
    
    @Test
    public void testGetAllServicesWithFilter() {
        
        List<?> handles = locator.getAllServices(new Filter() {

            @Override
            public boolean matches(Descriptor d) {
                return true;
            }
            
        });
        Assert.assertNotNull(handles);
        
        Assert.assertTrue("Expected at least four handles, but got " + handles.size(), handles.size() >= 4);
        
        // Now get the specific items that should be in the set, and verify they are all there
        BootCommand bootCommand = (BootCommand) locator.getService(AdminCommand.class, "BootCommand");
        GetStatisticsCommand statsCommand = (GetStatisticsCommand) locator.getService(AdminCommand.class, "GetStatisticsCommand");
        ShutdownCommand shutdownCommand = (ShutdownCommand) locator.getService(AdminCommand.class, "ShutdownCommand");
        ServiceLocator locatorItself = (ServiceLocator) locator.getService(ServiceLocator.class);
        
        Assert.assertTrue("Returned services did not contain BootCommand " + Pretty.collection(handles), handles.contains(bootCommand));
        Assert.assertTrue("Returned services did not contain StatsCommand " + Pretty.collection(handles), handles.contains(statsCommand));
        Assert.assertTrue("Returned services did not contain ShtudownCommand " + Pretty.collection(handles), handles.contains(shutdownCommand));
        Assert.assertTrue("Returned services did not contain ServiceLocator " + Pretty.collection(handles), handles.contains(locatorItself));
    }

}