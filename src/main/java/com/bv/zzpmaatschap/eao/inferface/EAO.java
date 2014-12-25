package com.bv.zzpmaatschap.eao.inferface;


public interface EAO<T> {

	public void persist(T obj);

	public T merge(T obj);

	public T find(Class<T> entityClass, Object primaryKey);

	public void refresh(T entity);

	public void remove(T entity);

}
