package operations.impl;

import dto.CFGrammar;
import exceptions.AlphabetExceededException;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import operations.Operations;
import utils.OperationsUtils;

import java.util.*;
import java.util.stream.Collectors;

@NoArgsConstructor
public class OperationsImpl implements Operations {
	private CFGrammar removeLambdaRules(CFGrammar cfGrammar) {
		List<String> lambdListOG;
		List<String> lambdaList = new ArrayList<>();
		CFGrammar newGrammar = cfGrammar;
		do {
			lambdListOG = lambdaList;
			// Identifica novas regras nulas
			lambdaList = newGrammar.getRules().stream()
					.map(l -> {
						List<String> returnList = new ArrayList<>();
						l.stream().forEach(rule -> {
							if(rule.contains("#")) {
								returnList.add(l.get(0));
							}
						});
						return returnList;
					})
					.flatMap(t -> t.stream())
					.collect(Collectors.toList());

			// Atualiza Gramatica
			List<List<String>> newRules = new ArrayList<>();
			for(String c : lambdaList){
				for(List<String> l : cfGrammar.getRules()) {
					if(!newRules.contains(l))
						newRules.add(l);

					// Se este caractere possui regra Lambda
					if(l.get(1).contains(c)) {
						String rule = l.get(1);

						// Lista todos os indices do caracter mencionado
						List<Integer> charsWithLambda = new ArrayList<>();
						for(int i=0; i<rule.length(); i++) {
							if (lambdaList.contains(rule.substring(i, i+1))) {
								charsWithLambda.add(i);
							}
						}

						// Cria todas permutacoes disponivel de variacoes de posicoes do caractere e escreve as regras
						List<String> pendingRules = new ArrayList<>();
						for(HashSet<Integer> combination : OperationsUtils.permute(charsWithLambda)) {
							String newRuleAux = "";
							for(int i=0; i<rule.length(); i++) {
								if(charsWithLambda.contains(i)) {
									if(combination.contains(i)) {
										newRuleAux = newRuleAux.concat(rule.substring(i,i+1));
									}
								}
								else {
									newRuleAux = newRuleAux.concat(rule.substring(i,i+1));
								}
							}

							// Caso a regra ï¿½ vazia, retorna uma nova regra Lambda
							if(newRuleAux.isEmpty())
								pendingRules.add("#");
							else
								pendingRules.add(newRuleAux);
						}

						// Formata e armazenas as novas regras, elinando regras autoreferentes no processo
						for(String pendingRule : pendingRules) {
							List<String> newRule = new ArrayList<>();
							newRule.add(l.get(0));
							newRule.add(pendingRule);
							if(!newRules.contains(newRule) && !(l.get(0).equals(pendingRule)))
								newRules.add(newRule);
						}
					}
				}
			};

			// Atualiza as novas regras
			newGrammar.setRules(newRules);

			// Enquanto existir novas regras lï¿½mbdas geradas, o processo se repete
		} while (!new HashSet<>(lambdaList).equals(new HashSet<>(lambdListOG)));

		// Remove as regras Lambdas, exceto para variï¿½vel inicial
		newGrammar.setRules(newGrammar.getRules().stream()
				.filter(l -> (!(l.get(1).contains("#") && !(newGrammar.getStartVar().equals(l.get(0))))))
				.collect(Collectors.toList())
		);
		return newGrammar;
	}
	@Override
	@SneakyThrows
	public CFGrammar secondStep(CFGrammar cfGrammar){
		var grammarRules = cfGrammar.getRules();
		Map<String, Integer> position_value = new HashMap<>();
		//pegar o número de cada regra
		for(int i = 0; i < cfGrammar.getOriginalVariables().size() ; i++){
			position_value.put(cfGrammar.getOriginalVariables().get(i), i);
		}
		var newRules = grammarRules;

		for(int x = 0; x < position_value.size() ; x++) {
			while(existsCondition(newRules, position_value, x)) {
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
								//////////////////////////////////////2.2
								if (newRules.get(i).get(0).equals(aux.substring(0, 1))) { // A ->Ay
									List<String> auxiliar_all_rules = new ArrayList<>();
									String aux_recE = "";
									for (int k = 0; k < newRules.size(); k++) {
										auxiliar_all_rules.add(newRules.get(k).get(0));
									}
									// pega tds as variaveis e pega uma nova a partir disso
									try {
										aux_recE = OperationsUtils.getNewVarLetter(auxiliar_all_rules); // PEGO UMA LETRA PRA UMA NOVA REGRA QUE N EXISTE
									} catch (AlphabetExceededException e) {
										throw new RuntimeException(e);
									}

									String rule_Found = searchRule(newRules, newRules.get(i).get(0)); // Achando uma regra que n tenha recursão a esquerda
									// sempre vai vir com algo?
									//trocar new rules(2°) e adicionar nova regra(1°)
									int size_newrule = newRules.size();
									for (int j = 0; j < size_newrule ; j++) { // 1°
										if(j==0){
											List<String> auxiliar_add_new_rule = new ArrayList<>();
											auxiliar_add_new_rule.add(aux_recE);
											auxiliar_add_new_rule.add(rule_Found);
											newRules.add(auxiliar_add_new_rule);
										}
										else if(newRules.get(i).get(0).equals(newRules.get(j).get(0))){
											List<String> auxiliar_add_new_rule = new ArrayList<>();
											if(newRules.get(j).get(1).equals(rule_Found)) {
												auxiliar_add_new_rule.add(aux_recE);
												auxiliar_add_new_rule.add(newRules.get(j).get(1).substring(1).concat(aux_recE)); // parte sem recursão
												newRules.add(auxiliar_add_new_rule);
											}
										}
									}
									for (int j = 0; j < newRules.size(); j++) { // 2°
										if(newRules.get(j).get(0).charAt(0) == aux_recE.charAt(0)){
											List<String> auxiliar_add_new_rule = new ArrayList<>();
											auxiliar_add_new_rule.add(newRules.get(i).get(0));
											auxiliar_add_new_rule.add(newRules.get(j).get(1));
											newRules.add(auxiliar_add_new_rule);
										}
									}
									// //RETIRANDO REGRAS LAMBDA
									// CFGrammar aaa = cfGrammar;
									// aaa.setRules(newRules);
									// removeLambdaRules(aaa);
									// newRules = aaa.getRules();
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


	public boolean existsCondition(List<List<String>> grammarRules, Map<String, Integer> position_value, int x){
		for (int i = 0; i < grammarRules.size(); i++) {
			if(position_value.get(grammarRules.get(i).get(0)) == x) {// conferindo o valor com a posição
				String aux = grammarRules.get(i).get(1); // toda troca de regra, atualiza aux e coloca a regra atual
				if (aux.charAt(0) <= 90 && aux.charAt(0) >= 65) { // conferimos se é uma regra  // 2.1
					if (aux.length() > 1) { // conferimos se ao lado da regra há ao menos uma
						// variavel
						if (position_value.get(grammarRules.get(i).get(0)) > position_value.get(aux.substring(0, 1))) {
							return true;

						}
					}
				}
				if(grammarRules.get(i).get(0).charAt(0) == aux.charAt(0)){ // 2.2
					if (aux.charAt(0) <= 90 && aux.charAt(0) >= 65) { // conferimos se é uma regra
						if (aux.length() > 1) { // conferimos se ao lado da regra há ao menos uma
							return true; // A -> Ay, |y| >= 1
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
