package operations;

import dto.CFGrammar;
import exceptions.AlphabetExceededException;

public interface Operations {
    CFGrammar firstSecondStep(CFGrammar cfGrammar);
    CFGrammar thirdStep(CFGrammar cfGrammar);
    CFGrammar fourthStep(CFGrammar cfGrammar);
}
