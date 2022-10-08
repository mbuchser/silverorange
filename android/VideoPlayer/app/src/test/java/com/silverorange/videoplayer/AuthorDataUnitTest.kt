package com.silverorange.videoplayer

import com.silverorange.videoplayer.model.Author
import org.junit.Assert
import org.junit.Test

class AuthorDataUnitTest {

    var author = Author(
        "2f1fe9c0-bdbf-4104-bee2-3c0ec514436f",
        "Mel Herbert",
    )

    @Test
    fun author_correct() {
        Assert.assertEquals(author.name, "Mel Herbert")
        Assert.assertTrue(author.name.isNotEmpty())
        Assert.assertTrue(author.id.length > 20)
        Assert.assertEquals(author.id, "2f1fe9c0-bdbf-4104-bee2-3c0ec514436f")
    }

    @Test
    fun author_wrong(){
        Assert.assertNotEquals(author.name, "XYZ")
        Assert.assertFalse(author.name.isEmpty())
        Assert.assertFalse(author.id.length < 5)
        Assert.assertEquals(author.id, "2f1fe9c0-bdbf-4104-bee2-3c0ec514436f")
    }

}