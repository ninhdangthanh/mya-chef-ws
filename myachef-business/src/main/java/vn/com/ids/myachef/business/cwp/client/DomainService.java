package vn.com.ids.myachef.business.cwp.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class DomainService {

    public static void main(String[] args) throws Exception {

        System.out.println("Number of Command Line Argument = " + args.length);

        for (int i = 0; i < args.length; i++) {
            System.out.println(String.format("Command Line Argument %d is %s ", i, args[i]));
        }

        String subDomain = args[0].toString();

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        CWPDomainService cwpService = new CWPDomainService();

        addSubDomain(gson, cwpService, subDomain);

        System.out.println(true);
    }

    private static void addSubDomain(Gson gson, CWPDomainService cwpService, String subDomain) {

        String action = cwpService.addSubDomain(subDomain);
        JsonElement object = JsonParser.parseString(action);
        System.out.println(gson.toJson(object));
    }
}
