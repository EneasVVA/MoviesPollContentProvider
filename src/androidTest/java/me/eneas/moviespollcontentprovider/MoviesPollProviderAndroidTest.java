package me.eneas.moviespollcontentprovider;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.test.InstrumentationRegistry;
import android.test.ProviderTestCase2;

import org.junit.Test;

/**
 * Instrumentation test, which will execute on an Android device.
 *@RunWith(AndroidJUnit4.class)
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class MoviesPollProviderAndroidTest extends ProviderTestCase2<MoviesPollProvider> {

    public MoviesPollProviderAndroidTest() {
        super(MoviesPollProvider.class, MoviesPollContract.AUTHORITY);
    }

    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("me.eneas.moviespollcontentprovider", appContext.getPackageName());
    }

    @Test
    public void testInsert() {
        ContentValues values = new ContentValues();
        values.put(MoviesPollContract.Movie.COL_TITLE, "Gameover");
        values.put(MoviesPollContract.Movie.COL_YEAR, 2014);
        values.put(MoviesPollContract.Movie.COL_POPULARITY, 1.0007);

        Uri uri = getProvider().insert(MoviesPollProvider.CONTENT_URI, values);
        assertNotNull(uri);
    }

    @Test
    public void testQuery1() {
            Cursor c = getProvider().query(MoviesPollProvider.CONTENT_URI, null, null, null, null);
            assertFalse(c.moveToFirst());
            assertTrue(c.getColumnIndex(MoviesPollContract.Movie.COL_ID_MOVIE) >=0);
            c.close();

    }
}
