package com.sabomanq.currencyservice.dao;

import java.sql.Connection;
import java.sql.SQLException;

public interface ConnectionProvider {
    Connection open() throws SQLException;
}
