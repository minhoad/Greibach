package operations.impl;

import dto.CFGrammar;
import lombok.NoArgsConstructor;
import operations.Operations;

import java.util.*;

@NoArgsConstructor
public class OperationsImpl implements Operations {
	@Override
	public CFGrammar firstSecondStep(CFGrammar cfGrammar){
		var grammarRules = cfGrammar.getRules();
		Map<String, Integer> position_value = new HashMap<>();
		//pegar o número de cada regra
		for(int i = 0; i < grammarRules.size() ; i++){
			position_value.put(grammarRules.get(i).get(0), i);
		}
		List<List<String>> newRules = new ArrayList<>();




		for(int i = 0; i < grammarRules.size() ; i++){
			String aux = grammarRules.get(i).get(1); // toda troca de regra, atualiza aux e coloca a regra atual
			if(aux.charAt(0) <= 90 && aux.charAt(0) >= 65) { // conferimos se é uma regra
				if(aux.charAt(1) >= 97 && aux.charAt(1) <= 122 && aux.length()>1) { // conferimos se ao lado da regra há ao menos uma
																					// variavel
					if (position_value.get(grammarRules.get(i).get(0)) > position_value.get(aux.substring(0, 1))) { //  conferimos se a regra da
																													// esquerda é maior
						var sufix = aux.substring(1);

						for (int j = 0; j < grammarRules.size(); j++) {
							if (aux.charAt(0) == grammarRules.get(j).get(0).charAt(0)) {
								sufix = grammarRules.get(j).get(1).concat(sufix);
								List<String> aux_add_rules = new ArrayList<>();
								aux_add_rules.add(grammarRules.get(i).get(0));
								aux_add_rules.add(sufix);
								newRules.add(aux_add_rules);
							}
						}
					}
				}
				else{ // pra n perder a regras
					newRules.add(grammarRules.get(i));
				}
			}
			else{ // pra n perder a regras
				newRules.add(grammarRules.get(i));
			}
		}
		
		cfGrammar.setRules(newRules);
		return cfGrammar;
	}

	// public CFGrammar secondSecondStep(CFGrammar cfGrammar){
	
	// }



	@Override
	public CFGrammar thirdStep(CFGrammar cfGrammar) {
		var grammarRules = cfGrammar.getRules();
		var variables = cfGrammar.getVariables();
		var originalVariablesSize = cfGrammar.getOriginalVariables().size() - 1;
		var newVariables = new ArrayList<>();
		List<List<String>> newRule = new ArrayList<>();
		var analysedVar = new String();
		var currentVar = new String();

		//for (int i = variables.size()-1; i > originalVariablesSize; i--) { //TODO: testar
		for (int i = variables.size()-1; i > 4; i--) { //TODO: arrumar pra terminar nas variáveis originais
			analysedVar = variables.get(i);
			for (int j = 0; j < grammarRules.size(); j++) {
				currentVar = grammarRules.get(j).get(0);
				if (currentVar.equals(analysedVar)) {
					newRule.add(grammarRules.get(j));
				}
			}
			newVariables.add(analysedVar);
		}

		//for (int i = originalVariablesSize; i >= 0; i--) { //TODO: testar
		for (int i = 4; i >= 0; i--) { //TODO: arrumar pra começar nas variáveis originais
			analysedVar = variables.get(i);
			for (int j = 0; j < grammarRules.size(); j++) {
				currentVar = grammarRules.get(j).get(0);
				if (currentVar.equals(analysedVar)) {
					var firstRuleChar = grammarRules.get(j).get(1).charAt(0);
					if (newVariables.contains(currentVar)){
						if (firstRuleChar <= 90 && firstRuleChar >= 65) { //TODO: mudar para a lista de variáveis
							for (int k = 0; k < grammarRules.size(); k++) {
								var currentVar2 = grammarRules.get(k).get(0).charAt(0);
								if (currentVar2 == firstRuleChar) {
									List<String> rule = new ArrayList<>();
									rule.add(currentVar);
									rule.add(grammarRules.get(k).get(1) + grammarRules.get(j).get(1).substring(1));
									newRule.add(rule);
								}
							}
						}
						else {
							newRule.add(grammarRules.get(j));
						}
					}
					else {
						if (firstRuleChar <= 90 && firstRuleChar >= 65) { //TODO: mudar para a lista de variáveis
							for (int k = 0; k < newRule.size(); k++) {
								var currentVar2 = newRule.get(k).get(0).charAt(0);
								if (currentVar2 == firstRuleChar) {
									List<String> rule = new ArrayList<>();
									rule.add(currentVar);
									rule.add(newRule.get(k).get(1) + grammarRules.get(j).get(1).substring(1));
									newRule.add(rule);
								}
							}
						}
						else {
							newRule.add(grammarRules.get(j));
						}
					}
				}
			}
			newVariables.add(analysedVar);
		}
		Collections.reverse(newRule);
		cfGrammar.setRules(newRule);
		return cfGrammar;
	}

	@Override
	public CFGrammar fourthStep(CFGrammar cfGrammar) {
		var grammarRules = cfGrammar.getRules();
		var variables = cfGrammar.getVariables();
		var originalVariablesSize = cfGrammar.getOriginalVariables().size() - 1;
		List<List<String>> newRule = new ArrayList<>();
		var analysedVar = new String();
		var currentVar = new String();

		//for (int i = 0; i <= originalVariablesSize; i++) { //TODO: testar
		for (int i = 0; i <= 4; i++) { //TODO: arrumar pra terminar nas variáveis originais
			analysedVar = variables.get(i);
			for (int j = 0; j < grammarRules.size(); j++) {
				currentVar = grammarRules.get(j).get(0);
				if (currentVar.equals(analysedVar)) {
					newRule.add(grammarRules.get(j));
				}
			}
		}

		for (int i = originalVariablesSize + 1; i < variables.size(); i++) { //TODO: testar
		//for (int i = 5; i < variables.size(); i++) { //TODO: arrumar pra comecar nas variáveis novas
			analysedVar = variables.get(i);
			for (int j = 0; j < grammarRules.size(); j++) {
				currentVar = grammarRules.get(j).get(0);
				if (currentVar.equals(analysedVar)) {
					var firstRuleChar = grammarRules.get(j).get(1).charAt(0);
					if (firstRuleChar <= 90 && firstRuleChar >= 65) { //TODO: mudar para a lista de variáveis
						for (int k = 0; k < newRule.size(); k++) {
							var currentVar2 = newRule.get(k).get(0).charAt(0);
							if (currentVar2 == firstRuleChar) {
								List<String> rule = new ArrayList<>();
								rule.add(currentVar);
								rule.add(newRule.get(k).get(1) + grammarRules.get(j).get(1).substring(1));
								newRule.add(rule);
							}
						}
					}
					else {
						newRule.add(grammarRules.get(j));
					}
				}
			}
		}

		cfGrammar.setRules(newRule);
		return cfGrammar;
	}
}
