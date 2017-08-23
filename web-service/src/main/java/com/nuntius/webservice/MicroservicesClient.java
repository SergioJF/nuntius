package com.nuntius.webservice;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.hateoas.PagedResources;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient("zuulserver")
public interface MicroservicesClient {

	@RequestMapping(method = RequestMethod.GET, value = "/comments")
	public PagedResources<Comment> getComments();
	
	@RequestMapping(method = RequestMethod.GET, value = "/clients")
	public PagedResources<Client> getClients();
	
}
