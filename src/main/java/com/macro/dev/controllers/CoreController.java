package com.macro.dev.controllers;

import java.io.File;
import java.io.IOException;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.macro.dev.dao.UserDao;
import com.macro.dev.repositories.LnkMenuRepository;
import com.macro.dev.repositories.UserRepository;
import com.macro.dev.service.Services;
import com.macro.dev.service.SmtpMailSender;
import com.macro.dev.service.UserService;
import org.dom4j.DocumentException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.macro.dev.models.DataSourceResult;
import com.macro.dev.models.LnkMenurole;
import com.macro.dev.models.LutMenu;
import com.macro.dev.models.LutRole;
import com.macro.dev.models.LutUser;
@RestController
@RequestMapping("/core")
public class CoreController {
	
	@Autowired
	private SmtpMailSender smtpMailSender;
	
	@Autowired
    private UserDao dao;
	
	@Autowired
	Services services;

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private LnkMenuRepository lpo;

	@Autowired
	private UserService userService;


	BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	
	@RequestMapping(value = "/api/{domain}/{id}", method = RequestMethod.GET, produces={"application/json; charset=UTF-8"})
    public @ResponseBody String tree(@PathVariable String domain,@PathVariable long id) {
	try{

		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();  
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		JSONArray arr=new JSONArray();
		if (!(auth instanceof AnonymousAuthenticationToken)) {
			
			if(domain.equalsIgnoreCase("LutMenu")){
				List<LutMenu> aw=  (List<LutMenu>) dao.getHQLResult("from LutMenu t where t.id='"+id+"'", "list");
				 for(int i=0; i<aw.size();i++){
					 JSONObject obj=new JSONObject();   
					 obj.put("id", aw.get(i).getId());
					 obj.put("menuname",  aw.get(i).getMenuname());
					 obj.put("stateurl",  aw.get(i).getStateurl());
					 obj.put("uicon",  aw.get(i).getUicon());
					 obj.put("parentid",  aw.get(i).getParentid());
					 obj.put("orderid",  aw.get(i).getOrderid());
					 
					 arr.put(obj);
				 }	
			}


		}
        return arr.toString();
		}
		catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	@RequestMapping(value = "/list/{domain}", method= RequestMethod.POST)
    public @ResponseBody DataSourceResult customers(@PathVariable String domain, @RequestBody String request, HttpServletRequest req) throws HttpRequestMethodNotSupportedException {
		Long count=(long) 0;
		List<?> rs = null;
		UserDetails userDetail = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		LutUser loguser= (LutUser) dao.getHQLResult("from LutUser t where t.username='"+userDetail.getUsername()+"'", "current");

		DataSourceResult result = new DataSourceResult();	
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (!(auth instanceof AnonymousAuthenticationToken)) {
			
			if(domain.equalsIgnoreCase("LutMenu")){
				List<LutMenu> wrap = new ArrayList<LutMenu>();
				
				rs= dao.kendojson(request, domain);
				count=(long) dao.resulsetcount(request, domain);
								
				for(int i=0;i<rs.size();i++){
					LutMenu or=(LutMenu) rs.get(i);
					LutMenu cor=new LutMenu();
					cor.setId(or.getId());
					cor.setUicon(or.getUicon());
					cor.setIsactive(or.getIsactive());
					cor.setMenuname(or.getMenuname());
					cor.setStateurl(or.getStateurl());
					cor.setOrderid(or.getOrderid());
					wrap.add(cor);
				}
				ObjectMapper mapper = new ObjectMapper();
				mapper.setSerializationInclusion(Include.NON_NULL);
				//mapper.writeValueAsString(wmap);
				
				result.setData(rs);	
				result.setTotal(count);
			}

			else if(domain.equalsIgnoreCase("LutRole")){
				List<LutRole> wrap = new ArrayList<LutRole>();

				rs= dao.kendojson(request, domain);
				count=(long) dao.resulsetcount(request, domain);
				
				for(int i=0;i<rs.size();i++){
					LutRole or=(LutRole) rs.get(i);
					LutRole cor=new LutRole();
					cor.setId(or.getId());
					cor.setName(or.getName());
					cor.setAccessid(or.getAccessid());
					wrap.add(cor);
				}
				result.setData(wrap);	
				result.setTotal((long) count);
			}

			else{
				rs= dao.kendojson(request, domain);
				count=(long) dao.resulsetcount(request, domain);
				
			
				result.setData(rs);	
				result.setTotal((long) count);
			}
			return  result;
		}
		return null;
	}
	
    @RequestMapping(value = "/resource/{domain}/{id}", method = RequestMethod.GET, produces={"application/json; charset=UTF-8"})
    public @ResponseBody String treeReource(@PathVariable String domain,@PathVariable long id) {
	try{

		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();  
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		JSONArray arr=new JSONArray();
		if (!(auth instanceof AnonymousAuthenticationToken)) {
			


		}
        return arr.toString();
		}
		catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	@RequestMapping(value = "/resource/{domain}", method = RequestMethod.GET, produces={"application/json; charset=UTF-8"})
    public @ResponseBody String tree(@PathVariable String domain) {
		try{
				
			List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();  
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			JSONArray arr=new JSONArray();
			
			UserDetails userDetail = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			LutUser loguser= (LutUser) dao.getHQLResult("from LutUser t where t.username='"+userDetail.getUsername()+"'", "current");
			if (!(auth instanceof AnonymousAuthenticationToken)) {
				
				if(domain.equalsIgnoreCase("LutMenu")){
					 List<LutMenu> rs=(List<LutMenu>) dao.getHQLResult("from LutMenu t  order by t.orderid", "list");
					 for(int i=0;i<rs.size();i++){
						 	JSONObject obj=new JSONObject();      	
						 	obj.put("value", rs.get(i).getId());
						 	obj.put("text", rs.get(i).getMenuname());			        		
			        		arr.put(obj);        	
			        	}		
				}	
				else if(domain.equalsIgnoreCase("LutRole")){
						
					 if(loguser==null){
						 List<LutRole> rs=(List<LutRole>) dao.getHQLResult("from LutRole t  order by t.id", "list");
						 for(int i=0;i<rs.size();i++){
							 	JSONObject obj=new JSONObject();      	
							 	obj.put("value", rs.get(i).getId());
							 	obj.put("text", rs.get(i).getName());
							 	obj.put("id", rs.get(i).getId());
							 	obj.put("title", rs.get(i).getName());
				        		arr.put(obj);        	
				        	}
					 }
					 else{
						 List<LutRole> rs=(List<LutRole>) dao.getHQLResult("from LutRole t  order by t.id", "list");
						 for(int i=0;i<rs.size();i++){
							 	JSONObject obj=new JSONObject();      	
							 	obj.put("value", rs.get(i).getId());
							 	obj.put("text", rs.get(i).getName());
							 	obj.put("id", rs.get(i).getId());
							 	obj.put("title", rs.get(i).getName());
				        		arr.put(obj);        	
				        	}
					 }
				}

			}		    	
	        return arr.toString();
		}
		catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	 
   	@RequestMapping(value="/parentmenus",method=RequestMethod.GET)
   	public @ResponseBody String parentmenus(HttpServletRequest req) throws ClassNotFoundException, JSONException{
    	//List<LutMenu> rel=(List<LutMenu>) dao.getHQLResult("from LutMenu t where t.stateurl!='#'", "list");
    	List<LutMenu> rel=(List<LutMenu>) dao.getHQLResult("from LutMenu t where t.parentid is null order by t.orderid ", "list");
			JSONArray arr= new JSONArray();
			for(int i=0;i<rel.size();i++){
	       		JSONObject fistList= new JSONObject();           		
	       		fistList.put("id", rel.get(i).getId());
	       		fistList.put("title", rel.get(i).getMenuname());
	       		fistList.put("text", rel.get(i).getMenuname());
	       		fistList.put("value", rel.get(i).getId());     
	       		fistList.put("parent_id", rel.get(i).getParentid());   
        		
	       		arr.put(fistList);           
	       		
        		if(rel.get(i).getLutMenus().size()>0){
        			
        			List<LutMenu> chi=rel.get(i).getLutMenus();
    				
        			for(int j=0;j<chi.size();j++){
        				LutMenu rs=chi.get(j);
        				JSONObject fistList1= new JSONObject();      
        				fistList1.put("id", rs.getId());
        				fistList1.put("title", rs.getMenuname());
        				fistList1.put("text", rel.get(i).getMenuname());
        				fistList1.put("value",rs.getId());     
        				fistList1.put("parent_id", rs.getParentid());   
        	       		arr.put(fistList1);   
        	       		

            			if(rs.getLutMenus().size()>0){
            				for(int c=0;c<rs.getLutMenus().size();c++){
                				LutMenu rc=rs.getLutMenus().get(c);
                				JSONObject fistList2= new JSONObject();      
                				fistList2.put("id", rc.getId());
                				fistList2.put("title", rc.getMenuname());
                				fistList2.put("text", rc.getMenuname());
                				fistList2.put("value",rc.getId());     
                				fistList2.put("parent_id", rc.getParentid());   
                				arr.put(fistList2);   
                	       		
                			}
            			}
        			}
        			
        		}
        		
	       					
	       	}	
			JSONObject wmap= new JSONObject();     
			wmap.put("options", arr);
					
	        return wmap.toString();
   	}
   	
   	
    @ResponseBody 
 	@RequestMapping(value="/action/read/{domain}/{id}",method=RequestMethod.GET)
 	public Object read(@PathVariable long id, @PathVariable String domain, HttpServletRequest req) throws ClassNotFoundException, JSONException{
 		try {
 			List<LnkMenurole> rel=lpo.findById(id);
 			List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
 			for(int i=0;i<rel.size();i++){
         		Map<String,Object> wmap=new HashMap<String, Object>();  
         		wmap.put("menuid", rel.get(i).getLutMenu().getId());
         		wmap.put("roleid", rel.get(i).getLutRole().getId());
         		wmap.put("create", rel.get(i).getRcreate());
         		wmap.put("read", rel.get(i).getRread());
         		wmap.put("update", rel.get(i).getRupdate());
         		wmap.put("delete", rel.get(i).getRdelete());
         		wmap.put("export", rel.get(i).getRexport());      		
         		result.add(wmap);        	
         	}	
 			ObjectMapper mapper = new ObjectMapper();
 	        return mapper.writeValueAsString(result);
 		}
 		catch (Exception e) {
 			e.printStackTrace();
 		}
 		return null;
 	}
   	
	@RequestMapping(value="/rolesubmit",method = RequestMethod.PUT, produces={"application/json; charset=UTF-8"})
	public @ResponseBody String ajaxsubmit(@RequestBody String jsonString) throws JSONException{
		 System.out.println(jsonString);
		 JSONArray rs= new JSONArray(jsonString);
		 
		 for(int i=0; i<rs.length();i++){
			 String str=rs.get(i).toString();
			 JSONObject batch= new JSONObject(str);  
			 
			 String roleauth=batch.getString("roleauth");
			 String rolename=batch.getString("rolename");
			 int access=batch.getInt("accessid");
			 int roleid=batch.getInt("roleid");
			 System.out.println("sss"+roleid);
			 if(roleid==0){
				 LutRole object = new LutRole();
		    	 object.setName(rolename);
		    	 object.setAccessid(access);   	
		    	 dao.PeaceCrud(object, "Category", "save", (long) 0, 0, 0, null);
				 
		    	 
				 JSONArray mn= (JSONArray) batch.get("ilist");	
				 
				 if(mn.length()>0){
					 for(int j=0;j<mn.length();j++){
						 JSONObject itr = mn.getJSONObject(j);
	    				 int menuid=Integer.parseInt(itr.get("menuid").toString());
	    				System.out.println("menuid"+menuid);
	    				 int create=0;
	    				 int read=0;    				 
	    				 int update=0;
	    				 int destroy=0;
	    				 int export=0;
	    				 JSONArray ids= (JSONArray) itr.get("ids"); 
	    				 if(ids.length()>0){
	    					 for(int c=0;c<ids.length();c++){ 
	    						 int r= (int) ids.get(c);
	    						 switch (r) {
	    				            case 1:  create = 1;
	    				                     break;
	    				            case 2:  read = 1;
	    				                     break;
	    				            case 3:  update = 1;
	    				                     break;
	    				            case 4:  destroy = 1;
	    				                     break;
	    				            case 5:  export = 1;
	    				                     break;
	    				        }    					
	    					 }
	    				 } 				
	    				LutMenu current = (LutMenu)dao.PeaceCrud(null, "LutMenu", "current", (long) menuid, 0, 0, "");
	     					    				
	    				LnkMenurole rmenu = new LnkMenurole();
	     				rmenu.setMenuid(rmenu.getId());
	     				rmenu.setRoleid(object.getId());
	     				rmenu.setRcreate(create);
	     				rmenu.setRread(read);
	     				rmenu.setRupdate(update);
	     				rmenu.setRdelete(destroy);
	     				rmenu.setRexport(export);    
	     				rmenu.setOrderid(current.getOrderid());
	     				dao.PeaceCrud(rmenu, "Category", "save", (long) 0, 0, 0, null);
	    				
					 }
					 
				 }
				 return "true";
			 }
			 else{
				 LutRole object1 = (LutRole) dao.getHQLResult("from LutRole t where t.id="+roleid+"", "current");
		    	 object1.setName(rolename);
		    	 object1.setAccessid(access);      	
		    	 dao.PeaceCrud(object1, "Category", "update", (long) roleid, 0, 0, null);
				 
		    	 
		    	 dao.PeaceCrud(null, "LnkMenurole", "delete", (long) roleid, 0, 0, "roleid");
		    	 
				 JSONArray mn= (JSONArray) batch.get("ilist");	
				 
				 if(mn.length()>0){
					 for(int j=0;j<mn.length();j++){
						 JSONObject itr = mn.getJSONObject(j);
	    				 int menuid=Integer.parseInt(itr.get("menuid").toString());
	    				 System.out.println("menuid"+menuid);
	    				 int create=0;
	    				 int read=0;    				 
	    				 int update=0;
	    				 int destroy=0;
	    				 int export=0;
	    				 JSONArray ids= (JSONArray) itr.get("ids"); 
	    				 if(ids.length()>0){
	    					 for(int c=0;c<ids.length();c++){ 
	    						 int r= (int) ids.get(c);
	    						 switch (r) {
	    				            case 1:  create = 1;
	    				                     break;
	    				            case 2:  read = 1;
	    				                     break;
	    				            case 3:  update = 1;
	    				                     break;
	    				            case 4:  destroy = 1;
	    				                     break;
	    				            case 5:  export = 1;
	    				                     break;
	    				        }    					
	    					 }
	    				 } 	
	    				LutMenu mnu = (LutMenu) dao.getHQLResult("from LutMenu t where t.id="+menuid+"", "current");	    				
	    				LnkMenurole rmenu=new LnkMenurole(); 
	    				rmenu.setMenuid(mnu.getId());
	     				rmenu.setRoleid(object1.getId());
	     				rmenu.setRcreate(create);
	     				rmenu.setRread(read);
	     				rmenu.setRupdate(update);
	     				rmenu.setRdelete(destroy);
	     				rmenu.setRexport(export); 
	     				rmenu.setOrderid(mnu.getOrderid());
	     				dao.PeaceCrud(rmenu, "Category", "save", (long) 0, 0, 0, null);

					 }
					 return "true";
				 }
			 }
			
			
		 }
		 
		 return "true";
		    
   
    }
	
	@RequestMapping(value = "/{action}/{domain}", method=RequestMethod.POST)
    public @ResponseBody String update(Model model,@RequestBody String jsonString, @PathVariable String action,@PathVariable String domain) throws JSONException,ClassCastException{
       System.out.println("json STR "+jsonString);	
       try{
    	   Class<?> classtoConvert;
		   JSONObject obj = new JSONObject(jsonString);    		
		   JSONObject resp= new JSONObject();
		   
		   String domainName=domain;
		   System.out.println(domain);
		   classtoConvert=Class.forName(domain);
		   Gson gson = new Gson();
		   Object object = gson.fromJson(obj.toString(),classtoConvert);	
		   
		   if(action.equals("update")){
			  
			   if(!obj.has("models")){
				   
				   if(domainName.equalsIgnoreCase("com.netgloo.models.LutUser")){
				    	  JSONObject str= new JSONObject(jsonString);
				    	  
				    	   dao.PeaceCrud(null, "LnkUserrole", "delete", (long) str.getLong("id"), 0, 0, "userid");
				    	   
						   LutUser cr= (LutUser) dao.getHQLResult("from LutUser t where t.id='"+str.getInt("id")+"'", "current");	
				           //cr.setLutDepartment(lutDepartment);(str.getLong("lpid"));
						   cr.setFamilyname(str.getString("familyname"));
						   cr.setGivenname(str.getString("givenname"));
						   cr.setEmail(str.getString("email"));
						   cr.setMobile(str.getString("mobile"));
						   cr.setUsername(str.getString("username"));
						   if(cr.getPassword().equalsIgnoreCase(str.getString("password"))){
							   cr.setPassword(str.getString("password"));
						   }
						   else{
							   cr.setPassword(passwordEncoder.encode(str.getString("password")));
						   }
						   
						   cr.setIsactive(str.getBoolean("isactive"));
						   dao.PeaceCrud(cr, domainName, "update", str.getLong("id"), 0, 0, null);
						   
						 
						   
						  /* LnkUserrole rl= new LnkUserrole();
						   rl.setRoleid(str.getLong("roleid"));
						   rl.setUserid(cr.getId());
						   dao.PeaceCrud(rl, "CLnkUserRole", "save", (long) 0, 0, 0, null);*/
				    	  
				    	  						  
				   }				   
				   else{
				    	  int id=(int)obj.getInt("id");
						  dao.PeaceCrud(object, domainName, "update", (long) id, 0, 0, null);
				   }			 
			   }  
			   
			   else{
				   JSONArray rs=(JSONArray) obj.get("models");
				   System.out.println("rs obj "+rs);
				   for(int i=0;i<rs.length();i++){
					   String str=rs.get(i).toString();
					   JSONObject batchobj= new JSONObject(str);  
					   Object bobj = gson.fromJson(batchobj.toString(),classtoConvert);
					   int upid=batchobj.getInt("id");					   
					   dao.PeaceCrud(bobj, domainName, "update", (long) upid, 0, 0, null); 					  
				   }
				  
			   }
			   if(domainName.equalsIgnoreCase("com.netgloo.models.LutDepartment")){
			
			   }
		   }
		   else if(action.equals("delete")){
			   
			   if(domainName.equalsIgnoreCase("com.netgloo.models.LutDepartment")){
				 			   			   
			   }
			   else{
				   dao.PeaceCrud(object, domainName, "delete", obj.getLong("id"), 0, 0, null);	
			   }
			   		  
		   }
		   else if(action.equals("create")){
			   
			   if(domainName.equalsIgnoreCase("com.netgloo.models.LutUser")){
				   
			    	  JSONObject str= new JSONObject(jsonString);
			    	  
			    	   LutUser cr= new LutUser();			    	   
					   cr.setFamilyname(str.getString("familyname"));
					   cr.setGivenname(str.getString("givenname"));
					   cr.setEmail(str.getString("email"));
					   cr.setMobile(str.getString("mobile"));
					   cr.setUsername(str.getString("username"));
									   
					   cr.setPassword(passwordEncoder.encode(str.getString("password")));
					   cr.setIsactive(str.getBoolean("isactive"));
					   dao.PeaceCrud(cr, domainName, "save", (long) 0, 0, 0, null);
					   
					  /* LnkUserrole rl= new LnkUserrole();
					   rl.setRoleid(str.getLong("roleid"));
					   rl.setUserid(cr.getId());
					   dao.PeaceCrud(rl, "LnkUserRole", "save", (long) 0, 0, 0, null);*/
			    	  
			    	  						  
			   }

			    else{
			    	dao.PeaceCrud(object, domainName, "save", (long) 0, 0, 0, null);			   
			    }
			   
		   }		  
		   return "true";
       }
       catch(Exception  e){
    	   e.printStackTrace();
    		return null;
       }
      
    }
	
	@RequestMapping(value="/core/defaultSuccess",method=RequestMethod.GET)
	public String defaultSuccess (HttpServletRequest req,HttpServletResponse res){
		try{
			req.setCharacterEncoding("utf-8");
			UserDetails userDetail = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			JSONObject js = new JSONObject();		
			Collection<?> coll=userDetail.getAuthorities();
			Iterator<?> itr=coll.iterator();
			while(itr.hasNext()){
				String rolename = itr.next().toString();
				
				System.out.println("uj"+rolename);
				
				//String returnS=dao.loginedUserViewAuthority(userDetail, id);				
				if("ROLE_SUPER".equals(rolename)){  
					js.put("url", "restricted.dashboard");
				}
				else{
					
					LutUser loguser= (LutUser) dao.getHQLResult("from LutUser t where t.username='"+userDetail.getUsername()+"'", "current");
					
					LutMenu mnu=(LutMenu) dao.getHQLResult("from LutMenu t where t.id='"+loguser.getRoles().get(0).getAccessid()+"'", "current");
					js.put("url", mnu.getStateurl());
					
				}
			}
		
			return  js.toString();
			
		}
		catch(Exception e){
			e.printStackTrace();
			return null;
		}	
	}
	
	
/*	@RequestMapping(value = "/tree/list/{domain}", method = RequestMethod.POST)
    public @ResponseBody List<?> read(@PathVariable String domain,@RequestBody String request) {
		if(domain.equalsIgnoreCase("LutForm")){
			List<LutForm> rs=fservice.getFormList();
	        return rs;
		}
		else if(domain.equalsIgnoreCase("LnkAuditForm")){
			System.out.println(request);
			 JSONObject obj= new JSONObject(request);
			List<LnkAuditForm> rs=(List<LnkAuditForm>) dao.getHQLResult("from LnkAuditForm t where t.appid="+obj.getInt("appid")+"", "list");
	        return rs;
		}
		else{
			return null;
		}
    }*/
	

 
	


    


	
	@RequestMapping(value="/ujson",method=RequestMethod.GET, produces={"application/json; charset=UTF-8"})
   	public @ResponseBody String ujson(HttpServletRequest req){
   		try{
   			//List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();  
   			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
   			if (!(auth instanceof AnonymousAuthenticationToken)) {
   				JSONObject result = new JSONObject();  
   				LutUser loguser= null;
   				UserDetails userDetail = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
				Collection<?> coll=userDetail.getAuthorities();
				Iterator<?> itr=coll.iterator();
				long userid = 0;
				String roles="";
				boolean rolesuper=false;
				loguser=userService.getUser(SecurityContextHolder.getContext().getAuthentication().getName());
				if(loguser==null){
					rolesuper=true;
				}
				
				if(rolesuper){
					result=services.getUjson(roles,true,loguser);
				}
				else{
					for(int i=0;i<loguser.getRoles().size();i++){
						roles=roles+","+loguser.getRoles().get(i).getId();
					}
					result=services.getUjson(roles,false,loguser);
				}
				System.out.println("done");
   		        return result.toString();
   			}
       	
   	    	/*ObjectMapper mapper = new ObjectMapper();
   	        return mapper.writeValueAsString(result);*/
   			return null;
   		}
   		catch(Exception e){
   			e.printStackTrace();
   			return null;
   		}
   	}
	
	@RequestMapping(value="/mjson",method=RequestMethod.GET, produces={"application/json; charset=UTF-8"})
	public @ResponseBody String mjson(HttpServletRequest req){
		try{
			
			JSONObject result = new JSONObject();
			LutUser loguser= null;
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			if (!(auth instanceof AnonymousAuthenticationToken)) {

				long userid = 0;
				String roles="";
				boolean rolesuper=false;
				loguser=userService.getUser(SecurityContextHolder.getContext().getAuthentication().getName());
			//	loguser=(LutUser) dao.getHQLResult("from LutUser t where t.username='"+SecurityContextHolder.getContext().getAuthentication().getName()+"'", "current");
				if(loguser==null){
					rolesuper=true;
				}
				if(rolesuper){
					result=services.getMjson(roles,true,loguser);
				}
				else{
					System.out.println(SecurityContextHolder.getContext().getAuthentication().getName());
					System.out.println(loguser.getUsername());
					for(int i=0;i<loguser.getRoles().size();i++){
						roles=roles+","+loguser.getRoles().get(i).getId();
					}
					result=services.getMjson(roles,false,loguser);
				}
				
				System.out.println("done");
				
			}
		  
	        return result.toString();
			
		}
		catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	
	@RequestMapping(value="/rjson/{id}/{path}",method=RequestMethod.GET, produces={"application/json; charset=UTF-8"})
   	public @ResponseBody String rjson(HttpServletRequest req, @PathVariable long id, @PathVariable String path){
   		try{
   			
   			List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();  
   			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
   				UserDetails userDetail = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
   				
   				Collection<?> coll=userDetail.getAuthorities();
   				Iterator<?> itr=coll.iterator();	   				
   				System.out.println("odoo end");
   				while(itr.hasNext()){
   					String rolename = itr.next().toString();
   						   					
   					//String returnS=dao.loginedUserViewAuthority(userDetail, id);				
   					if("ROLE_SUPER".equals(rolename)){ 
   						ObjectMapper mapper = new ObjectMapper();
			    		Map<String,Object> wmap=new HashMap<String, Object>();        	
		        		wmap.put("rcreate", 1);
		        		wmap.put("rupdate", 1);
		        		wmap.put("rdelete", 1);
		        		wmap.put("rread", 1);
		        		wmap.put("rexport", 1);
		        		result.add(wmap);        	
			   	        return mapper.writeValueAsString(wmap);
   					}
   					else{
   						LutUser loguser=(LutUser) dao.getHQLResult("from LutUser t where t.id='"+id+"'", "current");
							LutMenu lm=(LutMenu) dao.getHQLResult("from LutMenu t where t.stateurl='"+path+"'", "current");
		   				
							List<LutRole> us=loguser.getRoles();
							ObjectMapper mapper = new ObjectMapper();
			    		Map<String,Object> wmap=new HashMap<String, Object>();   
			    		
			    		JSONObject obj = new JSONObject();

			    	    obj.put("rcreate", 0);
			    	    obj.put("rupdate", 0);
			    	    obj.put("rdelete", 0);
			    	    obj.put("rread", 0);
			    	    obj.put("rexport", 0);
			    		
							for(int u=0;u<us.size();u++){
								System.out.println("@@"+us.get(u).getId());
								List<LnkMenurole> rs=(List<LnkMenurole>) dao.getHQLResult("from LnkMenurole t where t.roleid='"+us.get(u).getId()+"' and t.menuid="+lm.getId()+"", "list");
						    	if(rs.size()>0){   		
						    		if(rs.get(0).getRcreate()!=0){
						    			obj.remove("rcreate");
						    			obj.put("rcreate", rs.get(0).getRcreate());
						    		}
						    		if(rs.get(0).getRupdate()!=0){
						    			obj.remove("rupdate");
						    			obj.put("rupdate", rs.get(0).getRupdate());
						    		}
						    		if(rs.get(0).getRdelete()!=0){
						    			obj.remove("rdelete");
						    			obj.put("rdelete", rs.get(0).getRdelete());
						    		}
						    		if(rs.get(0).getRread()!=0){
						    			obj.remove("rread");
						    			obj.put("rread", rs.get(0).getRread());
						    		}
						    		if(rs.get(0).getRexport()!=0){
						    			obj.remove("rexport");
						    			obj.put("rexport", rs.get(0).getRexport());
						    		}   					        		
						    	}   
							}
							return obj.toString();
				    	//return mapper.writeValueAsString(wmap);
						}
					}
   				   	    	
   			
   		}
   		catch(Exception e){
   			e.printStackTrace();
   			return null;
   		}
		return path;
		
   	}
	    
	@RequestMapping(value="/useradd/{id}", method=RequestMethod.PUT)
	public @ResponseBody String ajaxuser(@RequestBody String jsonString) throws JSONException, MessagingException{
		JSONObject obj= new JSONObject(jsonString); 
		JSONObject result= new JSONObject();
		
		UserDetails userDetail = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		LutUser loguser= (LutUser) dao.getHQLResult("from LutUser t where t.username='"+userDetail.getUsername()+"'", "current");
		 
		//List<LutUser> orgs=(List<LutUser>) dao.getHQLResult("from LutUser t where t.orgcode='"+obj.getString("orgcode")+"'", "list");
		if(obj.getLong("id")==0){
			
			LutUser norg= new LutUser();				
			norg.setEmail(obj.getString("mail"));
			norg.setFamilyname(obj.getString("fname"));
			norg.setGivenname(obj.getString("gname"));
			norg.setUsername(obj.getString("uname"));
			norg.setMobile(obj.getString("phone"));
			norg.setPassword(passwordEncoder.encode(obj.getString("pass")));

		//	norg.setDepartmentid(obj.getLong("org"));
			norg.setIsactive(obj.getBoolean("isac"));			
				
			dao.PeaceCrud(norg, "LutUser", "save", (long) 0, 0, 0, null);
			
			JSONArray arr= obj.getJSONArray("roles");
			
		/*	for(int a=0;a<arr.length();a++){
					
				LnkUserrole rl= new LnkUserrole();			
			    rl.setRoleid(arr.getLong(a));
			    rl.setUserid(norg.getId());
			    dao.PeaceCrud(rl, "LnkUserRole", "save", (long) 0, 0, 0, null);			
				
			}	*/
			result.put("re", 0);
			
			
		}
		else{			
			LutUser norg = (LutUser) dao.getHQLResult("from LutUser t where t.id='"+obj.getLong("id")+"'", "current"); 		
			norg.setEmail(obj.getString("mail"));
			norg.setFamilyname(obj.getString("fname"));
			norg.setGivenname(obj.getString("gname"));
			norg.setUsername(obj.getString("uname"));
			norg.setMobile(obj.getString("phone"));
		    if(norg.getPassword().equalsIgnoreCase(obj.getString("pass"))){
			   norg.setPassword(obj.getString("pass"));
		    }
		    else{
			   norg.setPassword(passwordEncoder.encode(obj.getString("pass")));
		    }
			//norg.setDepartmentid(obj.getLong("org"));
			norg.setIsactive(obj.getBoolean("isac"));	
			dao.PeaceCrud(norg, "LutUser", "update", obj.getLong("id"), 0, 0, null);
			
			if(loguser!=null){
				if(obj.has("roles")){										
					dao.PeaceCrud(null, "LnkUserrole", "delete", (long) obj.getLong("id"), 0, 0, "userid");	    
					
					JSONArray arr= obj.getJSONArray("roles");
					
					if(arr.length()>0){						
						/*for(int a=0;a<arr.length();a++){
							LnkUserrole rusr=new LnkUserrole();	
							rusr.setRoleid(Long.parseLong(arr.getString(a)));
							rusr.setUserid(obj.getLong("id"));
							dao.PeaceCrud(rusr, "LnkUserrole", "save",  (long) 0, 0, 0, null);	
						}*/
					}
				}				
			}
			else{
				if(obj.has("roles")){		
										
					dao.PeaceCrud(null, "LnkUserrole", "delete", (long) obj.getLong("id"), 0, 0, "userid");	    
					
					JSONArray arr= obj.getJSONArray("roles");
					
					/*if(arr.length()>0){
						for(int a=0;a<arr.length();a++){					
							LnkUserrole rusr=new LnkUserrole();	
							rusr.setRoleid(Long.parseLong(arr.getString(a)));
							rusr.setUserid(obj.getLong("id"));
							dao.PeaceCrud(rusr, "LnkUserrole", "save",  (long) 0, 0, 0, null);	
					    		
							
						}
					}*/
				}		
			
			}			
			
			result.put("re", 1);
		}
		
		return result.toString(); 
		 
	 }
}
