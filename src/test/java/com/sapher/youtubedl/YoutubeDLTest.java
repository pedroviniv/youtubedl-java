package com.sapher.youtubedl;

import com.sapher.youtubedl.mapper.VideoFormat;
import com.sapher.youtubedl.mapper.VideoInfo;
import com.sapher.youtubedl.mapper.VideoThumbnail;
import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import org.junit.Test;
import org.junit.Assert;

import java.util.List;

public class YoutubeDLTest {

    private final static String DIRECTORY = System.getProperty("java.io.tmpdir");
    private final static String VIDEO_URL = "https://www.youtube.com/watch?v=dQw4w9WgXcQ";
    private final static String NONE_EXISTENT_VIDEO_URL = "https://www.youtube.com/watch?v=dQw4w9WgXcZ";

    public static final String EXECUTABLE_PATH = getYoutubeDLLocation();
    public static final YoutubeDL YOUTUBE_DL = new YoutubeDL(EXECUTABLE_PATH);

    private static String getYoutubeDLLocation() {
        try {
            URL res = YoutubeDLTest.class.getClassLoader()
                    .getResource("youtube-dl");

            File file = Paths.get(res.toURI()).toFile();
            String youtubeDlPath = file.getAbsolutePath();
            return youtubeDlPath;
        } catch (URISyntaxException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * @Test public void testUsingOwnExecutablePath() throws YoutubeDLException
     * { YoutubeDL.setExecutablePath("/usr/bin/youtube-dl");
     * Assert.assertNotNull(YoutubeDL.getVersion());
    }*
     */
    @Test
    public void testGetVersion() throws YoutubeDLException {
        Assert.assertNotNull(YOUTUBE_DL.getVersion());
    }

    @Test
    public void testElapsedTime() throws YoutubeDLException {

        long startTime = System.nanoTime();

        YoutubeDLRequest request = new YoutubeDLRequest();
        request.setOption("version");
        YoutubeDLResponse response = YOUTUBE_DL.execute(request);

        int elapsedTime = (int) (System.nanoTime() - startTime);

        Assert.assertTrue(elapsedTime > response.getElapsedTime());
    }

    @Test
    public void testSimulateDownload() throws YoutubeDLException {

        YoutubeDLRequest request = new YoutubeDLRequest();
        request.setUrl(VIDEO_URL);
        request.setOption("simulate");

        YoutubeDLResponse response = YOUTUBE_DL.execute(request);

        Assert.assertEquals(String.format("%s ", EXECUTABLE_PATH) + VIDEO_URL + " --simulate", response.getCommand());
    }

    @Test
    public void testDirectory() throws YoutubeDLException {

        YoutubeDLRequest request = new YoutubeDLRequest(VIDEO_URL, DIRECTORY);
        request.setOption("simulate");

        YoutubeDLResponse response = YOUTUBE_DL.execute(request);

        Assert.assertEquals(DIRECTORY, response.getDirectory());
    }

    @Test
    public void testGetVideoInfo() throws YoutubeDLException {
        VideoInfo videoInfo = YOUTUBE_DL.getVideoInfo(VIDEO_URL);
        Assert.assertNotNull(videoInfo);
    }

    @Test
    public void testGetFormats() throws YoutubeDLException {
        List<VideoFormat> formats = YOUTUBE_DL.getFormats(VIDEO_URL);
        Assert.assertNotNull(formats);
        Assert.assertTrue(formats.size() > 0);
    }

    @Test
    public void testGetThumbnails() throws YoutubeDLException {
        List<VideoThumbnail> thumbnails = YOUTUBE_DL.getThumbnails(VIDEO_URL);
        Assert.assertNotNull(thumbnails);
        Assert.assertTrue(thumbnails.size() > 0);
    }

    @Test
    public void testGetTags() throws YoutubeDLException {
        List<String> tags = YOUTUBE_DL.getTags(VIDEO_URL);
        Assert.assertNotNull(tags);
        Assert.assertTrue(tags.size() > 0);
    }

    @Test
    public void testGetCategories() throws YoutubeDLException {
        List<String> categories = YOUTUBE_DL.getCategories(VIDEO_URL);
        Assert.assertNotNull(categories);
        Assert.assertTrue(categories.size() > 0);
    }

    @Test(expected = YoutubeDLException.class)
    public void testFailGetNonExistentVideoInfo() throws YoutubeDLException {
        YOUTUBE_DL.getVideoInfo(NONE_EXISTENT_VIDEO_URL);
    }
}
