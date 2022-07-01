package operations.impl;

import dto.CFGrammar;
import lombok.NoArgsConstructor;
import operations.Operations;

import java.util.*;

@NoArgsConstructor
public class OperationsImpl implements Operations {
	@Override
	public CFGrammar secondStep(CFGrammar cfGrammar){
		var grammarRules = cfGrammar.getRules();
		Map<String, Integer> position_value = new HashMap<>();
		//pegar o número de cada regra
		for(int i = 0; i < cfGrammar.getVariables().size() ; i++){
			position_value.put(cfGrammar.getVariables().get(i), i);
		}
		var newRules = grammarRules;

		for(int x = 0; x < position_value.size() ; x++) {
			while(x<position_value.size()){//existsCondition2_2(newRules, position_value, x) // 2.2

				int finalX = x;
				newRules.stream()
						.filter(r -> position_value.get(r.get(0)) == finalX)
						.filter(re -> re.get(1).charAt(0) <= 90 && re.get(1).charAt(0) >= 65)
						.filter(reg -> reg.get(1).length() > 1)
						.filter(regr -> regr.get(1).charAt(0) == regr.get(0).charAt(0))
						.forEach(regra -> {
							System.out.println("aaaa");

						});
			}


			while(existsCondition2_1(newRules, position_value, x)) {
				for (int i = 0; i < newRules.size(); i++) {
					if(position_value.get(newRules.get(i).get(0)) == x) {
						String aux = newRules.get(i).get(1); // toda troca de regra, atualiza aux e coloca a regra atual
						if (aux.charAt(0) <= 90 && aux.charAt(0) >= 65) { // conferimos se é uma regra
							if (aux.length() > 1) { // conferimos se ao lado da regra há ao menos uma
								// variavel
								if (position_value.get(newRules.get(i).get(0)) > position_value.get(aux.substring(0, 1))) { //  conferimos se a regra da
									// esquerda é maior
									for (int j = 0; j < newRules.size(); j++) {
										var sufix = aux.substring(1);
										if (aux.charAt(0) == newRules.get(j).get(0).charAt(0)) {
											sufix = newRules.get(j).get(1).concat(sufix);
											List<String> aux_add_rules = new ArrayList<>();
											aux_add_rules.add(newRules.get(i).get(0));
											aux_add_rules.add(sufix);
											newRules.add(aux_add_rules);

										}
									}
									newRules.remove(i);
								}
							}
						}
					}
				}
			}
		}
		cfGrammar.setRules(newRules);
		return cfGrammar;
	}


	public boolean existsCondition2_1(List<List<String>> grammarRules, Map<String, Integer> position_value, int x){
		for (int i = 0; i < grammarRules.size(); i++) {
			if(position_value.get(grammarRules.get(i).get(0)) == x) {
				String aux = grammarRules.get(i).get(1); // toda troca de regra, atualiza aux e coloca a regra atual
				if (aux.charAt(0) <= 90 && aux.charAt(0) >= 65) { // conferimos se é uma regra
					if (aux.length() > 1) { // conferimos se ao lado da regra há ao menos uma
						// variavel
						if (position_value.get(grammarRules.get(i).get(0)) > position_value.get(aux.substring(0, 1))) {
							return true;

						}
					}
				}
			}
		}
		return false;
	}








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
