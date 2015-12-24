package com.example.entity;
// Generated Dec 19, 2015 11:20:12 PM by Hibernate Tools 4.3.1

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * CatalogCategoryEntity generated by hbm2java
 */
@Entity
@Table(name = "catalog_category_entity", catalog = "lovefashion")
public class CatalogCategoryEntity implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer entityId;
	private int parentId;
	private Date createdAt;
	private Date updatedAt;
	private String path;
	private int position;
	private int level;
	private int childrenCount;
	private String description;
	private String image;
	private Integer includeInMenu;
	private Integer isActive;
	private String metaDescription;
	private String metaKeywords;
	private String metaTitle;
	private String name;
	private String nameEn;
	private String thumbnail;
	private String urlKey;
	private String urlPath;
	private Set<CatalogCategoryProduct> catalogCategoryProducts = new HashSet<CatalogCategoryProduct>(0);

	public CatalogCategoryEntity() {
	}

	public CatalogCategoryEntity(int parentId, String path, int position, int level, int childrenCount) {
		this.parentId = parentId;
		this.path = path;
		this.position = position;
		this.level = level;
		this.childrenCount = childrenCount;
	}

	public CatalogCategoryEntity(int parentId, Date createdAt, Date updatedAt, String path, int position, int level,
			int childrenCount, String description, String image, Integer includeInMenu, Integer isActive,
			String metaDescription, String metaKeywords, String metaTitle, String name, String nameEn, String thumbnail,
			String urlKey, String urlPath, Set<CatalogCategoryProduct> catalogCategoryProducts) {
		this.parentId = parentId;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.path = path;
		this.position = position;
		this.level = level;
		this.childrenCount = childrenCount;
		this.description = description;
		this.image = image;
		this.includeInMenu = includeInMenu;
		this.isActive = isActive;
		this.metaDescription = metaDescription;
		this.metaKeywords = metaKeywords;
		this.metaTitle = metaTitle;
		this.name = name;
		this.nameEn = nameEn;
		this.thumbnail = thumbnail;
		this.urlKey = urlKey;
		this.urlPath = urlPath;
		this.catalogCategoryProducts = catalogCategoryProducts;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)

	@Column(name = "entity_id", unique = true, nullable = false)
	public Integer getEntityId() {
		return this.entityId;
	}

	public void setEntityId(Integer entityId) {
		this.entityId = entityId;
	}

	@Column(name = "parent_id", nullable = false)
	public int getParentId() {
		return this.parentId;
	}

	public void setParentId(int parentId) {
		this.parentId = parentId;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_at", length = 19)
	public Date getCreatedAt() {
		return this.createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "updated_at", length = 19)
	public Date getUpdatedAt() {
		return this.updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	@Column(name = "path", nullable = false)
	public String getPath() {
		return this.path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	@Column(name = "position", nullable = false)
	public int getPosition() {
		return this.position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	@Column(name = "level", nullable = false)
	public int getLevel() {
		return this.level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	@Column(name = "children_count", nullable = false)
	public int getChildrenCount() {
		return this.childrenCount;
	}

	public void setChildrenCount(int childrenCount) {
		this.childrenCount = childrenCount;
	}

	@Column(name = "description", length = 65535)
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "image")
	public String getImage() {
		return this.image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	@Column(name = "include_in_menu")
	public Integer getIncludeInMenu() {
		return this.includeInMenu;
	}

	public void setIncludeInMenu(Integer includeInMenu) {
		this.includeInMenu = includeInMenu;
	}

	@Column(name = "is_active")
	public Integer getIsActive() {
		return this.isActive;
	}

	public void setIsActive(Integer isActive) {
		this.isActive = isActive;
	}

	@Column(name = "meta_description", length = 65535)
	public String getMetaDescription() {
		return this.metaDescription;
	}

	public void setMetaDescription(String metaDescription) {
		this.metaDescription = metaDescription;
	}

	@Column(name = "meta_keywords", length = 65535)
	public String getMetaKeywords() {
		return this.metaKeywords;
	}

	public void setMetaKeywords(String metaKeywords) {
		this.metaKeywords = metaKeywords;
	}

	@Column(name = "meta_title")
	public String getMetaTitle() {
		return this.metaTitle;
	}

	public void setMetaTitle(String metaTitle) {
		this.metaTitle = metaTitle;
	}

	@Column(name = "name")
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "name_en")
	public String getNameEn() {
		return this.nameEn;
	}

	public void setNameEn(String nameEn) {
		this.nameEn = nameEn;
	}

	@Column(name = "thumbnail")
	public String getThumbnail() {
		return this.thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

	@Column(name = "url_key")
	public String getUrlKey() {
		return this.urlKey;
	}

	public void setUrlKey(String urlKey) {
		this.urlKey = urlKey;
	}

	@Column(name = "url_path")
	public String getUrlPath() {
		return this.urlPath;
	}

	public void setUrlPath(String urlPath) {
		this.urlPath = urlPath;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "catalogCategoryEntity")
	public Set<CatalogCategoryProduct> getCatalogCategoryProducts() {
		return this.catalogCategoryProducts;
	}

	public void setCatalogCategoryProducts(Set<CatalogCategoryProduct> catalogCategoryProducts) {
		this.catalogCategoryProducts = catalogCategoryProducts;
	}

}