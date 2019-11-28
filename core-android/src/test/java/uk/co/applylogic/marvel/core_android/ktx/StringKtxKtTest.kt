package uk.co.applylogic.marvel.core_android.ktx

import org.junit.Test;
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.assertThat

internal class StringKtxKtTest {

    @Test
    fun test_md5() {
        val TS = "1"
        val MARVEL_PRIVATE_API_KEY = "54a71dfe50e67f0ee657dee853450190fa90ffc3"
        val MARVEL_PUBLIC_API_KEY = "3d3ce5daa8ec0f7c17afc52bb68f15f7"
        val MD5_RESULT = "a45bdb0bf57b06e72ad4c2c5854e2843"

        assertThat(
            "${TS}${MARVEL_PRIVATE_API_KEY}${MARVEL_PUBLIC_API_KEY}".md5(),
            `is`(equalTo(MD5_RESULT))
        )
    }
}
