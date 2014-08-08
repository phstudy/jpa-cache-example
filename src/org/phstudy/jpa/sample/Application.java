package org.phstudy.jpa.sample;

import java.util.Properties;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.phstudy.jpa.sample.model.Book;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Application {
	static Logger logger = LoggerFactory.getLogger(Application.class);
	
	private static final String PERSISTENCE_UNIT_WITH_L2_CACHE_NAME = "jpa-with-l2-cache";
	private static final String PERSISTENCE_UNIT_WITHOUT_L2_CACHE_NAME = "jpa-without-l2-cache";

	private static final String DATABASE_JDBC_URL = "jdbc:postgresql:study";
	private static final String DATABASE_USERNAME = "study";
	private static final String DATABASE_PASSWORD = "";

	private static EntityManagerFactory factory;

	public static void main(String[] args) {
		run(PERSISTENCE_UNIT_WITH_L2_CACHE_NAME);
		run(PERSISTENCE_UNIT_WITHOUT_L2_CACHE_NAME);
	}
	
	private static void run(String pu) {
		factory = Persistence.createEntityManagerFactory(pu,
				getProperties());
		
		// persist
		EntityManager em = factory.createEntityManager();
		em.getTransaction().begin();
		Book book = new Book();
		em.persist(book);
		em.getTransaction().commit();
		em.close();

		// query
		em = factory.createEntityManager();
		Book book1 = em.find(Book.class, book.getId());
		Book book2 = em.find(Book.class, book.getId());
		em.close();

		logger.info("{} using first-level cache: {}", pu, book1 == book2);		
		logger.info("{} using second-level cache: {}", pu, factory.getCache().contains(Book.class, book.getId()));

		factory.close();
	}

	private static Properties getProperties() {
		Properties properties = new Properties();
		properties
				.put("javax.persistence.jdbc.driver", "org.postgresql.Driver");
		properties.put("javax.persistence.database-major-version", "9");
		properties.put("javax.persistence.database-minor-version", "3");
		properties.put("javax.persistence.jdbc.url", DATABASE_JDBC_URL);
		properties.put("javax.persistence.jdbc.user", DATABASE_USERNAME);
		properties.put("javax.persistence.jdbc.password", DATABASE_PASSWORD);
		properties.put("javax.persistence.database-product-name", "PostgreSQL");
		properties.put("javax.persistence.schema-generation.database.action",
				"create");
		properties.put(
				"javax.persistence.schema-generation.create-database-schemas",
				"true");
		return properties;
	}
}
