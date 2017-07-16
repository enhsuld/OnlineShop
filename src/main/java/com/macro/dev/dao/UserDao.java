package com.macro.dev.dao;


import com.macro.dev.models.LutUser;

import java.util.List;


public interface UserDao {

//	void inserBatch(List<FinCtt5> datas);
	public Object PeaceCrud(Object obj, String domainname, String method, Long obj_id, int page_val, int maxresult, String whereclause);
	public Object getHQLResult(String hqlString, String type);
	public List<?> kendojson(String request, String domain);
	public int resulsetcount(String request, String domain);
	Object findAll(String domain, String whereclause);
	Object saveOrUpdate(Object obj);
	public Object findById(String domain, long id, String whereclause);
	void deleteById(String domain, long id, String whereclause);
	public List<?> jData(Integer pageSize, Integer skip, String sortColumn, String sortColumnDir, String searchStr, String domain);
	public Object getNativeSQLResult(String queryStr, String type);
	void inserBatch(List<?> datas, String model, long planid);

}