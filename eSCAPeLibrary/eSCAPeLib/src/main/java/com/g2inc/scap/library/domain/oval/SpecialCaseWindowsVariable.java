package com.g2inc.scap.library.domain.oval;

public class SpecialCaseWindowsVariable {
	/**
	 * Represents a windows environment variable whose value is resolvable in the
	 * registry
	 */
	// the hive
	private String hive = "";

	// the name of the environment variable including the wrapping %'s
	private String envVarName = "";

	// the key in the registry containing the value
	private String keyName = "";

	// the name of the value representing this environment variable
	private String valueName = "";

	// description of what this variable is
	private String description = "";

	public SpecialCaseWindowsVariable(String hive, String envVarName, String keyName,
			String valueName, String desc) {
		this.hive = hive;
		this.envVarName = envVarName;
		this.keyName = keyName;
		this.valueName = valueName;
		this.description = desc;
	}

	@Override
	public boolean equals(Object o) {
		boolean ret = false;
		if (o != null) {
			if (o instanceof SpecialCaseWindowsVariable) {
				SpecialCaseWindowsVariable other = (SpecialCaseWindowsVariable) o;

				return envVarName.equals(other.envVarName);
			}
		}
		return ret;
	}

	@Override
	public int hashCode() {
		int hash = 3;
		hash = 37 * hash
				+ (this.envVarName != null ? this.envVarName.hashCode() : 0);
		hash = 37 * hash + (this.keyName != null ? this.keyName.hashCode() : 0);
		hash = 37 * hash
				+ (this.valueName != null ? this.valueName.hashCode() : 0);
		return hash;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getEnvVarName() {
		return envVarName;
	}

	public void setEnvVarName(String environmentVar) {
		this.envVarName = environmentVar;
	}

	public String getKeyName() {
		return keyName;
	}

	public void setKeyName(String keyName) {
		this.keyName = keyName;
	}

	public String getValueName() {
		return valueName;
	}

	public void setValueName(String valueName) {
		this.valueName = valueName;
	}

	public String getHive() {
		return hive;
	}

	public void setHive(String hive) {
		this.hive = hive;
	}
}
