package com.shopping.core.enums;

/**
 * Created by dyp on 2016/8/26.
 */
public enum ConfirmStatus {
    ALL("全部", 0, Boolean.TRUE),
    YES("已确认", 1, Boolean.FALSE),
    NO("未确认", 2, Boolean.FALSE);

    private String name;
    private int value;
    private Boolean onlyQuery;                  // 仅用于页面查询和业务逻辑无关
    private int type;

    private ConfirmStatus(String name, int value, Boolean onlyQuery) {
        this.name = name;
        this.value = value;
        this.onlyQuery = onlyQuery;
    }

    public static ConfirmStatus getEnumFromString(String string) {
        if (string != null) {
            try {
                return Enum.valueOf(ConfirmStatus.class, string.trim());
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
