package com.bv.zzpmaatschap.eao.inferface;

import java.util.List;

import com.bv.zzpmaatschap.model.Category;

public interface ICategoryEAO extends EAO<Category>{

	List<Category> getAllCategories();

	Category findCategory(String categoryName);
}
