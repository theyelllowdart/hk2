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

package org.glassfish.hk2.runlevel;


import org.glassfish.hk2.api.ActiveDescriptor;
import org.jvnet.hk2.annotations.Contract;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


/**
 * Contract for handling the activation and deactivation of run level
 * services.
 *
 * @author jtrent, tbeerbower
 */
@Contract
public interface Activator {

    /**
     * Activate the run level service associated with given descriptor.
     *
     * @param activeDescriptor  the descriptor
     */
    void activate(ActiveDescriptor<?> activeDescriptor);

    /**
     * Deactivate the run level service associated with given descriptor.
     *
     * @param activeDescriptor  the descriptor
     */
    void deactivate(ActiveDescriptor<?> activeDescriptor);

    /**
     * Wait for completion of run level progression.
     *
     * @throws java.util.concurrent.ExecutionException    if the completion code threw an exception
     * @throws java.util.concurrent.TimeoutException      if the wait timed out
     * @throws InterruptedException  if the current thread was interrupted
     *                               while waiting
     */
    void awaitCompletion() throws ExecutionException, InterruptedException,
            TimeoutException;

    /**
     * Wait for completion of run level progression.
     *
     * @param timeout  the timeout value
     * @param unit     the time unit
     *
     * @throws ExecutionException    if the completion code threw an exception
     * @throws TimeoutException      if the wait timed out
     * @throws InterruptedException  if the current thread was interrupted
     *                               while waiting
     */
    void awaitCompletion(long timeout, TimeUnit unit) throws ExecutionException,
            InterruptedException, TimeoutException;
}