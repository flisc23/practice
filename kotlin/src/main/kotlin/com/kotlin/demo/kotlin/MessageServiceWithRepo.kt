package com.kotlin.demo.kotlin

import org.springframework.stereotype.Service
import java.util.*

@Service
class MessageServiceWithRepo(val db: MessageRepository) {
    fun findMessages(): List<Message> = db.findAll().toList()

    fun findMessageById(id: String): List<Message> = db.findById(id).toList()

    fun save(message: Message) {
        db.save(message)
    }

    fun <T : Any> Optional<out T>.toList(): List<T> =
        if (isPresent) listOf(get()) else emptyList()

//    private fun filterProjectArticles(projectArticles: List<ProjectArticle>): List<ProjectArticle> {
//        val productArticleByProductId = hashMapOf<Long, ProjectArticle>()
//        val productArticleByProductVariantId = hashMapOf<Long, ProjectArticle>() return mutableListOf<ProjectArticle>().let { targetList ->
//            val (productArticleByProductId, productArticleByProductVariantId) = projectArticles.onEach {
//                requireNotNull(it.quantityFirstYear) { "quantityFirstYear must not be null" }
//                require (it.productId != null || it.productVariantsId != null) { "no productId nor productVariantsId for article=${it.productName}" }
//            }.sortedByDescending { it.quantityFirstYear }
//                .partition { it.productId != null }
//            productArticleByProductId . groupBy { it.productId }.values.mapTo(
//                targetList
//            ) { it.first() } productArticleByProductVariantId . groupBy { it.productVariantsId }.values.mapTo(targetList) { it.first() }
//        }
    }