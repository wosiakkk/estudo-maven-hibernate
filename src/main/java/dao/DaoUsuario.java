package dao;

import java.util.List;

import javax.persistence.Query;

import model.UsuarioPessoa;

public class DaoUsuario<E> extends DaoGeneric<UsuarioPessoa>{

		/*Para não comprometer o método de delete por id genérico do dao genérico
		 * é criado ste dao, para poder deletar os filhos do usuário antes dele 
		 * mesmo, que no caso é os telefones.*/
	
	public void removerUsuario(UsuarioPessoa pessoa) throws Exception {
	/*
		//Como a classe extends dao generic temos acesso ao método que retorna o entitymanager
		getEntityManager().getTransaction().begin();
		String sqlDeleteTelefone = "delete from telefoneuser where usuariopessoa_id = "+pessoa.getId();
		getEntityManager().createNativeQuery(sqlDeleteTelefone).executeUpdate(); 
		String sqlDeleteEmails = "delete from emailuser where usuariopessoa_id = "+pessoa.getId();
		
		getEntityManager().createNativeQuery(sqlDeleteEmails).executeUpdate(); 
		
		getEntityManager().getTransaction().commit();
		/*Após deletar os telefones e emails(filhos), basta deletar o usuário, com isso chamamos o método da 
		 * classe pai extendida com super.*/
		/*super.deletarPorId(pessoa);
		
		*TODO CÓDIGO FOI SUBSTITUIDO APÓS A IMPLEMENÇÃO DA OPÇÃO DE DELETE ME CASCATA NO MODEL USUÁRIO
		*/
		//FORMA DE DELETE EM CASCATA:
		
		getEntityManager().getTransaction().begin();
		
		getEntityManager().remove(pessoa); //método remove do JPA
		
		getEntityManager().getTransaction().commit();
	}

	public List<UsuarioPessoa> pesquisar(String campoPesquisa) {
	
		Query query = super.getEntityManager().createQuery("from UsuarioPessoa where nome like '%"+campoPesquisa+"%'" );
		
		return query.getResultList();
	}
}
