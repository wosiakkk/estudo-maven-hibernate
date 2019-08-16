package dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import estudomavenhibernate.HibernateUtil;

public class DaoGeneric<E> {

	private EntityManager entityManager = HibernateUtil.getEntityManager();

	public void salvar(E entidade) {
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		entityManager.persist(entidade);
		transaction.commit();
	}

	public E updateMerge(E entidade) { // salva ou atualiza
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		E entidadeSalva = entityManager.merge(entidade);
		transaction.commit();
		return entidadeSalva;
	}

	public E pesquisar(E entidade) {

		Object id = HibernateUtil.getPrimaryKey(entidade);

		E e = (E) entityManager.find(entidade.getClass(), id);

		return e;
	}

	public E pesquisar(Long id, Class<E> entidade) {

		E e = (E) entityManager.find(entidade, id);

		return e;
	}

	public void deletarPorId(E entidade) throws Exception{
		Object id = HibernateUtil.getPrimaryKey(entidade);
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		entityManager
				.createNativeQuery(
						"delete from " + entidade.getClass().getSimpleName().toLowerCase() + " where id= " + id)
				.executeUpdate();//monta aquery de delete, evita erros montar ela manualmente
		transaction.commit();//realiza a operação no banco
	}

	public List<E> listar(Class<E> entidade){
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		
		List<E> lista = entityManager.createQuery("from " + entidade.getName()).getResultList();
		
		transaction.commit();
		
		return lista;
	}
	
	public EntityManager getEntityManager() { //método par apoder acessar o entityManager  por outras partes do projeto
		return entityManager;
	}
	
}
