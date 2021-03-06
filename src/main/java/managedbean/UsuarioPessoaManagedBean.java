package managedbean;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;

import org.apache.tomcat.util.codec.binary.Base64;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.ChartSeries;

import com.google.gson.Gson;

import dao.DaoEmail;
import dao.DaoGeneric;
import dao.DaoUsuario;
import datatablelazy.LazyDataTableModelUserPessoa;
import model.EmailUser;
import model.UsuarioPessoa;

@ManagedBean(name = "usuarioPessoaManagedBean")
@ViewScoped
public class UsuarioPessoaManagedBean {

	private UsuarioPessoa usuarioPessoa = new UsuarioPessoa();
	private LazyDataTableModelUserPessoa<UsuarioPessoa> list =  new LazyDataTableModelUserPessoa<UsuarioPessoa>();
	private DaoUsuario<UsuarioPessoa> daoGeneric = new DaoUsuario<UsuarioPessoa>();
	private BarChartModel barChartModel = new BarChartModel();
	private EmailUser emailUser = new EmailUser();
	private DaoEmail<EmailUser> daoEmail = new DaoEmail<EmailUser>();
	private String campoPesquisa;
	
	@PostConstruct
	public void init() {
		barChartModel = new BarChartModel();
		/*por questões de performace a lista é pesquisada somente uma vez na construção
		 * do objeto. Com isso, no método salvar e deletar a lista é manipulada em memória.
		 * Por este motivo também foi add o equals e hashcode por id no objeto pessoa
		 * para o sistema saber a dfenreça entre eles em memória*/
		//list = daoGeneric.listar(UsuarioPessoa.class); *******************NÃO USADO AMIS A PARTIR DA IMPLEMENTAÇÃO DA LISTA LAZY PAGINADA***********************
		
		//*************LISTA PAGINADA********************
		//carregando a primeira p´[agina da lista paginada, com 5 valores
		list.load(0, 5, null, null, null);
		
		
		ChartSeries userSalario = new ChartSeries();
		//carregando o gráfico
		for (UsuarioPessoa usuarioPessoa : list.list) {
			userSalario.set(usuarioPessoa.getNome(), usuarioPessoa.getSalario());
		}
		barChartModel.addSeries(userSalario);
		barChartModel.setTitle("Salário dos Usuários");
	}
	
	public void setEmailUser(EmailUser emailUser) {
		this.emailUser = emailUser;
	}
	
	public EmailUser getEmailUser() {
		return emailUser;
	}
	
	public BarChartModel getBarChartModel() {
		return barChartModel;
	}
	
	public UsuarioPessoa getUsuarioPessoa() {
		return usuarioPessoa;
	}
	public void setUsuarioPessoa(UsuarioPessoa usuarioPessoa) {
		this.usuarioPessoa = usuarioPessoa;
	}
	
	public void setCampoPesquisa(String campoPesquisa) {
		this.campoPesquisa = campoPesquisa;
	}
	
	public String getCampoPesquisa() {
		return campoPesquisa;
	}
	
	public String salvar() {
		daoGeneric.salvar(usuarioPessoa);
		list.list.add(usuarioPessoa);
		usuarioPessoa = new UsuarioPessoa();
		init(); //chama novamnte o init para reconstruir o gráfco de salário após salvar um novo usuário
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Informação: ","Salvo com Sucesso!"));
		//novo();
		return "";
	}
	
	public String novo() { //método para limpar o formulário e criar um novo objeto
		usuarioPessoa = new UsuarioPessoa();
		return "";
	}
	
