package com.g2inc.scap.library.domain.oval;

import java.util.List;

import org.jdom.Element;

/**
 * Class represents a note element.
 *
 * @author jcockerill
 * 
 * @see com.g2inc.scap.library.domain.oval.OvalNotes
 */
public abstract class OvalNote extends OvalElement
{
    public OvalNote(OvalDefinitionsDocument parentDocument) {
        super(parentDocument);
    }

    /**
     * Get the value of this note.
     *
     * @return String
     */
    public String getValue() {
        String value = getElement().getValue();
        return value;
    }

    /**
     * Set the value of this note.
     *
     * @param value The value of the note.
     */
    public void setValue(String value) {
        getElement().setText(value);
    }


    /**
     * Remove this note from it's parent.
     * 
     */
    public void removeFromParent()
    {
        if(getElement() != null)
        {
            Element parent = getElement().getParentElement();

            if(parent != null)
            {
                parent.removeContent(getElement());
            }
        }
    }
    
    /**
     * This method determines if this object is a duplicate of another object.
     * 
     * @param other
     * @return boolean
     */
    @Override
	public boolean isDuplicateOf(Object other)
	{
    	// Call the ancestor isDuplicateOf method.
    	if (!(super.isDuplicateOf(other)))
    	{
    		return false;
    	}
		
		// If the other object is not an instance of this class,
		// then return false.
		if (!(other instanceof OvalNote))
		{
			return false;
		}
		
		// Cast the other object to this class.
		OvalNote other2 = (OvalNote) other;
		
		// If the other object's value does not match,
		// then return false.
    	if (!(areStringsEqualOrBothNull(other2.getValue(), this.getValue())))
    	{
    		return false;
    	}
		
		// Return true if we get to this point.
		return true;
	}
}
