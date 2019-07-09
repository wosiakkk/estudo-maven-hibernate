package estudomavenhibernate;

import org.junit.Test;

import dao.DaoGeneric;
import model.UsuarioPessoa;

public class TesteHibernate {

	@Test
	public void testeHibernateUtil() {
		
		DaoGeneric<UsuarioPessoa> daoGeneric = new DaoGeneric<UsuarioPessoa>();
		
		UsuarioPessoa usuarioPessoa = new UsuarioPessoa();
		usuarioPessoa.setNome("Otavio");
		usuarioPessoa.setSobrenome("Fernando");
		usuarioPessoa.setEmail("otaviofernando@gmail.com");
		usuarioPessoa.setLogin("otaviofernando");
		usuarioPessoa.setSenha("1234");
		
		
		daoGeneric.salvar(usuarioPessoa);
	}
	
	
	
}
