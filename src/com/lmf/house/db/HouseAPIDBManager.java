package com.lmf.house.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import com.google.common.collect.ImmutableBiMap.Builder;
import com.google.gson.Gson;
import com.lmf.common.DBHelper;
import com.lmf.common.Utils;
import com.lmf.house.model.HouseInfoModel;
import com.lmf.house.model.HousePriceModel;

public class HouseAPIDBManager {

	public static final String JDBC_URL = "jdbc:MySQL://127.0.0.1:3306/crawler";// ���ݿ�
	public static final String USER = "crawler"; // �û���
	public static final String PASSWORD = "shui"; // ����

	public static final String HOUSE_INFO_API_TABLE = "house_info_api";

	public static final String SQL_INSERT_INFO_MODEL = "INSERT  INTO house_info_api (" + " uuid ," + " house_code ,"
			+ " kv_house_type ," + " title ," + " cover_pic ," + " comm_avg_price ," + " district_id ,"
			+ " bizcircle_id ," + "  bizcircle_name ," + "  community_id ," + "  community_name ,"
			+ "  blueprint_hall_num ," + "  blueprint_bedroom_num ," + "  area ," + "  price ," + "  unit_price ,"
			+ "  sign_price ," + "  sign_unit_price ," + "  sign_time ," + "  sign_source ," + "  floor_state ,"
			+ "  orientation ," + "  baidu_la ," + "  baidu_lo ," + "  tags ," + "  school_info ," + "  subway_info  ,"
			+ "  stamp  " + ")VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";

	public static final String SQL_UPDATE_PRICE_MODEL = "update house_price_api set uuid=?,price=? ,price_list=? ,unit_price=? ,sign_price=? ,sign_unit_price=?,status=?,status_list=?,stamp=?,stamp_list=? where  house_code =?";

	public static final String SQL_DELETE = "delete from house_info_api where _id =?";

	public static final String SQL_INSERT_PRICE_MODEL = "INSERT  INTO house_price_api (" + " uuid ," + " house_code ,"
			+ " price ," + " price_list ," + " unit_price ," + " sign_price ," + " sign_unit_price ," + " status ,"
			+ " status_list ," + " stamp ," + " stamp_list " + ")VALUES (?,?,?,?,?,?,?,?,?,?,?);";
	public static final String SQL_GET_PRICE_MODEL = "select uuid,house_code,price,price_list,unit_price,sign_price,sign_unit_price,status,status_list,stamp,stamp_list from house_price_api   where house_code =? order by _id desc";
	public static final String SQL_GET_PRICE_IDS = "select price from house_info_api  where house_code =? order by _id desc";

	public static void main(String[] args) throws Exception {
		init();

		release();
	}

	public static void init() {
		DBHelper.getInstance().init(JDBC_URL, USER, PASSWORD);
	}

	public static void release() {
		DBHelper.getInstance().release();
	}

	public static int insertInfo(String uuid, HouseInfoModel info) {
		if (info == null) {
			return 0;
		}

		Gson gson = new Gson();
		String tags = gson.toJson(info.tags);
		String school_info = gson.toJson(info.school_info);
		String subway_info = gson.toJson(info.subway_info);

		return DBHelper.getInstance().executeNonQuery(SQL_INSERT_INFO_MODEL, uuid, info.house_code, info.kv_house_type,
				info.title, info.cover_pic, info.comm_avg_price, info.district_id, info.bizcircle_id,
				info.bizcircle_name, info.community_id, info.community_name, info.blueprint_hall_num,
				info.blueprint_bedroom_num, info.area, info.price, info.unit_price, info.sign_price,
				info.sign_unit_price, info.sign_time, info.sign_source, info.floor_state, info.orientation,
				info.baidu_la, info.baidu_lo, tags, school_info, subway_info, System.currentTimeMillis());

	}

	public static int insertPrice(String uuid, HouseInfoModel info) {
		if (info == null) {
			return 0;
		}
		long time = System.currentTimeMillis();
		return DBHelper.getInstance().executeNonQuery(SQL_INSERT_PRICE_MODEL, uuid, info.house_code, info.price,
				info.price, info.unit_price, info.sign_price, info.sign_unit_price, HousePriceModel.STATUS_DEFAULT,
				HousePriceModel.STATUS_DEFAULT, time, time);

	}

	public static int updatePrice(String uuid, HouseInfoModel info, HousePriceModel temp) {
		if (info == null) {
			return 0;
		}
		long time = System.currentTimeMillis();
		temp.price_list = temp.price_list == null ? info.price : temp.price_list + "-" + info.price;
		int priceOld = Utils.parseInteger(temp.price);
		int priceNew = Utils.parseInteger(info.price);
		if (priceOld > priceNew) {
			temp.status = HousePriceModel.STATUS_DROP;
		} else if (priceOld < priceNew) {
			temp.status = HousePriceModel.STATUS_RISE;
		} else {
			temp.status = HousePriceModel.STATUS_DEFAULT;
		}

		temp.status_list = temp.status_list == null ? String.valueOf(temp.status) : temp.status_list + "-" + temp.status;
		temp.stamp_list = temp.stamp_list == null ? String.valueOf(time) : temp.stamp_list + "-" + String.valueOf(time);

		return DBHelper.getInstance().executeNonQuery(SQL_UPDATE_PRICE_MODEL, uuid, info.price, temp.price_list,
				info.unit_price, info.sign_price, info.sign_unit_price, temp.status, temp.status_list,
				time, temp.stamp_list, info.house_code);

	}

	public static int delete(int id) {
		return DBHelper.getInstance().executeNonQuery(SQL_DELETE, id);

	}

	public static HousePriceModel getHousePriceModelFromDB(String house_code) {

		ResultSet rs = DBHelper.getInstance().executeQuery(SQL_GET_PRICE_MODEL, house_code);
		try {
			if (rs.next()) {
				HousePriceModel model = new HousePriceModel();
				model.uuid = rs.getString(1);
				model.house_code = rs.getString(2);
				model.price = rs.getString(3);
				model.price_list = rs.getString(4);
				model.unit_price = rs.getString(5);
				model.sign_price = rs.getString(6);
				model.sign_unit_price = rs.getString(7);
				model.status = rs.getInt(8);
				model.status_list = rs.getString(9);
				model.stamp = rs.getString(10);
				model.stamp_list = rs.getString(11);
				return model;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DBHelper.getInstance().free(rs);
		}
		return null;
	}

}
