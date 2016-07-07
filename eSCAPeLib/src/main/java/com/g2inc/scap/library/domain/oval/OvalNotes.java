package com.g2inc.scap.library.domain.oval;

import java.util.ArrayList;
import java.util.List;
import org.jdom.Element;

/**
 * Class represents a notes element. This element contains individual note elements.
 *
 * @author jcockerill
 * 
 * @see com.g2inc.scap.library.domain.oval.OvalNote
 */
public abstract class OvalNotes extends OvalElement
{
	public OvalNotes(OvalDefinitionsDocument parentDocument)
	{
		super(parentDocument);
	}

    /**
     * Returns a list of notes contained by this notes element.
     *
     * @return List<OvalNote>
     */
	public List<OvalNote> getNotes()
	{
		List<OvalNote> notes = new ArrayList<OvalNote>();
		List children = getElement().getChildren();
		for(int x = 0; x < children.size(); x++)
		{
			Element child = (Element) children.get(x);
			OvalNote note = null;
			if(child.getName().toLowerCase().indexOf("note") > -1)
			{
				note = getParentDocument().getOvalNoteWrapper();
			}
			if(note != null)
			{
				note.setElement(child);
				note.setRoot(getRoot());
				note.setValue(child.getText());
				notes.add(note);
			}
		}
		return notes;
	}
	
    /**
     * Sets the notes contained by this notes element.
     * Existing notes are removed.
     *
     * @param List<OvalNote> The list of notes to set.
     */
	public void setNotes(List<OvalNote> notes)
	{
		getElement().removeContent();
		addNotes(notes);
	}

    /**
     * Add a note to this element.
     *
     * @param OvalNote The note to add.
     */
    public void addNote(OvalNote note)
	{
        List<OvalNote> notes = new ArrayList<OvalNote>();
        notes.add(note);
        addNotes(notes);
	}

    /**
     * Adds notes to this element.
     *
     * @param List<OvalNote> The list of notes to add.
     */
    public void addNotes(List<OvalNote> notes)
	{
		if(notes != null && notes.size() > 0)
		{
            for(int x = 0; x < notes.size(); x++)
			{
            	OvalNote note = notes.get(x);
                if(note.getElement().getParentElement() != null)
                {
                	note.getElement().detach();
                }
                getElement().addContent(note.getElement());
			}
		}
	}
    
    /**
     * Creates a note.
     * 
     * @param OvalDefinitionsDocument The parent document.
     * 
     * @return OvalNote
     */
	public OvalNote createNote(OvalDefinitionsDocument parentDoc)
	{
		OvalNote note = parentDoc.getOvalNoteWrapper();
		Element e = new Element("note", getElement().getNamespace());		
		note.setElement(e);
		note.setRoot(parentDoc.getElement());		
		return note;
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
		if (!(other instanceof OvalNotes))
		{
			return false;
		}
		
		// Cast the other object to this class.
		OvalNotes other2 = (OvalNotes) other;
		
		// Compare the individual note elements against each other.
    	List<OvalNote> myNotes = this.getNotes();
    	List<OvalNote> otherNotes = other2.getNotes();
    	if (myNotes.size() != otherNotes.size())
    	{
    		return false;
    	}
    	for (int i = 0; i < myNotes.size(); i++)
    	{
    		OvalNote myNote = myNotes.get(i);
    		OvalNote otherNote = otherNotes.get(i);
    		if (!(myNote.isDuplicateOf(otherNote)))
    		{
    			return false;
    		}
    	}
		
		// Return true if we get to this point.
		return true;
	}
}