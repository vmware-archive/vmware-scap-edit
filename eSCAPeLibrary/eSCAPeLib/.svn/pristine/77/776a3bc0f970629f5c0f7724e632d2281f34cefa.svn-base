package com.g2inc.scap.library.domain.ocil;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.jdom.Element;
import org.jdom.Namespace;

import com.g2inc.scap.library.domain.SCAPElement;
import com.g2inc.scap.library.domain.SCAPElementImpl;
import com.g2inc.scap.model.ocil.Identified;
import com.g2inc.scap.model.ocil.ModelBase;
import com.g2inc.scap.model.ocil.OcilDocument;

public class ModelBaseImpl extends SCAPElementImpl implements ModelBase {
	
	private static Logger LOG = Logger.getLogger(ModelBaseImpl.class);

	protected OcilDocument ocilDocument;
	
	public ModelBaseImpl() {
		super();
	}
	
	public ModelBaseImpl(OcilDocument ocilDoc) {
		super();
		this.ocilDocument = ocilDoc;
	}

	@Override
	public OcilDocument getOcilDocument() {
		return ocilDocument;
	}

	@Override
	public void setOcilDocument(OcilDocument ocilDoc) {
		this.ocilDocument = ocilDoc;		
	}

	/**
	 * Gets a list of API (model) Elements that are children of this one, and have the same namespace as this Element.
	 *
	 * @param list The already allocated List<ApiInterface> to be added to. Normally this list is empty
	 * @param elementName The name of the element
	 * @param itemClass The class of the item implementation; this class must implement T
	 * 
	 * @return List<T> the same list passed as an input parameter, but now populated
	 */
	public <T> List<T> getApiElementList(List<T> list, String elementName, Class<?> itemClass)
	{
		return getApiElementList(list, elementName, itemClass, element.getNamespace());
	}

	/**
	 * Gets a list of API (model) Elements that are children of this one SCAPElement. The model elements are
	 * SCAPElements, but a version of this method could be provided that does not depend on them being 
	 * SCAPElements. The list passed as the first argument is there so the caller can create
	 * the list with the appropriate model api type, for example new ArrayList<Choice>() because the
	 * function cannot specify the appropriate api type. The same list passed, is also returned. The itemClass
	 * is the Class of the Api implementation class (normally a SCAPElement), not the Api interface. 
	 *
	 * @param list The already allocated List<ApiInterface> to be added to. Normally this list is empty
	 * @param elementName The name of the element
	 * @param itemClass The class of the item implementation; this class must implement T
	 * @param namespace The namespace of the element.
	 * 
	 * @return List<T> the same list passed as an input parameter, but now populated
	 */
	@SuppressWarnings("unchecked")
	public <T> List<T> getApiElementList(List<T> list, String elementName, Class<?> itemClass, Namespace namespace)
	{
		List<Element> childList = this.element.getChildren(elementName, namespace);
		for (Element elem : childList) {
			T t = null;
			try {
				t = (T) itemClass.newInstance();
			} catch (Exception e) {
				throw new IllegalArgumentException("Can't instantiate Api Element of type " + itemClass.getName(), e);
			}
			SCAPElementImpl scapElement = (SCAPElementImpl) t;
			scapElement.setElement(elem);
			scapElement.setRoot(root);
			scapElement.setDoc(doc);
			scapElement.setSCAPDocument(SCAPDocument);
			((ModelBase) t).setOcilDocument(this.getOcilDocument());
			list.add(t);
		}
		return list;
	}  	  

