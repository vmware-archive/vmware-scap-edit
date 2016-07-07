package com.g2inc.scap.library.domain.xccdf;

/**
 * Fields common to both Fix and FixText. Useful for editor
 * @author glenn.strickland
 */
public interface FixCommon {

    public boolean getReboot();

    public void setReboot(boolean reboot);

    public StrategyEnum getStrategy();

    public void setStrategy(StrategyEnum strategy);

    public RatingEnum getDisruption();

    public void setDisruption(RatingEnum disruption);

    public RatingEnum getComplexity();

    public void setComplexity(RatingEnum complexity);
}
