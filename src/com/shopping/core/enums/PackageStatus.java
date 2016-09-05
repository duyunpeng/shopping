package com.shopping.core.enums;

/**
 * Created by dyp on 2016/8/23.
 */
public enum PackageStatus {

    ALL("全部", 0, Boolean.TRUE),
    FILE("文件", 1, Boolean.FALSE),
    EXPRESS_DELIVERY("快件", 2, Boolean.FALSE);

    private String name;
    private int value;
    private Boolean onlyQuery;                  // 仅用于页面查询和业务逻辑无关
    private int type;

    private PackageStatus(String name, int value, Boolean onlyQuery) {
        this.name = name;
        this.value = value;
        this.onlyQuery = onlyQuery;
    }

    public static PackageStatus getEnumFromString(String string) {
        if (string != null) {
            try {
                return Enum.valueOf(PackageStatus.class, string.trim());
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
