package com.example.repository;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.example.entity.CatalogProductEntity;

@Repository
public class ProductDAO extends AbstractDAO<CatalogProductEntity> implements IProductDAO{

	public CatalogProductEntity getProduct(String urlKey) {
		// TODO Auto-generated method stub
		Query query = getSession().createQuery("from CatalogProductEntity where urlPath=:urlPath");
		query.setString("urlPath", urlKey);
		return (CatalogProductEntity) query.uniqueResult();
	}

}
