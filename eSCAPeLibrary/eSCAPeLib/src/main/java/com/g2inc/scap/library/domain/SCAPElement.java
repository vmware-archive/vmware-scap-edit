package com.g2inc.scap.library.domain;
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


import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;

/**
 * This class is the base class for most of the other domain objects.
 * it actually holds the JDOM element for the object in question as well as
 * the Element that represents the root of the document.
 */
public interface SCAPElement
{

  /**
   * Returns the SCAPDocument object this belongs to.
   *
   * @return SCAPDocument
   * @see com.g2inc.scap.library.domain.SCAPDocument
   */
  public SCAPDocument getSCAPDocument();


  /**
   * Set the SCAPDocument object this belongs to.
   *
   * @param document SCAPDocument this is part of
   * @see com.g2inc.scap.library.domain.SCAPDocument
   */
  public void setSCAPDocument(SCAPDocument document);


  public static final Namespace XCCDF11_NAMESPACE = Namespace.getNamespace("http://checklists.nist.gov/xccdf/1.1");
  public static final Namespace XCCDF12_NAMESPACE = Namespace.getNamespace("http://checklists.nist.gov/xccdf/1.2");
  public static final Namespace CPE_LANG_NAMESPACE = Namespace.getNamespace("http://cpe.mitre.org/language/2.0");
  public static final Namespace DUBLIN_CORE_NAMESPACE = Namespace.getNamespace("http://purl.org/dc/elements/1.1/");

  /**
   * Get the JDOM Element this object represents.
   *
   * @return Element
   * @see org.jdom.Element
   */
  public Element getElement();

  /**
   * Set the JDOM Element this object represents.
   *
   * @param element
   * @see org.jdom.Element
   */
  public void setElement(Element element);


  /**
   * Get the JDOM Element for the root of the document
   * this is part of.
   *
   * @return Element
   * @see org.jdom.Element
   */
  public Element getRoot();

  /**
   * Set the JDOM Element for the root of the document
   * this is part of.
   *
   * @param root
   * @see org.jdom.Element
   */
  public void setRoot(Element root);

  /**
   * Get the JDOM Document object for the SCAPDocument
   * this is part of.
   *
   * @return Document
   * @see org.jdom.Document
   * @see com.g2inc.scap.library.domain.SCAPDocument
   */
  public Document getDoc();

  /**
   * Set the JDOM Document object for the SCAPDocument
   * this is part of.
   *
   * @param doc JDOM Document
   * @see org.jdom.Document
   * @see com.g2inc.scap.library.domain.SCAPDocument
   */
  public void setDoc(Document doc);


  /**
   * Get the id, as defined in the element's "id" attribute.
   *
   * @return String
   */
  public String getId();

  /**
   * Set the "id" attribute in element
   *
   * @param id String representing id
   */
  public void setId(String id);

  /**
   * Inserts a new element under this one in the proper location according the orderMap.
   * Some schemas insist on elements being in a certain order, so this method takes care of
   * inserting in the correct location.
   *
   * @param newElement The element to be inserted
   * @param orderMap The map that tells us what order the element needs to be in
   */
  public void insertChild(Element newElement, HashMap<String, Integer> orderMap);

  /**
   * Inserts a new element under this one in the proper location according the orderMap.
   * Some schemas insist on elements being in a certain order, so this method takes care of
   * inserting in the correct location.
   *
   * @param newSCAPElement The element to be inserted
   * @param orderMap The map that tells us what order the element needs to be in
   * @param indexInSameTagList desired index
   */
  public void insertChild(SCAPElement newSCAPElement, HashMap<String, Integer> orderMap, int indexInSameTagList);

  
  /**
   * Inserts a new element under this one in the proper location according the orderMap.
   * Some schemas insist on elements being in a certain order, so this method takes care of
   * inserting in the correct location.
   *
   * @param newElement The element to be inserted
   * @param orderMap The map that tells us what order the element needs to be in
   */
  public void insertChild(Element newElement, HashMap<String, Integer> orderMap, Namespace namespace);

  /**
   * Inserts a new element under this one in the proper location according the orderMap.
   * Some schemas insist on elements being in a certain order, so this method takes care of
   * inserting in the correct location.
   *
   * @param newElement The element to be inserted
   * @param orderMap The map that tells us what order the element needs to be in
   * @param indexInSameTagList desired index
   */
  public void insertChild(Element newElement, HashMap<String, Integer> orderMap, int indexInSameTagList);
  
