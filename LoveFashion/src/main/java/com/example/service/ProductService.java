package com.example.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.dto.ListProductModel;
import com.example.entity.CatalogCategoryEntity;
import com.example.entity.CatalogProductEntity;
import com.example.entity.CatalogProductEntityGallery;
import com.example.repository.ICategoryDAO;
import com.example.repository.IProductDAO;

@Service
public class ProductService implements IProductSevice {

	@Autowired
	private ICategoryDAO categoryRepository;

	@Autowired
	private IProductDAO productRepository;
	
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

	@Transactional
	public List<ListProductModel> getListProductByCategoy(String urlPath) {
		// TODO Auto-generated method stub
		Set<CatalogProductEntity> products = categoryRepository
				.findCategoryByUrl(urlPath).getCatalogProductEntitys();
		List<ListProductModel> list = new ArrayList<ListProductModel>();
		ListProductModel model;
		Set<CatalogProductEntityGallery> images;
		for (CatalogProductEntity item : products) {
			if (item.getStatus() == 1) {
				model = new ListProductModel();
				model.setName(item.getName());
				model.setPrice(item.getPrice());
				model.setUrlPath(item.getUrlPath());
				model.setSale(item.getSale() == 1 ? true : false);
				model.setNews(item.getIsNew() == 1 ? true : false);
				images = item.getCatalogProductEntityGalleries();
				for (CatalogProductEntityGallery image : images) {
					model.getListImage().add(image.getValue());
				}
				if (model.getListImage().size() < 2
						&& model.getListImage().size() > 0) {
					model.getListImage().add(model.getListImage().get(0));
				}
				list.add(model);
			}
		}
		return list;
	}

	@Transactional
	public CatalogProductEntity getProduct(String urlPath) {
		// TODO Auto-generated method stub
		return productRepository.getProduct(urlPath);
	}

}
