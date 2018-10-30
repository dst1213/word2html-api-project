package org.dark;

import org.junit.Assert;
import org.junit.Test;


public class StringUtilTest {

    @Test
    public void hasSuffix() {
        boolean r = StringUtil.hasSuffix("abc.doc", "doc");
        Assert.assertTrue(r);
    }
}