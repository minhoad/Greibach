package operations;

import dto.CFGrammar;
import exceptions.AlphabetExceededException;

public interface Operations {
    CFGrammar thirdStep(CFGrammar cfGrammar);
    CFGrammar fourthStep(CFGrammar cfGrammar);
}
