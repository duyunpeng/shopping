package com.shopping.core.enums;

/**
 * Created by dyp on 2016/8/26.
 */
public enum RepairStatus {
    ALL("全部", 0, Boolean.TRUE),
    PUBLIC_AREA_MAINTENANCE("公共部位维修", 1, Boolean.FALSE),
    SHARED_FACILITIES_EQUIPMENT("共用设施设备", 2, Boolean.FALSE),
    PERSONAL_SITE_MAINTENANCE ("自用部位维修",3,Boolean.FALSE);
    private String name;
    private int value;
    private Boolean onlyQuery;                  // 仅用于页面查询和业务逻辑无关
    private int type;

    private RepairStatus(String name, int value, Boolean onlyQuery) {
        this.name = name;
        this.value = value;
        this.onlyQuery = onlyQuery;
    }

    public static RepairStatus getEnumFromString(String string) {
        if (string != null) {
            try {
                return Enum.valueOf(RepairStatus.class, string.trim());
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
