package pupper115.pupper;

import android.provider.Settings;

import org.junit.Test;
import java.util.regex.Pattern;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class SettingsActivityUnitTest {
    @Test
    public void validEmail_ReturnsTrue() throws Exception {
        assertTrue(SettingsActivity.isEmailValid("aadle@ucsc.edu"));
    }

    @Test
    public void invalidEmail_ReturnsFalse() throws Exception {
        assertFalse(SettingsActivity.isEmailValid("aqwerty"));
    }

    @Test
    public void emptyEmail_ReturnsFalse() throws Exception {
        assertFalse(SettingsActivity.isEmailValid(""));
    }

    @Test
    public void validPassword_ReturnsTrue() throws Exception {
        assertTrue(SettingsActivity.isPasswordValid("115pupper"));
    }

    @Test
    public void invalidPassword_ReturnsFalse() throws Exception {
        assertFalse(SettingsActivity.isPasswordValid("pupper"));
    }

    @Test
    public void emptyPassword_ReturnsFalse() throws Exception {
        assertFalse(SettingsActivity.isPasswordValid(""));
    }
}