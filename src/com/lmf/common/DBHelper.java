package com.lmf.common;

import java.sql.Connection;

import java.sql.DriverManager;

import java.sql.PreparedStatement;

import java.sql.ResultSet;

import java.sql.SQLException;

import java.sql.Statement;

public class DBHelper {

	public static final String DRIVER = "com.mysql.jdbc.Driver"; 


	private static DBHelper mInstance;
	private Connection mConnection;

	private DBHelper() {

	}

	public static DBHelper getInstance() {
		if (mInstance == null) {
			mInstance = new DBHelper();
		}
		return mInstance;
	}

	public void release() {
		free(mConnection);
	}

	public void init(String url, String user, String pw) {
		mConnection = getConnection(url, user, pw);
	}


	private Connection getConnection(String url, String user, String pw) {

		Connection conn = null;
		try {
			String driver = DRIVER; 

			Class.forName(driver); 
			if (null == conn) {
				conn = DriverManager.getConnection(url, user, pw);
			}

		} catch (ClassNotFoundException e) {
			System.out.println("Sorry,can't find the Driver!");
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return conn;
	}


	public int executeNonQuery(String sql) {

		int result = 0;
		Statement stmt = null;
		try {
			stmt = mConnection.createStatement();
			result = stmt.executeUpdate(sql);
		} catch (SQLException err) {
			err.printStackTrace();
			free(stmt);
		} finally {
			free(stmt);

		}
		return result;
	}



	public int executeNonQuery(String sql, Object... obj) {

		int result = 0;
		PreparedStatement pstmt = null;
		try {
			pstmt = mConnection.prepareStatement(sql);
			for (int i = 0; i < obj.length; i++) {
				pstmt.setObject(i + 1, obj[i]);
			}
			result = pstmt.executeUpdate();

		} catch (SQLException err) {
			err.printStackTrace();
			free(pstmt);
		} finally {
			free(pstmt);
		}

		return result;
	}



	public ResultSet executeQuery(String sql) {
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = mConnection.createStatement();
			rs = stmt.executeQuery(sql);
		} catch (SQLException err) {
			err.printStackTrace();
			free(rs);
			free(stmt);
		}
		return rs;
	}



	public ResultSet executeQuery(String sql, Object... obj) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = mConnection.prepareStatement(sql);
			for (int i = 0; i < obj.length; i++) {
				pstmt.setObject(i + 1, obj[i]);
			}
			rs = pstmt.executeQuery();
		} catch (SQLException err) {
			err.printStackTrace();
			free(rs);
			free(pstmt);
		}
		return rs;

	}


	public Boolean isExist(String sql) {
		ResultSet rs = null;
		try {
			rs = executeQuery(sql);
			rs.last();
			int count = rs.getRow();
			if (count > 0) {
				return true;
			} else {
				return false;
			}

		} catch (SQLException err) {
			err.printStackTrace();
			free(rs);
			return false;

		} finally {
			free(rs);
		}

	}

	public Boolean isExist(String sql, Object... obj) {
		ResultSet rs = null;
		try {
			rs = executeQuery(sql, obj);
			rs.last();
			int count = rs.getRow();
			if (count > 0) {
				return true;
			} else {
				return false;
			}

		} catch (SQLException err) {
			err.printStackTrace();
			free(rs);
			return false;

		} finally {
			free(rs);
		}
	}


	public int getCount(String sql) {
		int result = 0;
		ResultSet rs = null;
		try {
			rs = executeQuery(sql);
			rs.last();
			result = rs.getRow();
		} catch (SQLException err) {
			free(rs);
			err.printStackTrace();
		} finally {
			free(rs);
		}
		return result;
	}


	public int getCount(String sql, Object... obj) {
		int result = 0;
		ResultSet rs = null;
		try {
			rs = executeQuery(sql, obj);
			rs.last();
			result = rs.getRow();
		} catch (SQLException err) {
			err.printStackTrace();
		} finally {
			free(rs);
		}
		return result;
	}


	public void free(ResultSet rs) {
		try {
			if (rs != null) {
				rs.close();
			}
		} catch (SQLException err) {
			err.printStackTrace();
		}
	}


	public void free(Statement st) {
		try {
			if (st != null) {
				st.close();
			}
		} catch (SQLException err) {
			err.printStackTrace();
		}
	}


	public void free(Connection conn) {
		try {
			if (conn != null) {
				conn.close();
			}
		} catch (SQLException err) {
			err.printStackTrace();
		}

	}

}