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
package org.jvnet.hk2.internal;

import java.util.SortedSet;

import javax.inject.Inject;
import javax.inject.Named;

import org.glassfish.hk2.api.ActiveDescriptor;
import org.glassfish.hk2.api.Injectee;
import org.glassfish.hk2.api.InjectionResolver;
import org.glassfish.hk2.api.JustInTimeInjectionResolver;
import org.glassfish.hk2.api.ServiceHandle;

/**
 * @author jwells
 *
 */
@Named(InjectionResolver.SYSTEM_RESOLVER_NAME)
public class ThreeThirtyResolver implements InjectionResolver<Inject> {
    private final ServiceLocatorImpl locator;
    
    /* package */ ThreeThirtyResolver(ServiceLocatorImpl locator) {
        this.locator = locator;
    }
    
    private Object secondChanceResolve(Injectee injectee, ServiceHandle<?> root) {
        // OK, lets do the second chance protocol
        Collector collector = new Collector();
        collector.addThrowable(new IllegalStateException("There was no object available for injection at " + injectee));
        
        SortedSet<ServiceHandle<JustInTimeInjectionResolver>> jitResolvers =
                Utilities.<SortedSet<ServiceHandle<JustInTimeInjectionResolver>>>cast(
                locator.getAllServiceHandles(JustInTimeInjectionResolver.class));
        
        try {
            boolean modified = false;
            boolean aJITFailed = false;
            for (ServiceHandle<JustInTimeInjectionResolver> handle : jitResolvers) {
                JustInTimeInjectionResolver jitResolver = handle.getService();
                
                boolean jitModified = false;
                try {
                    jitModified = jitResolver.justInTimeResolution(injectee);
                }
                catch (Throwable th) {
                    collector.addThrowable(th);
                    aJITFailed = true;
                }
                
                modified = jitModified || modified;
            }
            
            if (aJITFailed) {
                collector.throwIfErrors();
            }
            
            if (!modified) {
                if (injectee.isOptional()) return null;
                
                collector.throwIfErrors();
            }
            
            // Try again
            ActiveDescriptor<?> ad = locator.getInjecteeDescriptor(injectee);
            
            if (ad == null) {
                if (injectee.isOptional()) return null;
                
                collector.throwIfErrors();
            }
            
            return locator.getService(ad, root);  
        }
        finally {
            for (ServiceHandle<JustInTimeInjectionResolver> jitResolver : jitResolvers) {
                jitResolver.destroy();
            }
        }
    }

    /* (non-Javadoc)
     * @see org.glassfish.hk2.api.InjectionResolver#resolve(org.glassfish.hk2.api.Injectee, org.glassfish.hk2.api.ServiceHandle)
     */
    @Override
    public Object resolve(Injectee injectee, ServiceHandle<?> root) {
        ActiveDescriptor<?> ad = locator.getInjecteeDescriptor(injectee);
        
        if (ad == null) {
            return secondChanceResolve(injectee, root);
        }
        
        return locator.getService(ad, root);
    }

}