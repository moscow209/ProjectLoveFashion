package com.example.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.dto.CategoryModel;
import com.example.entity.CatalogCategoryEntity;
import com.example.repository.ICategoryDAO;

@Service
public class ProductService implements IProductSevice {

	@Autowired
	private ICategoryDAO categoryRepository;

	@Transactional
	public List<CatalogCategoryEntity> getAllCategoryByOrder() {
		// TODO Auto-generated method stub
		return categoryRepository.getAllCategoryByOrder();
	}

	@Transactional
	public CatalogCategoryEntity getRootCategory() {
		// TODO Auto-generated method stub
		return categoryRepository.getRootCategory();
	}

	@Transactional
	public List<CatalogCategoryEntity> getCategoryByParent(int parent) {
		// TODO Auto-generated method stub
		return categoryRepository.getCategoryByParent(parent);
	}

}
