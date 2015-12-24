package com.example.service;

import java.util.List;

import com.example.dto.CategoryModel;
import com.example.entity.CatalogCategoryEntity;

public interface IProductSevice {

	public List<CatalogCategoryEntity> getAllCategoryByOrder();
	
	public CatalogCategoryEntity getRootCategory();
	
	public List<CatalogCategoryEntity> getCategoryByParent(int parent);
}
