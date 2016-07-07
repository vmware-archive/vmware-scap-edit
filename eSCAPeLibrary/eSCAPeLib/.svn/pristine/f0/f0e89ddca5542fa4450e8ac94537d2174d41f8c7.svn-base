package com.g2inc.scap.library.schema;
/* ESCAPE Software Copyright 2010 G2, Inc. - All rights reserved.
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

import java.util.HashMap;
import java.util.Iterator;

import org.apache.log4j.Logger;

/**
 * Class used to encapuslate the string used as the value to an xsi:schemaLocation attribute.
 * The value to this attribute is a string in the form of "namespaceURI locationURI ...".  The string
 * always has an even number of space separated entries.  The namespaceURI represents a namespace, and the 
 * locationURI tells you where to find the schema for the given namespace. 
 */
public class SchemaLocationAttribute
{
	private static Logger log = Logger.getLogger(SchemaLocationAttribute.class);

	private HashMap<String, String> entries;
	
	/**
	 * No args constructor
	 */
	public SchemaLocationAttribute()
	{
		this(null);
	}
		
	/**
	 * A constructor that takes an existing schemaLocation string and builds a map of existing
	 * entries from that string.
	 * 
	 * @param schemaLocationString
	 */
	public SchemaLocationAttribute(String schemaLocationString)
	{
		entries = new HashMap<String, String>();
		
		if(schemaLocationString != null)
		{
			String[] parts = schemaLocationString.split("\\s+");
			
			if(parts != null)
			{
				if((parts.length % 2) == 0)
				{
					String namespaceUri = null;
					String locationUri = null;
					
					for(int x = 0; x < parts.length; x++)
					{
						if(namespaceUri == null)
						{
							namespaceUri = parts[x];
						}
						else if(locationUri == null)
						{
							locationUri = parts[x];
							
							entries.put(namespaceUri, locationUri);
							
							namespaceUri = null;
							locationUri = null;
						}
					}
				}
				else
				{
					log.error("schemaLocation string has uneven number of entries: " + schemaLocationString);
				}
			}
		}
	}

	/**
	 * Does this schemaLocation contain an entry for the given namespace.
	 * 
	 * @param namespaceURI The namespace to check for.
	 * 
	 * @return boolean
	 */
	public boolean containsNamespaceURI(String namespaceURI)
	{
		return entries.containsKey(namespaceURI);
	}
	
	/**
	 * Add an entry for the given namespace.
	 * 
	 * @param namespaceURI The namespace to add a location for.
	 * @param locationURI The URI where to find the schema for the given namespace.
	 */
	public void addNamespace(String namespaceURI, String locationURI)
	{
		entries.put(namespaceURI, locationURI);
	}
	
	/**
	 * Override the default toString() method to return a string
	 * that's can be used as a value to the schemaLocation attribute.
	 * 
	 *  @return String
	 */
	@Override
	public String toString()
	{
		String ret = null;
		
		StringBuilder sb = new StringBuilder();
		
		if(entries.size() > 0)
		{
			Iterator<String> nsItr = entries.keySet().iterator();
			
			while(nsItr.hasNext())
			{
				String key = nsItr.next();
				String value = entries.get(key);
				
				if(nsItr.hasNext())
				{
					sb.append(key + " " + value + " ");
				}
				else
				{
					sb.append(key + " " + value);
				}
			}
			
			ret = sb.toString();
		}
		else
		{
			ret = "";
		}
		
		return ret;
	}
	
	/**
	 * Return the number of namespace entries in this attribute.
	 * 
	 * @return int
	 */
	public int entryCount()
	{
		return entries.size();
	}
}