	/**
	 * Replaces a list of Api elements under this one in the proper location according the orderMap.
	 * Some schemas insist on elements being in a certain order, so this method takes care of
	 * inserting in the correct location.
	 *
	 * @param newElementList The Api elements to be inserted
	 * @param orderMap The map that tells us what order the element needs to be in
	 * @param elementName
	 */
	public void replaceApiList(List<? extends ModelBase> newElementList, HashMap<String, Integer> orderMap, String elementName)
	{
		replaceApiList(newElementList, orderMap, elementName, element.getNamespace());
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
	  public void replaceApiList(List<?> newElementList, HashMap<String, Integer> orderMap, String elementName, Namespace namespace)
	  {
		  if (elementName != null) 
		  {
			  element.removeChildren(elementName, namespace);
		  }   
	      for (int i = 0; i < newElementList.size(); i++)
	      {
	    	  SCAPElementImpl scapElement = (SCAPElementImpl) newElementList.get(i);
	    	  insertApiChild((ModelBase) scapElement, orderMap, -1);
	      }
	  }
	  
	  public void addApiElement(ModelBase apiChild, HashMap<String, Integer> orderMap) {
		  insertApiChild(apiChild, orderMap, -1);
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
	  public void insertApiChild(ModelBase newElement, HashMap<String, Integer> orderMap, int indexInSameTagList)
	  {		  
		  insertChild((SCAPElementImpl)newElement, orderMap, indexInSameTagList);
		  setChanged(newElement);
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
	  public <T> T getApiElement(String elementName, Class<?> itemClass)
	  {
		  List<T> itemList = getApiElementList(new ArrayList<T>(), elementName, itemClass);
		  return (itemList.size() > 0 ? itemList.get(0) : null);
	  }  	  
	  
	  /**
	   * Sets a new api element child of this element, replacing any existing child, using the underlying
	   * SCAPElement method setSCAPElement.  Use this method only when maxOccurs of the child element is 1.
	   * If maxOccurs is > 1 or unbounded, use getApiElementList.
	   * 
	   * @param scapElement  new SCAPElement to be set
	   * @param orderMap   The map that tells us what order the element needs to be in
	   * @param elementName  name of the child element 
	   */
	  public void setApiElement(ModelBase newApiElement, 
			  HashMap<String, Integer> orderMap, 
			  String elementName) {
		  setSCAPElement((SCAPElementImpl) newApiElement, orderMap, elementName, this.element.getNamespace());
	  }
	  
	  public <T extends ModelBase> T createApiElement(String tag, Class<?> clazz) {
		  SCAPElement scapElement = createSCAPElement(tag, clazz);
		  @SuppressWarnings("unchecked")
		T modelBase = (T) scapElement;
		  modelBase.setOcilDocument(ocilDocument);
		  return modelBase;
	  }	  
	  
	  public <T extends ModelBase> T createApiElement(Element element, Class<?> clazz) {
		  SCAPElement scapElement = createSCAPElement(element, clazz);
		  @SuppressWarnings("unchecked")
		T modelBase = (T) scapElement;
		  modelBase.setOcilDocument(ocilDocument);
		  return modelBase;
	  }	  	  
	  
	  public void removeApiElement(ModelBase apiElement) {
		  Element xmlElement = ((ModelBaseImpl) apiElement).getElement();
		  if (xmlElement == null) {
			  LOG.warn("removeApiElement called for element with no xml Element");
		  } else if (!this.element.removeContent(xmlElement)) {
			  LOG.warn("removeApiElement called for xml Element not child of this element");
		  }
		  setChanged(apiElement);
	  }
	  
	  public void removeApiElementFromList(List<? extends ModelBase> list, ModelBase apiElement) {
		  if (list.contains(apiElement)) {
			  list.remove(apiElement);
			  removeApiElement(apiElement);
		  } else {
			  LOG.warn("removeApiElementFromList called for element not in list");
		  }
	  }
	  
	  public BigDecimal getBigDecimal(String decimalString, String name) {
		  BigDecimal decimal = null;
		  if (decimalString != null && decimalString.length() > 0) {
			  try {
				  decimal = new BigDecimal(decimalString);
			  } catch (NumberFormatException e) {
				  System.out.println(name + " not in valid decimal format: " + decimalString);
				  throw e;
			  }
		  }
		  return decimal;
	  }
	  
	  @Override
	  public void setId(String id) {
		  if (id == null && getId() == null) {
			  return;
		  } else if (id == null && getId() != null || id != null && getId() == null) {
			  LOG.debug("Setting id from " + (getId() == null ? "null" : getId() ) 
					  + " to " + (id == null ? "null" : id));
			  super.setId(id);
			  ((OcilDocumentImpl) ocilDocument).setChanged(true);
		  } else if (!id.equals(getId())) {
			  LOG.debug("Setting id from " + getId() + " to " + id);
			  super.setId(id);
			  ((OcilDocumentImpl) ocilDocument).setChanged(true);
		  }
	  }
	  
	  protected void setChanged(ModelBase apiElement) {
		  if (apiElement instanceof Identified) {
			  ((OcilDocumentImpl) ocilDocument).setChanged(true);
		  }
	  }

}
