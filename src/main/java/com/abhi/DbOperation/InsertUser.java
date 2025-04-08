package com.abhi.DbOperation;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.procedure.ProcedureCall;
import org.hibernate.procedure.ProcedureOutputs;
import org.hibernate.result.Output;
import org.hibernate.result.ResultSetOutput;

import com.abhi.model.Invoice;
import com.abhi.model.Order;
import com.abhi.model.User;

import Utils.EntityMapper;
import Utils.HibernateUtil;

public class InsertUser {

	public int AddUser() {
		int add = 0;
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = null;

		try {
			tx = session.beginTransaction();

			User user = new User();
			user.setUsername("admin6");
			user.setPassword(
					"948e0e7e3729d2cdb2e1e75af34af8742590ee23388f50d7bf52c934dd311e30cdc8d014ce4a641a135facb4cb70432cf1181e882ca740b6ea889f31ea738f5c");
			user.setRole("ADMIN");
			user.setEmail("admin5@example.com");
			user.setCreatedAt(java.time.LocalDateTime.now());
			user.setValidTo(java.time.LocalDateTime.now().plusYears(1));

			session.persist(user);

			tx.commit();
			System.out.println("User saved successfully.");
			add = 1;
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
			add = 0;
		} finally {
			session.close();
			return add;
		}
	}

	public void updateUser() {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = session.beginTransaction();

		User user = session.get(User.class, 1); // Already Persistent
		user.setUsername("new_username75"); // Hibernate tracks this

		tx.commit(); // Hibernate issues an UPDATE
		session.close();

	}

	@SuppressWarnings("unchecked")
	public void GetData() {
		List<Object[]> rows = null;
		List<User> users = null;
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = session.beginTransaction();

		// Call procedure without entity mapping
		ProcedureCall call = session.createStoredProcedureCall("get_summary");

		// 1️⃣ First result set - Users (map manually or via entity)
		ProcedureOutputs outputs = call.getOutputs();
		Output output = outputs.getCurrent();
		rows = ((ResultSetOutput) output).getResultList();
		users = EntityMapper.mapToEntity(rows, User.class);

		// 2️⃣ Second result set - Orders
		List<Order> orders = null;
		if (outputs.goToNext()) {
		    output = outputs.getCurrent();
		    rows = ((ResultSetOutput) output).getResultList();
		    
		    orders = EntityMapper.mapToEntity(rows, Order.class);
		}

		// 3️⃣ Third result set - Invoices
		List<Invoice> invoices = null;
		if (outputs.goToNext()) {
		    output = outputs.getCurrent();
		    rows = ((ResultSetOutput) output).getResultList();
		    invoices = EntityMapper.mapToEntity(rows, Invoice.class);
		    if(invoices != null)
		    {
		    	Order order = invoices.get(0).getOrder();
		    	System.out.println(order);
		    }
		}

		tx.commit();
		session.close();

		// Print results
		users.forEach(System.out::println);
		orders.forEach(System.out::println);
		invoices.forEach(System.out::println);
	}
	
	public void LazyLoadExample()
	{
		String hql = "SELECT i FROM Invoice i JOIN FETCH i.order";
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = session.beginTransaction();
		List<Invoice> invoices = session.createQuery(hql, Invoice.class).list();
		invoices.forEach(System.out::println);
		if(invoices != null)
		{
			Order order = invoices.get(0).getOrder();
			System.out.println(order);
		}
		tx.commit();
		session.close();
	}
	
	public void demoCacheL2()
	{
		Session session1 = HibernateUtil.getSessionFactory().openSession();
		User user1 = session1.get(User.class, 1); // DB hit + store in L2
		session1.close();
		
		Session session2 = HibernateUtil.getSessionFactory().openSession();
		User user2 = session2.get(User.class, 1); // Do not hit DB 
		session2.close();

	}

}
