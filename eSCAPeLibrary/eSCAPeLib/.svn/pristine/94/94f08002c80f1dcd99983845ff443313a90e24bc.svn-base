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

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;

import com.g2inc.scap.library.domain.oval.AvailableObjectBehavior;
import com.g2inc.scap.library.domain.oval.OvalEntity;
import com.g2inc.scap.library.domain.oval.TypeEnum;
import com.g2inc.scap.library.schema.NameDoc;
import com.g2inc.scap.library.schema.PlatformNameKey;

/**
 * This class parses existing schema and gathers information that can be used to get
 * schema specific lists of allowed values and types.
 *
 * @author gstrickland
 */
public class SchemaDOMParser
{
  private static final Logger LOG = Logger.getLogger(SchemaDOMParser.class.getName());
  private ArrayList<NameDoc> validTestsForPlatform = new ArrayList<NameDoc>();
  private ArrayList<NameDoc> validObjectsForPlatform = new ArrayList<NameDoc>();
  private ArrayList<NameDoc> validStatesForPlatform = new ArrayList<NameDoc>();
  private HashMap<String, List<OvalEntity>> validObjectEntitiesList = new HashMap<String, List<OvalEntity>>();
  private HashMap<String, List<OvalEntity>> validStateEntitiesList = new HashMap<String, List<OvalEntity>>();
  private HashMap<String, List<NameDoc>> enumerationValueMap = new HashMap<String, List<NameDoc>>();
  private HashMap<String, NameDoc> enumerationNameMap = new HashMap<String, NameDoc>();
  private String platform;
  private String lastObject;
  private HashMap<String, String> behaviorsObjectIdMap = new HashMap<String, String>();
  private HashMap<PlatformNameKey, List<AvailableObjectBehavior>> behaviorsValuesMap = new HashMap<PlatformNameKey, List<AvailableObjectBehavior>>();
  //	private HashMap<PlatformNameKey, NameDoc> behaviorNameMap = new HashMap<PlatformNameKey, NameDoc>();
  public static final Namespace XSD_NAMESPACE = Namespace.getNamespace("xsd", "http://www.w3.org/2001/XMLSchema");
  
