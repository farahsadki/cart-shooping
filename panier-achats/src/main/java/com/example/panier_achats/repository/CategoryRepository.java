package com.example.panier_achats.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.panier_achats.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

	public Boolean existsByName(String name);

	public List<Category> findByIsActiveTrue();

}
