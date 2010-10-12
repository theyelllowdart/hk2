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
package org.jvnet.hk2.component.classmodel;

import java.io.File;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import org.jvnet.hk2.component.Habitat;

/**
 * Helper for creating classpath approximating SE behavior.
 * 
 * @author Jerome Dochez
 * @author Jeff Trent
 * 
 * @since 3.1
 */
public abstract class ClassPathHelper {

  public LinkedHashSet<String> classpathEntries = new LinkedHashSet<String>();

  /**
   * Creates a ClassPathHelper instance.
   * 
   * @param h reserved for future use
   * @param allowTestClassPath true if surefire.test.class.path is considered
   * 
   * @return the ClassPathHelper
   */
  public static ClassPathHelper create(Habitat h, boolean allowTestClassPath) {
    return new ClassPathHelper(allowTestClassPath) {};
  }
  
  public static ClassPathHelper create(Habitat h, String classPath) {
    return new ClassPathHelper(classPath) {};
  }
  
  protected ClassPathHelper(boolean allowTestClassPath) {
    String classPath = (allowTestClassPath) ? System
        .getProperty("surefire.test.class.path") : null;
    if (null == classPath) {
      classPath = System.getProperty("java.class.path");
    }
    initialize(classPath);
  }
  
  public ClassPathHelper(String classPath) {
    initialize(classPath);
  }

  protected void initialize(String classPath) {
    if (classPath != null) {
      String[] filenames = classPath.split(File.pathSeparator);

      for (String filename : filenames) {
        if (!filename.equals("")) {
          final File classpathEntry = new File(filename);
          addTransitiveJars(classpathEntries, classpathEntry);
        }
      }
    }
  }

  /**
   * Find all jars referenced directly and indirectly via a classpath
   * specification typically drawn from java.class.path or
   * surefire.test.class.path System properties
   *
   * @return the set of entries in the classpath
   */
  public Set<String> getEntries() {
    return Collections.unmodifiableSet(classpathEntries);
  }

  /**
   * Add provided File and all of its transitive manifest classpath entries to
   * the provided set
   * 
   * @param cpSet
   *          a Set to hold classpath entries
   * @param classpathFile
   *          File to transitively add to set
   */
  private static void addTransitiveJars(Set<String> cpSet,
      final File classpathFile) {
    cpSet.add(classpathFile.getAbsolutePath());

    if (classpathFile.exists()) {
      try {
        if (classpathFile.isFile()) {
          JarFile jarFile = null;
          Manifest mf;
          try {
            jarFile = new JarFile(classpathFile);

            mf = jarFile.getManifest();
          } finally {
            if (jarFile != null) {
              jarFile.close();
            }
          }

          // manifest may contain additional classpath
          if (mf != null) {
            String additionalClasspath = mf.getMainAttributes().getValue(
                Attributes.Name.CLASS_PATH);

            if (additionalClasspath != null) {
              for (String classpathEntry : additionalClasspath.split(" ")) {
                if (!classpathEntry.equals("")) {
                  File mfClasspathFile = new File(classpathFile.getParent(),
                      classpathEntry.trim());

                  if (mfClasspathFile.exists()
                      && !cpSet.contains(mfClasspathFile.getAbsolutePath())) {
                    addTransitiveJars(cpSet, mfClasspathFile);
                  }
                }
              }
            }
          }
        }
      } catch (Exception ex) {
        ex.printStackTrace();
      }
    }
  }

}