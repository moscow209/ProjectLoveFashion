package com.example.repository;

import com.example.entity.CatalogProductEntity;

public interface IProductDAO extends DAO<CatalogProductEntity>{

	public CatalogProductEntity getProduct(String urlKey);
	
}
