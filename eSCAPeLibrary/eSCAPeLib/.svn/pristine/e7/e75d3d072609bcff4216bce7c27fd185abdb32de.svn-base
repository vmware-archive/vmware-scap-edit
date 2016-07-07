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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.jdom.Content;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;

/**
 * This class is the base class for most of the other domain objects.
 * it actually holds the JDOM element for the object in question as well as
 * the Element that represents the root of the document.
 */
public abstract class SCAPElementImpl implements SCAPElement
{

  protected Element element = null;
  protected Element root = null;
  protected Document doc = null;
  protected SCAPDocument SCAPDocument = null;

  /**
   * Returns the SCAPDocument object this belongs to.
   *
   * @return SCAPDocument
   * @see com.g2inc.scap.library.domain.SCAPDocument
   */
  public SCAPDocument getSCAPDocument()
  {
    return SCAPDocument;
  }

  /**
   * Set the SCAPDocument object this belongs to.
   *
   * @param document SCAPDocument this is part of
   * @see com.g2inc.scap.library.domain.SCAPDocument
   */
  public void setSCAPDocument(SCAPDocument document)
  {
    SCAPDocument = document;
  }

  /**
   * Get the JDOM Element this object represents.
   *
   * @return Element
   * @see org.jdom.Element
   */
  public Element getElement()
  {
    return element;
  }

  /**
   * Set the JDOM Element this object represents.
   *
   * @param element
   * @see org.jdom.Element
   */
  public void setElement(Element element)
  {
    this.element = element;
  }

  /**
   * Get the JDOM Element for the root of the document
   * this is part of.
   *
   * @return Element
   * @see org.jdom.Element
   */
  public Element getRoot()
  {
    return root;
  }

  /**
   * Set the JDOM Element for the root of the document
   * this is part of.
   *
   * @param root
   * @see org.jdom.Element
   */
  public void setRoot(Element root)
  {
    this.root = root;
  }

  /**
   * Get the JDOM Document object for the SCAPDocument
   * this is part of.
   *
   * @return Document
   * @see org.jdom.Document
   * @see com.g2inc.scap.library.domain.SCAPDocument
   */
  public Document getDoc()
  {
    return doc;
  }

  /**
   * Set the JDOM Document object for the SCAPDocument
   * this is part of.
   *
   * @param doc JDOM Document
   * @see org.jdom.Document
   * @see com.g2inc.scap.library.domain.SCAPDocument
   */
  public void setDoc(Document doc)
  {
    this.doc = doc;
  }


  /**
   * Get the id, as defined in the element's "id" attribute.
   *
   * @return String
   */
  public String getId()
  {
    String id = getElement().getAttributeValue("id");
    return id;
  }

  /**
   * Set the "id" attribute in element
   *
   * @param id String representing id
   */
  public void setId(String id)
  {
	  this.element.setAttribute("id", id);
  }

  /**
   * Inserts a new element under this one in the proper location according the orderMap.
   * Some schemas insist on elements being in a certain order, so this method takes care of
   * inserting in the correct location.
   *
   * @param newElement The element to be inserted
   * @param orderMap The map that tells us what order the element needs to be in
   */
  public void insertChild(Element newElement, HashMap<String, Integer> orderMap)
  {
    insertChild(newElement, orderMap, -1);
  }

  /**
   * Inserts a new element under this one in the proper location according the orderMap.
   * Some schemas insist on elements being in a certain order, so this method takes care of
   * inserting in the correct location.
   *
   * @param newSCAPElement The element to be inserted
   * @param orderMap The map that tells us what order the element needs to be in
   * @param indexInSameTagList desired index
   */
  public void insertChild(SCAPElement newSCAPElement, HashMap<String, Integer> orderMap, int indexInSameTagList)
  {
    insertChild(newSCAPElement.getElement(), orderMap, indexInSameTagList);
  }
  
