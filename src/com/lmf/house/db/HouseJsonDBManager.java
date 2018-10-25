package com.lmf.house.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.google.common.collect.ImmutableBiMap.Builder;
import com.google.gson.Gson;
import com.lmf.common.DBHelper;
import com.lmf.common.Utils;
import com.lmf.house.model.CrawlerJsonModel;
import com.lmf.house.model.HouseInfoModel;
import com.lmf.house.model.HouseJsonModel;
import com.lmf.house.model.HousePriceModel;
import com.lmf.house.model.ImageJsonModel;

public class HouseJsonDBManager {

	// CREATE TABLE IF NOT EXISTS `house_crawler`(
	// `_id` INT UNSIGNED AUTO_INCREMENT,
	// `title` TEXT ,
	// `url` TEXT NOT NULL,
	// `status` INT ,
	// `content` TEXT ,
	// `stamp` LONG NOT NULL,
	// PRIMARY KEY ( `_id` )
	// )
	// 插入demo
	// INSERT INTO house_crawler(title,url,content,stamp)
	// VALUES("test","no","content",123123123);
	// 建表demo
	// CREATE TABLE IF NOT EXISTS `house_info`(
	// `_id` INT UNSIGNED AUTO_INCREMENT,
	// `ucid` varchar(50) ,
	// `houseType` TEXT ,
	// `isUnique` TEXT ,
	// `registerTime` TEXT ,
	// `totalPrice` varchar(10) ,
	// `price` varchar(20) ,
	// `houseId` TEXT ,
	// `resblockId` TEXT ,
	// `resblockName` TEXT ,
	// `isRemove` varchar(10) ,
	// `defaultImg` TEXT ,
	// `defaultBrokerIcon` TEXT ,
	// `resblockPosition` TEXT ,
	// `hasDaikan` varchar(10) ,
	// `uniqueAgent` INT ,
	// `showCart` TEXT ,
	// `hasFangjia` INT ,
	// `test_400_hide` TEXT ,
	// `newTax` INT ,
	// `uuid` TEXT ,
	// `loadingImg` TEXT ,
	// `qrImg` TEXT ,
	// `title` TEXT ,
	// `stamp` LONG NOT NULL,
	// PRIMARY KEY ( `_id` )
	// )
	// 插入demo
	// INSERT INTO
	// house_info(ucid,houseType,isUnique,registerTime,totalPrice,price,houseId,
	// resblockId,resblockName,isRemove,defaultImg,defaultBrokerIcon,resblockPosition,
	// hasDaikan,uniqueAgent,showCart,hasFangjia,test_400_hide,newTax,uuid,loadingImg,qrImg,title,stamp
	// )
	// VALUES("sdf","?","?","?","?","?","?","?","?","?","?","?","?",4,3,"?",2,"?",1,"?","?","?","?",12312);

	public static final String JDBC_URL = "jdbc:MySQL://45.77.85.189:3306/crawler?useUnicode=true&characterEncoding=utf-8&useSSL=false";
	public static final String USER = "crawler";
	public static final String PASSWORD = "Shui@125";

	public static final String HOUSE_CRAWLER = "house_crawler";
	public static final String HOUSE_INFO = "house_info";

	public static final String SQL_INSERT_CRAWLER = "INSERT INTO house_crawler(title,url,status,content,stamp) "
			+ "VALUES(?,?,?,?,?)";
	public static final String SQL_INSERT_INFO = "	INSERT INTO house_info" + "("
			+ "ucid,houseType,isUnique,registerTime,totalPrice,price,houseId,resblockId,resblockName,isRemove,defaultImg,defaultBrokerIcon,resblockPosition,"
			+ "	hasDaikan,uniqueAgent,showCart,hasFangjia,test_400_hide,newTax,uuid,loadingImg,qrImg,title,stamp"
			+ "	) " + "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

	public static final String SQL_INSERT_SALT = "INSERT INTO house_salt(url,stamp) " + "VALUES(?,?)";

	public static final String SQL_SELECT_SALT = "select url from house_salt  order by _id ";

	public static final String SQL_DELETE_SALT = "delete from house_salt where stamp < ?";

	public static void main(String[] args) throws Exception {
		init();
		insertSalt("https://bj.lianjia.com/ershoufang/bajiao1/?sug=%E5%85%AB%E8%A7%92");
		insertSalt("https://bj.lianjia.com/ershoufang/rs%E5%8F%A4%E5%9F%8E/");
		insertSalt("https://bj.lianjia.com/ershoufang/rs%E8%8B%B9%E6%9E%9C%E5%9B%AD/");
		insertSalt("https://bj.lianjia.com/ershoufang/rs%E8%80%81%E5%B1%B1/");
		insertSalt("https://bj.lianjia.com/ershoufang/rs%E5%9B%9E%E9%BE%99%E8%A7%82/");
	
		release();
		System.out.println("done");
	}

	public static void init() {
		DBHelper.getInstance().init(JDBC_URL, USER, PASSWORD);
	}

	public static void release() {
		DBHelper.getInstance().release();
	}

	public static int insertCrawler(CrawlerJsonModel model) {
		if (model == null) {
			return 0;
		}
		return DBHelper.getInstance().executeNonQuery(SQL_INSERT_CRAWLER, model.title, model.url, model.status,
				model.content, System.currentTimeMillis());
	}

	public static int insertHouse(HouseJsonModel model) {
		if (model == null) {
			return 0;
		}
		return DBHelper.getInstance().executeNonQuery(SQL_INSERT_INFO, model.ucid, model.houseType, model.isUnique,
				model.registerTime, model.totalPrice, model.price, model.houseId, model.resblockId, model.resblockName,
				model.isRemove, model.defaultImg, model.defaultBrokerIcon, model.resblockPosition, model.hasDaikan,
				model.uniqueAgent, model.showCart, model.hasFangjia, model.test_400_hide, model.newTax, model.uuid,
				model.loadingImg, model.qrImg, model.title, System.currentTimeMillis());

	}

	public static int insertSalt(String url) {
		if (url == null) {
			return 0;
		}
		return DBHelper.getInstance().executeNonQuery(SQL_INSERT_SALT, url,System.currentTimeMillis());
	}

	public static List<String> selectSalt() {
		List<String> list = new ArrayList();

		ResultSet rs = DBHelper.getInstance().executeQuery(SQL_SELECT_SALT);
		try {
			while (rs.next()) {
				list.add(rs.getString(1));
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DBHelper.getInstance().free(rs);
		}
	
		return list;
	}

	public static int deleteSalt(long stamp) {
		return DBHelper.getInstance().executeNonQuery(SQL_DELETE_SALT,stamp);
	}

}
