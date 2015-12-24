package com.example.service;

import java.util.List;

import com.example.dto.ListProductModel;
import com.example.entity.CatalogCategoryEntity;
import com.example.entity.CatalogProductEntity;

public interface IProductSevice {

	public List<CatalogCategoryEntity> getAllCategoryByOrder();
	
	public CatalogCategoryEntity getRootCategory();
	
	public List<CatalogCategoryEntity> getCategoryByParent(int parent);
	
	public List<ListProductModel> getListProductByCategoy(String urlPath);
	
	public CatalogProductEntity getProduct(String urlPath);
}
