package com.macro.dev.dao;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.exception.ConstraintViolationException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Repository
@Transactional
public class UserDaoImpl  extends JdbcDaoSupport implements UserDao {
	
	
	@PersistenceContext
	private EntityManager em;

	@Autowired
	private SessionFactory sessionFactory;	
	
	@Autowired 
	DataSource dataSource;
	
	@PostConstruct
	private void initialize(){
		setDataSource(dataSource);
	}
	

	@Override
	public void inserBatch(final List<?> dt, String model,long planid) {

		/*if(model.equals("FinJournal")){
			String sql = "INSERT INTO fin_journal " + "(planid,data1,data2,data3,data4,data5,data6,data7,data8,data9,data10,data11,data12,data13,data14,data15,data16,data17,data18,data19,data20,a,b,c,d,e,amount) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			getJdbcTemplate().batchUpdate(sql, new BatchPreparedStatementSetter() {
				public void setValues(PreparedStatement ps, int i) throws SQLException,NullPointerException {
					FinJournal item = (FinJournal) dt.get(i);
					ps.setLong(1, planid);
					ps.setString(2, item.getData1());
					ps.setString(3, item.getData2());
					ps.setString(4, item.getData3());
					ps.setString(5, item.getData4());
					ps.setString(6, item.getData5());
					ps.setString(7, item.getData6());
					ps.setString(8, item.getData7());
					ps.setString(9, item.getData8());
					ps.setString(10, item.getData9());
					ps.setLong(11, item.getData10());
					ps.setString(12, item.getData11());
					ps.setString(13, item.getData12());
					ps.setString(14, item.getData13());
					ps.setString(15, item.getData14());
					ps.setString(16, item.getData15());
					ps.setString(17, item.getData16());
					ps.setString(18, item.getData17());
					ps.setString(19, item.getData18());
					ps.setString(20, item.getData19());
					ps.setString(21, item.getData20());
					ps.setBoolean(22, true);
					ps.setBoolean(23, true);
					ps.setBoolean(24, true);
					ps.setBoolean(25, true);
					ps.setBoolean(26, true);
					ps.setInt(27, 0);
				}

				@Override
				public int getBatchSize() {
					return dt.size();
				}
			});
		}*/
	}
	
	@Override
	public Object saveOrUpdate(Object obj) {
		try{			
			sessionFactory.getCurrentSession().update(obj);
			return true;
		}
		catch (ConstraintViolationException err){
			err.printStackTrace();			
			return false;
		}
		
	}
	
	@Override
	public Object findAll(String domain, String whereclause) {
		Query query=null;
		if(whereclause!=null){			
			query = sessionFactory.getCurrentSession().createQuery(whereclause);			
		}
		else{
			query = sessionFactory.getCurrentSession().createQuery("from "+domain+" objlist  order by objlist.id desc ");
		}
		
		List<Object> robj = query.list();
		query = null;
		return robj;
		
	}
	
	@Override
	public Object findById(String domain,long id, String whereclause) {
		Query query=null;
		query=sessionFactory.getCurrentSession().createQuery("from "+domain+" t where t.id=:id");
		query.setParameter("id", id);
		Object robj = query.list().get(0);		
		return robj;
		
	}
	
	@Override
	public void deleteById(String domain,long obj_id, String whereclause) {
		Query query=null;
		if(whereclause!=null){			
			query= sessionFactory.getCurrentSession().createQuery("delete from "+domain+"  t where t."+whereclause+"=:obj_id");
			query.setParameter("obj_id", obj_id);
			
		}
		else{
			query= sessionFactory.getCurrentSession().createQuery("delete from "+domain+"  t where t.id=:obj_id");
    		query.setParameter("obj_id", obj_id);
		}
		//List list = query.list();
		int qresult = query.executeUpdate();
		//return qresult;
		
	}
		
	
	@Override
	public Object getHQLResult(String hqlString,String returnType){
		Query query = sessionFactory.getCurrentSession().createQuery(hqlString);
		System.out.println(query);
		
		if("list".equals(returnType)){
			return  query.list();
		}
		else if("current".equals(returnType)){
			if(query.list().size()>0){
				if(query.list().size()==1){
					return query.list().get(0);
				}
				else {
					return null;
				}
			}
			
		}
		else if("count".equals(returnType)){
			int resultInt =  ((Long)query.uniqueResult()).intValue();
			return resultInt;
		}
		
		else {
			return null;
		}
		
		return null;
	}

