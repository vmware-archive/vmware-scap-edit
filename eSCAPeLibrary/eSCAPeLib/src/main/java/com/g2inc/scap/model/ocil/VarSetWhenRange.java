package com.g2inc.scap.model.ocil;

import java.math.BigDecimal;

public interface VarSetWhenRange extends VarSetWhenCondition {
	
	public BigDecimal getMin();
	public void setMin(BigDecimal min);
	
	public BigDecimal getMax();
	public void setMax(BigDecimal max);
	
}
