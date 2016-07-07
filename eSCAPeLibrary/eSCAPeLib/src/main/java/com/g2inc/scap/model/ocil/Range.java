package com.g2inc.scap.model.ocil;


public interface Range extends ModelBase {
	
	public RangeValue getMin();	
	public void setMin(RangeValue min);

	public RangeValue getMax();
	public void setMax(RangeValue max);
	
}
