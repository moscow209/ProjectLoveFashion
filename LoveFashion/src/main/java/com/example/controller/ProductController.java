package com.example.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.example.dto.ListProductModel;
import com.example.entity.CatalogProductEntity;
import com.example.service.IProductSevice;

@Controller
public class ProductController {

	@Autowired
	private IProductSevice proService;
	
	@RequestMapping(value = "/list/*", method = RequestMethod.GET)
	public String showListProductCategory(HttpServletRequest request, Model model){
		String path = request.getRequestURI().substring("/list/".length());
		List<ListProductModel> lists = proService.getListProductByCategoy(path);
		model.addAttribute("lists", lists);
		return "list";
	}
	
	@RequestMapping(value = "/detail/{urlPath}")
	public String showDetail(@PathVariable("urlPath") String path, Model model){
		CatalogProductEntity product = proService.getProduct(path);
		model.addAttribute("product", product);
		return "detail";
	}
}
