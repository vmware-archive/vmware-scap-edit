/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.g2inc.scap.library.domain.xccdf;

import java.util.List;

/**
 *
 * @author glenn.strickland
 */
public interface MetadataAndDcStatusContainer {
    
    public List<DcStatus> getDcStatusList();
    public void setDcStatusList(List<DcStatus> dcStatusList);
    
    public List<Metadata> getMetadataList();
    public void setMetadataList(List<Metadata> metadataList);
}
