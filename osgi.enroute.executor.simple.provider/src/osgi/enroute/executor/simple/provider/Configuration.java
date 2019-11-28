package osgi.enroute.executor.simple.provider;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(description = "Configuration for the enRoute::Executor")
public @interface Configuration {
	
	@AttributeDefinition(description = "The minimum number of threads allocated to this pool", required=false)
	int coreSize() default 20;

	@AttributeDefinition(description = "Maximum number of threads allocated to this pool", required=false)
	int maximumPoolSize() default 0;

	@AttributeDefinition(description = "Nr of seconds an idle free thread should survive before being destroyed",required=false)
	long keepAliveTime() default 60;
	
	@AttributeDefinition(description= "Ranking", defaultValue="-1000", required=false)
	long service_ranking() default -1000L; 
}
