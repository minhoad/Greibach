import com.fasterxml.jackson.databind.ObjectMapper;
import dto.CFGrammar;
import dto.CFGrammarDTO;
import job.Parser;

import java.io.File;

public class Main {

    public static void main(String[] args) throws Exception {

        String fileName = args[0];
        ObjectMapper objectMapper = new ObjectMapper();
        CFGrammarDTO dto = objectMapper.readValue(new File(fileName), CFGrammarDTO.class);

        CFGrammar grammar = new CFGrammar(dto);
        Parser parser = new Parser();
        CFGrammar FNCGrammar = parser.parseGrammarToFNG(grammar);
        parser.printFNG(FNCGrammar);
    }

}