  /**
   * Inserts a new element under this one in the proper location according the orderMap.
   * Some schemas insist on elements being in a certain order, so this method takes care of
   * inserting in the correct location.
   *
   * @param newElement The element to be inserted
   * @param orderMap The map that tells us what order the element needs to be in
   */
  public void insertChild(Element newElement, HashMap<String, Integer> orderMap, Namespace namespace)
  {
    insertChild(newElement, orderMap, -1);
  }

  /**
   * Inserts a new element under this one in the proper location according the orderMap.
   * Some schemas insist on elements being in a certain order, so this method takes care of
   * inserting in the correct location.
   *
   * @param newElement The element to be inserted
   * @param orderMap The map that tells us what order the element needs to be in
   * @param indexInSameTagList desired index
   */
  public void insertChild(Element newElement, HashMap<String, Integer> orderMap, int indexInSameTagList)
  {
	  insertChild(newElement, orderMap, indexInSameTagList, null);
  }
  /**
   * Inserts a new element under this one in the proper location according the orderMap.
   * Some schemas insist on elements being in a certain order, so this method takes care of
   * inserting in the correct location.
   *
   * @param newElement The element to be inserted
   * @param orderMap The map that tells us what order the element needs to be in
   * @param indexInSameTagList desired index
   */
  public void insertChild(Element newElement, HashMap<String, Integer> orderMap, int indexInSameTagList, Namespace namespace)
  {
    //
    // indexInSameTagList specifies the relative position of the new Element within
    // the existing List of identically named tags. A value of 0 -> new Element is first,
    // a value of -1 means new Element is last, within the List.
    int contentSize = element.getContentSize();
    int lastTagIndex = -1;
    Element childElement = null;
    String newElementTag = newElement.getName();
    int newElementTagOrder = orderMap.get(newElementTag);
    int contentIndex = 0;
    for (contentIndex = 0; contentIndex < contentSize; contentIndex++)
    {
      Content childContent = element.getContent(contentIndex);
      if (childContent instanceof Element)
      {
        childElement = (Element) childContent;
        String childTag = childElement.getName();
		if (childTag == null) {
			throw new IllegalStateException("Child element tag is null");
		}
		if (orderMap == null) {
			throw new IllegalStateException("Order map is null");
		}
		Integer integer = orderMap.get(childTag);
		if (integer == null) {
			throw new IllegalStateException("Order map has no value for tag " + childTag);
		}
        int childElementTagOrder = orderMap.get(childTag);
        if (newElementTagOrder > childElementTagOrder)
        {
          // if still finding tags which must come BEFORE new Element, continue
          continue;
        } else if (newElementTagOrder < childElementTagOrder)
        {
          // just reached an element which must come AFTER new Element, so new Element goes here
          break;
        }
        // childTag and newElementTag have same insertion order
        if (!newElementTag.equals(childTag))
        {
          // probably won't happen often, but we found a childTag with the SAME order as
          // the new Element, but not with the same tag name. Put new Element AFTER any
          // such tags.
          continue;
        }
        // we are working through list of Elements with same tag as Element to be added
        if (indexInSameTagList == -1)
        {
          // if new Element goes at end of identically named tag list, continue until
          // we get to Element with different tag, or end of Content list, whichever is first
          continue;
        }
        if (indexInSameTagList == 0)
        {
          // if new Element goes at start of identically named tag list, it goes here
          break;
        }
        lastTagIndex++;
        if (lastTagIndex > indexInSameTagList)
        {
          break;
        }
      }
    }
    if (newElement.getParent() == null) {
    	element.addContent(contentIndex, newElement);
    } else {
    	element.addContent(contentIndex, (Element) newElement.clone());
    }
  }
  
  public void removeElement(SCAPElement scapElement) {
	  if (this.element.equals(scapElement.getElement().getParentElement())) {
		  this.element.removeContent(scapElement.getElement());
	  }
  }
  
