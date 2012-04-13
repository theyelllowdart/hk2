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
package org.jvnet.hk2.deprecated.internal;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.glassfish.hk2.MultiMap;
import org.glassfish.hk2.utilities.DescriptorImpl;

/**
 * @author jwells
 *
 */
public class Utilities {
    public static boolean safeEquals(Object a, Object b) {
        if (a == b) return true;
        if (a == null) return false;
        if (b == null) return false;
        
        return a.equals(b);
    }
    
    public static void printThrowable(Throwable th) {
        int lcv = 0;
        while (th != null) {
            System.err.println("[" + lcv++ + "]=" + th.getMessage());
            th.printStackTrace();
            
            th = th.getCause();
        }
    }
    
    public static void fillInMetadata(MultiMap<String, String> multi, DescriptorImpl d) {
        if (multi == null) return;
        
        for (String key : multi.keySet()) {
            List<String> values = multi.get(key);
            
            if (values == null) continue;
            
            for (String value : values) {
                d.addMetadata(key, value);
            }
        }
    }

}