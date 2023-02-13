package com.digitalchina.resourcecatalog.db.constant;

/**
 * 物业类型枚举
 * 
 * @author wangmh
 *
 */
public enum PropertyTypeEnum {

	HOUSE("1", "住宅"), SHOP("2", "底商"), BASEMENT("3", "地下室"), PARKING("4", "车库"), PUBLIC("5", "公建");

	private String code;

	private String name;

	private PropertyTypeEnum(String code, String name) {
		this.code = code;
		this.name = name;
	}

	/**
	 * 获取物业名称
	 * 
	 * @param code
	 * @return
	 */
	public static String getNameByCode(String code) {
		for (PropertyTypeEnum value : PropertyTypeEnum.values()) {
			if (value.getCode().equals(code)) {
				return value.getName();
			}
		}
		return null;
	}

	public String getCode() {
		return code;
	}

	public String getName() {
		return name;
	}

}
