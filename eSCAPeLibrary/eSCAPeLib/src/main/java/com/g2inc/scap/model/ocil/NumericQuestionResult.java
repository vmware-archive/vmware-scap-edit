package com.g2inc.scap.model.ocil;

import java.math.BigDecimal;

public interface NumericQuestionResult extends QuestionResult {

	public BigDecimal getAnswer();
	public void setAnswer(BigDecimal answer);
	
}
