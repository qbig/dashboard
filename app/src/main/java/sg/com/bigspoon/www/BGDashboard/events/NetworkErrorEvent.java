package sg.com.bigspoon.www.BGDashboard.events;

import retrofit.RetrofitError;

/**
 * The event that is posted when a network error event occurs.
 * TODO: Consume this event in the {@link sg.com.bigspoon.www.BGDashboard.ui.BootstrapActivity} and
 * show a dialog that something went wrong.
 */
public class NetworkErrorEvent {
    private RetrofitError cause;

    public NetworkErrorEvent(RetrofitError cause) {
        this.cause = cause;
    }

    public RetrofitError getCause() {
        return cause;
    }
}
