package br.com.project.bean.view;

import java.util.Date;

import javax.faces.bean.ManagedBean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import br.com.framework.interfac.crud.InterfaceCrud;
import br.com.project.bean.geral.BeanManagedViewAbstract;
import br.com.project.bean.geral.EntidadeAtualizaSenhaBean;
import br.com.project.geral.controller.EntidadeController;
import br.com.project.model.classes.Entidade;

@Controller
@Scope(value = "session")
@ManagedBean(name = "entidadeBeanView")
public class EntidadeBeanView extends BeanManagedViewAbstract{
	
	@Autowired
	private ContextoBean contextoBean;

	private static final long serialVersionUID = 1L;
	
	@Autowired
	private EntidadeController entidadeController;
	
	private EntidadeAtualizaSenhaBean entidadeAtualizaSenhaBean = new EntidadeAtualizaSenhaBean();
	
	
	public void updateSenha() throws Exception{
			Entidade entidadeLogada = contextoBean.getEntidadeLogada();
			if (!entidadeAtualizaSenhaBean.getSenhaAtual()
					.equals(entidadeLogada.getEnt_senha())){
				addMsg("A senha atual n�o � valida.");
				return;
			}else if (entidadeAtualizaSenhaBean.getSenhaAtual().equals(entidadeAtualizaSenhaBean.getNovaSenha())) {
				addMsg("A senha atual n�o pode ser igual a nova senha.");
				return;
			}else if (!entidadeAtualizaSenhaBean.getNovaSenha()
					.equals(entidadeAtualizaSenhaBean.getConfirmaSenha())){
				addMsg("A nova senha e a confirm��o n�o conferem.");
				return;
			}else {
				entidadeLogada.setEnt_senha(entidadeAtualizaSenhaBean.getNovaSenha());
				entidadeController.saveOrUpdate(entidadeLogada);
				entidadeLogada = entidadeController.findByPorId(Entidade.class, entidadeLogada.getEnt_codigo());
				if (entidadeLogada.getEnt_senha().equals(entidadeAtualizaSenhaBean.getNovaSenha())){
					sucesso();
				}else {
					addMsg("A nova senha n�o foi atualizada");
					error();
				}
			}
	}
	
	
	
	public EntidadeAtualizaSenhaBean getEntidadeAtualizaSenhaBean() {
		return entidadeAtualizaSenhaBean;
	}

	public void setEntidadeAtualizaSenhaBean(
			EntidadeAtualizaSenhaBean entidadeAtualizaSenhaBean) {
		this.entidadeAtualizaSenhaBean = entidadeAtualizaSenhaBean;
	}

	public String getUsuarioLogadoSecurity() {
		return contextoBean.getAuthentication().getName();
	}
	
	public Date getUltimoAcesso() throws Exception {
		return contextoBean.getEntidadeLogada().getEnt_ultimoacesso();
	}

	@Override
	protected Class<Entidade> getClassImplement() {
		return Entidade.class;
	}

	@Override
	protected InterfaceCrud<Entidade> getController() {
		return entidadeController;
	}

	@Override
	public String condicaoAndParaPesquisa() throws Exception {
		return null;
	}

}