  /**
   * Inserts a new element under this one in the proper location according the orderMap.
   * Some schemas insist on elements being in a certain order, so this method takes care of
   * inserting in the correct location.
   *
   * @param newElement The element to be inserted
   * @param orderMap The map that tells us what order the element needs to be in
   * @param indexInSameTagList desired index
   */
  public void insertChild(Element newElement, HashMap<String, Integer> orderMap, int indexInSameTagList, Namespace namespace);
  
  public void removeElement(SCAPElement scapElement);
  
  /**
   * Gets the text value of a child Element of the current Element. This is handy
   * when the child element has a simple string value, and is not itself a SCAPElement.
   * This version assumes the child element Namespace is the same as this Element's 
   * Namespace.
   * 
   * @param elementName the child element name 
   * @return the text contained in the named child element
   */  
  public String getChildStringValue(String elementName);
  /**
   * Gets the text value of a child Element of the current Element. This is handy
   * when the child element has a simple string value, and is not itself a SCAPElement
   * 
   * @param elementName the child element name 
   * @param namespace   the child element namespace
   * @return the text contained in the named child element
   */
  public String getChildStringValue(String elementName, Namespace namespace);
  
  /**
   * Sets the text value of a child Element of the current Element. This is handy
   * when the child element has a simple string value, and is not itself a SCAPElement.
   * This version assumes the child element Namespace is the same as this Element's 
   * Namespace.
   * 
   * @param elementName the child element name 
   * @param elementValue the value to be set as the child elements contents
   * @param orderMap The map that tells us what order the element needs to be in
   * 
   * @return the text contained in the named child element
   */
  public void setStringValueChild(String elementName, 
		  String elementValue, 
		  HashMap<String, Integer> orderMap);
  
  /**
   * Sets the text value of a child Element of the current Element. This is handy
   * when the child element has a simple string value, and is not itself a SCAPElement
   * 
   * @param elementName the child element name 
   * @param elementValue the value to be set as the child elements contents
   * @param orderMap The map that tells us what order the element needs to be in
   * @param namespace   the child element namespace
   * 
   * @return the text contained in the named child element
   */
  public void setStringValueChild(String elementName, 
		  String elementValue, 
		  HashMap<String, Integer> orderMap, 
		  Namespace namespace);

  /**
   * Replaces a list of elements under this one in the proper location according the orderMap.
   * Some schemas insist on elements being in a certain order, so this method takes care of
   * inserting in the correct location.
   *
   * @param newElementList The elements to be inserted
   * @param orderMap The map that tells us what order the element needs to be in
   * @param elementName
   */
  public void replaceList(List<?> newElementList, HashMap<String, Integer> orderMap, String elementName);
  
  /**
   * Replaces a list of elements under this one in the proper location according the orderMap.
   * Some schemas insist on elements being in a certain order, so this method takes care of
   * inserting in the correct location.
   *
   * @param newElementList The elements to be inserted
   * @param orderMap The map that tells us what order the element needs to be in
   * @param elementName
   */
  public void replaceList(List<?> newElementList, HashMap<String, Integer> orderMap, String elementName, Namespace namespace);;
  /**
   * Replaces a list of string element names under this one in the proper location according the orderMap.
   * Some schemas insist on elements being in a certain order, so this method takes care of
   * inserting in the correct location.
   *
   * @param newElementList The element names to be inserted
   * @param orderMap The map that tells us what order the element needs to be in
   * @param elementName
   */
  public void replaceStringList(List<String> newElementList, HashMap<String, Integer> orderMap, String elementName);
  
  /**
   * Replaces a list of string element names under this one in the proper location according the orderMap,
   * assuming that the list has only one element. Used in some SCAPElements which have both setXList(newElementList) 
   * and setX(String newValue) methods, where X is the name of a child element. 
   * Some schemas insist on elements being in a certain order, so this method takes care of
   * inserting in the correct location.
   *
   * @param newElementList The element names to be inserted
   * @param orderMap The map that tells us what order the element needs to be in
   * @param elementName
   */
  public void replaceStringListWithSingleValue(String newValue, HashMap<String, Integer> orderMap, String elementName);

  
  /**
   * Gets a list of SCAPElements that are children of this one, and have the same namespace as this Element.
   *
   * @param elementName
   * @param itemClass
   * @return List<T>
   */
  public <T extends SCAPElementImpl> List<T> getSCAPElementList(String elementName, Class<T> itemClass);
  
  
  /**
   * Gets a list of SCAPElements that are children of this one.
   *
   * @param elementName The name of the element
   * @param itemClass The class of the item.
   * @param namespace The namespace of the element.
   * 
   * @return List<T>
   */
  public <T extends SCAPElementImpl> List<T> getSCAPElementList(String elementName, Class<T> itemClass, Namespace namespace);
  
