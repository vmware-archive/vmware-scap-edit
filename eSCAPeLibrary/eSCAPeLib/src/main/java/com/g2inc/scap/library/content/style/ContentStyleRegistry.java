package com.g2inc.scap.library.content.style;

/* ESCAPE Software   Copyright 2010 G2, Inc. - All rights reserved.
*
* ESCAPE is open source software distributed under GNU General Public License Version 3.  ESCAPE is not in the public domain
* and G2, Inc. holds its copyright.  Redistribution and use in source and binary forms, with or without modification, are
* permitted provided that the following conditions are met:

* 1. Redistributions of ESCAPE source code must retain the above copyright notice, this list of conditions and the following disclaimer.
* 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the ESCAPE Software distribution.
* 3. Neither the name of G2, Inc. nor the names of any contributors may be used to endorse or promote products derived from this software without specific prior written permission.

* THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS ``AS IS'' AND ANY EXPRESS OR IMPLIED WARRANTIES,
* INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
* IN NO EVENT SHALL G2, INC., THE AUTHORS OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY,
* OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
* OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
* OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
* POSSIBILITY OF SUCH DAMAGE.

* You should have received a copy of the GNU General Public License Version 3 along with this program.
* If not, see http://www.gnu.org/licenses/ for a copy.
*/

import java.io.DataInputStream;
import java.io.File;
import java.io.FilenameFilter;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.ClassFile;
import javassist.bytecode.annotation.Annotation;

import org.apache.log4j.Logger;

import com.g2inc.scap.library.domain.LibraryConfiguration;

/**
 * A singleton which can be queried for the available document styles
 *
 * @author ssill2
 */
public class ContentStyleRegistry {
	
    private static ContentStyleRegistry instance;
    private Map<String, ContentStyle> availableStyles = new HashMap<String, ContentStyle>();

    private List<String> styleClassNameList = new ArrayList<String>();
    private URLClassLoader urlClassLoader = null;
    List<URL> jarUrlList = new ArrayList<URL>();
    private static Logger LOG = Logger.getLogger(ContentStyleRegistry.class);

    private ContentStyleRegistry () {
        registerAvailableStyles();
    }

    public void registerAvailableStyles() {
    	availableStyles.clear();
    	// First find all jar files in the "style" directory
    	File styleJarDir = LibraryConfiguration.getInstance().getStyleJarDir();
    	if (styleJarDir != null) {
    		LOG.debug("Searching for jars in directory " + styleJarDir.getAbsolutePath());
    		File[] styleJars = styleJarDir.listFiles(new FilenameFilter() {

				@Override
				public boolean accept(File dir, String name) {
					return name.endsWith(".jar");
				}
    			
    		});
    		if (styleJars == null || styleJars.length == 0) {
    			return;
    		}
    		for (int i=0; i<styleJars.length; i++) {
    			findContentStylesInJar(styleJars[i]);
    		}
    	}
    	// At this point, all ContentStyle implementations should have their 
    	// class names in styleClassNameList. Now load the named classes.
    	LOG.debug("Found " + styleClassNameList.size() + " ContentStyles");
    	if (styleClassNameList.size() > 0) {
    		URL[] urls = jarUrlList.toArray(new URL[0]);
    		LOG.debug("Creating URLClassLoader");
    		urlClassLoader = new URLClassLoader(urls, this.getClass().getClassLoader());
        	for (String className : styleClassNameList) {
        		try {
        			LOG.debug("Loading class: " + className);
					Class<?> clazz = urlClassLoader.loadClass(className);
					ContentStyle style = (ContentStyle) clazz.newInstance();
					registerStyle(style);
				} catch (ClassNotFoundException e) {
					LOG.error("Could not find class " + className + " in jars " + Arrays.toString(urls), e);
				} catch (InstantiationException e) {
					LOG.error("Could not instantiate class " + className + " in jars " + Arrays.toString(urls), e);
				} catch (IllegalAccessException e) {
					LOG.error("Could not access class " + className + " in jars " + Arrays.toString(urls), e);
				}
        	}
    	}

        ContentStyle defaultStyle = new DefaultContentStyle();
        registerStyle(defaultStyle);
    }
    
    private void findContentStylesInJar(File jarFile) {
    	LOG.debug("Looking in directory: " + jarFile.getAbsolutePath());
    	boolean foundStyle = false;
    	try {
    		JarFile jar = new JarFile(jarFile);
    		Enumeration<JarEntry> entries = jar.entries();
    		LOG.debug("Number of entries: " + jar.size());
    		while(entries.hasMoreElements()) {
    			JarEntry je = entries.nextElement();
    			String entryName = je.getName();
    			LOG.debug("Processing entry: " + entryName);
    			if(entryName.endsWith(".class")) {
    				ClassFile classFile = null;
    				DataInputStream dis = new DataInputStream(jar.getInputStream(je));
    				classFile = new ClassFile(dis);
    				AnnotationsAttribute visible = 
    					(AnnotationsAttribute) classFile.getAttribute(AnnotationsAttribute.visibleTag);
    				LOG.debug("visible: " + (visible == null ? "null" : visible.getName()));
    				if(visible != null) {
    					Annotation annotation =  visible.getAnnotation("com.g2inc.scap.library.content.style.SCAPContentStyle");
    					LOG.debug("Annotation: " + (annotation == null ? "null" : annotation.getTypeName()));
    					if(annotation != null) {
    						String className = classFile.getName();
    						LOG.debug("className: " + className);
    						styleClassNameList.add(className);
    						foundStyle = true;
    					}
    				}
    			}
    		}
    		if (foundStyle) {
    			URL url = jarFile.toURI().toURL();
    			jarUrlList.add(url);
    		}
    		jar.close();
    	} catch(Exception e) {
    		LOG.error("Error searching jar file for ContentStyles: " + jarFile.getAbsolutePath(), e);
    	}
    }

    private void registerStyle(ContentStyle style) {
        if(!availableStyles.containsKey(style.getStyleName())) {
            availableStyles.put(style.getStyleName(), style);
        }
    }

    /**
     * Get an instance of this singleton.
     * 
     * @return ContentStyleRegistry
     */
    public static synchronized ContentStyleRegistry getInstance()
    {
        if(instance == null)
        {
            instance = new ContentStyleRegistry();
        }

        return instance;
    }

    /**
     * Get a list of the available content styles.
     * 
     * @return List<ContentStyle>
     */
    public List<ContentStyle> getAvailableStyles()
    {
        List<ContentStyle> ret = new ArrayList<ContentStyle>(availableStyles.size());
        ret.addAll(availableStyles.values());

        Collections.sort(ret, new ContentStyleComparator<ContentStyle>());
        return ret;
    }

    /**
     * Get a particular style by it's name.
     * 
     * @param name
     * @return ContentStyle
     */
    public ContentStyle getDocumentStyleByName(String name)
    {
        return availableStyles.get(name);
    }
}
