package com.example.panier_achats.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.example.panier_achats.model.Category;
import com.example.panier_achats.repository.CategoryRepository;
import com.example.panier_achats.service.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService {

	@Autowired
	private CategoryRepository categoryRepository;

	@Override
	public Category saveCategory(Category category) {
		return categoryRepository.save(category);
	}

	@Override
	public List<Category> getAllCategory() {
		return categoryRepository.findAll();
	}

	@Override
	public Boolean existCategory(String name) {
		return categoryRepository.existsByName(name);
	}

	@Override
	public Boolean deleteCategory(int id) {
		Category category = categoryRepository.findById(id).orElse(null);//category est un objet Category ou null     //null verifie la avleur de l optional 
//grace a cet variable qui stocke on verifie dabord lexistence de category avec l id avant de proceder a la supp
		if (!ObjectUtils.isEmpty(category)) {
			categoryRepository.delete(category);
			return true;
		}
		return false;
	}

	@Override
	public Category getCategoryById(int id) {
		Category category = categoryRepository.findById(id).orElse(null);
		return category;
	}

	@Override
	public List<Category> getAllActiveCategory() {
		List<Category> categories = categoryRepository.findByIsActiveTrue();
		return categories;
	}
	/*orElse(null) est utilisé 
	 * pour extraire la catégorie de l'Optional 
	 * ou retourner null si la catégorie n'est pas trouvée.
	 */

	@Override
	public Page<Category> getAllCategorPagination(Integer pageNo, Integer pageSize) {
		Pageable pageable = PageRequest.of(pageNo, pageSize);
		return categoryRepository.findAll(pageable);
	}

}