  /**
   * Gets a a SCAPElement that is a child of this one, and has the same namespace. If more than one 
   * child of this element with the requested elementName exist, only the first one will be returned. 
   * This method is handy when maxOccurs for the requested elementName is "1" or defaulted.
   *
   * @param elementName The name of the element
   * @param itemClass The class of the item
   * 
   * @return List<T>
   */
  public <T extends SCAPElementImpl> T getSCAPElement(String elementName, Class<T> itemClass);
  
  /**
   * Gets a a SCAPElement that is a child of this one. If more than one child of this element with
   * the requested elementName exist, only the first one will be returned. This method is handy when
   * maxOccurs for the requested elementName is "1" or defaulted.
   *
   * @param elementName The name of the element
   * @param itemClass The class of the item
   * @param namespace The namespace of the element
   * 
   * @return List<T>
   */
  public <T extends SCAPElementImpl> T getSCAPElement(String elementName, Class<T> itemClass, Namespace namespace);
  
  /**
   * Creates a new SCAPElement, whose org.jdom.Element is created based on the 
   * supplied tag name and class, and whose root and doc fields are 
   * copied from this SCAPElement. 
   *
   * @param tag  - the string used to create the jdom Element for the new SCAPElement
   * @param clazz - the class of the new SCAPElement (eg, Profile, Group, etc)
   * 
   * @return SCAPElement - the new SCAPElement
   */
  public <T> SCAPElement createSCAPElement(String tag, Class<T> clazz);
  
  /**
   * Creates a new SCAPElement, whose org.jdom.Element is supplied as a parameter. 
   * The new SCAPElement is instantiated based on the supplied clazz parameter, and 
   * has the same root and doc fields as this SCAPElement.
   *
   * @param elem The jdom Element for the new SCAPElement
   * @param clazz The class of the new SCAPElement (eg, Profile, Group, etc)
   * 
   * @return SCAPElement - the new SCAPElement
   */
  public <T> SCAPElement createSCAPElement(Element elem, Class<T> clazz);
  
  /**
   * Sets a new SCAPelement child of this element, replacing any existing child, assuming child element
   * has the same namespace as this element.  Use this method only when maxOccurs of the child element is 1.
   * If maxOccurs is > 1 or unbounded, use getSCAPElementList.
   * 
   * @param scapElement  new SCAPElement to be set
   * @param orderMap   The map that tells us what order the element needs to be in
   * @param elementName  name of the child element 
   */
  public void setSCAPElement(SCAPElement scapElement, 
		  HashMap<String, Integer> orderMap, 
		  String elementName);
  
  /**
   * Sets a new SCAPelement child of this element, replacing any existing child.  Use this method only 
   * when maxOccurs of the child element is 1. If maxOccurs is > 1 or unbounded, use getSCAPElementList.
   * 
   * @param scapElement  new SCAPElement to be set
   * @param orderMap   The map that tells us what order the element needs to be in
   * @param elementName  name of the child element 
   * @param namespace    namespace of the child element
   */
  public void setSCAPElement(SCAPElement scapElement, 
		  HashMap<String, Integer> orderMap, 
		  String elementName, 
		  Namespace namespace);

  /**
   * Gets a list of element values (Strings) from child elements of a specified name.
   *
   * @param elementName The name of the element.
   * 
   * @return List<String>
   * 
   * @see org.jdom.Element
   */
  public List<String> getStringList(String elementName);
  
  /**
   * Gets the first element value (String) from child elements of a specified name.
   * 
   * @param elementName the name of the child element
   * @return the first child nodes text value, or null if no children of that name
   */
  public String getStringListSingleValue(String elementName);

