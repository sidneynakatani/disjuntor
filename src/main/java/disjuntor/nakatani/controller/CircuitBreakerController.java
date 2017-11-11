package disjuntor.nakatani.controller;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;

@RestController
public class CircuitBreakerController {
	
	final static Logger logger = Logger.getLogger(CircuitBreakerController.class);
	
	@RequestMapping(value = "/callMyName/{name}", method = RequestMethod.GET)
	@HystrixCommand(fallbackMethod = "openCircuit" , commandProperties = {
	@HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "10"),
	@HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "6000"),
	})
	public String callMyName(@PathVariable("name") String name) {
		
		logger.info("Fail name: "+ name);
		
		if(name.equalsIgnoreCase("Error")){
			throw new RuntimeException("Braking flow...");
		}
		
		return "Your name is: "+ name;
	}
	
	public String openCircuit(String name){
		logger.info("Fail..");
		return "Fail";
	}

}