	public LazyDataTableModelUserPessoa<UsuarioPessoa> getList() {
		
		return list;
	}
	public String remover() {
		try {
			daoGeneric.removerUsuario(usuarioPessoa);
			list.list.remove(usuarioPessoa);
			init();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Informação: ","Excluido com Sucesso!"));
		} catch (Exception e) {
			if(e.getCause() instanceof org.hibernate.exception.ConstraintViolationException) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Informação: ", "Existem telefones para esse usuário"));
			}
			e.printStackTrace();
		}
		novo();
		return "";
	}
	
	public void pesquisaCep(AjaxBehaviorEvent event) {
		try {
			
			URL url = new URL("https://viacep.com.br/ws/"+usuarioPessoa.getCep()+"/json/"); //definindo a URL para chamar o WS
			URLConnection connection = url.openConnection(); //abrindo uma conexão para a URL
			InputStream ist = connection.getInputStream(); //pegando o retorno(fluxo de dados)
			BufferedReader br = new BufferedReader(new InputStreamReader(ist, "UTF-8")); //lendo os dados
			//varrendo o buffer, lendo e coloncado em uma variável de texto
			
			String cep = ""; //variável auxiliar par amanipular no while as linhas
			StringBuilder jsonCep = new StringBuilder();
			while ((cep = br.readLine()) != null) {
				jsonCep.append(cep);
			}
			//objeto auxiliar para pegar os dados do json
			//como os atributos do json e do objeto estão com os mesmo nome podemos fazer a adição direta
			UsuarioPessoa userCepPessoa = new Gson().fromJson(jsonCep.toString(), UsuarioPessoa.class);
			//passando os dados do objeto auxiliar para o objeto controlado na tela
			usuarioPessoa.setCep(userCepPessoa.getCep());
			usuarioPessoa.setLogradouro(userCepPessoa.getLogradouro());
			usuarioPessoa.setComplemento(userCepPessoa.getComplemento());
			usuarioPessoa.setBairro(userCepPessoa.getBairro());
			usuarioPessoa.setLocalidade(userCepPessoa.getLocalidade());
			usuarioPessoa.setUf(userCepPessoa.getUf());
			usuarioPessoa.setUnidade(userCepPessoa.getUnidade());
			usuarioPessoa.setIbge(userCepPessoa.getIbge());
			usuarioPessoa.setGia(userCepPessoa.getGia());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void addEmail() {
		//amarrando o email ao objeto pessoa
		emailUser.setUsuarioPessoa(usuarioPessoa);
		//nesse moemento o emailUser recebe o obejto completo gravado no banco, anteriormente só tinha o texto digitado na tela
		emailUser = daoEmail.updateMerge(emailUser);
		//adicionando o email a lista de emails do usuário
		usuarioPessoa.getEmailUser().add(emailUser);
		//preparando o objeto para gravar um novo email
		emailUser = new EmailUser();
		//mensagem
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Informação: ", "E-mail salvo com sucesso!"));
	}
	
	public void removerEmail() throws Exception {
		//pegando o código da requisição que foi enviado como parâmetro
		String codigoEmail = FacesContext.getCurrentInstance().
				getExternalContext().getRequestParameterMap().get("codigoemail");
		//definindo um objeto com o id da requisição para passar para o método de remover por id
		EmailUser remover = new EmailUser();
		remover.setId(Long.parseLong(codigoEmail));
		daoEmail.deletarPorId(remover);
		//para não precisar consultar todos os emails novamente no banco para atualizar a lista, apenas removemos na que esta em memória, poupando processamento
		usuarioPessoa.getEmailUser().remove(remover);
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Informação: ", "Removido com sucesso!"));
	}
	
	public void pesquisar() {
		//list = daoGeneric.pesquisar(campoPesquisa);
		list.pesquisar(campoPesquisa);
	}
	
	//para o método capturar o arquivo da tela primefaces através do ajax, é necessário esse parâmetro FileUploadEvent
	public void upload(FileUploadEvent image) {
		//convertendo a iamgem para base64, ela vem como bytes pelo parâmetro e usamos o método DatatypeConverter.printBase64Binary do javax para converte-la em base64
		String imagem = "data:image/png;base64," + DatatypeConverter.printBase64Binary(image.getFile().getContents()); //cabeçalho base 64
		usuarioPessoa.setImagem(imagem);
	}
	
	public void download() throws IOException{
		//pegando o id do usuário da linha da tablea para download
		String codUser = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("fileDownloadId");
		//pesquisando o usuário
		UsuarioPessoa pessoa  = daoGeneric.pesquisar(Long.parseLong(codUser), UsuarioPessoa.class);
		//preparar a imagem apra download
		//byte[] imagem = Base64().decodeBase64(pessoa.getImagem().split("\\,")[1]);
		byte[] imagem = Base64.decodeBase64(pessoa.getImagem().split("\\,")[1]);
		
		HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
		
		response.addHeader("Content-Disposition", "attachment; filename=donwload.png"); 
		response.setContentType("aplication/octet-stream");
		response.setContentLength(imagem.length);
		response.getOutputStream().write(imagem);
		response.getOutputStream().flush();
		FacesContext.getCurrentInstance().responseComplete();
	}
}
