package com.bleyl.pictorial;

import com.bleyl.pictorial.utils.LinkUtil;
import com.bleyl.pictorial.utils.LinkUtil.LinkType;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;

public class LinkUtilTest {

    @Test
    public void isDirectImage() {
        assertThat(LinkUtil.getLinkType("http://example.com/12345.png"), is(LinkType.DIRECT_IMAGE));
        assertThat(LinkUtil.getLinkType("http://example.com/12345.jpg"), is(LinkType.DIRECT_IMAGE));
        assertThat(LinkUtil.getLinkType("http://example.com/12345.jpeg"), is(LinkType.DIRECT_IMAGE));
        assertThat(LinkUtil.getLinkType("http://example.com/12345.bmp"), is(LinkType.DIRECT_IMAGE));
        assertThat(LinkUtil.getLinkType("http://example.com/12345.png?12345"), is(LinkType.DIRECT_IMAGE));
        assertThat(LinkUtil.getLinkType("http://www.example.com/12345.png"), is(LinkType.DIRECT_IMAGE));
    }

    @Test
    public void isDirectGif() {
        assertThat(LinkUtil.getLinkType("http://example.com/12345.gif"), is(LinkType.DIRECT_GIF));
        assertThat(LinkUtil.getLinkType("http://example.com/12345.gif?12345"), is(LinkType.DIRECT_GIF));
        assertThat(LinkUtil.getLinkType("http://www.example.com/12345.gif"), is(LinkType.DIRECT_GIF));
    }

    @Test
    public void isImgurDirect() {
        assertThat(LinkUtil.getLinkType("http://i.imgur.com/tzlB1i0.png"), is(LinkType.IMGUR_DIRECT));
        assertThat(LinkUtil.getLinkType("http://i.imgur.com/tzlB1i0.jpg"), is(LinkType.IMGUR_DIRECT));
        assertThat(LinkUtil.getLinkType("http://i.imgur.com/tzlB1i0.jpeg"), is(LinkType.IMGUR_DIRECT));
        assertThat(LinkUtil.getLinkType("http://i.imgur.com/tzlB1i0.gif"), is(LinkType.IMGUR_DIRECT));
        assertThat(LinkUtil.getLinkType("http://i.imgur.com/tzlB1i0.gifv"), is(LinkType.IMGUR_DIRECT));
        assertThat(LinkUtil.getLinkType("http://i.imgur.com/tzlB1i0.mp4"), is(LinkType.IMGUR_DIRECT));
        assertThat(LinkUtil.getLinkType("http://i.imgur.com/tzlB1i0.webm"), is(LinkType.IMGUR_DIRECT));
        assertThat(LinkUtil.getLinkType("http://i.imgur.com/tzlB1i0.webm?12345"), is(LinkType.IMGUR_DIRECT));
        assertThat(LinkUtil.getLinkType("http://www.i.imgur.com/tzlB1i0.webm"), is(LinkType.IMGUR_DIRECT));
        assertThat(LinkUtil.getLinkType("http://m.imgur.com/tzlB1i0.png"), is(LinkType.IMGUR_DIRECT));
        assertThat(LinkUtil.getLinkType("http://www.m.imgur.com/tzlB1i0.png"), is(LinkType.IMGUR_DIRECT));
        assertThat(LinkUtil.getLinkType("http://imgur.com/tzlB1i0"), is(LinkType.IMGUR_DIRECT));
        assertThat(LinkUtil.getLinkType("http://www.imgur.com/tzlB1i0"), is(LinkType.IMGUR_DIRECT));
    }

    @Test
    public void isImgurAlbum() {
        assertThat(LinkUtil.getLinkType("http://imgur.com/a/tzlB1i0"), is(LinkType.IMGUR_ALBUM));
        assertThat(LinkUtil.getLinkType("http://www.imgur.com/a/tzlB1i0"), is(LinkType.IMGUR_ALBUM));
        assertThat(LinkUtil.getLinkType("http://imgur.com/a/tzlB1i0?gallery#1"), is(LinkType.IMGUR_ALBUM));
    }

    @Test
    public void isImgurGallery() {
        assertThat(LinkUtil.getLinkType("http://imgur.com/gallery/tzlB1i0"), is(LinkType.IMGUR_GALLERY));
        assertThat(LinkUtil.getLinkType("http://www.imgur.com/gallery/tzlB1i0"), is(LinkType.IMGUR_GALLERY));
        assertThat(LinkUtil.getLinkType("http://imgur.com/gallery/tzlB1i0?gallery#1"), is(LinkType.IMGUR_GALLERY));
    }

    @Test
    public void isGfycat() {
        assertThat(LinkUtil.getLinkType("https://gfycat.com/WillingFoolishGazelle"), is(LinkType.GFYCAT));
        assertThat(LinkUtil.getLinkType("https://www.gfycat.com/WillingFoolishGazelle"), is(LinkType.GFYCAT));
        assertThat(LinkUtil.getLinkType("https://gfycat.com/WillingFoolishGazelle#"), is(LinkType.GFYCAT));
    }

    @Test
    public void getsImgurId() {
        assertThat("tzlB1i0", is(equalTo(LinkUtil.getImgurId("http://i.imgur.com/tzlB1i0.png"))));
        assertThat("tzlB1i0", is(equalTo(LinkUtil.getImgurId("http://m.imgur.com/tzlB1i0.png"))));
        assertThat("tzlB1i0", is(equalTo(LinkUtil.getImgurId("http://imgur.com/tzlB1i0"))));
        assertThat("tzlB1i0", is(equalTo(LinkUtil.getImgurId("http://www.i.imgur.com/tzlB1i0.png"))));
        assertThat("tzlB1i0", is(equalTo(LinkUtil.getImgurId("http://www.imgur.com/tzlB1i0"))));
    }

    @Test
    public void getsImgurAlbumId() {
        assertThat("tzlB1i0", is(equalTo(LinkUtil.getImgurAlbumId("http://imgur.com/a/tzlB1i0"))));
        assertThat("tzlB1i0", is(equalTo(LinkUtil.getImgurAlbumId("http://www.imgur.com/a/tzlB1i0"))));
    }

    @Test
    public void getsImgurGalleryId() {
        assertThat("tzlB1i0", is(equalTo(LinkUtil.getImgurGalleryId("http://imgur.com/gallery/tzlB1i0"))));
        assertThat("tzlB1i0", is(equalTo(LinkUtil.getImgurGalleryId("http://www.imgur.com/gallery/tzlB1i0"))));
    }

    @Test
    public void getGfycatId() {
        assertThat("WillingFoolishGazelle", is(equalTo(LinkUtil.getGfycatId("https://gfycat.com/WillingFoolishGazelle"))));
        assertThat("WillingFoolishGazelle", is(equalTo(LinkUtil.getGfycatId("https://www.gfycat.com/WillingFoolishGazelle"))));
    }
}