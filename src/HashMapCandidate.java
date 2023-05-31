import java.util.HashMap;
import java.util.Map;


public class HashMapCandidate {

    public static Map<String,String> hashMapCandidate;
    static{
        hashMapCandidate = new HashMap<>();
        hashMapCandidate.put( "presidente", new String( "Presidente" ));
        hashMapCandidate.put( "senador", new String( "Senador" ));
        hashMapCandidate.put( "governador", new String( "Governador" ));
        hashMapCandidate.put( "df", new String( "Deputado Federal" ));
        hashMapCandidate.put( "de", new String( "Deputado Estadual" ));

        hashMapCandidate.put( "prefeito", new String( "Prefeito" ));
        hashMapCandidate.put( "vereador", new String( "vereador" ));
    }
}