  /**
   * Sets an attribute in the underlying JDOM Element
   *
   * @param name The attribute name
   * @param value The attribute value
   * 
   * @see org.jdom.Element
   */
  public void setAttribute(String name, String value);

  /**
   * Gets an attribute in the underlying JDOM Element.
   *
   * @param name The attribute name

   * @return String
   * 
   * @see org.jdom.Element
   */
  public String getAttribute(String name);

  /**
   *  Gets the name of the underlying JDOM Element.
   *
   * @return String
   * 
   * @see org.jdom.Element
   */
  public String getElementName();

  /**
   *  Sets the name of the underlying JDOM Element.
   *
   * @param elementName The name of the element
   *
   * @see org.jdom.Element
   */
  public void setElementName(String elementName);
  
  /**
   * Returns the order map that insertChild uses to determine 
   * the proper order to insert elements. This method is intended
   * to be overriden by subclasses of this on.
   *   
   * @return HashMap<String, Integer>
   */
  public HashMap<String, Integer> getOrderMap();
  
  public void setOptionalAttribute(String attributeName, String attributeValue);
  
  public boolean getBoolean(String attributeName, boolean defaultValue);
  
  public void setBoolean(String attributeName, Boolean attributeValue); 
  
  public Boolean getBoolean(String attributeName);
    
  public void setBoolean(String attributeName, boolean attributeValue);
  
  public BigDecimal getBigDecimalAttribute(String attributeName) throws NumberFormatException; 

  
  public void setBigDecimalAttribute(String attributeName, BigDecimal decimal);
  
  public void insertStringValueChild(String elementName, 
		  String elementValue, 
		  HashMap<String, Integer> orderMap);
  
  public void insertStringValueChild(String elementName, 
		  String elementValue, 
		  HashMap<String, Integer> orderMap, 
		  Namespace namespace) ;
  
  public <T extends SCAPElement> List<T> getSCAPElementIntList(String elementName, Class<T> itemClass);
  
  
  /**
   * Gets a list of SCAPElements that are children of this one.
   *
   * @param elementName The name of the element
   * @param itemClass The class of the item.
   * @param namespace The namespace of the element.
   * 
   * @return List<T>
   */
  public <T extends SCAPElement> List<T> getSCAPElementIntList(String elementName, Class<T> itemClass, Namespace namespace);
  
  /**
   * Gets a a SCAPElement that is a child of this one, and has the same namespace. If more than one 
   * child of this element with the requested elementName exist, only the first one will be returned. 
   * This method is handy when maxOccurs for the requested elementName is "1" or defaulted.
   *
   * @param elementName The name of the element
   * @param itemClass The class of the item
   * 
   * @return List<T>
   */
  public <T extends SCAPElement> T getSCAPElementInt(String elementName, Class<T> itemClass);
  
  /**
   * Gets a a SCAPElement that is a child of this one. If more than one child of this element with
   * the requested elementName exist, only the first one will be returned. This method is handy when
   * maxOccurs for the requested elementName is "1" or defaulted.
   *
   * @param elementName The name of the element
   * @param itemClass The class of the item
   * @param namespace The namespace of the element
   * 
   * @return List<T>
   */
  public <T extends SCAPElement> T getSCAPElementInt(String elementName, Class<T> itemClass, Namespace namespace);
  
  /**
   * Creates a new SCAPElement, whose org.jdom.Element is created based on the 
   * supplied tag name and class, and whose root and doc fields are 
   * copied from this SCAPElement. 
   *
   * @param tag  - the string used to create the jdom Element for the new SCAPElement
   * @param clazz - the class of the new SCAPElement (eg, Profile, Group, etc)
   * 
   * @return SCAPElement - the new SCAPElement
   */
  public <T extends SCAPElement> SCAPElement createSCAPElementInt(String tag, Class<T> clazz);
  
  /**
   * Creates a new SCAPElement, whose org.jdom.Element is supplied as a parameter. 
   * The new SCAPElement is instantiated based on the supplied clazz parameter, and 
   * has the same root and doc fields as this SCAPElement.
   *
   * @param elem The jdom Element for the new SCAPElement
   * @param clazz The class of the new SCAPElement (eg, Profile, Group, etc)
   * 
   * @return SCAPElement - the new SCAPElement
   */
  public <T extends SCAPElement> SCAPElement createSCAPElementInt(Element elem, Class<T> clazz);

}
