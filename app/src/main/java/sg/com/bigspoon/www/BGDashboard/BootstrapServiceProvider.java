
package sg.com.bigspoon.www.BGDashboard;

import android.accounts.AccountsException;
import android.app.Activity;

import java.io.IOException;

import retrofit.RestAdapter;
import sg.com.bigspoon.www.BGDashboard.authenticator.ApiKeyProvider;
import sg.com.bigspoon.www.BGDashboard.core.BootstrapService;

/**
 * Provider for a {@link sg.com.bigspoon.www.BGDashboard.core.BootstrapService} instance
 */
public class BootstrapServiceProvider {

    private RestAdapter restAdapter;
    private ApiKeyProvider keyProvider;



    public BootstrapServiceProvider(RestAdapter restAdapter, ApiKeyProvider keyProvider) {
        this.restAdapter = restAdapter;
        this.keyProvider = keyProvider;
    }

    /**
     * Get service for configured key provider
     * <p/>
     * This method gets an auth key and so it blocks and shouldn't be called on the main thread.
     *
     * @return bootstrap service
     * @throws IOException
     * @throws AccountsException
     */
    public BootstrapService getService(final Activity activity)
            throws IOException, AccountsException {
        // The call to keyProvider.getAuthKey(...) is what initiates the login screen. Call that now.
        keyProvider.getAuthKey(activity);

        // TODO: See how that affects the bootstrap service.
        return new BootstrapService(restAdapter);
    }
}
