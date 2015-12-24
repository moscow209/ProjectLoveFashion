package com.example.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.example.dto.CategoryModel;
import com.example.entity.CatalogCategoryEntity;
import com.example.service.IProductSevice;

@Controller
@SessionAttributes("categories")
public class HomeController {

	@Autowired
	private IProductSevice service;

	@RequestMapping(value = "/home", method = RequestMethod.GET)
	public String home(Model model) {
		CategoryModel category = this.buildTreeCategory(0);
		model.addAttribute("categories", category);
		return "about";
	}

	public CategoryModel buildTreeCategory(int parent) {
		// TODO Auto-generated method stub
		CatalogCategoryEntity root = service.getRootCategory();
		CategoryModel model = this.convertEntity(root);
		List<CatalogCategoryEntity> level1 = service.getCategoryByParent(model
				.getEntityId());
		int lv2Inx = 0, lv3Inx = 0;
		for (CatalogCategoryEntity lv1 : level1) {
			List<CatalogCategoryEntity> level2 = service
					.getCategoryByParent(lv1.getEntityId());
			model.getSubCategories().add(this.convertEntity(lv1));
			lv3Inx = 0;
			for (CatalogCategoryEntity lv2 : level2) {
				List<CatalogCategoryEntity> level3 = service
						.getCategoryByParent(lv2.getEntityId());
				model.getSubCategories().get(lv2Inx).getSubCategories()
						.add(this.convertEntity(lv2));
				for (CatalogCategoryEntity lv3 : level3) {
					model.getSubCategories().get(lv2Inx).getSubCategories()
							.get(lv3Inx).getSubCategories()
							.add(this.convertEntity(lv3));
				}
				lv3Inx++;
			}
			lv2Inx++;
		}
		return model;
	}

	public CategoryModel convertEntity(CatalogCategoryEntity entity) {
		CategoryModel model = new CategoryModel();
		model.setEntityId(entity.getEntityId());
		model.setLevel(entity.getLevel());
		model.setName(entity.getName());
		model.setNameEn(entity.getNameEn());
		model.setUrlPath(entity.getUrlPath());
		return model;
	}
}