	public List<Object> kendojson(String request,String tablename){

		try{			
			
		//	Gson gson = new Gson ();	    
			int skip=0;
			int take=2;
			int page=0;			
			String field="";
			String order="";
			String dir="";
			JSONArray sort=null;
			String group="";
			JSONObject filter=null;
			String org="";
			String custom="";
			String nat="";
			String flquery="";
			String isspecial="";
			System.out.println("sss"+request+tablename);
			JSONObject req= new JSONObject(request);				
			skip=req.getInt("skip");			
			page=req.getInt("page");
			if(req.has("sort")){
				sort=req.getJSONArray("sort");
			}
			if(req.has("take")){
				take=req.getInt("take");
			}
			
			if(req.has("customPsize")){
				take=req.getInt("customPsize");
			}
			
			if(req.has("group")){
				group=req.getString("group");
			}
			if(req.has("filter")){
				
				if(!req.isNull("filter")){
					filter=req.getJSONObject("filter");	
				}
						
			}
		
			if(req.has("custom")){
				custom=req.getString("custom");
			}
			if(req.has("field")){
				field=req.getString("field");
			}
			if(req.has("dir")){
				dir=req.getString("dir");
			}
			if(custom.length()>0){
				flquery=custom;
			}
			if(req.has("native")){
				nat=req.getString("native");
			}
			if(req.has("isspecial")){
				isspecial=req.getString("isspecial");
			}
			
			String multiOrde="";
			
			if(sort!=null){
				JSONArray arr= sort;
				for(int i=0; i<arr.length();i++){
					String str=arr.get(i).toString();
					JSONObject srt= new JSONObject(str);
					if(srt.isNull("field")){
						field="";	
					}
					else{
						field=srt.getString("field");
						multiOrde=multiOrde+ " "+ field;
						
					}
					if(srt.isNull("dir")){
						dir="";
					}
					else{
						dir=srt.getString("dir");
						multiOrde=multiOrde + " " +dir + ",";
					}
				}
				
			}
			if(multiOrde.length()>0){
				System.out.println("$$$$ "+multiOrde.substring(0, multiOrde.length()-1));
			}
			
			
			String groupfield="";
			String groupdir="";
			if(group.length()>0){
				JSONArray arr= new JSONArray(group);
				for(int i=0; i<arr.length();i++){
					String str=arr.get(i).toString();
					JSONObject srt= new JSONObject(str);
					if(srt.isNull("field")){
						groupfield="";	
					}
					else{
						groupfield=srt.getString("field");	
					}
					if(srt.isNull("dir")){
						groupdir="";
					}
					else{
						groupdir=srt.getString("dir");
					}
				}
				
			}
			String filterfield="";
			String operator="";
			String value="";
			
			if(filter!=null){
				
				JSONObject fltr= filter;		
				
				String logic=fltr.getString("logic");
				
				JSONArray arr= fltr.getJSONArray("filters");
				for(int i=0; i<arr.length();i++){
					String str=arr.get(i).toString();
					JSONObject srt= new JSONObject(str);
					if(srt.isNull("field")){
						filterfield="";	
					}
					else{
						filterfield=srt.getString("field");	
					}
					if(srt.isNull("operator")){
						operator="";
					}
					else{
						operator=srt.getString("operator");
					}
					if(srt.isNull("value")){
						value="";
					}
					else{
						value=String.valueOf(srt.get("value")).toLowerCase();
					}
					if(i>0){
						
						switch(operator){
							case "startswith":flquery=flquery+  " "+logic+" lower("+filterfield+ ") LIKE '"+value+"%'"; break;
							case "contains":flquery=flquery+  " "+logic+" lower("+filterfield+ ") LIKE '%"+value+"%'"; break;
							case "doesnotcontain":flquery=flquery+  " "+logic+" lower("+filterfield+ ") NOT LIKE '%"+value+"%'"; break;
							case "endswith":flquery=flquery+  " "+logic+" lower("+filterfield+ ") LIKE '%"+value+"'"; break;
							case "neq":flquery=flquery+  " "+logic+" lower("+filterfield+ ") != '"+value+"'"; break;
							case "eq":flquery=flquery+  " "+logic+" lower("+filterfield+ ") = '"+value+"'"; break;
							case "gte":flquery=flquery+  " "+logic+" lower("+filterfield+ ") >="+value+""; break;
						}						
					}
					else{
						if(custom.length()>0){
							System.out.println("enduu");
							if(custom.contains("orgid1")){
								
								System.out.println("enduu 111");
								switch(operator){
									case "startswith":flquery=" Where lower("+filterfield+ ") LIKE '"+value+"%'"; break;
									case "contains":flquery=" Where lower("+filterfield+ ") LIKE '%"+value+"%'"; break;
									case "doesnotcontain":flquery=" Where lower("+filterfield+ ") NOT LIKE '%"+value+"%'"; break;
									case "endswith":flquery=" Where lower("+filterfield+ ") LIKE '%"+value+"'"; break;
									case "neq":flquery=" Where lower("+filterfield+ ") != '"+value+"'"; break;
									case "eq":flquery=" Where lower("+filterfield+ ") = '"+value+"'"; break;
									case "gte":flquery=" Where lower("+filterfield+ ") >= '"+value+"'"; break;
								}								
							}
							else{
								
								System.out.println("enduu 222 "); 
								switch(operator){
									case "startswith":flquery=" "+custom+" and lower("+filterfield+ ") LIKE '"+value+"%'"; break;
									case "contains":flquery=" "+custom+" and lower("+filterfield+ ") LIKE '%"+value+"%'"; break;
									case "doesnotcontain":flquery=" "+custom+" and lower("+filterfield+ ") NOT LIKE '%"+value+"%'"; break;
									case "endswith":flquery=" "+custom+" and lower("+filterfield+ ") LIKE '%"+value+"'"; break;
									case "neq":flquery=" "+custom+" and lower("+filterfield+ ") != '"+value+"'"; break;
									case "eq":flquery=" "+custom+" and lower("+filterfield+ ") = '"+value+"'"; break;
									case "gte":flquery=" "+custom+" and lower("+filterfield+ ") >= '"+value+"'"; break;
								}
							}							
						}
						else{
							switch(operator){
								case "startswith":flquery=" Where lower("+filterfield+ ") LIKE '"+value+"%'"; break;
								case "contains":flquery=" Where lower("+filterfield+ ") LIKE '%"+value+"%'"; break;
								case "doesnotcontain":flquery=" Where lower("+filterfield+ ") NOT LIKE '%"+value+"%'"; break;
								case "endswith":flquery=" Where lower("+filterfield+ ") LIKE '%"+value+"'"; break;
								case "neq":flquery=" Where lower("+filterfield+ ") != '"+value+"'"; break;
								case "eq":flquery=" Where lower("+filterfield+ ") = '"+value+"'"; break;
								case "gte":flquery=" Where lower("+filterfield+ ") >= '"+value+"'"; break;
							}
						}
						
					}
					
				}
			
			}
			
			if(groupfield.isEmpty()){
				group="";
			}
			else{
				group="group by "+groupfield+" "+groupdir+"";
			}
			
			if(field.isEmpty()){
				order="order by t.id desc";
			}
			else{
				order="order by "+multiOrde.substring(0,multiOrde.length()-1)+"";
			}
					
			
			String query="from "+tablename+" t  "+flquery+"  "+group+" "+order+"";
			
			System.out.println("query " +query);
			
			if(isspecial.isEmpty()){
				Query hql = sessionFactory.getCurrentSession().createQuery(query);
				hql.setFirstResult(skip);
				hql.setMaxResults(take);
				List<Object> rlist = hql.list();
				  //List list = hql.list();
				//sessionFactory.getCurrentSession().flush();
				
				return rlist;
			}
			else {
				Query  nquery= sessionFactory.getCurrentSession().createSQLQuery(isspecial);
				List<Object> nlist=nquery.list();
				
				return nlist;
			}			
		}
		catch(Exception e){
			e.printStackTrace();
			return null;
		}
		
	}
	
	

