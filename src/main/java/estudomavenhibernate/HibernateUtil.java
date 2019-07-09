package estudomavenhibernate;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class HibernateUtil {

	public static EntityManagerFactory factory = null;
	
	static {
		init();
	}
	
	private static void init() {
		try {
			//verificação para a leitura do xml ser lida apenas uma vez no progrma de forma estatica
			if(factory == null) {
				factory = Persistence.createEntityManagerFactory("estudo-maven-hibernate");
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static EntityManager getEntityManager() {
		return factory.createEntityManager(); /*Prove a parte de persistencia*/
	}
	
	//método para a identificação da primary key
	//retorna a primary key de um objeto qualquer
	public static Object getPrimaryKey(Object entidade) {
		return factory.getPersistenceUnitUtil().getIdentifier(entidade);
	}
	
}
