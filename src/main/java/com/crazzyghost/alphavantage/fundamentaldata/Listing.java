package com.crazzyghost.alphavantage.fundamentaldata;

import java.io.IOException;

import com.crazzyghost.alphavantage.AlphaVantageException;
import com.crazzyghost.alphavantage.Config;
import com.crazzyghost.alphavantage.Fetcher;
import com.crazzyghost.alphavantage.UrlExtractor;
import com.crazzyghost.alphavantage.fundamentaldata.response.ListingDelistingStatusResponse;
import com.crazzyghost.alphavantage.parser.Parser;

import okhttp3.Call;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class Listing extends FundamentalData {

    public Listing(Config config) { 
    	super(config); 
    }
    
	@Override
	public void fetch() {
		
        Config.checkNotNullOrKeyEmpty(config);

        config.getOkHttpClient().newCall(UrlExtractor.extract(builder.build(), config.getKey())).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if(failureCallback != null) failureCallback.onFailure(new AlphaVantageException());
            }

            @Override
            public void onResponse(Call call,  Response response) throws IOException {
                if(!response.isSuccessful()){
                    if(failureCallback != null) failureCallback.onFailure(new AlphaVantageException());
                } else {
                    try(ResponseBody body = response.body()){
                        byte[] bytes = body.bytes();
                        parseListing(bytes);
                    }
                }
            }
        });
    }
	
    /**
     * Make a blocking synchronous http request to fetch the data.
     * This will be called by the {@link FundamentalData.RequestProxy#fetchSync()}.
     *
     * Using this method will overwrite any async callback
     *
     * @since 1.6.0
     * @param successCallback internally used {@link SuccessCallback}
     * @throws AlphaVantageException exception thrown
     */
    protected void fetchSync(SuccessCallback<?> successCallback) throws AlphaVantageException {

        Config.checkNotNullOrKeyEmpty(config);

        this.successCallback = successCallback;
        this.failureCallback = null;
        okhttp3.OkHttpClient client = config.getOkHttpClient();
        try {
			Call call = client.newCall(UrlExtractor.extract(builder.build(), config.getKey()));
			Response r = call.execute();
			byte[] bytes = r.body().bytes();
			parseListing(bytes);
        } catch(IOException e) {
            throw new AlphaVantageException(e.getMessage());
        }
    }
    // this method will not use the "of" function as we do not need to parse JSON due to response being CSV file
    @SuppressWarnings("unchecked")
    private void parseListing(byte[] fileBytes) {
        ListingDelistingStatusResponse response = new ListingDelistingStatusResponse(null) ;
        response.setFileBytes(fileBytes);
        if(response.getErrorMessage() != null && failureCallback != null) {
            failureCallback.onFailure(new AlphaVantageException(response.getErrorMessage()));
        }
        if(successCallback != null){
            ((Fetcher.SuccessCallback<ListingDelistingStatusResponse>)successCallback).onSuccess(response);
        }
    }

}
