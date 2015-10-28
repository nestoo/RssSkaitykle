package com.ernkebe.database;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.SimpleExpression;

import com.ernkebe.agregator.Agregator;
import com.ernkebe.entities.CommonWords;
import com.ernkebe.entities.CommonWordsEnds;
import com.ernkebe.entities.grouping.KmeansUncommonWords;
import com.ernkebe.reader.Reader;


public class SqlUtils {

	public static <T> List<T> findAll(Class<T> classObject) {
		Session session = Reader.getFactory().openSession();
		try {
			return session.createCriteria(classObject).list();
		} catch (HibernateException e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return new ArrayList<T>();
	}

	public static <T> void saveAll(List<T> list) {
		Session session = Reader.getFactory().openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			for (T t : list) {
				session.save(t);
				session.refresh(t);
			}
			
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
		
	}

	public static <T> Object findByField(Class<T> class1,String field, Object value) {
		Session session = Reader.getFactory().openSession();
		try {
			Criteria cr = session.createCriteria(class1);
			cr.add(Restrictions.eq(field, value));
			if(cr.list().size() != 0)
			{
				return cr.list().get(0);
			}
			else
			{
				return null;
			}
			
		} catch (HibernateException e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return null;
	}

	public static void updateEntity(Object entity) {
		Session session = Reader.getFactory().openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			session.merge(entity);
			
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
		
	}

	public static  void saveNewEntity(Object entity) {
		Session session = Reader.getFactory().openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			session.save(entity);
			session.refresh(entity);
			
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
		
	}
	
	public static  Object saveNewEntityAndRefresh(Object entity) {
		Session session = Reader.getFactory().openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			session.save(entity);
			session.refresh(entity);
			
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
		return entity;
	}

	public static  <T> List<T> findByFieldExpresion(Class<T> class1,SimpleExpression restriction) {
		Session session = Reader.getFactory().openSession();
		try {
			Criteria cr = session.createCriteria(class1);
			cr.add(restriction);
			return  cr.list();

		} catch (HibernateException e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return new ArrayList<T>();
	}
	
	public static  <T> List<T> findByFieldExpresion(Class<T> class1,SimpleExpression restriction,SimpleExpression restriction2) {
		Session session = Reader.getFactory().openSession();
		try {
			
			Criteria cr = session.createCriteria(class1);
			cr.add(restriction);
			cr.add(restriction2);
			return  cr.list();

		} catch (HibernateException e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return new ArrayList<T>();
	}

	public static void saveOrUpdateEntity(Object entity) {
		Session session = Reader.getFactory().openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			session.saveOrUpdate(entity);
			session.refresh(entity);
			
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
	}

	public static void removeDataFromTable(String table) {
		Session session = Reader.getFactory().openSession();
		try {
			session.createSQLQuery("truncate table "+table).executeUpdate();
		} catch (HibernateException e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
	}
	
	public static void deleteObject(Object entity) {
		Session session = Reader.getFactory().openSession();
		try 
		{
			session.delete(entity);
		} catch (HibernateException e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
	}

	public static <T> List<KmeansUncommonWords> findByFieldAll(Class<T> class1,String field, Object value) {
		Session session = Reader.getFactory().openSession();
		try {
			Criteria cr = session.createCriteria(class1);
			cr.add(Restrictions.eq(field, value));
			return cr.list();
			
		} catch (HibernateException e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return null;
	}

	public static void saveAllCommonWords(Set<Entry<String, Integer>> entrySet) {
		Session session = Reader.getFactory().openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			for (Map.Entry<String, Integer> item : entrySet) {
				CommonWords t =new CommonWords(item.getKey(),item.getValue());
				session.save(t);
				session.refresh(t);
			}
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
	}
	
	public static void saveAllCommonWordsEnd(Set<Entry<String, Integer>> entrySet) {
		Session session = Reader.getFactory().openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			for (Map.Entry<String, Integer> item : entrySet) {
				CommonWordsEnds t = new CommonWordsEnds(item.getKey(),item.getValue());
				session.save(t);
				session.refresh(t);
			}
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
	}
	
}
