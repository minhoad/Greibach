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
			position_value.put(cfGrammar.getOriginalVariables().get(i), i+1);
		}

		var newRules = grammarRules;

		for(int a = 0; a < newRules.size(); a++){
			List<String> rules = new ArrayList<>();
			for(List<String> xa : newRules){
				if(newRules.get(a).get(0).equals(xa.get(0))) // quando a regra que eu to olhando é igual a do for de fora
					rules.add(xa.get(1));
			}

			for(int ab = 0; ab < rules.size(); ab++) {
				String rule = rules.get(ab);
				if (rule.length() > 1) {
					String newRule = Character.toString(rule.charAt(0));
					for (int i = 1; i < rule.length(); i++) {
						String c = String.valueOf(rule.charAt(i));
						if (cfGrammar.getAlphabetSymbols().contains(c)) {
							String nextRule = getNewVarLetter(cfGrammar.getOriginalVariables()); // pega uma nova letra pra uma regra
							List<String> new_rule_added = new ArrayList<>();
							position_value.put(nextRule,position_value.size()); // adcionando a nova letra da regra nos valores
							newRule = newRule.concat(nextRule);
							new_rule_added.add(nextRule);
							new_rule_added.add(c);
							newRules.add(new_rule_added);
						} else {
							newRule = newRule.concat(c);
						}

					}
					newRules = changeRule(newRules, rule, newRule, newRules.get(a).get(0));
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
		cfGrammar.setOriginalVariables(auxiliar_variables);
		cfGrammar.setRules(newRules);
		cfGrammar.setPosition_value(position_value);
		return cfGrammar;
	}

	public List<List<String>> changeRule(List<List<String>> newRules, String Rule, String newRule, String RuleName){
		List<String> aux = new ArrayList<>();
		for(int i = 0; i < newRules.size() ; i++){
			if(newRules.get(i).get(1).equals(Rule)){
				aux.add(RuleName);
				aux.add(newRule);
				newRules.set(i, aux);
			}
		}
		return newRules;
	}
	@Override
	@SneakyThrows
	public CFGrammar secondStep(CFGrammar cfGrammar){
		var grammarRules = cfGrammar.getRules();

		for (int i = 0; i < cfGrammar.getVariables().size(); i++) {
			String variable = cfGrammar.getVariables().get(i);
			if (cfGrammar.getPosition_value().containsKey(variable)) {
				List<String> rules = new ArrayList<>();
				for (List<String> xa : grammarRules) {
					if (grammarRules.get(i).get(0).equals(xa.get(0))) // quando a regra que eu to olhando é igual a do for de fora
						rules.add(xa.get(1));
				}
				for (int ab = 0; ab < rules.size(); ab++) {
					String rule = rules.get(ab);
					String firstSymbol = Character.toString(rule.charAt(0));

					if (cfGrammar.getVariables().contains(firstSymbol)) {

						if (firstSymbol.equals(variable)) { //2.2
							rules.remove(ab);
							List<String> rules_w_rec = new ArrayList<>();
							for (String xa : rules) {
								if (grammarRules.get(i).get(0).equals(xa)) // quando a regra que eu to olhando é igual a do for de fora
									rules_w_rec.add(xa);
							}
							String rule_w_rec = rule.substring(1);

							String nextRule = getNewVarLetter(cfGrammar.getVariables());
							List<String> aux = new ArrayList<>();
							aux.add(nextRule);
							aux.add(rule_w_rec);

							grammarRules.add(aux);

							String rule_with_rc = rule_w_rec.concat(nextRule);

							for (String r : rules_w_rec) {
								String aux_add = r.concat(nextRule);
								List<String> aux_ = new ArrayList<>();
								aux_.add(firstSymbol);
								aux_.add(rule_with_rc);
								grammarRules.add(aux);
							}
						}

						else if (cfGrammar.getPosition_value().get(firstSymbol) > cfGrammar.getPosition_value().get(variable)) { //A -> B //2.1
							List<String> rules_ = new ArrayList<>();
							for (String x : rules) {
								rules_.add(x);
							}
							rules_.remove(rule);
							for (String r : rules_) {
								String oldrule = rule.substring(1);
								String newrule = r + oldrule;
								List<String> aux_ = new ArrayList<>();
								aux_.add(firstSymbol);
								aux_.add(newrule);
								grammarRules.add(aux_);
							}
						}
						List<String> auxiliar_variables = new ArrayList<>();
						for (int k = 0; k < grammarRules.size(); k++) {
							if (!auxiliar_variables.contains(grammarRules.get(k).get(0))) {
								auxiliar_variables.add(grammarRules.get(k).get(0));
							}
						}
						cfGrammar.setVariables(auxiliar_variables);
						cfGrammar.setOriginalVariables(auxiliar_variables);
						cfGrammar.setRules(grammarRules);
					}
				}
			}
		}
		List<String> auxiliar_variables = new ArrayList<>();
		for (int k = 0; k < grammarRules.size(); k++) {
			if (!auxiliar_variables.contains(grammarRules.get(k).get(0))) {
				auxiliar_variables.add(grammarRules.get(k).get(0));
			}
		}
		cfGrammar.setVariables(auxiliar_variables);
		cfGrammar.setOriginalVariables(auxiliar_variables);
		cfGrammar.setRules(grammarRules);
		return cfGrammar;
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