	@Override
	public int resulsetcount(String request,String tablename) {
		try{			
		//	Gson gson = new Gson ();	    
			
			String field="";
			String order="";
			String dir="";
			String sort="";
			String group="";
			JSONObject filter=null;
			String org="";
			String custom="";
			String isspecial="";
			System.out.println("req "+request);
			System.out.println("req "+tablename);
			JSONObject req= new JSONObject(request);				
			
			if(req.has("filter")){
				if(!req.isNull("filter")){
					filter=req.getJSONObject("filter");	
				}
			}
		
			if(req.has("custom")){
				custom=req.getString("custom");
			}
			System.out.println("req "+request);
			System.out.println("group "+group);
			System.out.println("sort "+sort);
			System.out.println("filter "+filter);

			
			String filterfield="";
			String operator="";
			String value="";
			String flquery="";
			if(custom.length()>0){
				flquery=custom;
			}
			if(req.has("isspecial")){
				isspecial=req.getString("isspecial");
			}
			if(filter!=null){
				
				JSONObject fltr= filter;		
				
				String logic=fltr.getString("logic");
				//String filters=fltr.getString("filters");
			
				JSONArray arr= fltr.getJSONArray("filters");
				for(int i=0; i<arr.length();i++){
					String str=arr.get(i).toString();
					JSONObject srt= new JSONObject(str);
					if(srt.isNull("field")){
						filterfield="";	
					}
					else{
						filterfield=srt.getString("field");	
					}
					if(srt.isNull("operator")){
						operator="";
					}
					else{
						operator=srt.getString("operator");
					}
					if(srt.isNull("value")){
						value="";
					}
					else{						
						value=String.valueOf(srt.get("value")).toLowerCase();
					}
					if(i>0){
						
						switch(operator){
							case "startswith":flquery=flquery+  " "+logic+" lower("+filterfield+ ") LIKE '"+value+"%'"; break;
							case "contains":flquery=flquery+  " "+logic+" lower("+filterfield+ ") LIKE '%"+value+"%'"; break;
							case "doesnotcontain":flquery=flquery+  " "+logic+" lower("+filterfield+ ") NOT LIKE '%"+value+"%'"; break;
							case "endswith":flquery=flquery+  " "+logic+" lower("+filterfield+ ") LIKE '%"+value+"'"; break;
							case "neq":flquery=flquery+  " "+logic+" lower("+filterfield+ ") != '"+value+"'"; break;
							case "eq":flquery=flquery+  " "+logic+" lower("+filterfield+ ") = '"+value+"'"; break;
							case "gte":flquery=flquery+  " "+logic+" lower("+filterfield+ ") >= '"+value+"'"; break;
						}						
					}
					else{
						if(custom.length()>0){
							if(custom.contains("orgid")){
								switch(operator){
									case "startswith":flquery=" Where lower("+filterfield+ ") LIKE '"+value+"%'"; break;
									case "contains":flquery=" Where lower("+filterfield+ ") LIKE '%"+value+"%'"; break;
									case "doesnotcontain":flquery=" Where lower("+filterfield+ ") NOT LIKE '%"+value+"%'"; break;
									case "endswith":flquery=" Where lower("+filterfield+ ") LIKE '%"+value+"'"; break;
									case "neq":flquery=" Where lower("+filterfield+ ") != '"+value+"'"; break;
									case "eq":flquery=" Where lower("+filterfield+ ") = '"+value+"'"; break;
									case "gte":flquery=" Where lower("+filterfield+ ") >= '"+value+"'"; break;
								}								
							}
							else{
								switch(operator){
									case "startswith":flquery=" "+custom+" and lower("+filterfield+ ") LIKE '"+value+"%'"; break;
									case "contains":flquery=" "+custom+" and lower("+filterfield+ ") LIKE '%"+value+"%'"; break;
									case "doesnotcontain":flquery=" "+custom+" and lower("+filterfield+ ") NOT LIKE '%"+value+"%'"; break;
									case "endswith":flquery=" "+custom+" and lower("+filterfield+ ") LIKE '%"+value+"'"; break;
									case "neq":flquery=" "+custom+" and lower("+filterfield+ ") != '"+value+"'"; break;
									case "eq":flquery=" "+custom+" and lower("+filterfield+ ") = '"+value+"'"; break;
									case "gte":flquery=" "+custom+" and lower("+filterfield+ ") >= '"+value+"'"; break;
								}
							}							
						}
						else{
							switch(operator){
								case "startswith":flquery=" Where lower("+filterfield+ ") LIKE '"+value+"%'"; break;
								case "contains":flquery=" Where lower("+filterfield+ ") LIKE '%"+value+"%'"; break;
								case "doesnotcontain":flquery=" Where lower("+filterfield+ ") NOT LIKE '%"+value+"%'"; break;
								case "endswith":flquery=" Where lower("+filterfield+ ") LIKE '%"+value+"'"; break;
								case "neq":flquery=" Where lower("+filterfield+ ") != '"+value+"'"; break;
								case "eq":flquery=" Where lower("+filterfield+ ") = '"+value+"'"; break;
								case "gte":flquery=" Where lower("+filterfield+ ") >= '"+value+"'"; break;
							}
						}						
					}					
				}				
			}

			
			
			if(!req.has("customPsize")){
				String query="select count(*) from "+tablename+" t "+flquery+" ";
				Query hql = sessionFactory.getCurrentSession().createQuery(query);
		    	int count = Integer.parseInt(hql.list().get(0).toString());			
				return count;
			}
			else {
				String query="from "+tablename+" t "+flquery+" ";
				Query  nquery= sessionFactory.getCurrentSession().createSQLQuery(isspecial);
				nquery.setMaxResults(req.getInt("customPsize"));
				
				List<Object> nlist=nquery.list();
				
				return nlist.size();
			}			
		}
		catch(Exception e){
			e.printStackTrace();
			return 0;
		}
	}
	
