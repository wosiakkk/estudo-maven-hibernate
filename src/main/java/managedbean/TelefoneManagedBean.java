package managedbean;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import dao.DaoUsuario;
import model.UsuarioPessoa;

@ManagedBean(name = "telefoneManagedBean")
@ViewScoped
public class TelefoneManagedBean {
	
	private UsuarioPessoa user = new UsuarioPessoa();
	private DaoUsuario<UsuarioPessoa> dao = new DaoUsuario<UsuarioPessoa>();
	
	@PostConstruct
	public void init() {
		
		String codUser = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("codigouser"); //pegando o parâmetro passado pela outra tela
		user = dao.pesquisar(Long.parseLong(codUser), UsuarioPessoa.class);
	}
	
	public void setUser(UsuarioPessoa user) {
		this.user = user;
	}
	public UsuarioPessoa getUser() {
		return user;
	}
}
