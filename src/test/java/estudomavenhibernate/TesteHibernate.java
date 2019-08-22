package estudomavenhibernate;

import java.util.List;

import org.junit.Test;

import dao.DaoGeneric;
import model.TelefoneUser;
import model.UsuarioPessoa;

public class TesteHibernate {

	
	@Test
	public void testeHibernate() {
		HibernateUtil.getEntityManager();
	}
	
	@Test
	public void testeHibernateUtil() {

		DaoGeneric<UsuarioPessoa> daoGeneric = new DaoGeneric<UsuarioPessoa>();

		UsuarioPessoa usuarioPessoa = new UsuarioPessoa();
		usuarioPessoa.setNome("testeIdade");
		usuarioPessoa.setSobrenome("Idade");
		usuarioPessoa.setLogin("idade");
		usuarioPessoa.setSenha("1234");
		usuarioPessoa.setIdade(23);
		daoGeneric.salvar(usuarioPessoa);
	}

	@Test
	public void testeBuscar() {
		DaoGeneric<UsuarioPessoa> daoGeneric = new DaoGeneric<UsuarioPessoa>();

		UsuarioPessoa usuarioPessoa = new UsuarioPessoa();
		usuarioPessoa.setId(4L);

		usuarioPessoa = daoGeneric.pesquisar(usuarioPessoa);

		System.out.println(usuarioPessoa);
	}

	@Test
	public void testeBuscar2() {
		DaoGeneric<UsuarioPessoa> daoGeneric = new DaoGeneric<UsuarioPessoa>();

		UsuarioPessoa usuarioPessoa = daoGeneric.pesquisar(2L, UsuarioPessoa.class);

		System.out.println(usuarioPessoa);
	}

	@Test
	public void testeUpdate() {
		DaoGeneric<UsuarioPessoa> daoGeneric = new DaoGeneric<UsuarioPessoa>();

		UsuarioPessoa usuarioPessoa = daoGeneric.pesquisar(2L, UsuarioPessoa.class);

		usuarioPessoa.setNome("Nome atualizado pelo hibernate");
		usuarioPessoa.setSenha("Senha atualizada pelo hibernate");

		usuarioPessoa = daoGeneric.updateMerge(usuarioPessoa);
		System.out.println(usuarioPessoa);
	}

	@Test
	public void testeDelete() {
		// primeiramente consultamos
		DaoGeneric<UsuarioPessoa> daoGeneric = new DaoGeneric<UsuarioPessoa>();

		UsuarioPessoa usuarioPessoa = daoGeneric.pesquisar(2L, UsuarioPessoa.class);

		//daoGeneric.deletarPorId(usuarioPessoa);
	}

	@Test
	public void testeConsultar() {
		// primeiramente consultamos
		DaoGeneric<UsuarioPessoa> daoGeneric = new DaoGeneric<UsuarioPessoa>();

		List<UsuarioPessoa> lista = daoGeneric.listar(UsuarioPessoa.class);

		for (UsuarioPessoa usuarioPessoa : lista) {
			System.out.println("---------------------------");
			System.out.println(usuarioPessoa);
			System.out.println("---------------------------");
		}
	}

	@Test
	public void testeQueryList() {
		DaoGeneric<UsuarioPessoa> daoGeneric = new DaoGeneric<UsuarioPessoa>();
		// List<UsuarioPessoa> lista = daoGeneric.getEntityManager().createQuery("from
		// UsuarioPessoa").getResultList(); //todos os registros
		List<UsuarioPessoa> lista = daoGeneric.getEntityManager().createQuery("from UsuarioPessoa where nome = 'Bruno'")
				.getResultList(); // um registro em especifico
		for (UsuarioPessoa usuarioPessoa : lista) {
			System.out.println(usuarioPessoa);
		}
	}

	@Test
	public void testeQueryListMaxResult() {
		DaoGeneric<UsuarioPessoa> daoGeneric = new DaoGeneric<UsuarioPessoa>();
		List<UsuarioPessoa> lista = daoGeneric.getEntityManager().createQuery("from UsuarioPessoa order by id")
				.setMaxResults(4).getResultList(); // pegando os 4 primeiiros resultados ordenados pelo id.
		for (UsuarioPessoa usuarioPessoa : lista) {
			System.out.println(usuarioPessoa);
		}
	}

