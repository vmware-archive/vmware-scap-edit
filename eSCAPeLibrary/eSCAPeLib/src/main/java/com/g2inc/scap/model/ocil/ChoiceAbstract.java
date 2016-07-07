package com.g2inc.scap.model.ocil;
/**
 * This marker interface is implemented by Choice and ChoiceGroup. It is used to differentiate 
 * the "choice" and "choice_group_ref" elements which can be in a ChoiceQuestion. Note that even 
 * though the ChoiceGroupRef SCAPElement exists, it does not implement this interface. When the 
 * api user adds a ChoiceGroup to a ChoiceQuestion he/she does not need to instantiate the ChoiceGroupRef
 * explicitly; adding the ChoiceGroup will build one internally.
 * 
 * @author glenn.strickland
 *
 */

public interface ChoiceAbstract extends ModelBase {

}