  /**
   * Gets the text value of a child Element of the current Element. This is handy
   * when the child element has a simple string value, and is not itself a SCAPElement.
   * This version assumes the child element Namespace is the same as this Element's 
   * Namespace.
   * 
   * @param elementName the child element name 
   * @return the text contained in the named child element
   */  
  public String getChildStringValue(String elementName) {
	  return getChildStringValue(elementName, this.element.getNamespace());
  }
  
  /**
   * Gets the text value of a child Element of the current Element. This is handy
   * when the child element has a simple string value, and is not itself a SCAPElement
   * 
   * @param elementName the child element name 
   * @param namespace   the child element namespace
   * @return the text contained in the named child element
   */
  public String getChildStringValue(String elementName, Namespace namespace) {
	  String value = null;
	  if (namespace == null) {
		  namespace = this.element.getNamespace();
	  }
	  Element childElement = this.element.getChild(elementName, namespace);
	  if (childElement != null) {
		  value = childElement.getText();
	  }
	  return value;
  }
  
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
		  HashMap<String, Integer> orderMap) {
	  setStringValueChild(elementName, elementValue, orderMap, this.element.getNamespace());
  }
  
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
		  Namespace namespace) {
	  Element childElement = this.element.getChild(elementName, namespace);
	  if (childElement == null) {
		  childElement = new Element(elementName, namespace);
		  insertChild(childElement, orderMap);
	  }
	  childElement.setText(elementValue);
  }

  /**
   * Replaces a list of elements under this one in the proper location according the orderMap.
   * Some schemas insist on elements being in a certain order, so this method takes care of
   * inserting in the correct location.
   *
   * @param newElementList The elements to be inserted
   * @param orderMap The map that tells us what order the element needs to be in
   * @param elementName
   */
  public void replaceList(List<?> newElementList, HashMap<String, Integer> orderMap, String elementName)
  {
	  replaceList(newElementList, orderMap, elementName, element.getNamespace());
  }
  
  /**
   * Replaces a list of elements under this one in the proper location according the orderMap.
   * Some schemas insist on elements being in a certain order, so this method takes care of
   * inserting in the correct location.
   *
   * @param newElementList The elements to be inserted
   * @param orderMap The map that tells us what order the element needs to be in
   * @param elementName
   */
  public void replaceList(List<?> newElementList, HashMap<String, Integer> orderMap, String elementName, Namespace namespace)
  {
	  if (elementName != null) 
	  {
		  element.removeChildren(elementName, namespace);
	  }   
      for (int i = 0; i < newElementList.size(); i++)
      {
    	  SCAPElementImpl scapElement = (SCAPElementImpl) newElementList.get(i);
    	  insertChild(scapElement, orderMap, -1);
      }
  }
  
  /**
   * Replaces a list of elements under this one in the proper location according the orderMap.
   * Some schemas insist on elements being in a certain order, so this method takes care of
   * inserting in the correct location.
   *
   * @param newElementList The elements to be inserted
   * @param orderMap The map that tells us what order the element needs to be in
   * @param elementName
   */
  public void replaceList(List<?> newElementList, HashMap<String, Integer> orderMap, String[] elementNames)
  {
	  replaceList(newElementList, orderMap, elementNames, element.getNamespace());
  }
  
  /**
   * Replaces a list of elements under this one in the proper location according the orderMap.
   * Some schemas insist on elements being in a certain order, so this method takes care of
   * inserting in the correct location.
   *
   * @param newElementList The elements to be inserted
   * @param orderMap The map that tells us what order the element needs to be in
   * @param elementNames String[] containing element names to be replaced; should all be same index in orderMap 
   */
  public void replaceList(List<?> newElementList, HashMap<String, Integer> orderMap, String[] elementNames, Namespace namespace)
  {
	  for (String elementName : elementNames) {
		  element.removeChildren(elementName, namespace);
	  } 
      for (int i = 0; i < newElementList.size(); i++)
      {
    	  SCAPElementImpl scapElement = (SCAPElementImpl) newElementList.get(i);
    	  insertChild(scapElement, orderMap, -1);
      }
  }

  /**
   * Replaces a list of string element names under this one in the proper location according the orderMap.
   * Some schemas insist on elements being in a certain order, so this method takes care of
   * inserting in the correct location.
   *
   * @param newElementList The element names to be inserted
   * @param orderMap The map that tells us what order the element needs to be in
   * @param elementName
   */
  public void replaceStringList(List<String> newElementList, HashMap<String, Integer> orderMap, String elementName)
  {
    element.removeChildren(elementName, element.getNamespace());
    for (int i = 0; i < newElementList.size(); i++)
    {
      Element newElement = new Element(elementName, element.getNamespace());
      newElement.setText(newElementList.get(i));
      insertChild(newElement, orderMap, -1);
    }
  }
  
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
  public void replaceStringListWithSingleValue(String newValue, HashMap<String, Integer> orderMap, String elementName)
  {
	  element.removeChildren(elementName, element.getNamespace());
	  Element newElement = new Element(elementName, element.getNamespace());
	  insertChild(newElement, orderMap, -1);
  }

