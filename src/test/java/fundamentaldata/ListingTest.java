package fundamentaldata;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.junit.Before;

import com.crazzyghost.alphavantage.AlphaVantage;
import com.crazzyghost.alphavantage.Config;
import com.crazzyghost.alphavantage.fundamentaldata.response.ListingDelistingStatusResponse;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.mock.Behavior;
import okhttp3.mock.MockInterceptor;
import util.TestUtils;

public class ListingTest {

    MockInterceptor mockInterceptor = new MockInterceptor(Behavior.RELAYED);
    HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
    Config config;
    String key = "key";
	
    @Before
    public void setup() throws IOException {

    }
    // this is a live test and you need to add api key to perform the test
//    @Test()
	public void realTest() {
        TestUtils.forDirectory("fundamentaldata");

        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(30, TimeUnit.SECONDS)
                .callTimeout(30, TimeUnit.SECONDS).addInterceptor(loggingInterceptor).addInterceptor(mockInterceptor)
                .build();

        Config config = Config.builder().key(this.key).httpClient(client).build();

        AlphaVantage.api().init(config);
        
        ListingDelistingStatusResponse response = AlphaVantage.api().listingData().listingCSVFile().fetchSync();
        byte[] b = response.getFileBytes();

        assertNotNull(b);
		
	}

}
