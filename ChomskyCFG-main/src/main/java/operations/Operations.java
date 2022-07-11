package operations;

import dto.CFGrammar;
import exceptions.AlphabetExceededException;

public interface Operations {

    CFGrammar firstStep(CFGrammar cfGrammar) throws AlphabetExceededException;

    //CFGrammar secondStep(CFGrammar cfGrammar);
    CFGrammar thirdStep(CFGrammar cfGrammar);
    CFGrammar fourthStep(CFGrammar cfGrammar);
}