	public Object getNativeSQLResult(String queryStr, String type){
				
		try{
			if("insert".equals(type)){
				Query query=sessionFactory.getCurrentSession().createSQLQuery(queryStr);
				int numberOfRowsAffected = query.executeUpdate();
				try{
					return true;
				}
				catch(Exception e){
					e.printStackTrace();
					return null;
				}
			}
			else if("delete".equals(type)){
				Query query=sessionFactory.getCurrentSession().createSQLQuery(queryStr);
				int numberOfRowsAffected = query.executeUpdate();
				try{
					return true;
				}
				catch(Exception e){
					e.printStackTrace();
					return null;
				}
			}
			else {
				return null;
			}
			
		}
		catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	
	@Override
	public Object PeaceCrud(Object obj, String domainname, String method, Long obj_id, int page_val, int maxresult,
			String whereclause) {
		try{
    		Query query=null;
    		if("save".equals(method)){
    			try{
    				sessionFactory.getCurrentSession().saveOrUpdate(obj);
    				System.out.println(query);
    				return true;
    			}
    			catch (ConstraintViolationException aldaa){
    				aldaa.printStackTrace();
    				
    				return false;
    			}
    			
        	}
    		else if("update".equals(method)){
    			try{
    				
    				sessionFactory.getCurrentSession().update(obj);
    				System.out.println(query);
    				//sessionFactory.getCurrentSession().flush();
					//session.getCurrentSession().clear();
					
					
    				return true;
    			}
    			catch (ConstraintViolationException aldaa){
    				System.out.println("ooou laitai");
    				aldaa.printStackTrace();
    				
    				return false;
    			}
    		}
        	else if ("delete".equals(method)){
        		
    			if(whereclause!=null){
        			
        			query= sessionFactory.getCurrentSession().createQuery("delete from "+domainname+"  dname where dname."+whereclause+"=:obj_id");
        			query.setParameter("obj_id", obj_id);
        			System.out.println(query);
        		}
        		else{
        			query= sessionFactory.getCurrentSession().createQuery("delete from "+domainname+"  dname where dname.id=:obj_id");
            		query.setParameter("obj_id", obj_id);
            		System.out.println(query);
        		}
    			//List list = query.list();
        		int qresult = query.executeUpdate();
        	}
        	else if ("multidelete".equals(method)){
        		
    			if(whereclause!=null){        			
        			query= sessionFactory.getCurrentSession().createQuery("delete from "+domainname + " " + whereclause);
        		}
        		else{
        			query= sessionFactory.getCurrentSession().createQuery("delete from "+domainname+"  dname where dname.id=:obj_id");
            		query.setParameter("obj_id", obj_id);
        		}
        		int qresult = query.executeUpdate();
        	}
        	else if("list".equals(method)){
        		if(whereclause!=null){
        			
        			query = sessionFactory.getCurrentSession().createQuery(whereclause);
        			System.out.println(query);
        		}
        		else{
        			query = sessionFactory.getCurrentSession().createQuery("from "+domainname+" objlist  order by objlist.id desc ");
        			System.out.println(query);
        		}
        		int pval = page_val-1;
        		query.setFirstResult(maxresult*pval);
        		query.setMaxResults(maxresult);
        		
        		List<Object> robj = query.list();
        		query = null;
        	//	sessionFactory.getCurrentSession().flush();
        		//session.getCurrentSession().clear();
        		return robj;
        	}
        	else if("current".equals(method)){
        		query=sessionFactory.getCurrentSession().createQuery("from "+domainname+" t where t.id=:obj_id");
        		query.setParameter("obj_id", obj_id);
        		Object robj = query.list().get(0);
        		System.out.println(query);
        		return robj;
        		
        	}
        	else if("calculatepage".equals(method)){
        		if(whereclause==null){
        			int resultInt =  ((Long)sessionFactory.getCurrentSession().createQuery("select count(*) from "+domainname+"").uniqueResult()).intValue();
            		System.out.println("ene bol niit bichlegiin too "+resultInt);
            		
            		sessionFactory.getCurrentSession().flush();
            		if(resultInt%maxresult==0){
            			return resultInt/maxresult;
            		}
            		else{
            			return resultInt/maxresult+1;
            		}
        		}
        		else{
        			int resultInt =  sessionFactory.getCurrentSession().createQuery(whereclause).list().size();
            		System.out.println("ene bol niit bichlegiin too "+resultInt);
            		query=null;
            		sessionFactory.getCurrentSession().flush();
            		if(resultInt%maxresult==0){
            			return resultInt/maxresult;
            		}
            		else{
            			return resultInt/maxresult+1;
            		}
        		}	
        	}
        	else if ("countrecord".equalsIgnoreCase(method)){
        		if(whereclause==null){
        			long resultInt =  ((Long)sessionFactory.getCurrentSession().createQuery("select count(*) from "+domainname+"").uniqueResult()).intValue();
            		System.out.println("ene bol niit bichlegiin too "+resultInt);
            		query=null;
            		sessionFactory.getCurrentSession().flush();
            		return resultInt;
        		}
        		else{
        			long resultInt =  ((Long) sessionFactory.getCurrentSession().createQuery(whereclause).uniqueResult()).intValue();
            		System.out.println("ene bol niit bichlegiin too "+resultInt);
            		
            		sessionFactory.getCurrentSession().flush();
            		//session.getCurrentSession().clear();
            		return resultInt;
        		}
        	}
    		
    		return 1;		
    	}
    	catch (Exception e){
    		e.printStackTrace();
    		return 0;
    	}
	}



	@Override
	public List<?> jData(Integer pageSize, Integer skip, String sortColumn, String sortColumnDir, String searchStr,
			String domain) {
		
	
		
		String order="order by "+sortColumn+" "+sortColumnDir+"";
		String query="";
		if(domain==null){
			query=searchStr;
		}
		else{
			query="from "+domain+"  "+searchStr+"  "+order+"";
		}
		
		Query hql = sessionFactory.getCurrentSession().createQuery(query);
		hql.setFirstResult(skip);
		hql.setMaxResults(pageSize);
		List<Object> rlist = hql.list();
		
		return rlist;
	}

}