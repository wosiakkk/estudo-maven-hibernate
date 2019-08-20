package managedbean;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import dao.DaoTelefones;
import dao.DaoUsuario;
import model.TelefoneUser;
import model.UsuarioPessoa;

@ManagedBean(name = "telefoneManagedBean")
@ViewScoped
public class TelefoneManagedBean {
	
	private UsuarioPessoa user = new UsuarioPessoa();
	private DaoUsuario<UsuarioPessoa> dao = new DaoUsuario<UsuarioPessoa>();
	private DaoTelefones<TelefoneUser> daoTelefone = new DaoTelefones<TelefoneUser>();
	private TelefoneUser telefone = new TelefoneUser();
	
	@PostConstruct
	public void init() {
		
		String codUser = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("codigouser"); //pegando o parâmetro passado pela outra tela
		user = dao.pesquisar(Long.parseLong(codUser), UsuarioPessoa.class);
	}
	
	public String salvar() {
		telefone.setUsuarioPessoa(user);;
		daoTelefone.salvar(telefone);
		telefone = new TelefoneUser();
		user = dao.pesquisar(user.getId(), UsuarioPessoa.class); //recarregando a tebela de telefones
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Informação: ","Salvo com Sucesso!"));
		
		return "";
	}
	
	public String removeTelefone() throws Exception {
		daoTelefone.deletarPorId(telefone);
		user = dao.pesquisar(user.getId(), UsuarioPessoa.class); //recarregando a tebela de telefones
		telefone = new TelefoneUser();
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Informação: ","Excluído com Sucesso!"));
		return "";
	}
	
	public void setUser(UsuarioPessoa user) {
		this.user = user;
	}
	public UsuarioPessoa getUser() {
		return user;
	}
	
	public void setDaoTelefone(DaoTelefones<TelefoneUser> daoTelefone) {
		this.daoTelefone = daoTelefone;
	}
	public DaoTelefones<TelefoneUser> getDaoTelefone() {
		return daoTelefone;
	}
	
	public void setTelefone(TelefoneUser telefone) {
		this.telefone = telefone;
	}
	public TelefoneUser getTelefone() {
		return telefone;
	}
	
}
