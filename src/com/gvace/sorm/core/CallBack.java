package com.gvace.sorm.core;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public interface CallBack {
	Object execute(Connection conn, PreparedStatement ps, ResultSet rs);
}
