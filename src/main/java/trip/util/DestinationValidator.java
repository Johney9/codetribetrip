package trip.util;

import org.springframework.stereotype.Component;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import trip.model.google.GoogleLocation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

@Component("beforeCreateDestinationValidator")
public class DestinationValidator implements ConstraintValidator<DestinationExists, String> {

    public static final String GOOGLE_API_PROPERTIES_LOCATION = "src/main/resources/google-api.properties";
    public static final String GOOGLE_API_PROPERTY_NAME = "key";

    private boolean checkDestinationValidity(String destination) {
        boolean retVal = false;

        try {
            String key = getApiKeyFromProperties();
            String locationStatus = getGoogleLocationFromServer(destination, key).getStatus();
            if (locationStatus.matches("OK") || locationStatus.matches("OVER_QUERY_LIMIT")) {
                retVal = true;
            }
        } catch (IOException ioe) {
            System.err.println(ioe.getLocalizedMessage());
            System.err.println("Error reading properties/Google api. Program will continue without verifying locations with Google.");
            retVal = true;
        }

        return retVal;
    }

    private String getApiKeyFromProperties() throws IOException {
        Properties prop = new Properties();
        FileInputStream file;
        file = new FileInputStream(GOOGLE_API_PROPERTIES_LOCATION);
        prop.load(file);
        return prop.getProperty(GOOGLE_API_PROPERTY_NAME);
    }


    private GoogleLocation getGoogleLocationFromServer(String destination, String key) throws IOException {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(GooglePlacesApiEndpointInterface.BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        GooglePlacesApiEndpointInterface service = retrofit.create(GooglePlacesApiEndpointInterface.class);
        Call<GoogleLocation> locationCall = service.getGoogleLocationResults(destination, key);
        return locationCall.execute().body();
    }

    @Override
    public void initialize(DestinationExists constraintAnnotation) {

    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {

        return checkDestinationValidity(s);
    }
}
