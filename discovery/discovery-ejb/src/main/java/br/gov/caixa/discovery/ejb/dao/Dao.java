package br.gov.caixa.discovery.ejb.dao;

import java.util.HashMap;
import java.util.Map;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.FlushModeType;
import javax.persistence.Persistence;

@Stateless
public class Dao {
	public static String PERSISTENCE_PU = "discovery-postgresql-local";

	private EntityManager entityManager = null;
	public EntityTransaction transaction = null;
	private EntityManagerFactory emFactory = null;

	public void abrirConexao() {
		// this.entityManager = PersistenceManager.INSTANCE.getEntityManager();
		// this.emFactory = Persistence.createEntityManagerFactory(PERSISTENCE_PU);

		Map<String, Object> properties = new HashMap<String, Object>();
		properties.put("connection.provider_class", "org.hibernate.jpa.HibernatePersistenceProvider");

		properties.put("javax.persistence.jdbc.url", "jdbc:postgresql://10.208.200.82:5432/pgdiscovery?charSet=UTF8");
		properties.put("javax.persistence.jdbc.user", "postgres");
		properties.put("javax.persistence.jdbc.password", "postgres");

		properties.put("javax.persistence.jdbc.driver", "org.postgresql.Driver");
		properties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");

		properties.put("hibernate.c3p0.min_size", 5);
		properties.put("hibernate.c3p0.max_size", 20);
		properties.put("hibernate.c3p0.timeout", 500);
		properties.put("hibernate.c3p0.max_statements", 20);
		properties.put("hibernate.c3p0.idle_test_period", 2000);

		properties.put("hibernate.use_sql_comments", true);
		properties.put("hibernate.query.conventional_java_constants", false);
		// properties.put("", true);
		properties.put("hibernate.jdbc.batch_size", 25);
		properties.put("hibernate.order_inserts", true);
		properties.put("hibernate.order_updates", true);
		properties.put("hibernate.show_sql", false);
		properties.put("hibernate.format_sql", false);
		// properties.put("", true);
		properties.put("hibernate.hbm2ddl.auto", "validate");
		properties.put("hibernate.connection.useUnicode", true);
		properties.put("hibernate.connection.charsetEncoding", "UTF-8");

		this.emFactory = Persistence.createEntityManagerFactory(PERSISTENCE_PU, properties);

	}

	public void criarEntityManager() {
		this.entityManager = this.emFactory.createEntityManager();
	};

	public void fecharEntityManager() {
		entityManager.close();
	};

	public void fecharConexao() {
		this.emFactory.close();
		// PersistenceManager.INSTANCE.close();
	}

	public void iniciarTransaction() {
		if (this.transaction == null || this.transaction.isActive() == false) {
			this.transaction = entityManager.getTransaction();
			entityManager.setFlushMode(FlushModeType.COMMIT);
			this.transaction.begin();
		}
	}

	public void setTransaction(EntityTransaction transaction) {
		this.transaction = transaction;
	}

	public EntityManager getEntityManager() {
		return entityManager;
	}

	public EntityTransaction getTransaction() {
		return transaction;
	}

	public EntityManagerFactory getEmFactory() {
		return emFactory;
	}

	public void setEmFactory(EntityManagerFactory emFactory) {
		this.emFactory = emFactory;
	}

}
