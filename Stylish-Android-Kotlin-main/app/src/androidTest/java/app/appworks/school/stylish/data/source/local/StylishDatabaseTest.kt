package app.appworks.school.stylish.data.source.local

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import app.appworks.school.stylish.data.Product
import app.appworks.school.stylish.data.ProductListResult
import app.appworks.school.stylish.ext.blockingObserve
import app.appworks.school.stylish.network.moshi
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class StylishDatabaseTest {
    private lateinit var dao: StylishDatabaseDao
    private lateinit var db: StylishDatabase
    private val rawProductList = """
        {
          "data": [
            {
              "id": 201807201824,
              "category": "women",
              "title": "前開衩扭結洋裝",
              "description": "厚薄：薄\r\n彈性：無",
              "price": 799,
              "texture": "棉 100%",
              "wash": "手洗，溫水",
              "place": "中國",
              "note": "實品顏色依單品照為主",
              "story": "O.N.S is all about options, which is why we took our staple polo shirt and upgraded it with slubby linen jersey, making it even lighter for those who prefer their summer style extra-breezy.",
              "main_image": "https://api.appworks-school.tw/assets/201807201824/main.jpg",
              "images": [
                "https://api.appworks-school.tw/assets/201807201824/0.jpg",
                "https://api.appworks-school.tw/assets/201807201824/1.jpg",
                "https://api.appworks-school.tw/assets/201807201824/0.jpg",
                "https://api.appworks-school.tw/assets/201807201824/1.jpg"
              ],
              "variants": [
                {
                  "color_code": "FFFFFF",
                  "size": "S",
                  "stock": 2
                },
                {
                  "color_code": "FFFFFF",
                  "size": "M",
                  "stock": 1
                },
                {
                  "color_code": "FFFFFF",
                  "size": "L",
                  "stock": 2
                },
                {
                  "color_code": "DDFFBB",
                  "size": "S",
                  "stock": 9
                },
                {
                  "color_code": "DDFFBB",
                  "size": "M",
                  "stock": 0
                },
                {
                  "color_code": "DDFFBB",
                  "size": "L",
                  "stock": 5
                },
                {
                  "color_code": "CCCCCC",
                  "size": "S",
                  "stock": 8
                },
                {
                  "color_code": "CCCCCC",
                  "size": "M",
                  "stock": 5
                },
                {
                  "color_code": "CCCCCC",
                  "size": "L",
                  "stock": 9
                }
              ],
              "colors": [
                {
                  "code": "FFFFFF",
                  "name": "白色"
                },
                {
                  "code": "DDFFBB",
                  "name": "亮綠"
                },
                {
                  "code": "CCCCCC",
                  "name": "淺灰"
                }
              ],
              "sizes": [
                "S",
                "M",
                "L"
              ]
            },
            {
              "id": 201807202140,
              "category": "women",
              "title": "透肌澎澎防曬襯衫",
              "description": "厚薄：薄\r\n彈性：無",
              "price": 599,
              "texture": "棉 100%",
              "wash": "手洗，溫水",
              "place": "中國",
              "note": "實品顏色依單品照為主",
              "story": "O.N.S is all about options, which is why we took our staple polo shirt and upgraded it with slubby linen jersey, making it even lighter for those who prefer their summer style extra-breezy.",
              "main_image": "https://api.appworks-school.tw/assets/201807202140/main.jpg",
              "images": [
                "https://api.appworks-school.tw/assets/201807202140/0.jpg",
                "https://api.appworks-school.tw/assets/201807202140/1.jpg",
                "https://api.appworks-school.tw/assets/201807202140/0.jpg",
                "https://api.appworks-school.tw/assets/201807202140/1.jpg"
              ],
              "variants": [
                {
                  "color_code": "DDFFBB",
                  "size": "S",
                  "stock": 7
                },
                {
                  "color_code": "DDFFBB",
                  "size": "M",
                  "stock": 5
                },
                {
                  "color_code": "DDFFBB",
                  "size": "L",
                  "stock": 8
                },
                {
                  "color_code": "CCCCCC",
                  "size": "S",
                  "stock": 1
                },
                {
                  "color_code": "CCCCCC",
                  "size": "M",
                  "stock": 6
                },
                {
                  "color_code": "CCCCCC",
                  "size": "L",
                  "stock": 2
                }
              ],
              "colors": [
                {
                  "code": "DDFFBB",
                  "name": "亮綠"
                },
                {
                  "code": "CCCCCC",
                  "name": "淺灰"
                }
              ],
              "sizes": [
                "S",
                "M",
                "L"
              ]
            },
            {
              "id": 201807202150,
              "category": "women",
              "title": "小扇紋細織上衣",
              "description": "厚薄：薄\r\n彈性：無",
              "price": 599,
              "texture": "棉 100%",
              "wash": "手洗，溫水",
              "place": "中國",
              "note": "實品顏色依單品照為主",
              "story": "O.N.S is all about options, which is why we took our staple polo shirt and upgraded it with slubby linen jersey, making it even lighter for those who prefer their summer style extra-breezy.",
              "main_image": "https://api.appworks-school.tw/assets/201807202150/main.jpg",
              "images": [
                "https://api.appworks-school.tw/assets/201807202150/0.jpg",
                "https://api.appworks-school.tw/assets/201807202150/1.jpg",
                "https://api.appworks-school.tw/assets/201807202150/0.jpg",
                "https://api.appworks-school.tw/assets/201807202150/1.jpg"
              ],
              "variants": [
                {
                  "color_code": "DDFFBB",
                  "size": "S",
                  "stock": 3
                },
                {
                  "color_code": "DDFFBB",
                  "size": "M",
                  "stock": 5
                },
                {
                  "color_code": "CCCCCC",
                  "size": "S",
                  "stock": 4
                },
                {
                  "color_code": "CCCCCC",
                  "size": "M",
                  "stock": 1
                },
                {
                  "color_code": "BB7744",
                  "size": "S",
                  "stock": 2
                },
                {
                  "color_code": "BB7744",
                  "size": "M",
                  "stock": 6
                }
              ],
              "colors": [
                {
                  "code": "DDFFBB",
                  "name": "亮綠"
                },
                {
                  "code": "CCCCCC",
                  "name": "淺灰"
                },
                {
                  "code": "BB7744",
                  "name": "淺棕"
                }
              ],
              "sizes": [
                "S",
                "M"
              ]
            },
            {
              "id": 201807202157,
              "category": "women",
              "title": "活力花紋長筒牛仔褲",
              "description": "厚薄：薄\r\n彈性：無",
              "price": 1299,
              "texture": "棉 100%",
              "wash": "手洗，溫水",
              "place": "中國",
              "note": "實品顏色依單品照為主",
              "story": "O.N.S is all about options, which is why we took our staple polo shirt and upgraded it with slubby linen jersey, making it even lighter for those who prefer their summer style extra-breezy.",
              "main_image": "https://api.appworks-school.tw/assets/201807202157/main.jpg",
              "images": [
                "https://api.appworks-school.tw/assets/201807202157/0.jpg",
                "https://api.appworks-school.tw/assets/201807202157/1.jpg",
                "https://api.appworks-school.tw/assets/201807202157/0.jpg",
                "https://api.appworks-school.tw/assets/201807202157/1.jpg"
              ],
              "variants": [
                {
                  "color_code": "DDF0FF",
                  "size": "S",
                  "stock": 8
                },
                {
                  "color_code": "DDF0FF",
                  "size": "M",
                  "stock": 5
                },
                {
                  "color_code": "DDF0FF",
                  "size": "L",
                  "stock": 6
                },
                {
                  "color_code": "CCCCCC",
                  "size": "S",
                  "stock": 0
                },
                {
                  "color_code": "CCCCCC",
                  "size": "M",
                  "stock": 6
                },
                {
                  "color_code": "CCCCCC",
                  "size": "L",
                  "stock": 5
                },
                {
                  "color_code": "334455",
                  "size": "S",
                  "stock": 2
                },
                {
                  "color_code": "334455",
                  "size": "M",
                  "stock": 7
                },
                {
                  "color_code": "334455",
                  "size": "L",
                  "stock": 9
                }
              ],
              "colors": [
                {
                  "code": "DDF0FF",
                  "name": "淺藍"
                },
                {
                  "code": "CCCCCC",
                  "name": "淺灰"
                },
                {
                  "code": "334455",
                  "name": "深藍"
                }
              ],
              "sizes": [
                "S",
                "M",
                "L"
              ]
            },
            {
              "id": 201902191210,
              "category": "women",
              "title": "精緻扭結洋裝",
              "description": "厚薄：薄\r\n彈性：無",
              "price": 999,
              "texture": "棉 100%",
              "wash": "手洗",
              "place": "越南",
              "note": "實品顏色依單品照為主",
              "story": "O.N.S is all about options, which is why we took our staple polo shirt and upgraded it with slubby linen jersey, making it even lighter for those who prefer their summer style extra-breezy.",
              "main_image": "https://api.appworks-school.tw/assets/201902191210/main.jpg",
              "images": [
                "https://api.appworks-school.tw/assets/201902191210/0.jpg",
                "https://api.appworks-school.tw/assets/201902191210/1.jpg",
                "https://api.appworks-school.tw/assets/201902191210/0.jpg",
                "https://api.appworks-school.tw/assets/201902191210/1.jpg"
              ],
              "variants": [
                {
                  "color_code": "FFFFFF",
                  "size": "S",
                  "stock": 0
                },
                {
                  "color_code": "FFFFFF",
                  "size": "M",
                  "stock": 9
                },
                {
                  "color_code": "FFDDDD",
                  "size": "S",
                  "stock": 2
                },
                {
                  "color_code": "FFDDDD",
                  "size": "M",
                  "stock": 1
                }
              ],
              "colors": [
                {
                  "code": "FFFFFF",
                  "name": "白色"
                },
                {
                  "code": "FFDDDD",
                  "name": "粉紅"
                }
              ],
              "sizes": [
                "S",
                "M"
              ]
            },
            {
              "id": 201902191242,
              "category": "women",
              "title": "透肌澎澎薄紗襯衫",
              "description": "厚薄：薄\r\n彈性：無",
              "price": 999,
              "texture": "棉 100%",
              "wash": "手洗",
              "place": "越南",
              "note": "實品顏色依單品照為主",
              "story": "O.N.S is all about options, which is why we took our staple polo shirt and upgraded it with slubby linen jersey, making it even lighter for those who prefer their summer style extra-breezy.",
              "main_image": "https://api.appworks-school.tw/assets/201902191242/main.jpg",
              "images": [
                "https://api.appworks-school.tw/assets/201902191242/0.jpg",
                "https://api.appworks-school.tw/assets/201902191242/1.jpg",
                "https://api.appworks-school.tw/assets/201902191242/0.jpg",
                "https://api.appworks-school.tw/assets/201902191242/1.jpg"
              ],
              "variants": [
                {
                  "color_code": "DDFFBB",
                  "size": "M",
                  "stock": 3
                },
                {
                  "color_code": "DDFFBB",
                  "size": "L",
                  "stock": 9
                },
                {
                  "color_code": "DDF0FF",
                  "size": "M",
                  "stock": 2
                },
                {
                  "color_code": "DDF0FF",
                  "size": "L",
                  "stock": 6
                }
              ],
              "colors": [
                {
                  "code": "DDFFBB",
                  "name": "亮綠"
                },
                {
                  "code": "DDF0FF",
                  "name": "淺藍"
                }
              ],
              "sizes": [
                "M",
                "L"
              ]
            }
          ],
          "next_paging": 1
        }
    """.trimIndent()
    private val rawProducts =
        moshi.adapter(ProductListResult::class.java).fromJson(rawProductList)?.products

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, StylishDatabase::class.java
        ).build()
        dao = db.stylishDatabaseDao
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun test_insert_and_read() {
        runBlocking {
            assert(rawProducts != null)
            assert(rawProducts!!.size == 6)
            dao.insert(rawProducts.first())
            val products = dao.getAllProducts().blockingObserve()
            assertTrue(products != null)
            assertEquals(1, products!!.size)

            //since we are using data class, just compare directly
            assertEquals(rawProducts.first(), products.first())
        }
    }

    @Test
    @Throws(Exception::class)
    fun test_insert_2_and_read() {
        runBlocking {
            assert(rawProducts != null)
            assert(rawProducts!!.size == 6)
            dao.insert(rawProducts.first())
            var products = dao.getAllProducts().blockingObserve()
            assertTrue(products != null)
            assertEquals(1, products!!.size)
            assertEquals(rawProducts.first(), products.first())
            dao.insert(rawProducts.last())
            products = dao.getAllProducts().blockingObserve()
            assertEquals(2, products!!.size)
            assertEquals(rawProducts.last(), products.last())
        }
    }

    @Test
    @Throws(Exception::class)
    fun test_update_and_read() {
        runBlocking {
            dao.insert(rawProducts!!.first())
            var products = dao.getAllProducts().blockingObserve()
            assertTrue(products != null)
            assertEquals(1, products!!.size)
            val prod = Product(
                id = products.first().id,
                title = "title",
                description = "desc",
                price = 100000,
                texture = "texture",
                wash = "wash",
                place = "place",
                note = "note",
                story = "story",
                colors = listOf(),
                sizes = listOf(),
                variants = products.first().variants,
                mainImage = "main image",
                images = listOf(),
            )
            dao.update(prod)
            products = dao.getAllProducts().blockingObserve()
            assertEquals(prod, products!!.first())
        }
    }

    @Test
    @Throws(Exception::class)
    fun test_delete_and_read() {
        runBlocking {
            //insert first and last
            dao.insert(rawProducts!!.first())
            dao.insert(rawProducts.last())
            var products = dao.getAllProducts().blockingObserve()
            assertTrue(products != null)
            assertEquals(2, products!!.size)

            //delete first
            dao.delete(
                id = products.first().id,
                colorCode = products.first().selectedVariant.colorCode,
                size = products.first().selectedVariant.size
            )
            products = dao.getAllProducts().blockingObserve()
            assertEquals(1, products!!.size)
            assertNotEquals(rawProducts.first(), products.first())
        }
    }

    @Test
    @Throws(Exception::class)
    fun test_clear_and_read() {
        runBlocking {
            //insert first and last
            dao.insert(rawProducts!!.first())
            dao.insert(rawProducts.last())
            var products = dao.getAllProducts().blockingObserve()
            assertTrue(products != null)
            assertEquals(2, products!!.size)

            //clear
            dao.clear()
            products = dao.getAllProducts().blockingObserve()
            assertEquals(0, products!!.size)
        }
    }

    @Test
    @Throws(Exception::class)
    fun test_get_single_product() {
        runBlocking {
            //insert first and last
            dao.insert(rawProducts!!.first())

            var product = dao.get(
                id = rawProducts.first().id,
                colorCode = rawProducts.first().selectedVariant.colorCode,
                size = rawProducts.first().selectedVariant.size,
            )
            assertTrue(product != null)
            assertEquals(rawProducts.first(), product)

            product = dao.get(
                id = rawProducts.last().id,
                colorCode = rawProducts.last().selectedVariant.colorCode,
                size = rawProducts.last().selectedVariant.size,
            )
            assertTrue(product == null)
        }
    }
}