//  /**
//   * Gets a list of SCAPElements that are children of this one, and have the same namespace as this Element.
//   *
//   * @param elementName
//   * @param itemClass
//   * @return List<T>
//   */
//  public <T> List<T> getApiElementList(List<T> list, String elementName, Class<?> itemClass)
//  {
//    return getApiElementList(list, elementName, itemClass, element.getNamespace());
//  }
  
  /**
   * Gets a list of SCAPElements that are children of this one, and have the same namespace as this Element.
   *
   * @param elementName
   * @param itemClass
   * @return List<T>
   */
  @SuppressWarnings("unchecked")
public <T extends SCAPElementImpl> List<T> getSCAPElementList(String elementName, Class<T> itemClass)
  {
    return  (List<T>) getSCAPElementList(elementName, itemClass, element.getNamespace());
  }  
  
//  /**
//   * Gets a list of API (model) Elements that are children of this one SCAPElement. The model elements are
//   * not necessarily SCAPElements. The list passed as the first argument is there so the caller can create
//   * the list with the appropriate model api type, for example new ArrayList<Choice>() because the
//   * function cannot specify the appropriate api type. The same list passed, is also returned. The itemClass
//   * is the Class of the Api implementation class (normally a SCAPElement), not the Api interface. 
//   *
//   * @param list The already allocated List<ApiInterface> to be added to. Normally this list is empty
//   * @param elementName The name of the element
//   * @param itemClass The class of the item implementation; this class must implement T
//   * @param namespace The namespace of the element.
//   * 
//   * @return List<T>
//   */
//@SuppressWarnings("unchecked")
//public <T> List<T> getApiElementList(List<T> list, String elementName, Class<?> itemClass, Namespace namespace)
//{
//	  List<Element> childList = this.element.getChildren(elementName, namespace);
//	  for (Element elem : childList) {
//		  T t = null;
//		  try {
//			  t = (T) itemClass.newInstance();
//		  } catch (Exception e) {
//			  throw new IllegalArgumentException("Can't instantiate SCAPElement of type " + itemClass.getName(), e);
//		  }
//		  SCAPElement scapElement = (SCAPElement) t;
//		  scapElement.setElement(elem);
//		  scapElement.setRoot(root);
//		  scapElement.setDoc(doc);
//		  scapElement.setSCAPDocument(SCAPDocument);
//		  list.add(t);
//	  }
//	  return list;
//}  
  
  /**
   * Gets a list of SCAPElements that are children of this one.
   *
   * @param elementName The name of the element
   * @param itemClass The class of the item.
   * @param namespace The namespace of the element.
   * 
   * @return List<T>
   */
  public <T extends SCAPElementImpl> List<T> getSCAPElementList(String elementName, Class<T> itemClass, Namespace namespace)
  {
    List<T> itemList = new ArrayList<T>();
    List<Element> childList = this.element.getChildren(elementName, namespace);
    for (int i = 0; i < childList.size(); i++)
    {
      Element elem = (Element) childList.get(i);
      T t = null;
      try
      {
        t = itemClass.newInstance();
      } catch (Exception e)
      {
        throw new IllegalArgumentException("Can't instantiate SCAPElement of type " + itemClass.getName(), e);
      }
      SCAPElementImpl scapElement = (SCAPElementImpl) t;
      scapElement.setElement(elem);
      scapElement.setRoot(root);
      scapElement.setDoc(doc);
      scapElement.setSCAPDocument(SCAPDocument);
      itemList.add(t);
    }
    return itemList;
  }
  
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
  public <T extends SCAPElementImpl> T getSCAPElement(String elementName, Class<T> itemClass)
  {
	  List<T> itemList = getSCAPElementList(elementName, itemClass);
	  return (itemList.size() > 0 ? itemList.get(0) : null);
  }  
  
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
  public <T extends SCAPElementImpl> T getSCAPElement(String elementName, Class<T> itemClass, Namespace namespace)
  {
	  List<T> itemList = getSCAPElementList(elementName, itemClass, namespace);
	  return (itemList.size() > 0 ? itemList.get(0) : null);
  }    
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
  public <T> SCAPElement createSCAPElement(String tag, Class<T> clazz) {
	  Element elem = new Element(tag, this.element.getNamespace());
	  return createSCAPElement(elem, clazz);
  }
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
  public <T> SCAPElement createSCAPElement(Element elem, Class<T> clazz) {
		T t = null;
		try {
                    t = clazz.newInstance();
		} catch (Exception e) {
                    throw new IllegalArgumentException("Can't instantiate SCAPElement of type " + clazz.getName(),e);
		} 
		SCAPElementImpl scapElement = (SCAPElementImpl) t;
		scapElement.setElement(elem);
		scapElement.setRoot(root);
		scapElement.setDoc(doc);
		if (SCAPDocument == null) {
			throw new IllegalStateException("Null SCAPDocument, this element=" + element.getName() );
		}
		scapElement.setSCAPDocument(SCAPDocument);
		return scapElement;
  }
  
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
		  String elementName) {
	  setSCAPElement(scapElement, orderMap, elementName, this.element.getNamespace());
  }
  
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
		  Namespace namespace) {
		Element existingElement = this.element.getChild(elementName, namespace);
		if (existingElement != null) {
			this.element.removeContent(existingElement);
		}
		insertChild(scapElement, orderMap, -1);
  }

  /**
   * Gets a list of element values (Strings) from child elements of a specified name.
   *
   * @param elementName The name of the element.
   * 
   * @return List<String>
   * 
   * @see org.jdom.Element
   */
  public List<String> getStringList(String elementName)
  {
    List<String> itemList = new ArrayList<String>();
    List<Element> childList = this.element.getChildren(elementName, element.getNamespace());
    for (int i = 0; i < childList.size(); i++)
    {
      Element elem = (Element) childList.get(i);
      itemList.add(elem.getText());
    }
    return itemList;
  }
  
  /**
   * Gets the first element value (String) from child elements of a specified name.
   * 
   * @param elementName the name of the child element
   * @return the first child nodes text value, or null if no children of that name
   */
  public String getStringListSingleValue(String elementName) {
	  List<Element> childList = this.element.getChildren(elementName, element.getNamespace());
	  return (childList.size() > 0 ? childList.get(0).getText() : null);
  }

  /**
   * Sets an attribute in the underlying JDOM Element
   *
   * @param name The attribute name
   * @param value The attribute value
   * 
   * @see org.jdom.Element
   */
  public void setAttribute(String name, String value)
  {
    getElement().setAttribute(name, value);
  }

  /**
   * Gets an attribute in the underlying JDOM Element.
   *
   * @param name The attribute name

   * @return String
   * 
   * @see org.jdom.Element
   */
  public String getAttribute(String name)
  {
    return getElement().getAttributeValue(name);
  }

  /**
   *  Gets the name of the underlying JDOM Element.
   *
   * @return String
   * 
   * @see org.jdom.Element
   */
  public String getElementName()
  {
    return getElement().getName();
  }

  /**
   *  Sets the name of the underlying JDOM Element.
   *
   * @param elementName The name of the element
   *
   * @see org.jdom.Element
   */
  public void setElementName(String elementName)
  {
    if (elementName != null)
    {
      getElement().setName(elementName);
    }
  }
  
  /**
   * Returns the order map that insertChild uses to determine 
   * the proper order to insert elements. This method is intended
   * to be overriden by subclasses of this on.
   *   
   * @return HashMap<String, Integer>
   */
  public HashMap<String, Integer> getOrderMap()
  {
	  return null;
  }
  
  public void setOptionalAttribute(String attributeName, String attributeValue) {
	  if (attributeValue != null && attributeValue.trim().length() != 0) {
		  element.setAttribute(attributeName, attributeValue);
	  } else {
		  element.removeAttribute(attributeName);
	  }
  }
  
  public boolean getBoolean(String attributeName, boolean defaultValue)
  {
      boolean result = defaultValue;
      String stringValue = element.getAttributeValue(attributeName);
      if (stringValue != null) {
    	  if (stringValue.equals("1") || stringValue.equalsIgnoreCase("true")) {
    		  result = true;
    	  } else if (stringValue.equals("0") || stringValue.equalsIgnoreCase("false")) { 
    		  result = false;
    	  }
      }
      return result;
  }
  
  public void setBoolean(String attributeName, Boolean attributeValue) {
	  if (attributeValue != null) {
		  setBoolean(attributeName, attributeValue.booleanValue());
	  } else {
		  element.removeAttribute(attributeName);
	  }
  }
  
  public Boolean getBoolean(String attributeName)
  {
      Boolean result = null;
      String stringValue = element.getAttributeValue(attributeName);
      if (stringValue != null) {
    	  if (stringValue.equals("1") || stringValue.equalsIgnoreCase("true")) {
    		  result = Boolean.TRUE;
    	  } else if (stringValue.equals("0") || stringValue.equalsIgnoreCase("false")) { 
    		  result = Boolean.FALSE;
    	  }
      }
      return result;
  }
    
  public void setBoolean(String attributeName, boolean attributeValue) {
	  element.setAttribute(attributeName, (attributeValue ? "true" : "false") );
  }
  
  public BigDecimal getBigDecimalAttribute(String attributeName) throws NumberFormatException {
	  BigDecimal decimal = null;
	  String decimalString = element.getAttributeValue(attributeName);
	  if (decimalString != null && decimalString.length() > 0) {
		  try {
			  decimal = new BigDecimal(decimalString);
		  } catch (NumberFormatException e) {
			  System.out.println("Attribute " + attributeName + " not in valid decimal format: " + decimalString);
			  throw e;
		  }
	  }
	  return decimal;
  }
  
  public void setBigDecimalAttribute(String attributeName, BigDecimal decimal) {
	  String decimalString = null;
	  if (decimal != null) {
		  decimalString = decimal.stripTrailingZeros().toPlainString();
		  element.setAttribute(attributeName, decimalString);
	  } else {
		  element.removeAttribute(attributeName);
	  }
  }
  
  public void insertStringValueChild(String elementName, 
		  String elementValue, 
		  HashMap<String, Integer> orderMap) {
	  insertStringValueChild(elementName, elementValue, orderMap, element.getNamespace());
  }  
  
  public void insertStringValueChild(String elementName, 
		  String elementValue, 
		  HashMap<String, Integer> orderMap, 
		  Namespace namespace) {
	  Element childElement = new Element(elementName, namespace);
	  insertChild(childElement, orderMap);
	  childElement.setText(elementValue);
  }  
  
  /**
   * Gets a list of SCAPElements that are children of this one.
   *
   * @param elementName The name of the element
   * @param implClass The class of the items Interface (eg, Profile.class, not ProfileImpl.class)
   * @param namespace The namespace of the element.
   * 
   * @return List<T>
   */
  @Override
  public <T extends SCAPElement> List<T> getSCAPElementIntList(String elementName, Class<T> interfaceClass, Namespace namespace) {
	  List<T> itemList = new ArrayList<T>();
	  @SuppressWarnings("unchecked")
	  List<Element> childList = this.element.getChildren(elementName, namespace);
	  for (Element elem : childList) {
		  Class<? extends SCAPElementImpl> implClass = getSCAPDocument().getImplClass(interfaceClass);
		  T t = null;
		  try {
			  t = (T) implClass.newInstance();
		  } catch (Exception e) {
			  throw new IllegalArgumentException("Can't instantiate SCAPElement of type " + implClass.getName() + " implementing " + interfaceClass, e);
		  }
		  SCAPElementImpl scapElement = (SCAPElementImpl) t;
		  scapElement.setElement(elem);
		  scapElement.setRoot(root);
		  scapElement.setDoc(doc);
		  scapElement.setSCAPDocument(SCAPDocument);
		  itemList.add(t);
	  }
	  return itemList;
  }
  
  /**
   * Gets a list of SCAPElements that are children of this one, and have the same namespace as this Element.
   *
   * @param elementName
   * @param itemClass
   * @return List<T>
   */
  @SuppressWarnings("unchecked")
  @Override
  public <T extends SCAPElement> List<T> getSCAPElementIntList(String elementName, Class<T> itemClass)
  {
	  return  (List<T>) getSCAPElementIntList(elementName, itemClass, element.getNamespace());
  }  
  
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
  public <T extends SCAPElement> T getSCAPElementInt(String elementName, Class<T> itemClass) {
	  List<T> itemList = getSCAPElementIntList(elementName, itemClass);
	  return (itemList.size() > 0 ? itemList.get(0) : null);
  }
  
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
  public <T extends SCAPElement> T getSCAPElementInt(String elementName, Class<T> itemClass, Namespace namespace) {
	  List<T> itemList = getSCAPElementIntList(elementName, itemClass, namespace);
	  return (itemList.size() > 0 ? itemList.get(0) : null);
  }
  
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
  @Override
  public <T extends SCAPElement> SCAPElement createSCAPElementInt(String tag, Class<T> clazz) {
	  Element elem = new Element(tag, this.element.getNamespace());
	  return createSCAPElementInt(elem, clazz);
  }
  
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
@Override
  public <T extends SCAPElement> SCAPElement createSCAPElementInt(Element elem, Class<T> interfaceClass) {
	  T t = null;
	  Class<? extends SCAPElementImpl> implClass = getSCAPDocument().getImplClass(interfaceClass);
      if (implClass == null) {
          throw new IllegalArgumentException("Can't find implementation class for interface " + interfaceClass.getName() + ", element " + elem.getClass().getName());
      }
	  try {
		  t = (T) implClass.newInstance();
	  } catch (Exception e) {
		  throw new IllegalArgumentException("Can't instantiate SCAPElement of type " + implClass.getName() + " implementing " + interfaceClass, e);
	  } 
	  SCAPElementImpl scapElement = (SCAPElementImpl) t;
	  scapElement.setElement(elem);
	  scapElement.setRoot(root);
	  scapElement.setDoc(doc);
	  scapElement.setSCAPDocument(SCAPDocument);
	  return scapElement;
  }
	public void setOptionalAttribute(String attributeName, Object attributeValue) {
		if (attributeValue == null) {
			element.removeAttribute(attributeName);
		} else {
			element.setAttribute(attributeName, attributeValue.toString());
		}
	}
}
