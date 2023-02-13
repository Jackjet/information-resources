package com.digitalchina.resourcecatalog.admin.util;

public class AdminResponseCode {
    public static final Integer ADMIN_INVALID_NAME = 601;
    public static final Integer ADMIN_INVALID_PASSWORD = 602;
    public static final Integer ADMIN_NAME_EXIST = 602;
    public static final Integer ADMIN_ALTER_NOT_ALLOWED = 603;
    public static final Integer ADMIN_DELETE_NOT_ALLOWED = 604;
    public static final Integer ADMIN_INVALID_ACCOUNT = 605;
    public static final Integer USER_INVALID_NAME = 630;
    public static final Integer USER_INVALID_PASSWORD = 631;
    public static final Integer USER_INVALID_MOBILE = 632;
    public static final Integer USER_NAME_EXIST = 633;
    public static final Integer USER_MOBILE_EXIST = 634;
    public static final Integer ROLE_NAME_EXIST = 640;
    public static final Integer ROLE_SUPER_SUPERMISSION = 641;
    public static final Integer ROLE_USER_EXIST = 642;
    public static final Integer NOTICE_UPDATE_NOT_ALLOWED = 660;
    
    public static final Integer EXIST = 701;
    public static final Integer NO_EXIST = 702;
    public static final Integer HAS_HIS_DATA = 703;
    //业务逻辑错误
    public static final Integer BUS_ERROR = 704;
    
    public static final Integer INFO_CODE_EXIST = 801;
    public static final Integer INFO_EXIST = 802;
    public static final Integer INFO_NO_DELETE = 803;

    public static final Integer KEYCLOAK_USER_NO_EXIST = 900;
}
