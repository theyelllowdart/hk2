/*
 * 
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 2007-2010 Sun Microsystems, Inc. All rights reserved.
 * 
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License. You can obtain
 * a copy of the License at https://glassfish.dev.java.net/public/CDDL+GPL.html
 * or glassfish/bootstrap/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 * 
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at glassfish/bootstrap/legal/LICENSE.txt.
 * Sun designates this particular file as subject to the "Classpath" exception
 * as provided by Sun in the GPL Version 2 section of the License file that
 * accompanied this code.  If applicable, add the following below the License
 * Header, with the fields enclosed by brackets [] replaced by your own
 * identifying information: "Portions Copyrighted [year]
 * [name of copyright owner]"
 * 
 * Contributor(s):
 * 
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
package org.jvnet.hk2.component;

/**
 * Holds the state(s) for a particular RunLevelService.
 * 
 * @author Jeff Trent
 * 
 * @since 3.1
 *
 */
public interface RunLevelState<T> {

  /**
   * The environment value for this state.
   * 
   * @return the class type used to uniquely identify the environment in
   * context, or null representing the default environment.
   */
  Class<T> getEnvironment();

  /**
   * The current run level state.  This represents the last run level
   * successfully achieved by the underlying RunLevelService responsible
   * for this environment.
   * 
   * @return the current run level, or null if no run level has been
   * been achieved.
   */
  Integer getCurrentRunLevel();
  
  /**
   * The planned run level state.  If this value is different from
   * current run level, this signifies movement of the underlying
   * RunLevelService.
   * 
   * @return the planned run level, or null if there is no planned
   *    level.  This value is established by a call to
   *    {@link RunLevelService#proceedTo(int)}.
   */
  Integer getPlannedRunLevel();
  
}