package privacyanalyzer.backend.service;

import java.io.Serializable;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import privacyanalyzer.backend.VariablesRepository;
import privacyanalyzer.backend.data.entity.Variables;

@Service
public class VariablesService implements Serializable{

	private final VariablesRepository variablesRepository;

	
	private Variables variables;
	
	@Autowired
	public VariablesService(VariablesRepository variablesRepository) {
		this.variablesRepository = variablesRepository;
	}
	
	
	@PostConstruct
	public void init() {
		variables=variablesRepository.findByName("default");
		
	}


	public Variables getVariables() {
		return variablesRepository.findByName("default");
	}
	
	

	
}
