package com.mrd.organization.config;

import org.hibernate.dialect.MySQL5Dialect;
import org.hibernate.dialect.function.SQLFunctionTemplate;
import org.hibernate.type.StandardBasicTypes;

public class CustomMySQLDialect extends MySQL5Dialect {
    public CustomMySQLDialect(){
        super();
        this.registerFunction("group_concat", new SQLFunctionTemplate(
                StandardBasicTypes.STRING,"group_concat(?1)"
        ));
    }

    @Override
    public String getTableTypeString() {
        return " ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin";
    }
}
