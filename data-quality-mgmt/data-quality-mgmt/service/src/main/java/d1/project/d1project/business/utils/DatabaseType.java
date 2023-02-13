package d1.project.d1project.business.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据库类型枚举
 */
public enum DatabaseType {
    POSTGRESQL("Postgresql"),
    MYSQL("Mysql"),
    SQLSERVER("SqlServer"),
    Oracle("Oracle");

    private String name;

    DatabaseType(String name) {
        this.name = name;
    }

    public static DatabaseType getOperatorByName(String name) {
        for (DatabaseType databaseType : DatabaseType.values()) {
            if (databaseType.getName().equals(name)) {
                return databaseType;
            }
        }
        return null;
    }

    public static boolean check(String name) {
        for (DatabaseType databaseType : DatabaseType.values()) {
            if (databaseType.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }


    public static List<String> getNames() {
        List<String> names = new ArrayList<>();
        for (DatabaseType databaseType : DatabaseType.values()) {
            names.add(databaseType.getName());
        }
        return names;
    }


    public String getName() {
        return name;
    }

}
