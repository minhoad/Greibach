package operations.impl;

import dto.CFGrammar;
import lombok.NoArgsConstructor;
import operations.Operations;

import java.util.*;

@NoArgsConstructor
public class OperationsImpl implements Operations {
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