	@Test
	public void testeQueryListParameter() {
		DaoGeneric<UsuarioPessoa> daoGeneric = new DaoGeneric<UsuarioPessoa>();
		List<UsuarioPessoa> lista = daoGeneric.getEntityManager().createQuery("from UsuarioPessoa where nome = :n")
				.setParameter("n", "Otavio").getResultList(); // query com parametro ( :nome_parametro) que é definida
																// no set
		for (UsuarioPessoa usuarioPessoa : lista) {
			System.out.println(usuarioPessoa);
		}
		//mais de um parâmetro pode ser definido com OR
		/*List<UsuarioPessoa> lista = daoGeneric.getEntityManager().createQuery("from UsuarioPessoa where nome = :n or sobrenome = :s")
				.setParameter("n", "Otavio")
				.setParameter("s", "Silva")
				.getResultList();*/
		//mais de um parâmetro pode ser definido com AND
				/*List<UsuarioPessoa> lista = daoGeneric.getEntityManager().createQuery("from UsuarioPessoa where nome = :n and sobrenome = :s")
						.setParameter("n", "Otavio")
						.setParameter("s", "Silva")
						.getResultList();*/
	}
	
	@Test
	public void testeQuerySomaIdade() {
		DaoGeneric<UsuarioPessoa> daoGeneric = new DaoGeneric<UsuarioPessoa>();
		Long somaIdade = (Long) daoGeneric.getEntityManager()
				.createQuery("select sum(u.idade) from UsuarioPessoa u").getSingleResult();
		
		System.out.println("A soma é: " +somaIdade);
	}
	
	@Test
	public void testeNamedQuery(){

		DaoGeneric<UsuarioPessoa> daoGeneric = new DaoGeneric<UsuarioPessoa>();
		List<UsuarioPessoa> lista = daoGeneric.getEntityManager().createNamedQuery("UsuarioPessoa.findAll").getResultList();
		for (UsuarioPessoa usuarioPessoa : lista) {
			System.out.println("---------------------------");
			System.out.println(usuarioPessoa);
			System.out.println("---------------------------");
		}
	}

	@Test
	public void testeNamedQuery2(){
		DaoGeneric<UsuarioPessoa> daoGeneric = new DaoGeneric<UsuarioPessoa>();
		List<UsuarioPessoa> lista = daoGeneric.getEntityManager().createNamedQuery("UsuarioPessoa.findPorNome")
				.setParameter("nome", "Bruno")
				.getResultList();
		for (UsuarioPessoa usuarioPessoa : lista) {
			System.out.println("---------------------------");
			System.out.println(usuarioPessoa);
			System.out.println("---------------------------");
		}
	}

	@Test
	public void testeGravaTelefone(){
		
		DaoGeneric daoGeneric = new DaoGeneric(); //como irá ser trabalhado duas classes, não informamos o tipo do genérico
		UsuarioPessoa pessoa = (UsuarioPessoa) daoGeneric.pesquisar(4L, UsuarioPessoa.class);
		
		TelefoneUser telefoneUser = new TelefoneUser();
		telefoneUser.setTipo("casa");
		telefoneUser.setNumero("123456789");
		telefoneUser.setUsuarioPessoa(pessoa);
		
		daoGeneric.salvar(telefoneUser);
	}
	
	@Test
	public void testeConsultarTelefones(){
		
		DaoGeneric daoGeneric = new DaoGeneric();
		UsuarioPessoa pessoa = (UsuarioPessoa) daoGeneric.pesquisar(4L, UsuarioPessoa.class);
		
		for (TelefoneUser telefone : pessoa.getTelefoneUsers()) {
			System.out.println(telefone.getTipo());
			System.out.println(telefone.getNumero());
			System.out.println(telefone.getUsuarioPessoa().getNome());
			System.out.println("----------------------------------------");
		}
	}
}