  /**
   * Parse a particular schema file
   *
   * @param schema
   */
  public void parseSchema(InputStream schema)
  {
    SAXBuilder builder = new SAXBuilder();
    Document doc = null;
    try
    {
      doc = builder.build(schema);
    } catch (IOException e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (JDOMException e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    HashSet<NameDoc> testsForPlatform = new HashSet<NameDoc>();
    HashSet<NameDoc> objectsForPlatform = new HashSet<NameDoc>();
    HashSet<NameDoc> statesForPlatform = new HashSet<NameDoc>();
    Element root = doc.getRootElement();
    List<Element> elementNodes = root.getChildren("element", XSD_NAMESPACE);
    for (int i = 0; i < elementNodes.size(); i++)
    {
      Element element = elementNodes.get(i);
      String name = element.getAttributeValue("name");
      if (name == null)
      {
        continue;
      }
      if (name.endsWith("_test"))
      {
        NameDoc entry = new NameDoc(name, getDocumentation(element));
        testsForPlatform.add(entry);
      } else if (name.endsWith("_object"))
      {
        lastObject = name;
        NameDoc entry = new NameDoc(name, getDocumentation(element));
        objectsForPlatform.add(entry);
        ArrayList<OvalEntity> entityList = new ArrayList<OvalEntity>();
        processEntities(element, entityList, "EntityObject");
//				ArrayList<OvalEntity> objectList = new ArrayList<OvalEntity>(entitySet);
//				Collections.sort(objectList);
        validObjectEntitiesList.put(name, entityList);
      } else if (name.endsWith("_state"))
      {
        NameDoc entry = new NameDoc(name, getDocumentation(element));
        statesForPlatform.add(entry);
        ArrayList<OvalEntity> entityList = new ArrayList<OvalEntity>();
        processEntities(element, entityList, "EntityState");
//				ArrayList<OvalEntity> stateList = new ArrayList<OvalEntity>(entitySet);
//				Collections.sort(stateList);
        validStateEntitiesList.put(name, entityList);
      }
    }
    validTestsForPlatform = new ArrayList<NameDoc>(testsForPlatform);
    Collections.sort(validTestsForPlatform);
    validObjectsForPlatform = new ArrayList<NameDoc>(objectsForPlatform);
    Collections.sort(validObjectsForPlatform);
    validStatesForPlatform = new ArrayList<NameDoc>(statesForPlatform);
    Collections.sort(validStatesForPlatform);

    List<Element> simpleTypeNodes = root.getChildren("simpleType", XSD_NAMESPACE);
    for (int i = 0; i < simpleTypeNodes.size(); i++)
    {
      Element stElement = (Element) simpleTypeNodes.get(i);
      String enumName = stElement.getAttributeValue("name");
      if (enumName.endsWith("Enumeration"))
      {
        Element restrictionElement = stElement.getChild("restriction", XSD_NAMESPACE);
        if (restrictionElement == null)
        {
          continue;
        }
        List<Element> enumElements = restrictionElement.getChildren("enumeration", XSD_NAMESPACE);
        if (enumElements == null || enumElements.size() == 0)
        {
          continue;
        }
        
                
        NameDoc entry = new NameDoc(enumName, getDocumentation(stElement));
        enumerationNameMap.put(enumName, entry);
        List<NameDoc> enumValueList = getEnumerationValues(enumElements);
        enumerationValueMap.put(enumName, enumValueList);
      }
    }

    List<Element> complexTypeNodes = root.getChildren("complexType", XSD_NAMESPACE);
    for (int i = 0; i < complexTypeNodes.size(); i++)
    {
      Element complexElement = (Element) complexTypeNodes.get(i);
      String enumName = complexElement.getAttributeValue("name");
      if (enumName.endsWith("Type"))
      {
        NameDoc entry = new NameDoc(enumName, getDocumentation(complexElement));
        enumerationNameMap.put(enumName, entry);
        List<NameDoc> enumValueList = new ArrayList<NameDoc>();
        enumerationValueMap.put(enumName, enumValueList);
        Element simpleContentElement = complexElement.getChild("simpleContent", XSD_NAMESPACE);
        if (simpleContentElement == null)
        {
          continue;
        }
        Element restrictionElement = simpleContentElement.getChild("restriction", XSD_NAMESPACE);
        if (restrictionElement == null)
        {
          continue;
        }
        List<Element> enumElements = restrictionElement.getChildren("enumeration", XSD_NAMESPACE);
        if (enumElements == null || enumElements.size() == 0)
        {
          continue;
        }
        enumValueList = getEnumerationValues(enumElements);
        enumerationValueMap.put(enumName, enumValueList);
      } else if (enumName.endsWith("Behaviors"))
      {
        String objectType = behaviorsObjectIdMap.get(enumName);
        List<AvailableObjectBehavior> behaviorValues = new ArrayList<AvailableObjectBehavior>();
        PlatformNameKey key = new PlatformNameKey(platform, objectType);
        behaviorsValuesMap.put(key, behaviorValues);
        List<Element> behaviorAtts = complexElement.getChildren("attribute", XSD_NAMESPACE);
        for (int iAtt = 0; iAtt < behaviorAtts.size(); iAtt++)
        {
          Element behaviorAtt = behaviorAtts.get(iAtt);
          String behaviorName = behaviorAtt.getAttributeValue("name");
          String behaviorDatatype = behaviorAtt.getAttributeValue("type");
          String behaviorDefault = behaviorAtt.getAttributeValue("default");
          AvailableObjectBehavior behavior = new AvailableObjectBehavior(behaviorName, getDocumentation(behaviorAtt));
          behaviorValues.add(behavior);
          if (behaviorDatatype != null && behaviorDatatype.startsWith("xsd:"))
          {
            behavior.setDatatype(TypeEnum.valueOf(behaviorDatatype.substring("xsd:".length()).toUpperCase()));
          } else
          {
            behavior.setDatatype(TypeEnum.STRING);
          }
          if (behaviorDefault != null)
          {
            behavior.setDefaultValue(behaviorDefault);
          }
          Element simpleType = behaviorAtt.getChild("simpleType", XSD_NAMESPACE);
          if (simpleType != null)
          {
            Element restriction = simpleType.getChild("restriction", XSD_NAMESPACE);
            String restrictionBase = restriction.getAttributeValue("base");
            if (restrictionBase.endsWith("integer"))
            {
              behavior.setDatatype(TypeEnum.INT);
            } else if (restrictionBase.endsWith("boolean"))
            {
              behavior.setDatatype(TypeEnum.BOOLEAN);
            }
            List<Element> restrictions = restriction.getChildren("enumeration", XSD_NAMESPACE);
            if (restrictions.size() > 0)
            {
              ArrayList<String> enums = new ArrayList<String>();
              behavior.setValuesAllowed(enums);
              for (int iEnum = 0; iEnum < restrictions.size(); iEnum++)
              {
                String value = restrictions.get(iEnum).getAttributeValue("value");
                enums.add(value);
              }
            }
          }
        }
      }
    }

  }

  /*
   * Get map of possible enumeration names
   *
   * key is enum name (eg, "DatatypeEnumeration"), value is NameDoc providing Documentation of that enum name
   *
   * @return Map<String, NameDoc>
   */
  public Map<String, NameDoc> getEnumerationNameMap()
  {
    return enumerationNameMap;
  }

  /*
   * Get the map of possible enumeration values
   *
   * key is enum name (eg, "DatatypeEnumeration"), value is a List of NameDocs, each of which provides
   * the name and Documentation for one of the enums possible values (eg, "evr_string", "float", etc)
   *
   * @return Map<String, List<NameDoc>>
   */
  public Map<String, List<NameDoc>> getEnumerationValueMap()
  {
    return enumerationValueMap;
  }

  /**
   * Get a list of possible enumeration values
   *
   * @param enumElements
   * @return List<NameDoc>
   */
  private List<NameDoc> getEnumerationValues(List<Element> enumElements)
  {
    HashSet<NameDoc> enumValueSet = new HashSet<NameDoc>();
    for (int iEnum = 0; iEnum < enumElements.size(); iEnum++)
    {
      Element enumElement = (Element) enumElements.get(iEnum);
      String enumValueName = enumElement.getAttributeValue("value");
      NameDoc enumDoc = new NameDoc(enumValueName, getDocumentation(enumElement));
      enumValueSet.add(enumDoc);
    }
    ArrayList<NameDoc> enumValueList = new ArrayList<NameDoc>(enumValueSet);
    Collections.sort(enumValueList);
    return enumValueList;
  }

  private void processEntities(Element element, List<OvalEntity> list, String entityOrStateType)
  {
    List<Element> children = element.getChildren();
    for (int i = 0; i < children.size(); i++)
    {
      Element child = children.get(i);
      if (child.getName().equals("element"))
      {
        String type = child.getAttributeValue("type");
        String entityName = child.getAttributeValue("name");
        if (type != null && type.indexOf(entityOrStateType) != -1)
        {
          String entityDoc = getDocumentation(child);
          OvalEntity entity = new OvalEntity(entityName, entityDoc);
          // start special case logic for windows file_state
          if (platform.equals("windows") && entityName.equals("version"))
          {
            type = "version";
          }
          // end special case logic
          entity.setDatatypeString(type);
          String minOccurs = child.getAttributeValue("minOccurs");
          if (minOccurs != null && !minOccurs.equals("0"))
          {
            entity.setRequired(true);
          }
          String nillable = child.getAttributeValue("nillable");
          if (nillable != null && !nillable.equals("false"))
          {
            entity.setNillable(true);
          }
          list.add(entity);
        } else if (entityName != null && entityName.equals("behaviors"))
        {
          String behaviorId = type.substring(type.indexOf(":") + 1);
          behaviorsObjectIdMap.put(behaviorId, lastObject);
        }
      }
      processEntities(child, list, entityOrStateType);
    }
  }

  private String getDocumentation(Element element)
  {
    StringBuilder documentation = new StringBuilder();
    Element annotation = element.getChild("annotation", XSD_NAMESPACE);
    if (annotation != null)
    {
      Iterator<Element> documentationNodes = annotation.getChildren("documentation", XSD_NAMESPACE).iterator();
      while (documentationNodes.hasNext())
      {
        documentation.append(documentationNodes.next().getValue());
      }
    }
    return documentation.toString();
  }


  /**
   * Return a list of valid test names
   *
   * @return List<NameDoc>
   */
  public List<NameDoc> getValidTestsForPlatform()
  {
    return validTestsForPlatform;
  }

  /**
   * Return a list of valid object names
   *
   * @return List<NameDoc>
   */
  public List<NameDoc> getValidObjectsForPlatform()
  {
    return validObjectsForPlatform;
  }

  /**
   * Return a list of valid state names
   *
   * @return List<NameDoc>
   */
  public List<NameDoc> getValidStatesForPlatform()
  {
    return validStatesForPlatform;
  }

  /**
   * Return a list of valid object entity names
   *
   * @return Map<String, List<OvalEntity>>
   */
  public Map<String, List<OvalEntity>> getValidObjectEntitiesByObject()
  {
    return validObjectEntitiesList;
  }

  /**
   * Return a list of valid state entity names
   *
   * @return Map<String, List<OvalEntity>>
   */
  public Map<String, List<OvalEntity>> getValidStateEntitiesByState()
  {
    return validStateEntitiesList;
  }

  /**
   *  Set the platform to be used
   *
   * @param platform
   */
  public void setPlatform(String platform)
  {
    this.platform = platform;
  }

  /**
   * Return a list of possible behaviors values
   * 
   * @return Map<PlatformNameKey, List<AvailableObjectBehavior>>
   */
  public Map<PlatformNameKey, List<AvailableObjectBehavior>> getBehaviorsValuesMap()
  {
    return behaviorsValuesMap;
  }
}
