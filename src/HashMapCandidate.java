import java.util.HashMap;
import java.util.Map;


public class HashMapCandidate {

    public static Map<String,String> hashMapCandidate;
    static{
        hashMapCandidate = new HashMap<>();
        hashMapCandidate.put( "presidente", new String( "Presidente" ));
        hashMapCandidate.put( "df", new String( "Deputado Federal" ));
    }
}