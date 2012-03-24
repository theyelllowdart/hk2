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
package org.glassfish.hk2.tests.locator.typesafety;

import org.glassfish.hk2.api.Configuration;
import org.glassfish.hk2.api.Factory;
import org.glassfish.hk2.tests.locator.utilities.TestModule;
import org.glassfish.hk2.utilities.BuilderHelper;

/**
 * @author jwells
 *
 */
public class TypeSafetyModule implements TestModule {

    /* (non-Javadoc)
     * @see org.glassfish.hk2.api.Module#configure(org.glassfish.hk2.api.Configuration)
     */
    @Override
    public void configure(Configuration configurator) {
        configurator.bind(BuilderHelper.linkFactory(PSIntegerFactory.class).to(ParameterizedService.class).build());
        configurator.bind(BuilderHelper.link(PSIntegerFactory.class).to(Factory.class).build());
        
        // The ranking here should make this one get chosen in ambiguous cases
        configurator.bind(BuilderHelper.linkFactory(PSStringFactory.class).to(ParameterizedService.class).build());
        configurator.bind(BuilderHelper.link(PSStringFactory.class).to(Factory.class).build());
        
        // This guy is called by default
        configurator.bind(BuilderHelper.linkFactory(PSDoubleFactory.class).to(ParameterizedService.class).ofRank(100).build());
        configurator.bind(BuilderHelper.link(PSDoubleFactory.class).to(Factory.class).build());
        
        configurator.bind(BuilderHelper.link(TypeVariableService.class).build());
        
        configurator.bind(BuilderHelper.link(RawPSInjectee.class).build());
        configurator.bind(BuilderHelper.link(WildcardPSInjectee.class).build());
        configurator.bind(BuilderHelper.link(WildcardUpperBoundPSInjectee.class).build());
        configurator.bind(BuilderHelper.link(WildcardLowerBoundPSInjectee.class).build());
        configurator.bind(BuilderHelper.link(WildcardTVSInjectee.class).build());
        configurator.bind(BuilderHelper.link(ActualTypeTVSInjectee.class).build());
        configurator.bind(BuilderHelper.link(TypeVariableTVSInjectee.class).build());
        
    }

}