package com.plumgine.serialization;

import org.codehaus.jackson.Version;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig.Feature;
import org.codehaus.jackson.map.module.SimpleModule;

import com.plumgine.core.Util;

public class JsonMappers {

	public static ObjectMapper defaultMapper = new ObjectMapper();
	public static ObjectMapper userIdOnly = new ObjectMapper();
	public static ObjectMapper processInstanceList = new ObjectMapper();
	public static ObjectMapper taskList = new ObjectMapper();
	public static ObjectMapper taskPlanList = new ObjectMapper();
	    
    public static void standardMapperConfigure(ObjectMapper mapper)
    {
    	mapper.configure(Feature.INDENT_OUTPUT, true);
    	// may hide some errors !
    	mapper.configure(Feature.FAIL_ON_EMPTY_BEANS, false);    	
    }

	static {
    	standardMapperConfigure(defaultMapper);

    	// not used for code sample brevity - just to get hang of what kind of configuration takes place here
//		//userIdOnly.getSerializationConfig().withView(JsonViews.IdOnly.class);
//		userIdOnly.getSerializationConfig().setSerializationView(JsonViews.IdOnly.class);
//		userIdOnly.getSerializationConfig().addMixInAnnotations(User.class, UserIdJacksonMixin.class);
//		userIdOnly.configure(Feature.DEFAULT_VIEW_INCLUSION, false);
//		standardMapperConfigure(userIdOnly);

				
	}
}
