

package sg.com.bigspoon.www.BGDashboard.test;

import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.Button;

import sg.com.bigspoon.www.BGDashboard.authenticator.BootstrapAuthenticatorActivity;
import sg.com.bigspoon.www.BGDashboard.R;


/**
 * Tests of displaying the authenticator activity
 */
public class BootstrapAuthenticatorTest extends ActivityInstrumentationTestCase2<BootstrapAuthenticatorActivity> {

    /**
     * Create test for {@link sg.com.bigspoon.www.BGDashboard.authenticator.BootstrapAuthenticatorActivity}
     */
    public BootstrapAuthenticatorTest() {
        super(BootstrapAuthenticatorActivity.class);
    }

    /**
     * Verify activity exists
     */
    public void testActivityExists() {
        assertNotNull(getActivity());
    }

    /**
     * Verify sign in button is initially disabled
     */
    public void testSignInDisabled() {
        View view = getActivity().findViewById(R.id.b_signin);
        assertNotNull(view);
        assertTrue(view instanceof Button);
        assertFalse(((Button) view).isEnabled());
    }
}
