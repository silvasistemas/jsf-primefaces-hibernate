package br.com.project.bean.view;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;

import org.primefaces.model.StreamedContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import br.com.framework.interfac.crud.InterfaceCrud;
import br.com.project.bean.geral.BeanManagedViewAbstract;
import br.com.project.carregamento.lazy.CarregamentoLazyListForObject;
import br.com.project.geral.controller.EntidadeController;
import br.com.project.geral.controller.TituloController;
import br.com.project.model.classes.Entidade;
import br.com.project.model.classes.Titulo;
import br.com.project.util.all.Messagens;

@Controller
@Scope("session")
@ManagedBean(name="tituloBeanView")
public class TituloBeanView extends BeanManagedViewAbstract{
	
	private static final long serialVersionUID = 1L;
	
	private String urlFind = "/cadastro/find_titulo.jsf?faces-redirect=true";
	
	private String url = "/cadastro/cad_titulo.jsf?faces-redirect=true"; 

	private Titulo objetoSelecionado = new Titulo();
	
	@Autowired
	private ContextoBean contextoBean;
	
	@Autowired
	private TituloController tituloController;
	
	@Autowired
	private EntidadeController entidadeController;
	
	
	private CarregamentoLazyListForObject<Titulo> list = new CarregamentoLazyListForObject<Titulo>();

	
	@PostConstruct
	public void init() throws Exception{
		objetoSelecionado.setEnt_codigoabertura(contextoBean.getEntidadeLogada());
	}
	
	@Override
	public StreamedContent getArquivoReport() throws Exception {
		super.setNomeRelatorioJasper("report_titulo");
		super.setNomeRelatorioSaida("report_titulo");
		super.setListDataBeanCollectionReport(tituloController.
				findList(getClassImplement()));
		return super.getArquivoReport();
	}
	
	@Override
	protected Class<Titulo> getClassImplement() {
		return Titulo.class;
	}
	
	@Override
	public String novo() throws Exception {
		objetoSelecionado = new Titulo();
		init();
		list.clean();
		return url;
	}
	
	
	@Override
	public void excluir() throws Exception {
		if (objetoSelecionado.getEnt_codigo() != null
				&& objetoSelecionado.getTit_codigo() > 0 ){
			tituloController.delete(objetoSelecionado);
			list.remove(objetoSelecionado);
			objetoSelecionado = new Titulo();
			sucesso();
		}
	}

	@Override
	protected InterfaceCrud<?> getController() {
		return tituloController;
	}

	@Override
	public String condicaoAndParaPesquisa() throws Exception {
		return "";
	}
	
	public void setObjetoSelecionado(Titulo objetoSelecionado) {
		this.objetoSelecionado = objetoSelecionado;
	}
	
	public Titulo getObjetoSelecionado() {
		return objetoSelecionado;
	}
	
	public void setList(CarregamentoLazyListForObject<Titulo> list) {
		this.list = list;
	}
	public CarregamentoLazyListForObject<Titulo> getList() {
		return list;
	}
	
	
	@Override
	public String redirecionarFindEntidade() throws Exception {
		novo();
		return urlFind;
	}
	
	@Override
	public void consultarEntidade() throws Exception {
		objetoSelecionado = new Titulo();
		list.clean();
		list.setTotalResgistroConsulta(super.totalRegistroConsulta(),
				super.getSqlLazyQuery());
		
	}
	
	@Override
	public void saveNotReturn() throws Exception {
		
		objetoSelecionado = tituloController.merge(objetoSelecionado);
		list.add(objetoSelecionado);
		objetoSelecionado = new Titulo();
		sucesso();
	}
	
	@Override
	public void saveEdit() throws Exception {
		objetoSelecionado = tituloController.merge(objetoSelecionado);
		list.add(objetoSelecionado);
		
		objetoSelecionado = new Titulo();
		Messagens.msgSeverityInfor("Atualizado com sucesso!");
	}
	
	@Override
	public String editar() throws Exception {
		list.clean();
		return url;
	}
	
	public List<Entidade> pesquisarPagador(String nome) throws Exception{
		
		return entidadeController.pesquisarPorNome(nome);
		
	}

}
