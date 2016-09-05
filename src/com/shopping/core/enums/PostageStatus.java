package com.shopping.core.enums;

/**
 * Created by dyp on 2016/8/26.
 */
public enum PostageStatus {
    ALL("全部", 0, Boolean.TRUE),
    FREE_SHIPPING("免邮", 1, Boolean.FALSE),
    PROPERTY_TO_PAY("物业代付", 2, Boolean.FALSE);

    private String name;
    private int value;
    private Boolean onlyQuery;                  // 仅用于页面查询和业务逻辑无关
    private int type;

    private PostageStatus(String name, int value, Boolean onlyQuery) {
        this.name = name;
        this.value = value;
        this.onlyQuery = onlyQuery;
    }

    public static PostageStatus getEnumFromString(String string) {
        if (string != null) {
            try {
                return Enum.valueOf(PostageStatus.class, string.trim());
            } catch (IllegalArgumentException ex) {
            }
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public Integer getValue() {
        return value;
    }

    public Boolean isOnlyQuery() {
        return onlyQuery;
    }

    public int getType() {
        return type;
    }

}
