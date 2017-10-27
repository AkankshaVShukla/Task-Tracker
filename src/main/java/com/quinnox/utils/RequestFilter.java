package com.quinnox.utils;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.quinnox.model.LoginHistory;

@Configuration
@ComponentScan
@EnableAutoConfiguration
@Component
public class RequestFilter implements Filter {

	public RequestFilter() {
		// TODO Auto-generated constructor stub
	}

	private @Autowired AutowireCapableBeanFactory beanFactory;
	private @Autowired MongoOperations operations;

    @Bean
    public FilterRegistrationBean myFilter() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        //Filter myFilter = new Filter();
        Filter securityFilter = new Filter() {
			
			@Override
			public void init(FilterConfig arg0) throws ServletException {
				// TODO Auto-generated method stub				
			}
			
			@Override
			public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
					throws IOException, ServletException {
				// TODO Auto-generated method stub
				filterChain.doFilter(request, response);
				return;
			}
			
			@Override
			public void destroy() {
				// TODO Auto-generated method stub
			}
		};
        beanFactory.autowireBean(securityFilter);
        registration.setFilter(securityFilter);
        //registration.addUrlPatterns("/login");
        return registration;
    }

	@Override
	public void destroy() {
		// TODO Auto-generated method stub		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
			throws IOException, ServletException {
		// TODO Auto-generated method stub

		 HttpServletRequest httpRequest = (HttpServletRequest) request;
		 
		 Cookie ck[]=httpRequest.getCookies();  
		 for(int i=0;i<ck.length;i++){  
			 System.out.println("Name =  "+ck[i].getName()+"   Value=  "+ck[i].getValue());//printing name and value of cookie  
		 }  
		 
		 System.out.println(" httpRequest.getRequestURI() == "+httpRequest.getRequestURI());
		 if (httpRequest.getRequestURI() != "/login"){			 
			 // validate the request
			 Map<String, String> header = HeaderInfo.getHeadersInfo(httpRequest);
			 httpRequest.setAttribute("cookie", header.get("cookie"));
			 System.out.println("cookie = "+header.get("cookie"));
			 //System.out.println("tokenId from session in request = "+httpRequest.getSession().getAttribute("tokenId"));
			 
			 String tokenId = getTokenFromCookie(header);
			 try {
				 if (!ObjectUtils.isEmpty(tokenId)){
					 validateToken(tokenId);
					 System.out.println("We are in doFilter of RequestFilter *********** and tokenId = "+tokenId);
				 }			
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			 
		 }

		filterChain.doFilter(request, response);
		return;
	}
		
	//Get tokenId from cookie
	private String getTokenFromCookie(Map<String, String> header){
		 String tokenFromRequest = header.get("cookie");
		 int tempIndex = 0 ;
		 if(tokenFromRequest != null){
			 tempIndex = tokenFromRequest.indexOf("tokenId");
		 }
		 
		 String tokenId = null;
		 if(tempIndex == -1 || tempIndex == 0){
			 return null;
		 } else {
			 tokenId = tokenFromRequest.substring(tempIndex + 8, tempIndex + 40);
		 }		 
		 return tokenId;
	}		

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub		
	}
	
	@RequestMapping(method = RequestMethod.GET,consumes = "application/json", produces = "application/json")
	public boolean validateToken(String tempTokenId) throws ParseException {
		Query q1;
		//String a = "103faac5de5dea0ab9c80bf2f9f18930";
		 q1 = new BasicQuery("{tokenId:\""+tempTokenId+"\"}").limit(100);

		 List<LoginHistory> resourceList = operations.find(q1, LoginHistory.class);
		 Iterator<LoginHistory> temp = resourceList.iterator();
		 LoginHistory loginDocument = null;
		 boolean isTokenValid = false;
		 
		 if(ObjectUtils.isEmpty(resourceList)){
			 return isTokenValid;
		 } else {
			 while(temp.hasNext()){
				 loginDocument = temp.next();					
			 }
			 
			 boolean valideTimeGap = findTimeDifference(loginDocument.getLoginTime());			 
			 
			 if(!ObjectUtils.isEmpty(operations.find(q1, LoginHistory.class)) && valideTimeGap ){
				 System.out.println("Got the valit token");
				 isTokenValid = true;
			 }  else {
				 System.out.println("Invalid Token..!");
				 isTokenValid = false;
			 }			 
		 }		 		 
		return isTokenValid;
		//return 0;		
	}
	
	public boolean findTimeDifference(String loginTime) throws ParseException {

		Date convertedLoginTime = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(loginTime);

		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
		//Date date = new Date();
		String currentDateTime = dateFormat.format(new Date());
		System.out.println(" currentDateTime = "+currentDateTime);
		
		Date currentDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(currentDateTime);
		Long diff = currentDate.getTime() - convertedLoginTime.getTime();
		long diffInSeconds = (TimeUnit.MILLISECONDS.toSeconds(diff))/* / 1000  % 60*/;

		boolean valideTimeGap = false;
		if(diffInSeconds >= 1800){
			System.out.println("Time out..!!!");
			valideTimeGap = false;
		} else {
			System.out.println("Valid Token with less than 30 min. gap i.e. time gap in seconds = " + diffInSeconds);
			valideTimeGap = true;
		}	
		return valideTimeGap;		
	}	
}
