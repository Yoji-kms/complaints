package ru.netology

import ru.netology.exceptions.CommentNotFoundException
import ru.netology.exceptions.CommentOwnerNotFoundException
import ru.netology.exceptions.PostNotFoundException

object WallService {
    private var posts = emptyArray<Post>()
    private var comments = emptyArray<Comment>()
    private var reports = emptyArray<Report>()

    fun clear() {
        posts = emptyArray()
        comments = emptyArray()
    }

    fun createComment(comment: Comment, postId: Int): Comment? {
        val post: Post? = findPostById(postId)

        if (post != null) {
            comments = post.comments?.comments ?: emptyArray()
            val newComment = comment.copy(id = comments.lastIndex + 1)
            comments += newComment
            update(post.copy(comments = Comments(comments = comments)))
            return newComment
        }
        return null
    }

    private fun findPostById(postId: Int): Post? {
        for (post: Post in posts) {
            if (post.id == postId) return post
        }
        throw PostNotFoundException()
    }

    fun add(post: Post): Post {
        val newPost = post.copy(id = posts.lastIndex + 1)
        posts += newPost
        return newPost
    }

    fun update(post: Post): Boolean {
        for ((index, updatingPost) in posts.withIndex()) {
            if (index == post.id) {
                posts[index] = post.copy(
                    ownerId = updatingPost.ownerId,
                    date = updatingPost.date
                )
                return true
            }
        }
        return false
    }

    fun reportComment(report: Report): Boolean {
        var ownerComments = emptyArray<Comment>()

        for (comment: Comment in comments) {
            if (comment.fromId == report.ownerId) {
                ownerComments += comment
            }
        }

        if (ownerComments.isNotEmpty()) {
            for (comment: Comment in ownerComments) {
                if (comment.id == report.commentId) {
                    reports += report
                    return true
                }
            }
            throw CommentNotFoundException()
        }
        throw CommentOwnerNotFoundException()
    }
}