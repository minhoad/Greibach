package operations.impl;

import dto.CFGrammar;
import exceptions.AlphabetExceededException;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import operations.Operations;
import utils.OperationsUtils;

import java.util.*;
import java.util.stream.Collectors;

import static utils.OperationsUtils.getNewVarLetter;

@NoArgsConstructor
public class OperationsImpl implements Operations {

	@Override
	public CFGrammar firstStep(CFGrammar cfGrammar) throws AlphabetExceededException {
		var grammarRules = cfGrammar.getRules();

		Map<String, Integer> position_value = new HashMap<>();
		//pegar o número de cada regra

		for(int i = 0; i < cfGrammar.getOriginalVariables().size() ; i++){
			position_value.put(cfGrammar.getOriginalVariables().get(i), i);
		}

		var newRules = grammarRules;

		for(int a = 0; a < newRules.size(); a++){
			List<String> rules = new ArrayList<>();
			for(List<String> xa : newRules){
				if(newRules.get(a).get(0).equals(xa.get(0))) // quando a regra que eu to olhando é igual a do for de fora
					rules.add(xa.get(1));
			}

			for(int ab = 0; ab < rules.size(); ab++) {
				//System.out.println("GET: " + arrayList.get(ab) + "Index: " + ab);
				String rule = rules.get(ab);
				//System.out.println("V: " + variavel.getVariavel() + " -> " + regra);
				if (rule.length() > 1) {
					//System.out.println("Entrou if " + regra);
					String newRule = Character.toString(rule.charAt(0));
					for (int i = 1; i < rule.length(); i++) {
						String c = String.valueOf(rule.charAt(i));
						//System.out.println("Entrou for " + c);
						if (cfGrammar.getAlphabetSymbols().contains(c)) {
							String nextRule = getNewVarLetter(cfGrammar.getOriginalVariables());
							List<String> new_rule_added = new ArrayList<>();
							position_value.put(nextRule,position_value.size());
							newRule = newRule.concat(nextRule);
						} else {
							newRule = newRule.concat(c);
						}

					}
					changeRule(newRules, rule, newRule, newRules.get(a).get(0));
				}
			}
		}
		List<String> auxiliar_variables = new ArrayList<>();
		for(int i = 0; i < grammarRules.size() ; i++){
			if(!auxiliar_variables.contains(grammarRules.get(i).get(0))){
				auxiliar_variables.add(grammarRules.get(i).get(0));
			}
		}
		cfGrammar.setVariables(auxiliar_variables);
		cfGrammar.setRules(newRules);
		cfGrammar.setPosition_value(position_value);
		return cfGrammar;
	}

	public void changeRule(List<List<String>> newRules, String Rule, String newRule, String RuleName){
		List<String> aux = new ArrayList<>();
		for(int i = 0; i < newRules.size() ; i++){
			if(newRules.get(i).get(1).equals(Rule)){
				aux.add(RuleName);
				aux.add(newRule);
				newRules.set(i, aux);
			}
		}
	}





	@SneakyThrows
	public CFGrammar secondStep(CFGrammar cfGrammar){
		var grammarRules = cfGrammar.getRules();
		for(int i = 0; i < cfGrammar.getVariables().size() ; i++){

		}


		return cfGrammar;
	}

	public String searchRule(List<List<String>> grammarRules, String x){
		for(int i = 0; i < grammarRules.size() ; i++){
			if(grammarRules.get(i).get(0).equals(x)){ // regra que estamos na iteração igual a regra analisada
				if(grammarRules.get(i).get(1).length() == 1 && grammarRules.get(i).get(1).equals(grammarRules.get(i).get(1).toLowerCase())){ // se não possui recursividade a esquerda
					return grammarRules.get(i).get(1); // w
				}
			}
		}
		return "";
	}


	public boolean existsCondition(List<List<String>> grammarRules, Map<String, Integer> position_value){
		for (int i = 0; i < grammarRules.size(); i++) {
			if(position_value.containsKey(grammarRules.get(i).get(0))) {// conferindo se é uma regra original
				String aux = grammarRules.get(i).get(1); // toda troca de regra, atualiza aux e coloca a regra atual
				if (aux.charAt(0) <= 90 && aux.charAt(0) >= 65) { // conferimos se é uma regra  // 2.1
					if (aux.length() > 1) { // conferimos se ao lado da regra há ao menos uma
						// variavel
						if (position_value.get(grammarRules.get(i).get(0)) > position_value.get(aux.substring(0, 1))) {
							return true;

						}
						if(grammarRules.get(i).get(0).charAt(0) == aux.charAt(0)) { // 2.2
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
