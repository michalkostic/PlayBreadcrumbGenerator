package com.plumgine.play;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.type.TypeReference;

import play.mvc.ActionInvoker;
import play.mvc.Before;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Router;
import play.mvc.Http.Header;
import play.mvc.Util;

public class BreadcrumbGenerator extends Controller {

	public static class BreadcrumbObj {
		public String value;
		public int level;
		public boolean replacePreviousAtLevel;
		public String url;		
		public String absoluteUrl;
		public static BreadcrumbObj fromBreadcrumb(Breadcrumb annotation) {
			BreadcrumbObj obj = new BreadcrumbObj();
			obj.value = annotation.value();
			obj.level = annotation.level();
			obj.replacePreviousAtLevel = annotation.replacePreviousAtLevel();
			return obj;
		}
		
		@Override
		public boolean equals(Object arg0) {			
			if (arg0!=null && this.value!=null && arg0 instanceof BreadcrumbObj) {				
				return this.value.equals(((BreadcrumbObj)arg0).value);
			} else {
				return super.equals(arg0);	
			}			
		}
		
		@Override
		public int hashCode() {
			if (this.value!=null) {
				return this.value.hashCode();
			} else {
				return super.hashCode();	
			}			
		}
	}
	
	public static boolean ignoreNext = false;
	
	protected static void updateBreadcrumbs(ArrayList<BreadcrumbObj> breadcrumbs, Breadcrumb annotation) {
		BreadcrumbObj currBreadcrumb = BreadcrumbObj.fromBreadcrumb(annotation);
		ignoreNext = annotation.ignoreNext();
		currBreadcrumb.url = request.url;
		currBreadcrumb.absoluteUrl = "http://" + request.host + request.url;
	
		if (currBreadcrumb.level==0) {
			breadcrumbs.clear();
			breadcrumbs.add(currBreadcrumb);
		} else {
			int indexOfBreadcrumb = breadcrumbs.indexOf(currBreadcrumb); 
			if (indexOfBreadcrumb>=0) {
				shortenToIndex(breadcrumbs, indexOfBreadcrumb);
			} else if (currBreadcrumb.replacePreviousAtLevel) {
				String callingUrl = getCallingPageUrl();
				int urlIndex = getUrlIndex(callingUrl); 
				if (urlIndex>=0) {
					shortenToIndex(breadcrumbs, urlIndex);
				}
				if (breadcrumbs.size() > 1) {
					breadcrumbs.remove(breadcrumbs.size() - 1);
				}
				breadcrumbs.add(currBreadcrumb);
			} else {
				String callingUrl = getCallingPageUrl();
				int urlIndex = getUrlIndex(callingUrl); 
				if (urlIndex>=0) {
					shortenToIndex(breadcrumbs, urlIndex);
				}
				breadcrumbs.add(currBreadcrumb);
			}			
		}
	}
	
	@Before
	public static void beforeRequest() {
		ArrayList<BreadcrumbObj> breadcrumbs = getBreadcrumbs();
		Breadcrumb annotation = getActionAnnotation(Breadcrumb.class);

		if (ignoreNext) {
			ignoreNext = false;
			renderArgs.put(RENDERARGS_BREADCRUMBS, breadcrumbs);
			return;
		}
		
		if (annotation==null) {
			// do nothing			
		} else {
			updateBreadcrumbs(breadcrumbs, annotation);
		}
		setBreadcrumbs(breadcrumbs);
		renderArgs.put(RENDERARGS_BREADCRUMBS, breadcrumbs);
	}
	
	static String RENDERARGS_BREADCRUMBS = "breadcrumbs";
	static String SESSION_BREADCRUMBS = "BreadcrumbGenerator.breadcrumbs";
	
	@Util
	public static ArrayList<BreadcrumbObj> getBreadcrumbs() {
		
		String breadcrumbStr = session.get(SESSION_BREADCRUMBS);
		ArrayList<BreadcrumbObj> breadcrumbs = null;
		if (breadcrumbStr==null || "".equals(breadcrumbStr)) {
			breadcrumbs = new ArrayList<BreadcrumbObj>();
		} else {
			breadcrumbs = com.plumgine.core.Util.jsonParseCollection(
													new TypeReference<ArrayList<BreadcrumbObj>>() {},
													breadcrumbStr);			
		}
		
		return breadcrumbs;
	}
	
	@Util
	public static BreadcrumbObj getBackTarget() {
		ArrayList<BreadcrumbObj> breadcrumbs = getBreadcrumbs();
		if (breadcrumbs.size()>=2) {
			return breadcrumbs.get(breadcrumbs.size()-2);	
		} else {
			return null;
		}
		
	}
	
	@Util
	public static void setBreadcrumbs(ArrayList<BreadcrumbObj> breadcrumbs)
	{
		String breadcrumbStr = com.plumgine.core.Util.jsonStringify(breadcrumbs);
		session.put(SESSION_BREADCRUMBS, breadcrumbStr);
	}
	
	@Util
	public static Method getActionMethod() 
	{
		String callingUrl = getCallingPageUrl();
		Map<String, String> actionMap = Router.route("GET", callingUrl);
		String action = actionMap.get("action");
		if (action==null) { return null;}
		Method actionMethod = (Method) ActionInvoker.getActionMethod(action)[1];
		return actionMethod;
	}
	
	@Util
	public static String getCallingPageUrl() 
	{
		// Redirecting to the calling page, because this action can be called from different pages 
		    try{
		    	
		      Header refererHeader = Http.Request.current().headers.get("referer"); 
		      if(refererHeader != null){ 
		        List<String> refererList = refererHeader.values; 
		        if(refererList != null){ 
		          String callingPageURL = refererList.get(0); 
		          if(callingPageURL != null && callingPageURL.length()>0){ 
		            //redirect(new URL(callingPageURL).getFile());
		        	  return callingPageURL;
		          } 
		        } 
		      } 
		    } catch (Exception e) { // MalformedURLException and any other 
		      e.printStackTrace(); 
		    } 
		    return null; 		     
	}
	
	@Util
	public static int getUrlIndex(String url) {
		List<BreadcrumbObj> breadcrumbs = getBreadcrumbs();
		int index = 0;
		for (BreadcrumbObj breadcrumbObj : breadcrumbs) {			
			if (breadcrumbObj.absoluteUrl.equals(url)) {
				return index;
			}
			index++;
		}
		return -1;
	}
	
	@Util
	public static <T> void shortenToIndex(List<T> list, int index) {
		while (list.size()>index+1){
			list.remove(list.size()-1);
		}		
	}

}
