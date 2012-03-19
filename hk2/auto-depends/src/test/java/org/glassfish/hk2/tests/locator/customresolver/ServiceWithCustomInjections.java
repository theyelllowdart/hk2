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
package org.glassfish.hk2.tests.locator.customresolver;

import javax.inject.Inject;

import junit.framework.Assert;

import org.glassfish.hk2.api.ServiceLocator;

/**
 * @author jwells
 *
 */
public class ServiceWithCustomInjections {
    private final static String FIELD = "Field";
    private final static String CONSTRUCTOR = "Constructor";
    private final static String METHOD = "Method";
    
    @Inject @Path(FIELD)
    private String byField;
    
    private final String byConstructor;
    
    private String byMethod;
    
    private boolean isValid = false;
    
    @Inject
    private ServiceWithCustomInjections(
            ServiceLocator locator,
            @Path(CONSTRUCTOR) String byConstructor) {
        Assert.assertNotNull(locator);
        this.byConstructor = byConstructor; 
    }
    
    @SuppressWarnings("unused")
    @Inject
    private void viaMethod(@Path(METHOD) String byMethod) {
        this.byMethod = byMethod;
    }
    
    @SuppressWarnings("unused")
    private void postConstruct() {
        Assert.assertEquals(FIELD, byField);
        Assert.assertEquals(CONSTRUCTOR, byConstructor);
        Assert.assertEquals(METHOD, byMethod);
        
        isValid = true;
    }

    public boolean isValid() {
        return isValid;
    }
}