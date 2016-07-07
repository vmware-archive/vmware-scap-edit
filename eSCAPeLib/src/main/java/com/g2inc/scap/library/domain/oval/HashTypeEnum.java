package com.g2inc.scap.library.domain.oval;

public enum HashTypeEnum {
	MD5("MD5"),
	SHA1("SHA-1"),
	SHA224("SHA-224"),
	SHA256("SHA-256"),
	SHA384("SHA-384"),
	SHA512("SHA-512");
	
	private String name;
	
	private HashTypeEnum(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public static HashTypeEnum valueOfName(String name) {
		HashTypeEnum value = null;
		for (HashTypeEnum enumValue : HashTypeEnum.values()) {
			if (enumValue.name.equals(name)) {
				value = enumValue;
			}
		}
		return value;
	}

}
